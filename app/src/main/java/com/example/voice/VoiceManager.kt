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
    BILINGUAL("Eng + தமிழ்", "இரு மொழிகளிலும்"),
    ENGLISH_ONLY("English", "ஆங்கிலம் மட்டும்"),
    TAMIL_ONLY("தமிழ்", "தமிழ் மட்டும்")
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
    private var chainedTamilRunnable: Runnable? = null
    private var speakingWatchdogRunnable: Runnable? = null
    private var pendingInitSpeakAction: (() -> Unit)? = null

    // SpeechRecognizer session management to prevent stale ERROR_CLIENT / BUSY callbacks
    private var isManuallyStoppedOrCancelled = false
    private var autoRetryCount = 0
    private var lastRequestedLanguageCode: String = SttLanguage.ENGLISH.code
    private var startListeningRunnable: Runnable? = null

    // Scoped ephemeral STT callbacks (for single-purpose drills like practice/pronunciation)
    private var scopedOnResult: ((String) -> Unit)? = null
    private var scopedOnError: ((String) -> Unit)? = null

    var onSpeechResult: ((String) -> Unit)? = null
    var onSpeechError: ((String) -> Unit)? = null
    var onSpeakingComplete: (() -> Unit)? = null

    init {
        mainHandler.post {
            initializeTts()
            initializeRecognizer()
        }
    }

    fun setSttLanguage(language: SttLanguage) {
        _selectedSttLanguage.value = language
    }

    fun clearSpeechError() {
        _speechError.value = null
    }

    private fun initializeTts() {
        try {
            textToSpeech = TextToSpeech(context.applicationContext, this)
        } catch (_: Exception) {}
    }

    private fun recreateRecognizerOnMainThread() {
        try {
            speechRecognizer?.setRecognitionListener(null)
            speechRecognizer?.cancel()
            speechRecognizer?.destroy()
        } catch (_: Exception) {}
        speechRecognizer = null

        try {
            if (SpeechRecognizer.isRecognitionAvailable(context)) {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context.applicationContext).apply {
                    setRecognitionListener(this@VoiceManager)
                }
            }
        } catch (_: Exception) {
            try {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                    setRecognitionListener(this@VoiceManager)
                }
            } catch (_: Exception) {}
        }
    }

    private fun initializeRecognizer() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            recreateRecognizerOnMainThread()
        } else {
            mainHandler.post { recreateRecognizerOnMainThread() }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsInitialized = true
            configureTtsLocalesAndListener()
            pendingInitSpeakAction?.invoke()
            pendingInitSpeakAction = null
        }
    }

    private fun scheduleSpeakingWatchdog(textLength: Int) {
        speakingWatchdogRunnable?.let { mainHandler.removeCallbacks(it) }
        val timeoutMs = (3500L + textLength * 110L).coerceIn(4500L, 18000L)
        val runnable = Runnable {
            if (_isSpeaking.value) {
                _isSpeaking.value = false
                _currentlySpeakingLang.value = CurrentlySpeakingLanguage.NONE
                _currentPlayingMessageId.value = null
                pendingTamilUtterance = null
                pendingMessageId = null
            }
        }
        speakingWatchdogRunnable = runnable
        mainHandler.postDelayed(runnable, timeoutMs)
    }

    private fun cancelSpeakingWatchdog() {
        speakingWatchdogRunnable?.let { mainHandler.removeCallbacks(it) }
        speakingWatchdogRunnable = null
    }

    private fun configureTtsLocalesAndListener() {
        val tts = textToSpeech ?: return

        val taLocale = Locale.forLanguageTag("ta-IN")
        val taGeneric = Locale.forLanguageTag("ta")
        val taStatus = try {
            val s1 = tts.isLanguageAvailable(taLocale)
            if (s1 >= TextToSpeech.LANG_AVAILABLE) s1 else tts.isLanguageAvailable(taGeneric)
        } catch (_: Exception) {
            TextToSpeech.LANG_NOT_SUPPORTED
        }

        _isTamilTtsAvailable.value = (taStatus >= TextToSpeech.LANG_AVAILABLE)

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
                    cancelSpeakingWatchdog()
                    val tamilText = pendingTamilUtterance
                    if (utteranceId?.startsWith("bilingual_en_") == true && !tamilText.isNullOrBlank() && _isTamilTtsAvailable.value) {
                        pendingTamilUtterance = null
                        val runnable = Runnable {
                            speakTamilInternal(tamilText, isChained = true, msgId = pendingMessageId)
                        }
                        chainedTamilRunnable = runnable
                        mainHandler.postDelayed(runnable, 280)
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

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                handleTtsError()
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                handleTtsError()
            }

            private fun handleTtsError() {
                mainHandler.post {
                    cancelSpeakingWatchdog()
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
            .replace(Regex("[\\p{So}\\p{Cn}\\p{Cs}]"), "")
            .replace(Regex("[*#_~`|]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    fun speakBotResponse(
        englishText: String,
        tamilText: String,
        mode: TtsMode = _ttsMode.value,
        messageId: Long? = null
    ) {
        if (!isTtsInitialized) {
            pendingInitSpeakAction = {
                speakBotResponse(englishText, tamilText, mode, messageId)
            }
            return
        }
        when (mode) {
            TtsMode.BILINGUAL -> speakBilingual(englishText, tamilText, messageId)
            TtsMode.ENGLISH_ONLY -> speakEnglish(englishText, messageId)
            TtsMode.TAMIL_ONLY -> speakTamil(tamilText, messageId)
        }
    }

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

        cancelListening()
        stopSpeaking()

        _currentPlayingMessageId.value = messageId
        pendingTamilUtterance = if (_isTamilTtsAvailable.value) cleanTam else null
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
        _isSpeaking.value = true
        _currentlySpeakingLang.value = CurrentlySpeakingLanguage.ENGLISH
        scheduleSpeakingWatchdog(cleanEng.length)

        val result = tts.speak(cleanEng, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
        if (result == TextToSpeech.ERROR) {
            stopSpeaking()
        }
    }

    fun speakEnglish(text: String, messageId: Long? = null) {
        val clean = cleanTextForSpeech(text)
        if (clean.isBlank()) return

        cancelListening()
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
        _isSpeaking.value = true
        _currentlySpeakingLang.value = CurrentlySpeakingLanguage.ENGLISH
        scheduleSpeakingWatchdog(clean.length)

        val result = tts.speak(clean, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
        if (result == TextToSpeech.ERROR) {
            stopSpeaking()
        }
    }

    fun speakTamil(text: String, messageId: Long? = null) {
        val clean = cleanTextForSpeech(text)
        if (clean.isBlank()) return

        cancelListening()
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

        if (status < TextToSpeech.LANG_AVAILABLE) {
            // Tamil TTS voice pack not installed on device; finish gracefully instead of hanging
            _isSpeaking.value = false
            _currentlySpeakingLang.value = CurrentlySpeakingLanguage.NONE
            _currentPlayingMessageId.value = null
            if (isChained) {
                onSpeakingComplete?.invoke()
            }
            return
        }

        tts.setSpeechRate(_speechRate.value)

        val params = Bundle()
        val prefix = if (isChained) "bilingual_ta_" else "single_ta_"
        val utteranceId = "${prefix}${System.currentTimeMillis()}"
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        _isSpeaking.value = true
        _currentlySpeakingLang.value = CurrentlySpeakingLanguage.TAMIL
        scheduleSpeakingWatchdog(text.length)

        val result = tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
        if (result == TextToSpeech.ERROR) {
            stopSpeaking()
        }
    }

    fun speak(text: String, isTamil: Boolean = false) {
        if (isTamil) {
            speakTamil(text)
        } else {
            speakEnglish(text)
        }
    }

    fun stopSpeaking() {
        chainedTamilRunnable?.let { mainHandler.removeCallbacks(it) }
        chainedTamilRunnable = null
        cancelSpeakingWatchdog()
        pendingTamilUtterance = null
        pendingMessageId = null
        try {
            textToSpeech?.stop()
        } catch (_: Exception) {}
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
        autoRetryCount = 0
        lastRequestedLanguageCode = languageCode
        scopedOnResult = onResult
        scopedOnError = onError
        startListeningInternal(languageCode, delayMs = 0L)
    }

    private fun startListeningInternal(languageCode: String, delayMs: Long) {
        startListeningRunnable?.let { mainHandler.removeCallbacks(it) }

        mainHandler.post {
            stopSpeaking()
            isManuallyStoppedOrCancelled = true
            try {
                speechRecognizer?.setRecognitionListener(null)
                speechRecognizer?.cancel()
                speechRecognizer?.destroy()
            } catch (_: Exception) {}
            speechRecognizer = null

            _partialSpeechText.value = ""
            _speechError.value = null
            _isListening.value = true

            val runnable = Runnable {
                isManuallyStoppedOrCancelled = false
                recreateRecognizerOnMainThread()

                val recognizer = speechRecognizer
                if (recognizer == null) {
                    _isListening.value = false
                    val errorMsg = "Google Voice Typing / Speech service not enabled on this phone"
                    _speechError.value = errorMsg
                    val callback = scopedOnError ?: onSpeechError
                    scopedOnResult = null
                    scopedOnError = null
                    callback?.invoke(errorMsg)
                    return@Runnable
                }

                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, languageCode)
                    putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
                    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                    putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1800L)
                    putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 1500L)
                    putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 1200L)
                }

                try {
                    recognizer.startListening(intent)
                    _isListening.value = true
                } catch (e: Exception) {
                    _isListening.value = false
                    val errorMsg = e.localizedMessage ?: "Failed to start microphone"
                    _speechError.value = errorMsg
                    val callback = scopedOnError ?: onSpeechError
                    scopedOnResult = null
                    scopedOnError = null
                    callback?.invoke(errorMsg)
                }
            }

            startListeningRunnable = runnable
            if (delayMs > 0L) {
                mainHandler.postDelayed(runnable, delayMs)
            } else {
                mainHandler.postDelayed(runnable, 80L)
            }
        }
    }

    /**
     * Handles speech result coming from either SpeechRecognizer or the System Voice Dialog fallback.
     */
    fun deliverExternalSpeechResult(spokenText: String) {
        val clean = spokenText.trim()
        if (clean.isBlank()) return
        _isListening.value = false
        _speechRms.value = 0f
        _partialSpeechText.value = ""
        _speechError.value = null
        val callback = scopedOnResult ?: onSpeechResult
        scopedOnResult = null
        scopedOnError = null
        callback?.invoke(clean)
    }

    fun stopListening() {
        startListeningRunnable?.let { mainHandler.removeCallbacks(it) }
        mainHandler.post {
            val capturedPartial = _partialSpeechText.value.trim()
            isManuallyStoppedOrCancelled = true
            try {
                speechRecognizer?.stopListening()
            } catch (_: Exception) {}
            _isListening.value = false
            _speechRms.value = 0f

            // If user already spoke partial text and tapped stop, deliver it immediately if onResults doesn't fire within 400ms
            if (capturedPartial.isNotBlank()) {
                mainHandler.postDelayed({
                    if (_partialSpeechText.value.isNotBlank()) {
                        val finalFallback = _partialSpeechText.value.trim()
                        _partialSpeechText.value = ""
                        val callback = scopedOnResult ?: onSpeechResult
                        scopedOnResult = null
                        scopedOnError = null
                        callback?.invoke(finalFallback)
                    }
                }, 400L)
            }
        }
    }

    fun cancelListening() {
        startListeningRunnable?.let { mainHandler.removeCallbacks(it) }
        mainHandler.post {
            isManuallyStoppedOrCancelled = true
            try {
                speechRecognizer?.setRecognitionListener(null)
                speechRecognizer?.cancel()
                speechRecognizer?.destroy()
            } catch (_: Exception) {}
            speechRecognizer = null
            _isListening.value = false
            _speechRms.value = 0f
            _partialSpeechText.value = ""
        }
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
        _speechRms.value = 0f
    }

    override fun onError(error: Int) {
        val fallbackText = _partialSpeechText.value.trim()
        _partialSpeechText.value = ""
        _speechRms.value = 0f

        // If we already captured partial speech, use it as a valid result!
        if (fallbackText.isNotBlank()) {
            _isListening.value = false
            _speechError.value = null
            val callback = scopedOnResult ?: onSpeechResult
            scopedOnResult = null
            scopedOnError = null
            callback?.invoke(fallbackText)
            return
        }

        // Ignore spurious ERROR_CLIENT if the user manually stopped/cancelled
        if (isManuallyStoppedOrCancelled && error == SpeechRecognizer.ERROR_CLIENT) {
            _isListening.value = false
            return
        }

        // Automatic self-healing retry for transient BUSY or CLIENT errors on real devices
        if ((error == SpeechRecognizer.ERROR_RECOGNIZER_BUSY || error == SpeechRecognizer.ERROR_CLIENT) && autoRetryCount < 2) {
            autoRetryCount++
            startListeningInternal(lastRequestedLanguageCode, delayMs = 250L)
            return
        }

        _isListening.value = false

        val message = when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "மைக் ஆடியோ பிழை — மீண்டும் மைக் பட்டனை அழுத்திப் பேசவும்"
            SpeechRecognizer.ERROR_CLIENT -> "மைக் தயாராகிறது — மீண்டும் ஒருமுறை தொட்டுப் பேசவும் (Tap Mic to speak)"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "மைக் அனுமதி (Microphone Permission) தேவை"
            SpeechRecognizer.ERROR_NETWORK -> "இணைய இணைப்பு (Internet) தேவை"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "இணைய காலாவதி — மீண்டும் முயற்சிக்கவும்"
            SpeechRecognizer.ERROR_NO_MATCH -> "பேச்சு தெளிவாகக் கேட்கவில்லை — மைக் பட்டனை தொட்டு சத்தமாகப் பேசவும்"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "மைக் தயாராகிறது — மீண்டும் ஒருமுறை தொட்டுப் பேசவும்"
            SpeechRecognizer.ERROR_SERVER -> "வாய்ஸ் சர்வர் பிழை — மீண்டும் பேசவும்"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "பேச்சு கேட்கவில்லை — மைக் பட்டனை தொட்டு உடனே பேசவும்"
            else -> "குரல் பதிவு பிழை ($error) — மீண்டும் முயற்சிக்கவும்"
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
        _speechError.value = null
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
        startListeningRunnable?.let { mainHandler.removeCallbacks(it) }
        chainedTamilRunnable?.let { mainHandler.removeCallbacks(it) }
        cancelSpeakingWatchdog()
        try {
            speechRecognizer?.setRecognitionListener(null)
            speechRecognizer?.destroy()
            speechRecognizer = null
            textToSpeech?.stop()
            textToSpeech?.shutdown()
            textToSpeech = null
        } catch (_: Exception) {}
    }
}
