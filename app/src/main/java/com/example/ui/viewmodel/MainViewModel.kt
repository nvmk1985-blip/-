package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.TutorEngine
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
import com.example.voice.CurrentlySpeakingLanguage
import com.example.voice.SttLanguage
import com.example.voice.TtsMode
import com.example.voice.VoiceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    private val _isBotThinking = MutableStateFlow(false)
    val isBotThinking: StateFlow<Boolean> = _isBotThinking.asStateFlow()

    private val _activeScenario = MutableStateFlow<RoleplayScenario?>(null)
    val activeScenario: StateFlow<RoleplayScenario?> = _activeScenario.asStateFlow()

    // Voice Call Mode
    private val _isCallActive = MutableStateFlow(false)
    val isCallActive: StateFlow<Boolean> = _isCallActive.asStateFlow()

    private val _callStatus = MutableStateFlow("Tap mic to speak with Dhanam Teacher")
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
                _callStatus.value = "சுபியின் முறை! இப்போது பேசு சுபி..."
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
                    englishText = "Hello Subiksha (Subi)! I am your Dhanam Teacher. You are a smart 9-year-old girl! Speak to me in Tamil or English anytime!",
                    tamilText = "வணக்கம் சுபிக்சா (சுபி)! நான் உன் தனம் டீச்சர். என் 9 வயது சுட்டி மாணவி சுபி, என்னுடன் தயங்காமல் தமிழில் அல்லது ஆங்கிலத்தில் பேசு!",
                    tanglishText = "ஹலோ சுபிக்சா (சுபி)! ஐ அம் யுவர் தனம் டீச்சர். யூ ஆர் எ ஸ்மார்ட் நைன் இயர் ஓல்ட் கேர்ள்!",
                    coachingTip = "சுபி செல்லம், தவறுகளைப் பற்றி தயங்காமல் பேசு. தனம் டீச்சருடன் தினமும் பேசப் பேச ஆங்கிலம் சுலபமாக வரும்!"
                )
                repository.addChatMessage(greeting)
            }
        }
    }

    fun sendUserMessage(text: String, autoSpeakReply: Boolean = true) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val userMsg = ChatMessageEntity(
                sender = "user",
                englishText = text
            )
            repository.addChatMessage(userMsg)
            _isBotThinking.value = true

            val tutorReply = TutorEngine.getTutorReply(text, _activeScenario.value?.id)
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
        _callStatus.value = "தனம் டீச்சர் இணைப்பில் உள்ளார்... வணக்கம் சொல்லு சுபி!"
        val (welcomeEng, welcomeTam) = if (_activeScenario.value != null) {
            _activeScenario.value!!.starterMessage to _activeScenario.value!!.starterTamil
        } else {
            "Hello Subiksha (Subi)! Dhanam Teacher is ready to talk with you. How are you today, Subi?" to "வணக்கம் சுபிக்சா (சுபி)! தனம் டீச்சர் உன்னுடன் பேசத் தயாராக இருக்கிறேன். சுபி இன்று எப்படி இருக்கிறாய்?"
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
            _callStatus.value = "சுபி பேசியது: \"$text\""
            _isBotThinking.value = true

            val reply = TutorEngine.getTutorReply(text, _activeScenario.value?.id)
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
            _callStatus.value = "தனம் டீச்சர் பதிலளிக்கிறார்..."
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
            val res = TutorEngine.translateTamilToEnglish(tamilInput)
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
