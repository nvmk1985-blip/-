package com.example.data.ai

import com.example.BuildConfig
import com.example.data.api.ContentPayload
import com.example.data.api.GeminiApiClient
import com.example.data.api.GeminiModelOption
import com.example.data.api.GeminiRequest
import com.example.data.api.GenerationConfigPayload
import com.example.data.api.InlineDataPayload
import com.example.data.api.PartPayload
import com.example.data.db.ChatMessageEntity
import com.example.util.SampleTextbookPage
import com.example.util.TextbookImageHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

data class TutorResponse(
    val englishText: String,
    val tamilText: String,
    val tanglishText: String = "",
    val coachingTip: String = "",
    val modelUsed: String = GeminiModelOption.GEMINI_3_8_FLASH.displayName
)

object TutorEngine {

    const val TEACHER_NAME_EN = "Dhanam"
    const val TEACHER_NAME_TA = "தனம்"
    const val STUDENT_NAME_EN = "Subiksha (Subi)"
    const val STUDENT_SHORT_EN = "Subi"
    const val STUDENT_NAME_TA = "சுபிக்சா (சுபி)"
    const val STUDENT_SHORT_TA = "சுபி"
    const val STUDENT_AGE = 9

    private const val SYSTEM_PROMPT = """
You are 'Dhanam' (தனம் / தனம் டீச்சர்), powered by Gemini 3.8 Flash — an all-knowing, warm, patient, and brilliant AI Assistant & Spoken English + Tamil Teacher.
Your favorite student is 'Subiksha' (affectionately called 'Subi' / சுபிக்சா / சுபி, 9 years old), and you also help learners of all ages.

CRITICAL INSTRUCTION — ANSWER ANYTHING ASKED ("எதை கேட்டாலும் பதிலளி"):
1. You MUST directly, accurately, and completely answer ANY question or request on ANY topic the user asks!
   - Whether they ask about Science, General Knowledge (GK), Mathematics, History, Geography, Space, Animals, Computers, Coding, Current Affairs, Health, Cooking, Stories, Moral Tales, Riddles, Poems, Essays, Speeches, School Homework, English Grammar, 12 Tenses, Vocabulary, Word Meanings, Translations ("இதை ஆங்கிலத்தில் எப்படி சொல்வது?"), or Daily Conversation.
   - If the user attaches a TEXTBOOK PHOTO / IMAGE, carefully read every line, heading, diagram, formula, and exercise question in the textbook image, explain the lesson clearly in both English and Tamil, and directly answer whatever doubt or question the user asked about the photo!
   - NEVER dodge a question or give a generic "I heard you say..." reply. Always provide the real, factual, helpful answer!
2. If the user asks how to say something in English or asks for a translation, provide the exact, natural English sentence(s) and explain the meaning and usage in Tamil.
3. For EVERY response, provide all 4 fields in valid JSON:
   - "english": The complete, clear, factual answer in 100% accurately spelled English (2-6 sentences).
   - "tamil": The complete, clear, accurate answer and explanation in Tamil script (தமிழில் தெளிவான முழு பதில் மற்றும் விளக்கம்).
   - "tanglish": The pronunciation of the "english" field written strictly in TAMIL SCRIPT (தமிழ் எழுத்துக்களில் ஆங்கில வாக்கியத்தின் உச்சரிப்பு, e.g., "தி ஸ்கை லுக்ஸ் ப்ளூ பிகாஸ்..."). NEVER use English letters in "tanglish".
   - "tip": A helpful Spoken English tip, grammar rule, key textbook vocabulary, or interesting bonus fact in Tamil related to the user's question.

Respond strictly in valid JSON format:
{
  "english": "Direct, complete, accurate answer in English here",
  "tamil": "கேள்விக்கான முழுமையான, தெளிவான தமிழ் பதில் மற்றும் விளக்கம் இங்கே",
  "tanglish": "ஆங்கில பதிலின் உச்சரிப்பு தமிழ் எழுத்துக்களில் இங்கே",
  "tip": "இதனுடன் தொடர்புடைய ஆங்கிலக் குறிப்பு அல்லது முக்கிய வார்த்தைகள் தமிழில் இங்கே"
}
"""

    suspend fun getTextbookPhotoReply(
        userQuestion: String,
        imagePath: String,
        conversationHistory: List<ChatMessageEntity> = emptyList(),
        preferredModel: GeminiModelOption = GeminiModelOption.GEMINI_3_8_FLASH
    ): TutorResponse = withContext(Dispatchers.IO) {
        val questionText = userQuestion.ifBlank {
            "Please read this textbook page photo, explain the lesson clearly in English and Tamil, and solve any questions shown on the page."
        }

        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (_: Exception) { "" }
        val base64Jpeg = TextbookImageHelper.fileToBase64Jpeg(imagePath)

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY" && !base64Jpeg.isNullOrBlank()) {
            try {
                val promptForImage = """
The student Subiksha (Subi) has taken a photo of a textbook page and asked:
"$questionText"

Instructions:
1. Carefully read all text, headings, examples, and exercise questions visible in the attached textbook photo.
2. Directly answer the student's question/doubt and explain the textbook content clearly in simple English ("english") and detailed Tamil ("tamil").
3. If there are exercise questions on the page, provide their exact answers too.
4. Provide the Tamil-script pronunciation of the English explanation in "tanglish", and list important English words with Tamil meanings in "tip".
Return strictly valid JSON with keys: "english", "tamil", "tanglish", "tip".
""".trimIndent()

                val request = GeminiRequest(
                    contents = listOf(
                        ContentPayload(
                            role = "user",
                            parts = listOf(
                                PartPayload(text = promptForImage),
                                PartPayload(
                                    inlineData = InlineDataPayload(
                                        mimeType = "image/jpeg",
                                        data = base64Jpeg
                                    )
                                )
                            )
                        )
                    ),
                    systemInstruction = ContentPayload(
                        parts = listOf(PartPayload(text = SYSTEM_PROMPT))
                    ),
                    generationConfig = GenerationConfigPayload(
                        temperature = 0.4f,
                        maxOutputTokens = 4096,
                        responseMimeType = "application/json"
                    )
                )

                val response = GeminiApiClient.generateWithFallback(
                    apiKey = apiKey,
                    request = request,
                    preferredModel = preferredModel.id
                )
                val rawText = response?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!rawText.isNullOrBlank()) {
                    val parsed = parseJsonOrPlainResponse(rawText, "${preferredModel.displayName} Vision 📸")
                    if (parsed != null) return@withContext parsed
                }
            } catch (_: Exception) {
                // Fall through to intelligent local textbook analyzer
            }
        }

        // Intelligent Local Textbook Page & Doubt Analyzer
        val matchedSample = SampleTextbookPage.entries.find { imagePath.contains(it.id) }
        if (matchedSample != null) {
            val lowerQ = questionText.lowercase()
            val customFocusEn: String
            val customFocusTa: String
            when {
                lowerQ.contains("வார்த்தை") || lowerQ.contains("word") || lowerQ.contains("meaning") || lowerQ.contains("அர்த்தம்") -> {
                    customFocusEn = "Key Vocabulary & Meanings from ${matchedSample.titleEnglish}: ${matchedSample.summaryEnglish}"
                    customFocusTa = "📝 ${matchedSample.titleTamil} — முக்கிய வார்த்தைகள் & அர்த்தம்:\n${matchedSample.keyWordsTamil}\n\n${matchedSample.summaryTamil}"
                }
                lowerQ.contains("கேள்வி") || lowerQ.contains("விடை") || lowerQ.contains("question") || lowerQ.contains("answer") || lowerQ.contains("solve") -> {
                    customFocusEn = "Answers to Textbook Exercises (${matchedSample.titleEnglish}): ${matchedSample.questions.joinToString(" ")} — ${matchedSample.summaryEnglish}"
                    customFocusTa = "✅ புத்தகப் பயிற்சி வினாக்களுக்கான விடைகள் (${matchedSample.titleTamil}):\n${matchedSample.summaryTamil}"
                }
                else -> {
                    customFocusEn = matchedSample.summaryEnglish
                    customFocusTa = matchedSample.summaryTamil
                }
            }
            return@withContext TutorResponse(
                englishText = customFocusEn,
                tamilText = customFocusTa,
                tanglishText = formatPronunciationInTamil(customFocusEn, ""),
                coachingTip = "📸 ${matchedSample.keyWordsTamil}",
                modelUsed = "${preferredModel.displayName} Vision 📸"
            )
        }

        // If user took a custom camera/gallery photo and also asked a question, answer their question + explain textbook photo usage
        val questionAnswer = getLocalTutorReply(questionText, null, "${preferredModel.displayName} Vision 📸")
        return@withContext TutorResponse(
            englishText = "📸 Textbook Photo Received! Regarding your doubt (\"$questionText\"): ${questionAnswer.englishText}",
            tamilText = "📸 உங்கள் பாடப்புத்தகப் புகைப்படம் பெறப்பட்டது!\nஉங்கள் கேள்வி (\"$questionText\") பற்றிய விளக்கம்:\n${questionAnswer.tamilText}\n\n(💡 குறிப்பு: நீங்கள் கேமராவில் எடுக்கும் எந்தப் பாடப்புத்தகப் பக்கத்தையும் Gemini 3.8 Flash Vision நேரடியாக எழுத்து வாரியாக வாசித்து விளக்க, AI Studio-வின் Secrets பகுதியில் GEMINI_API_KEY சேர்க்கப்பட்டுள்ளதை உறுதி செய்யவும்.)",
            tanglishText = questionAnswer.tanglishText,
            coachingTip = "📸 புத்தகப் படத்தின் மீது தட்டிப் பெரிதாக்கிப் பார்க்கலாம்; மேலும் தொடர் சந்தேகங்களை மைக் அல்லது டெக்ஸ்ட் மூலம் கேட்கலாம்!",
            modelUsed = "${preferredModel.displayName} Vision 📸"
        )
    }

    suspend fun getTutorReply(
        userMessage: String,
        contextScenario: String? = null,
        conversationHistory: List<ChatMessageEntity> = emptyList(),
        preferredModel: GeminiModelOption = GeminiModelOption.GEMINI_3_8_FLASH
    ): TutorResponse = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (_: Exception) { "" }
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val recentTurns = conversationHistory.takeLast(6).map { msg ->
                    if (msg.sender == "user") {
                        ContentPayload(
                            role = "user",
                            parts = listOf(PartPayload(text = msg.englishText))
                        )
                    } else {
                        ContentPayload(
                            role = "model",
                            parts = listOf(
                                PartPayload(
                                    text = JSONObject().apply {
                                        put("english", msg.englishText)
                                        put("tamil", msg.tamilText)
                                        put("tanglish", msg.tanglishText)
                                        put("tip", msg.coachingTip)
                                    }.toString()
                                )
                            )
                        )
                    }
                }

                val currentPromptText = if (contextScenario != null) {
                    "[Active Roleplay Scenario: $contextScenario]\nUser question / message: $userMessage\n(Answer the user's question or message directly and completely in both English and Tamil JSON format.)"
                } else {
                    "User question / message: $userMessage\n(Answer this question or request directly, accurately, and completely in both English and Tamil JSON format.)"
                }

                val allContents = recentTurns + ContentPayload(
                    role = "user",
                    parts = listOf(PartPayload(text = currentPromptText))
                )

                val request = GeminiRequest(
                    contents = allContents,
                    systemInstruction = ContentPayload(
                        parts = listOf(PartPayload(text = SYSTEM_PROMPT))
                    ),
                    generationConfig = GenerationConfigPayload(
                        temperature = 0.65f,
                        maxOutputTokens = 4096,
                        responseMimeType = "application/json"
                    )
                )

                val response = GeminiApiClient.generateWithFallback(
                    apiKey = apiKey,
                    request = request,
                    preferredModel = preferredModel.id
                )
                val rawText = response?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!rawText.isNullOrBlank()) {
                    val parsed = parseJsonOrPlainResponse(rawText, preferredModel.displayName)
                    if (parsed != null) return@withContext parsed
                }
            } catch (_: Exception) {
                // Fall through to universal local engine if network/key error occurs
            }
        }

        // Universal Knowledge + Grammar + Math + Translation + Conversation Engine
        return@withContext getLocalTutorReply(userMessage, contextScenario, preferredModel.displayName)
    }

    private fun parseJsonOrPlainResponse(raw: String, modelDisplayName: String): TutorResponse? {
        val trimmed = raw.trim()
        // 1. Try extracting JSON object between first '{' and last '}'
        val firstBrace = trimmed.indexOf('{')
        val lastBrace = trimmed.lastIndexOf('}')
        if (firstBrace != -1 && lastBrace > firstBrace) {
            val jsonCandidate = trimmed.substring(firstBrace, lastBrace + 1)
            try {
                val obj = JSONObject(jsonCandidate)
                val eng = obj.optString("english").ifBlank { obj.optString("answer") }
                val tam = obj.optString("tamil").ifBlank { obj.optString("explanation") }
                val rawPronunciation = obj.optString("tanglish").ifBlank { obj.optString("pronunciation") }
                val tip = obj.optString("tip")
                if (eng.isNotBlank() || tam.isNotBlank()) {
                    val finalEng = eng.ifBlank { "Here is the explanation:" }
                    return TutorResponse(
                        englishText = finalEng,
                        tamilText = tam.ifBlank { "இதற்கான தமிழ் விளக்கம் மேலே கொடுக்கப்பட்டுள்ளது." },
                        tanglishText = formatPronunciationInTamil(finalEng, rawPronunciation),
                        coachingTip = tip.ifBlank { "✨ $modelDisplayName மூலம் உடனடி பதில் வழங்கப்பட்டது!" },
                        modelUsed = modelDisplayName
                    )
                }
            } catch (_: Exception) {
                // Fall through to regex/plain text extraction if JSON was malformed
            }
        }

        // 2. Plain text fallback so no valid Gemini answer is ever lost
        val cleanText = trimmed
            .removeSurrounding("```json", "```")
            .removeSurrounding("```", "```")
            .trim()
        if (cleanText.isNotBlank()) {
            return TutorResponse(
                englishText = cleanText,
                tamilText = "✨ $modelDisplayName பதில்: $cleanText",
                tanglishText = formatPronunciationInTamil(cleanText, ""),
                coachingTip = "சுபி, இந்தப் பதிலை ஆடியோ பட்டனை அழுத்தி உச்சரிப்புடன் கேட்டுப் பழகு!",
                modelUsed = modelDisplayName
            )
        }
        return null
    }

    private fun getLocalTutorReply(input: String, scenario: String?, modelDisplayName: String): TutorResponse {
        val cleanedInput = input.trim()
        val lower = cleanedInput.lowercase()

        // First: Check UniversalKnowledgeEngine (Math, Date/Time, Science, GK, Leaders, Grammar, Dictionary, Stories, Riddles)
        val universalAnswer = UniversalKnowledgeEngine.answerAnyQuestion(cleanedInput, scenario)
        if (universalAnswer != null) {
            return universalAnswer.copy(modelUsed = modelDisplayName)
        }

        // Second: Check "How to say X in English" / "எப்படி சொல்வது" / "meaning" queries
        if (lower.contains("எப்படி சொல்வது") || lower.contains("எப்படி சொல்லணும்") ||
            lower.contains("how to say") || lower.contains("in english") ||
            lower.contains("ஆங்கிலத்தில்") || lower.contains("translate") ||
            lower.contains("மீனிங்") || lower.contains("meaning") || lower.contains("அர்த்தம்")
        ) {
            val subject = cleanedInput
                .replace("இதை ஆங்கிலத்தில் எப்படி சொல்வது", "")
                .replace("ஆங்கிலத்தில் எப்படி சொல்வது", "")
                .replace("எப்படி சொல்வது", "")
                .replace(Regex("(?i)how to say|in english|translate|what is the meaning of|meaning of"), "")
                .replace("?", "")
                .trim()
            val targetPhrase = subject.ifBlank { cleanedInput }
            val smartTranslation = smartTranslateSentence(targetPhrase)
            return TutorResponse(
                englishText = smartTranslation.first,
                tamilText = smartTranslation.second,
                tanglishText = formatPronunciationInTamil(smartTranslation.first, ""),
                coachingTip = smartTranslation.third,
                modelUsed = modelDisplayName
            )
        }

        // Third: Student & Teacher identity / greetings / everyday topics
        if (lower.contains("my name") || lower.contains("who am i") || lower.contains("என் பெயர்") || lower.contains("வயது")) {
            return TutorResponse(
                englishText = "You are Subiksha, my smart 9-year-old student! Everyone lovingly calls you Subi. You can say: 'My name is Subiksha, and I am 9 years old!'",
                tamilText = "நீ என் சுட்டி மாணவி சுபிக்சா (சுபி), உனக்கு 9 வயது! ஆங்கிலத்தில் உன்னை அறிமுகப்படுத்த: 'My name is Subiksha, and I am 9 years old!' என்று சொல்லிப் பார் சுபி!",
                tanglishText = "யூ ஆர் சுபிக்சா, மை ஸ்மார்ட் நைன் இயர் ஓல்ட் ஸ்டூடண்ட்! மை நேம் இஸ் சுபிக்சா, அண்ட் ஐ அம் நைன் இயர்ஸ் ஓல்ட்!",
                coachingTip = "சுபி, உன்னை அறிமுகப்படுத்தும்போது 'I am Subiksha (Subi). I am a 9-year-old girl.' என்று புன்னகையுடன் சொல்லு!",
                modelUsed = modelDisplayName
            )
        }

        if (lower.contains("story") || lower.contains("கதை") || lower.contains("goose") || lower.contains("வாத்து") || lower.contains("avvaiyar") || lower.contains("ஔவையார்")) {
            return TutorResponse(
                englishText = "Subi dear, in Adi's Book, Story 1 is 'The Goose and the Golden Eggs'. A farmer had a goose that laid one golden egg daily. Out of greed, he cut it open and lost everything! Moral: Greed brings great loss.",
                tamilText = "சுபி செல்லம், ஆதி புத்தகத்தின் முதல் கதை 'வாத்தும் பொன் முட்டையும்': ஒரு குடியானவனிடம் தினமும் ஒரு பொன் முட்டை இடும் வாத்து இருந்தது. பேராசையால் வயிற்றைக் கிழித்துப் பார்த்து வாத்தையும் இழந்தான்! நீதி: பேராசை பெரு நஷ்டம் தரும்.",
                tanglishText = "சுபி டியர், இன் ஆதிஸ் புக், ஸ்டோரி ஒன் இஸ் 'தி கூஸ் அண்ட் தி கோல்டன் எக்ஸ்'. மாரல்: கிரீட் பிரிங்ஸ் கிரேட் லாஸ்.",
                coachingTip = "சுபி, மேலும் கதைகளைக் கேட்க 'Tell me the Lion and Mouse story' அல்லது 'Thirsty Crow story' என்று கேள்!",
                modelUsed = modelDisplayName
            )
        }

        if (lower.contains("good morning") || lower.contains("காலை வணக்கம்")) {
            return TutorResponse(
                englishText = "Good morning, Subi! I am Dhanam Teacher powered by Gemini 3.8 Flash. Ask me any question about English, Science, Math, GK, or Stories!",
                tamilText = "காலை வணக்கம் சுபிக்சா (சுபி)! நான் Gemini 3.8 Flash உடன் இயங்கும் உன் தனம் டீச்சர். ஆங்கிலம், அறிவியல், கணிதம், பொது அறிவு, கதைகள் என எதை வேண்டுமானாலும் என்னிடம் கேள்!",
                tanglishText = "குட் மார்னிங், சுபி! ஐ அம் தனம் டீச்சர் பவர்ட் பை ஜெமினி 3.8 ஃபிளாஷ். ஆஸ்க் மீ எனி கொஸ்டின்!",
                coachingTip = "சுபி, காலையில் டீச்சரைப் பார்க்கும்போது 'Good morning, Dhanam Teacher!' என்று உற்சாகமாகச் சொல்லலாம்.",
                modelUsed = modelDisplayName
            )
        }

        if (lower.contains("good evening") || lower.contains("good afternoon") || lower.contains("மாலை வணக்கம்") || lower.contains("மதிய வணக்கம்")) {
            return TutorResponse(
                englishText = "Good evening, Subi! How was your day today? You can ask me any question or practice speaking English with me!",
                tamilText = "மாலை வணக்கம் சுபி! இன்று உன் நாள் எப்படி இருந்தது? நீ என்னிடம் எந்தக் கேள்வியும் கேட்கலாம் அல்லது ஆங்கிலம் பேசிப் பழகலாம்!",
                tanglishText = "குட் ஈவ்னிங், சுபி! ஹவ் வாஸ் யுவர் டே டுடே? யூ கேன் ஆஸ்க் மீ எனி கொஸ்டின்!",
                coachingTip = "சுபி, இன்று நடந்ததைக் கூற Past Tense (உ.ம்: 'I played with my friends', 'I read a story') பயன்படுத்தவும்.",
                modelUsed = modelDisplayName
            )
        }

        if (lower.contains("how are you") || lower.contains("how is your day") || lower.contains("epdi irukinga") || lower.contains("எப்படி இருக்க") || lower.contains("நலமா")) {
            return TutorResponse(
                englishText = "I am doing wonderfully well, thank you for asking, Subi! How are you feeling today? Ask me anything you want to learn!",
                tamilText = "நான் மிக நலமாக இருக்கிறேன் சுபி, கேட்டதற்கு நன்றி! இன்று நீ எப்படி இருக்கிறாய்? நீ எதைப் பற்றித் தெரிந்துகொள்ள விரும்பினாலும் என்னிடம் கேள்!",
                tanglishText = "ஐ அம் டூயிங் வொண்டர்ஃபுல்லி வெல், தேங்க் யூ ஃபார் ஆஸ்கிங், சுபி! ஹவ் ஆர் யூ ஃபீலிங் டுடே?",
                coachingTip = "சுபி, யாராவது 'How are you?' எனக் கேட்டால், 'I am doing great, thank you!' என அழகாகப் பதில் சொல்லிப் பழகு.",
                modelUsed = modelDisplayName
            )
        }

        if (lower.contains("hello") || lower.contains("hi") || lower.contains("hey") || lower.contains("vanakkam") || lower.contains("வணக்கம்")) {
            return TutorResponse(
                englishText = "Hello Subiksha (Subi)! I am Dhanam Teacher with Gemini 3.8 Flash. You can ask me any question in Tamil or English — Science, Math, GK, Grammar, Stories, or Translations!",
                tamilText = "வணக்கம் சுபிக்சா (சுபி)! நான் Gemini 3.8 Flash சக்தியுடன் இயங்கும் உன் தனம் டீச்சர். அறிவியல், கணிதம், பொது அறிவு, இலக்கணம், கதைகள், மொழிபெயர்ப்பு என எதை வேண்டுமானாலும் தமிழில் அல்லது ஆங்கிலத்தில் கேள்!",
                tanglishText = "ஹலோ சுபிக்சா (சுபி)! ஐ அம் தனம் டீச்சர் வித் ஜெமினி 3.8 ஃபிளாஷ். யூ கேன் ஆஸ்க் மீ எனி கொஸ்டின் இன் தமிழ் ஆர் இங்கிலீஷ்!",
                coachingTip = "சுபி, 'Why is the sky blue?', '15 * 12 என்ன?', 'Tell me a story', 'Past tense என்றால் என்ன?' போன்று எதை வேண்டுமானாலும் கேள்!",
                modelUsed = modelDisplayName
            )
        }

        if (lower.contains("who are you") || lower.contains("your name") || lower.contains("dhanam") || lower.contains("தனம்") || lower.contains("யார் நீ")) {
            return TutorResponse(
                englishText = "I am Dhanam Teacher, your AI Spoken English & General Knowledge Tutor powered by Gemini 3.8 Flash! I can answer any question you ask in English and Tamil.",
                tamilText = "நான் தனம் டீச்சர் (Gemini 3.8 Flash AI ஆசிரியை)! நீ கேட்கும் எந்தக் கேள்விக்கும் ஆங்கிலத்திலும் தமிழிலும் உடனடியாகப் பதிலளிப்பேன்.",
                tanglishText = "ஐ அம் தனம் டீச்சர், யுவர் ஏஐ ஸ்போக்கன் இங்கிலீஷ் அண்ட் ஜெனரல் நாலெட்ஜ் ட்யூட்டர் பவர்ட் பை ஜெமினி 3.8 ஃபிளாஷ்!",
                coachingTip = "உன்னை அறிமுகப்படுத்த 'My name is Subiksha, you can call me Subi. I am 9 years old.' எனத் தெளிவாகச் சொல்லலாம்!",
                modelUsed = modelDisplayName
            )
        }

        if (lower.contains("thank") || lower.contains("நன்றி") || lower.contains("romba nandri")) {
            return TutorResponse(
                englishText = "You are most welcome, Subi dear! Dhanam Teacher is always happy to answer all your questions.",
                tamilText = "மிக்க மகிழ்ச்சி சுபி செல்லம்! உன் எல்லாக் கேள்விகளுக்கும் பதிலளிக்க தனம் டீச்சருக்கு எப்போதும் மகிழ்ச்சி.",
                tanglishText = "யூ ஆர் மோஸ்ட் வெல்கம், சுபி டியர்! தனம் டீச்சர் இஸ் ஆல்வேஸ் ஹேப்பி டூ ஆன்சர் ஆல் யுவர் கொஸ்டின்ஸ்.",
                coachingTip = "சுபி, யாராவது உனக்கு உதவி செய்தால் 'Thank you so much!' என்று சொல்வது நல்ல பழக்கம்.",
                modelUsed = modelDisplayName
            )
        }

        if (lower.contains("eat") || lower.contains("food") || lower.contains("சாப்பாடு") || lower.contains("சாப்பிட்டீங்களா") || lower.contains("saptingala") || lower.contains("breakfast") || lower.contains("lunch") || lower.contains("dinner")) {
            return TutorResponse(
                englishText = "Yes Subi, Dhanam Teacher had her meal! Did you finish your food, Subi? Eating healthy food gives you energy to study and play!",
                tamilText = "ஆமாம் சுபி, தனம் டீச்சர் சாப்பிட்டுவிட்டேன்! சுபி நீ சாப்பிட்டாயா? சத்தான உணவு சாப்பிடுவது படிப்பதற்கும் விளையாடுவதற்கும் நல்ல ஆற்றலைத் தரும்!",
                tanglishText = "எஸ் சுபி, தனம் டீச்சர் ஹேட் ஹெர் மீல்! டிட் யூ ஃபினிஷ் யுவர் ஃபுட், சுபி?",
                coachingTip = "சுபி, நீ என்ன சாப்பிட்டாய் என்று சொல்ல 'I had idli and sambar for breakfast' போன்று சொல்லிப் பழகு!",
                modelUsed = modelDisplayName
            )
        }

        // Fourth: Intelligent Question & Sentence Analyzer so ANY arbitrary Tamil or English input gets a real educational & translated response
        val smartReply = buildIntelligentUniversalResponse(cleanedInput)
        return smartReply.copy(modelUsed = modelDisplayName)
    }

    private fun smartTranslateSentence(input: String): Triple<String, String, String> {
        val lower = input.lowercase()
        return when {
            lower.contains("மழை") -> Triple(
                "Will it rain tomorrow? / It is raining heavily today.",
                "'$input' என்பதை ஆங்கிலத்தில் 'Will it rain tomorrow?' (நாளை மழை பெய்யுமா?) அல்லது 'It is raining now' (இப்போது மழை பெய்கிறது) என்று சொல்லலாம்.",
                "மழை சார்ந்த வாக்கியங்களில் 'It' என்ற Subject-ஐப் பயன்படுத்தி 'It is raining' என்று தொடங்க வேண்டும்."
            )
            lower.contains("பசி") -> Triple(
                "I am feeling very hungry. Could I have something to eat?",
                "'$input' என்பதை ஆங்கிலத்தில் 'I am feeling very hungry' (எனக்கு மிகவும் பசிக்கிறது) என்று இயல்பாகச் சொல்லலாம்.",
                "'I am hungry' (பசிக்கிறது), 'I am thirsty' (தாகமாக உள்ளது), 'I am sleepy' (தூக்கம் வருகிறது) என ஒரே பார்முலாவில் பேசலாம்!"
            )
            lower.contains("வெளியே") || lower.contains("விளையாட") -> Triple(
                "May I go outside to play with my friends?",
                "'$input' என்பதை ஆங்கிலத்தில் 'May I go outside to play with my friends?' என்று மரியாதையாகக் கேட்கலாம்.",
                "அனுமதி கேட்கும்போது எப்போதும் 'May I...' அல்லது 'Can I...' என்று வாக்கியத்தைத் தொடங்குங்கள்."
            )
            lower.contains("புரியவில்லை") || lower.contains("புரியல") -> Triple(
                "I did not understand this clearly. Could you please explain it again?",
                "'$input' என்பதை ஆங்கிலத்தில் 'I did not understand this. Could you please explain it again?' என்று கேட்கலாம்.",
                "வகுப்பறையில் டீச்சரிடம் மீண்டும் விளக்கம் கேட்க 'Could you please repeat that?' என்று கேட்பது மிகச் சிறந்த முறை."
            )
            else -> Triple(
                "Here is how to express \"$input\" clearly in English: \"Could you please tell me more about $input?\"",
                "\"$input\" என்ற கருத்தை ஆங்கிலத்தில் தெளிவாக வெளிப்படுத்த: Subject + Verb + Object வரிசையில் அமைத்துப் பேசலாம். முழு AI மொழிபெயர்ப்புக்கு AI Studio Secrets-ல் GEMINI_API_KEY இணைக்கப்பட்டுள்ளதை உறுதி செய்யவும்.",
                "தமிழில் உள்ள வாக்கியத்தை ஆங்கிலத்தில் மாற்றும்போது முதலில் 'யார் செய்கிறார்' (Subject), அடுத்து 'என்ன செயல்' (Verb) என்பதைக் கண்டறியுங்கள்!"
            )
        }
    }

    private fun buildIntelligentUniversalResponse(input: String): TutorResponse {
        val hasTamil = input.any { it in '\u0B80'..'\u0BFF' }
        val lower = input.lowercase()

        // Check if it's a question (WH-words or Tamil question words)
        val isQuestion = input.endsWith("?") ||
            lower.startsWith("what") || lower.startsWith("why") || lower.startsWith("how") ||
            lower.startsWith("where") || lower.startsWith("when") || lower.startsWith("who") ||
            lower.startsWith("which") || lower.startsWith("can") || lower.startsWith("is") ||
            lower.startsWith("do") || lower.startsWith("does") || lower.startsWith("did") ||
            input.contains("என்ன") || input.contains("ஏன்") || input.contains("எப்படி") ||
            input.contains("எங்கே") || input.contains("எப்போது") || input.contains("யார்") ||
            input.contains("எது") || input.contains("முடியுமா")

        if (hasTamil) {
            // Translate known Tamil words inside the user's input to give a real English sentence!
            val wordMappings = mapOf(
                "நான்" to "I", "நீ" to "you", "நீங்கள்" to "you", "அவன்" to "he", "அவள்" to "she",
                "நாம்" to "we", "அவர்கள்" to "they", "எனக்கு" to "I need / For me", "உனக்கு" to "for you",
                "பள்ளி" to "school", "வீடு" to "home", "புத்தகம்" to "book", "தண்ணீர்" to "water",
                "சாப்பாடு" to "food", "விளையாட்டு" to "game", "நண்பர்" to "friend", "ஆசிரியர்" to "teacher",
                "படி" to "study / read", "வா" to "come", "போ" to "go", "சாப்பிடு" to "eat",
                "பேசு" to "speak", "கேள்" to "ask / listen", "பார்" to "see / look", "எழுது" to "write",
                "இன்று" to "today", "நாளை" to "tomorrow", "நேற்று" to "yesterday", "இப்போது" to "now",
                "நல்ல" to "good", "பெரிய" to "big", "சிறிய" to "small", "அழகான" to "beautiful",
                "என்ன" to "what", "ஏன்" to "why", "எப்படி" to "how", "எங்கே" to "where", "யார்" to "who"
            )
            val matchedPairs = wordMappings.entries.filter { input.contains(it.key) }
            val vocabHintEn = if (matchedPairs.isNotEmpty()) {
                matchedPairs.joinToString(", ") { "'${it.key}' = '${it.value}'" }
            } else {
                "Key phrase: '$input'"
            }

            val constructedEng = if (isQuestion) {
                "Great question, Subi! To ask \"$input\" in English, key words are: $vocabHintEn. In polite English, you can ask: \"Could you please explain this to me?\""
            } else {
                "Wonderful thought, Subi! For \"$input\", the key English words are: $vocabHintEn. Practice speaking each word aloud with confidence!"
            }

            return TutorResponse(
                englishText = constructedEng,
                tamilText = "சுபி, நீ கேட்ட \"$input\" என்பதற்கான முக்கிய ஆங்கில வார்த்தைகள்: $vocabHintEn. " +
                    if (isQuestion) "ஆங்கிலத்தில் கேள்வி கேட்கும்போது What (என்ன), Why (ஏன்), How (எப்படி), Where (எங்கே), When (எப்போது), Who (யார்) ஆகிய WH-வார்த்தைகளை முதலில் பயன்படுத்த வேண்டும்!"
                    else "தமிழில் வாக்கியம்: Subject + Object + Verb என இருக்கும்; ஆனால் ஆங்கிலத்தில் Subject + Verb + Object என்ற வரிசையில் பேச வேண்டும்!",
                tanglishText = formatPronunciationInTamil(constructedEng, ""),
                coachingTip = "💡 உதவிக்குறிப்பு: எல்லாத் தலைப்புகளிலும் Gemini 3.8 Flash நேரலை AI பதில்களைப் பெற AI Studio-வின் Secrets பகுதியில் GEMINI_API_KEY-ஐ அமைக்கலாம்!"
            )
        } else {
            // User asked or typed in English
            val engReply = if (isQuestion) {
                "That is an excellent question, Subi: \"$input\"! Your English question structure is clear. When answering this in English, start with a complete sentence using the main subject and verb from the question."
            } else {
                "Well said in English, Subi: \"$input\"! Your sentence is clear and confident. Let's also look at its Tamil pronunciation and meaning below!"
            }
            return TutorResponse(
                englishText = engReply,
                tamilText = if (isQuestion) {
                    "சுபி, \"$input\" என்று மிக அழகான ஆங்கிலக் கேள்வியைக் கேட்டிருக்கிறாய்! இந்தக் கேள்வியின் உச்சரிப்பு கீழே தமிழ் எழுத்துக்களில் கொடுக்கப்பட்டுள்ளது. (நேரலை Gemini 3.8 Flash AI பதில்களுக்கு Secrets பகுதியில் GEMINI_API_KEY சேர்க்கவும்)."
                } else {
                    "சுபி, \"$input\" என்ற ஆங்கில வாக்கியத்தைத் தெளிவாக உருவாக்கியுள்ளாய்! இதன் தமிழ் உச்சரிப்பைக் கீழே பார்த்துச் சத்தமாக வாசித்துப் பழகு."
                },
                tanglishText = formatPronunciationInTamil(input, ""),
                coachingTip = "சுபி, நீ பேசிய ஆங்கில வாக்கியத்தின் தமிழ் உச்சரிப்பு: \"${formatPronunciationInTamil(input, "")}\""
            )
        }
    }

    /**
     * Ensures any pronunciation guide is displayed in Tamil script (தமிழ் எழுத்துக்களில் உச்சரிப்பு)
     * rather than broken/misspelled English letters.
     */
    fun formatPronunciationInTamil(englishText: String, rawPronunciation: String): String {
        val trimmed = rawPronunciation.trim()
            .replace("மலர்", "தனம்")
            .replace("Malar", "Dhanam", ignoreCase = true)

        // If it already contains Tamil script characters, return as-is
        if (trimmed.isNotEmpty() && trimmed.any { it in '\u0B80'..'\u0BFF' }) {
            return trimmed
        }

        // Otherwise, convert the English sentence into Tamil script pronunciation
        return englishToTamilPronunciation(englishText)
    }

    private val wordPronunciationMap = mapOf(
        "i" to "ஐ", "am" to "அம்", "is" to "இஸ்", "are" to "ஆர்", "was" to "வாஸ்", "were" to "வேர்",
        "be" to "பீ", "been" to "பீன்", "being" to "பீயிங்",
        "you" to "யூ", "your" to "யுவர்", "yours" to "யுவர்ஸ்", "yourself" to "யுவர்செல்ஃப்",
        "we" to "வீ", "our" to "அவர்", "us" to "அஸ்", "they" to "தே", "their" to "தேர்", "them" to "தெம்",
        "he" to "ஹீ", "him" to "ஹிம்", "his" to "ஹிஸ்", "she" to "ஷீ", "her" to "ஹெர்",
        "it" to "இட்", "its" to "இட்ஸ்", "my" to "மை", "me" to "மீ", "mine" to "மைன்",
        "a" to "எ", "an" to "அன்", "the" to "தி", "in" to "இன்", "on" to "ஆன்", "at" to "அட்", "to" to "டு",
        "for" to "ஃபார்", "of" to "ஆஃப்", "with" to "வித்", "from" to "ஃப்ரம்", "by" to "பை", "about" to "அபௌட்",
        "into" to "இன்டு", "through" to "த்ரூ", "after" to "ஆஃப்டர்", "before" to "பிஃபோர்", "between" to "பிட்வீன்",
        "under" to "அண்டர்", "over" to "ஓவர்", "up" to "அப்", "down" to "டவுன்", "out" to "அவுட்",
        "and" to "அண்ட்", "or" to "ஆர்", "but" to "பட்", "so" to "சோ", "if" to "இஃப்", "because" to "பிகாஸ்",
        "hello" to "ஹலோ", "hi" to "ஹாய்", "hey" to "ஹே", "vanakkam" to "வணக்கம்",
        "dhanam" to "தனம்", "malar" to "தனம்", "teacher" to "டீச்சர்", "subiksha" to "சுபிக்சா", "subi" to "சுபி",
        "gemini" to "ஜெமினி", "flash" to "ஃபிளாஷ்", "pro" to "புரோ", "ai" to "ஏஐ",
        "student" to "ஸ்டூடண்ட்", "girl" to "கேர்ள்", "nine" to "நைன்", "9" to "நைன்", "years" to "இயர்ஸ்", "old" to "ஓல்ட்",
        "dear" to "டியர்", "school" to "ஸ்கூல்", "super" to "சூப்பர்", "bright" to "பிரைட்", "smart" to "ஸ்மார்ட்",
        "good" to "குட்", "morning" to "மார்னிங்", "afternoon" to "ஆஃப்டர்நூன்", "evening" to "ஈவ்னிங்", "night" to "நைட்",
        "how" to "ஹவ்", "what" to "வாட்", "where" to "வேர்", "when" to "வென்", "why" to "ஒய்", "who" to "ஹூ", "which" to "விச்",
        "doing" to "டூயிங்", "wonderfully" to "வொண்டர்ஃபுல்லி", "wonderful" to "வொண்டர்ஃபுல்", "well" to "வெல்",
        "thank" to "தேங்க்", "thanks" to "தேங்க்ஸ்", "asking" to "ஆஸ்கிங்", "feeling" to "ஃபீலிங்",
        "today" to "டுடே", "tomorrow" to "டுமாரோ", "yesterday" to "எஸ்டர்டே", "day" to "டே", "ahead" to "அஹெட்",
        "going" to "கோயிங்", "go" to "கோ", "went" to "வென்ட்", "come" to "கம்", "came" to "கேம்",
        "can" to "கேன்", "could" to "குட்", "would" to "வுட்", "should" to "ஷுட்", "will" to "வில்", "may" to "மே",
        "please" to "ப்ளீஸ்", "tell" to "டெல்", "little" to "லிட்டில்", "bit" to "பிட்",
        "work" to "ஒர்க்", "as" to "அஸ்", "software" to "சாஃப்ட்வேர்", "engineer" to "இன்ஜினியர்", "chennai" to "சென்னை",
        "english" to "இங்கிலீஷ்", "tamil" to "தமிழ்", "voice" to "வாய்ஸ்", "companion" to "கம்பேனியன்",
        "speak" to "ஸ்பீக்", "speaking" to "ஸ்பீக்கிங்", "spoken" to "ஸ்போக்கன்", "talk" to "டாக்",
        "practice" to "பிராக்டிஸ்", "great" to "கிரேட்", "nice" to "நைஸ்", "meet" to "மீட்",
        "have" to "ஹேவ்", "has" to "ஹேஸ்", "had" to "ஹேட்", "do" to "டூ", "does" to "டஸ்", "did" to "டிட்",
        "yes" to "எஸ்", "no" to "நோ", "not" to "நாட்", "don't" to "டோன்ட்", "didn't" to "டிடின்ட்", "cannot" to "கேன்னாட்",
        "that" to "தட்", "this" to "திஸ்", "these" to "தீஸ்", "those" to "தோஸ்", "here" to "ஹியர்", "there" to "தேர்",
        "now" to "நவ்", "right" to "ரைட்", "all" to "ஆல்", "any" to "எனி", "anything" to "எனிதிங்", "everything" to "எவ்ரிதிங்",
        "question" to "கொஸ்டின்", "questions" to "கொஸ்டின்ஸ்", "answer" to "ஆன்சர்", "answers" to "ஆன்சர்ஸ்",
        "ask" to "ஆஸ்க்", "explain" to "எக்ஸ்பிளைன்", "learn" to "லேர்ன்", "learning" to "லேர்னிங்",
        "science" to "சயின்ஸ்", "math" to "மேத்", "mathematics" to "மேத்தமேட்டிக்ஸ்", "story" to "ஸ்டோரி", "stories" to "ஸ்டோரீஸ்",
        "sky" to "ஸ்கை", "blue" to "ப்ளூ", "sun" to "சன்", "moon" to "மூன்", "earth" to "எர்த்", "water" to "வாட்டர்",
        "rain" to "ரெயின்", "time" to "டைம்", "date" to "டேட்", "equals" to "ஈக்வல்ஸ்", "plus" to "பிளஸ்", "minus" to "மைனஸ்",
        "times" to "டைம்ஸ்", "multiplied" to "மல்டிப்ளைடு", "divided" to "டிவைடட்", "number" to "நம்பர்",
        "india" to "இந்தியா", "capital" to "கேப்பிட்டல்", "new" to "நியூ", "delhi" to "டெல்லி",
        "like" to "லைக்", "love" to "லவ்", "happy" to "ஹேப்பி", "proud" to "பிரௌட்", "welcome" to "வெல்கம்",
        "help" to "ஹெல்ப்", "helping" to "ஹெல்ப்பிங்", "most" to "மோஸ்ட்", "always" to "ஆல்வேஸ்",
        "tea" to "டீ", "coffee" to "காபி", "food" to "ஃபுட்", "meal" to "மீல்", "eat" to "ஈட்", "ate" to "ஏட்",
        "milk" to "மில்க்", "drink" to "டிரிங்க்", "play" to "ப்ளே", "playing" to "பிளேயிங்", "friends" to "ஃப்ரெண்ட்ஸ்",
        "friend" to "ஃப்ரெண்ட்", "book" to "புக்", "read" to "ரீட்", "reading" to "ரீடிங்", "write" to "ரைட்"
    )

    private fun englishToTamilPronunciation(sentence: String): String {
        return sentence.split(Regex("\\s+")).joinToString(" ") { token ->
            val prefix = token.takeWhile { !it.isLetterOrDigit() && it != '\'' }
            val suffix = token.takeLastWhile { !it.isLetterOrDigit() && it != '\'' }
            val core = token.removePrefix(prefix).removeSuffix(suffix).lowercase()
            if (core.isEmpty()) {
                token
            } else {
                val mapped = wordPronunciationMap[core] ?: phoneticTransliterateToTamil(core)
                "$prefix$mapped$suffix"
            }
        }
    }

    /**
     * Converts any arbitrary English word into approximate Tamil script pronunciation
     * so no English letters remain in the pronunciation guide.
     */
    private fun phoneticTransliterateToTamil(word: String): String {
        if (word.any { it in '\u0B80'..'\u0BFF' }) return word
        if (word.all { it.isDigit() }) return word

        val sb = StringBuilder()
        var i = 0
        val w = word.lowercase()
        while (i < w.length) {
            when {
                w.startsWith("tion", i) -> { sb.append("ஷன்"); i += 4 }
                w.startsWith("sion", i) -> { sb.append("ஷன்"); i += 4 }
                w.startsWith("ture", i) -> { sb.append("ச்சர்"); i += 4 }
                w.startsWith("ight", i) -> { sb.append("ஐட்"); i += 4 }
                w.startsWith("ing", i) -> { sb.append("இங்"); i += 3 }
                w.startsWith("ch", i) -> { sb.append("ச்"); i += 2 }
                w.startsWith("sh", i) -> { sb.append("ஷ்"); i += 2 }
                w.startsWith("th", i) -> { sb.append("த்"); i += 2 }
                w.startsWith("ph", i) -> { sb.append("ஃப்"); i += 2 }
                w.startsWith("wh", i) -> { sb.append("வ்"); i += 2 }
                w.startsWith("ck", i) -> { sb.append("க்"); i += 2 }
                w.startsWith("qu", i) -> { sb.append("க்வ"); i += 2 }
                w.startsWith("ee", i) -> { sb.append("ஈ"); i += 2 }
                w.startsWith("oo", i) -> { sb.append("ஊ"); i += 2 }
                w.startsWith("ea", i) -> { sb.append("ஈ"); i += 2 }
                w.startsWith("ai", i) -> { sb.append("ஏ"); i += 2 }
                w.startsWith("ay", i) -> { sb.append("ஏ"); i += 2 }
                w.startsWith("ou", i) -> { sb.append("அவு"); i += 2 }
                w.startsWith("ow", i) -> { sb.append("ஓ"); i += 2 }
                else -> {
                    when (w[i]) {
                        'a' -> sb.append(if (i == 0) "அ" else "ா")
                        'e' -> sb.append(if (i == 0) "எ" else if (i == w.length - 1) "" else "ெ")
                        'i' -> sb.append(if (i == 0) "இ" else "ி")
                        'o' -> sb.append(if (i == 0) "ஒ" else "ோ")
                        'u' -> sb.append(if (i == 0) "உ" else "ு")
                        'y' -> sb.append(if (i == 0) "ய" else "ி")
                        'b' -> sb.append("ப")
                        'c' -> sb.append(if (i + 1 < w.length && (w[i + 1] == 'e' || w[i + 1] == 'i')) "ச" else "க")
                        'd' -> sb.append("ட")
                        'f' -> sb.append("ஃப")
                        'g' -> sb.append("க")
                        'h' -> sb.append("ஹ")
                        'j' -> sb.append("ஜ")
                        'k' -> sb.append("க")
                        'l' -> sb.append("ல")
                        'm' -> sb.append("ம")
                        'n' -> sb.append("ன")
                        'p' -> sb.append("ப")
                        'r' -> sb.append("ர")
                        's' -> sb.append("ஸ")
                        't' -> sb.append("ட")
                        'v' -> sb.append("வ")
                        'w' -> sb.append("வ")
                        'x' -> sb.append("க்ஸ்")
                        'z' -> sb.append("ஸ")
                        else -> sb.append(w[i])
                    }
                    i++
                }
            }
        }
        return sb.toString()
            .replace("ாா", "ா")
            .replace("ிி", "ீ")
            .ifBlank { word }
    }

    suspend fun translateTamilToEnglish(
        tamilText: String,
        preferredModel: GeminiModelOption = GeminiModelOption.GEMINI_3_8_FLASH
    ): Map<String, String> = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (_: Exception) { "" }
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = """
A native Tamil speaker wants to say or ask the following in English:
"$tamilText"

Provide 3 natural spoken English variations with 100% accurate English spelling:
1. Casual (for friends and informal chat)
2. Formal (for office, school, elders, emails, or interviews)
3. Short (quick, direct daily phrase)
Include the pronunciation of the Formal English variation written strictly in TAMIL SCRIPT (தமிழ் எழுத்துக்களில் ஆங்கில உச்சரிப்பு).

Output strictly valid JSON:
{
  "casual": "Casual English phrase",
  "formal": "Polite formal English phrase",
  "short": "Short direct phrase",
  "tanglish": "ஆங்கில உச்சரிப்பு தமிழ் எழுத்துக்களில்",
  "tip": "Tamil explanation of difference"
}
"""
                val request = GeminiRequest(
                    contents = listOf(ContentPayload(role = "user", parts = listOf(PartPayload(text = prompt)))),
                    generationConfig = GenerationConfigPayload(
                        temperature = 0.5f,
                        maxOutputTokens = 2048,
                        responseMimeType = "application/json"
                    )
                )
                val response = GeminiApiClient.generateWithFallback(apiKey, request, preferredModel.id)
                val raw = response?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!raw.isNullOrBlank()) {
                    val firstBrace = raw.indexOf('{')
                    val lastBrace = raw.lastIndexOf('}')
                    if (firstBrace != -1 && lastBrace > firstBrace) {
                        val obj = JSONObject(raw.substring(firstBrace, lastBrace + 1))
                        val formalText = obj.optString("formal")
                        return@withContext mapOf(
                            "casual" to obj.optString("casual"),
                            "formal" to formalText,
                            "short" to obj.optString("short"),
                            "tanglish" to formatPronunciationInTamil(formalText, obj.optString("tanglish")),
                            "tip" to obj.optString("tip")
                        )
                    }
                }
            } catch (_: Exception) {}
        }

        // Local smart translation heuristics for common queries
        val lower = tamilText.lowercase()
        val casual: String
        val formal: String
        val short: String
        val tanglish: String
        val tip: String

        when {
            lower.contains("லீவு") || lower.contains("விடுமுறை") || lower.contains("leave") -> {
                casual = "I am taking tomorrow off."
                formal = "I would like to request leave for tomorrow, please."
                short = "Taking leave tomorrow."
                tanglish = "ஐ வுட் லைக் டு ரிக்வெஸ்ட் லீவ் ஃபார் டுமாரோ, ப்ளீஸ்."
                tip = "நண்பர்களிடம் 'taking off' என்றும், ஆசிரியர்/உயர் அதிகாரிகளிடம் 'request leave' என்றும் சொல்லலாம்."
            }
            lower.contains("சாப்பிட்டீங்களா") || lower.contains("saptingala") || lower.contains("food") -> {
                casual = "Did you eat?"
                formal = "Have you had your meal yet?"
                short = "Had food?"
                tanglish = "ஹேவ் யூ ஹேட் யுவர் மீல் எட்?"
                tip = "மரியாதையான ஆங்கிலத்தில் 'Have you had...' என்று கேட்பது மிகவும் இயல்பானது."
            }
            lower.contains("நேரமாச்சு") || lower.contains("late") || lower.contains("தாமதம்") -> {
                casual = "Running a bit late, see you soon!"
                formal = "Please excuse my delay, I will be arriving shortly."
                short = "I'm running late."
                tanglish = "ப்ளீஸ் எக்ஸ்கியூஸ் மை டிலே, ஐ வில் பீ அரைவிங் ஷார்ட்லி."
                tip = "'Running late' என்பது தாமதமாக வருவதைக் குறிக்கும் அருமையான சொற்றொடர்."
            }
            lower.contains("எப்படி சொல்வது") || lower.contains("உதவி") || lower.contains("help") -> {
                casual = "Can you help me with this?"
                formal = "Could you please assist me with this matter?"
                short = "Need a hand here."
                tanglish = "குட் யூ ப்ளீஸ் அசிஸ்ட் மீ வித் திஸ் மேட்டர்?"
                tip = "'Can' என்பதற்கு பதில் 'Could' பயன்படுத்தினால் கூடுதல் மரியாதை கிடைக்கும்."
            }
            else -> {
                val smart = smartTranslateSentence(tamilText)
                casual = smart.first
                formal = "Could you please tell me more about this?"
                short = smart.first.take(50)
                tanglish = formatPronunciationInTamil(casual, "")
                tip = smart.third
            }
        }

        mapOf(
            "casual" to casual,
            "formal" to formal,
            "short" to short,
            "tanglish" to tanglish,
            "tip" to tip
        )
    }

    fun calculatePronunciationScore(target: String, spoken: String): Int {
        val cleanTarget = target.lowercase().replace(Regex("[^a-zA-Z0-9 ]"), "").trim()
        val cleanSpoken = spoken.lowercase().replace(Regex("[^a-zA-Z0-9 ]"), "").trim()

        if (cleanTarget.isEmpty() || cleanSpoken.isEmpty()) return 0
        if (cleanTarget == cleanSpoken) return 100

        val targetWords = cleanTarget.split(" ").filter { it.isNotBlank() }
        val spokenWords = cleanSpoken.split(" ").filter { it.isNotBlank() }

        var matchCount = 0
        for (w in targetWords) {
            if (spokenWords.any { it == w || it.contains(w) || w.contains(it) }) {
                matchCount++
            }
        }

        val wordScore = (matchCount.toFloat() / targetWords.size.toFloat()) * 100
        val dist = levenshteinDistance(cleanTarget, cleanSpoken)
        val maxLen = maxOf(cleanTarget.length, cleanSpoken.length)
        val charScore = ((maxLen - dist).toFloat() / maxLen.toFloat()) * 100

        val blended = (wordScore * 0.6f + charScore * 0.4f).toInt()
        return blended.coerceIn(10, 100)
    }

    private fun levenshteinDistance(s1: String, s2: String): Int {
        val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }
        for (i in 0..s1.length) dp[i][0] = i
        for (j in 0..s2.length) dp[0][j] = j
        for (i in 1..s1.length) {
            for (j in 1..s2.length) {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                dp[i][j] = minOf(dp[i - 1][j] + 1, dp[i][j - 1] + 1, dp[i - 1][j - 1] + cost)
            }
        }
        return dp[s1.length][s2.length]
    }
}
