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
        val lower = input.lowercase().trim()

        if (lower.contains("hello") || lower.contains("hi") || lower.contains("vanakkam") || lower.contains("வணக்கம்")) {
            return TutorResponse(
                englishText = "Hello! I am Malar, your English companion. How is your day going?",
                tamilText = "வணக்கம்! நான் மலர், உங்கள் ஆங்கிலத் தோழன். இன்றைய நாள் உங்களுக்கு எப்படி போகிறது?",
                tanglishText = "Hellow! Ai am Malar, yor Inglish kampaanyan. How iz yor dey goying?",
                coachingTip = "ஒருவரிடம் நலம் விசாரிக்க 'How is your day going?' அல்லது 'How are you doing?' என கேட்கலாம்."
            )
        }

        if (lower.contains("name") || lower.contains("பெயர்") || lower.contains("peru")) {
            return TutorResponse(
                englishText = "My name is Malar! What can I help you practice speaking today?",
                tamilText = "என் பெயர் மலர்! இன்று என்ன விஷயத்தை பேசி பழக விரும்புகிறீர்கள்?",
                tanglishText = "Mai neym iz Malar! Vaat kaen ai help yu praaktis speeking tudey?",
                coachingTip = "'My name is...' என முழு வாக்கியமாக அறிமுகம் செய்வது சிறந்த வழக்கமாகும்."
            )
        }

        if (lower.contains("leave") || lower.contains("விடுமுறை") || lower.contains("office") || lower.contains("work")) {
            return TutorResponse(
                englishText = "I would like to request leave for two days due to some personal work.",
                tamilText = "எனக்கு சில சொந்த வேலைகள் இருப்பதால் இரண்டு நாட்கள் விடுமுறை தேவைப்படுகிறது.",
                tanglishText = "Ai vud laik tu rikvest leev phor tu deyz dyoo tu sam parsonal vork.",
                coachingTip = "அலுவலகத்தில் விடுமுறை கேட்கும்போது 'I want leave' என்பதற்கு பதிலாக 'I would like to request leave' என்பது மிகவும் கண்ணியமானது."
            )
        }

        if (lower.contains("eat") || lower.contains("food") || lower.contains("சாப்பாடு") || lower.contains("saptingala") || lower.contains("breakfast")) {
            return TutorResponse(
                englishText = "Yes, I had my breakfast. Have you had your food yet?",
                tamilText = "ஆம், நான் காலை உணவு சாப்பிட்டுவிட்டேன். நீங்கள் சாப்பிட்டீர்களா?",
                tanglishText = "Yes, ai haed mai brekphaast. Haev yu haed yor phood yet?",
                coachingTip = "'Did you have breakfast?' அல்லது 'Have you had breakfast?' எனக் கேட்க வேண்டும். 'Did you had' என சொல்லக்கூடாது."
            )
        }

        if (lower.contains("tea") || lower.contains("coffee") || lower.contains("டீ") || lower.contains("காபி")) {
            return TutorResponse(
                englishText = "Would you like a hot cup of tea or filter coffee?",
                tamilText = "சூடான டீ அல்லது ஃபில்டர் காபி குடிக்கிறீர்களா?",
                tanglishText = "Vud yu laik a haat kap aav tee oor philtar kaaphi?",
                coachingTip = "விருந்தினருக்கு உபசரிக்க 'Do you want tea?' என்பதை விட 'Would you like some tea?' என்பது மரியாதையானது."
            )
        }

        if (lower.contains("how are you") || lower.contains("epdi irukinga") || lower.contains("எப்படி இருக்கீங்க")) {
            return TutorResponse(
                englishText = "I am doing wonderfully well, thank you! How about you?",
                tamilText = "நான் மிகச் சிறப்பாக இருக்கிறேன், நன்றி! நீங்கள் எப்படி இருக்கிறீர்கள்?",
                tanglishText = "Ai am dooing vandarphulli vel, thaenk yu! How abowt yu?",
                coachingTip = "யாராவது 'How are you?' எனக் கேட்டால், 'I am fine' என்பதற்கு பதிலாக 'I'm doing well, thank you!' என கூறி பாருங்கள்."
            )
        }

        if (lower.contains("job") || lower.contains("interview") || lower.contains("வேலை")) {
            return TutorResponse(
                englishText = "I am looking for new career opportunities where I can utilize my skills.",
                tamilText = "எனது திறமைகளை பயன்படுத்தக்கூடிய புதிய வேலை வாய்ப்புகளை தேடிக்கொண்டிருக்கிறேன்.",
                tanglishText = "Ai am looking phor nyoo kareeyar aapportyooniteez ver ai kaen yootilaiz mai skilz.",
                coachingTip = "'Job search' என்பதற்கு பதிலாக 'Looking for career opportunities' என்று சொன்னால் தொழில்முறை கம்பீரம் இருக்கும்."
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

        // Generic encouraging conversation reply
        return TutorResponse(
            englishText = "You expressed that nicely! Try saying: 'I am practicing English every day to improve my fluency.'",
            tamilText = "அழகாக பேசினீர்கள்! 'ஆங்கிலத்தில் சரளமாக பேச நான் தினமும் பயிற்சி செய்கிறேன்' என சொல்லி பழகுங்கள்.",
            tanglishText = "Ai am praaktising Inglish evridey tu improov mai phlooansi.",
            coachingTip = "தவறு செய்தாலும் கவலைப்பட வேண்டாம்! தொடர்ந்து சத்தமாக பேசுவதே சரளமான ஆங்கிலத்திற்கான எளிய வழி."
        )
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
