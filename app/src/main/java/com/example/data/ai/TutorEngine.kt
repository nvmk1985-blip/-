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

    private const val SYSTEM_PROMPT = """
You are 'Malar' (மலர்), a patient, encouraging, and friendly Spoken English voice tutor for native Tamil speakers.
Your goal is to help them speak fluent, confident English without fear.
Whenever the user speaks to you (in English, Tamil, or Tanglish):
1. Give a natural, conversational English response (keep it simple, 1-3 sentences).
2. Give an accurate Tamil explanation & translation in Tamil script.
3. Provide a Tanglish phonetic pronunciation helper for the English sentence.
4. Give a practical Tamil coaching tip (e.g. grammar correction, vocabulary nuance, or confidence booster).

Respond strictly in valid JSON format:
{
  "english": "English reply here",
  "tamil": "தமிழ் விளக்கம் மற்றும் அர்த்தம் இங்கே",
  "tanglish": "Tanglish pronunciation helper here",
  "tip": "ஆங்கிலத்தில் பேசும்போது கவனிக்க வேண்டிய குறிப்பு இங்கே"
}
"""

    suspend fun getTutorReply(userMessage: String, contextScenario: String? = null): TutorResponse = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (_: Exception) { "" }
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val promptText = if (contextScenario != null) {
                    "Scenario: $contextScenario\nUser said: $userMessage"
                } else {
                    "User said: $userMessage"
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
            TutorResponse(
                englishText = obj.optString("english", "Hello! Great to hear from you."),
                tamilText = obj.optString("tamil", "வணக்கம்! உங்களுடன் பேசுவதில் மகிழ்ச்சி."),
                tanglishText = obj.optString("tanglish", "Hellow! Greyt tu heer phram yu."),
                coachingTip = obj.optString("tip", "தினமும் பேசி பழகும்போது தன்னம்பிக்கை அதிகரிக்கும்!")
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun getLocalTutorReply(input: String, scenario: String?): TutorResponse {
        val cleanedInput = input.trim()
        val lower = cleanedInput.lowercase()

        if (lower.contains("good morning") || lower.contains("காலை வணக்கம்")) {
            return TutorResponse(
                englishText = "Good morning! I hope you have a productive and wonderful day ahead. What are your plans today?",
                tamilText = "காலை வணக்கம்! உங்களின் இந்த நாள் மிகவும் சிறப்பாக அமைய வாழ்த்துகள். இன்று உங்களின் திட்டம் என்ன?",
                tanglishText = "Gud maarning! Ai hop yu haev a pradaktiv and vandarphul dey ahed.",
                coachingTip = "'Good morning' சொன்ன பிறகு 'Hope you have a great day!' என்று சேர்த்துச் சொன்னால் உரையாடல் இனிமையாகத் தொடங்கும்."
            )
        }

        if (lower.contains("good evening") || lower.contains("good afternoon") || lower.contains("மாலை வணக்கம்")) {
            return TutorResponse(
                englishText = "Good evening! How was your day today? Tell me one thing you did in English!",
                tamilText = "மாலை வணக்கம்! இன்று உங்கள் நாள் எப்படி இருந்தது? இன்று நீங்கள் செய்த ஒரு விஷயத்தை ஆங்கிலத்தில் கூறுங்கள்!",
                tanglishText = "Gud eevning! How vaaz yor dey tudey? Tel mi van thing yu did in Inglish!",
                coachingTip = "கடந்த காலத்தில் நடந்ததைக் கூற Past Tense (உ.ம்: 'I went to work', 'I met my friend') பயன்படுத்தவும்."
            )
        }

        if (lower.contains("how are you") || lower.contains("how is your day") || lower.contains("epdi irukinga") || lower.contains("எப்படி இருக்க") || lower.contains("நலமா")) {
            return TutorResponse(
                englishText = "I am doing wonderfully well, thank you for asking! How are you feeling today?",
                tamilText = "நான் மிகச் சிறப்பாக இருக்கிறேன், கேட்டதற்கு நன்றி! இன்று நீங்கள் எப்படி உணர்கிறீர்கள்?",
                tanglishText = "Ai am dooing vandarphulli vel, thaenk yu phor aasking! How aar yu pheeling tudey?",
                coachingTip = "யாராவது 'How are you?' எனக் கேட்டால், 'I am fine' என்பதற்கு பதிலாக 'I'm doing great, thank you!' என கூறிப் பாருங்கள்."
            )
        }

        if (lower.contains("fine") || lower.contains("doing well") || lower.contains("good") || lower.contains("நல்லா இருக்கேன்") || lower.contains("nalla iruken")) {
            return TutorResponse(
                englishText = "That is wonderful to hear! What topic would you like to practice speaking about right now?",
                tamilText = "கேட்கவே மகிழ்ச்சியாக உள்ளது! இப்போது எந்த தலைப்பில் ஆங்கிலம் பேசிப் பழக விரும்புகிறீர்கள்?",
                tanglishText = "Thaat iz vandarphul tu heer! Vaat taapik vud yu laik tu praaktis speeking abowt rait now?",
                coachingTip = "'I am good' என்பதை விட 'I am doing great!' அல்லது 'Pretty good, thanks!' என்று இயல்பாகப் பேசலாம்."
            )
        }

        if (lower.contains("hello") || lower.contains("hi") || lower.contains("hey") || lower.contains("vanakkam") || lower.contains("வணக்கம்")) {
            return TutorResponse(
                englishText = "Hello! I am Malar, your English companion. How is your day going?",
                tamilText = "வணக்கம்! நான் மலர், உங்கள் ஆங்கிலத் தோழன். இன்றைய நாள் உங்களுக்கு எப்படி போகிறது?",
                tanglishText = "Hellow! Ai am Malar, yor Inglish kampaanyan. How iz yor dey goying?",
                coachingTip = "ஒருவரிடம் நலம் விசாரிக்க 'How is your day going?' அல்லது 'How are you doing?' என கேட்கலாம்."
            )
        }

        if (lower.contains("name") || lower.contains("who are you") || lower.contains("பெயர்") || lower.contains("peru") || lower.contains("யார்")) {
            return TutorResponse(
                englishText = "My name is Malar, your spoken English coach! How may I call you?",
                tamilText = "என் பெயர் மலர், உங்களின் ஆங்கிலப் பயிற்சித் தோழி! உங்களின் பெயர் என்ன?",
                tanglishText = "Mai neym iz Malar, yor spoken Inglish koch! How mey ai kaal yu?",
                coachingTip = "உங்களை அறிமுகப்படுத்த 'My name is...' அல்லது 'I am...' எனத் தெளிவாகத் தொடங்கலாம்."
            )
        }

        if (lower.contains("thank") || lower.contains("நன்றி") || lower.contains("romba nandri")) {
            return TutorResponse(
                englishText = "You are most welcome! It is always a pleasure helping you speak confident English.",
                tamilText = "மகிழ்ச்சி! நீங்கள் தன்னம்பிக்கையுடன் ஆங்கிலம் பேச உதவுவது எனக்கு எப்போதும் மகிழ்ச்சியே.",
                tanglishText = "Yu aar most velkam! It iz aalveyz a pleshar helping yu speek kaanphident Inglish.",
                coachingTip = "யாராவது Thank you சொன்னால் 'You're welcome' அல்லது 'Happy to help!' என்று பதில் அளிக்கலாம்."
            )
        }

        if (lower.contains("leave") || lower.contains("விடுமுறை") || lower.contains("office") || lower.contains("work") || lower.contains("லீவு")) {
            return TutorResponse(
                englishText = "I would like to request leave for two days due to some personal work.",
                tamilText = "எனக்கு சில சொந்த வேலைகள் இருப்பதால் இரண்டு நாட்கள் விடுமுறை தேவைப்படுகிறது.",
                tanglishText = "Ai vud laik tu rikvest leev phor tu deyz dyoo tu sam parsonal vork.",
                coachingTip = "அலுவலகத்தில் விடுமுறை கேட்கும்போது 'I want leave' என்பதற்கு பதிலாக 'I would like to request leave' என்பது மிகவும் கண்ணியமானது."
            )
        }

        if (lower.contains("eat") || lower.contains("food") || lower.contains("சாப்பாடு") || lower.contains("சாப்பிட்டீங்களா") || lower.contains("saptingala") || lower.contains("breakfast") || lower.contains("lunch") || lower.contains("dinner")) {
            return TutorResponse(
                englishText = "Yes, I had my meal. Have you had your food yet? What did you eat?",
                tamilText = "ஆம், நான் சாப்பிட்டுவிட்டேன். நீங்கள் சாப்பிட்டீர்களா? என்ன சாப்பிட்டீர்கள்?",
                tanglishText = "Yes, ai haed mai meel. Haev yu haed yor phood yet? Vaat did yu eet?",
                coachingTip = "'Did you have lunch?' அல்லது 'Have you had your meal?' எனக் கேட்க வேண்டும். 'Did you had' என சொல்லக்கூடாது."
            )
        }

        if (lower.contains("tea") || lower.contains("coffee") || lower.contains("டீ") || lower.contains("காபி")) {
            return TutorResponse(
                englishText = "Would you like a hot cup of tea or filter coffee? Let's take a quick break!",
                tamilText = "சூடான டீ அல்லது ஃபில்டர் காபி குடிக்கிறீர்களா? ஒரு சிறிய இடைவேளை எடுப்போம்!",
                tanglishText = "Vud yu laik a haat kap aav tee oor philtar kaaphi? Lets teyk a kvik breyk!",
                coachingTip = "விருந்தினருக்கு உபசரிக்க 'Do you want tea?' என்பதை விட 'Would you like some tea?' என்பது மரியாதையானது."
            )
        }

        if (lower.contains("job") || lower.contains("interview") || lower.contains("வேலை") || lower.contains("career")) {
            return TutorResponse(
                englishText = "I am looking for new career opportunities where I can utilize my skills and grow.",
                tamilText = "எனது திறமைகளைப் பயன்படுத்தி வளரக்கூடிய புதிய வேலை வாய்ப்புகளைத் தேடிக்கொண்டிருக்கிறேன்.",
                tanglishText = "Ai am looking phor nyoo kareeyar aapportyooniteez ver ai kaen yootilaiz mai skilz.",
                coachingTip = "'Job search' என்பதற்கு பதிலாக 'Looking for career opportunities' என்று சொன்னால் தொழில்முறை கம்பீரம் இருக்கும்."
            )
        }

        if (lower.contains("help") || lower.contains("teach") || lower.contains("english") || lower.contains("உதவி") || lower.contains("ஆங்கிலம்") || lower.contains("சொல்லಿಕொடு") || lower.contains("பேச")) {
            return TutorResponse(
                englishText = "Sure! Let's practice step by step. Repeat after me: 'Could you please speak a little slower?'",
                tamilText = "நிச்சயமாக! படிப்படியாகப் பயிற்சி செய்வோம். என்னைப் பின்பற்றிச் சொல்லுங்கள்: 'தயவுசெய்து சற்று மெதுவாகப் பேசுகிறீர்களா?'",
                tanglishText = "Kud yu pleez speek a litil slovar?",
                coachingTip = "மற்றவர் வேகமாக ஆங்கிலம் பேசும்போது தயங்காமல் 'Could you please speak a bit slower?' எனக் கேட்கலாம்."
            )
        }

        if (lower.contains("where") || lower.contains("ஊர்") || lower.contains("எங்கே") || lower.contains("place") || lower.contains("location")) {
            return TutorResponse(
                englishText = "Could you please tell me how to get to the nearest bus station from here?",
                tamilText = "இங்கிருந்து அருகிலுள்ள பேருந்து நிலையத்திற்கு எப்படிச் செல்வது என்று தயவுசெய்து கூற முடியுமா?",
                tanglishText = "Kud yu pleez tel mi how tu get tu dhi neeyarest bas steyshan phram heer?",
                coachingTip = "வழி கேட்கும்போது 'Excuse me, could you tell me how to get to...' என்று தொடங்குவது மிகவும் நாகரிகமானது."
            )
        }

        if (scenario == "interview") {
            return TutorResponse(
                englishText = "That's impressive. Could you tell me about a challenging situation you handled recently?",
                tamilText = "மிகவும் அற்புதம். சமீபத்தில் நீங்கள் கையாண்ட சவாலான சூழ்நிலையை பற்றி கூற முடியுமா?",
                tanglishText = "Thaats impresiv. Kud yu tel mi abowt a chaalenjing sichuveshan yu haandild reesentli?",
                coachingTip = "சவால்களை விளக்கும் போது STAR முறை (Situation, Task, Action, Result) பயன்படுத்துங்கள்."
            )
        }

        // Dynamic contextual reply acknowledging what the user actually said
        val hasTamilScript = cleanedInput.any { it in '\u0B80'..'\u0BFF' }
        return if (hasTamilScript) {
            TutorResponse(
                englishText = "I heard you say \"$cleanedInput\". In polite English, you can say: \"Could we talk about this for a moment?\"",
                tamilText = "\"$cleanedInput\" என்று அழகாகக் கூறினீர்கள்! தொடர்ந்து என்னுடன் ஆங்கிலத்தில் பேசிப் பழகுங்கள்.",
                tanglishText = "Kud vee taak abowt dhis phor a moment?",
                coachingTip = "தமிழில் நினைக்கும் கருத்தை சிறிய ஆங்கில வாக்கியங்களாக (Subject + Verb + Object) மாற்றிப் பேசிப் பழகுங்கள்."
            )
        } else {
            TutorResponse(
                englishText = "Great job saying: \"$cleanedInput\"! Keep going — what else happened today?",
                tamilText = "\"$cleanedInput\" என்று மிகத் தெளிவாகச் சொன்னீர்கள்! தொடர்ந்து பேசுங்கள் — இன்று வேறு என்ன நடந்தது?",
                tanglishText = "Greyt jaab seying: \"$cleanedInput\"! Keep goying — vaat els haepend tudey?",
                coachingTip = "மிக நன்று! இவ்வாறு முழு வாக்கியங்களாகச் சத்தமாகப் பேசுவது உங்கள் ஆங்கிலத் தயக்கத்தை விரைவில் போக்கும்."
            )
        }
    }

    suspend fun translateTamilToEnglish(tamilText: String): Map<String, String> = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (_: Exception) { "" }
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = """
A native Tamil speaker wants to say the following in English:
"$tamilText"

Provide 3 natural spoken English variations:
1. Casual (for friends and informal chat)
2. Formal (for office, elders, emails, or interviews)
3. Short (quick, direct daily phrase)
Include pronunciation in Tanglish for the formal variation.

Output strictly valid JSON:
{
  "casual": "Casual English phrase",
  "formal": "Polite formal English phrase",
  "short": "Short direct phrase",
  "tanglish": "Pronunciation in Tanglish",
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
                    return@withContext mapOf(
                        "casual" to obj.optString("casual"),
                        "formal" to obj.optString("formal"),
                        "short" to obj.optString("short"),
                        "tanglish" to obj.optString("tanglish"),
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
                tanglish = "Ai vud laik tu rikvest leev phor tumaarro, pleez."
                tip = "நண்பர்களிடம் 'taking off' என்றும், உயர் அதிகாரிகளிடம் 'request leave' என்றும் சொல்லலாம்."
            }
            lower.contains("சாப்பிட்டீங்களா") || lower.contains("saptingala") || lower.contains("food") -> {
                casual = "Did you eat?"
                formal = "Have you had your meal yet?"
                short = "Had food?"
                tanglish = "Haev yu haed yor meel yet?"
                tip = "மரியாதையான ஆங்கிலத்தில் 'Have you had...' என்று கேட்பது மிகவும் இயல்பானது."
            }
            lower.contains("நேரமாச்சு") || lower.contains("late") || lower.contains("தாமதம்") -> {
                casual = "Running a bit late, see you soon!"
                formal = "Please excuse my delay, I will be arriving shortly."
                short = "I'm running late."
                tanglish = "Pleez ekskyooz mai diley, ai vil bee araiving shaartli."
                tip = "'Running late' என்பது தாமதமாக வருவதை குறிக்கும் அருமையான சொற்றொடர்."
            }
            lower.contains("எப்படி சொல்வது") || lower.contains("உதவி") || lower.contains("help") -> {
                casual = "Can you help me with this?"
                formal = "Could you please assist me with this matter?"
                short = "Need a hand here."
                tanglish = "Kud yu pleez asist mi vidh this maettar?"
                tip = "'Can' என்பதற்கு பதில் 'Could' பயன்படுத்தினால் கூடுதல் மரியாதை கிடைக்கும்."
            }
            else -> {
                casual = "I am trying to say: $tamilText"
                formal = "Could you please help me communicate this properly?"
                short = "Saying this in English."
                tanglish = "Kud yu pleez help mi kamyoonikeyt this praaparli?"
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
