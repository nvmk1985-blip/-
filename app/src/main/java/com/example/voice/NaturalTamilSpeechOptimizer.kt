package com.example.voice

/**
 * Optimizes text for natural, human-like Spoken Tamil (பேச்சுத் தமிழ்) and Bilingual TTS speech synthesis.
 * Specially calibrated for 5 Female Tamil Dubbing Artist voices (டப்பிங் கலைஞர் குரல்கள்).
 *
 * Solves robotic TTS artifacts by:
 * 1. Converting rigid bookish/written Tamil (எழுத்துத் தமிழ்) into natural conversational Spoken Tamil (பேச்சுத் தமிழ்).
 * 2. Expanding arithmetic symbols and math expressions into natural spoken Tamil words.
 * 3. Phoneticizing English loanwords and acronyms seamlessly into native Tamil script.
 * 4. Smoothing punctuation, pauses, and cadence so speech sounds melodious, expressive, and human-like.
 */
object NaturalTamilSpeechOptimizer {

    fun optimizeForTamilSpeech(rawText: String, tone: VoiceTone? = null): String {
        var text = rawText

        // 1. Clean markdown, formatting artifacts, and special delimiters
        text = text
            .replace(Regex("[*#_~`|\\[\\]{}]"), " ")
            .replace("•", ", ")
            .replace("→", " ")
            .replace("—", ", ")
            .replace("--", ", ")
            .replace(":", ": ")

        // 2. Expand Math and Arithmetic symbols into natural spoken Tamil words
        text = text
            .replace("+", " கூட்டல் ")
            .replace("−", " கழித்தல் ")
            .replace("- ", " கழித்தல் ")
            .replace("×", " பெருக்கல் ")
            .replace("*", " பெருக்கல் ")
            .replace("÷", " வகுத்தல் ")
            .replace("/", " வகுத்தல் ")
            .replace("=", " சமம் ")
            .replace("%", " சதவீதம் ")
            .replace("^2", " வர்க்கம் ")
            .replace("²", " வர்க்கம் ")
            .replace("³", " கனம் ")
            .replace("√", " வர்க்கமூலம் ")
            .replace(">", " விடப் பெரியது ")
            .replace("<", " விடச் சிறியது ")
            .replace("₹", " ரூபாய் ")
            .replace("$", " டாலர் ")

        // 3. Conversational Spoken Tamil (பேச்சுத் தமிழ்) conversions
        // Transforms formal bookish written verbs into fluid, natural spoken Tamil
        text = convertToSpokenTamil(text)

        // 4. Phoneticize common English words and conversational terms embedded in Tamil text
        val phoneticReplacements = listOf(
            Regex("\\bAI\\b", RegexOption.IGNORE_CASE) to "ஏஐ",
            Regex("\\bApp\\b", RegexOption.IGNORE_CASE) to "ஆப்",
            Regex("\\bGemini\\b", RegexOption.IGNORE_CASE) to "ஜெமினி",
            Regex("\\bFlash\\b", RegexOption.IGNORE_CASE) to "ஃபிளாஷ்",
            Regex("\\bPDF\\b", RegexOption.IGNORE_CASE) to "பிடிஎஃப்",
            Regex("\\bSubi\\b", RegexOption.IGNORE_CASE) to "சுபி",
            Regex("\\bSubiksha\\b", RegexOption.IGNORE_CASE) to "சுபிக்சா",
            Regex("\\bDhanam\\b", RegexOption.IGNORE_CASE) to "தனம்",
            Regex("\\bTeacher\\b", RegexOption.IGNORE_CASE) to "டீச்சர்",
            Regex("\\bNila\\b", RegexOption.IGNORE_CASE) to "நிலா",
            Regex("\\bAnanya\\b", RegexOption.IGNORE_CASE) to "அனன்யா",
            Regex("\\bKavitha\\b", RegexOption.IGNORE_CASE) to "கவிதா",
            Regex("\\bAkka\\b", RegexOption.IGNORE_CASE) to "அக்கா",
            Regex("\\bDubbing\\b", RegexOption.IGNORE_CASE) to "டப்பிங்",
            Regex("\\bArtist\\b", RegexOption.IGNORE_CASE) to "கலைஞர்",
            Regex("\\bEnglish\\b", RegexOption.IGNORE_CASE) to "ஆங்கிலம்",
            Regex("\\bTamil\\b", RegexOption.IGNORE_CASE) to "தமிழ்",
            Regex("\\bBot\\b", RegexOption.IGNORE_CASE) to "பாட்",
            Regex("\\bVoice\\b", RegexOption.IGNORE_CASE) to "குரல்",
            Regex("\\bCall\\b", RegexOption.IGNORE_CASE) to "அழைப்பு",
            Regex("\\bLesson\\b", RegexOption.IGNORE_CASE) to "பாடம்",
            Regex("\\bQuiz\\b", RegexOption.IGNORE_CASE) to "வினாடி வினா",
            Regex("\\bGrammar\\b", RegexOption.IGNORE_CASE) to "இலக்கணம்",
            Regex("\\bMath\\b", RegexOption.IGNORE_CASE) to "கணிதம்",
            Regex("\\bMental\\s+Math\\b", RegexOption.IGNORE_CASE) to "மனக் கணக்கு",
            Regex("\\bSuper\\b", RegexOption.IGNORE_CASE) to "சூப்பர்",
            Regex("\\bOK\\b", RegexOption.IGNORE_CASE) to "சரி",
            Regex("\\bOkay\\b", RegexOption.IGNORE_CASE) to "சரி",
            Regex("\\bHi\\b", RegexOption.IGNORE_CASE) to "ஹாய்",
            Regex("\\bHello\\b", RegexOption.IGNORE_CASE) to "ஹலோ",
            Regex("\\bWelcome\\b", RegexOption.IGNORE_CASE) to "நல்வரவு",
            Regex("\\bGood\\b", RegexOption.IGNORE_CASE) to "நல்லது",
            Regex("\\bGreat\\b", RegexOption.IGNORE_CASE) to "அருமை",
            Regex("\\bWonderful\\b", RegexOption.IGNORE_CASE) to "அற்புதம்",
            Regex("\\bExcellent\\b", RegexOption.IGNORE_CASE) to "மிக நன்று",
            Regex("\\bReady\\b", RegexOption.IGNORE_CASE) to "தயாரா",
            Regex("\\bStep\\b", RegexOption.IGNORE_CASE) to "படி",
            Regex("\\bTrick\\b", RegexOption.IGNORE_CASE) to "சுருக்கு வழி",
            Regex("\\bTip\\b", RegexOption.IGNORE_CASE) to "குறிப்பு",
            Regex("\\bScore\\b", RegexOption.IGNORE_CASE) to "மதிப்பெண்",
            Regex("\\bStreak\\b", RegexOption.IGNORE_CASE) to "தொடர் வெற்றி",
            Regex("\\bSeconds\\b", RegexOption.IGNORE_CASE) to "வினாடிகள்",
            Regex("\\bSecond\\b", RegexOption.IGNORE_CASE) to "வினாடி",
            Regex("\\bMinutes\\b", RegexOption.IGNORE_CASE) to "நிமிடங்கள்",
            Regex("\\bMinute\\b", RegexOption.IGNORE_CASE) to "நிமிடம்",
            Regex("\\bPractice\\b", RegexOption.IGNORE_CASE) to "பயிற்சி",
            Regex("\\bListen\\b", RegexOption.IGNORE_CASE) to "கவனியுங்கள்",
            Regex("\\bRepeat\\b", RegexOption.IGNORE_CASE) to "திரும்பச் சொல்லுங்கள்",
            Regex("\\bTone\\b", RegexOption.IGNORE_CASE) to "தொனி",
            Regex("\\bTones\\b", RegexOption.IGNORE_CASE) to "தொனிகள்"
        )

        for ((regex, replacement) in phoneticReplacements) {
            text = text.replace(regex, replacement)
        }

        // 5. Smooth punctuation pauses so sentences sound conversational, fluid and melodic
        text = text
            .replace(Regex("!+"), "! ")
            .replace(Regex("\\?+"), "? ")
            .replace(Regex(",+"), ", ")
            .replace(Regex(";+"), ", ")
            .replace(Regex("\\s+"), " ")
            .trim()

        return text
    }

    /**
     * Converts formal bookish Tamil into natural Spoken Tamil (பேச்சுத் தமிழ்)
     * making TTS pronunciation sound like professional Tamil dubbing artists.
     */
    private fun convertToSpokenTamil(input: String): String {
        var str = input

        val spokenPairs = listOf(
            // Verbs - Present & Future
            "செய்கிறேன்" to "செய்றேன்",
            "செய்கின்றேன்" to "செய்றேன்",
            "செய்கிறோம்" to "செய்றோம்",
            "செய்கின்றோம்" to "செய்றோம்",
            "செய்கிறீர்கள்" to "செய்றீங்க",
            "செய்கிறார்" to "செய்றாரு",
            "செய்கிறார்கள்" to "செய்றாங்க",
            "பண்ணுகிறேன்" to "பண்றேன்",
            "பண்ணுகிறோம்" to "பண்றோம்",
            "பண்ணுங்கள்" to "பண்ணுங்க",
            "வாருங்கள்" to "வாங்க",
            "வருகிறேன்" to "வர்றேன்",
            "வருகின்றேன்" to "வர்றேன்",
            "வருகிறோம்" to "வர்றோம்",
            "இருக்கிறேன்" to "இருக்கேன்",
            "இருக்கிறோம்" to "இருக்கோம்",
            "இருக்கிறீர்கள்" to "இருக்கீங்க",
            "இருக்கிறது" to "இருக்கு",
            "இருக்கின்றன" to "இருக்கு",
            "சொல்கிறேன்" to "சொல்றேன்",
            "சொல்கின்றேன்" to "சொல்றேன்",
            "சொல்லுங்கள்" to "சொல்லுங்க",
            "கேளுங்கள்" to "கேளுங்க",
            "கேட்கிறேன்" to "கேக்குறேன்",
            "கற்றுக்கொள்வோம்" to "கத்துக்கலாம்",
            "கற்றுக்கொள்ளுங்கள்" to "கத்துக்கோங்க",
            "கற்றுக்கொள்ளலாம்" to "கத்துக்கலாம்",
            "பேசுவோம்" to "பேசலாம்",
            "பேசுங்கள்" to "பேசுங்க",
            "பார்க்கலாம்" to "பாக்கலாம்",
            "பாருங்கள்" to "பாருங்க",
            "பார்த்தேன்" to "பாத்தேன்",
            "பயப்படாதீர்கள்" to "பயப்படாதீங்க",
            "கவலைப்படாதீர்கள்" to "கவலையே படாதீங்க",
            "முயற்சி செய்யுங்கள்" to "முயற்சி பண்ணுங்க",
            "சாப்பிட்டீர்களா" to "சாப்பிட்டீங்களா",
            "எப்படி இருக்கிறீர்கள்" to "எப்படி இருக்கீங்க",
            "நன்றாக இருக்கிறேன்" to "நல்லா இருக்கேன்",
            "புரிந்துவிட்டதா" to "புரிஞ்சுதா",
            "புரிந்ததா" to "புரிஞ்சுதா",
            "தெரிந்துவிட்டது" to "தெரிஞ்சு போச்சு",

            // Pronouns & Postpositions
            "அதைப்பற்றி" to "அதப்பத்தி",
            "இதைப்பற்றி" to "இதப்பத்தி",
            "உங்களிடம்" to "உங்ககிட்ட",
            "என்னிடம்" to "என்கிட்ட",
            "அவர்களிடம்" to "அவங்ககிட்ட",
            "இவர்களிடம்" to "இவங்ககிட்ட",
            "நீங்கள்" to "நீங்க",
            "அவர்கள்" to "அவங்க",
            "இவர்கள்" to "இவங்க",
            "எவ்வளவு" to "எவ்ளோ",
            "அவ்வளவு" to "அவ்ளோ",
            "இவ்வளவு" to "இவ்ளோ",
            "மிகவும்" to "ரொம்ப",
            "நன்றாக" to "நல்லா",
            "சரியாக" to "கரெக்டா",
            "சுலபமாக" to "ஈஸியா",
            "எளிதாக" to "ரொம்ப எளிதா",
            "அழகாக" to "அழகா",
            "விரைவாக" to "வேகமா",
            "மெதுவாக" to "பொறுமையா"
        )

        for ((formal, spoken) in spokenPairs) {
            str = str.replace(formal, spoken)
        }

        return str
    }

    fun optimizeForEnglishSpeech(rawText: String): String {
        return rawText
            .replace(Regex("[*#_~`|\\[\\]{}]"), " ")
            .replace("•", ", ")
            .replace("—", ", ")
            .replace("--", ", ")
            .replace("×", " times ")
            .replace("÷", " divided by ")
            .replace("=", " equals ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }
}
