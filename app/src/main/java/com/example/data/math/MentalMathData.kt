package com.example.data.math

import kotlin.random.Random

enum class MathOperation(val symbol: String, val titleEn: String, val titleTa: String) {
    ALL("★", "All Mix", "அனைத்தும்"),
    ADDITION("+", "Addition", "கூட்டல்"),
    SUBTRACTION("−", "Subtraction", "கழித்தல்"),
    MULTIPLICATION("×", "Multiplication", "பெருக்கல்"),
    DIVISION("÷", "Division", "வகுத்தல்"),
    WORD_PROBLEMS("📖", "Word Problems", "கதை கணக்குகள்"),
    VEDIC_TRICKS("⚡", "Mental Tricks", "வேக உத்திகள்")
}

enum class MathDifficulty(val titleEn: String, val titleTa: String, val icon: String) {
    BEGINNER("Beginner", "எளிய நிலை (சுபி)", "👧"),
    INTERMEDIATE("Intermediate", "நடுத்தர நிலை", "🚀"),
    CHAMPION("Champion", "சவால் நிலை", "🏆")
}

data class MathVocabularyItem(
    val english: String,
    val tamil: String,
    val exampleEn: String,
    val exampleTa: String
)

data class MentalMathQuestion(
    val id: String,
    val operation: MathOperation,
    val difficulty: MathDifficulty,
    val questionEn: String,
    val questionTa: String,
    val tanglishPronunciation: String,
    val num1: Int = 0,
    val num2: Int = 0,
    val symbol: String = "",
    val correctAnswer: Int,
    val options: List<Int>,
    val stepsEn: List<String>,
    val stepsTa: List<String>,
    val mentalTrickEn: String,
    val mentalTrickTa: String,
    val vocabulary: List<MathVocabularyItem>
)

object MentalMathDataSource {

    val mathGlossary: List<MathVocabularyItem> = listOf(
        MathVocabularyItem("Addition", "கூட்டல்", "Addition of 5 and 3 is 8.", "5 மற்றும் 3-ன் கூட்டல் 8 ஆகும்."),
        MathVocabularyItem("Sum", "மொத்தம் / கூடுதல்", "What is the sum of these two numbers?", "இந்த இரு எண்களின் கூடுதல் என்ன?"),
        MathVocabularyItem("Plus", "கூட்டல் குறி (+)", "Ten plus five equals fifteen.", "பத்து கூட்டல் ஐந்து சமம் பதினைந்து."),
        MathVocabularyItem("Subtraction", "கழித்தல்", "Subtraction means taking away.", "கழித்தல் என்றால் எடுத்துவிடுவது."),
        MathVocabularyItem("Difference", "வித்தியாசம்", "Find the difference between 20 and 8.", "20 மற்றும் 8-க்கு இடையேயான வித்தியாசத்தைக் காண்க."),
        MathVocabularyItem("Minus", "கழித்தல் குறி (−)", "Twelve minus four is eight.", "பன்னிரண்டு கழித்தல் நான்கு எட்டு ஆகும்."),
        MathVocabularyItem("Multiplication", "பெருக்கல்", "Multiplication is repeated addition.", "பெருக்கல் என்பது தொடர் கூட்டல் ஆகும்."),
        MathVocabularyItem("Product", "பெருக்கற்பலன்", "The product of 6 and 7 is 42.", "6 மற்றும் 7-ன் பெருக்கற்பலன் 42."),
        MathVocabularyItem("Times", "மடங்கு", "Five times four is twenty.", "ஐந்து மடங்கு நான்கு இருபது ஆகும்."),
        MathVocabularyItem("Division", "வகுத்தல்", "Division means sharing equally.", "வகுத்தல் என்றால் சமமாகப் பிரிப்பது."),
        MathVocabularyItem("Quotient", "ஈவு", "The quotient is 9.", "ஈவு 9 ஆகும்."),
        MathVocabularyItem("Remainder", "மீதி", "The remainder is zero.", "மீதி பூஜ்ஜியம்."),
        MathVocabularyItem("Equals", "சமம் (=)", "Two plus two equals four.", "இரண்டு கூட்டல் இரண்டு சமம் நான்கு."),
        MathVocabularyItem("Even Number", "இரட்டைப் படை எண்", "Eight is an even number.", "எட்டு ஓர் இரட்டைப் படை எண்."),
        MathVocabularyItem("Odd Number", "ஒற்றைப் படை எண்", "Seven is an odd number.", "ஏழு ஓர் ஒற்றைப் படை எண்."),
        MathVocabularyItem("Double", "இரண்டு மடங்கு", "Double of 15 is 30.", "15-ன் இரண்டு மடங்கு 30 ஆகும்."),
        MathVocabularyItem("Half", "பாதி", "Half of 50 is 25.", "50-ன் பாதி 25 ஆகும்."),
        MathVocabularyItem("Round up", "முழுமையாக்குதல்", "Round up 28 to 30.", "28 ஐ 30 ஆக முழுமையாக்குங்கள்."),
        MathVocabularyItem("Calculate mentally", "மனதிலேயே கணக்கிடு", "Can you calculate this mentally?", "இதை மனதிலேயே கணக்கிட முடியுமா?"),
        MathVocabularyItem("Share equally", "சமமாகப் பகிர்", "Share 12 mangoes equally among 3 friends.", "12 மாம்பழங்களை 3 நண்பர்களுக்கு சமமாகப் பகிருங்கள்.")
    )

    val curatedQuestions: List<MentalMathQuestion> = listOf(
        // 1. Beginner Addition
        MentalMathQuestion(
            id = "add_curated_1",
            operation = MathOperation.ADDITION,
            difficulty = MathDifficulty.BEGINNER,
            questionEn = "What is 15 + 28?",
            questionTa = "15 கூட்டல் 28 என்ன?",
            tanglishPronunciation = "வாட் இஸ் பிஃப்டீன் ப்ளஸ் டுவெண்டி எய்ட்?",
            num1 = 15,
            num2 = 28,
            symbol = "+",
            correctAnswer = 43,
            options = listOf(41, 43, 45, 33),
            stepsEn = listOf(
                "Step 1: Round 28 up to 30 by adding 2 (28 + 2 = 30).",
                "Step 2: Add 15 to 30: 15 + 30 = 45.",
                "Step 3: Subtract the 2 we added earlier: 45 - 2 = 43."
            ),
            stepsTa = listOf(
                "படி 1: 28-உடன் 2-ஐக் கூட்டி அதை 30 ஆக முழுமையாக்குங்கள் (28 + 2 = 30).",
                "படி 2: 15-உடன் 30-ஐக் கூட்டுங்கள்: 15 + 30 = 45.",
                "படி 3: முதலில் சேர்த்த 2-ஐக் கழியுங்கள்: 45 - 2 = 43."
            ),
            mentalTrickEn = "Rounding Trick: When adding numbers ending in 8 or 9, round up to the nearest ten, then subtract the extra!",
            mentalTrickTa = "முழுமையாக்கும் உத்தி: 8 அல்லது 9-ல் முடியும் எண்களைக் கூட்டும்போது, அருகிலுள்ள 10-ஆக மாற்றி கூட்டிவிட்டு, சேர்த்த வித்தியாசத்தைக் கழியுங்கள்!",
            vocabulary = listOf(
                MathVocabularyItem("Sum", "கூடுதல்", "The sum of 15 and 28 is 43.", "15 மற்றும் 28-ன் கூடுதல் 43."),
                MathVocabularyItem("Round up", "முழுமையாக்குதல்", "Round 28 to 30.", "28-ஐ 30-ஆக முழுமையாக்குங்கள்.")
            )
        ),

        // 2. Beginner Subtraction
        MentalMathQuestion(
            id = "sub_curated_1",
            operation = MathOperation.SUBTRACTION,
            difficulty = MathDifficulty.BEGINNER,
            questionEn = "What is 63 − 19?",
            questionTa = "63 கழித்தல் 19 என்ன?",
            tanglishPronunciation = "வாட் இஸ் சிக்ஸ்டி த்ரீ மைனஸ் நைண்டீன்?",
            num1 = 63,
            num2 = 19,
            symbol = "−",
            correctAnswer = 44,
            options = listOf(42, 44, 46, 54),
            stepsEn = listOf(
                "Step 1: 19 is very close to 20 (just 1 less).",
                "Step 2: Subtract 20 from 63 first: 63 - 20 = 43.",
                "Step 3: Since we subtracted 1 too much, add 1 back: 43 + 1 = 44."
            ),
            stepsTa = listOf(
                "படி 1: 19 என்பது 20-க்கு மிக அருகில் உள்ளது (1 மட்டுமே குறைவு).",
                "படி 2: முதலில் 63-ல் இருந்து 20-ஐக் கழியுங்கள்: 63 - 20 = 43.",
                "படி 3: நாம் 1-ஐ அதிகமாகக் கழித்ததால், 1-ஐ திரும்பக் கூட்டுங்கள்: 43 + 1 = 44."
            ),
            mentalTrickEn = "Subtraction Shortcut: Subtracting 19 is the same as subtracting 20 and adding 1 back!",
            mentalTrickTa = "கழித்தல் குறுக்குவழி: 19-ஐக் கழிப்பதற்குப் பதிலாக, 20-ஐக் கழித்துவிட்டு 1-ஐக் கூட்டினால் எளிதாக விடை வரும்!",
            vocabulary = listOf(
                MathVocabularyItem("Difference", "வித்தியாசம்", "The difference is 44.", "வித்தியாசம் 44 ஆகும்."),
                MathVocabularyItem("Subtract", "கழி", "Subtract 19 from 63.", "63-ல் இருந்து 19-ஐக் கழி.")
            )
        ),

        // 3. Beginner Multiplication
        MentalMathQuestion(
            id = "mul_curated_1",
            operation = MathOperation.MULTIPLICATION,
            difficulty = MathDifficulty.BEGINNER,
            questionEn = "What is 14 × 5?",
            questionTa = "14 பெருக்கல் 5 என்ன?",
            tanglishPronunciation = "வாட் இஸ் ஃபார்டீன் டைம்ஸ் ஃபைவ்?",
            num1 = 14,
            num2 = 5,
            symbol = "×",
            correctAnswer = 70,
            options = listOf(60, 65, 70, 75),
            stepsEn = listOf(
                "Step 1: Multiplying by 5 is the same as multiplying by 10 and dividing by 2.",
                "Step 2: First, find half of 14: Half of 14 is 7.",
                "Step 3: Multiply 7 by 10 (add a zero): 7 × 10 = 70!"
            ),
            stepsTa = listOf(
                "படி 1: 5-ஆல் பெருக்குவது என்பது 10-ஆல் பெருக்கி 2-ஆல் வகுப்பதற்குச் சமம்.",
                "படி 2: முதலில் 14-ன் பாதியைக் காணுங்கள்: 14-ன் பாதி 7.",
                "படி 3: 7-ஐ 10-ஆல் பெருக்குங்கள் (பூஜ்ஜியம் சேருங்கள்): 7 × 10 = 70!"
            ),
            mentalTrickEn = "Multiply by 5 Trick: Cut the number in half and add a 0 at the end! Half of 14 is 7 -> 70.",
            mentalTrickTa = "5-ஆல் பெருக்கும் உத்தி: எண்ணின் பாதியை எடுத்து இறுதியில் '0' சேருங்கள்! 14-ன் பாதி 7 -> 70.",
            vocabulary = listOf(
                MathVocabularyItem("Product", "பெருக்கற்பலன்", "The product of 14 and 5 is 70.", "14 மற்றும் 5-ன் பெருக்கற்பலன் 70."),
                MathVocabularyItem("Half", "பாதி", "Half of 14 is 7.", "14-ன் பாதி 7 ஆகும்.")
            )
        ),

        // 4. Beginner Division
        MentalMathQuestion(
            id = "div_curated_1",
            operation = MathOperation.DIVISION,
            difficulty = MathDifficulty.BEGINNER,
            questionEn = "What is 72 ÷ 8?",
            questionTa = "72 வகுத்தல் 8 என்ன?",
            tanglishPronunciation = "வாட் இஸ் செவண்டி டூ டிவைடட் பை எய்ட்?",
            num1 = 72,
            num2 = 8,
            symbol = "÷",
            correctAnswer = 9,
            options = listOf(7, 8, 9, 12),
            stepsEn = listOf(
                "Step 1: Division is the reverse of multiplication. Ask: What number times 8 gives 72?",
                "Step 2: Recall 8 times table: 8 × 8 = 64, and 8 × 9 = 72.",
                "Step 3: Therefore, 72 divided by 8 is 9!"
            ),
            stepsTa = listOf(
                "படி 1: வகுத்தல் என்பது பெருக்கலின் தலைகீழ். 8-உடன் எந்த எண்ணைப் பெருக்கினால் 72 வரும் என்று நினையுங்கள்.",
                "படி 2: 8-ஆம் வாய்ப்பாடு: 8 × 8 = 64, மற்றும் 8 × 9 = 72.",
                "படி 3: எனவே, 72 வகுத்தல் 8 என்பது 9 ஆகும்!"
            ),
            mentalTrickEn = "Division Strategy: Connect division directly to multiplication tables!",
            mentalTrickTa = "வகுத்தல் உத்தி: வகுத்தலை எப்போதும் வாய்ப்பாட்டு பெருக்கலுடன் தொடர்புபடுத்தி மனதிலேயே விடை காணுங்கள்!",
            vocabulary = listOf(
                MathVocabularyItem("Quotient", "ஈவு", "The quotient is 9.", "ஈவு 9 ஆகும்."),
                MathVocabularyItem("Divided by", "வகுத்தல்", "72 divided by 8 is 9.", "72 வகுத்தல் 8 என்பது 9.")
            )
        ),

        // 5. Word Problem (Subi & Apples)
        MentalMathQuestion(
            id = "word_curated_1",
            operation = MathOperation.WORD_PROBLEMS,
            difficulty = MathDifficulty.BEGINNER,
            questionEn = "Subi has 35 chocolates. She gives 12 to her friends and buys 8 more. How many chocolates does Subi have now?",
            questionTa = "சுபியிடம் 35 சாக்லேட்டுகள் உள்ளன. அவள் 12-ஐ நண்பர்களுக்குக் கொடுத்துவிட்டு, மேலும் 8 சாக்லேட்டுகளை வாங்குகிறாள். இப்போது சுபியிடம் எத்தனை சாக்லேட்டுகள் உள்ளன?",
            tanglishPronunciation = "சுபி ஹேஸ் தர்ட்டி ஃபைவ் சாக்லேட்ஸ்...",
            num1 = 35,
            num2 = 12,
            symbol = "📖",
            correctAnswer = 31,
            options = listOf(28, 30, 31, 35),
            stepsEn = listOf(
                "Step 1: Start with initial chocolates: 35.",
                "Step 2: Subi gives away 12 chocolates: 35 - 12 = 23 chocolates left.",
                "Step 3: Then she buys 8 more: 23 + 8 = 31 chocolates!"
            ),
            stepsTa = listOf(
                "படி 1: சுபியிடம் முதலில் இருந்தவை: 35 சாக்லேட்டுகள்.",
                "படி 2: நண்பர்களுக்குக் கொடுத்தது 12: 35 - 12 = 23 சாக்லேட்டுகள் மிச்சம்.",
                "படி 3: மேலும் வாங்கியது 8: 23 + 8 = 31 சாக்லேட்டுகள்!"
            ),
            mentalTrickEn = "Word Problem Tip: Break the story into two simple steps: first subtract what is given away, then add what is received!",
            mentalTrickTa = "கணக்கு உத்தி: கதையை இரு எளிய படிகளாகப் பிரியுங்கள்: முதலில் கொடுத்ததைக் கழித்துவிட்டு, வாங்கியதைக் கூட்டுங்கள்!",
            vocabulary = listOf(
                MathVocabularyItem("Give away", "கொடுத்துவிடுதல்", "She gives away 12 chocolates.", "அவள் 12 சாக்லேட்டுகளைக் கொடுக்கிறாள்."),
                MathVocabularyItem("How many left?", "எத்தனை மீதம்?", "How many are left?", "எத்தனை மீதம் உள்ளன?")
            )
        ),

        // 6. Vedic Trick (Multiply by 11)
        MentalMathQuestion(
            id = "trick_curated_1",
            operation = MathOperation.VEDIC_TRICKS,
            difficulty = MathDifficulty.INTERMEDIATE,
            questionEn = "What is 43 × 11?",
            questionTa = "43 பெருக்கல் 11 என்ன?",
            tanglishPronunciation = "வாட் இஸ் பார்டி த்ரீ டைம்ஸ் இலெவன்?",
            num1 = 43,
            num2 = 11,
            symbol = "⚡",
            correctAnswer = 473,
            options = listOf(463, 473, 483, 493),
            stepsEn = listOf(
                "Step 1: Take the digits 4 and 3.",
                "Step 2: Add the two digits together: 4 + 3 = 7.",
                "Step 3: Place the sum (7) right in the middle between 4 and 3: 4 [7] 3 = 473!"
            ),
            stepsTa = listOf(
                "படி 1: 43-ல் உள்ள எண்கள் 4 மற்றும் 3.",
                "படி 2: அந்த இரண்டு எண்களையும் கூட்டுங்கள்: 4 + 3 = 7.",
                "படி 3: வந்த விடையை (7) 4 மற்றும் 3-க்கு நடுவில் வையுங்கள்: 4 [7] 3 = 473!"
            ),
            mentalTrickEn = "11-Multiplication Magic: To multiply any 2-digit number by 11, add the digits and put the sum in the middle!",
            mentalTrickTa = "11-ஆல் பெருக்கும் மாய உத்தி: எந்த ஓர் ஈரிலக்க எண்ணையும் 11-ஆல் பெருக்க, இரண்டு எண்களையும் கூட்டி நடுவில் வைத்தால் போதும்!",
            vocabulary = listOf(
                MathVocabularyItem("Digits", "இலக்கங்கள்", "Add the two digits.", "இரு இலக்கங்களையும் கூட்டு."),
                MathVocabularyItem("In the middle", "நடுவில்", "Place it in the middle.", "நடுவில் வையுங்கள்.")
            )
        ),

        // 7. Intermediate Addition (Rounding to 100)
        MentalMathQuestion(
            id = "add_curated_2",
            operation = MathOperation.ADDITION,
            difficulty = MathDifficulty.INTERMEDIATE,
            questionEn = "What is 87 + 46?",
            questionTa = "87 கூட்டல் 46 என்ன?",
            tanglishPronunciation = "வாட் இஸ் எய்டி செவன் ப்ளஸ் பார்டி சிக்ஸ்?",
            num1 = 87,
            num2 = 46,
            symbol = "+",
            correctAnswer = 133,
            options = listOf(123, 131, 133, 137),
            stepsEn = listOf(
                "Step 1: 87 is only 13 away from 100.",
                "Step 2: Take 13 from 46 (46 - 13 = 33).",
                "Step 3: Combine: 100 + 33 = 133!"
            ),
            stepsTa = listOf(
                "படி 1: 87 என்பது 100 ஆவதற்கு 13 மட்டுமே தேவைப்படுகிறது.",
                "படி 2: 46-ல் இருந்து 13-ஐ எடுத்து 87-உடன் சேர்த்து 100 ஆக்குங்கள் (46 - 13 = 33 மீதம்).",
                "படி 3: இப்போது கூட்டிவிடுங்கள்: 100 + 33 = 133!"
            ),
            mentalTrickEn = "Make 100 Strategy: Lend from the second number to complete the first to 100, then add the rest!",
            mentalTrickTa = "100 ஆக்கும் உத்தி: இரண்டாவது எண்ணிலிருந்து சிறிதை எடுத்து முதல் எண்ணை 100 ஆக்கிவிட்டு, மீதமுள்ளதைக் கூட்டவும்!",
            vocabulary = listOf(
                MathVocabularyItem("Complement to 100", "100-ன் நிறைவு", "87 needs 13 to become 100.", "87 100 ஆவதற்கு 13 தேவை."),
                MathVocabularyItem("Combine", "இணைத்துக் கூட்டு", "Combine 100 and 33.", "100 மற்றும் 33-ஐ இணை.")
            )
        ),

        // 8. Intermediate Subtraction from 100
        MentalMathQuestion(
            id = "sub_curated_2",
            operation = MathOperation.SUBTRACTION,
            difficulty = MathDifficulty.INTERMEDIATE,
            questionEn = "What is 100 − 37?",
            questionTa = "100 கழித்தல் 37 என்ன?",
            tanglishPronunciation = "வாட் இஸ் ஒன் ஹண்ட்ரட் மைனஸ் தர்டி செவன்?",
            num1 = 100,
            num2 = 37,
            symbol = "−",
            correctAnswer = 63,
            options = listOf(53, 63, 67, 73),
            stepsEn = listOf(
                "Step 1: Vedic rule 'All from 9 and last from 10'.",
                "Step 2: Subtract the tens digit (3) from 9: 9 - 3 = 6.",
                "Step 3: Subtract the units digit (7) from 10: 10 - 7 = 3. Result = 63!"
            ),
            stepsTa = listOf(
                "படி 1: வேத கணித விதி: 'ஒன்பதில் இருந்து அனைத்தும், பத்தில் இருந்து கடைசியும்'.",
                "படி 2: பத்துகள் இலக்கமான 3-ஐ 9-ல் இருந்து கழியுங்கள்: 9 - 3 = 6.",
                "படி 3: கடைசி ஒன்றுகள் இலக்கமான 7-ஐ 10-ல் இருந்து கழியுங்கள்: 10 - 7 = 3. விடை = 63!"
            ),
            mentalTrickEn = "Subtract from 100 Rule: Tens digit from 9, units digit from 10. Instant mental answer!",
            mentalTrickTa = "100-ல் இருந்து கழிக்கும் சூத்திரம்: பத்தாவது இலக்கத்தை 9-ல் கழித்து, கடைசி இலக்கத்தை 10-ல் கழித்தால் கண் இமைக்கும் நேரத்தில் விடை!",
            vocabulary = listOf(
                MathVocabularyItem("Units digit", "ஒன்றுகள் இலக்கம்", "The units digit is 7.", "ஒன்றுகள் இலக்கம் 7 ஆகும்."),
                MathVocabularyItem("Tens digit", "பத்துகள் இலக்கம்", "The tens digit is 3.", "பத்துகள் இலக்கம் 3 ஆகும்.")
            )
        ),

        // 9. Intermediate Multiplication (Double and Halve)
        MentalMathQuestion(
            id = "mul_curated_2",
            operation = MathOperation.MULTIPLICATION,
            difficulty = MathDifficulty.INTERMEDIATE,
            questionEn = "What is 16 × 25?",
            questionTa = "16 பெருக்கல் 25 என்ன?",
            tanglishPronunciation = "வாட் இஸ் சிக்ஸ்டீன் டைம்ஸ் டுவெண்டி ஃபைவ்?",
            num1 = 16,
            num2 = 25,
            symbol = "×",
            correctAnswer = 400,
            options = listOf(350, 375, 400, 425),
            stepsEn = listOf(
                "Step 1: Double and halve trick! Double 25 to 50, halve 16 to 8 (8 × 50).",
                "Step 2: Double again: 50 doubles to 100, halve 8 to 4 (4 × 100).",
                "Step 3: 4 × 100 = 400! Simple as that."
            ),
            stepsTa = listOf(
                "படி 1: இரட்டிப்பு & பாதி உத்தி! 25-ஐ இரட்டிப்பாக்குங்கள் = 50, 16-ஐ பாதியாக்குங்கள் = 8 (8 × 50).",
                "படி 2: மீண்டும் இரட்டிப்பாக்குங்கள்: 50 -> 100, 8 -> 4 (4 × 100).",
                "படி 3: 4 × 100 = 400! மிக எளிய மனக் கணக்கு."
            ),
            mentalTrickEn = "Double & Halve: When multiplying by 25, 50, or ending in 5, double one number and halve the other!",
            mentalTrickTa = "இரட்டிப்பு & பாதி உத்தி: 25, 50 போன்ற எண்களால் பெருக்கும்போது, ஒன்றை பாதியாக்கி மற்றதை இரட்டிப்பாக்குங்கள்!",
            vocabulary = listOf(
                MathVocabularyItem("Double", "இரட்டிப்பு", "Double 25 to get 50.", "25-ஐ இரட்டிப்பாக்கினால் 50."),
                MathVocabularyItem("Halve", "பாதியாக்குதல்", "Halve 16 to get 8.", "16-ஐ பாதியாக்கினால் 8.")
            )
        ),

        // 10. Word Problem (Subi & Book Reading)
        MentalMathQuestion(
            id = "word_curated_2",
            operation = MathOperation.WORD_PROBLEMS,
            difficulty = MathDifficulty.INTERMEDIATE,
            questionEn = "Subi reads 12 pages of a story book every day. How many pages will she read in 5 days?",
            questionTa = "சுபி தினமும் ஒரு கதைப் புத்தகத்தில் 12 பக்கங்கள் வாசிக்கிறாள். 5 நாட்களில் அவள் எத்தனை பக்கங்கள் வாசிப்பாள்?",
            tanglishPronunciation = "சுபி ரீட்ஸ் டுவெல்வ் பேஜஸ் ஆஃப் எ ஸ்டோரி புக் எவ்ரி டே...",
            num1 = 12,
            num2 = 5,
            symbol = "📖",
            correctAnswer = 60,
            options = listOf(50, 55, 60, 65),
            stepsEn = listOf(
                "Step 1: Identify pages per day: 12 pages.",
                "Step 2: Number of days: 5 days.",
                "Step 3: Multiply 12 by 5: 12 × 5 = 60 pages!"
            ),
            stepsTa = listOf(
                "படி 1: ஒரு நாளைக்கு வாசிக்கும் பக்கங்கள்: 12 பக்கங்கள்.",
                "படி 2: நாட்களின் எண்ணிக்கை: 5 நாட்கள்.",
                "படி 3: 12-ஐ 5-ஆல் பெருக்குங்கள்: 12 × 5 = 60 பக்கங்கள்!"
            ),
            mentalTrickEn = "Fast Multiply by 5: Half of 12 is 6, append zero -> 60 pages!",
            mentalTrickTa = "5-ஆல் பெருக்கும் உத்தி: 12-ன் பாதி 6, பூஜ்ஜியம் சேர்த்தால் 60 பக்கங்கள்!",
            vocabulary = listOf(
                MathVocabularyItem("Per day", "ஒரு நாளைக்கு", "12 pages per day.", "ஒரு நாளைக்கு 12 பக்கங்கள்."),
                MathVocabularyItem("Total pages", "மொத்த பக்கங்கள்", "Total pages read is 60.", "படித்த மொத்த பக்கங்கள் 60.")
            )
        ),

        // 11. Champion Word Problem (Sharing Pens)
        MentalMathQuestion(
            id = "word_curated_3",
            operation = MathOperation.WORD_PROBLEMS,
            difficulty = MathDifficulty.CHAMPION,
            questionEn = "A teacher buys 8 packets of colour pencils. Each packet contains 10 pencils. If she distributes 45 pencils to students, how many pencils are remaining?",
            questionTa = "ஒரு டீச்சர் 8 பாக்கெட் வண்ண பென்சில்களை வாங்குகிறார். ஒவ்வொரு பாக்கெட்டிலும் 10 பென்சில்கள் உள்ளன. மாணவர்களுக்கு 45 பென்சில்களைக் கொடுத்தால், மீதம் எத்தனை பென்சில்கள் இருக்கும்?",
            tanglishPronunciation = "எ டீச்சர் பைஸ் எய்ட் பாக்கெட்ஸ் ஆஃப் கலர் பென்சில்ஸ்...",
            num1 = 80,
            num2 = 45,
            symbol = "📖",
            correctAnswer = 35,
            options = listOf(30, 35, 40, 45),
            stepsEn = listOf(
                "Step 1: Total pencils = 8 packets × 10 pencils = 80 pencils.",
                "Step 2: Distributed pencils = 45 pencils.",
                "Step 3: Remaining pencils = 80 - 45 = 35 pencils!"
            ),
            stepsTa = listOf(
                "படி 1: மொத்த பென்சில்கள் = 8 பாக்கெட்டுகள் × 10 பென்சில்கள் = 80 பென்சில்கள்.",
                "படி 2: மாணவர்களுக்குக் கொடுத்தவை = 45 பென்சில்கள்.",
                "படி 3: மீதம் உள்ளவை = 80 - 45 = 35 பென்சில்கள்!"
            ),
            mentalTrickEn = "Two-Step Math: First find the total with quick 10-multiplication, then subtract easily by breaking 45 into 40 and 5!",
            mentalTrickTa = "இருபடி கணக்கு: முதலில் 10-ஆல் பெருக்கி மொத்தம் 80-ஐக் காணுங்கள். பின் 45-ஐ 40 மற்றும் 5 எனக் கழித்தால் 35 எளிதில் கிடைக்கும்!",
            vocabulary = listOf(
                MathVocabularyItem("Distribute", "பகிர்ந்தளி", "Distribute pencils to students.", "மாணவர்களுக்கு பென்சில்களைப் பகிர்ந்தளி."),
                MathVocabularyItem("Remaining", "மீதம்", "How many are remaining?", "எத்தனை மீதம் உள்ளன?")
            )
        ),

        // 12. Vedic Trick: Multiplying Numbers near 100
        MentalMathQuestion(
            id = "trick_curated_2",
            operation = MathOperation.VEDIC_TRICKS,
            difficulty = MathDifficulty.CHAMPION,
            questionEn = "What is 104 × 106?",
            questionTa = "104 பெருக்கல் 106 என்ன?",
            tanglishPronunciation = "வாட் இஸ் ஒன் ஹண்ட்ரட் ஃபோர் டைம்ஸ் ஒன் ஹண்ட்ரட் சிக்ஸ்?",
            num1 = 104,
            num2 = 106,
            symbol = "⚡",
            correctAnswer = 11024,
            options = listOf(11018, 11024, 11032, 11124),
            stepsEn = listOf(
                "Step 1: 104 is +4 above 100, and 106 is +6 above 100.",
                "Step 2: Cross add: 104 + 6 = 110 (this is the left part: 110).",
                "Step 3: Multiply surpluses: 4 × 6 = 24 (this is the right part: 24). Combine: 11,024!"
            ),
            stepsTa = listOf(
                "படி 1: 104 என்பது 100-ஐ விட 4 அதிகம் (+4); 106 என்பது 100-ஐ விட 6 அதிகம் (+6).",
                "படி 2: குறுக்குக் கூட்டல்: 104 + 6 = 110 (இடது பகுதி: 110).",
                "படி 3: உபரி எண்களைப் பெருக்குங்கள்: 4 × 6 = 24 (வலது பகுதி: 24). இரண்டையும் சேர்த்தால்: 11,024!"
            ),
            mentalTrickEn = "Vedic Base 100 Trick: (100 + a)(100 + b) = (100 + a + b) and (a × b). Lightning fast!",
            mentalTrickTa = "100-ஐ அடிப்படையாகக் கொண்ட வேத உத்தி: குறுக்கே கூட்டி இடதுபுறமும், உபரியைப் பெருக்கி வலதுபுறமும் வைத்தால் உடனே விடை!",
            vocabulary = listOf(
                MathVocabularyItem("Surplus / Above", "அதிகம் / உபரி", "4 above 100.", "100-க்கு மேல் 4 அதிகம்."),
                MathVocabularyItem("Base", "அடிப்படை எண்", "Base number is 100.", "அடிப்படை எண் 100.")
            )
        )
    )

    fun generateDynamicQuestion(
        operation: MathOperation,
        difficulty: MathDifficulty
    ): MentalMathQuestion {
        val op = if (operation == MathOperation.ALL) {
            listOf(
                MathOperation.ADDITION,
                MathOperation.SUBTRACTION,
                MathOperation.MULTIPLICATION,
                MathOperation.DIVISION
            ).random()
        } else {
            operation
        }

        return when (op) {
            MathOperation.ADDITION -> generateAdditionQuestion(difficulty)
            MathOperation.SUBTRACTION -> generateSubtractionQuestion(difficulty)
            MathOperation.MULTIPLICATION -> generateMultiplicationQuestion(difficulty)
            MathOperation.DIVISION -> generateDivisionQuestion(difficulty)
            MathOperation.WORD_PROBLEMS -> curatedQuestions.filter { it.operation == MathOperation.WORD_PROBLEMS }.random()
            MathOperation.VEDIC_TRICKS -> curatedQuestions.filter { it.operation == MathOperation.VEDIC_TRICKS }.random()
            else -> generateAdditionQuestion(difficulty)
        }
    }

    private fun generateAdditionQuestion(difficulty: MathDifficulty): MentalMathQuestion {
        val (a, b) = when (difficulty) {
            MathDifficulty.BEGINNER -> Pair(Random.nextInt(6, 30), Random.nextInt(5, 25))
            MathDifficulty.INTERMEDIATE -> Pair(Random.nextInt(25, 90), Random.nextInt(15, 80))
            MathDifficulty.CHAMPION -> Pair(Random.nextInt(120, 480), Random.nextInt(75, 360))
        }
        val ans = a + b
        val options = makeOptions(ans)
        val tensB = (b / 10) * 10
        val onesB = b % 10

        return MentalMathQuestion(
            id = "dyn_add_${System.currentTimeMillis()}_${Random.nextInt(1000)}",
            operation = MathOperation.ADDITION,
            difficulty = difficulty,
            questionEn = "What is $a + $b?",
            questionTa = "$a கூட்டல் $b என்ன?",
            tanglishPronunciation = "வாட் இஸ் $a ப்ளஸ் $b?",
            num1 = a,
            num2 = b,
            symbol = "+",
            correctAnswer = ans,
            options = options,
            stepsEn = listOf(
                "Step 1: Break $b into tens ($tensB) and ones ($onesB).",
                "Step 2: Add tens first: $a + $tensB = ${a + tensB}.",
                "Step 3: Now add ones: ${a + tensB} + $onesB = $ans!"
            ),
            stepsTa = listOf(
                "படி 1: $b-ஐ பத்துகள் ($tensB) மற்றும் ஒன்றுகள் ($onesB) ஆகப் பிரியுங்கள்.",
                "படி 2: முதலில் பத்துகளைக் கூட்டுங்கள்: $a + $tensB = ${a + tensB}.",
                "படி 3: இப்போது ஒன்றுகளைக் கூட்டுங்கள்: ${a + tensB} + $onesB = $ans!"
            ),
            mentalTrickEn = "Left-to-Right Addition: Add the larger place values (tens/hundreds) first in your head!",
            mentalTrickTa = "இடமிருந்து வலம் கூட்டும் உத்தி: மனதிலேயே பெரிய மதிப்புள்ள பத்துகளை முதலில் கூட்டி, பின் ஒன்றுகளைச் சேர்க்கவும்!",
            vocabulary = listOf(
                MathVocabularyItem("Addition", "கூட்டல்", "$a plus $b is $ans.", "$a கூட்டல் $b சமம் $ans."),
                MathVocabularyItem("Sum", "கூடுதல்", "The sum is $ans.", "கூடுதல் $ans ஆகும்.")
            )
        )
    }

    private fun generateSubtractionQuestion(difficulty: MathDifficulty): MentalMathQuestion {
        val (a, b) = when (difficulty) {
            MathDifficulty.BEGINNER -> {
                val num2 = Random.nextInt(5, 20)
                val num1 = num2 + Random.nextInt(6, 25)
                Pair(num1, num2)
            }
            MathDifficulty.INTERMEDIATE -> {
                val num2 = Random.nextInt(18, 55)
                val num1 = num2 + Random.nextInt(15, 60)
                Pair(num1, num2)
            }
            MathDifficulty.CHAMPION -> {
                val num2 = Random.nextInt(65, 280)
                val num1 = num2 + Random.nextInt(50, 300)
                Pair(num1, num2)
            }
        }
        val ans = a - b
        val options = makeOptions(ans)
        val tensB = (b / 10) * 10
        val onesB = b % 10

        return MentalMathQuestion(
            id = "dyn_sub_${System.currentTimeMillis()}_${Random.nextInt(1000)}",
            operation = MathOperation.SUBTRACTION,
            difficulty = difficulty,
            questionEn = "What is $a − $b?",
            questionTa = "$a கழித்தல் $b என்ன?",
            tanglishPronunciation = "வாட் இஸ் $a மைனஸ் $b?",
            num1 = a,
            num2 = b,
            symbol = "−",
            correctAnswer = ans,
            options = options,
            stepsEn = listOf(
                "Step 1: Break $b into tens ($tensB) and ones ($onesB).",
                "Step 2: Subtract the tens first: $a - $tensB = ${a - tensB}.",
                "Step 3: Subtract the ones: ${a - tensB} - $onesB = $ans!"
            ),
            stepsTa = listOf(
                "படி 1: $b-ஐ பத்துகள் ($tensB) மற்றும் ஒன்றுகள் ($onesB) எனப் பிரியுங்கள்.",
                "படி 2: முதலில் பத்துகளைக் கழியுங்கள்: $a - $tensB = ${a - tensB}.",
                "படி 3: இப்போது ஒன்றுகளைக் கழியுங்கள்: ${a - tensB} - $onesB = $ans!"
            ),
            mentalTrickEn = "Compensation Trick: If subtracting a number near a ten, subtract the rounded ten and adjust!",
            mentalTrickTa = "முழுமைப்படுத்தி கழிக்கும் உத்தி: பத்துகளுக்கு அருகில் உள்ள எண்ணை முழுமையாக்கி கழித்துவிட்டு, சமன் செய்யுங்கள்!",
            vocabulary = listOf(
                MathVocabularyItem("Subtraction", "கழித்தல்", "Subtract $b from $a.", "$a-ல் இருந்து $b-ஐக் கழி."),
                MathVocabularyItem("Difference", "வித்தியாசம்", "The difference is $ans.", "வித்தியாசம் $ans ஆகும்.")
            )
        )
    }

    private fun generateMultiplicationQuestion(difficulty: MathDifficulty): MentalMathQuestion {
        val (a, b) = when (difficulty) {
            MathDifficulty.BEGINNER -> Pair(Random.nextInt(3, 10), Random.nextInt(2, 9))
            MathDifficulty.INTERMEDIATE -> Pair(Random.nextInt(11, 25), Random.nextInt(3, 9))
            MathDifficulty.CHAMPION -> Pair(Random.nextInt(15, 45), Random.nextInt(6, 15))
        }
        val ans = a * b
        val options = makeOptions(ans)

        return MentalMathQuestion(
            id = "dyn_mul_${System.currentTimeMillis()}_${Random.nextInt(1000)}",
            operation = MathOperation.MULTIPLICATION,
            difficulty = difficulty,
            questionEn = "What is $a × $b?",
            questionTa = "$a பெருக்கல் $b என்ன?",
            tanglishPronunciation = "வாட் இஸ் $a டைம்ஸ் $b?",
            num1 = a,
            num2 = b,
            symbol = "×",
            correctAnswer = ans,
            options = options,
            stepsEn = listOf(
                "Step 1: Multiplication is grouping. Think of $b groups of $a.",
                "Step 2: Break $a into easy parts: e.g., ${(a/10)*10} and ${a%10}.",
                "Step 3: Multiply each part by $b and add them together: Result is $ans!"
            ),
            stepsTa = listOf(
                "படி 1: பெருக்கல் என்பது குழுக்களாகச் சேர்த்தல். $a-ன் $b மடங்காக நினையுங்கள்.",
                "படி 2: $a-ஐ எளிய பகுதிகளாகப் பிரியுங்கள்: ${(a/10)*10} மற்றும் ${a%10}.",
                "படி 3: ஒவ்வொன்றையும் $b-ஆல் பெருக்கிக் கூட்டுங்கள்: விடை $ans!"
            ),
            mentalTrickEn = "Distributive Trick: Break larger numbers into (tens + ones) and multiply each separately!",
            mentalTrickTa = "பிரித்துப் பெருக்கும் உத்தி: பெரிய எண்களைப் பத்துகளாகவும் ஒன்றுகளாகவும் பிரித்து தனித்தனியாகப் பெருக்கிக் கூட்டவும்!",
            vocabulary = listOf(
                MathVocabularyItem("Product", "பெருக்கற்பலன்", "Product of $a and $b is $ans.", "$a மற்றும் $b-ன் பெருக்கற்பலன் $ans."),
                MathVocabularyItem("Times", "மடங்கு", "$a times $b is $ans.", "$a-ன் $b மடங்கு $ans.")
            )
        )
    }

    private fun generateDivisionQuestion(difficulty: MathDifficulty): MentalMathQuestion {
        val (divisor, quotient) = when (difficulty) {
            MathDifficulty.BEGINNER -> Pair(Random.nextInt(2, 8), Random.nextInt(2, 9))
            MathDifficulty.INTERMEDIATE -> Pair(Random.nextInt(4, 12), Random.nextInt(6, 15))
            MathDifficulty.CHAMPION -> Pair(Random.nextInt(7, 16), Random.nextInt(12, 35))
        }
        val dividend = divisor * quotient
        val options = makeOptions(quotient)

        return MentalMathQuestion(
            id = "dyn_div_${System.currentTimeMillis()}_${Random.nextInt(1000)}",
            operation = MathOperation.DIVISION,
            difficulty = difficulty,
            questionEn = "What is $dividend ÷ $divisor?",
            questionTa = "$dividend வகுத்தல் $divisor என்ன?",
            tanglishPronunciation = "வாட் இஸ் $dividend டிவைடட் பை $divisor?",
            num1 = dividend,
            num2 = divisor,
            symbol = "÷",
            correctAnswer = quotient,
            options = options,
            stepsEn = listOf(
                "Step 1: Ask: What number multiplied by $divisor gives $dividend?",
                "Step 2: Recall that $divisor × $quotient = $dividend.",
                "Step 3: Therefore, $dividend ÷ $divisor = $quotient!"
            ),
            stepsTa = listOf(
                "படி 1: $divisor-உடன் எந்த எண்ணைப் பெருக்கினால் $dividend வரும் எனப் பாருங்கள்.",
                "படி 2: $divisor × $quotient = $dividend என்பதை நினைவுகூருங்கள்.",
                "படி 3: எனவே, $dividend வகுத்தல் $divisor = $quotient!"
            ),
            mentalTrickEn = "Inverse Multiplication: Every division fact is a multiplication fact backwards!",
            mentalTrickTa = "பெருக்கலின் தலைகீழ்: வகுத்தலின் விடையை எப்போதுமே தொடர்புடைய வாய்ப்பாட்டுடன் பொருத்தி மனதிலேயே எளிதில் அறியலாம்!",
            vocabulary = listOf(
                MathVocabularyItem("Quotient", "ஈவு", "The quotient is $quotient.", "ஈவு $quotient ஆகும்."),
                MathVocabularyItem("Divided by", "வகுத்தல்", "$dividend divided by $divisor is $quotient.", "$dividend வகுத்தல் $divisor சமம் $quotient.")
            )
        )
    }

    private fun makeOptions(correct: Int): List<Int> {
        val set = mutableSetOf(correct)
        val deltas = listOf(-2, 2, -1, 1, -10, 10, -5, 5, 3, -3)
        var i = 0
        while (set.size < 4 && i < deltas.size) {
            val candidate = correct + deltas[i]
            if (candidate > 0 && candidate != correct) {
                set.add(candidate)
            }
            i++
        }
        while (set.size < 4) {
            val candidate = (correct + Random.nextInt(-8, 9)).coerceAtLeast(1)
            set.add(candidate)
        }
        return set.toList().shuffled()
    }
}
