package com.example.data.math

object MentalMathSecretsPart1 {

    val additionSecrets: List<MentalMathSecret> = listOf(
        MentalMathSecret(
            number = 1,
            titleTa = "10-ன் கூட்டாளிகள் (10's Complements)",
            titleEn = "Number Pairs that Make 10",
            formulaTa = "1+9=10, 2+8=10, 3+7=10, 4+6=10, 5+5=10",
            explanationTa = "எந்தவொரு கூட்டலையும் எளிதாக்க முதலில் 10-ஐ உருவாக்கும் ஜோடி எண்களைத் தேடிக் கூட்ட வேண்டும்.",
            stepsTa = listOf(
                "படி 1: கூட்டலில் இருக்கும் எண்களில் 10 தரும் ஜோடிகளைத் தேடுங்கள்.",
                "படி 2: அவற்றை முதலில் கூட்டி 10, 20, 30 ஆக்குங்கள்.",
                "படி 3: மீதமுள்ள எண்ணைச் சேர்த்துக் கூட்டவும்."
            ),
            examples = listOf(
                "7 + 5 + 3" to "(7 + 3) + 5 = 10 + 5 = 15",
                "8 + 6 + 2" to "(8 + 2) + 6 = 10 + 6 = 16",
                "4 + 9 + 6" to "(4 + 6) + 9 = 10 + 9 = 19"
            ),
            practiceQuestions = listOf("6 + 8 + 4 = ?", "9 + 5 + 1 = ?", "7 + 8 + 3 = ?"),
            category = MathSecretCategory.ADDITION
        ),
        MentalMathSecret(
            number = 2,
            titleTa = "இடமிருந்து வலம் கூட்டுதல் (Left-to-Right Addition)",
            titleEn = "Add Left to Right for Mental Speed",
            formulaTa = "பத்துகள் + ஒன்றுகள் (Tens first, then Ones)",
            explanationTa = "காகிதத்தில் வலமிருந்து கூட்டுவோம். ஆனால் மனக்கணக்கில் இடமிருந்து வலம் கூட்டினால் விடை மின்னல் வேகத்தில் வரும்!",
            stepsTa = listOf(
                "படி 1: முதலில் பத்துகள் ஸ்தானங்களைக் கூட்டுங்கள்.",
                "படி 2: பின்னர் ஒன்றுகள் ஸ்தானங்களைக் கூட்டுங்கள்.",
                "படி 3: இரண்டையும் மனதிலேயே இணைத்துக் கொள்ளுங்கள்."
            ),
            examples = listOf(
                "45 + 32" to "(40 + 30) + (5 + 2) = 70 + 7 = 77",
                "54 + 28" to "(50 + 20) + (4 + 8) = 70 + 12 = 82",
                "63 + 25" to "(60 + 20) + (3 + 5) = 80 + 8 = 88"
            ),
            practiceQuestions = listOf("34 + 43 = ?", "52 + 37 = ?", "68 + 24 = ?"),
            category = MathSecretCategory.ADDITION
        ),
        MentalMathSecret(
            number = 3,
            titleTa = "9-ஐக் கூட்டும்போது: 10 கூட்டி 1 கழிக்கவும்",
            titleEn = "Add 9: Add 10 then Subtract 1",
            formulaTa = "n + 9 = (n + 10) - 1",
            explanationTa = "9-ஐக் கூட்டுவதை விட, 10-ஐக் கூட்டி ஒன்றைக் கழிப்பது மனக்கணக்கில் மிக மிக எளிது!",
            stepsTa = listOf(
                "படி 1: கொடுக்கப்பட்ட எண்ணுடன் 10-ஐக் கூட்டுங்கள்.",
                "படி 2: வந்த விடையிலிருந்து 1-ஐக் கழிக்கவும்."
            ),
            examples = listOf(
                "37 + 9" to "37 + 10 = 47; 47 - 1 = 46",
                "58 + 9" to "58 + 10 = 68; 68 - 1 = 67",
                "84 + 9" to "84 + 10 = 94; 94 - 1 = 93"
            ),
            practiceQuestions = listOf("46 + 9 = ?", "69 + 9 = ?", "83 + 9 = ?"),
            category = MathSecretCategory.ADDITION
        ),
        MentalMathSecret(
            number = 4,
            titleTa = "19-ஐக் கூட்டும்போது: 20 கூட்டி 1 கழிக்கவும்",
            titleEn = "Add 19: Add 20 then Subtract 1",
            formulaTa = "n + 19 = (n + 20) - 1",
            explanationTa = "19 என்பது 20-ஐ விட 1 குறைவு. எனவே 20 கூட்டி 1-ஐக் கழித்தால் நொடியில் விடை கிடைக்கும்.",
            stepsTa = listOf(
                "படி 1: கொடுக்கப்பட்ட எண்ணுடன் 20-ஐக் கூட்டுங்கள்.",
                "படி 2: விடையிலிருந்து 1-ஐக் கழிக்கவும்."
            ),
            examples = listOf(
                "45 + 19" to "45 + 20 = 65; 65 - 1 = 64",
                "67 + 19" to "67 + 20 = 87; 87 - 1 = 86",
                "82 + 19" to "82 + 20 = 102; 102 - 1 = 101"
            ),
            practiceQuestions = listOf("38 + 19 = ?", "54 + 19 = ?", "76 + 19 = ?"),
            category = MathSecretCategory.ADDITION
        ),
        MentalMathSecret(
            number = 5,
            titleTa = "99-ஐக் கூட்டும்போது: 100 கூட்டி 1 கழிக்கவும்",
            titleEn = "Add 99: Add 100 then Subtract 1",
            formulaTa = "n + 99 = (n + 100) - 1",
            explanationTa = "99-ஐக் கூட்டக் கஷ்டப்பட வேண்டாம்; 100 கூட்டி 1-ஐக் கழித்தால் கணக்கீடு நொடியில் முடியும்!",
            stepsTa = listOf(
                "படி 1: எண்ணுடன் 100-ஐச் சேர்க்கவும்.",
                "படி 2: 1-ஐக் கழித்து இறுதி விடை காணவும்."
            ),
            examples = listOf(
                "156 + 99" to "156 + 100 = 256; 256 - 1 = 255",
                "284 + 99" to "284 + 100 = 384; 384 - 1 = 383",
                "437 + 99" to "437 + 100 = 537; 537 - 1 = 536"
            ),
            practiceQuestions = listOf("245 + 99 = ?", "678 + 99 = ?", "512 + 99 = ?"),
            category = MathSecretCategory.ADDITION
        ),
        MentalMathSecret(
            number = 6,
            titleTa = "இரட்டை எண்களைப் பயன்படுத்தி கூட்டுதல் (Doubles + 1)",
            titleEn = "Using Doubles Strategy",
            formulaTa = "n + (n+1) = (2 × n) + 1",
            explanationTa = "அடுத்தடுத்த எண்களைக் கூட்டும்போது, சிறிய எண்ணை இரட்டிப்பாக்கி 1 கூட்டினால் போதும்.",
            stepsTa = listOf(
                "படி 1: சிறிய எண்ணை இரட்டிப்பாக்குங்கள்.",
                "படி 2: அதனுடன் 1-ஐக் கூட்டுங்கள்."
            ),
            examples = listOf(
                "7 + 8" to "(7 × 2) + 1 = 14 + 1 = 15",
                "15 + 16" to "(15 × 2) + 1 = 30 + 1 = 31",
                "25 + 26" to "(25 × 2) + 1 = 50 + 1 = 51"
            ),
            practiceQuestions = listOf("8 + 9 = ?", "12 + 13 = ?", "18 + 19 = ?"),
            category = MathSecretCategory.ADDITION
        ),
        MentalMathSecret(
            number = 7,
            titleTa = "நண்பன் எண்களைக் கடன் கொடுத்து சமன் செய்தல் (Compensation)",
            titleEn = "Make a Round Number (Give & Take)",
            formulaTa = "(a - k) + (b + k) = a + b",
            explanationTa = "ஒரு எண்ணிலிருந்து சிறிதை எடுத்து அடுத்த எண்ணில் போட்டு 10 அல்லது 100 ஆக்கிக் கூட்டுங்கள்.",
            stepsTa = listOf(
                "படி 1: 38 போன்ற எண்ணை 40 ஆக்க 2 தேவை.",
                "படி 2: அடுத்த எண்ணான 25-ல் இருந்து 2 எடுத்து 38-க்கு கொடுங்கள் (40 + 23).",
                "படி 3: இப்போது 40 + 23 = 63 சுலபமாகிவிடும்!"
            ),
            examples = listOf(
                "38 + 25" to "(38 + 2) + (25 - 2) = 40 + 23 = 63",
                "49 + 36" to "(49 + 1) + (36 - 1) = 50 + 35 = 85",
                "57 + 28" to "(57 + 3) + (28 - 3) = 60 + 25 = 85"
            ),
            practiceQuestions = listOf("48 + 35 = ?", "59 + 24 = ?", "67 + 19 = ?"),
            category = MathSecretCategory.ADDITION
        ),
        MentalMathSecret(
            number = 8,
            titleTa = "மூன்று எண்களைக் கூட்டும்போது 10 அல்லது 20 உருவாக்குதல்",
            titleEn = "Grouping 3 Numbers to make 10 or 20",
            formulaTa = "Group pairs that total 10, 20 or round numbers",
            explanationTa = "வரிசையாகக் கூட்டாமல், எளிதில் கூடும் எண்களைக் குழுவாக்கிக் கூட்ட வேண்டும்.",
            stepsTa = listOf(
                "படி 1: ஒன்றுகள் ஸ்தானம் கூடினால் 10 வரும் இரு எண்களைத் தேர்ந்தெடுங்கள்.",
                "படி 2: அவற்றை முதலில் கூட்டி வட்டமான எண் ஆக்குங்கள்.",
                "படி 3: மூன்றாவது எண்ணைக் கூட்டி விடை காணுங்கள்."
            ),
            examples = listOf(
                "14 + 27 + 16" to "(14 + 16) + 27 = 30 + 27 = 57",
                "23 + 45 + 17" to "(23 + 17) + 45 = 40 + 45 = 85",
                "32 + 19 + 18" to "(32 + 18) + 19 = 50 + 19 = 69"
            ),
            practiceQuestions = listOf("15 + 28 + 25 = ?", "36 + 17 + 14 = ?", "42 + 39 + 18 = ?"),
            category = MathSecretCategory.ADDITION
        ),
        MentalMathSecret(
            number = 9,
            titleTa = "8-ஐக் கூட்டும்போது: 10 கூட்டி 2 கழிக்கவும்",
            titleEn = "Add 8: Add 10 then Subtract 2",
            formulaTa = "n + 8 = (n + 10) - 2",
            explanationTa = "8 என்பது 10-ஐ விட 2 குறைவு. எனவே 10 கூட்டி 2 கழிப்பது மனக்கணக்கில் மின்னல் வழி!",
            stepsTa = listOf(
                "படி 1: எண்ணுடன் 10-ஐக் கூட்டுங்கள்.",
                "படி 2: வந்த விடையிலிருந்து 2-ஐக் கழிக்கவும்."
            ),
            examples = listOf(
                "35 + 8" to "35 + 10 = 45; 45 - 2 = 43",
                "57 + 8" to "57 + 10 = 67; 67 - 2 = 65",
                "74 + 8" to "74 + 10 = 84; 84 - 2 = 82"
            ),
            practiceQuestions = listOf("46 + 8 = ?", "68 + 8 = ?", "85 + 8 = ?"),
            category = MathSecretCategory.ADDITION
        ),
        MentalMathSecret(
            number = 10,
            titleTa = "தொடர்ச்சியான 5 எண்களின் கூடுதல் (Sum of 5 Consecutive Numbers)",
            titleEn = "Sum of 5 Consecutive Numbers",
            formulaTa = "நடு எண் × 5 (Middle Number × 5)",
            explanationTa = "தொடர்ச்சியான 5 எண்களைக் கூட்டும்போது, நடுவில் உள்ள எண்ணை 5-ஆல் பெருக்கினால் கூடுதல் கிடைக்கும்!",
            stepsTa = listOf(
                "படி 1: கொடுக்கப்பட்ட 5 எண்களில் நடு எண்ணைக் கண்டுபிடியுங்கள்.",
                "படி 2: அந்த நடு எண்ணை 5-ஆல் பெருக்குங்கள்."
            ),
            examples = listOf(
                "11 + 12 + 13 + 14 + 15" to "நடு எண் 13 ➔ 13 × 5 = 65",
                "20 + 21 + 22 + 23 + 24" to "நடு எண் 22 ➔ 22 × 5 = 110",
                "1 + 2 + 3 + 4 + 5" to "நடு எண் 3 ➔ 3 × 5 = 15"
            ),
            practiceQuestions = listOf("6 + 7 + 8 + 9 + 10 = ?", "31 + 32 + 33 + 34 + 35 = ?"),
            category = MathSecretCategory.ADDITION
        ),
        MentalMathSecret(
            number = 11,
            titleTa = "1 முதல் n வரையிலான எண்களின் கூடுதல் (Sum 1 to N)",
            titleEn = "Sum of First N Natural Numbers",
            formulaTa = "(n × (n + 1)) ÷ 2",
            explanationTa = "1 முதல் 10 வரை அல்லது 1 முதல் 20 வரை உள்ள எண்களின் கூடுதலை காஸ் (Gauss) சூத்திரத்தில் நொடியில் காணலாம்.",
            stepsTa = listOf(
                "படி 1: கடைசி எண் n-ஐயும் அதற்கு அடுத்த எண் (n+1)-ஐயும் பெருக்குங்கள்.",
                "படி 2: வந்த விடையை 2-ஆல் வகுத்து பாதியாக்குங்கள்."
            ),
            examples = listOf(
                "1 முதல் 10 வரை" to "(10 × 11) ÷ 2 = 110 ÷ 2 = 55",
                "1 முதல் 20 வரை" to "(20 × 21) ÷ 2 = 420 ÷ 2 = 210",
                "1 முதல் 6 வரை" to "(6 × 7) ÷ 2 = 42 ÷ 2 = 21"
            ),
            practiceQuestions = listOf("1 முதல் 12 வரை = ?", "1 முதல் 15 வரை = ?"),
            category = MathSecretCategory.ADDITION
        ),
        MentalMathSecret(
            number = 12,
            titleTa = "ஒற்றை எண்களின் கூடுதல் (Sum of First N Odd Numbers)",
            titleEn = "Sum of Consecutive Odd Numbers",
            formulaTa = "எண்களின் எண்ணிக்கை² (n²)",
            explanationTa = "1-ல் தொடங்கும் முதல் n ஒற்றை எண்களின் கூடுதல் எப்போதும் n² ஆக இருக்கும்!",
            stepsTa = listOf(
                "படி 1: எத்தனை ஒற்றை எண்கள் உள்ளன என எண்ணுங்கள் (n).",
                "படி 2: அந்த எண்ணிக்கையை வர்க்கப்படுத்துங்கள் (n × n)."
            ),
            examples = listOf(
                "1 + 3 + 5" to "3 எண்கள் ➔ 3² = 9",
                "1 + 3 + 5 + 7 + 9" to "5 எண்கள் ➔ 5² = 25",
                "1 + 3 + 5 + 7 + 9 + 11" to "6 எண்கள் ➔ 6² = 36"
            ),
            practiceQuestions = listOf("1+3+5+7 = ?", "1 முதல் 15 வரையிலான 8 ஒற்றை எண்கள் = ?"),
            category = MathSecretCategory.ADDITION
        ),
        MentalMathSecret(
            number = 13,
            titleTa = "இரட்டை எண்களின் கூடுதல் (Sum of First N Even Numbers)",
            titleEn = "Sum of First N Even Numbers",
            formulaTa = "n × (n + 1)",
            explanationTa = "2-ல் தொடங்கும் முதல் n இரட்டை எண்களின் கூடுதல், n-ஐ அதன் அடுத்த எண்ணால் பெருக்கினால் கிடைக்கும்.",
            stepsTa = listOf(
                "படி 1: எத்தனை இரட்டை எண்கள் உள்ளன என எண்ணுங்கள் (n).",
                "படி 2: n-ஐ (n+1)-ஆல் பெருக்குங்கள்."
            ),
            examples = listOf(
                "2 + 4 + 6" to "3 எண்கள் ➔ 3 × 4 = 12",
                "2 + 4 + 6 + 8" to "4 எண்கள் ➔ 4 × 5 = 20",
                "2 + 4 + 6 + 8 + 10" to "5 எண்கள் ➔ 5 × 6 = 30"
            ),
            practiceQuestions = listOf("2+4+6+8+10+12 = ?", "முதல் 8 இரட்டை எண்கள் = ?"),
            category = MathSecretCategory.ADDITION
        ),
        MentalMathSecret(
            number = 14,
            titleTa = "பணக் கூட்டல்: ரூபாயை முதலில், காசுகளைப் பின்னர் கூட்டுதல்",
            titleEn = "Money Math: Rupee First, Paise Next",
            formulaTa = "ரூபாய்கள் கூடுதல் + (பைசா கூடுதல் ÷ 100)",
            explanationTa = "கடைக்குப் போகும்போது கணக்கு போட, ரூபாய்களைத் தனியாகவும் பைசாக்களைத் தனியாகவும் கூட்டிச் சேர்க்கவும்.",
            stepsTa = listOf(
                "படி 1: முழு ரூபாய்களைக் கூட்டுங்கள்.",
                "படி 2: பைசாக்களைக் கூட்டி, 100 பைசா வந்தால் 1 ரூபாய் என மாற்றவும்."
            ),
            examples = listOf(
                "₹24.50 + ₹15.50" to "(24 + 15) = 39; 50p + 50p = ₹1 ➔ ₹40",
                "₹12.25 + ₹8.75" to "(12 + 8) = 20; 25p + 75p = ₹1 ➔ ₹21",
                "₹35.50 + ₹14.25" to "(35 + 14) = 49; 50p + 25p = 75p ➔ ₹49.75"
            ),
            practiceQuestions = listOf("₹18.50 + ₹12.50 = ?", "₹45.75 + ₹15.25 = ?"),
            category = MathSecretCategory.ADDITION
        ),
        MentalMathSecret(
            number = 15,
            titleTa = "மூன்று இலக்க எண்களைப் பிரித்து கூட்டுதல் (3-Digit Chunking)",
            titleEn = "3-Digit Chunking Addition",
            formulaTa = "நூறுகள் + பத்துகள் + ஒன்றுகள்",
            explanationTa = "பெரிய எண்களை நூறு, பத்து, ஒன்றாகப் பிரித்து மனக்கணக்கில் சுலபமாகக் கூட்டலாம்.",
            stepsTa = listOf(
                "படி 1: நூறுகளைக் கூட்டுங்கள்.",
                "படி 2: பத்துகளைக் கூட்டுங்கள்.",
                "படி 3: ஒன்றுகளைக் கூட்டி மொத்தத்தை இணைக்கவும்."
            ),
            examples = listOf(
                "235 + 142" to "(200+100) + (30+40) + (5+2) = 300 + 70 + 7 = 377",
                "314 + 253" to "(300+200) + (10+50) + (4+3) = 500 + 60 + 7 = 567",
                "426 + 138" to "400+100=500; 26+38=64 ➔ 564"
            ),
            practiceQuestions = listOf("145 + 234 = ?", "325 + 162 = ?", "513 + 246 = ?"),
            category = MathSecretCategory.ADDITION
        )
    )

    val subtractionSecrets: List<MentalMathSecret> = listOf(
        MentalMathSecret(
            number = 16,
            titleTa = "100-லிருந்து கழித்தல்: 9-லிருந்து அனைத்தும், 10-லிருந்து கடைசி!",
            titleEn = "Subtract from 100: All from 9, Last from 10",
            formulaTa = "(9 - முதல் எண்) மற்றும் (10 - கடைசி எண்)",
            explanationTa = "100-லிருந்து ஓர் ஈரிலக்க எண்ணைக் கழிக்க, முதல் இலக்கத்தை 9-லிருந்தும், கடைசி இலக்கத்தை 10-லிருந்தும் கழிக்க வேண்டும்!",
            stepsTa = listOf(
                "படி 1: பத்துகள் ஸ்தானத்தை 9-லிருந்து கழிக்கவும்.",
                "படி 2: ஒன்றுகள் ஸ்தானத்தை 10-லிருந்து கழிக்கவும்.",
                "படி 3: இரண்டையும் பக்கத்தில் எழுதினால் விடை தயார்!"
            ),
            examples = listOf(
                "100 - 36" to "9 - 3 = 6; 10 - 6 = 4 ➔ விடை: 64",
                "100 - 47" to "9 - 4 = 5; 10 - 7 = 3 ➔ விடை: 53",
                "100 - 72" to "9 - 7 = 2; 10 - 2 = 8 ➔ விடை: 28"
            ),
            practiceQuestions = listOf("100 - 54 = ?", "100 - 63 = ?", "100 - 81 = ?"),
            category = MathSecretCategory.SUBTRACTION
        ),
        MentalMathSecret(
            number = 17,
            titleTa = "1000-லிருந்து கழித்தல்: வேத கணித நிகிலம் சூத்திரம்",
            titleEn = "Subtract from 1000: Nikhilam Method",
            formulaTa = "முதல் இரு எண்கள் 9-ல், கடைசி எண் 10-ல்",
            explanationTa = "1000-லிருந்து கழிக்கும்போது கடன் வாங்கத் தேவையே இல்லை; முதல் எண்களை 9-லும் கடைசியை 10-லும் கழிக்கவும்.",
            stepsTa = listOf(
                "படி 1: நூறுகள் எண்ணை 9-லிருந்து கழிக்கவும்.",
                "படி 2: பத்துகள் எண்ணை 9-லிருந்து கழிக்கவும்.",
                "படி 3: ஒன்றுகள் எண்ணை 10-லிருந்து கழிக்கவும்."
            ),
            examples = listOf(
                "1000 - 347" to "9-3=6, 9-4=5, 10-7=3 ➔ விடை: 653",
                "1000 - 682" to "9-6=3, 9-8=1, 10-2=8 ➔ விடை: 318",
                "1000 - 425" to "9-4=5, 9-2=7, 10-5=5 ➔ விடை: 575"
            ),
            practiceQuestions = listOf("1000 - 274 = ?", "1000 - 589 = ?", "1000 - 716 = ?"),
            category = MathSecretCategory.SUBTRACTION
        ),
        MentalMathSecret(
            number = 18,
            titleTa = "9-ஐக் கழிக்கும்போது: 10 கழித்து 1 கூட்டவும்",
            titleEn = "Subtract 9: Minus 10 then Add 1",
            formulaTa = "n - 9 = (n - 10) + 1",
            explanationTa = "9-ஐக் கழிப்பதை விட, 10-ஐக் கழித்து 1-ஐக் கூட்டுவது மிக வேகமானது.",
            stepsTa = listOf(
                "படி 1: எண்ணிலிருந்து 10-ஐக் கழிக்கவும்.",
                "படி 2: வந்த விடையுடன் 1-ஐக் கூட்டவும்."
            ),
            examples = listOf(
                "43 - 9" to "43 - 10 = 33; 33 + 1 = 34",
                "67 - 9" to "67 - 10 = 57; 57 + 1 = 58",
                "85 - 9" to "85 - 10 = 75; 75 + 1 = 76"
            ),
            practiceQuestions = listOf("52 - 9 = ?", "74 - 9 = ?", "91 - 9 = ?"),
            category = MathSecretCategory.SUBTRACTION
        ),
        MentalMathSecret(
            number = 19,
            titleTa = "19-ஐக் கழிக்கும்போது: 20 கழித்து 1 கூட்டவும்",
            titleEn = "Subtract 19: Minus 20 then Add 1",
            formulaTa = "n - 19 = (n - 20) + 1",
            explanationTa = "19-ஐக் கழிக்க 20-ஐக் கழித்து 1-ஐக் கூட்டினால் கடன் வாங்காமல் கணக்கு முடிந்துவிடும்.",
            stepsTa = listOf(
                "படி 1: கொடுக்கப்பட்ட எண்ணிலிருந்து 20-ஐக் கழிக்கவும்.",
                "படி 2: 1-ஐக் கூட்டவும்."
            ),
            examples = listOf(
                "54 - 19" to "54 - 20 = 34; 34 + 1 = 35",
                "72 - 19" to "72 - 20 = 52; 52 + 1 = 53",
                "93 - 19" to "93 - 20 = 73; 73 + 1 = 74"
            ),
            practiceQuestions = listOf("63 - 19 = ?", "85 - 19 = ?", "47 - 19 = ?"),
            category = MathSecretCategory.SUBTRACTION
        ),
        MentalMathSecret(
            number = 20,
            titleTa = "99-ஐக் கழிக்கும்போது: 100 கழித்து 1 கூட்டவும்",
            titleEn = "Subtract 99: Minus 100 then Add 1",
            formulaTa = "n - 99 = (n - 100) + 1",
            explanationTa = "99-ஐக் கழிக்க 100-ஐக் கழித்து 1-ஐக் கூட்டினால் பெரிய கழித்தலும் நொடியில் முடியும்.",
            stepsTa = listOf(
                "படி 1: எண்ணிலிருந்து 100-ஐக் கழிக்கவும்.",
                "படி 2: 1-ஐக் கூட்டவும்."
            ),
            examples = listOf(
                "243 - 99" to "243 - 100 = 143; 143 + 1 = 144",
                "415 - 99" to "415 - 100 = 315; 315 + 1 = 316",
                "678 - 99" to "678 - 100 = 578; 578 + 1 = 579"
            ),
            practiceQuestions = listOf("352 - 99 = ?", "527 - 99 = ?", "764 - 99 = ?"),
            category = MathSecretCategory.SUBTRACTION
        ),
        MentalMathSecret(
            number = 21,
            titleTa = "எண்ணி மேலேறுதல் முறை (Counting Up Method)",
            titleEn = "Counting Up to Subtract",
            formulaTa = "Difference = Jump to nearest 10 + Jump to target",
            explanationTa = "கடைக்காரர்கள் மீதி சில்லறை தருவது போல, சிறிய எண்ணிலிருந்து பெரிய எண்ணுக்குத் தாவி கழித்தலைக் காணலாம்.",
            stepsTa = listOf(
                "படி 1: சிறிய எண்ணிலிருந்து அடுத்த பத்தின் மடங்குக்குத் தாவவும்.",
                "படி 2: அங்கிருந்து பெரிய எண்ணுக்குத் தாவவும்.",
                "படி 3: தாவிய தூரங்களைக் கூட்டினால் கழித்தலின் விடை!"
            ),
            examples = listOf(
                "72 - 47" to "47 ➔ 50 (+3); 50 ➔ 72 (+22) ➔ 3 + 22 = 25",
                "84 - 38" to "38 ➔ 40 (+2); 40 ➔ 84 (+44) ➔ 2 + 44 = 46",
                "91 - 56" to "56 ➔ 60 (+4); 60 ➔ 91 (+31) ➔ 4 + 31 = 35"
            ),
            practiceQuestions = listOf("63 - 28 = ?", "75 - 39 = ?", "94 - 48 = ?"),
            category = MathSecretCategory.SUBTRACTION
        ),
        MentalMathSecret(
            number = 22,
            titleTa = "இரு பக்கமும் சமமாக கூட்டுதல் (Equal Addition Trick)",
            titleEn = "Same Change Rule for Subtraction",
            formulaTa = "(a + k) - (b + k) = a - b",
            explanationTa = "கழிக்கும் எண்ணை வட்டமான 10 ஆக்க இரு பக்கமும் ஒரே எண்ணைக் கூட்டினால் விடை மாறாது!",
            stepsTa = listOf(
                "படி 1: கழிக்கும் எண்ணை வட்டமான பத்தாக மாற்ற என்ன கூட்ட வேண்டும் எனப் பாருங்கள்.",
                "படி 2: அதே எண்ணை முதல் எண்ணிலும் கூட்டுங்கள்.",
                "படி 3: இப்போது எளிதாகக் கழிக்கலாம்!"
            ),
            examples = listOf(
                "73 - 38" to "இரு பக்கமும் +2 ➔ 75 - 40 = 35",
                "85 - 29" to "இரு பக்கமும் +1 ➔ 86 - 30 = 56",
                "62 - 27" to "இரு பக்கமும் +3 ➔ 65 - 30 = 35"
            ),
            practiceQuestions = listOf("64 - 28 = ?", "83 - 39 = ?", "91 - 47 = ?"),
            category = MathSecretCategory.SUBTRACTION
        ),
        MentalMathSecret(
            number = 23,
            titleTa = "8-ஐக் கழிக்கும்போது: 10 கழித்து 2 கூட்டவும்",
            titleEn = "Subtract 8: Minus 10 then Add 2",
            formulaTa = "n - 8 = (n - 10) + 2",
            explanationTa = "8-ஐக் கழிக்க 10-ஐக் கழித்து 2-ஐக் கூட்டுங்கள்.",
            stepsTa = listOf(
                "படி 1: எண்ணிலிருந்து 10-ஐக் கழிக்கவும்.",
                "படி 2: விடையுடன் 2-ஐக் கூட்டவும்."
            ),
            examples = listOf(
                "45 - 8" to "45 - 10 = 35; 35 + 2 = 37",
                "62 - 8" to "62 - 10 = 52; 52 + 2 = 54",
                "81 - 8" to "81 - 10 = 71; 71 + 2 = 73"
            ),
            practiceQuestions = listOf("53 - 8 = ?", "75 - 8 = ?", "94 - 8 = ?"),
            category = MathSecretCategory.SUBTRACTION
        ),
        MentalMathSecret(
            number = 24,
            titleTa = "தலைகீழ் எண்களைக் கழிக்கும் மேஜிக் (Reverse Digit Subtraction)",
            titleEn = "Subtracting 2-Digit Reversible Numbers",
            formulaTa = "ab - ba = 9 × (a - b)",
            explanationTa = "ஓர் ஈரிலக்க எண்ணிலிருந்து அதன் தலைகீழ் எண்ணைக் கழித்தால், விடை எப்போதும் 9-ன் மடங்காக இருக்கும்!",
            stepsTa = listOf(
                "படி 1: இரு இலக்கங்களின் வித்தியாசத்தைக் காணவும் (a - b).",
                "படி 2: அந்த வித்தியாசத்தை 9-ஆல் பெருக்கினால் கழித்தல் விடை வந்துவிடும்!"
            ),
            examples = listOf(
                "72 - 27" to "7 - 2 = 5 ➔ 5 × 9 = 45",
                "83 - 38" to "8 - 3 = 5 ➔ 5 × 9 = 45",
                "94 - 49" to "9 - 4 = 5 ➔ 5 × 9 = 45",
                "61 - 16" to "6 - 1 = 5 ➔ 5 × 9 = 45"
            ),
            practiceQuestions = listOf("62 - 26 = ?", "81 - 18 = ?", "93 - 39 = ?"),
            category = MathSecretCategory.SUBTRACTION
        ),
        MentalMathSecret(
            number = 25,
            titleTa = "பிரித்துக் கழித்தல் முறை (Subtract in Chunks)",
            titleEn = "Chunking Subtraction",
            formulaTa = "n - (Tens + Ones)",
            explanationTa = "கழிக்கும் எண்ணை பத்து தனியாக, ஒன்று தனியாகப் பிரித்துக் கழிக்கலாம்.",
            stepsTa = listOf(
                "படி 1: முதலில் பத்துகளைக் கழிக்கவும்.",
                "படி 2: பின்னர் மீதமுள்ள ஒன்றுகளைக் கழிக்கவும்."
            ),
            examples = listOf(
                "86 - 34" to "86 - 30 = 56; 56 - 4 = 52",
                "95 - 42" to "95 - 40 = 55; 55 - 2 = 53",
                "78 - 25" to "78 - 20 = 58; 58 - 5 = 53"
            ),
            practiceQuestions = listOf("77 - 35 = ?", "89 - 46 = ?", "68 - 27 = ?"),
            category = MathSecretCategory.SUBTRACTION
        ),
        MentalMathSecret(
            number = 26,
            titleTa = "50-லிருந்து கழிக்கும் ரகசியம் (Subtract from 50)",
            titleEn = "Subtracting from 50 Quickly",
            formulaTa = "50 - n = (40 - Tens) & (10 - Ones)",
            explanationTa = "50-லிருந்து ஓர் எண்ணைக் கழிக்க, பத்துகளை 4-லிருந்தும் ஒன்றுகளை 10-லிருந்தும் கழிக்கலாம்.",
            stepsTa = listOf(
                "படி 1: பத்துகள் ஸ்தானத்தை 4-லிருந்து கழிக்கவும்.",
                "படி 2: ஒன்றுகள் ஸ்தானத்தை 10-லிருந்து கழிக்கவும்."
            ),
            examples = listOf(
                "50 - 24" to "4 - 2 = 2; 10 - 4 = 6 ➔ 26",
                "50 - 37" to "4 - 3 = 1; 10 - 7 = 3 ➔ 13",
                "50 - 18" to "4 - 1 = 3; 10 - 8 = 2 ➔ 32"
            ),
            practiceQuestions = listOf("50 - 29 = ?", "50 - 33 = ?", "50 - 16 = ?"),
            category = MathSecretCategory.SUBTRACTION
        ),
        MentalMathSecret(
            number = 27,
            titleTa = "200, 300, 500-லிருந்து கழிக்கும் குறுக்குவழி",
            titleEn = "Subtracting from Multiples of 100",
            formulaTa = "நூறில் 1 குறைத்து, மீதியை 100-லிருந்து கழிக்கவும்",
            explanationTa = "300 - 64 என்றால், 200 தனியாக வைத்துவிட்டு, 100 - 64 = 36 சேர்த்தால் 236!",
            stepsTa = listOf(
                "படி 1: நூறுகள் எண்ணிலிருந்து 100-ஐக் குறைக்கவும்.",
                "படி 2: மீதமுள்ள 100-லிருந்து கொடுக்கப்பட்ட எண்ணைக் கழிக்கவும்.",
                "படி 3: இரண்டையும் இணைக்கவும்."
            ),
            examples = listOf(
                "300 - 64" to "200 + (100 - 64) = 200 + 36 = 236",
                "500 - 82" to "400 + (100 - 82) = 400 + 18 = 418",
                "400 - 45" to "300 + (100 - 45) = 300 + 55 = 355"
            ),
            practiceQuestions = listOf("200 - 48 = ?", "600 - 75 = ?", "800 - 37 = ?"),
            category = MathSecretCategory.SUBTRACTION
        ),
        MentalMathSecret(
            number = 28,
            titleTa = "பணக் கழித்தல்: சில்லறை மீதியை மனதிலேயே காணுதல்",
            titleEn = "Change Return Calculation",
            formulaTa = "கொடுத்த தொகை - வாங்கிய தொகை",
            explanationTa = "₹100 கொடுத்து ₹43-க்கு பொருள் வாங்கினால், 100-லிருந்து 43 கழிக்கும் சூத்திரத்தைப் (9-4=5, 10-3=7) பயன்படுத்தலாம்.",
            stepsTa = listOf(
                "படி 1: 100-ன் நிகிலம் சூத்திரத்தைப் பயன்படுத்துங்கள்.",
                "படி 2: விடை ₹57 உடனே தெரிந்துவிடும்."
            ),
            examples = listOf(
                "₹100 - ₹67" to "9 - 6 = 3, 10 - 7 = 3 ➔ ₹33 மீதி",
                "₹50 - ₹28" to "4 - 2 = 2, 10 - 8 = 2 ➔ ₹22 மீதி",
                "₹500 - ₹340" to "500 - 300 = 200; 200 - 40 = ₹160 மீதி"
            ),
            practiceQuestions = listOf("₹100 - ₹58 = ?", "₹50 - ₹34 = ?", "₹200 - ₹125 = ?"),
            category = MathSecretCategory.SUBTRACTION
        ),
        MentalMathSecret(
            number = 29,
            titleTa = "மூன்று இலக்க தலைகீழ் எண்களின் கழித்தல் மேஜிக்",
            titleEn = "3-Digit Reversible Number Subtraction",
            formulaTa = "abc - cba = 99 × (a - c)",
            explanationTa = "மூன்றிலக்க எண்ணிலிருந்து அதன் தலைகீழ் எண்ணைக் கழித்தால் நடுவில் எப்போதும் 9 வரும்; ஓரங்களின் கூடுதல் 9 ஆகும்!",
            stepsTa = listOf(
                "படி 1: முதல் எண்ணையும் கடைசி எண்ணையும் கழியுங்கள் (a - c).",
                "படி 2: அதை 99-ஆல் பெருக்குங்கள்."
            ),
            examples = listOf(
                "752 - 257" to "(7 - 2) × 99 = 5 × 99 = 495 (நடுவில் 9, 4+5=9)",
                "831 - 138" to "(8 - 1) × 99 = 7 × 99 = 693 (நடுவில் 9, 6+3=9)",
                "642 - 246" to "(6 - 2) × 99 = 4 × 99 = 396"
            ),
            practiceQuestions = listOf("953 - 359 = ?", "541 - 145 = ?"),
            category = MathSecretCategory.SUBTRACTION
        ),
        MentalMathSecret(
            number = 30,
            titleTa = "கழித்தலைச் சரிபார்க்கும் கூட்டல் சோதனை (Check by Addition)",
            titleEn = "Verify Subtraction by Reverse Addition",
            formulaTa = "விடை + கழித்த எண் = முதல் எண்",
            explanationTa = "கழித்த விடை சரியா என்று பார்க்க, வந்த விடையுடன் கழித்த எண்ணைக் கூட்டிப் பார்த்தால் முதல் எண் வர வேண்டும்.",
            stepsTa = listOf(
                "படி 1: வந்த விடையையும் கழித்த எண்ணையும் கூட்டுங்கள்.",
                "படி 2: அது முதல் எண்ணுடன் பொருந்துகிறதா என சரிபார்க்கவும்."
            ),
            examples = listOf(
                "85 - 37 = 48" to "சரிபார்ப்பு: 48 + 37 = (40+30) + (8+7) = 70 + 15 = 85 (சரி!)",
                "92 - 46 = 46" to "சரிபார்ப்பு: 46 + 46 = 92 (சரி!)",
                "74 - 29 = 45" to "சரிபார்ப்பு: 45 + 29 = 45 + 30 - 1 = 74 (சரி!)"
            ),
            practiceQuestions = listOf("65 - 28 = ? (சரிபார்க்க)", "83 - 47 = ? (சரிபார்க்க)"),
            category = MathSecretCategory.SUBTRACTION
        )
    )
}
