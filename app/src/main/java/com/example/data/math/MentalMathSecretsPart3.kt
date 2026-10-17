package com.example.data.math

object MentalMathSecretsPart3 {

    val divisionSecrets: List<MentalMathSecret> = listOf(
        MentalMathSecret(
            number = 61,
            titleTa = "5-ஆல் வகுத்தல்: இரட்டிப்பாக்கி புள்ளி வைக்கவும்",
            titleEn = "Divide by 5: Double and Move Decimal",
            formulaTa = "(n × 2) ÷ 10",
            explanationTa = "எந்த ஓர் எண்ணையும் 5-ஆல் வகுக்க, அந்த எண்ணை இரட்டிப்பாக்கி வலமிருந்து ஒரு ஸ்தானம் தள்ளி புள்ளி வைக்கவும்!",
            stepsTa = listOf(
                "படி 1: எண்ணை 2-ஆல் பெருக்கி இரட்டிப்பாக்குங்கள்.",
                "படி 2: வலது ஓரத்திலிருந்து ஒரு ஸ்தானம் தள்ளி புள்ளி வைக்கவும் (10-ஆல் வகுக்கவும்)."
            ),
            examples = listOf(
                "140 ÷ 5" to "140 × 2 = 280 ➔ புள்ளி வைத்தால்: 28",
                "235 ÷ 5" to "235 × 2 = 470 ➔ புள்ளி வைத்தால்: 47",
                "63 ÷ 5" to "63 × 2 = 126 ➔ புள்ளி வைத்தால்: 12.6"
            ),
            practiceQuestions = listOf("85 ÷ 5 = ?", "165 ÷ 5 = ?", "94 ÷ 5 = ?"),
            category = MathSecretCategory.DIVISION
        ),
        MentalMathSecret(
            number = 62,
            titleTa = "25-ஆல் வகுத்தல்: 4-ஆல் பெருக்கி 2 ஸ்தானம் தள்ளி புள்ளி வைக்கவும்",
            titleEn = "Divide by 25: Multiply by 4, Move Decimal 2 Places",
            formulaTa = "(n × 4) ÷ 100",
            explanationTa = "25-ஆல் வகுக்க எண்ணை 4-ஆல் பெருக்கி, வலமிருந்து இரு ஸ்தானங்கள் தள்ளி புள்ளி வைத்தால் நொடியில் விடை கிடைக்கும்.",
            stepsTa = listOf(
                "படி 1: எண்ணை 4-ஆல் பெருக்குங்கள் (இருமுறை இரட்டிப்பாக்குங்கள்).",
                "படி 2: வலமிருந்து இரண்டு ஸ்தானங்கள் தள்ளி புள்ளி வைக்கவும்."
            ),
            examples = listOf(
                "200 ÷ 25" to "200 × 4 = 800 ➔ 8.00 = 8",
                "350 ÷ 25" to "350 × 4 = 1400 ➔ 14.00 = 14",
                "125 ÷ 25" to "125 × 4 = 500 ➔ 5.00 = 5"
            ),
            practiceQuestions = listOf("450 ÷ 25 = ?", "175 ÷ 25 = ?", "600 ÷ 25 = ?"),
            category = MathSecretCategory.DIVISION
        ),
        MentalMathSecret(
            number = 63,
            titleTa = "50-ஆல் வகுத்தல்: இரட்டிப்பாக்கி 2 ஸ்தானம் தள்ளி புள்ளி வைக்கவும்",
            titleEn = "Divide by 50: Double and Divide by 100",
            formulaTa = "(n × 2) ÷ 100",
            explanationTa = "50-ஆல் வகுக்க எண்ணை இரட்டிப்பாக்கி, இரண்டு ஸ்தானங்கள் தள்ளி புள்ளி வைத்தால் போதும்.",
            stepsTa = listOf(
                "படி 1: எண்ணை 2-ஆல் பெருக்குங்கள்.",
                "படி 2: 100-ஆல் வகுக்க இரு ஸ்தானம் தள்ளி புள்ளி வைக்கவும்."
            ),
            examples = listOf(
                "450 ÷ 50" to "450 × 2 = 900 ➔ 9",
                "650 ÷ 50" to "650 × 2 = 1300 ➔ 13",
                "320 ÷ 50" to "320 × 2 = 640 ➔ 6.4"
            ),
            practiceQuestions = listOf("250 ÷ 50 = ?", "750 ÷ 50 = ?", "580 ÷ 50 = ?"),
            category = MathSecretCategory.DIVISION
        ),
        MentalMathSecret(
            number = 64,
            titleTa = "2-ஆல் வகுபடும் விதி (Divisibility by 2)",
            titleEn = "Divisibility Rule for 2",
            formulaTa = "கடைசி இலக்கம்: 0, 2, 4, 6, 8",
            explanationTa = "ஓர் எண்ணின் கடைசி இலக்கம் இரட்டைப்படை எண்ணாக (0, 2, 4, 6, 8) இருந்தால் மட்டுமே அது 2-ஆல் மீதியின்றி வகுபடும்.",
            stepsTa = listOf(
                "படி 1: எண்ணின் கடைசி இலக்கத்தை (ஒன்றுகள்) மட்டும் பாருங்கள்.",
                "படி 2: அது 0, 2, 4, 6, 8-ல் முடிந்தால் 2-ஆல் வகுபடும்."
            ),
            examples = listOf(
                "384" to "கடைசி எண் 4 (இரட்டைப்படை) ➔ 2-ஆல் வகுபடும்!",
                "579" to "கடைசி எண் 9 (ஒற்றைப்படை) ➔ 2-ஆல் வகுபடாது!",
                "1020" to "கடைசி எண் 0 ➔ 2-ஆல் வகுபடும்!"
            ),
            practiceQuestions = listOf("456 வகுபடுமா?", "891 வகுபடுமா?", "738 வகுபடுமா?"),
            category = MathSecretCategory.DIVISION
        ),
        MentalMathSecret(
            number = 65,
            titleTa = "3-ஆல் வகுபடும் விதி (Divisibility by 3)",
            titleEn = "Divisibility Rule for 3",
            formulaTa = "இலக்கங்களின் கூடுதல் 3-ன் மடங்காக இருக்க வேண்டும்",
            explanationTa = "ஓர் எண்ணிலுள்ள அனைத்து இலக்கங்களையும் கூட்டி, அந்தக் கூடுதல் 3-ஆல் வகுபட்டால் முழு எண்ணும் 3-ஆல் வகுபடும்!",
            stepsTa = listOf(
                "படி 1: எண்ணிலுள்ள அனைத்து இலக்கங்களையும் கூட்டுங்கள்.",
                "படி 2: அந்தக் கூடுதல் 3, 6, 9, 12, 15 என 3-ன் வாய்ப்பாட்டில் வருகிறதா எனப் பாருங்கள்."
            ),
            examples = listOf(
                "423" to "4 + 2 + 3 = 9 (9 என்பது 3-ன் மடங்கு) ➔ 3-ஆல் வகுபடும்!",
                "518" to "5 + 1 + 8 = 14 (14 மூன்றால் வகுபடாது) ➔ வகுபடாது!",
                "729" to "7 + 2 + 9 = 18 ➔ 1+8 = 9 ➔ 3-ஆல் வகுபடும்!"
            ),
            practiceQuestions = listOf("237 வகுபடுமா?", "652 வகுபடுமா?", "915 வகுபடுமா?"),
            category = MathSecretCategory.DIVISION
        ),
        MentalMathSecret(
            number = 66,
            titleTa = "4-ஆல் வகுபடும் விதி (Divisibility by 4)",
            titleEn = "Divisibility Rule for 4",
            formulaTa = "கடைசி இரு இலக்கங்கள் 4-ஆல் வகுபட வேண்டும்",
            explanationTa = "எவ்வளவு பெரிய எண்ணாக இருந்தாலும், அதன் கடைசி இரண்டு இலக்கங்கள் 4-ஆல் வகுபட்டால் அல்லது 00 என முடிந்தால் அது 4-ஆல் வகுபடும்.",
            stepsTa = listOf(
                "படி 1: எண்ணின் கடைசி இரண்டு இலக்கங்களை மட்டும் தனியே எடுங்கள்.",
                "படி 2: அதை இருமுறை பாதியாக்குங்கள்; முழு எண் வந்தால் 4-ஆல் வகுபடும்!"
            ),
            examples = listOf(
                "5324" to "கடைசி இரு இலக்கம் 24; 24 ÷ 4 = 6 ➔ 4-ஆல் வகுபடும்!",
                "7818" to "கடைசி இரு இலக்கம் 18; 18 என்பது 4-ஆல் வகுபடாது ➔ வகுபடாது!",
                "9500" to "கடைசி இரு இலக்கம் 00 ➔ 4-ஆல் வகுபடும்!"
            ),
            practiceQuestions = listOf("3416 வகுபடுமா?", "7822 வகுபடுமா?", "9136 வகுபடுமா?"),
            category = MathSecretCategory.DIVISION
        ),
        MentalMathSecret(
            number = 67,
            titleTa = "5 மற்றும் 10-ஆல் வகுபடும் விதிகள்",
            titleEn = "Divisibility Rules for 5 and 10",
            formulaTa = "5-க்கு: கடைசி 0 அல்லது 5; 10-க்கு: கடைசி 0",
            explanationTa = "5-ஆல் வகுபட ஒன்றுகள் ஸ்தானம் 0 அல்லது 5 ஆக இருக்க வேண்டும். 10-ஆல் வகுபட கடைசி எண் கண்டிப்பாக 0 ஆக இருக்க வேண்டும்.",
            stepsTa = listOf(
                "படி 1: கடைசி இலக்கத்தைப் பாருங்கள்.",
                "படி 2: 0 அல்லது 5 என்றால் 5-ஆல் வகுபடும்; 0 என்றால் 10-ஆலும் வகுபடும்."
            ),
            examples = listOf(
                "475" to "கடைசி 5 ➔ 5-ஆல் வகுபடும், 10-ஆல் வகுபடாது.",
                "890" to "கடைசி 0 ➔ 5 மற்றும் 10 ஆகிய இரண்டாலும் வகுபடும்!",
                "632" to "கடைசி 2 ➔ 5 மற்றும் 10 இரண்டாலும் வகுபடாது."
            ),
            practiceQuestions = listOf("685 வகுபடுமா?", "940 வகுபடுமா?"),
            category = MathSecretCategory.DIVISION
        ),
        MentalMathSecret(
            number = 68,
            titleTa = "6-ஆல் வகுபடும் விதி: 2 மற்றும் 3-ன் கூட்டணி!",
            titleEn = "Divisibility Rule for 6",
            formulaTa = "2-ஆலும் வகுபட வேண்டும் AND 3-ஆலும் வகுபட வேண்டும்",
            explanationTa = "ஓர் எண் இரட்டைப்படை எண்ணாகவும் இருக்க வேண்டும், அதேசமயம் அதன் இலக்கங்களின் கூடுதல் 3-ன் மடங்காகவும் இருந்தால் மட்டுமே 6-ஆல் வகுபடும்.",
            stepsTa = listOf(
                "படி 1: எண் இரட்டைப்படை எண்ணா எனப் பாருங்கள் (கடைசி 0,2,4,6,8).",
                "படி 2: இலக்கங்களைக் கூட்டி 3-ஆல் வகுபடுகிறதா எனப் பாருங்கள்."
            ),
            examples = listOf(
                "372" to "இரட்டை எண் (சரி); 3+7+2 = 12 (3-ன் மடங்கு) ➔ 6-ஆல் வகுபடும்!",
                "453" to "ஒற்றைப்படை எண் ➔ 6-ஆல் வகுபடாது!",
                "514" to "இரட்டை எண் (சரி); 5+1+4 = 10 (3-ஆல் வகுபடாது) ➔ வகுபடாது!"
            ),
            practiceQuestions = listOf("258 வகுபடுமா?", "462 வகுபடுமா?", "574 வகுபடுமா?"),
            category = MathSecretCategory.DIVISION
        ),
        MentalMathSecret(
            number = 69,
            titleTa = "8-ஆல் வகுபடும் விதி (Divisibility by 8)",
            titleEn = "Divisibility Rule for 8",
            formulaTa = "கடைசி மூன்று இலக்கங்கள் 8-ஆல் வகுபட வேண்டும்",
            explanationTa = "பெரிய எண்களின் கடைசி மூன்று இலக்கங்கள் 8-ஆல் வகுபட்டால் அல்லது 000 என முடிந்தால் முழு எண்ணும் 8-ஆல் வகுபடும்.",
            stepsTa = listOf(
                "படி 1: கடைசி மூன்று இலக்கங்களை மட்டும் கவனியுங்கள்.",
                "படி 2: அதை மூன்று முறை பாதியாக்கிப் பாருங்கள்."
            ),
            examples = listOf(
                "45128" to "128 ÷ 8 = 16 ➔ முழு எண்ணும் 8-ஆல் வகுபடும்!",
                "23000" to "கடைசி மூன்று 000 ➔ 8-ஆல் வகுபடும்!",
                "17114" to "114 ÷ 8 வகுபடாது ➔ வகுபடாது!"
            ),
            practiceQuestions = listOf("34248 வகுபடுமா?", "56120 வகுபடுமா?"),
            category = MathSecretCategory.DIVISION
        ),
        MentalMathSecret(
            number = 70,
            titleTa = "9-ஆல் வகுபடும் விதி (Divisibility by 9)",
            titleEn = "Divisibility Rule for 9",
            formulaTa = "இலக்கங்களின் கூடுதல் 9, 18, 27 என 9-ன் மடங்காக வர வேண்டும்",
            explanationTa = "எண்ணிலுள்ள அனைத்து இலக்கங்களையும் கூட்டிக்கொண்டே போனால் இறுதியில் 9 வந்தால் மட்டுமே அந்த எண் 9-ஆல் முழுமையாக வகுபடும்!",
            stepsTa = listOf(
                "படி 1: அனைத்து இலக்கங்களையும் கூட்டுங்கள்.",
                "படி 2: வரும் விடையின் இலக்கங்களையும் மீண்டும் கூட்டி ஒற்றை எண்ணாக்குங்கள்; 9 வந்தால் வகுபடும்!"
            ),
            examples = listOf(
                "549" to "5 + 4 + 9 = 18 ➔ 1 + 8 = 9 ➔ 9-ஆல் வகுபடும்!",
                "738" to "7 + 3 + 8 = 18 ➔ 1 + 8 = 9 ➔ 9-ஆல் வகுபடும்!",
                "463" to "4 + 6 + 3 = 13 ➔ 1 + 3 = 4 ➔ வகுபடாது!"
            ),
            practiceQuestions = listOf("819 வகுபடுமா?", "648 வகுபடுமா?", "573 வகுபடுமா?"),
            category = MathSecretCategory.DIVISION
        ),
        MentalMathSecret(
            number = 71,
            titleTa = "11-ஆல் வகுபடும் விதி: மாற்று இலக்கங்களின் வித்தியாசம்",
            titleEn = "Divisibility Rule for 11",
            formulaTa = "(ஒற்றை ஸ்தான கூடுதல்) - (இரட்டை ஸ்தான கூடுதல்) = 0 அல்லது 11",
            explanationTa = "ஒன்று விட்டு ஒன்று இலக்கங்களைக் கூட்டி, அவ்விரண்டு கூடுதல்களின் வித்தியாசம் 0 அல்லது 11-ன் மடங்காக வந்தால் அது 11-ஆல் வகுபடும்.",
            stepsTa = listOf(
                "படி 1: 1-வது, 3-வது, 5-வது இலக்கங்களைக் கூட்டுங்கள் (A).",
                "படி 2: 2-வது, 4-வது இலக்கங்களைக் கூட்டுங்கள் (B).",
                "படி 3: A - B காண்க; 0 அல்லது 11 வந்தால் 11-ஆல் வகுபடும்!"
            ),
            examples = listOf(
                "1331" to "(1+3) - (3+1) = 4 - 4 = 0 ➔ 11-ஆல் வகுபடும்!",
                "2816" to "(2+1) = 3; (8+6) = 14 ➔ 14 - 3 = 11 ➔ 11-ஆல் வகுபடும்!",
                "5423" to "(5+2)=7; (4+3)=7 ➔ 7 - 7 = 0 ➔ 11-ஆல் வகுபடும்!"
            ),
            practiceQuestions = listOf("14641 வகுபடுமா?", "3572 வகுபடுமா?"),
            category = MathSecretCategory.DIVISION
        ),
        MentalMathSecret(
            number = 72,
            titleTa = "12-ஆல் வகுபடும் விதி: 3 மற்றும் 4-ன் கூட்டணி",
            titleEn = "Divisibility Rule for 12",
            formulaTa = "3-ஆலும் வகுபட வேண்டும் AND 4-ஆலும் வகுபட வேண்டும்",
            explanationTa = "12 = 3 × 4. ஓர் எண் 3 மற்றும் 4 ஆகிய இரண்டாலும் வகுபட்டால் மட்டுமே அது 12-ஆல் வகுபடும்.",
            stepsTa = listOf(
                "படி 1: கடைசி இரு இலக்கங்கள் 4-ஆல் வகுபடுகிறதா எனப் பாருங்கள்.",
                "படி 2: அனைத்து இலக்கங்களின் கூடுதல் 3-ஆல் வகுபடுகிறதா எனப் பாருங்கள்."
            ),
            examples = listOf(
                "348" to "48 என்பது 4-ஆல் வகுபடும்; 3+4+8 = 15 (3-ஆல் வகுபடும்) ➔ 12-ஆல் வகுபடும்!",
                "524" to "24 என்பது 4-ஆல் வகுபடும்; ஆனால் 5+2+4 = 11 (3-ஆல் வகுபடாது) ➔ வகுபடாது!"
            ),
            practiceQuestions = listOf("432 வகுபடுமா?", "624 வகுபடுமா?"),
            category = MathSecretCategory.DIVISION
        ),
        MentalMathSecret(
            number = 73,
            titleTa = "15-ஆல் வகுபடும் விதி: 3 மற்றும் 5-ன் கூட்டணி",
            titleEn = "Divisibility Rule for 15",
            formulaTa = "3-ஆலும் வகுபட வேண்டும் AND 5-ஆலும் வகுபட வேண்டும்",
            explanationTa = "கடைசி எண் 0 அல்லது 5 ஆக இருக்க வேண்டும்; மேலும் இலக்கங்களின் கூடுதல் 3-ன் மடங்காக இருக்க வேண்டும்.",
            stepsTa = listOf(
                "படி 1: கடைசி எண் 0 அல்லது 5 என உறுதிசெய்யவும்.",
                "படி 2: இலக்கங்களைக் கூட்டி 3-ஆல் வகுபடுகிறதா எனப் பாருங்கள்."
            ),
            examples = listOf(
                "345" to "கடைசி 5 (5-ஆல் வகுபடும்); 3+4+5 = 12 (3-ஆல் வகுபடும்) ➔ 15-ஆல் வகுபடும்!",
                "480" to "கடைசி 0; 4+8+0 = 12 ➔ 15-ஆல் வகுபடும்!"
            ),
            practiceQuestions = listOf("555 வகுபடுமா?", "620 வகுபடுமா?"),
            category = MathSecretCategory.DIVISION
        ),
        MentalMathSecret(
            number = 74,
            titleTa = "மீதி காணும் மின்னல் குறுக்குவழி (Quick Remainder)",
            titleEn = "Mental Remainder Finding",
            formulaTa = "அருகிலுள்ள பெருக்கல் மடங்கைக் கழித்தல்",
            explanationTa = "வகுக்காமல் மனதிலேயே மிக அருகிலுள்ள வாய்ப்பாட்டு எண்ணைக் கழித்து மீதியைக் கணக்கிடலாம்.",
            stepsTa = listOf(
                "படி 1: வகுக்கும் எண்ணின் மிக அருகிலுள்ள மடங்கைக் கண்டறியுங்கள்.",
                "படி 2: கொடுக்கப்பட்ட எண்ணிலிருந்து அதைக் கழிக்கவும்; மீதி உடனே கிடைக்கும்!"
            ),
            examples = listOf(
                "38 ÷ 5-ன் மீதி" to "38-க்கு முந்தைய 5-ன் மடங்கு 35 ➔ 38 - 35 = 3 மீதி",
                "47 ÷ 6-ன் மீதி" to "6 × 7 = 42 ➔ 47 - 42 = 5 மீதி",
                "59 ÷ 8-ன் மீதி" to "8 × 7 = 56 ➔ 59 - 56 = 3 மீதி"
            ),
            practiceQuestions = listOf("29 ÷ 4 மீதி என்ன?", "53 ÷ 7 மீதி என்ன?"),
            category = MathSecretCategory.DIVISION
        ),
        MentalMathSecret(
            number = 75,
            titleTa = "பிரித்து வகுத்தல் முறை (Division by Chunking)",
            titleEn = "Mental Division by Splitting",
            formulaTa = "(A + B) ÷ d = (A ÷ d) + (B ÷ d)",
            explanationTa = "பெரிய எண்களை எளிதில் வகுபடும் இரு எண்களாகப் பிரித்து மனக்கணக்கில் எளிதாக வகுக்கலாம்.",
            stepsTa = listOf(
                "படி 1: எண்ணை வாய்ப்பாட்டில் வரும் இரு எண்களாகப் பிரியுங்கள் (எ.கா: 96 = 60 + 36).",
                "படி 2: ஒவ்வொன்றையும் வகுக்கவும் (60÷6=10, 36÷6=6).",
                "படி 3: இரண்டையும் கூட்டவும்: 10 + 6 = 16!"
            ),
            examples = listOf(
                "96 ÷ 6" to "(60 ÷ 6) + (36 ÷ 6) = 10 + 6 = 16",
                "84 ÷ 4" to "(80 ÷ 4) + (4 ÷ 4) = 20 + 1 = 21",
                "115 ÷ 5" to "(100 ÷ 5) + (15 ÷ 5) = 20 + 3 = 23"
            ),
            practiceQuestions = listOf("92 ÷ 4 = ?", "108 ÷ 6 = ?", "126 ÷ 7 = ?"),
            category = MathSecretCategory.DIVISION
        )
    )

    val fractionsSecrets: List<MentalMathSecret> = listOf(
        MentalMathSecret(
            number = 76,
            titleTa = "வண்ணத்துப்பூச்சி முறை பின்னக் கூட்டல் (Butterfly Method)",
            titleEn = "Butterfly Method for Fraction Addition",
            formulaTa = "(a/b) + (c/d) = (ad + bc) / bd",
            explanationTa = "குறுக்காகப் பெருக்கி கூட்டிக் மேலே எழுதி, கீழே உள்ள பகுதிகளைப் பெருக்கி கீழே எழுதினால் எல்.சி.எம் (LCM) பார்க்காமலேயே விடை வரும்!",
            stepsTa = listOf(
                "படி 1: குறுக்கு பெருக்கல்: (a × d) மற்றும் (b × c).",
                "படி 2: இரண்டையும் கூட்டி தொகுதியில் (மேலே) எழுதுங்கள்.",
                "படி 3: கீழே உள்ள இரு எண்களையும் பெருக்கி பகுதியில் (கீழே) எழுதுங்கள்."
            ),
            examples = listOf(
                "1/3 + 2/5" to "(1×5 + 3×2) / (3×5) = (5 + 6) / 15 = 11/15",
                "2/7 + 1/4" to "(2×4 + 7×1) / (7×4) = (8 + 7) / 28 = 15/28",
                "1/2 + 1/3" to "(3 + 2) / 6 = 5/6"
            ),
            practiceQuestions = listOf("1/4 + 2/3 = ?", "3/5 + 1/4 = ?", "2/9 + 1/2 = ?"),
            category = MathSecretCategory.FRACTIONS_PERCENTAGES
        ),
        MentalMathSecret(
            number = 77,
            titleTa = "வண்ணத்துப்பூச்சி முறை பின்னக் கழித்தல்",
            titleEn = "Butterfly Method for Fraction Subtraction",
            formulaTa = "(a/b) - (c/d) = (ad - bc) / bd",
            explanationTa = "பின்னக் கூட்டல் போலவே குறுக்காகப் பெருக்கி, மேலே மட்டும் கழித்து கீழே பகுதிகளைப் பெருக்க வேண்டும்.",
            stepsTa = listOf(
                "படி 1: குறுக்கு பெருக்கல்: (a × d) மற்றும் (b × c).",
                "படி 2: முதல் பெருக்கலிலிருந்து இரண்டாம் பெருக்கலைக் கழிக்கவும் (ad - bc).",
                "படி 3: பகுதிகளைப் பெருக்கி கீழே எழுதவும் (bd)."
            ),
            examples = listOf(
                "3/4 - 1/3" to "(3×3 - 4×1) / (4×3) = (9 - 4) / 12 = 5/12",
                "4/5 - 1/2" to "(4×2 - 5×1) / (5×2) = (8 - 5) / 10 = 3/10",
                "2/3 - 1/4" to "(8 - 3) / 12 = 5/12"
            ),
            practiceQuestions = listOf("5/6 - 1/2 = ?", "3/4 - 2/5 = ?"),
            category = MathSecretCategory.FRACTIONS_PERCENTAGES
        ),
        MentalMathSecret(
            number = 78,
            titleTa = "பின்னப் பெருக்கல்: நேருக்கு நேர் பெருக்கல்",
            titleEn = "Straight Across Fraction Multiplication",
            formulaTa = "(a/b) × (c/d) = (a × c) / (b × d)",
            explanationTa = "பின்னங்களைப் பெருக்க மீ.சி.ம தேவையில்லை; மேல் எண்களை நேராகவும், கீழ் எண்களை நேராகவும் பெருக்கினால் போதும்!",
            stepsTa = listOf(
                "படி 1: தொகுதிகளை நேருக்கு நேர் பெருக்குங்கள் (a × c).",
                "படி 2: பகுதிகளை நேருக்கு நேர் பெருக்குங்கள் (b × d).",
                "படி 3: முடிந்தால் சுருக்கவும்."
            ),
            examples = listOf(
                "2/3 × 4/5" to "(2 × 4) / (3 × 5) = 8/15",
                "3/7 × 2/5" to "(3 × 2) / (7 × 5) = 6/35",
                "1/2 × 4/6" to "4/12 = 1/3"
            ),
            practiceQuestions = listOf("3/4 × 2/5 = ?", "5/7 × 1/3 = ?"),
            category = MathSecretCategory.FRACTIONS_PERCENTAGES
        ),
        MentalMathSecret(
            number = 79,
            titleTa = "பின்ன வகுத்தல்: தலைகீழாக்கி பெருக்குங்கள் (Keep, Change, Flip)",
            titleEn = "Fraction Division: Keep, Change, Flip",
            formulaTa = "(a/b) ÷ (c/d) = (a/b) × (d/c)",
            explanationTa = "முதல் பின்னத்தை அப்படியே வைத்து, வகுத்தலை பெருக்கலாக்கி, இரண்டாம் பின்னத்தைத் தலைகீழாக மாற்றவும்!",
            stepsTa = listOf(
                "படி 1: முதல் பின்னத்தை அப்படியே வைக்கவும் (Keep).",
                "படி 2: வகுத்தல் குறியை பெருக்கலாக மாற்றவும் (Change).",
                "படி 3: இரண்டாம் பின்னத்தைத் தலைகீழாக மாற்றிப் பெருக்கவும் (Flip)."
            ),
            examples = listOf(
                "2/3 ÷ 4/5" to "2/3 × 5/4 = 10/12 = 5/6",
                "1/2 ÷ 1/4" to "1/2 × 4/1 = 4/2 = 2",
                "3/5 ÷ 2/7" to "3/5 × 7/2 = 21/10"
            ),
            practiceQuestions = listOf("3/4 ÷ 1/2 = ?", "2/5 ÷ 3/4 = ?"),
            category = MathSecretCategory.FRACTIONS_PERCENTAGES
        ),
        MentalMathSecret(
            number = 80,
            titleTa = "50% காணுதல்: நேராகப் பாதியாக்குங்கள்!",
            titleEn = "Finding 50%: Just Divide by 2",
            formulaTa = "50% of n = n ÷ 2",
            explanationTa = "50% என்பது பாதி (1/2). எந்த எண்ணின் 50% காணவும் அந்த எண்ணை 2-ஆல் வகுத்தால் போதும்.",
            stepsTa = listOf(
                "படி 1: கொடுக்கப்பட்ட எண்ணை 2-ஆல் வகுத்து பாதியாக்குங்கள்."
            ),
            examples = listOf(
                "50% of 84" to "84 ÷ 2 = 42",
                "50% of 150" to "150 ÷ 2 = 75",
                "50% of 420" to "420 ÷ 2 = 210"
            ),
            practiceQuestions = listOf("50% of 68 = ?", "50% of 240 = ?"),
            category = MathSecretCategory.FRACTIONS_PERCENTAGES
        ),
        MentalMathSecret(
            number = 81,
            titleTa = "25% காணுதல்: கால் பங்கு (4-ஆல் வகுத்தல்)",
            titleEn = "Finding 25%: Divide by 4 (Half of Half)",
            formulaTa = "25% of n = (n ÷ 2) ÷ 2",
            explanationTa = "25% என்பது கால் பகுதி (1/4). எண்ணை ஒருமுறை பாதியாக்கி, மீண்டும் ஒருமுறை பாதியாக்கினால் 25% விடை!",
            stepsTa = listOf(
                "படி 1: எண்ணைப் பாதியாக்குங்கள் (50%).",
                "படி 2: வந்த விடையை மீண்டும் பாதியாக்குங்கள் (25%)."
            ),
            examples = listOf(
                "25% of 80" to "பாதி 40; 40-ன் பாதி 20",
                "25% of 160" to "பாதி 80; 80-ன் பாதி 40",
                "25% of 44" to "44 ÷ 4 = 11"
            ),
            practiceQuestions = listOf("25% of 120 = ?", "25% of 200 = ?"),
            category = MathSecretCategory.FRACTIONS_PERCENTAGES
        ),
        MentalMathSecret(
            number = 82,
            titleTa = "10% காணுதல்: ஒரு ஸ்தானம் தள்ளி புள்ளி வைக்கவும்",
            titleEn = "Finding 10%: Move Decimal Left by 1",
            formulaTa = "10% of n = n ÷ 10",
            explanationTa = "எந்த ஓர் எண்ணின் 10% காணவும், வலது ஓரத்திலிருந்து ஒரு ஸ்தானம் தள்ளி புள்ளி வைத்தால் போதும்!",
            stepsTa = listOf(
                "படி 1: எண்ணின் கடைசி பூஜ்ஜியத்தை நீக்கவும் அல்லது ஒரு ஸ்தானம் தள்ளி புள்ளி வைக்கவும்."
            ),
            examples = listOf(
                "10% of 450" to "45",
                "10% of 82" to "8.2",
                "10% of 1250" to "125"
            ),
            practiceQuestions = listOf("10% of 340 = ?", "10% of 95 = ?"),
            category = MathSecretCategory.FRACTIONS_PERCENTAGES
        ),
        MentalMathSecret(
            number = 83,
            titleTa = "5% காணுதல்: 10% கண்டு அதில் பாதியெடுக்கவும்",
            titleEn = "Finding 5%: Half of 10%",
            formulaTa = "5% of n = (10% of n) ÷ 2",
            explanationTa = "5% என்பது 10%-ன் பாதி. முதலில் 10% கண்டுபிடித்து அதில் பாதியெடுத்தால் 5% உடனே கிடைக்கும்.",
            stepsTa = listOf(
                "படி 1: எண்ணின் 10%-ஐக் காணுங்கள்.",
                "படி 2: அதை 2-ஆல் வகுத்து பாதியாக்குங்கள்."
            ),
            examples = listOf(
                "5% of 80" to "10% = 8 ➔ 8-ன் பாதி = 4",
                "5% of 240" to "10% = 24 ➔ 24-ன் பாதி = 12",
                "5% of 70" to "10% = 7 ➔ 7-ன் பாதி = 3.5"
            ),
            practiceQuestions = listOf("5% of 60 = ?", "5% of 180 = ?"),
            category = MathSecretCategory.FRACTIONS_PERCENTAGES
        ),
        MentalMathSecret(
            number = 84,
            titleTa = "20% காணுதல்: 10% கண்டு இரட்டிப்பாக்கவும்",
            titleEn = "Finding 20%: Double of 10%",
            formulaTa = "20% of n = (10% of n) × 2",
            explanationTa = "20% என்பது 10%-ன் இரட்டிப்பு. 10% கண்டு அதை 2-ஆல் பெருக்கினால் விடை!",
            stepsTa = listOf(
                "படி 1: எண்ணின் 10%-ஐக் காணுங்கள்.",
                "படி 2: அதை 2-ஆல் பெருக்குங்கள்."
            ),
            examples = listOf(
                "20% of 60" to "10% = 6 ➔ 6 × 2 = 12",
                "20% of 150" to "10% = 15 ➔ 15 × 2 = 30",
                "20% of 85" to "10% = 8.5 ➔ 8.5 × 2 = 17"
            ),
            practiceQuestions = listOf("20% of 90 = ?", "20% of 220 = ?"),
            category = MathSecretCategory.FRACTIONS_PERCENTAGES
        ),
        MentalMathSecret(
            number = 85,
            titleTa = "75% காணுதல்: 50% மற்றும் 25%-ஐக் கூட்டுங்கள்",
            titleEn = "Finding 75%: 50% + 25%",
            formulaTa = "75% of n = (50% of n) + (25% of n)",
            explanationTa = "75% என்பது முக்கால் பங்கு. பாதியையும் (50%) காலையும் (25%) கூட்டிவிட்டால் 75% வந்துவிடும்!",
            stepsTa = listOf(
                "படி 1: எண்ணின் பாதியைக் காணவும் (50%).",
                "படி 2: அந்தப் பாதியின் பாதியைக் காணவும் (25%).",
                "படி 3: இரண்டையும் கூட்டினால் 75%!"
            ),
            examples = listOf(
                "75% of 80" to "50% = 40; 25% = 20 ➔ 40 + 20 = 60",
                "75% of 120" to "50% = 60; 25% = 30 ➔ 60 + 30 = 90",
                "75% of 40" to "50% = 20; 25% = 10 ➔ 20 + 10 = 30"
            ),
            practiceQuestions = listOf("75% of 160 = ?", "75% of 240 = ?"),
            category = MathSecretCategory.FRACTIONS_PERCENTAGES
        )
    )

    val squaresMagicSecrets: List<MentalMathSecret> = listOf(
        MentalMathSecret(
            number = 86,
            titleTa = "5-ல் முடியும் எண்களை வர்க்கம் காணுதல்",
            titleEn = "Squaring Numbers Ending in 5",
            formulaTa = "பத்து × (பத்து + 1) | 25",
            explanationTa = "5-ல் முடியும் எந்த ஓர் எண்ணையும் வர்க்கப்படுத்த, அதன் முதல் எண்ணை அதன் அடுத்த எண்ணால் பெருக்கி, இறுதியில் 25-ஐச் சேர்த்தால் போதும்!",
            stepsTa = listOf(
                "படி 1: முதல் எண்ணை (பத்துகள்) குறித்துக் கொள்ளுங்கள்.",
                "படி 2: அந்த எண்ணை அதற்கு அடுத்த எண்ணால் பெருக்குங்கள் (n × (n+1)).",
                "படி 3: வரும் விடைக்கு அருகில் '25' என்று எழுதி முடியுங்கள்!"
            ),
            examples = listOf(
                "35²" to "3 × 4 = 12 ➔ விடை: 1225",
                "65²" to "6 × 7 = 42 ➔ விடை: 4225",
                "85²" to "8 × 9 = 72 ➔ விடை: 7225"
            ),
            practiceQuestions = listOf("25² = ?", "45² = ?", "75² = ?", "95² = ?"),
            category = MathSecretCategory.SQUARES_MAGIC
        ),
        MentalMathSecret(
            number = 87,
            titleTa = "11 முதல் 19 வரை வர்க்கம் காணும் குறுக்குவழி",
            titleEn = "Squaring Numbers from 11 to 19",
            formulaTa = "(எண் + ஒன்றுகள்) × 10 + ஒன்றுகள்²",
            explanationTa = "11 முதல் 19 வரையிலான எண்களை வர்க்கப்படுத்த, கொடுக்கப்பட்ட எண்ணுடன் அதன் கடைசி இலக்கத்தைக் கூட்டி, 10-ஆல் பெருக்கி, கடைசி இலக்கத்தின் வர்க்கத்தைக் கூட்டவும்.",
            stepsTa = listOf(
                "படி 1: எண்ணுடன் அதன் கடைசி இலக்கத்தைக் கூட்டுங்கள்.",
                "படி 2: அதனுடன் '0' சேருங்கள்.",
                "படி 3: கடைசி இலக்கத்தின் வர்க்கத்தைக் கூட்டுங்கள்."
            ),
            examples = listOf(
                "13²" to "(13 + 3) = 16 ➔ 160; 3² = 9 ➔ 160 + 9 = 169",
                "14²" to "(14 + 4) = 18 ➔ 180; 4² = 16 ➔ 180 + 16 = 196",
                "16²" to "(16 + 6) = 22 ➔ 220; 6² = 36 ➔ 220 + 36 = 256"
            ),
            practiceQuestions = listOf("12² = ?", "15² = ?", "17² = ?"),
            category = MathSecretCategory.SQUARES_MAGIC
        ),
        MentalMathSecret(
            number = 88,
            titleTa = "50-க்கு அருகிலுள்ள எண்களின் வர்க்கம் (41 முதல் 49)",
            titleEn = "Squaring Numbers 41 to 49",
            formulaTa = "(25 - குறைவு) | (குறைவு²)",
            explanationTa = "50-லிருந்து எவ்வளவு குறைவு என்று பார்த்து, அதை 25-லிருந்து கழித்து, குறைவின் வர்க்கத்தை அருகில் எழுதினால் விடை!",
            stepsTa = listOf(
                "படி 1: எண் 50-லிருந்து எவ்வளவு குறைவாக உள்ளது எனப் பாருங்கள் (d = 50 - n).",
                "படி 2: 25 - d காண்க (முதல் இரு எண்கள்).",
                "படி 3: d² காண்க (ஈரிலக்கமாக எழுதவும்)."
            ),
            examples = listOf(
                "47²" to "குறைவு 3 ➔ (25 - 3) | 3² ➔ 22 | 09 = 2209",
                "46²" to "குறைவு 4 ➔ (25 - 4) | 4² ➔ 21 | 16 = 2116",
                "44²" to "குறைவு 6 ➔ (25 - 6) | 6² ➔ 19 | 36 = 1936"
            ),
            practiceQuestions = listOf("48² = ?", "45² = ?", "42² = ?"),
            category = MathSecretCategory.SQUARES_MAGIC
        ),
        MentalMathSecret(
            number = 89,
            titleTa = "51 முதல் 59 வரை வர்க்கம் காணும் ரகசியம்",
            titleEn = "Squaring Numbers 51 to 59",
            formulaTa = "(25 + ஒன்றுகள்) | (ஒன்றுகள்²)",
            explanationTa = "50-க்கு மேல் உள்ள எண்களுக்கு, 25 உடன் ஒன்றுகள் இலக்கத்தைக் கூட்டி, ஒன்றுகளின் வர்க்கத்தை அருகில் எழுதினால் விடை!",
            stepsTa = listOf(
                "படி 1: 25 உடன் ஒன்றுகள் இலக்கத்தைக் கூட்டுங்கள் (25 + d).",
                "படி 2: d-ன் வர்க்கத்தை ஈரிலக்கமாக அருகில் எழுதவும்."
            ),
            examples = listOf(
                "52²" to "(25 + 2) | 2² ➔ 27 | 04 = 2704",
                "54²" to "(25 + 4) | 4² ➔ 29 | 16 = 2916",
                "57²" to "(25 + 7) | 7² ➔ 32 | 49 = 3249"
            ),
            practiceQuestions = listOf("53² = ?", "56² = ?", "58² = ?"),
            category = MathSecretCategory.SQUARES_MAGIC
        ),
        MentalMathSecret(
            number = 90,
            titleTa = "100-க்கு அருகிலுள்ள எண்களின் வர்க்கம் (91 முதல் 99)",
            titleEn = "Squaring Numbers 91 to 99",
            formulaTa = "(எண் - குறைவு) | (குறைவு²)",
            explanationTa = "100-லிருந்து எவ்வளவு குறைவு எனப் பார்த்து, அதே குறைவை எண்ணிலிருந்தும் கழித்து, குறைவின் வர்க்கத்தை அருகில் எழுதவும்!",
            stepsTa = listOf(
                "படி 1: 100-லிருந்து எவ்வளவு குறைவு (d).",
                "படி 2: எண்ணிலிருந்து d-ஐக் கழிக்கவும் (n - d).",
                "படி 3: d²-ஐ ஈரிலக்கமாக எழுதவும்."
            ),
            examples = listOf(
                "96²" to "குறைவு 4 ➔ (96 - 4) | 4² = 92 | 16 = 9216",
                "97²" to "குறைவு 3 ➔ (97 - 3) | 3² = 94 | 09 = 9409",
                "94²" to "குறைவு 6 ➔ (94 - 6) | 6² = 88 | 36 = 8836"
            ),
            practiceQuestions = listOf("98² = ?", "95² = ?", "93² = ?"),
            category = MathSecretCategory.SQUARES_MAGIC
        ),
        MentalMathSecret(
            number = 91,
            titleTa = "101 முதல் 109 வரை வர்க்கம் காணும் ரகசியம்",
            titleEn = "Squaring Numbers 101 to 109",
            formulaTa = "(எண் + கூடுதல்) | (கூடுதல்²)",
            explanationTa = "100-ஐ விட எவ்வளவு அதிகமோ அதை எண்ணுடன் கூட்டி, அந்த அதிகத்தின் வர்க்கத்தை அருகில் எழுதினால் விடை!",
            stepsTa = listOf(
                "படி 1: 100-ஐ விட எவ்வளவு அதிகம் எனப் பாருங்கள் (d).",
                "படி 2: எண்ணுடன் d-ஐக் கூட்டுங்கள் (n + d).",
                "படி 3: d²-ஐ ஈரிலக்கமாக அருகில் எழுதவும்."
            ),
            examples = listOf(
                "103²" to "(103 + 3) | 3² = 106 | 09 = 10609",
                "105²" to "(105 + 5) | 5² = 110 | 25 = 11025",
                "107²" to "(107 + 7) | 7² = 114 | 49 = 11449"
            ),
            practiceQuestions = listOf("102² = ?", "104² = ?", "106² = ?"),
            category = MathSecretCategory.SQUARES_MAGIC
        ),
        MentalMathSecret(
            number = 92,
            titleTa = "அடுத்தடுத்த எண்களின் வர்க்க வித்தியாசம்",
            titleEn = "Difference of Consecutive Squares",
            formulaTa = "(n + 1)² - n² = (n + 1) + n",
            explanationTa = "அடுத்தடுத்த இரு எண்களின் வர்க்கங்களைக் கழிக்கத் தேவையில்லை; அவ்விரு எண்களையும் கூட்டினாலே வித்தியாசம் வந்துவிடும்!",
            stepsTa = listOf(
                "படி 1: கொடுக்கப்பட்ட இரு எண்களையும் நேராகக் கூட்டுங்கள்."
            ),
            examples = listOf(
                "15² - 14²" to "15 + 14 = 29",
                "25² - 24²" to "25 + 24 = 49",
                "50² - 49²" to "50 + 49 = 99"
            ),
            practiceQuestions = listOf("18² - 17² = ?", "30² - 29² = ?", "41² - 40² = ?"),
            category = MathSecretCategory.SQUARES_MAGIC
        ),
        MentalMathSecret(
            number = 93,
            titleTa = "1-ல் முடியும் எண்களின் வர்க்கம் காணுதல்",
            titleEn = "Squaring Numbers Ending in 1",
            formulaTa = "(n - 1)² + 2n - 1",
            explanationTa = "முந்தைய முழு பத்தின் வர்க்கத்தைக் கண்டு, அதனுடன் இரு எண்களையும் கூட்டினால் விடை!",
            stepsTa = listOf(
                "படி 1: முந்தைய பத்தின் வர்க்கத்தைக் காணுங்கள் (எ.கா: 30² = 900).",
                "படி 2: அதனுடன் 30 மற்றும் 31-ஐக் கூட்டுங்கள் (900 + 30 + 31 = 961)."
            ),
            examples = listOf(
                "31²" to "30² + 30 + 31 = 900 + 61 = 961",
                "41²" to "40² + 40 + 41 = 1600 + 81 = 1681",
                "51²" to "50² + 50 + 51 = 2500 + 101 = 2601"
            ),
            practiceQuestions = listOf("21² = ?", "61² = ?", "71² = ?"),
            category = MathSecretCategory.SQUARES_MAGIC
        ),
        MentalMathSecret(
            number = 94,
            titleTa = "வர்க்கமூலம் அனுமானிக்கும் ரகசியம் (Square Root Estimation)",
            titleEn = "Estimating Square Roots",
            formulaTa = "அருகிலுள்ள முழு வர்க்கத்தைக் கொண்டு யூகித்தல்",
            explanationTa = "ஒரு எண் எந்த இரு முழு வர்க்கங்களுக்கு இடையில் வருகிறது மற்றும் கடைசி இலக்கம் என்ன என்பதை வைத்து வர்க்கமூலத்தை நொடியில் யூகிக்கலாம்.",
            stepsTa = listOf(
                "படி 1: எண் எந்த இரு பத்தின் வர்க்கங்களுக்கு இடையில் உள்ளது எனப் பாருங்கள்.",
                "படி 2: கடைசி இலக்கம் 4 எனில் வர்க்கமூலம் 2 அல்லது 8-ல் முடியும் என முடிவு செய்யுங்கள்."
            ),
            examples = listOf(
                "√144" to "10²=100 மற்றும் 20²=400; கடைசி எண் 4 ➔ விடை: 12",
                "√1024" to "30²=900; 32² ➔ கடைசி எண் 4 ➔ விடை: 32",
                "√2025" to "கடைசி 25 (5-ல் முடியும்); 4 × 5 = 20 ➔ விடை: 45"
            ),
            practiceQuestions = listOf("√256 = ?", "√576 = ?", "√1225 = ?"),
            category = MathSecretCategory.SQUARES_MAGIC
        ),
        MentalMathSecret(
            number = 95,
            titleTa = "காலண்டர் மேஜிக்: 3x3 கட்டத்தின் கூடுதல்",
            titleEn = "Calendar Magic: 3x3 Grid Sum",
            formulaTa = "நடுவில் உள்ள எண் × 9",
            explanationTa = "காலண்டரில் ஏதேனும் 3x3 கட்டத்தில் உள்ள 9 எண்களைக் கூட்டச் சொன்னால், நடுவில் உள்ள எண்ணை 9-ஆல் பெருக்கினால் மொத்த கூடுதல் நொடியில் கிடைக்கும்!",
            stepsTa = listOf(
                "படி 1: நண்பரிடம் காலண்டரில் 3x3 கட்டம் போடச் சொல்லுங்கள்.",
                "படி 2: நடுவில் உள்ள எண்ணை மட்டும் கேளுங்கள்.",
                "படி 3: அதை 9-ஆல் பெருக்கிக் கூடுதலைக் கூறி ஆச்சரியப்படுத்துங்கள்!"
            ),
            examples = listOf(
                "நடு எண் 15 எனில்" to "9 எண்களின் மொத்த கூடுதல் = 15 × 9 = 135!",
                "நடு எண் 11 எனில்" to "மொத்த கூடுதல் = 11 × 9 = 99!",
                "நடு எண் 18 எனில்" to "மொத்த கூடுதல் = 18 × 9 = 162!"
            ),
            practiceQuestions = listOf("நடு எண் 14 எனில் கூடுதல் என்ன?", "நடு எண் 20 எனில் கூடுதல் என்ன?"),
            category = MathSecretCategory.SQUARES_MAGIC
        ),
        MentalMathSecret(
            number = 96,
            titleTa = "மேஜிக் எண் 1089 (Math Magic 1089)",
            titleEn = "The Famous 1089 Magic Number",
            formulaTa = "(abc - cba) + Reverse = 1089 எப்போதும்!",
            explanationTa = "வேறுபட்ட மூன்று இலக்கங்களை எடுத்து, தலைகீழாக்கி கழித்து, வந்த விடையை மீண்டும் தலைகீழாக்கிக் கூட்டினால் எப்போதுமே 1089 வரும்!",
            stepsTa = listOf(
                "படி 1: 3 இலக்க எண் தேர்வு செய்க (எ.கா: 732).",
                "படி 2: தலைகீழாக்கி கழிக்கவும் (732 - 237 = 495).",
                "படி 3: வந்த விடையைத் தலைகீழாக்கிக் கூட்டவும் (495 + 594 = 1089)!"
            ),
            examples = listOf(
                "852" to "852 - 258 = 594; 594 + 495 = 1089!",
                "641" to "641 - 146 = 495; 495 + 594 = 1089!",
                "931" to "931 - 139 = 792; 792 + 297 = 1089!"
            ),
            practiceQuestions = listOf("521-க்கு செய்து பாருங்கள்", "742-க்கு செய்து பாருங்கள்"),
            category = MathSecretCategory.SQUARES_MAGIC
        ),
        MentalMathSecret(
            number = 97,
            titleTa = "வயதைக் கண்டுபிடிக்கும் கணித மேஜிக்",
            titleEn = "Age Guessing Math Trick",
            formulaTa = "((வயது × 2) + 5) × 50 + 1774 - பிறந்த வருடம்",
            explanationTa = "நண்பரிடம் சில எளிய கணக்குகளை செய்யச் சொல்லி, இறுதியில் அவரது வயதை சரியாகச் சொல்லும் அற்புதமான வித்தை!",
            stepsTa = listOf(
                "படி 1: நண்பரின் வயதை 2-ஆல் பெருக்கச் சொல்லுங்கள்.",
                "படி 2: அதனுடன் 5-ஐக் கூட்டி, 50-ஆல் பெருக்கச் சொல்லுங்கள்.",
                "படி 3: 1774 கூட்டி, பிறந்த வருடத்தைக் கழித்தால் வரும் எண்ணின் கடைசி இரு இலக்கங்கள் அவரது வயது!"
            ),
            examples = listOf(
                "வயது 12 எனில்" to "12 × 2 = 24 ➔ 24 + 5 = 29 ➔ 29 × 50 = 1450... விடையின் இறுதியில் 12 வரும்!"
            ),
            practiceQuestions = listOf("உங்கள் வயதுக்கு இந்த வித்தையைச் செய்து பாருங்கள்!"),
            category = MathSecretCategory.SQUARES_MAGIC
        ),
        MentalMathSecret(
            number = 98,
            titleTa = "9-ன் மாயம்: 9-ஆல் பெருக்கினால் இலக்கங்களின் கூடுதல் எப்போதும் 9!",
            titleEn = "The Mystery of Number 9",
            formulaTa = "Digital Root of (9 × any number) = 9",
            explanationTa = "நீங்கள் எந்தவொரு முழு எண்ணையும் 9-ஆல் பெருக்கினாலும், விடையின் இலக்கங்களைக் கூட்டிக்கொண்டே போனால் எப்போதுமே 9 மட்டுமே வரும்!",
            stepsTa = listOf(
                "படி 1: ஏதேனும் ஓர் எண்ணை 9-ஆல் பெருக்குங்கள்.",
                "படி 2: வந்த விடையிலுள்ள அனைத்து இலக்கங்களையும் கூட்டுங்கள்; விடை 9 வரும்!"
            ),
            examples = listOf(
                "9 × 7 = 63" to "6 + 3 = 9",
                "9 × 48 = 432" to "4 + 3 + 2 = 9",
                "9 × 375 = 3375" to "3 + 3 + 7 + 5 = 18 ➔ 1 + 8 = 9"
            ),
            practiceQuestions = listOf("9 × 86-ன் இலக்க கூடுதல் காண்க", "9 × 124-ன் இலக்க கூடுதல் காண்க"),
            category = MathSecretCategory.SQUARES_MAGIC
        ),
        MentalMathSecret(
            number = 99,
            titleTa = "வட்ட வடிவில் எண்களைக் கூட்டும் கடிகார கணிதம் (Modular Clock Math)",
            titleEn = "Clock Arithmetic Trick",
            formulaTa = "12 மணி நேர சுழற்சி (Mod 12)",
            explanationTa = "கடிகாரத்தில் 9 மணிக்கு மேல் 5 மணி நேரம் கழித்தால் என்ன நேரம் என்று கூட்ட, 9 + 5 = 14; 14 - 12 = 2 மணி என மனக்கணக்கில் சொல்லலாம்.",
            stepsTa = listOf(
                "படி 1: நேரங்களைக் கூட்டுங்கள்.",
                "படி 2: கூடுதல் 12-க்கு மேல் போனால் 12-ஐக் கழிக்கவும்."
            ),
            examples = listOf(
                "8 மணி + 7 மணி" to "8 + 7 = 15; 15 - 12 = 3 மணி",
                "10 மணி + 6 மணி" to "10 + 6 = 16; 16 - 12 = 4 மணி",
                "11 மணி + 4 மணி" to "11 + 4 = 15; 15 - 12 = 3 மணி"
            ),
            practiceQuestions = listOf("9 மணி + 8 மணி = ?", "7 மணி + 9 மணி = ?"),
            category = MathSecretCategory.SQUARES_MAGIC
        ),
        MentalMathSecret(
            number = 100,
            titleTa = "மனக்கணக்கு மாஸ்டர் சூத்திரம்: எளிய பகுதிகளாக உடைத்து வெல்லுதல்!",
            titleEn = "The Ultimate Mental Math Master Rule",
            formulaTa = "சிக்கலான கணக்கு = எளிய பல பகுதிகளின் சேர்க்கை!",
            explanationTa = "கணிதத்தில் எந்தவொரு பெரிய கணக்கையும் கண்டு பயப்பட வேண்டாம். அதை பத்துகளாகவும், நூறுகளாகவும், தெரிந்த வாய்ப்பாடுகளாகவும் உடைத்துப் போட்டால் எந்த மனக்கணக்கும் உங்கள் விரல் நுனியில்!",
            stepsTa = listOf(
                "படி 1: பெரிய எண்ணை வட்டமான எண்களாகப் பிரியுங்கள்.",
                "படி 2: தெரிந்த ரகசியங்களைப் பயன்படுத்தி எளிய பகுதிகளை மனதிலேயே கணக்கிடுங்கள்.",
                "படி 3: பகுதிகளை இணைத்து இறுதி விடையைக் கூறி மனக்கணக்கு மாஸ்டர் ஆகுங்கள்!"
            ),
            examples = listOf(
                "48 × 15" to "(48 × 10) + (48 × 5) = 480 + 240 = 720!",
                "235 + 199" to "235 + 200 - 1 = 435 - 1 = 434!",
                "1000 - 467" to "9-4=5, 9-6=3, 10-7=3 ➔ விடை: 533!"
            ),
            practiceQuestions = listOf("தினமும் 5 மனக்கணக்கு ரகசியங்களைப் பயிற்சி செய்து வெற்றி பெறுங்கள்! 🌟"),
            category = MathSecretCategory.SQUARES_MAGIC
        )
    )
}
