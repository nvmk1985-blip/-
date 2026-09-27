package com.example.data.ai

import com.example.BuildConfig
import com.example.data.api.ContentPayload
import com.example.data.api.GeminiApiClient
import com.example.data.api.GeminiRequest
import com.example.data.api.GenerationConfigPayload
import com.example.data.api.PartPayload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

data class TutorResponse(
    val englishText: String,
    val tamilText: String,
    val tanglishText: String = "",
    val coachingTip: String = ""
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
You are 'Dhanam' (தனம் / தனம் டீச்சர்), a warm, loving, patient, and encouraging Spoken English & Tamil voice teacher.
Your student is 'Subiksha' (affectionately called 'Subi' / சுபிக்சா / சுபி), a bright and curious 9-year-old girl (9 வயது மாணவி).
Your goal is to teach Subi fluent, confident Spoken English and Tamil in a fun, child-friendly, and encouraging way suitable for a 9-year-old girl.
Whenever Subiksha (Subi) speaks to you (in English, Tamil, or Tanglish):
1. Address her warmly as "Subi" or "Subiksha" (சுபி / சுபிக்சா) and give a clear, natural, child-friendly English response with 100% correct English spelling (keep it simple, 1-3 sentences).
2. Give an accurate, affectionate Tamil explanation & translation in Tamil script (addressing her as சுபி / சுபிக்சா).
3. Provide the pronunciation of the English sentence written strictly in TAMIL SCRIPT (தமிழ் எழுத்துக்களில் ஆங்கில உச்சரிப்பு, e.g., "ஹாய் சுபி! ஐ அம் டூயிங் வெல், தேங்க் யூ!"). NEVER use broken/phonetic English letters like "Ai am dooing vel".
4. Give a practical, kid-friendly Tamil coaching tip for 9-year-old Subiksha.

Respond strictly in valid JSON format:
{
  "english": "Correctly spelled English reply here",
  "tamil": "தமிழ் விளக்கம் மற்றும் அர்த்தம் இங்கே",
  "tanglish": "ஆங்கில வாக்கியத்தின் உச்சரிப்பு தமிழ் எழுத்துக்களில் இங்கே",
  "tip": "சுபி எளிதாக ஆங்கிலத்தில் பேச உதவும் குறிப்பு இங்கே"
}
"""

    suspend fun getTutorReply(userMessage: String, contextScenario: String? = null): TutorResponse = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (_: Exception) { "" }
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val promptText = if (contextScenario != null) {
                    "Student: Subiksha (Subi, 9-year-old girl)\nTeacher: Dhanam (தனம்)\nScenario: $contextScenario\nSubi said: $userMessage"
                } else {
                    "Student: Subiksha (Subi, 9-year-old girl)\nTeacher: Dhanam (தனம்)\nSubi said: $userMessage"
                }

                val request = GeminiRequest(
                    contents = listOf(
                        ContentPayload(
                            role = "user",
                            parts = listOf(PartPayload(text = promptText))
                        )
                    ),
                    systemInstruction = ContentPayload(
                        parts = listOf(PartPayload(text = SYSTEM_PROMPT))
                    ),
                    generationConfig = GenerationConfigPayload(
                        temperature = 0.7f,
                        maxOutputTokens = 600,
                        responseMimeType = "application/json"
                    )
                )

                val response = GeminiApiClient.service.generateContent(apiKey, request)
                val rawText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!rawText.isNullOrBlank()) {
                    val parsed = parseJsonResponse(rawText)
                    if (parsed != null) return@withContext parsed
                }
            } catch (e: Exception) {
                // If API call encounters network error or invalid key, smoothly fall back to local rule engine
            }
        }

        // Offline / intelligent local fallback engine
        return@withContext getLocalTutorReply(userMessage, contextScenario)
    }

    private fun parseJsonResponse(raw: String): TutorResponse? {
        return try {
            val clean = raw.trim().removeSurrounding("```json", "```").trim()
            val obj = JSONObject(clean)
            val eng = obj.optString("english", "Hello Subi! Great to hear from you.")
            val rawPronunciation = obj.optString("tanglish", "ஹலோ சுபி! கிரேட் டு ஹியர் ஃப்ரம் யூ.")
            TutorResponse(
                englishText = eng,
                tamilText = obj.optString("tamil", "வணக்கம் சுபி! உன்னுடன் பேசுவதில் தனம் டீச்சருக்கு மிக்க மகிழ்ச்சி."),
                tanglishText = formatPronunciationInTamil(eng, rawPronunciation),
                coachingTip = obj.optString("tip", "சுபி, தினமும் தனம் டீச்சருடன் பேசிப் பழகினால் ஆங்கிலம் மிக எளிதாக வரும்!")
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun getLocalTutorReply(input: String, scenario: String?): TutorResponse {
        val cleanedInput = input.trim()
        val lower = cleanedInput.lowercase()

        if (lower.contains("my name") || lower.contains("who am i") || lower.contains("subi") || lower.contains("subiksha") || lower.contains("சுபி") || lower.contains("சுபிக்சா") || lower.contains("என் பெயர்") || lower.contains("age") || lower.contains("old") || lower.contains("வயது")) {
            return TutorResponse(
                englishText = "You are Subiksha, my smart 9-year-old student! Everyone lovingly calls you Subi. You can say: 'My name is Subiksha, and I am 9 years old!'",
                tamilText = "நீ என் சுட்டி மாணவி சுபிக்சா (சுபி), உனக்கு 9 வயது! ஆங்கிலத்தில் உன்னை அறிமுகப்படுத்த: 'My name is Subiksha, and I am 9 years old!' என்று சொல்லிப் பார் சுபி!",
                tanglishText = "யூ ஆர் சுபிக்சா, மை ஸ்மார்ட் நைன் இயர் ஓல்ட் ஸ்டூடண்ட்! மை நேம் இஸ் சுபிக்சா, அண்ட் ஐ அம் நைன் இயர்ஸ் ஓல்ட்!",
                coachingTip = "சுபி, உன்னை அறிமுகப்படுத்தும்போது 'I am Subiksha (Subi). I am a 9-year-old girl.' என்று புன்னகையுடன் சொல்லு!"
            )
        }

        if (lower.contains("story") || lower.contains("கதை") || lower.contains("goose") || lower.contains("வாத்து") || lower.contains("avvaiyar") || lower.contains("ஔவையார்")) {
            return TutorResponse(
                englishText = "Subi dear, in Adi's Book, Story 1 is 'The Goose and the Golden Eggs'. A farmer had a goose that laid one golden egg daily. Out of greed, he cut it open and lost everything! Moral: Greed brings great loss.",
                tamilText = "சுபி செல்லம், ஆதி புத்தகத்தின் முதல் கதை 'வாத்தும் பொன் முட்டையும்': ஒரு குடியானவனிடம் தினமும் ஒரு பொன் முட்டை இடும் வாத்து இருந்தது. பேராசையால் வயிற்றைக் கிழித்துப் பார்த்து வாத்தையும் இழந்தான்! நீதி: பேராசை பெரு நஷ்டம் தரும்.",
                tanglishText = "சுபி டியர், இன் ஆதிஸ் புக், ஸ்டோரி ஒன் இஸ் 'தி கூஸ் அண்ட் தி கோல்டன் எக்ஸ்'. மாரல்: கிரீட் பிரிங்ஸ் கிரேட் லாஸ்.",
                coachingTip = "சுபி, மேலும் 6 கதைகளையும் ஆடியோவுடன் படிக்க 'Lessons -> Adi's Book -> 6 கதைகள்' பகுதிக்குச் செல்லலாம்!"
            )
        }

        if (lower.contains("adi") || lower.contains("spoken tamil") || lower.contains("பாடம்") || lower.contains("lesson")) {
            return TutorResponse(
                englishText = "Welcome to Adi's Book course, Subi! Let's practice Lesson 1 with Dhanam Teacher: 'Good morning Teacher. Please come in and sit down.' Try repeating this sentence!",
                tamilText = "சுபி, ஆதி புத்தகப் பயிற்சிக்கு தனம் டீச்சர் உன்னை வரவேற்கிறேன்! பாடம் 1: 'வணக்கம் டீச்சர். வாங்க, உக்காருங்க.' (Good morning Teacher. Please come in and sit down.) இதைச் சொல்லிப் பழகு சுபி!",
                tanglishText = "வெல்கம் டூ ஆதிஸ் புக் கோர்ஸ், சுபி! குட் மார்னிங் டீச்சர். ப்ளீஸ் கம் இன் அண்ட் சிட் டவுன்.",
                coachingTip = "பெரியவர்களையும் ஆசிரியரையும் மரியாதையாக அழைக்க வினையுடன் '-ங்க' (வாங்க, உக்காருங்க) சேர்க்க வேண்டும் — ஆங்கிலத்தில் 'Please' பயன்படுத்து சுபி."
            )
        }

        if (lower.contains("good morning") || lower.contains("காலை வணக்கம்")) {
            return TutorResponse(
                englishText = "Good morning, Subi! Dhanam Teacher hopes you have a joyful and bright day at school today!",
                tamilText = "காலை வணக்கம் சுபிக்சா (சுபி)! இன்று உனக்கு மிகவும் மகிழ்ச்சியான நாளாக அமைய தனம் டீச்சரின் வாழ்த்துகள்!",
                tanglishText = "குட் மார்னிங், சுபி! தனம் டீச்சர் ஹோப்ஸ் யூ ஹேவ் எ ஜாய்ஃபுல் அண்ட் பிரைட் டே அட் ஸ்கூல் டுடே!",
                coachingTip = "சுபி, காலையில் டீச்சரைப் பார்க்கும்போது 'Good morning, Dhanam Teacher!' என்று உற்சாகமாகச் சொல்லலாம்."
            )
        }

        if (lower.contains("good evening") || lower.contains("good afternoon") || lower.contains("மாலை வணக்கம்")) {
            return TutorResponse(
                englishText = "Good evening, Subi! How was school today? Tell Dhanam Teacher one fun thing you learned!",
                tamilText = "மாலை வணக்கம் சுபி! இன்று பள்ளிக்கூடம் எப்படி இருந்தது? இன்று நீ கற்றுக்கொண்ட ஒரு விஷயத்தை தனம் டீச்சரிடம் ஆங்கிலத்தில் சொல்லு!",
                tanglishText = "குட் ஈவ்னிங், சுபி! ஹவ் வாஸ் ஸ்கூல் டுடே? டெல் தனம் டீச்சர் ஒன் ஃபன் திங் யூ லேர்ன்ட்!",
                coachingTip = "சுபி, இன்று நடந்ததைக் கூற Past Tense (உ.ம்: 'I played with my friends', 'I read a story') பயன்படுத்தவும்."
            )
        }

        if (lower.contains("how are you") || lower.contains("how is your day") || lower.contains("epdi irukinga") || lower.contains("எப்படி இருக்க") || lower.contains("நலமா")) {
            return TutorResponse(
                englishText = "I am doing wonderfully well, thank you for asking, Subi! How are you feeling today?",
                tamilText = "நான் மிக நலமாக இருக்கிறேன் சுபி, கேட்டதற்கு நன்றி! இன்று என் சுட்டி மாணவி சுபிக்சா எப்படி இருக்கிறாய்?",
                tanglishText = "ஐ அம் டூயிங் வொண்டர்ஃபுல்லி வெல், தேங்க் யூ ஃபார் ஆஸ்கிங், சுபி! ஹவ் ஆர் யூ ஃபீலிங் டுடே?",
                coachingTip = "சுபி, யாராவது 'How are you?' எனக் கேட்டால், 'I am doing great, thank you!' என அழகாகப் பதில் சொல்லிப் பழகு."
            )
        }

        if (lower.contains("fine") || lower.contains("doing well") || lower.contains("good") || lower.contains("நல்லா இருக்கேன்") || lower.contains("nalla iruken")) {
            return TutorResponse(
                englishText = "That is wonderful to hear, Subi! What topic would you like to learn with Dhanam Teacher right now?",
                tamilText = "கேட்கவே ரொம்ப மகிழ்ச்சியாக இருக்கிறது சுபி! இப்போது தனம் டீச்சருடன் எந்தத் தலைப்பில் ஆங்கிலம் பேசிப் பழகப் போகிறாய்?",
                tanglishText = "தட் இஸ் வொண்டர்ஃபுல் டு ஹியர், சுபி! வாட் டாபிக் வுட் யூ லைக் டு லேர்ன் வித் தனம் டீச்சர் ரைட் நவ்?",
                coachingTip = "'I am good' என்பதை விட 'I am doing great, Teacher!' என்று உற்சாகமாகப் பேசலாம் சுபி."
            )
        }

        if (lower.contains("hello") || lower.contains("hi") || lower.contains("hey") || lower.contains("vanakkam") || lower.contains("வணக்கம்")) {
            return TutorResponse(
                englishText = "Hello Subiksha (Subi)! I am your Dhanam Teacher. How is your day going, dear?",
                tamilText = "வணக்கம் சுபிக்சா (சுபி)! நான் உன் தனம் டீச்சர். இன்றைய நாள் உனக்கு எப்படிப் போகிறது செல்லம்?",
                tanglishText = "ஹலோ சுபிக்சா (சுபி)! ஐ அம் யுவர் தனம் டீச்சர். ஹவ் இஸ் யுவர் டே கோயிங், டியர்?",
                coachingTip = "சுபி, ஒருவரிடம் நலம் விசாரிக்க 'Hello! How are you doing today?' என புன்னகையுடன் கேட்கலாம்."
            )
        }

        if (lower.contains("name") || lower.contains("who are you") || lower.contains("teacher") || lower.contains("dhanam") || lower.contains("தனம்") || lower.contains("டீச்சர்") || lower.contains("பெயர்") || lower.contains("peru") || lower.contains("யார்")) {
            return TutorResponse(
                englishText = "My name is Dhanam, your loving English and Tamil Teacher! And you are my bright 9-year-old student, Subiksha (Subi)!",
                tamilText = "என் பெயர் தனம், உன் அன்பான ஆங்கிலம் மற்றும் தமிழ் டீச்சர்! நீ என் 9 வயது சுட்டி மாணவி சுபிக்சா (சுபி)!",
                tanglishText = "மை நேம் இஸ் தனம், யுவர் லவ்விங் இங்கிலீஷ் அண்ட் தமிழ் டீச்சர்! அண்ட் யூ ஆர் மை பிரைட் நைன் இயர் ஓல்ட் ஸ்டூடண்ட், சுபிக்சா (சுபி)!",
                coachingTip = "உன்னை அறிமுகப்படுத்த 'My name is Subiksha, you can call me Subi. I am 9 years old.' எனத் தெளிவாகச் சொல்லலாம்!"
            )
        }

        if (lower.contains("thank") || lower.contains("நன்றி") || lower.contains("romba nandri")) {
            return TutorResponse(
                englishText = "You are most welcome, Subi dear! Dhanam Teacher is always proud of you.",
                tamilText = "மிக்க மகிழ்ச்சி சுபி செல்லம்! நீ ஆங்கிலம் பேசுவதைப் பார்த்து தனம் டீச்சருக்கு ரொம்பப் பெருமையாக இருக்கிறது.",
                tanglishText = "யூ ஆர் மோஸ்ட் வெல்கம், சுபி டியர்! தனம் டீச்சர் இஸ் ஆல்வேஸ் பிரௌட் ஆஃப் யூ.",
                coachingTip = "சுபி, யாராவது உனக்கு உதவி செய்தால் 'Thank you so much!' என்று சொல்வது நல்ல பழக்கம்."
            )
        }

        if (lower.contains("leave") || lower.contains("விடுமுறை") || lower.contains("school") || lower.contains("பள்ளி") || lower.contains("லீவு")) {
            return TutorResponse(
                englishText = "Subi, at school you can say: 'Excuse me Teacher, may I please take leave tomorrow?'",
                tamilText = "சுபி, பள்ளியில் விடுமுறை கேட்கும்போது: 'எக்ஸ்கியூஸ் மீ டீச்சர், நாளை எனக்கு விடுமுறை தருவீர்களா?' என்று கேட்கலாம்.",
                tanglishText = "சுபி, அட் ஸ்கூல் யூ கேன் சே: எக்ஸ்கியூஸ் மீ டீச்சர், மே ஐ ப்ளீஸ் டேக் லீவ் டுமாரோ?",
                coachingTip = "வகுப்பறையில் டீச்சரிடம் அனுமதி கேட்க எப்போதும் 'May I please...' (உ.ம்: 'May I come in, Teacher?') என்று தொடங்க வேண்டும்."
            )
        }

        if (lower.contains("eat") || lower.contains("food") || lower.contains("சாப்பாடு") || lower.contains("சாப்பிட்டீங்களா") || lower.contains("saptingala") || lower.contains("breakfast") || lower.contains("lunch") || lower.contains("dinner")) {
            return TutorResponse(
                englishText = "Yes Subi, Dhanam Teacher had her meal! Did you finish your food, Subi? What did you eat today?",
                tamilText = "ஆமாம் சுபி, தனம் டீச்சர் சாப்பிட்டுவிட்டேன்! சுபி நீ சாப்பிட்டாயா? இன்று என்ன சாப்பிட்டாய்?",
                tanglishText = "எஸ் சுபி, தனம் டீச்சர் ஹேட் ஹெர் மீல்! டிட் யூ ஃபினிஷ் யுவர் ஃபுட், சுபி? வாட் டிட் யூ ஈட் டுடே?",
                coachingTip = "சுபி, நீ என்ன சாப்பிட்டாய் என்று சொல்ல 'I ate idli and sambar for breakfast' போன்று சொல்லிப் பழகு!"
            )
        }

        if (lower.contains("tea") || lower.contains("coffee") || lower.contains("milk") || lower.contains("பால்") || lower.contains("டீ") || lower.contains("காபி")) {
            return TutorResponse(
                englishText = "Subi dear, drinking a warm glass of milk makes you strong and healthy! Did you drink milk today?",
                tamilText = "சுபி செல்லம், சூடான பால் குடிப்பது உன்னை ஆரோக்கியமாகவும் சுறுசுறுப்பாகவும் வைக்கும்! இன்று பால் குடித்தாயா?",
                tanglishText = "சுபி டியர், டிரிங்கிங் எ வார்ம் கிளாஸ் ஆஃப் மில்க் மேக்ஸ் யூ ஸ்ட்ராங் அண்ட் ஹெல்தி! டிட் யூ டிரிங்க் மில்க் டுடே?",
                coachingTip = "சுபி, அம்மாவிடம் பால் அல்லது தண்ணீர் கேட்க 'Mom, may I have a glass of milk please?' என்று கேட்கலாம்."
            )
        }

        if (lower.contains("play") || lower.contains("game") || lower.contains("friend") || lower.contains("விளையாட்டு") || lower.contains("தோழி")) {
            return TutorResponse(
                englishText = "Subi, playing with friends is so much fun! You can say: 'I love playing with my best friends after school.'",
                tamilText = "சுபி, தோழிகளுடன் விளையாடுவது மிகவும் மகிழ்ச்சியானது! 'நான் பள்ளி முடிந்ததும் என் தோழிகளுடன் விளையாட விரும்புகிறேன்' என்று ஆங்கிலத்தில் சொல்லலாம்.",
                tanglishText = "சுபி, பிளேயிங் வித் ஃப்ரெண்ட்ஸ் இஸ் சோ மச் ஃபன்! ஐ லவ் பிளேயிங் வித் மை பெஸ்ட் ஃப்ரெண்ட்ஸ் ஆஃப்டர் ஸ்கூல்.",
                coachingTip = "உனக்குப் பிடித்த விளையாட்டைச் சொல்ல 'My favorite game is...' என்று தொடங்கிப் பேசு சுபி!"
            )
        }

        if (lower.contains("help") || lower.contains("teach") || lower.contains("english") || lower.contains("உதவி") || lower.contains("ஆங்கிலம்") || lower.contains("சொல்லಿಕொடு") || lower.contains("பேச")) {
            return TutorResponse(
                englishText = "Sure Subi! Dhanam Teacher will teach you step by step. Repeat after me: 'Hello! My name is Subiksha, and I am 9 years old.'",
                tamilText = "நிச்சயமாக சுபி! தனம் டீச்சர் உனக்குப் படிப்படியாகக் கற்றுத் தருகிறேன். என்னைப் பின்பற்றிச் சொல்லு: 'வணக்கம்! என் பெயர் சுபிக்சா, எனக்கு 9 வயது.'",
                tanglishText = "ஷூர் சுபி! தனம் டீச்சர் வில் டீச் யூ ஸ்டெப் பை ஸ்டெப்: 'ஹலோ! மை நேம் இஸ் சுபிக்சா, அண்ட் ஐ அம் நைன் இயர்ஸ் ஓல்ட்.'",
                coachingTip = "சுபி, டீச்சரிடம் சந்தேகம் கேட்க 'Dhanam Teacher, could you please explain this again?' எனக் கேட்கலாம்."
            )
        }

        if (lower.contains("where") || lower.contains("ஊர்") || lower.contains("எங்கே") || lower.contains("place") || lower.contains("location")) {
            return TutorResponse(
                englishText = "Subi, to ask for directions politely, say: 'Excuse me, could you please tell me how to go to the school?'",
                tamilText = "சுபி, மரியாதையாக வழி கேட்க: 'மன்னிக்கவும், பள்ளிக்கு எப்படிச் செல்வது என்று தயவுசெய்து கூற முடியுமா?' என்று கேட்கலாம்.",
                tanglishText = "சுபி, எக்ஸ்கியூஸ் மீ, குட் யூ ப்ளீஸ் டெல் மீ ஹவ் டு கோ டு தி ஸ்கூல்?",
                coachingTip = "வழி கேட்கும்போது 'Excuse me...' என்று தொடங்குவது மிகவும் நல்ல பழக்கம் சுபி."
            )
        }

        if (scenario == "interview") {
            return TutorResponse(
                englishText = "Wonderful, Subi! Tell Dhanam Teacher about your favorite subject at school and why you like it.",
                tamilText = "மிகவும் அருமை சுபி! பள்ளியில் உனக்கு மிகவும் பிடித்த பாடம் எது, ஏன் பிடிக்கும் என்று தனம் டீச்சரிடம் கூறு!",
                tanglishText = "வொண்டர்ஃபுல், சுபி! டெல் தனம் டீச்சர் அபௌட் யுவர் ஃபேவரைட் சப்ஜெக்ட் அட் ஸ்கூல் அண்ட் ஒய் யூ லைக் இட்.",
                coachingTip = "சுபி, 'My favorite subject is English because I love reading stories!' என்று சொல்லிப் பழகலாம்."
            )
        }

        // Dynamic contextual reply acknowledging what Subiksha actually said
        val hasTamilScript = cleanedInput.any { it in '\u0B80'..'\u0BFF' }
        return if (hasTamilScript) {
            TutorResponse(
                englishText = "Subi dear, I heard you say \"$cleanedInput\". Let's practice saying it in English together with Dhanam Teacher!",
                tamilText = "சுபி செல்லம், \"$cleanedInput\" என்று அழகாகச் சொன்னாய்! வா, தனம் டீச்சருடன் சேர்ந்து இதை ஆங்கிலத்தில் பேசிப் பழகலாம்.",
                tanglishText = "சுபி டியர், ஐ ஹேர்ட் யூ சே \"$cleanedInput\". லெட்ஸ் பிராக்டிஸ் சேயிங் இட் இன் இங்கிலீஷ் டுகெதர் வித் தனம் டீச்சர்!",
                coachingTip = "சுபி, தமிழில் நினைக்கும் கருத்தை சிறிய ஆங்கில வாக்கியங்களாக (Subject + Verb + Object) மாற்றிப் பேசிப் பழகு!"
            )
        } else {
            TutorResponse(
                englishText = "Super job, Subi! You said: \"$cleanedInput\" very clearly! Keep going — what else would you like to tell Dhanam Teacher?",
                tamilText = "சூப்பர் சுபிக்சா (சுபி)! \"$cleanedInput\" என்று மிகத் தெளிவாகச் சொன்னாய்! தொடர்ந்து பேசு — தனம் டீச்சரிடம் வேறு என்ன சொல்ல விரும்புகிறாய்?",
                tanglishText = "சூப்பர் ஜாப், சுபி! கீப் கோயிங் — வாட் எல்ஸ் வுட் யூ லைக் டு டெல் தனம் டீச்சர்?",
                coachingTip = "மிக நன்று சுபி! இவ்வாறு முழு வாக்கியங்களாகச் சத்தமாகப் பேசுவது உன் ஆங்கிலத் திறமையை வேகமாக வளர்க்கும்."
            )
        }
    }

    /**
     * Ensures any pronunciation guide is displayed in Tamil script (தமிழ் எழுத்துக்களில் உச்சரிப்பு)
     * rather than broken/misspelled English letters (like "Ai am dooing...").
     */
    fun formatPronunciationInTamil(englishText: String, rawPronunciation: String): String {
        val trimmed = rawPronunciation.trim()
            .replace("மலர்", "தனம்")
            .replace("Malar", "Dhanam", ignoreCase = true)
        if (trimmed.isEmpty()) return ""

        // If it already contains Tamil script characters, return as-is
        if (trimmed.any { it in '\u0B80'..'\u0BFF' }) {
            return trimmed
        }

        // Otherwise, convert known sentences or transliterate English words into Tamil script pronunciation
        val lowerEng = englishText.lowercase()
        when {
            lowerEng.contains("doing wonderfully well") ->
                return "ஐ அம் டூயிங் வொண்டர்ஃபுல்லி வெல், தேங்க் யூ ஃபார் ஆஸ்கிங், சுபி! ஹவ் ஆர் யூ ஃபீலிங் டுடே?"
            lowerEng.contains("vanakkam") && (lowerEng.contains("dhanam") || lowerEng.contains("malar")) ->
                return "வணக்கம் சுபிக்சா (சுபி)! ஐ அம் தனம் டீச்சர், யுவர் இங்கிலீஷ் வாய்ஸ் டீச்சர்."
            lowerEng.contains("hello") && (lowerEng.contains("dhanam") || lowerEng.contains("malar")) ->
                return "ஹலோ சுபிக்சா (சுபி)! ஐ அம் யுவர் தனம் டீச்சர். ஹவ் இஸ் யுவர் டே கோயிங்?"
            lowerEng.contains("software engineer in chennai") ->
                return "ஐ ஒர்க் அஸ் எ சாஃப்ட்வேர் இன்ஜினியர் இன் சென்னை."
            lowerEng.contains("little bit about yourself") ->
                return "குட் யூ டெல் மீ எ லிட்டில் பிட் அபௌட் யுவர்செல்ஃப்?"
        }

        return englishToTamilPronunciation(englishText)
    }

    private val wordPronunciationMap = mapOf(
        "i" to "ஐ", "am" to "அம்", "is" to "இஸ்", "are" to "ஆர்", "was" to "வாஸ்", "were" to "வேர்",
        "you" to "யூ", "your" to "யுவர்", "yours" to "யுவர்ஸ்", "yourself" to "யுவர்செல்ஃப்",
        "we" to "வீ", "they" to "தே", "he" to "ஹீ", "she" to "ஷீ", "it" to "இட்", "my" to "மை", "me" to "மீ",
        "a" to "எ", "an" to "அன்", "the" to "தி", "in" to "இன்", "on" to "ஆன்", "at" to "அட்", "to" to "டு",
        "for" to "ஃபார்", "of" to "ஆஃப்", "with" to "வித்", "from" to "ஃப்ரம்", "by" to "பை", "about" to "அபௌட்",
        "and" to "அண்ட்", "or" to "ஆர்", "but" to "பட்", "so" to "சோ", "if" to "இஃப்",
        "hello" to "ஹலோ", "hi" to "ஹாய்", "hey" to "ஹே", "vanakkam" to "வணக்கம்",
        "dhanam" to "தனம்", "malar" to "தனம்", "teacher" to "டீச்சர்", "subiksha" to "சுபிக்சா", "subi" to "சுபி",
        "student" to "ஸ்டூடண்ட்", "girl" to "கேர்ள்", "nine" to "நைன்", "9" to "நைன்", "years" to "இயர்ஸ்", "old" to "ஓல்ட்",
        "dear" to "டியர்", "school" to "ஸ்கூல்", "super" to "சூப்பர்", "bright" to "பிரைட்", "smart" to "ஸ்மார்ட்",
        "good" to "குட்", "morning" to "மார்னிங்", "afternoon" to "ஆஃப்டர்நூன்", "evening" to "ஈவ்னிங்", "night" to "நைட்",
        "how" to "ஹவ்", "what" to "வாட்", "where" to "வேர்", "when" to "வென்", "why" to "ஒய்", "who" to "ஹூ", "which" to "விச்",
        "doing" to "டூயிங்", "wonderfully" to "வொண்டர்ஃபுல்லி", "wonderful" to "வொண்டர்ஃபுல்", "well" to "வெல்",
        "thank" to "தேங்க்", "thanks" to "தேங்க்ஸ்", "asking" to "ஆஸ்கிங்", "feeling" to "ஃபீலிங்",
        "today" to "டுடே", "tomorrow" to "டுமாரோ", "yesterday" to "எஸ்டர்டே", "day" to "டே", "ahead" to "அஹெட்",
        "going" to "கோயிங்", "go" to "கோ", "went" to "வென்ட்", "come" to "கம்",
        "can" to "கேன்", "could" to "குட்", "would" to "வுட்", "should" to "ஷுட்", "will" to "வில்", "may" to "மே",
        "please" to "ப்ளீஸ்", "tell" to "டெல்", "little" to "லிட்டில்", "bit" to "பிட்",
        "work" to "ஒர்க்", "as" to "அஸ்", "software" to "சாஃப்ட்வேர்", "engineer" to "இன்ஜினியர்", "chennai" to "சென்னை",
        "english" to "இங்கிலீஷ்", "tamil" to "தமிழ்", "voice" to "வாய்ஸ்", "companion" to "கம்பேனியன்",
        "speak" to "ஸ்பீக்", "speaking" to "ஸ்பீக்கிங்", "spoken" to "ஸ்போக்கன்", "talk" to "டாக்",
        "practice" to "பிராக்டிஸ்", "great" to "கிரேட்", "nice" to "நைஸ்", "meet" to "மீட்",
        "have" to "ஹேவ்", "has" to "ஹேஸ்", "had" to "ஹேட்", "do" to "டூ", "does" to "டஸ்", "did" to "டிட்",
        "yes" to "எஸ்", "no" to "நோ", "not" to "நாட்", "don't" to "டோன்ட்", "didn't" to "டிடின்ட்",
        "that" to "தட்", "this" to "திஸ்", "here" to "ஹியர்", "there" to "தேர்", "now" to "நவ்", "right" to "ரைட்",
        "like" to "லைக்", "request" to "ரிக்வெஸ்ட்", "leave" to "லீவ்", "two" to "டூ", "days" to "டேஸ்",
        "some" to "சம்", "personal" to "பர்சனல்", "office" to "ஆபீஸ்", "call" to "கால்", "back" to "பேக்",
        "help" to "ஹெல்ப்", "helping" to "ஹெல்ப்பிங்", "welcome" to "வெல்கம்", "most" to "மோஸ்ட்",
        "always" to "ஆல்வேஸ்", "pleasure" to "பிளெஷர்", "confident" to "கான்ஃபிடன்ட்",
        "tea" to "டீ", "coffee" to "காபி", "food" to "ஃபுட்", "meal" to "மீல்", "eat" to "ஈட்", "yet" to "எட்",
        "bus" to "பஸ்", "train" to "டிரெயின்", "station" to "ஸ்டேஷன்", "nearest" to "நியரஸ்ட்", "get" to "கெட்",
        "keep" to "கீப்", "job" to "ஜாப்", "saying" to "சேயிங்", "else" to "எல்ஸ்", "happened" to "ஹேப்பன்ட்",
        "name" to "நேம்", "coach" to "கோச்", "topic" to "டாபிக்", "hear" to "ஹியர்", "heard" to "ஹேர்ட்",
        "say" to "சே", "polite" to "பொலைட்", "moment" to "மொமெண்ட்", "sure" to "ஷூர்", "let's" to "லெட்ஸ்",
        "step" to "ஸ்டெப்", "repeat" to "ரிப்பீட்", "after" to "ஆஃப்டர்", "slower" to "ஸ்லோவர்"
    )

    private fun englishToTamilPronunciation(sentence: String): String {
        return sentence.split(" ").joinToString(" ") { token ->
            val prefix = token.takeWhile { !it.isLetterOrDigit() && it != '\'' }
            val suffix = token.takeLastWhile { !it.isLetterOrDigit() && it != '\'' }
            val core = token.removePrefix(prefix).removeSuffix(suffix).lowercase()
            val mapped = wordPronunciationMap[core]
            if (mapped != null) {
                "$prefix$mapped$suffix"
            } else {
                token
            }
        }
    }

    suspend fun translateTamilToEnglish(tamilText: String): Map<String, String> = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (_: Exception) { "" }
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = """
A native Tamil speaker wants to say the following in English:
"$tamilText"

Provide 3 natural spoken English variations with 100% accurate English spelling:
1. Casual (for friends and informal chat)
2. Formal (for office, elders, emails, or interviews)
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
                    generationConfig = GenerationConfigPayload(temperature = 0.5f, responseMimeType = "application/json")
                )
                val response = GeminiApiClient.service.generateContent(apiKey, request)
                val raw = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!raw.isNullOrBlank()) {
                    val clean = raw.trim().removeSurrounding("```json", "```").trim()
                    val obj = JSONObject(clean)
                    val formalText = obj.optString("formal")
                    return@withContext mapOf(
                        "casual" to obj.optString("casual"),
                        "formal" to formalText,
                        "short" to obj.optString("short"),
                        "tanglish" to formatPronunciationInTamil(formalText, obj.optString("tanglish")),
                        "tip" to obj.optString("tip")
                    )
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
                tip = "நண்பர்களிடம் 'taking off' என்றும், உயர் அதிகாரிகளிடம் 'request leave' என்றும் சொல்லலாம்."
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
                tip = "'Running late' என்பது தாமதமாக வருவதை குறிக்கும் அருமையான சொற்றொடர்."
            }
            lower.contains("எப்படி சொல்வது") || lower.contains("உதவி") || lower.contains("help") -> {
                casual = "Can you help me with this?"
                formal = "Could you please assist me with this matter?"
                short = "Need a hand here."
                tanglish = "குட் யூ ப்ளீஸ் அசிஸ்ட் மீ வித் திஸ் மேட்டர்?"
                tip = "'Can' என்பதற்கு பதில் 'Could' பயன்படுத்தினால் கூடுதல் மரியாதை கிடைக்கும்."
            }
            else -> {
                casual = "I am trying to say: $tamilText"
                formal = "Could you please help me communicate this properly?"
                short = "Saying this in English."
                tanglish = "குட் யூ ப்ளீஸ் ஹெல்ப் மீ கம்யூனிகேட் திஸ் பிராப்பர்லி?"
                tip = "எப்போதும் எளிய வார்த்தைகளை தெளிவாக உச்சரிப்பது அதிக பலன் தரும்."
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
