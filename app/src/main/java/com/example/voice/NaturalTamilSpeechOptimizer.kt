package com.example.voice

/**
 * Optimizes text for natural, human-like Tamil and Bilingual TTS speech synthesis.
 * Solves common robotic artifacts in Android Text-To-Speech by:
 * 1. Expanding arithmetic symbols and math expressions into natural spoken Tamil words.
 * 2. Phoneticizing English loanwords and acronyms seamlessly into native Tamil script.
 * 3. Smoothing punctuation, commas, and question marks so speech sounds conversational, warm, and expressive.
 */
object NaturalTamilSpeechOptimizer {

    fun optimizeForTamilSpeech(rawText: String): String {
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

        // 3. Phoneticize common English words and conversational terms embedded in Tamil text
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

        // 4. Smooth punctuation pauses so sentences sound conversational, fluid and melodic
        text = text
            .replace(Regex("!+"), "! ")
            .replace(Regex("\\?+"), "? ")
            .replace(Regex(",+"), ", ")
            .replace(Regex(";+"), ", ")
            .replace(Regex("\\s+"), " ")
            .trim()

        return text
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
