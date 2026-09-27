package com.example.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

enum class TtsMode(val label: String, val labelTamil: String) {
    BILINGUAL("English + தமிழ்", "இரு மொழிகளிலும்"),
    ENGLISH_ONLY("English Only", "ஆங்கிலம் மட்டும்"),
    TAMIL_ONLY("Tamil Only", "தமிழ் மட்டும்")
}

enum class CurrentlySpeakingLanguage {
    NONE,
    ENGLISH,
    TAMIL
}

enum class SttLanguage(val code: String, val label: String, val labelTamil: String) {
    ENGLISH("en-IN", "English (India)", "ஆங்கிலம்"),
    TAMIL("ta-IN", "Tamil (India)", "தமிழ்")
}

class VoiceManager(private val context: Context) : RecognitionListener, TextToSpeech.OnInitListener {

    private var speechRecognizer: SpeechRecognizer? = null
    private var textToSpeech: TextToSpeech? = null
    private var isTtsInitialized = false
    private val mainHandler = Handler(Looper.getMainLooper())

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _partialSpeechText = MutableStateFlow("")
    val partialSpeechText: StateFlow<String> = _partialSpeechText.asStateFlow()

    private val _selectedSttLanguage = MutableStateFlow(SttLanguage.ENGLISH)
    val selectedSttLanguage: StateFlow<SttLanguage> = _selectedSttLanguage.asStateFlow()

    private val _speechError = MutableStateFlow<String?>(null)
    val speechError: StateFlow<String?> = _speechError.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _currentlySpeakingLang = MutableStateFlow(CurrentlySpeakingLanguage.NONE)
    val currentlySpeakingLang: StateFlow<CurrentlySpeakingLanguage> = _currentlySpeakingLang.asStateFlow()

    private val _isTamilTtsAvailable = MutableStateFlow(false)
    val isTamilTtsAvailable: StateFlow<Boolean> = _isTamilTtsAvailable.asStateFlow()

    private val _isEnglishTtsAvailable = MutableStateFlow(true)
    val isEnglishTtsAvailable: StateFlow<Boolean> = _isEnglishTtsAvailable.asStateFlow()

    private val _ttsMode = MutableStateFlow(TtsMode.BILINGUAL)
    val ttsMode: StateFlow<TtsMode> = _ttsMode.asStateFlow()

    private val _speechRms = MutableStateFlow(0f)
    val speechRms: StateFlow<Float> = _speechRms.asStateFlow()

    private val _speechRate = MutableStateFlow(0.9f)
    val speechRate: StateFlow<Float> = _speechRate.asStateFlow()

    private val _currentPlayingMessageId = MutableStateFlow<Long?>(null)
    val currentPlayingMessageId: StateFlow<Long?> = _currentPlayingMessageId.asStateFlow()

    // Chained bilingual speech state
    private var pendingTamilUtterance: String? = null
    private var pendingMessageId: Long? = null

    // Scoped ephemeral STT callbacks (for single-purpose drills like practice/pronunciation)
    private var scopedOnResult: ((String) -> Unit)? = null
    private var scopedOnError: ((String) -> Unit)? = null

    var onSpeechResult: ((String) -> Unit)? = null
    var onSpeechError: ((String) -> Unit)? = null
    var onSpeakingComplete: (() -> Unit)? = null

    init {
        initializeTts()
        initializeRecognizer()
    }

    fun setSttLanguage(language: SttLanguage) {
        _selectedSttLanguage.value = language
    }

    fun clearSpeechError() {
        _speechError.value = null
    }

    private fun initializeTts() {
        textToSpeech = TextToSpeech(context.applicationContext, this)
    }

    private fun initializeRecognizer() {
        try {
            if (SpeechRecognizer.isRecognitionAvailable(context)) {
                speechRecognizer?.destroy()
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                    setRecognitionListener(this@VoiceManager)
                }
            }
        } catch (_: Exception) {}
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsInitialized = true
            configureTtsLocalesAndListener()
        }
    }

    private fun configureTtsLocalesAndListener() {
        val tts = textToSpeech ?: return

        // Check Tamil availability
        val taLocale = Locale.forLanguageTag("ta-IN")
        val taGeneric = Locale.forLanguageTag("ta")
        val taStatus = try {
            val s1 = tts.isLanguageAvailable(taLocale)
            if (s1 >= TextToSpeech.LANG_AVAILABLE) s1 else tts.isLanguageAvailable(taGeneric)
        } catch (_: Exception) {
            TextToSpeech.LANG_NOT_SUPPORTED
        }

        _isTamilTtsAvailable.value = (taStatus >= TextToSpeech.LANG_AVAILABLE)

        // Default to Indian English
        val enLocale = Locale.forLanguageTag("en-IN")
        val enStatus = try {
            tts.isLanguageAvailable(enLocale)
        } catch (_: Exception) {
            TextToSpeech.LANG_NOT_SUPPORTED
        }

        if (enStatus >= TextToSpeech.LANG_AVAILABLE) {
            tts.language = enLocale
            _isEnglishTtsAvailable.value = true
        } else {
            tts.language = Locale.US
            _isEnglishTtsAvailable.value = true
        }

        tts.setSpeechRate(_speechRate.value)

        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                mainHandler.post {
                    _isSpeaking.value = true
                    when {
                        utteranceId?.contains("_ta_") == true -> {
                            _currentlySpeakingLang.value = CurrentlySpeakingLanguage.TAMIL
                        }
                        utteranceId?.contains("_en_") == true -> {
                            _currentlySpeakingLang.value = CurrentlySpeakingLanguage.ENGLISH
                        }
                    }
                }
            }

            override fun onDone(utteranceId: String?) {
                mainHandler.post {
                    if (utteranceId?.startsWith("bilingual_en_") == true && !pendingTamilUtterance.isNullOrBlank()) {
                        // English finished, now speak the pending Tamil explanation after a natural pause
                        val tamilText = pendingTamilUtterance ?: ""
                        pendingTamilUtterance = null
                        mainHandler.postDelayed({
                            speakTamilInternal(tamilText, isChained = true, msgId = pendingMessageId)
                        }, 350)
                    } else {
                        _isSpeaking.value = false
                        _currentlySpeakingLang.value = CurrentlySpeakingLanguage.NONE
                        _currentPlayingMessageId.value = null
                        pendingTamilUtterance = null
                        pendingMessageId = null
                        onSpeakingComplete?.invoke()
                    }
                }
            }

            override fun onError(utteranceId: String?) {
                mainHandler.post {
                    _isSpeaking.value = false
                    _currentlySpeakingLang.value = CurrentlySpeakingLanguage.NONE
                    _currentPlayingMessageId.value = null
                    pendingTamilUtterance = null
                    pendingMessageId = null
                }
            }
        })
    }

    fun setSpeechRate(rate: Float) {
        _speechRate.value = rate
        textToSpeech?.setSpeechRate(rate)
    }

    fun setTtsMode(mode: TtsMode) {
        _ttsMode.value = mode
    }

    private fun cleanTextForSpeech(text: String): String {
        return text
            .replace(Regex("[\\p{So}\\p{Cn}\\p{Cs}]"), "") // remove emojis & symbols
            .replace(Regex("[*#_~`|]"), " ") // remove markdown characters
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    /**
     * Reads a full bot response according to the currently active TTS mode
     * or the explicitly provided mode.
     */
    fun speakBotResponse(
        englishText: String,
        tamilText: String,
        mode: TtsMode = _ttsMode.value,
        messageId: Long? = null
    ) {
        when (mode) {
            TtsMode.BILINGUAL -> speakBilingual(englishText, tamilText, messageId)
            TtsMode.ENGLISH_ONLY -> speakEnglish(englishText, messageId)
            TtsMode.TAMIL_ONLY -> speakTamil(tamilText, messageId)
        }
    }

    /**
     * Speaks English text first, then automatically chains and speaks the Tamil explanation.
     */
    fun speakBilingual(englishText: String, tamilText: String, messageId: Long? = null) {
        val cleanEng = cleanTextForSpeech(englishText)
        val cleanTam = cleanTextForSpeech(tamilText)

        if (cleanEng.isBlank() && cleanTam.isNotBlank()) {
            speakTamil(cleanTam, messageId)
            return
        }
        if (cleanTam.isBlank() && cleanEng.isNotBlank()) {
            speakEnglish(cleanEng, messageId)
            return
        }
        if (cleanEng.isBlank() && cleanTam.isBlank()) return

        stopListening()
        stopSpeaking()

        _currentPlayingMessageId.value = messageId
        pendingTamilUtterance = cleanTam
        pendingMessageId = messageId

        val tts = textToSpeech ?: return
        val enLocale = Locale.forLanguageTag("en-IN")
        if (tts.isLanguageAvailable(enLocale) >= TextToSpeech.LANG_AVAILABLE) {
            tts.language = enLocale
        } else {
            tts.language = Locale.US
        }
        tts.setSpeechRate(_speechRate.value)

        val params = Bundle()
        val utteranceId = "bilingual_en_${System.currentTimeMillis()}"
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        tts.speak(cleanEng, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    /**
     * Speaks text in English.
     */
    fun speakEnglish(text: String, messageId: Long? = null) {
        val clean = cleanTextForSpeech(text)
        if (clean.isBlank()) return

        stopListening()
        stopSpeaking()

        _currentPlayingMessageId.value = messageId
        val tts = textToSpeech ?: return
        val enLocale = Locale.forLanguageTag("en-IN")
        if (tts.isLanguageAvailable(enLocale) >= TextToSpeech.LANG_AVAILABLE) {
            tts.language = enLocale
        } else {
            tts.language = Locale.US
        }
        tts.setSpeechRate(_speechRate.value)

        val params = Bundle()
        val utteranceId = "single_en_${System.currentTimeMillis()}"
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        tts.speak(clean, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    /**
     * Speaks text in Tamil.
     */
    fun speakTamil(text: String, messageId: Long? = null) {
        val clean = cleanTextForSpeech(text)
        if (clean.isBlank()) return

        stopListening()
        stopSpeaking()

        speakTamilInternal(clean, isChained = false, msgId = messageId)
    }

    private fun speakTamilInternal(text: String, isChained: Boolean, msgId: Long?) {
        val tts = textToSpeech ?: return
        _currentPlayingMessageId.value = msgId

        val taLocale = Locale.forLanguageTag("ta-IN")
        val taGeneric = Locale.forLanguageTag("ta")
        val status = try {
            if (tts.isLanguageAvailable(taLocale) >= TextToSpeech.LANG_AVAILABLE) {
                tts.language = taLocale
                TextToSpeech.LANG_AVAILABLE
            } else if (tts.isLanguageAvailable(taGeneric) >= TextToSpeech.LANG_AVAILABLE) {
                tts.language = taGeneric
                TextToSpeech.LANG_AVAILABLE
            } else {
                TextToSpeech.LANG_NOT_SUPPORTED
            }
        } catch (_: Exception) {
            TextToSpeech.LANG_NOT_SUPPORTED
        }

        // If Tamil voice is missing on device, fall back to default TTS with announcement
        if (status < TextToSpeech.LANG_AVAILABLE) {
            tts.language = Locale.getDefault()
        }

        tts.setSpeechRate(_speechRate.value)

        val params = Bundle()
        val prefix = if (isChained) "bilingual_ta_" else "single_ta_"
        val utteranceId = "${prefix}${System.currentTimeMillis()}"
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    /**
     * Backwards-compatible speak function
     */
    fun speak(text: String, isTamil: Boolean = false) {
        if (isTamil) {
            speakTamil(text)
        } else {
            speakEnglish(text)
        }
    }

    fun stopSpeaking() {
        pendingTamilUtterance = null
        pendingMessageId = null
        textToSpeech?.stop()
        _isSpeaking.value = false
        _currentlySpeakingLang.value = CurrentlySpeakingLanguage.NONE
        _currentPlayingMessageId.value = null
    }

    fun openTtsSettings(context: Context) {
        try {
            val intent = Intent("com.android.settings.TTS_SETTINGS").apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            try {
                val intent = Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            } catch (_: Exception) {}
        }
    }

    fun startListening(
        languageCode: String = _selectedSttLanguage.value.code,
        onResult: ((String) -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        stopSpeaking()
        cancelListening()

        // Set or reset scoped callbacks
        scopedOnResult = onResult
        scopedOnError = onError
        _partialSpeechText.value = ""
        _speechError.value = null

        if (speechRecognizer == null) {
            initializeRecognizer()
        }
        if (speechRecognizer == null) {
            val errorMsg = "Speech recognition is not available on this device"
            _speechError.value = errorMsg
            val callback = scopedOnError ?: onSpeechError
            scopedOnResult = null
            scopedOnError = null
            callback?.invoke(errorMsg)
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, languageCode)
            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, true)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1500L)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 1500L)
        }

        try {
            speechRecognizer?.startListening(intent)
            _isListening.value = true
        } catch (e: Exception) {
            _isListening.value = false
            val errorMsg = e.localizedMessage ?: "Failed to start speech recognition"
            _speechError.value = errorMsg
            val callback = scopedOnError ?: onSpeechError
            scopedOnResult = null
            scopedOnError = null
            callback?.invoke(errorMsg)
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
        } catch (_: Exception) {}
        _isListening.value = false
        _speechRms.value = 0f
    }

    fun cancelListening() {
        try {
            speechRecognizer?.cancel()
        } catch (_: Exception) {}
        _isListening.value = false
        _speechRms.value = 0f
        _partialSpeechText.value = ""
    }

    // RecognitionListener callbacks
    override fun onReadyForSpeech(params: Bundle?) {
        _isListening.value = true
        _speechError.value = null
    }

    override fun onBeginningOfSpeech() {
        _isListening.value = true
    }

    override fun onRmsChanged(rmsdB: Float) {
        _speechRms.value = rmsdB.coerceIn(0f, 10f)
    }

    override fun onBufferReceived(buffer: ByteArray?) {}

    override fun onEndOfSpeech() {
        _isListening.value = false
    }

    override fun onError(error: Int) {
        _isListening.value = false
        _speechRms.value = 0f
        val fallbackText = _partialSpeechText.value
        _partialSpeechText.value = ""

        // If we captured partial speech and error is NO_MATCH or SPEECH_TIMEOUT,
        // we can still deliver the captured partial text!
        if (fallbackText.isNotBlank() && (error == SpeechRecognizer.ERROR_NO_MATCH || error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT)) {
            val callback = scopedOnResult ?: onSpeechResult
            scopedOnResult = null
            scopedOnError = null
            callback?.invoke(fallbackText.trim())
            return
        }

        val message = when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error (மைக் ரெக்கார்டிங் பிழை)"
            SpeechRecognizer.ERROR_CLIENT -> "Speech recognizer busy, tap to retry (மீண்டும் முயற்சிக்கவும்)"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission required (மைக் அனுமதி தேவை)"
            SpeechRecognizer.ERROR_NETWORK -> "Internet connection needed for speech (இணைய இணைப்பு தேவை)"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout (இணைய காலாவதி)"
            SpeechRecognizer.ERROR_NO_MATCH -> "Could not understand, please speak clearly (பேச்சு துல்லியமாக கேட்கவில்லை)"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Voice recognizer is busy (மீண்டும் அழுத்தவும்)"
            SpeechRecognizer.ERROR_SERVER -> "Voice server error (சர்வர் பிழை)"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech detected, please speak again (பேச்சு கேட்கவில்லை, மீண்டும் பேசவும்)"
            else -> "Speech recognition error ($error)"
        }
        _speechError.value = message
        val callback = scopedOnError ?: onSpeechError
        scopedOnResult = null
        scopedOnError = null
        callback?.invoke(message)
    }

    override fun onResults(results: Bundle?) {
        _isListening.value = false
        _speechRms.value = 0f
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val text = matches?.firstOrNull() ?: _partialSpeechText.value
        _partialSpeechText.value = ""
        if (!text.isNullOrBlank()) {
            val callback = scopedOnResult ?: onSpeechResult
            scopedOnResult = null
            scopedOnError = null
            callback?.invoke(text.trim())
        }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val text = matches?.firstOrNull()
        if (!text.isNullOrBlank()) {
            _partialSpeechText.value = text
        }
    }

    override fun onEvent(eventType: Int, params: Bundle?) {}

    fun cleanup() {
        try {
            speechRecognizer?.destroy()
            speechRecognizer = null
            textToSpeech?.stop()
            textToSpeech?.shutdown()
            textToSpeech = null
        } catch (_: Exception) {}
    }
}

