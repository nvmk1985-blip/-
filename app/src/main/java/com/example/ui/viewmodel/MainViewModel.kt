package com.example.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.TutorEngine
import com.example.data.api.GeminiModelOption
import com.example.data.db.AppDatabase
import com.example.data.db.ChatMessageEntity
import com.example.data.db.PracticeStatEntity
import com.example.data.db.SavedPhraseEntity
import com.example.data.lessons.LessonCategory
import com.example.data.lessons.LessonDataSource
import com.example.data.lessons.LessonPhrase
import com.example.data.lessons.PronunciationExercise
import com.example.data.lessons.RoleplayScenario
import com.example.data.repository.AppRepository
import com.example.util.SampleTextbookPage
import com.example.util.TextbookImageHelper
import com.example.voice.SttLanguage
import com.example.voice.TtsMode
import com.example.voice.VoiceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class PracticeResult(
    val targetText: String,
    val spokenText: String,
    val score: Int,
    val feedbackTamil: String
)

data class HowToSayResult(
    val casual: String = "",
    val formal: String = "",
    val short: String = "",
    val tanglish: String = "",
    val tip: String = ""
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository
    val voiceManager: VoiceManager = VoiceManager(application)

    val savedPhrases: StateFlow<List<SavedPhraseEntity>>
    val chatHistory: StateFlow<List<ChatMessageEntity>>
    val stats: StateFlow<PracticeStatEntity?>

    private val _selectedGeminiModel = MutableStateFlow(GeminiModelOption.GEMINI_3_8_FLASH)
    val selectedGeminiModel: StateFlow<GeminiModelOption> = _selectedGeminiModel.asStateFlow()

    private val _isBotThinking = MutableStateFlow(false)
    val isBotThinking: StateFlow<Boolean> = _isBotThinking.asStateFlow()

    private val _activeScenario = MutableStateFlow<RoleplayScenario?>(null)
    val activeScenario: StateFlow<RoleplayScenario?> = _activeScenario.asStateFlow()

    // Textbook Photo / Camera State
    private val _attachedImagePath = MutableStateFlow<String?>(null)
    val attachedImagePath: StateFlow<String?> = _attachedImagePath.asStateFlow()

    private val _attachedSamplePage = MutableStateFlow<SampleTextbookPage?>(null)
    val attachedSamplePage: StateFlow<SampleTextbookPage?> = _attachedSamplePage.asStateFlow()

    // Remembering the last analyzed textbook photo in session so follow-up voice/text questions can refer to it
    private val _lastAnalyzedImagePath = MutableStateFlow<String?>(null)
    val lastAnalyzedImagePath: StateFlow<String?> = _lastAnalyzedImagePath.asStateFlow()

    private val _lastAnalyzedSamplePage = MutableStateFlow<SampleTextbookPage?>(null)

    private val _openCameraSheetOnLaunch = MutableStateFlow(false)
    val openCameraSheetOnLaunch: StateFlow<Boolean> = _openCameraSheetOnLaunch.asStateFlow()

    // Voice Call Mode
    private val _isCallActive = MutableStateFlow(false)
    val isCallActive: StateFlow<Boolean> = _isCallActive.asStateFlow()

    private val _callStatus = MutableStateFlow("Tap mic to speak with Dhanam Teacher (Gemini 3.8 Flash)")
    val callStatus: StateFlow<String> = _callStatus.asStateFlow()

    // Interactive practice
    private val _lastPracticeResult = MutableStateFlow<PracticeResult?>(null)
    val lastPracticeResult: StateFlow<PracticeResult?> = _lastPracticeResult.asStateFlow()

    // How to Say
    private val _howToSayQuery = MutableStateFlow("")
    val howToSayQuery: StateFlow<String> = _howToSayQuery.asStateFlow()

    private val _howToSayResult = MutableStateFlow<HowToSayResult?>(null)
    val howToSayResult: StateFlow<HowToSayResult?> = _howToSayResult.asStateFlow()

    private val _isTranslating = MutableStateFlow(false)
    val isTranslating: StateFlow<Boolean> = _isTranslating.asStateFlow()

    // Pronunciation Arena
    private val _selectedPronunciation = MutableStateFlow(LessonDataSource.pronunciationExercises.first())
    val selectedPronunciation: StateFlow<PronunciationExercise> = _selectedPronunciation.asStateFlow()

    // Selected Lesson Category
    private val _selectedCategory = MutableStateFlow<LessonCategory?>(null)
    val selectedCategory: StateFlow<LessonCategory?> = _selectedCategory.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = AppRepository(database.appDao())

        savedPhrases = repository.savedPhrases.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
        chatHistory = repository.chatMessages
            .map { list ->
                list.map { msg ->
                    msg.copy(
                        englishText = msg.englishText
                            .replace("I am Malar, your English voice companion", "Hi Subiksha (Subi)! I am Dhanam, your English & Tamil Teacher")
                            .replace("Malar", "Dhanam"),
                        tamilText = msg.tamilText
                            .replace("நான் மலர், உங்களின் ஆங்கில பேச்சுத் தோழி", "நான் உன் தனம் டீச்சர்! என் 9 வயது சுட்டி மாணவி சுபிக்சா (சுபி)")
                            .replace("மலர்", "தனம்"),
                        tanglishText = msg.tanglishText
                            .replace("மலர்", "தனம்")
                            .replace("Malar", "Dhanam", ignoreCase = true)
                    )
                }
            }
            .stateIn(
                viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
            )
        stats = repository.practiceStats.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), PracticeStatEntity()
        )

        setupVoiceCallbacks()
        seedInitialGreetingIfNeeded()
    }

    fun setGeminiModel(model: GeminiModelOption) {
        _selectedGeminiModel.value = model
    }

    fun requestOpenCameraSheet() {
        _openCameraSheetOnLaunch.value = true
    }

    fun consumeOpenCameraSheet() {
        _openCameraSheetOnLaunch.value = false
    }

    fun attachCapturedCameraFile(filePath: String) {
        _attachedImagePath.value = filePath
        _attachedSamplePage.value = null
    }

    fun attachGalleryUri(uri: Uri) {
        viewModelScope.launch {
            val savedPath = withContext(Dispatchers.IO) {
                TextbookImageHelper.copyUriToInternalFile(getApplication(), uri)
            }
            if (savedPath != null) {
                _attachedImagePath.value = savedPath
                _attachedSamplePage.value = null
            }
        }
    }

    fun attachSampleTextbookPage(samplePage: SampleTextbookPage, autoAnalyze: Boolean = false) {
        viewModelScope.launch {
            val savedPath = withContext(Dispatchers.IO) {
                TextbookImageHelper.generateSampleTextbookBitmap(getApplication(), samplePage)
            }
            _attachedImagePath.value = savedPath
            _attachedSamplePage.value = samplePage
            if (autoAnalyze) {
                sendTextbookPhotoQuestion(
                    question = "இப்படத்தில் உள்ள பாடத்தை முழுமையாக விளக்கிச் சொல்லுங்கள் (Explain this textbook page clearly in Tamil and English)"
                )
            }
        }
    }

    fun clearAttachedImage() {
        _attachedImagePath.value = null
        _attachedSamplePage.value = null
    }

    fun clearActiveTextbookContext() {
        _attachedImagePath.value = null
        _attachedSamplePage.value = null
        _lastAnalyzedImagePath.value = null
        _lastAnalyzedSamplePage.value = null
    }

    fun sendTextbookPhotoQuestion(
        question: String = "",
        imagePathOverride: String? = null,
        samplePageOverride: SampleTextbookPage? = null,
        autoSpeakReply: Boolean = true
    ) {
        val targetImagePath = imagePathOverride
            ?: _attachedImagePath.value
            ?: _lastAnalyzedImagePath.value
            ?: return

        val targetSamplePage = samplePageOverride
            ?: _attachedSamplePage.value
            ?: _lastAnalyzedSamplePage.value

        val effectiveQuestion = question.trim().ifBlank {
            "இப்படத்தில் உள்ள பாடத்தை விளக்கிச் சொல்லுங்கள் (Explain this textbook page step-by-step in English and Tamil)"
        }

        // Remember as active textbook image for follow-up doubts
        _lastAnalyzedImagePath.value = targetImagePath
        _lastAnalyzedSamplePage.value = targetSamplePage
        // Clear pending attachment bar once sent into chat
        _attachedImagePath.value = null
        _attachedSamplePage.value = null

        viewModelScope.launch {
            val historySnapshot = chatHistory.value
            val userMsg = ChatMessageEntity(
                sender = "user",
                englishText = "📷 $effectiveQuestion",
                imagePath = targetImagePath
            )
            repository.addChatMessage(userMsg)
            _isBotThinking.value = true

            val tutorReply = TutorEngine.getTextbookPhotoReply(
                userQuestion = effectiveQuestion,
                imagePath = targetImagePath,
                conversationHistory = historySnapshot,
                preferredModel = _selectedGeminiModel.value
            )

            val botMsg = ChatMessageEntity(
                sender = "bot",
                englishText = tutorReply.englishText,
                tamilText = tutorReply.tamilText,
                tanglishText = tutorReply.tanglishText,
                coachingTip = tutorReply.coachingTip
            )
            repository.addChatMessage(botMsg)
            _isBotThinking.value = false

            incrementWordsSpoken(effectiveQuestion.split(" ").size)

            if (autoSpeakReply) {
                voiceManager.speakBotResponse(
                    englishText = tutorReply.englishText,
                    tamilText = tutorReply.tamilText,
                    messageId = null
                )
            }
        }
    }

    fun setupVoiceCallbacks() {
        voiceManager.onSpeechResult = { text ->
            if (_isCallActive.value) {
                handleUserVoiceInputInCall(text)
            } else {
                sendUserMessage(text)
            }
        }

        voiceManager.onSpeechError = { errorMsg ->
            if (_isCallActive.value) {
                _callStatus.value = "சுபி, கவனிக்க இயலவில்லை ($errorMsg). மீண்டும் பேசு!"
            }
        }

        voiceManager.onSpeakingComplete = {
            if (_isCallActive.value) {
                _callStatus.value = "சுபியின் முறை! இப்போது எதை வேண்டுமானாலும் கேள் சுபி..."
                voiceManager.startListening(voiceManager.selectedSttLanguage.value.code)
            }
        }
    }

    fun setSttLanguage(language: SttLanguage) {
        voiceManager.setSttLanguage(language)
    }

    fun startPushToTalk(languageCode: String? = null) {
        val code = languageCode ?: voiceManager.selectedSttLanguage.value.code
        voiceManager.startListening(code)
    }

    fun stopPushToTalk() {
        voiceManager.stopListening()
    }

    fun cancelPushToTalk() {
        voiceManager.cancelListening()
    }

    private fun seedInitialGreetingIfNeeded() {
        viewModelScope.launch {
            val currentMessages = repository.chatMessages.first()
            val hasOldMalarGreeting = currentMessages.size == 1 &&
                (currentMessages.first().englishText.contains("Malar", ignoreCase = true) ||
                    currentMessages.first().tamilText.contains("மலர்"))

            if (currentMessages.isEmpty() || hasOldMalarGreeting) {
                if (hasOldMalarGreeting) {
                    repository.clearChat()
                }
                val greeting = ChatMessageEntity(
                    sender = "bot",
                    englishText = "Hello Subiksha (Subi)! I am your Dhanam Teacher powered by Gemini 3.8 Flash! Ask me anything or take a photo of your textbook (📷 Camera) to get instant explanations and clear your doubts in Tamil & English!",
                    tamilText = "வணக்கம் சுபிக்சா (சுபி)! நான் Gemini 3.8 Flash சக்தியுடன் இயங்கும் உன் தனம் டீச்சர்! எந்தக் கேள்வியும் கேட்கலாம் அல்லது உன் பாடப்புத்தகத்தை 📷 கேமராவில் போட்டோ எடுத்து அதன் விளக்கம் மற்றும் சந்தேகங்களை உடனடியாகக் கேட்டுத் தெரிந்துகொள்ளலாம்!",
                    tanglishText = "ஹலோ சுபிக்சா (சுபி)! ஐ அம் யுவர் தனம் டீச்சர் பவர்ட் பை ஜெமினி 3.8 ஃபிளாஷ்! டேக் எ போட்டோ ஆஃப் யுவர் டெக்ஸ்ட்புக் டு கிளியர் யுவர் டவுட்ஸ்!",
                    coachingTip = "📷 புத்தகப் பக்கத்தை போட்டோ எடுக்க கீழே உள்ள கேமரா பட்டனை அழுத்தவும்!"
                )
                repository.addChatMessage(greeting)
            }
        }
    }

    fun sendUserMessage(text: String, autoSpeakReply: Boolean = true) {
        // If user currently has an attached textbook photo, route the question (or empty tap) to textbook photo analysis
        if (_attachedImagePath.value != null) {
            sendTextbookPhotoQuestion(question = text, autoSpeakReply = autoSpeakReply)
            return
        }
        if (text.isBlank()) return
        viewModelScope.launch {
            val historySnapshot = chatHistory.value
            val userMsg = ChatMessageEntity(
                sender = "user",
                englishText = text
            )
            repository.addChatMessage(userMsg)
            _isBotThinking.value = true

            val tutorReply = TutorEngine.getTutorReply(
                userMessage = text,
                contextScenario = _activeScenario.value?.id,
                conversationHistory = historySnapshot,
                preferredModel = _selectedGeminiModel.value
            )
            val botMsg = ChatMessageEntity(
                sender = "bot",
                englishText = tutorReply.englishText,
                tamilText = tutorReply.tamilText,
                tanglishText = tutorReply.tanglishText,
                coachingTip = tutorReply.coachingTip
            )
            repository.addChatMessage(botMsg)
            _isBotThinking.value = false

            incrementWordsSpoken(text.split(" ").size)

            if (autoSpeakReply) {
                voiceManager.speakBotResponse(
                    englishText = tutorReply.englishText,
                    tamilText = tutorReply.tamilText,
                    messageId = null
                )
            }
        }
    }

    fun speakBotMessage(message: ChatMessageEntity, mode: TtsMode? = null) {
        voiceManager.speakBotResponse(
            englishText = message.englishText,
            tamilText = message.tamilText,
            mode = mode ?: voiceManager.ttsMode.value,
            messageId = message.id
        )
    }

    fun speakEnglish(text: String, messageId: Long? = null) {
        voiceManager.speakEnglish(text, messageId)
    }

    fun speakTamil(text: String, messageId: Long? = null) {
        voiceManager.speakTamil(text, messageId)
    }

    fun setTtsMode(mode: TtsMode) {
        voiceManager.setTtsMode(mode)
    }

    fun startScenario(scenario: RoleplayScenario) {
        _activeScenario.value = scenario
        viewModelScope.launch {
            val scenarioGreeting = ChatMessageEntity(
                sender = "bot",
                englishText = scenario.starterMessage,
                tamilText = scenario.starterTamil,
                tanglishText = "",
                coachingTip = "சூழ்நிலை: ${scenario.titleTamil}. சுபி, தனம் டீச்சருடன் உரையாடலைத் தொடங்கு!"
            )
            repository.addChatMessage(scenarioGreeting)
            voiceManager.speakBotResponse(
                englishText = scenario.starterMessage,
                tamilText = scenario.starterTamil
            )
        }
    }

    fun clearScenario() {
        _activeScenario.value = null
    }

    // Live Call Mode
    fun startCallMode() {
        _isCallActive.value = true
        _callStatus.value = "⚡ Gemini 3.8 Flash • தனம் டீச்சர் இணைப்பில் உள்ளார்... எதை வேண்டுமானாலும் கேள் சுபி!"
        val (welcomeEng, welcomeTam) = if (_activeScenario.value != null) {
            _activeScenario.value!!.starterMessage to _activeScenario.value!!.starterTamil
        } else {
            "Hello Subiksha (Subi)! Dhanam Teacher with Gemini 3.8 Flash is ready. Ask me any question in Tamil or English!" to "வணக்கம் சுபிக்சா (சுபி)! Gemini 3.8 Flash உடன் தனம் டீச்சர் தயாராக இருக்கிறேன். எந்தக் கேள்வியும் தமிழில் அல்லது ஆங்கிலத்தில் கேள் சுபி!"
        }
        voiceManager.speakBotResponse(welcomeEng, welcomeTam)
    }

    fun endCallMode() {
        _isCallActive.value = false
        voiceManager.stopSpeaking()
        voiceManager.stopListening()
        _callStatus.value = "அழைப்பு முடிந்தது."
    }

    private fun handleUserVoiceInputInCall(text: String) {
        viewModelScope.launch {
            _callStatus.value = "சுபி கேட்டது: \"$text\""
            _isBotThinking.value = true

            val historySnapshot = chatHistory.value
            val reply = TutorEngine.getTutorReply(
                userMessage = text,
                contextScenario = _activeScenario.value?.id,
                conversationHistory = historySnapshot,
                preferredModel = _selectedGeminiModel.value
            )
            val userMsg = ChatMessageEntity(sender = "user", englishText = text)
            val botMsg = ChatMessageEntity(
                sender = "bot",
                englishText = reply.englishText,
                tamilText = reply.tamilText,
                tanglishText = reply.tanglishText,
                coachingTip = reply.coachingTip
            )
            repository.addChatMessage(userMsg)
            repository.addChatMessage(botMsg)

            _isBotThinking.value = false
            _callStatus.value = "⚡ ${_selectedGeminiModel.value.displayName} பதிலளிக்கிறது..."
            voiceManager.speakBotResponse(
                englishText = reply.englishText,
                tamilText = reply.tamilText
            )
            incrementWordsSpoken(text.split(" ").size)
        }
    }

    fun evaluateSpeakingPractice(targetPhrase: String, spokenPhrase: String) {
        val score = TutorEngine.calculatePronunciationScore(targetPhrase, spokenPhrase)
        val feedback = when {
            score >= 90 -> "சூப்பர் சுபி (Subiksha)! மிகத் துல்லியமாக உச்சரித்தாய் — தனம் டீச்சரின் பாராட்டுகள்! 🌟"
            score >= 75 -> "மிக நன்று சுபி! இன்னும் ஒரு முறை கேட்டுச் சொல்லிப் பார்! 👏"
            score >= 50 -> "நல்ல முயற்சி சுபி! மெதுவாக ஒவ்வொரு வார்த்தையையும் உச்சரித்துப் பார்."
            else -> "கவலை வேண்டாம் சுபி செல்லம்! மீண்டும் ஆடியோவைக் கேட்டு மெதுவாக முயற்சி செய்."
        }
        _lastPracticeResult.value = PracticeResult(
            targetText = targetPhrase,
            spokenText = spokenPhrase,
            score = score,
            feedbackTamil = feedback
        )
        incrementWordsSpoken(spokenPhrase.split(" ").size)
    }

    fun clearPracticeResult() {
        _lastPracticeResult.value = null
    }

    fun setHowToSayQuery(query: String) {
        _howToSayQuery.value = query
    }

    fun translateHowToSay(tamilInput: String) {
        if (tamilInput.isBlank()) return
        viewModelScope.launch {
            _isTranslating.value = true
            val res = TutorEngine.translateTamilToEnglish(tamilInput, _selectedGeminiModel.value)
            _howToSayResult.value = HowToSayResult(
                casual = res["casual"].orEmpty(),
                formal = res["formal"].orEmpty(),
                short = res["short"].orEmpty(),
                tanglish = res["tanglish"].orEmpty(),
                tip = res["tip"].orEmpty()
            )
            _isTranslating.value = false
        }
    }

    fun selectPronunciationExercise(exercise: PronunciationExercise) {
        _selectedPronunciation.value = exercise
        _lastPracticeResult.value = null
    }

    fun selectCategory(category: LessonCategory?) {
        _selectedCategory.value = category
    }

    fun toggleSavePhrase(phrase: LessonPhrase) {
        viewModelScope.launch {
            val isSaved = repository.isPhraseSaved(phrase.english)
            if (isSaved) {
                val existing = savedPhrases.value.find { it.englishText == phrase.english }
                existing?.let { repository.deletePhrase(it.id) }
            } else {
                repository.savePhrase(
                    SavedPhraseEntity(
                        englishText = phrase.english,
                        tamilText = phrase.tamil,
                        tanglishText = phrase.tanglish,
                        category = _selectedCategory.value?.titleEnglish ?: "General"
                    )
                )
            }
        }
    }

    fun saveDirectPhrase(english: String, tamil: String, tanglish: String) {
        viewModelScope.launch {
            repository.savePhrase(
                SavedPhraseEntity(
                    englishText = english,
                    tamilText = tamil,
                    tanglishText = tanglish,
                    category = "Notebook"
                )
            )
        }
    }

    fun deleteSavedPhrase(id: Long) {
        viewModelScope.launch {
            repository.deletePhrase(id)
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            clearActiveTextbookContext()
            repository.clearChat()
            seedInitialGreetingIfNeeded()
        }
    }

    private fun incrementWordsSpoken(count: Int) {
        viewModelScope.launch {
            val current = stats.value ?: PracticeStatEntity()
            val updated = current.copy(
                totalWordsSpoken = current.totalWordsSpoken + count,
                lastPracticeDate = System.currentTimeMillis()
            )
            repository.updateStats(updated)
        }
    }

    override fun onCleared() {
        super.onCleared()
        voiceManager.cleanup()
    }
}
