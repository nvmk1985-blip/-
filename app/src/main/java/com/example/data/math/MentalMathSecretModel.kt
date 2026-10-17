package com.example.data.math

enum class MathSecretCategory(val titleTa: String, val titleEn: String, val icon: String) {
    ALL("அனைத்தும் (100)", "All 100", "⚡"),
    ADDITION("கூட்டல் (1-15)", "Addition", "➕"),
    SUBTRACTION("கழித்தல் (16-30)", "Subtraction", "➖"),
    MULTIPLICATION("பெருக்கல் (31-60)", "Multiplication", "✖️"),
    DIVISION("வகுத்தல் (61-75)", "Division", "➗"),
    FRACTIONS_PERCENTAGES("பின்னங்கள் & % (76-85)", "Fractions & %", "🍰"),
    SQUARES_MAGIC("வர்க்கம் & மேஜிக் (86-100)", "Squares & Magic", "✨")
}

data class MentalMathSecret(
    val number: Int,
    val titleTa: String,
    val titleEn: String,
    val formulaTa: String,
    val explanationTa: String,
    val stepsTa: List<String>,
    val examples: List<Pair<String, String>>,
    val practiceQuestions: List<String>,
    val category: MathSecretCategory = MathSecretCategory.ALL
)
