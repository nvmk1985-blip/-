package com.example.data.ai

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

/**
 * Live Web Knowledge, Encyclopedia, Dictionary & Bilingual Translation Engine
 * powered by public REST endpoints so that Gemini 3.8 Flash Voice Bot answers
 * ANY question on ANY topic in English and Tamil even when an API key is not configured.
 */
object LiveWebKnowledgeEngine {

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)
            .followRedirects(true)
            .build()
    }

    /**
     * Translates arbitrary text between languages using the public Google GTX endpoint.
     */
    fun translateText(text: String, sourceLang: String = "auto", targetLang: String): String? {
        if (text.isBlank()) return null
        return try {
            val encoded = URLEncoder.encode(text.trim(), "UTF-8")
            val url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=$sourceLang&tl=$targetLang&dt=t&q=$encoded"
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Android; Mobile)")
                .build()
            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return null
                val body = response.body?.string() ?: return null
                val rootArray = JSONArray(body)
                val sentences = rootArray.optJSONArray(0) ?: return null
                val sb = StringBuilder()
                for (i in 0 until sentences.length()) {
                    val seg = sentences.optJSONArray(i)
                    val part = seg?.optString(0)
                    if (!part.isNullOrBlank()) {
                        sb.append(part)
                    }
                }
                sb.toString().trim().takeIf { it.isNotBlank() }
            }
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Extracts the core search topic from a natural Tamil or English question.
     */
    fun extractSearchTopic(rawQuery: String): String {
        var q = rawQuery.trim()
            .replace("?", "")
            .replace("!", "")
            .replace(".", "")

        val tamilRemovals = listOf(
            "என்றால் என்ன", "பற்றி சொல்லுங்கள்", "பற்றி சொல்லு", "பற்றி கூறு", "பற்றி விளக்கு",
            "என்பது என்ன", "யார் இந்த", "என்பவர் யார்", "பற்றி எனக்கு சொல்லு",
            "ஆங்கிலத்தில் எப்படி சொல்வது", "தமிழில் என்ன", "விளக்கம் தா", "விளக்கம் கொடு",
            "எங்கே உள்ளது", "எங்கே இருக்கிறது", "எப்படி வேலை செய்கிறது", "எப்படி உருவாகிறது",
            "எதற்காக", "எப்போது", "யார்", "என்ன", "எப்படி", "ஏன்", "பற்றி"
        )
        for (phrase in tamilRemovals) {
            q = q.replace(phrase, " ")
        }

        val englishRemovals = listOf(
            "(?i)^what is a\\b", "(?i)^what is an\\b", "(?i)^what is the\\b", "(?i)^what is\\b",
            "(?i)^what are the\\b", "(?i)^what are\\b", "(?i)^who is the\\b", "(?i)^who is\\b",
            "(?i)^who was\\b", "(?i)^where is the\\b", "(?i)^where is\\b",
            "(?i)^tell me about the\\b", "(?i)^tell me about\\b", "(?i)^explain about\\b",
            "(?i)^explain the\\b", "(?i)^explain\\b", "(?i)^define\\b",
            "(?i)^give me information about\\b", "(?i)^can you tell me about\\b",
            "(?i)^do you know about\\b", "(?i)^how does\\b", "(?i)^how do\\b",
            "(?i)^why is\\b", "(?i)^why do\\b", "(?i)^why does\\b",
            "(?i)\\bin tamil\\b", "(?i)\\bin english\\b", "(?i)\\bdhanam teacher\\b", "(?i)\\bsubi\\b"
        )
        for (regexStr in englishRemovals) {
            q = q.replace(Regex(regexStr), " ")
        }

        return q.replace(Regex("\\s+"), " ").trim()
    }

    /**
     * Queries Wikipedia (English & Tamil) + Dictionary + Translation to answer ANY question asked by the user.
     */
    fun fetchLiveAnswerForAnyQuestion(
        userQuery: String,
        modelDisplayName: String
    ): TutorResponse? {
        val cleaned = userQuery.trim()
        if (cleaned.isBlank()) return null

        val hasTamil = cleaned.any { it in '\u0B80'..'\u0BFF' }
        val lower = cleaned.lowercase()

        // 1. Check if the user is asking for a translation ("இதை ஆங்கிலத்தில் எப்படி சொல்வது" / "How to say ... in English")
        val isTranslationRequest = lower.contains("எப்படி சொல்வது") ||
            lower.contains("எப்படி சொல்லணும்") ||
            lower.contains("ஆங்கிலத்தில்") ||
            lower.contains("how to say") ||
            lower.contains("translate") ||
            lower.contains("in english")

        if (isTranslationRequest) {
            val phraseToTranslate = cleaned
                .replace("இதை ஆங்கிலத்தில் எப்படி சொல்வது", "")
                .replace("ஆங்கிலத்தில் எப்படி சொல்வது", "")
                .replace("இதை இங்கிலீஷில் எப்படி சொல்வது", "")
                .replace("இங்கிலீஷில் எப்படி சொல்வது", "")
                .replace("எப்படி சொல்வது", "")
                .replace("ஆங்கிலத்தில்", "")
                .replace(Regex("(?i)how to say|in english|translate to english|translate"), "")
                .replace("?", "")
                .trim()
                .ifBlank { cleaned }

            val englishTranslated = translateText(phraseToTranslate, sourceLang = "auto", targetLang = "en")
            val tamilTranslated = translateText(phraseToTranslate, sourceLang = "auto", targetLang = "ta")
            if (!englishTranslated.isNullOrBlank()) {
                val engText = "In English, you can say: \"$englishTranslated\""
                val tamText = "🇮🇳 \"$phraseToTranslate\" என்பதை ஆங்கிலத்தில்:\n\"$englishTranslated\"\nஎன்று தெளிவாகச் சொல்லலாம்! (${tamilTranslated ?: phraseToTranslate})"
                return TutorResponse(
                    englishText = engText,
                    tamilText = tamText,
                    tanglishText = TutorEngine.formatPronunciationInTamil(englishTranslated, ""),
                    coachingTip = "⚡ $modelDisplayName: இந்த வாக்கியத்தை சத்தமாக உச்சரித்துப் பழகுங்கள்!",
                    modelUsed = modelDisplayName
                )
            }
        }

        // 2. Extract topic and search Wikipedia (English & Tamil) for factual knowledge
        val rawTopic = extractSearchTopic(cleaned).ifBlank { cleaned }
        val englishTopic = if (hasTamil) {
            translateText(rawTopic, sourceLang = "ta", targetLang = "en") ?: rawTopic
        } else {
            rawTopic
        }

        // Try English Wikipedia search + summary
        val enWikiSummary = searchAndFetchWikipediaSummary(englishTopic, lang = "en")
            ?: searchAndFetchWikipediaSummary(cleaned, lang = "en")

        // Try Tamil Wikipedia search + summary if query had Tamil or to enrich Tamil explanation
        val taWikiSummary = if (hasTamil) {
            searchAndFetchWikipediaSummary(rawTopic, lang = "ta")
        } else {
            null
        }

        if (!enWikiSummary.isNullOrBlank() || !taWikiSummary.isNullOrBlank()) {
            val conciseEn = if (!enWikiSummary.isNullOrBlank()) {
                takeFirstSentences(enWikiSummary, maxSentences = 3, maxChars = 420)
            } else {
                translateText(
                    takeFirstSentences(taWikiSummary!!, maxSentences = 3, maxChars = 420),
                    sourceLang = "ta",
                    targetLang = "en"
                ) ?: ""
            }

            val conciseTa = if (!taWikiSummary.isNullOrBlank()) {
                takeFirstSentences(taWikiSummary, maxSentences = 3, maxChars = 460)
            } else {
                translateText(conciseEn, sourceLang = "en", targetLang = "ta") ?: ""
            }

            if (conciseEn.isNotBlank()) {
                return TutorResponse(
                    englishText = conciseEn,
                    tamilText = conciseTa.ifBlank { "இதற்கான விளக்கம் மேலே ஆங்கிலத்தில் கொடுக்கப்பட்டுள்ளது." },
                    tanglishText = TutorEngine.formatPronunciationInTamil(conciseEn, ""),
                    coachingTip = "⚡ $modelDisplayName • முக்கியத் தலைப்பு: \"$englishTopic\" — ஆடியோ பட்டனை அழுத்தி தமிழ் & ஆங்கில உச்சரிப்பைக் கேளுங்கள்!",
                    modelUsed = modelDisplayName
                )
            }
        }

        // 3. Try Free English Dictionary API if it's a 1-2 word vocabulary/meaning query
        val words = englishTopic.trim().split(Regex("\\s+"))
        if (words.size <= 2) {
            val dictMeaning = fetchDictionaryDefinition(englishTopic.trim())
            if (!dictMeaning.isNullOrBlank()) {
                val tamilMeaning = translateText(dictMeaning, sourceLang = "en", targetLang = "ta")
                val wordTa = translateText(englishTopic.trim(), sourceLang = "en", targetLang = "ta")
                val engOut = "${englishTopic.replaceFirstChar { it.uppercase() }}: $dictMeaning"
                val tamOut = "\"${englishTopic}\" என்பதன் தமிழ் அர்த்தம்: ${wordTa ?: ""}\nவிளக்கம்: ${tamilMeaning ?: dictMeaning}"
                return TutorResponse(
                    englishText = engOut,
                    tamilText = tamOut,
                    tanglishText = TutorEngine.formatPronunciationInTamil(engOut, ""),
                    coachingTip = "⚡ $modelDisplayName அகராதி: \"${englishTopic}\" என்ற வார்த்தையை தினசரி உரையாடலில் பயன்படுத்திப் பாருங்கள்!",
                    modelUsed = modelDisplayName
                )
            }
        }

        // 4. Universal Bilingual Translation + Conversational Answer for ANY sentence or question
        if (hasTamil) {
            val translatedToEn = translateText(cleaned, sourceLang = "ta", targetLang = "en")
            if (!translatedToEn.isNullOrBlank()) {
                // Also check if the translated English query matches our offline UniversalKnowledgeEngine!
                val offlineMatch = UniversalKnowledgeEngine.answerAnyQuestion(translatedToEn, null)
                if (offlineMatch != null) {
                    return offlineMatch.copy(modelUsed = modelDisplayName)
                }

                // Or try Wikipedia one more time with the full translated question
                val secondTryWiki = searchAndFetchWikipediaSummary(translatedToEn, lang = "en")
                if (!secondTryWiki.isNullOrBlank()) {
                    val shortEn = takeFirstSentences(secondTryWiki, 3, 400)
                    val shortTa = translateText(shortEn, sourceLang = "en", targetLang = "ta") ?: cleaned
                    return TutorResponse(
                        englishText = shortEn,
                        tamilText = shortTa,
                        tanglishText = TutorEngine.formatPronunciationInTamil(shortEn, ""),
                        coachingTip = "⚡ $modelDisplayName: உங்கள் கேள்வியின் ஆங்கில வடிவம்: \"$translatedToEn\"",
                        modelUsed = modelDisplayName
                    )
                }

                return TutorResponse(
                    englishText = "In English: \"$translatedToEn\"",
                    tamilText = "நீங்கள் கேட்ட \"$cleaned\" என்பதை ஆங்கிலத்தில்:\n\"$translatedToEn\"\nஎன்று தெளிவாகக் கூறலாம்!",
                    tanglishText = TutorEngine.formatPronunciationInTamil(translatedToEn, ""),
                    coachingTip = "⚡ $modelDisplayName: ஆங்கில உச்சரிப்பைக் கேட்டுத் திரும்பப் பேசிப் பயிற்சி செய்யுங்கள்!",
                    modelUsed = modelDisplayName
                )
            }
        } else {
            val translatedToTa = translateText(cleaned, sourceLang = "en", targetLang = "ta")
            if (!translatedToTa.isNullOrBlank()) {
                return TutorResponse(
                    englishText = cleaned.replaceFirstChar { it.uppercase() },
                    tamilText = "தமிழ் அர்த்தம் மற்றும் விளக்கம்:\n\"$translatedToTa\"",
                    tanglishText = TutorEngine.formatPronunciationInTamil(cleaned, ""),
                    coachingTip = "⚡ $modelDisplayName: ஆங்கில வாக்கியத்தின் தமிழ் அர்த்தம் மற்றும் உச்சரிப்பு மேலே கொடுக்கப்பட்டுள்ளது!",
                    modelUsed = modelDisplayName
                )
            }
        }

        return null
    }

    private fun searchAndFetchWikipediaSummary(query: String, lang: String = "en"): String? {
        if (query.isBlank()) return null
        return try {
            val encodedQuery = URLEncoder.encode(query.trim(), "UTF-8")
            val searchUrl = "https://$lang.wikipedia.org/w/api.php?action=query&list=search&srsearch=$encodedQuery&utf8=1&format=json&srlimit=1"
            val searchReq = Request.Builder()
                .url(searchUrl)
                .header("User-Agent", "PesalamAI/2.0 (Android Educational Tutor)")
                .build()

            val bestTitle = httpClient.newCall(searchReq).execute().use { resp ->
                if (!resp.isSuccessful) return@use null
                val body = resp.body?.string() ?: return@use null
                val json = JSONObject(body)
                val searchArr = json.optJSONObject("query")?.optJSONArray("search")
                if (searchArr != null && searchArr.length() > 0) {
                    searchArr.optJSONObject(0)?.optString("title")
                } else {
                    null
                }
            } ?: return null

            val encodedTitle = URLEncoder.encode(bestTitle.replace(" ", "_"), "UTF-8")
            val summaryUrl = "https://$lang.wikipedia.org/api/rest_v1/page/summary/$encodedTitle"
            val summaryReq = Request.Builder()
                .url(summaryUrl)
                .header("User-Agent", "PesalamAI/2.0 (Android Educational Tutor)")
                .build()

            httpClient.newCall(summaryReq).execute().use { resp ->
                if (!resp.isSuccessful) return null
                val body = resp.body?.string() ?: return null
                val json = JSONObject(body)
                val extract = json.optString("extract").trim()
                if (extract.length > 25 && !extract.contains("may refer to:")) {
                    extract
                } else {
                    null
                }
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun fetchDictionaryDefinition(word: String): String? {
        val cleanWord = word.lowercase().replace(Regex("[^a-z]"), "")
        if (cleanWord.length < 2) return null
        return try {
            val url = "https://api.dictionaryapi.dev/api/v2/entries/en/$cleanWord"
            val req = Request.Builder()
                .url(url)
                .header("User-Agent", "PesalamAI/2.0")
                .build()
            httpClient.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return null
                val body = resp.body?.string() ?: return null
                val arr = JSONArray(body)
                val firstObj = arr.optJSONObject(0) ?: return null
                val meanings = firstObj.optJSONArray("meanings") ?: return null
                val firstMeaning = meanings.optJSONObject(0) ?: return null
                val partOfSpeech = firstMeaning.optString("partOfSpeech")
                val defs = firstMeaning.optJSONArray("definitions") ?: return null
                val firstDef = defs.optJSONObject(0) ?: return null
                val definition = firstDef.optString("definition")
                val example = firstDef.optString("example")
                if (definition.isNotBlank()) {
                    if (example.isNotBlank()) {
                        "($partOfSpeech) $definition Example: \"$example\""
                    } else {
                        "($partOfSpeech) $definition"
                    }
                } else {
                    null
                }
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun takeFirstSentences(text: String, maxSentences: Int, maxChars: Int): String {
        val clean = text.replace(Regex("\\s+"), " ").trim()
        val parts = clean.split(Regex("(?<=[.!?।])\\s+"))
        val sb = StringBuilder()
        var count = 0
        for (part in parts) {
            if (part.isBlank()) continue
            if (sb.length + part.length > maxChars && count > 0) break
            if (sb.isNotEmpty()) sb.append(" ")
            sb.append(part)
            count++
            if (count >= maxSentences) break
        }
        val result = sb.toString().trim()
        return if (result.isNotBlank()) result else clean.take(maxChars)
    }
}
