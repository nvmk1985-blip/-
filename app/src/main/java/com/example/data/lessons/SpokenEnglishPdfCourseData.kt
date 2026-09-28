package com.example.data.lessons

data class PdfTableRow(
    val col1: String,
    val col2: String,
    val col3: String,
    val col4: String = ""
)

data class PdfCourseSentence(
    val english: String,
    val tamilPronunciation: String,
    val tamilMeaning: String,
    val grammarNote: String = ""
)

data class PdfCoursePage(
    val pageNumber: Int,
    val unitTag: String,
    val dayRange: String,
    val titleEnglish: String,
    val titleTamil: String,
    val introExplanationTamil: String,
    val formulaBoxTitle: String,
    val formulas: List<String>,
    val tableHeaders: List<String> = emptyList(),
    val tableRows: List<PdfTableRow> = emptyList(),
    val sentencesTitle: String = "🗣️ முக்கிய வாக்கியங்கள் & உச்சரிப்பு (Practice Sentences)",
    val sentences: List<PdfCourseSentence> = emptyList(),
    val teacherTipTamil: String
)

object SpokenEnglishPdfCourseData {

    const val BOOK_TITLE_EN = "SPOKEN ENGLISH VIA TAMIL"
    const val BOOK_TITLE_TA = "தமிழ் வழியே ஸ்போக்கன் இங்கிலீஷ் — முழுப் பாடப்புத்தகம்"
    const val BOOK_SUBTITLE = "30-Day Step-by-Step Spoken English Course Book (PDF & Audio Edition)"
    const val BOOK_AUTHOR = "ஆசிரியர்: தனம் டீச்சர் (Dhanam Teacher)"
    const val BOOK_STUDENT = "மாணவி பதிப்பு: சுபிக்சா (சுபி, 9 வயது / Subiksha - Subi, Age 9)"
    const val PDF_FILE_NAME = "Spoken_English_Via_Tamil_Course_Book.pdf"

    val pages: List<PdfCoursePage> = listOf(
        // PAGE 1: COVER PAGE & SYLLABUS OVERVIEW
        PdfCoursePage(
            pageNumber = 1,
            unitTag = "COVER PAGE • அட்டைப்படம்",
            dayRange = "30-Day Full Course",
            titleEnglish = "SPOKEN ENGLISH VIA TAMIL (Complete Course Book)",
            titleTamil = "தமிழ் வழியே ஸ்போக்கன் இங்கிலீஷ் — முழுமையான பாடப்புத்தகம்",
            introExplanationTamil = "ஆங்கில எழுத்துக்கள் மற்றும் அடிப்படை வார்த்தைகள் தெரிந்தவர்கள், தயக்கமின்றி சரளமாக ஆங்கிலத்தில் பேசும் வகையில் இந்த 10-யூனிட் (30 நாட்கள்) பாடப்புத்தகம் தனம் டீச்சரால் (Dhanam Teacher) மாணவி சுபிக்சாவுக்காக (சுபி, 9 வயது) பிரத்யேகமாக உருவாக்கப்பட்டுள்ளது.",
            formulaBoxTitle = "🌟 இந்தப் புத்தகத்தின் சிறப்பம்சங்கள் (Course Highlights)",
            formulas = listOf(
                "1. அடிப்படை முதல் சரளமாகப் பேசும் வரை 10 முழுமையான பாடப் பிரிவுகள் (10 Structured Course Units).",
                "2. ஒவ்வொரு ஆங்கில வாக்கியத்திற்கும் துல்லியமான தமிழ் உச்சரிப்பு (Tamil Pronunciation) மற்றும் தமிழ் அர்த்தம்.",
                "3. 12 காலங்கள் (12 Tenses), துணை வினைச்சொற்கள் (Modal Verbs), கேள்வி வாக்கியங்கள் (WH-Questions) எளிய பார்முலாக்களுடன்.",
                "4. வீடு, பள்ளி, கடை, பயணம் என அன்றாட வாழ்க்கைக்குத் தேவையான நேரடி உரையாடல்கள் (Real-life Dialogues).",
                "5. தமிழர்கள் ஆங்கிலம் பேசும்போது செய்யும் பொதுவான தவறுகளும் அவற்றின் சரியானத் திருத்தங்களும்."
            ),
            tableHeaders = listOf("Unit", "பாடத் தலைப்பு (Course Topic)", "நாட்கள்", "பக்கம்"),
            tableRows = listOf(
                PdfTableRow("Unit 1", "Pronouns & 'Be' Verbs (Am, Is, Are, Was, Were)", "Days 1–3", "P. 3–4"),
                PdfTableRow("Unit 2", "Have / Has / Had & Do / Does / Did", "Days 4–6", "P. 5"),
                PdfTableRow("Unit 3", "Present Tenses (Simple & Continuous)", "Days 7–10", "P. 6"),
                PdfTableRow("Unit 4", "Past & Future Tenses (Simple Past & Future)", "Days 11–13", "P. 7"),
                PdfTableRow("Unit 5", "Perfect Tenses & 12 Tenses Master Chart", "Days 14–16", "P. 8–9"),
                PdfTableRow("Unit 6", "Modal Verbs (Can, Could, Should, Must, May)", "Days 17–19", "P. 10"),
                PdfTableRow("Unit 7", "WH-Questions & Yes/No Questions Formula", "Days 20–21", "P. 11"),
                PdfTableRow("Unit 8", "Prepositions (In, On, At) & Conjunctions", "Days 22–23", "P. 12"),
                PdfTableRow("Unit 9", "Imperatives, Requests, 'Let me' & 'Let us'", "Days 24–25", "P. 13"),
                PdfTableRow("Unit 10", "Daily Dialogues, Common Mistakes, 30 Verbs & Speech", "Days 26–30", "P. 14–18")
            ),
            sentencesTitle = "🎙️ சுபிக்சாவுக்கான முதல் நாள் உறுதிமொழி (First Day Pledge)",
            sentences = listOf(
                PdfCourseSentence(
                    english = "Hello! My name is Subiksha, and you can call me Subi.",
                    tamilPronunciation = "ஹலோ! மை நேம் இஸ் சுபிக்சா, அண்ட் யூ கேன் கால் மீ சுபி.",
                    tamilMeaning = "வணக்கம்! என் பெயர் சுபிக்சா, என்னை சுபி என்று அழைக்கலாம்."
                ),
                PdfCourseSentence(
                    english = "I am 9 years old, and Dhanam is my English teacher.",
                    tamilPronunciation = "ஐ அம் நைன் இயர்ஸ் ஓல்ட், அண்ட் தனம் இஸ் மை இங்கிலீஷ் டீச்சர்.",
                    tamilMeaning = "எனக்கு 9 வயது, தனம் என் ஆங்கில ஆசிரியை."
                ),
                PdfCourseSentence(
                    english = "I will practice speaking English loudly every single day!",
                    tamilPronunciation = "ஐ வில் பிராக்டிஸ் ஸ்பீக்கிங் இங்கிலீஷ் லௌட்லி எவ்ரி சிங்கிள் டே!",
                    tamilMeaning = "நான் தினமும் வாய்விட்டு ஆங்கிலம் பேசிப் பயிற்சி செய்வேன்!"
                )
            ),
            teacherTipTamil = "தனம் டீச்சர் குறிப்பு: சுபி பாப்பா, ஆங்கிலத்தை மனப்பாடம் செய்யாமல் தினமும் 15 நிமிடம் வாய்விட்டுப் பேசினாலே 30 நாட்களில் சரளமாகப் பேசலாம்!"
        ),

        // PAGE 2: TABLE OF CONTENTS & STUDY METHOD
        PdfCoursePage(
            pageNumber = 2,
            unitTag = "INDEX & ROADMAP • பொருளடக்கம்",
            dayRange = "Pages 1 – 18",
            titleEnglish = "Table of Contents & 5 Golden Rules for Fluency",
            titleTamil = "பொருளடக்கம் மற்றும் சரளமாக ஆங்கிலம் பேச 5 தங்க விதிகள்",
            introExplanationTamil = "தமிழ் வாக்கிய அமைப்புக்கும் ஆங்கில வாக்கிய அமைப்புக்கும் உள்ள முக்கிய வேறுபாட்டைப் புரிந்துகொண்டால் ஆங்கிலம் மிக எளிது! தமிழில்: 'நான் பள்ளிக்குச் செல்கிறேன்' (எழுவாய் + செயப்படுபொருள் + வினை). ஆங்கிலத்தில்: 'I go to school' (Subject + Verb + Object).",
            formulaBoxTitle = "📐 அடிப்படை வாக்கிய விதி (Golden Sentence Rule)",
            formulas = listOf(
                "தமிழ் முறை (S + O + V): நான் (S) + ஆங்கிலம் (O) + பேசுகிறேன் (V).",
                "ஆங்கில முறை (S + V + O): I (S) + speak (V) + English (O).",
                "விதி 1: முதலில் யார் செய்கிறார் (Subject - I, We, You, He, She, It, They) என்பதைச் சொல்லுங்கள்.",
                "விதி 2: அடுத்ததாக காலத்தைக் காட்டும் வினைச்சொல்லை (Verb - am/is/are/go/went/will go) சொல்லுங்கள்.",
                "விதி 3: கடைசியாக எதை / எங்கே / எப்போது (Object / Place / Time) என்பதைச் சேருங்கள்!"
            ),
            tableHeaders = listOf("பக்கம்", "பாடப் பிரிவு (Lesson Title)", "முக்கிய இலக்கணம் (Key Focus)", "நாள்"),
            tableRows = listOf(
                PdfTableRow("பக்கம் 3", "Pronouns & Be Verbs (Part 1)", "I am, He is, You are, Was, Were", "Day 1–2"),
                PdfTableRow("பக்கம் 4", "Be Verbs Negative & Questions", "Am I? Is she? Aren't you?", "Day 3"),
                PdfTableRow("பக்கம் 5", "Have / Has / Had & Do / Does / Did", "Possession & Action Helpers", "Day 4–6"),
                PdfTableRow("பக்கம் 6", "Simple Present & Present Continuous", "Daily Habits vs Now Actions", "Day 7–10"),
                PdfTableRow("பக்கம் 7", "Simple Past & Simple Future", "V2, Didn't + V1, Will + V1", "Day 11–13"),
                PdfTableRow("பக்கம் 8", "Present Perfect & Past Continuous", "Have/Has + V3, Was/Were + V-ing", "Day 14–15"),
                PdfTableRow("பக்கம் 9", "12 Tenses Master Chart", "All 12 Tenses in 1 Table", "Day 16"),
                PdfTableRow("பக்கம் 10", "Modal Verbs Mastery", "Can, Could, Should, Must, May", "Day 17–19"),
                PdfTableRow("பக்கம் 11", "WH-Questions Formula", "What, Where, When, Why, How", "Day 20–21"),
                PdfTableRow("பக்கம் 12–18", "Prepositions, Commands, Dialogues & Verbs", "Real-Life Fluency & Mistakes", "Day 22–30")
            ),
            sentencesTitle = "🏆 தனம் டீச்சரின் 4 பயிற்சிப் படிகள் (How to Practice)",
            sentences = listOf(
                PdfCourseSentence(
                    english = "Step 1: Listen carefully to the English sentence and its Tamil meaning.",
                    tamilPronunciation = "ஸ்டெப் 1: லிசன் கேர்ஃபுல்லி டு தி இங்கிலீஷ் சென்டென்ஸ் அண்ட் இட்ஸ் தமிழ் மீனிங்.",
                    tamilMeaning = "படி 1: ஆங்கில வாக்கியத்தையும் அதன் தமிழ் அர்த்தத்தையும் கவனமாகக் கேளுங்கள்."
                ),
                PdfCourseSentence(
                    english = "Step 2: Read the sentence aloud three times clearly without hesitation.",
                    tamilPronunciation = "ஸ்டெப் 2: ரீட் தி சென்டென்ஸ் அலௌட் த்ரீ டைம்ஸ் கிளியர்லி வித்தவுட் ஹெசிடேஷன்.",
                    tamilMeaning = "படி 2: தயக்கமின்றி மூன்று முறை சத்தமாக வாசித்துப் பழகுங்கள்."
                ),
                PdfCourseSentence(
                    english = "Step 3: Don't fear mistakes; mistakes help us learn faster!",
                    tamilPronunciation = "ஸ்டெப் 3: டோன்ட் ஃபியர் மிஸ்டேக்ஸ்; மிஸ்டேக்ஸ் ஹெல்ப் அஸ் லேர்ன் ஃபாஸ்டர்!",
                    tamilMeaning = "படி 3: தவறுகளுக்குப் பயப்படாதீர்கள்; தவறுகளே நம்மை வேகமாகக் கற்க வைக்கும்!"
                )
            ),
            teacherTipTamil = "தனம் டீச்சர் குறிப்பு: ஒவ்வொரு பக்கத்தின் கீழேயும் உள்ள வாக்கியங்களை ஆடியோ பொத்தானை அழுத்திக் கேட்டு, நீங்களும் மைக்கில் பேசிப் பாருங்கள்!"
        ),

        // PAGE 3: UNIT 1 (PART A) - PRONOUNS & BE VERBS
        PdfCoursePage(
            pageNumber = 3,
            unitTag = "UNIT 1 • அடிப்படை வாக்கிய அமைப்பு",
            dayRange = "Days 1 – 2",
            titleEnglish = "Subject Pronouns & 'To Be' Verbs (Am, Is, Are, Was, Were)",
            titleTamil = "மூவிடப் பெயர்ச்சொற்கள் மற்றும் 'இரு' (Be) வினைச்சொற்கள்",
            introExplanationTamil = "ஆங்கிலத்தில் ஒருவரைப் பற்றியோ, ஒரு பொருளின் நிலை (பெயர், வயது, குணம், இடம்) பற்றியோ சொல்வதற்கு 'Be' வினைச்சொற்கள் (am, is, are, was, were, will be) கண்டிப்பாகத் தேவை. தமிழில் 'நான் சுபி' என்போம், ஆனால் ஆங்கிலத்தில் 'I am Subi' என்று 'am' சேர்க்க வேண்டும்.",
            formulaBoxTitle = "📐 பார்முலா (Formula): Subject + am / is / are / was / were + Noun / Adjective",
            formulas = listOf(
                "நிகழ்காலம் (Present - இப்போது): I -> am | He, She, It, Subi -> is | We, You, They -> are",
                "கடந்த காலம் (Past - நேற்று): I, He, She, It, Subi -> was | We, You, They -> were",
                "எதிர்காலம் (Future - நாளை): எல்லா Subject-க்கும் -> will be (இருப்பேன் / இருக்கும்)"
            ),
            tableHeaders = listOf("Subject (எழுவாய்)", "Present (இப்போது)", "Past (நேற்று)", "Future (நாளை)"),
            tableRows = listOf(
                PdfTableRow("I (நான்)", "I am (இருக்கிறேன்)", "I was (இருந்தேன்)", "I will be (இருப்பேன்)"),
                PdfTableRow("We (நாம் / நாங்கள்)", "We are (இருக்கிறோம்)", "We were (இருந்தோம்)", "We will be (இருப்போம்)"),
                PdfTableRow("You (நீ / நீங்கள்)", "You are (இருக்கிறாய்)", "You were (இருந்தாய்)", "You will be (இருப்பாய்)"),
                PdfTableRow("He (அவன் / அவர்)", "He is (இருக்கிறான்)", "He was (இருந்தான்)", "He will be (இருப்பான்)"),
                PdfTableRow("She / Subi (அவள் / சுபி)", "She is (இருக்கிறாள்)", "She was (இருந்தாள்)", "She will be (இருப்பாள்)"),
                PdfTableRow("It (அது / இது)", "It is (இருக்கிறது)", "It was (இருந்தது)", "It will be (இருக்கும்)"),
                PdfTableRow("They (அவர்கள் / அவை)", "They are (இருக்கிறார்கள்)", "They were (இருந்தார்கள்)", "They will be (இருப்பார்கள்)")
            ),
            sentencesTitle = "🗣️ பயிற்சி வாக்கியங்கள் (Unit 1 Practice Sentences)",
            sentences = listOf(
                PdfCourseSentence(
                    english = "I am Subiksha. I am a clever girl.",
                    tamilPronunciation = "ஐ அம் சுபிக்சா. ஐ அம் எ கிளவர் கேர்ள்.",
                    tamilMeaning = "நான் சுபிக்சா. நான் ஒரு புத்திசாலிப் பெண்.",
                    grammarNote = "I உடன் எப்போதும் 'am' மட்டுமே வரும்."
                ),
                PdfCourseSentence(
                    english = "Dhanam is my favorite English teacher.",
                    tamilPronunciation = "தனம் இஸ் மை ஃபேவரைட் இங்கிலீஷ் டீச்சர்.",
                    tamilMeaning = "தனம் எனக்கு மிகவும் பிடித்த ஆங்கில ஆசிரியை.",
                    grammarNote = "ஒரு நபரைக் (She/Dhanam) குறிக்கும்போது 'is' வரும்."
                ),
                PdfCourseSentence(
                    english = "You are very kind and helpful.",
                    tamilPronunciation = "யூ ஆர் வெரி கைண்ட் அண்ட் ஹெல்ப்ஃபுல்.",
                    tamilMeaning = "நீங்கள் மிகவும் அன்பானவர் மற்றும் உதவும் குணம் கொண்டவர்."
                ),
                PdfCourseSentence(
                    english = "We are good friends in school.",
                    tamilPronunciation = "வீ ஆர் குட் ஃபிரண்ட்ஸ் இன் ஸ்கூல்.",
                    tamilMeaning = "நாங்கள் பள்ளியில் நல்ல நண்பர்கள்."
                ),
                PdfCourseSentence(
                    english = "I was at my grandmother's house yesterday.",
                    tamilPronunciation = "ஐ வாஸ் அட் மை கிராண்ட்மதர்ஸ் ஹவுஸ் எஸ்டர்டே.",
                    tamilMeaning = "நான் நேற்று என் பாட்டி வீட்டில் இருந்தேன்.",
                    grammarNote = "Yesterday (நேற்று) என்பதால் 'was' பயன்படுத்தப்பட்டுள்ளது."
                ),
                PdfCourseSentence(
                    english = "They were very happy to see me.",
                    tamilPronunciation = "தே வேர் வெரி ஹேப்பி டு சீ மீ.",
                    tamilMeaning = "என்னைப் பார்த்ததில் அவர்கள் மிகவும் மகிழ்ச்சியாக இருந்தார்கள்."
                ),
                PdfCourseSentence(
                    english = "I will be ready in five minutes.",
                    tamilPronunciation = "ஐ வில் பி ரெடி இன் ஃபைவ் மினிட்ஸ்.",
                    tamilMeaning = "நான் ஐந்து நிமிடங்களில் தயாராக இருப்பேன்."
                )
            ),
            teacherTipTamil = "தனம் டீச்சர் குறிப்பு: 'You' என்பது ஒருமையிலும் (நீ) பன்மையிலும் (நீங்கள்) எப்போதும் 'are' மற்றும் 'were' மட்டுமே எடுக்கும்!"
        ),

        // PAGE 4: UNIT 1 (PART B) - NEGATIVES & QUESTIONS IN BE VERBS
        PdfCoursePage(
            pageNumber = 4,
            unitTag = "UNIT 1 (PART B) • எதிர்மறை & கேள்விகள்",
            dayRange = "Day 3",
            titleEnglish = "'Be' Verbs: Negative Sentences & Yes/No Questions",
            titleTamil = "'Be' வினைச்சொற்களில் 'இல்லை' மற்றும் 'ஆமா/இல்லை' கேள்விகள்",
            introExplanationTamil = "'am, is, are, was, were' வரும் வாக்கியங்களில் 'இல்லை' (Negative) என்று சொல்ல அவற்றின் பக்கத்தில் 'not' சேர்த்தால் போதும். கேள்வியாக மாற்ற அந்த 'am, is, are, was, were'-ஐ எழுவாய்க்கு (Subject) முன்னால் கொண்டு வந்தால் போதும்!",
            formulaBoxTitle = "📐 3-படி பார்முலா (Positive → Negative → Question)",
            formulas = listOf(
                "1. சாதாரண வாக்கியம் (Positive): You are ready. (நீ தயாராக இருக்கிறாய்.)",
                "2. எதிர்மறை வாக்கியம் (Negative): You are not ready. (நீ தயாராக இல்லை.)",
                "3. கேள்வி வாக்கியம் (Question): Are you ready? (நீ தயாராக இருக்கிறாயா?)",
                "சுருக்க வடிவம் (Spoken Contractions): is not = isn't | are not = aren't | was not = wasn't | were not = weren't"
            ),
            tableHeaders = listOf("Positive (சாதாரணம்)", "Negative (எதிர்மறை)", "Question (கேள்வி)", "தமிழ் அர்த்தம்"),
            tableRows = listOf(
                PdfTableRow("I am late.", "I am not late.", "Am I late?", "நான் தாமதமாக வந்தேனா?"),
                PdfTableRow("She is at home.", "She isn't at home.", "Is she at home?", "அவள் வீட்டில் இருக்கிறாளா?"),
                PdfTableRow("You are tired.", "You aren't tired.", "Are you tired?", "நீ சோர்வாக இருக்கிறாயா?"),
                PdfTableRow("It is easy.", "It isn't difficult.", "Is it easy?", "இது எளிதாக இருக்கிறதா?"),
                PdfTableRow("He was absent.", "He wasn't absent.", "Was he absent?", "அவன் நேற்று வரவில்லையா?"),
                PdfTableRow("They were busy.", "They weren't busy.", "Were they busy?", "அவர்கள் வேலையாக இருந்தார்களா?")
            ),
            sentencesTitle = "🗣️ கேள்வி-பதில் பயிற்சி (Question & Answer Practice)",
            sentences = listOf(
                PdfCourseSentence(
                    english = "Are you hungry, Subi? — Yes, I am a little hungry.",
                    tamilPronunciation = "ஆர் யூ ஹங்க்ரி, சுபி? — யெஸ், ஐ அம் எ லிட்டில் ஹங்க்ரி.",
                    tamilMeaning = "சுபி, உனக்குப் பசிக்கிறதா? — ஆமாம், எனக்குக் கொஞ்சம் பசிக்கிறது."
                ),
                PdfCourseSentence(
                    english = "Is this your English notebook? — Yes, it is mine.",
                    tamilPronunciation = "இஸ் திஸ் யுவர் இங்கிலீஷ் நோட்புக்? — யெஸ், இட் இஸ் மைன்.",
                    tamilMeaning = "இது உன்னுடைய ஆங்கில நோட்புக்கா? — ஆமாம், இது என்னுடையதுதான்."
                ),
                PdfCourseSentence(
                    english = "Is your father at home now? — No, he is at the office.",
                    tamilPronunciation = "இஸ் யுவர் ஃபாதர் அட் ஹோம் நவ்? — நோ, ஹி இஸ் அட் தி ஆபீஸ்.",
                    tamilMeaning = "உன் அப்பா இப்போது வீட்டில் இருக்கிறாரா? — இல்லை, அவர் அலுவலகத்தில் இருக்கிறார்."
                ),
                PdfCourseSentence(
                    english = "Were you asleep when I called? — No, I wasn't asleep.",
                    tamilPronunciation = "வேர் யூ அஸ்லீப் வென் ஐ கால்ட்? — நோ, ஐ வாசன்ட் அஸ்லீப்.",
                    tamilMeaning = "நான் கூப்பிட்டபோது நீ தூங்கிக் கொண்டிருந்தாயா? — இல்லை, நான் தூங்கவில்லை."
                ),
                PdfCourseSentence(
                    english = "Don't worry, English is not difficult at all!",
                    tamilPronunciation = "டோன்ட் வொர்ரி, இங்கிலீஷ் இஸ் நாட் டிஃபிகல்ட் அட் ஆல்!",
                    tamilMeaning = "கவலைப்படாதே, ஆங்கிலம் சற்றும் கடினமானது அல்ல!"
                )
            ),
            teacherTipTamil = "தனம் டீச்சர் குறிப்பு: 'You are...' என்பதை கேள்வியாக மாற்றும்போது 'Are you...?' என்று மாற்றி உச்சரிப்பில் கேள்வித் தொனியுடன் கேளுங்கள்!"
        ),

        // PAGE 5: UNIT 2 - HAVE / HAS / HAD & DO / DOES / DID
        PdfCoursePage(
            pageNumber = 5,
            unitTag = "UNIT 2 • உடைமை & துணை வினைகள்",
            dayRange = "Days 4 – 6",
            titleEnglish = "Have / Has / Had (Possession) & Do / Does / Did (Action Helpers)",
            titleTamil = "என்னிடம் உள்ளது (Have/Has/Had) & செயல் துணை வினைகள் (Do/Does/Did)",
            introExplanationTamil = "நம்மிடம் ஒரு பொருள் 'இருக்கிறது' (உடைமை) என்று சொல்ல 'Have / Has' பயன்படுகிறது. கடந்த காலத்தில் 'இருந்தது' என்று சொல்ல 'Had' பயன்படுகிறது. அதேபோல் செயல்களைப் பற்றிக் கேள்வி கேட்கவும் 'இல்லை' என்று சொல்லவும் 'Do / Does / Did' பயன்படுகிறது.",
            formulaBoxTitle = "📐 Have / Has மற்றும் Do / Does விதிகள்",
            formulas = listOf(
                "Group A (I, We, You, They) -> have (உள்ளது) | do (செய்கிறேன்) | don't (செய்யவில்லை)",
                "Group B (He, She, It, Subi) -> has (உள்ளது) | does (செய்கிறாள்) | doesn't (செய்யவில்லை)",
                "Past Tense (கடந்த காலம் - எல்லா Subject-க்கும்) -> had (இருந்தது) | did / didn't (செய்தேன் / செய்யவில்லை)",
                "முக்கிய விதி: கேள்வியிலோ பதிலிலோ 'Does / Doesn't / Did / Didn't' வந்தால், அடுத்த வினைச்சொல் 'have' (அல்லது V1) ஆகத்தான் இருக்கும் ('has' வராது)!"
            ),
            tableHeaders = listOf("Subject", "உடைமை (Present)", "இல்லாதது (Negative)", "கேள்வி (Question)"),
            tableRows = listOf(
                PdfTableRow("I / We / You / They", "I have a pen.", "I don't have a pen.", "Do you have a pen?"),
                PdfTableRow("He / She / Subi", "Subi has a bicycle.", "Subi doesn't have a car.", "Does Subi have a bicycle?"),
                PdfTableRow("Past (நேற்று)", "I had fever.", "I didn't have fever.", "Did you have fever?")
            ),
            sentencesTitle = "🗣️ அன்றாட வாக்கியங்கள் (Have / Has / Do / Does)",
            sentences = listOf(
                PdfCourseSentence(
                    english = "I have a new story book and two pencils.",
                    tamilPronunciation = "ஐ ஹேவ் எ நியூ ஸ்டோரி புக் அண்ட் டூ பென்சில்ஸ்.",
                    tamilMeaning = "என்னிடம் ஒரு புதிய கதைப் புத்தகமும் இரண்டு பென்சில்களும் உள்ளன."
                ),
                PdfCourseSentence(
                    english = "Subiksha has a very sweet voice.",
                    tamilPronunciation = "சுபிக்சா ஹேஸ் எ வெரி ஸ்வீட் வாய்ஸ்.",
                    tamilMeaning = "சுபிக்சாவுக்கு மிகவும் இனிமையான குரல் இருக்கிறது.",
                    grammarNote = "Subiksha (She) என்பதால் 'has' வருகிறது."
                ),
                PdfCourseSentence(
                    english = "Do you have an extra pen? — Yes, I have one.",
                    tamilPronunciation = "டூ யூ ஹேவ் அன் எக்ஸ்ட்ரா பென்? — யெஸ், ஐ ஹேவ் ஒன்.",
                    tamilMeaning = "உன்னிடம் கூடுதல் பேனா இருக்கிறதா? — ஆமாம், என்னிடம் ஒன்று உள்ளது."
                ),
                PdfCourseSentence(
                    english = "Does she have class today? — No, she doesn't have class today.",
                    tamilPronunciation = "டஸ் ஷி ஹேவ் கிளாஸ் டுடே? — நோ, ஷி டசன்ட் ஹேவ் கிளாஸ் டுடே.",
                    tamilMeaning = "அவளுக்கு இன்று வகுப்பு இருக்கிறதா? — இல்லை, அவளுக்கு இன்று வகுப்பு இல்லை.",
                    grammarNote = "கவனிக்க: 'Does she have' தான் சரி, 'Does she has' என்பது தவறு!"
                ),
                PdfCourseSentence(
                    english = "I had a wonderful time at the park yesterday.",
                    tamilPronunciation = "ஐ ஹேட் எ வொண்டர்ஃபுல் டைம் அட் தி பார்க் எஸ்டர்டே.",
                    tamilMeaning = "நேற்று பூங்காவில் எனக்கு ஒரு மகிழ்ச்சியான நேரம் அமைந்தது."
                ),
                PdfCourseSentence(
                    english = "I do my homework every evening without fail.",
                    tamilPronunciation = "ஐ டூ மை ஹோம்வொர்க் எவ்ரி ஈவ்னிங் வித்தவுட் ஃபெயில்.",
                    tamilMeaning = "நான் தினமும் மாலையில் தவறாமல் வீட்டுப்பாடம் செய்கிறேன்."
                )
            ),
            teacherTipTamil = "தனம் டீச்சர் குறிப்பு: 'She has a book' என்று சொல்வோம்; ஆனால் 'அவளிடம் புத்தகம் இல்லையா?' என்று கேட்கும்போது 'Doesn't she have a book?' என்று 'have' தான் போட வேண்டும்!"
        ),

        // PAGE 6: UNIT 3 - SIMPLE PRESENT & PRESENT CONTINUOUS
        PdfCoursePage(
            pageNumber = 6,
            unitTag = "UNIT 3 • நிகழ்காலம் (PRESENT TENSES)",
            dayRange = "Days 7 – 10",
            titleEnglish = "Simple Present (Daily Habits) vs Present Continuous (Happening Now)",
            titleTamil = "சாதாரண நிகழ்காலம் (தினசரி பழக்கம்) & தொடர் நிகழ்காலம் (இப்போது நடப்பது)",
            introExplanationTamil = "நாம் தினமும் செய்யும் செயல்களைச் சொல்ல Simple Present (V1) பயன்படுத்த வேண்டும். நாம் பேசிக்கொண்டிருக்கும் இந்த நொடியில் நடந்து கொண்டிருக்கும் செயல்களைச் சொல்ல Present Continuous (am/is/are + V-ing) பயன்படுத்த வேண்டும்.",
            formulaBoxTitle = "📐 நிகழ்காலப் பார்முலாக்கள் (Present Tense Formulas)",
            formulas = listOf(
                "1. Simple Present (தினமும் / வழக்கமாக): I/We/You/They + V1 (go) | He/She/It/Subi + V1+s/es (goes)",
                "   Negative: don't + V1 / doesn't + V1 | Question: Do + Sub + V1? / Does + Sub + V1?",
                "2. Present Continuous (இப்போது நடப்பது): Sub + am / is / are + Verb-ing (going, reading, speaking)",
                "   Key Words: Simple Present -> daily, always, usually, every morning | Continuous -> now, right now, at this moment"
            ),
            tableHeaders = listOf("Subject", "Simple Present (தினமும்)", "Present Continuous (இப்போது)", "தமிழ் வேறுபாடு"),
            tableRows = listOf(
                PdfTableRow("I", "I read English daily.", "I am reading English now.", "படிக்கிறேன் vs படித்துக்கொண்டிருக்கிறேன்"),
                PdfTableRow("You / We / They", "You speak well.", "You are speaking well.", "பேசுகிறாய் vs பேசிக்கொண்டிருக்கிறாய்"),
                PdfTableRow("She / Subi", "Subi goes to school.", "Subi is going to school.", "போகிறாள் vs போய்க்கொண்டிருக்கிறாள்"),
                PdfTableRow("He", "He plays cricket.", "He is playing cricket.", "விளையாடுகிறான் vs விளையாடிக்கொண்டிருக்கிறான்")
            ),
            sentencesTitle = "🗣️ நிகழ்கால வாக்கியப் பயிற்சி (Present Tense Practice)",
            sentences = listOf(
                PdfCourseSentence(
                    english = "I wake up at 6 o'clock every morning.",
                    tamilPronunciation = "ஐ வேக் அப் அட் சிக்ஸ் ஓ கிளாக் எவ்ரி மார்னிங்.",
                    tamilMeaning = "நான் தினமும் காலை 6 மணிக்கு எழுந்திருக்கிறேன்."
                ),
                PdfCourseSentence(
                    english = "My mother cooks delicious food for us.",
                    tamilPronunciation = "மை மதர் குக்ஸ் டெலிஷியஸ் ஃபுட் ஃபார் அஸ்.",
                    tamilMeaning = "என் அம்மா எங்களுக்காக சுவையான உணவு சமைக்கிறார்.",
                    grammarNote = "My mother (She) என்பதால் cook -> cooks என 's' சேர்க்கப்பட்டுள்ளது."
                ),
                PdfCourseSentence(
                    english = "Does Subi like ice cream? — Yes, she loves ice cream!",
                    tamilPronunciation = "டஸ் சுபி லைக் ஐஸ்கிரீம்? — யெஸ், ஷி லவ்ஸ் ஐஸ்கிரீம்!",
                    tamilMeaning = "சுபிக்கு ஐஸ்கிரீம் பிடிக்குமா? — ஆமாம், அவளுக்கு ஐஸ்கிரீம் மிகவும் பிடிக்கும்!"
                ),
                PdfCourseSentence(
                    english = "What are you doing right now? — I am learning Spoken English.",
                    tamilPronunciation = "வாட் ஆர் யூ டூயிங் ரைட் நவ்? — ஐ அம் லேர்னிங் ஸ்போக்கன் இங்கிலீஷ்.",
                    tamilMeaning = "நீ இப்போது என்ன செய்து கொண்டிருக்கிறாய்? — நான் ஸ்போக்கன் இங்கிலீஷ் கற்றுக் கொண்டிருக்கிறேன்."
                ),
                PdfCourseSentence(
                    english = "Dhanam Teacher is explaining the grammar rule clearly.",
                    tamilPronunciation = "தனம் டீச்சர் இஸ் எக்ஸ்பிளைனிங் தி கிராமர் ரூல் கிளியர்லி.",
                    tamilMeaning = "தனம் டீச்சர் இலக்கண விதியைத் தெளிவாக விளக்கிக் கொண்டிருக்கிறார்."
                ),
                PdfCourseSentence(
                    english = "Look! It is raining outside right now.",
                    tamilPronunciation = "லுக்! இட் இஸ் ரெயினிங் அவுட்சைடு ரைட் நவ்.",
                    tamilMeaning = "அங்கே பார்! வெளியே இப்போது மழை பெய்து கொண்டிருக்கிறது."
                )
            ),
            teacherTipTamil = "தனம் டீச்சர் குறிப்பு: 'நான் வருகிறேன்' என்று இப்போதைய செயலைச் சொல்லும்போது 'I am coming' என்று சொல்லுங்கள்!"
        ),

        // PAGE 7: UNIT 4 - SIMPLE PAST & SIMPLE FUTURE
        PdfCoursePage(
            pageNumber = 7,
            unitTag = "UNIT 4 • கடந்த காலம் & எதிர்காலம்",
            dayRange = "Days 11 – 13",
            titleEnglish = "Simple Past Tense (Completed Actions) & Simple Future Tense (Will)",
            titleTamil = "சாதாரண கடந்த காலம் (நடந்து முடிந்தது) & சாதாரண எதிர்காலம் (நடக்கப்போவது)",
            introExplanationTamil = "நேற்று, கடந்த வாரம் என நடந்து முடிந்த செயல்களைச் சொல்ல வினைச்சொல்லின் இரண்டாம் வடிவத்தை (Past Verb - V2: went, ate, saw, came, spoke) பயன்படுத்த வேண்டும். நாளை, அடுத்த வாரம் என இனி நடக்கப்போகும் செயல்களைச் சொல்ல 'will + V1' பயன்படுத்த வேண்டும்.",
            formulaBoxTitle = "📐 Past & Future பார்முலா (மிக முக்கியமான விதி!)",
            formulas = listOf(
                "1. Simple Past Positive: Subject + V2 (எ.கா: I went = நான் போனேன், She came = அவள் வந்தாள்).",
                "2. Simple Past Negative: Subject + did not (didn't) + V1 (எ.கா: I didn't go — 'didn't went' என்று ஒருபோதும் சொல்லக்கூடாது!).",
                "3. Simple Past Question: Did + Subject + V1? (எ.கா: Did you eat? = நீ சாப்பிட்டாயா?).",
                "4. Simple Future: Subject + will + V1 | Negative: won't (will not) + V1 | Question: Will + Subject + V1?"
            ),
            tableHeaders = listOf("V1 (Present)", "V2 (Past)", "Past Sentence (நேற்று)", "Future Sentence (நாளை)"),
            tableRows = listOf(
                PdfTableRow("go (போ)", "went (போனேன்)", "I went to school.", "I will go to school."),
                PdfTableRow("come (வா)", "came (வந்தேன்)", "She came early.", "She will come tomorrow."),
                PdfTableRow("eat (சாப்பிடு)", "ate (சாப்பிட்டேன்)", "We ate idli.", "We will eat dosa."),
                PdfTableRow("see (பார்)", "saw (பார்த்தேன்)", "I saw my friend.", "I will see you soon."),
                PdfTableRow("speak (பேசு)", "spoke (பேசினேன்)", "Subi spoke in English.", "Subi will speak on stage."),
                PdfTableRow("buy (வாங்கு)", "bought (வாங்கினேன்)", "Dad bought a dress.", "Dad will buy a gift.")
            ),
            sentencesTitle = "🗣️ கடந்த காலம் மற்றும் எதிர்கால வாக்கியங்கள்",
            sentences = listOf(
                PdfCourseSentence(
                    english = "I finished my homework an hour ago.",
                    tamilPronunciation = "ஐ ஃபினிஷ்ட் மை ஹோம்வொர்க் அன் ஹவர் அகோ.",
                    tamilMeaning = "நான் ஒரு மணி நேரத்திற்கு முன்பே என் வீட்டுப்பாடத்தை முடித்துவிட்டேன்."
                ),
                PdfCourseSentence(
                    english = "Did you call me yesterday? — No, I didn't call you.",
                    tamilPronunciation = "டிட் யூ கால் மீ எஸ்டர்டே? — நோ, ஐ டிடின்ட் கால் யூ.",
                    tamilMeaning = "நீ நேற்று என்னைக் கூப்பிட்டாயா? — இல்லை, நான் உன்னைக் கூப்பிடவில்லை.",
                    grammarNote = "Did மற்றும் didn't உடன் எப்போதும் V1 (call) மட்டுமே வரும்!"
                ),
                PdfCourseSentence(
                    english = "Where did you go last Sunday? — We went to the beach.",
                    tamilPronunciation = "வேர் டிட் யூ கோ லாஸ்ட் சண்டே? — வீ வென்ட் டு தி பீச்.",
                    tamilMeaning = "கடந்த ஞாயிற்றுக்கிழமை நீங்கள் எங்கே போனீர்கள்? — நாங்கள் கடற்கரைக்குப் போனோம்."
                ),
                PdfCourseSentence(
                    english = "I will call you back in ten minutes.",
                    tamilPronunciation = "ஐ வில் கால் யூ பேக் இன் டென் மினிட்ஸ்.",
                    tamilMeaning = "நான் பத்து நிமிடங்களில் உன்னைத் திரும்ப அழைப்பேன்."
                ),
                PdfCourseSentence(
                    english = "Will you come to school tomorrow? — Yes, I will definitely come.",
                    tamilPronunciation = "வில் யூ கம் டு ஸ்கூல் டுமாரோ? — யெஸ், ஐ வில் டெஃபனிட்லி கம்.",
                    tamilMeaning = "நீ நாளை பள்ளிக்கு வருவாயா? — ஆமாம், நான் நிச்சயமாக வருவேன்."
                ),
                PdfCourseSentence(
                    english = "Don't worry, I won't forget your birthday!",
                    tamilPronunciation = "டோன்ட் வொர்ரி, ஐ ஓன்ட் ஃபர்கெட் யுவர் பர்த்டே!",
                    tamilMeaning = "கவலைப்படாதே, உன் பிறந்தநாளை நான் மறக்க மாட்டேன்! (will not = won't)"
                )
            ),
            teacherTipTamil = "தனம் டீச்சர் குறிப்பு: 'Did you ate?' ❌ என்பது தவறு! 'Did you eat?' ✅ (நீ சாப்பிட்டாயா?) என்பதே சரி!"
        ),

        // PAGE 8: UNIT 5 (PART A) - PRESENT PERFECT, PAST CONTINUOUS & GOING TO
        PdfCoursePage(
            pageNumber = 8,
            unitTag = "UNIT 5 • முற்றுப்பெற்ற காலங்கள்",
            dayRange = "Days 14 – 15",
            titleEnglish = "Present Perfect (Have/Has + V3), Past Continuous & 'Going to'",
            titleTamil = "முற்றுப்பெற்ற நிகழ்காலம் (செய்துவிட்டேன்) & தொடர் கடந்த காலம்",
            introExplanationTamil = "தமிழில் 'நான் சாப்பிட்டு விட்டேன்', 'அவள் வந்துவிட்டாள்' என்று சற்று முன் முடிந்த செயலைச் சொல்ல Present Perfect (have/has + V3) பயன்படுகிறது. நேற்று ஒரு நேரத்தில் நடந்து கொண்டிருந்த செயலைச் சொல்ல Past Continuous (was/were + V-ing) பயன்படுகிறது.",
            formulaBoxTitle = "📐 3 முக்கிய பார்முலாக்கள் (Unit 5 Formulas)",
            formulas = listOf(
                "1. Present Perfect: I/We/You/They + have + V3 | He/She/It/Subi + has + V3 (எ.கா: I have eaten = நான் சாப்பிட்டுவிட்டேன்).",
                "2. Past Continuous: I/He/She/It + was + V-ing | We/You/They + were + V-ing (எ.கா: I was reading = படித்துக்கொண்டிருந்தேன்).",
                "3. Planned Future ('Going to'): Sub + am/is/are + going to + V1 (எ.கா: I am going to speak = நான் பேசப்போகிறேன்)."
            ),
            tableHeaders = listOf("Tense", "Formula", "English Example", "தமிழ் அர்த்தம்"),
            tableRows = listOf(
                PdfTableRow("Present Perfect", "have/has + V3", "I have finished my work.", "நான் என் வேலையை முடித்துவிட்டேன்."),
                PdfTableRow("Present Perfect", "has + V3", "Subi has gone to school.", "சுபி பள்ளிக்குச் சென்றுவிட்டாள்."),
                PdfTableRow("Present Perfect Q", "Have you + V3?", "Have you eaten breakfast?", "நீ காலை உணவு சாப்பிட்டுவிட்டாயா?"),
                PdfTableRow("Past Continuous", "was/were + V-ing", "I was sleeping at 10 PM.", "நான் இரவு 10 மணிக்குத் தூங்கிக்கொண்டிருந்தேன்."),
                PdfTableRow("Going to Future", "am/is/are going to", "We are going to win.", "நாங்கள் வெற்றி பெறப் போகிறோம்.")
            ),
            sentencesTitle = "🗣️ வாக்கியப் பயிற்சி (Perfect & Continuous Sentences)",
            sentences = listOf(
                PdfCourseSentence(
                    english = "Have you had your lunch? — Yes, I have just had my lunch.",
                    tamilPronunciation = "ஹேவ் யூ ஹேட் யுவர் லஞ்ச்? — யெஸ், ஐ ஹேவ் ஜஸ்ட் ஹேட் மை லஞ்ச்.",
                    tamilMeaning = "நீ மதிய உணவு சாப்பிட்டுவிட்டாயா? — ஆமாம், நான் இப்போதுதான் சாப்பிட்டேன்."
                ),
                PdfCourseSentence(
                    english = "I have already completed all my lessons.",
                    tamilPronunciation = "ஐ ஹேவ் ஆல்ரெடி கம்ப்ளீட்டட் ஆல் மை லெசன்ஸ்.",
                    tamilMeaning = "நான் ஏற்கனவே என் எல்லாப் பாடங்களையும் முடித்துவிட்டேன்."
                ),
                PdfCourseSentence(
                    english = "Have you ever seen the Taj Mahal? — No, I have never seen it.",
                    tamilPronunciation = "ஹேவ் யூ எவர் சீன் தி தாஜ்மஹால்? — நோ, ஐ ஹேவ் நெவர் சீன் இட்.",
                    tamilMeaning = "நீ எப்போதாவது தாஜ்மஹாலைப் பார்த்திருக்கிறாயா? — இல்லை, நான் ஒருபோதும் பார்த்ததில்லை."
                ),
                PdfCourseSentence(
                    english = "What were you doing when I called you?",
                    tamilPronunciation = "வாட் வேர் யூ டூயிங் வென் ஐ கால்ட் யூ?",
                    tamilMeaning = "நான் உன்னை அழைத்தபோது நீ என்ன செய்து கொண்டிருந்தாய்?"
                ),
                PdfCourseSentence(
                    english = "I was practicing English with Dhanam Teacher.",
                    tamilPronunciation = "ஐ வாஸ் பிராக்டிசிங் இங்கிலீஷ் வித் தனம் டீச்சர்.",
                    tamilMeaning = "நான் தனம் டீச்சருடன் ஆங்கிலம் பயிற்சி செய்து கொண்டிருந்தேன்."
                ),
                PdfCourseSentence(
                    english = "I am going to tell a moral story in assembly tomorrow.",
                    tamilPronunciation = "ஐ அம் கோயிங் டு டெல் எ மாரல் ஸ்டோரி இன் அசெம்பிளி டுமாரோ.",
                    tamilMeaning = "நான் நாளை பள்ளி இறைவணக்கக் கூட்டத்தில் ஒரு நீதிக்கதை சொல்லப் போகிறேன்."
                )
            ),
            teacherTipTamil = "தனம் டீச்சர் குறிப்பு: குறிப்பிட்ட கடந்த கால நேரம் (yesterday, last week) வந்தால் 'I went' (Simple Past) பயன்படுத்தவும்; நேரம் குறிப்பிடாமல் 'போயிருக்கிறேன் / போய்விட்டேன்' எனில் 'I have gone' பயன்படுத்தவும்!"
        ),

        // PAGE 9: UNIT 5 (PART B) - 12 TENSES MASTER CHART IN ONE PAGE
        PdfCoursePage(
            pageNumber = 9,
            unitTag = "UNIT 5 MASTER CHART • 12 காலங்கள்",
            dayRange = "Day 16",
            titleEnglish = "All 12 English Tenses Master Chart in One Page (Verb: Speak - பேசு)",
            titleTamil = "ஆங்கிலத்தின் 12 காலங்களும் ஒரே பார்வையில் (Master Reference Table)",
            introExplanationTamil = "இந்த ஒரு பக்க அட்டவணையில் ஆங்கிலத்தில் உள்ள 12 காலங்களின் (12 Tenses) பார்முலா, ஆங்கில உதாரணம் மற்றும் தமிழ் அர்த்தம் வரிசையாகக் கொடுக்கப்பட்டுள்ளன. தினசரி பேச்சில் இதில் முதல் 6 காலங்களே 90% பயன்படுகின்றன!",
            formulaBoxTitle = "📐 வினைச்சொல்லின் 4 வடிவங்கள் (4 Forms of 'Speak')",
            formulas = listOf(
                "V1 (Present): speak / speaks | V2 (Past): spoke | V3 (Past Participle): spoken | V4 (Continuous): speaking",
                "⭐ அன்றாடம் அதிகம் பயன்படும் 6 காலங்கள்: Simple Present, Present Continuous, Present Perfect, Simple Past, Past Continuous, Simple Future."
            ),
            tableHeaders = listOf("Tense Name", "Formula", "Example (I / Subi)", "தமிழ் அர்த்தம்"),
            tableRows = listOf(
                PdfTableRow("1. Simple Present", "Sub + V1 (s/es)", "I speak English.", "நான் ஆங்கிலம் பேசுகிறேன்."),
                PdfTableRow("2. Simple Past", "Sub + V2", "I spoke English.", "நான் ஆங்கிலம் பேசினேன்."),
                PdfTableRow("3. Simple Future", "Sub + will + V1", "I will speak English.", "நான் ஆங்கிலம் பேசுவேன்."),
                PdfTableRow("4. Present Continuous", "am/is/are + V-ing", "I am speaking English.", "நான் ஆங்கிலம் பேசிக்கொண்டிருக்கிறேன்."),
                PdfTableRow("5. Past Continuous", "was/were + V-ing", "I was speaking English.", "நான் ஆங்கிலம் பேசிக்கொண்டிருந்தேன்."),
                PdfTableRow("6. Future Continuous", "will be + V-ing", "I will be speaking English.", "நான் ஆங்கிலம் பேசிக்கொண்டிருப்பேன்."),
                PdfTableRow("7. Present Perfect", "have/has + V3", "I have spoken English.", "நான் ஆங்கிலம் பேசிவிட்டேன்."),
                PdfTableRow("8. Past Perfect", "had + V3", "I had spoken before he came.", "அவன் வருவதற்கு முன் நான் பேசியிருந்தேன்."),
                PdfTableRow("9. Future Perfect", "will have + V3", "I will have spoken by 5 PM.", "5 மணிக்குள் நான் பேசி முடித்திருப்பேன்."),
                PdfTableRow("10. Pres. Perf. Cont.", "have/has been + V-ing", "I have been speaking for 1 hour.", "1 மணி நேரமாகப் பேசிக்கொண்டிருக்கிறேன்."),
                PdfTableRow("11. Past Perf. Cont.", "had been + V-ing", "I had been speaking for 1 hour.", "1 மணி நேரமாகப் பேசிக்கொண்டிருந்தேன்."),
                PdfTableRow("12. Fut. Perf. Cont.", "will have been + V-ing", "I will have been speaking.", "நான் தொடர்ந்து பேசிக்கொண்டிருப்பேன்.")
            ),
            sentencesTitle = "🗣️ 10-வது காலத்தின் முக்கியப் பயன்பாடு (Since vs For)",
            sentences = listOf(
                PdfCourseSentence(
                    english = "I have been studying in this school since 2022.",
                    tamilPronunciation = "ஐ ஹேவ் பீன் ஸ்டடியிங் இன் திஸ் ஸ்கூல் சின்ஸ் 2022.",
                    tamilMeaning = "நான் 2022-ஆம் ஆண்டிலிருந்து இந்தப் பள்ளியில் படித்துக் கொண்டிருக்கிறேன்.",
                    grammarNote = "கடந்த காலத்தில் தொடங்கி இப்போதும் தொடரும் செயலுக்கு 'have/has been + V-ing' வரும்."
                ),
                PdfCourseSentence(
                    english = "Subiksha has been practicing English for two hours.",
                    tamilPronunciation = "சுபிக்சா ஹேஸ் பீன் பிராக்டிசிங் இங்கிலீஷ் ஃபார் டூ ஹவர்ஸ்.",
                    tamilMeaning = "சுபிக்சா இரண்டு மணி நேரமாக ஆங்கிலம் பயிற்சி செய்து கொண்டிருக்கிறாள்.",
                    grammarNote = "தொடங்கிய நேரம் (2022, Monday) எனில் 'since'; கால அளவு (2 hours, 5 days) எனில் 'for'!"
                )
            ),
            teacherTipTamil = "தனம் டீச்சர் குறிப்பு: இந்த 12 காலங்களின் அட்டவணையை ஒரு முறை மனதில் பதிய வைத்துக்கொண்டால், எந்த வினைச்சொல்லையும் (eat, go, write, play) வைத்து நூற்றுக்கணக்கான வாக்கியங்களை உருவாக்கலாம்!"
        ),

        // PAGE 10: UNIT 6 - MODAL VERBS
        PdfCoursePage(
            pageNumber = 10,
            unitTag = "UNIT 6 • துணை வினைச்சொற்கள் (MODALS)",
            dayRange = "Days 17 – 19",
            titleEnglish = "Modal Auxiliary Verbs: Can, Could, Should, Must, May, Would",
            titleTamil = "முடியும், வேண்டும், கடமை மற்றும் பணிவான வேண்டுகோள் (Modal Verbs)",
            introExplanationTamil = "நமது திறமை (Can), பணிவான வேண்டுகோள் (Could / Would), அறிவுரை (Should), கட்டாயக் கடமை (Must / Have to), மற்றும் அனுமதி (May) ஆகியவற்றை வெளிப்படுத்த Modal Verbs பயன்படுகின்றன. எல்லா Modal Verbs-க்குப் பிறகும் வினைச்சொல்லின் முதல் வடிவம் (V1) மட்டுமே வரும்!",
            formulaBoxTitle = "📐 Modal Verbs தங்க விதி: Subject + Modal Verb + V1 (Base Verb)",
            formulas = listOf(
                "Can + V1 = முடியும் (திறமை) | Cannot (Can't) + V1 = முடியாது",
                "Could + V1 = முடிந்தது (Past) / பணிவாகக் கேட்க (Could you please...?)",
                "Should + V1 = செய்ய வேண்டும் (நல்ல அறிவுரை - Advice)",
                "Must / Have to + V1 = கண்டிப்பாகச் செய்ய வேண்டும் (கட்டாயம்)",
                "May + V1 = அனுமதி கேட்க (May I...?) / ஒருவேளை நடக்கலாம்",
                "Would like to + V1 = விரும்புகிறேன் (மரியாதையாக விருப்பத்தைச் சொல்ல)"
            ),
            tableHeaders = listOf("Modal Verb", "தமிழ் அர்த்தம்", "English Sentence", "தமிழ் உச்சரிப்பு"),
            tableRows = listOf(
                PdfTableRow("Can", "முடியும்", "I can speak English.", "ஐ கேன் ஸ்பீக் இங்கிலீஷ்."),
                PdfTableRow("Can't", "முடியாது", "I can't lift this box.", "ஐ கான்ட் லிஃப்ட் திஸ் பாக்ஸ்."),
                PdfTableRow("Could", "பணிவான கேள்வி", "Could you help me?", "குட் யூ ஹெல்ப் மீ?"),
                PdfTableRow("Should", "செய்ய வேண்டும்", "You should sleep early.", "யூ ஷுட் ஸ்லீப் ஏர்லி."),
                PdfTableRow("Must", "கட்டாயம் வேண்டும்", "We must obey rules.", "வீ மஸ்ட் ஒபே ரூல்ஸ்."),
                PdfTableRow("May", "அனுமதி", "May I come in, Madam?", "மே ஐ கம் இன், மேடம்?"),
                PdfTableRow("Would like to", "விரும்புகிறேன்", "I would like to drink milk.", "ஐ வுட் லைக் டு டிரிங்க் மில்க்.")
            ),
            sentencesTitle = "🗣️ Modal Verbs வாக்கியப் பயிற்சி",
            sentences = listOf(
                PdfCourseSentence(
                    english = "I can read, write, and speak English confidently.",
                    tamilPronunciation = "ஐ கேன் ரீட், ரைட், அண்ட் ஸ்பீக் இங்கிலீஷ் கான்ஃபிடன்ட்லி.",
                    tamilMeaning = "என்னால் நம்பிக்கையுடன் ஆங்கிலம் படிக்கவும், எழுதவும், பேசவும் முடியும்."
                ),
                PdfCourseSentence(
                    english = "Could you please repeat that sentence slowly?",
                    tamilPronunciation = "குட் யூ ப்ளீஸ் ரிப்பீட் தட் சென்டென்ஸ் ஸ்லோலி?",
                    tamilMeaning = "தயவுசெய்து அந்த வாக்கியத்தை மெதுவாக மீண்டும் சொல்ல முடியுமா?"
                ),
                PdfCourseSentence(
                    english = "You should brush your teeth twice a day.",
                    tamilPronunciation = "யூ ஷுட் பிரஷ் யுவர் டீத் ட்வைஸ் எ டே.",
                    tamilMeaning = "நீ தினமும் இரண்டு முறை பல் துலக்க வேண்டும்."
                ),
                PdfCourseSentence(
                    english = "We must respect our parents and teachers.",
                    tamilPronunciation = "வீ மஸ்ட் ரெஸ்பெக்ட் அவர் பேரண்ட்ஸ் அண்ட் டீச்சர்ஸ்.",
                    tamilMeaning = "நாம் நமது பெற்றோரையும் ஆசிரியர்களையும் கண்டிப்பாக மதிக்க வேண்டும்."
                ),
                PdfCourseSentence(
                    english = "May I drink some water, Dhanam Teacher?",
                    tamilPronunciation = "மே ஐ டிரிங்க் சம் வாட்டர், தனம் டீச்சர்?",
                    tamilMeaning = "தனம் டீச்சர், நான் கொஞ்சம் தண்ணீர் குடிக்கலாமா?"
                ),
                PdfCourseSentence(
                    english = "I would like to ask a question, please.",
                    tamilPronunciation = "ஐ வுட் லைக் டு ஆஸ்க் எ கொஸ்டின், ப்ளீஸ்.",
                    tamilMeaning = "நான் தயவுசெய்து ஒரு கேள்வி கேட்க விரும்புகிறேன்."
                )
            ),
            teacherTipTamil = "தனம் டீச்சர் குறிப்பு: பெரியவர்களிடமும் ஆசிரியர்களிடமும் உதவி கேட்கும்போது 'Can you...' என்பதற்குப் பதிலாக 'Could you please...' என்று கேட்டால் மிகவும் மரியாதையாக இருக்கும்!"
        ),

        // PAGE 11: UNIT 7 - WH QUESTIONS
        PdfCoursePage(
            pageNumber = 11,
            unitTag = "UNIT 7 • கேள்வி கேட்கும் கலை",
            dayRange = "Days 20 – 21",
            titleEnglish = "Mastering WH-Questions (What, Where, When, Why, Who, How)",
            titleTamil = "ஆங்கிலத்தில் கேள்விகள் கேட்பது எப்படி? (WH-Questions Formula)",
            introExplanationTamil = "ஆங்கிலத்தில் உரையாடலைத் தொடங்கவும் தொடரவும் கேள்விகள் கேட்கத் தெரிவது மிக அவசியம். எல்லா WH-கேள்விகளுக்கும் ஒரே ஒரு எளிய பார்முலாதான்: முதலில் கேள்விச் சொல் (WH-Word), அடுத்து துணை வினைச்சொல் (Helping Verb), அடுத்து எழுவாய் (Subject), அடுத்து முக்கிய வினைச்சொல் (Main Verb)!",
            formulaBoxTitle = "📐 கேள்வி வாக்கியப் பார்முலா: WH-Word + Helping Verb + Subject + Main Verb?",
            formulas = listOf(
                "உதாரணம் 1: Where (எங்கே) + do (Helping Verb) + you (Subject) + live (Main Verb)? = நீ எங்கே வசிக்கிறாய்?",
                "உதாரணம் 2: What (என்ன) + are (Helping Verb) + you (Subject) + doing (Main Verb)? = நீ என்ன செய்துகொண்டிருக்கிறாய்?",
                "உதாரணம் 3: When (எப்போது) + will (Helping Verb) + she (Subject) + come (Main Verb)? = அவள் எப்போது வருவாள்?",
                "கவனிக்க: 'Where you are going?' ❌ தவறு! -> 'Where are you going?' ✅ என்பதே சரி!"
            ),
            tableHeaders = listOf("WH-Word", "தமிழ் அர்த்தம்", "உச்சரிப்பு", "Example Question"),
            tableRows = listOf(
                PdfTableRow("What", "என்ன", "வாட்", "What is your name? (உன் பெயர் என்ன?)"),
                PdfTableRow("Where", "எங்கே", "வேர்", "Where are you going? (எங்கே போகிறாய்?)"),
                PdfTableRow("When", "எப்பொழுது", "வென்", "When does school start? (பள்ளி எப்போது தொடங்கும்?)"),
                PdfTableRow("Why", "ஏன்", "ஒய்", "Why are you laughing? (ஏன் சிரிக்கிறாய்?)"),
                PdfTableRow("Who", "யார்", "ஹூ", "Who is your class teacher? (உன் வகுப்பு ஆசிரியர் யார்?)"),
                PdfTableRow("Whose", "யாருடைய", "ஹூஸ்", "Whose book is this? (இது யாருடைய புத்தகம்?)"),
                PdfTableRow("Which", "எந்த", "விச்", "Which color do you like? (உனக்கு எந்த நிறம் பிடிக்கும்?)"),
                PdfTableRow("How", "எப்படி", "ஹவ்", "How do you go to school? (பள்ளிக்கு எப்படிப் போகிறாய்?)"),
                PdfTableRow("How many", "எத்தனை (எண்ணக்கூடியது)", "ஹவ் மெனி", "How many pencils do you have? (எத்தனை பென்சில்கள்?)"),
                PdfTableRow("How much", "எவ்வளவு (விலை/அளவு)", "ஹவ் மச்", "How much does this cost? (இதன் விலை எவ்வளவு?)")
            ),
            sentencesTitle = "🗣️ தினசரி கேள்வி-பதில் வாக்கியங்கள்",
            sentences = listOf(
                PdfCourseSentence(
                    english = "What would you like to eat for breakfast?",
                    tamilPronunciation = "வாட் வுட் யூ லைக் டு ஈட் ஃபார் பிரேக்ஃபாஸ்ட்?",
                    tamilMeaning = "காலை உணவுக்கு நீ என்ன சாப்பிட விரும்புகிறாய்?"
                ),
                PdfCourseSentence(
                    english = "Where did you keep my English book?",
                    tamilPronunciation = "வேர் டிட் யூ கீப் மை இங்கிலீஷ் புக்?",
                    tamilMeaning = "என் ஆங்கிலப் புத்தகத்தை எங்கே வைத்தாய்?"
                ),
                PdfCourseSentence(
                    english = "Why were you late to class today?",
                    tamilPronunciation = "ஒய் வேர் யூ லேட் டு கிளாஸ் டுடே?",
                    tamilMeaning = "இன்று வகுப்பிற்கு ஏன் தாமதமாக வந்தாய்?"
                ),
                PdfCourseSentence(
                    english = "How old are you, Subi? — I am 9 years old.",
                    tamilPronunciation = "ஹவ் ஓல்ட் ஆர் யூ, சுபி? — ஐ அம் நைன் இயர்ஸ் ஓல்ட்.",
                    tamilMeaning = "சுபி, உனக்கு எத்தனை வயது? — எனக்கு 9 வயது."
                ),
                PdfCourseSentence(
                    english = "How long will it take to reach the station?",
                    tamilPronunciation = "ஹவ் லாங் வில் இட் டேக் டு ரீச் தி ஸ்டேஷன்?",
                    tamilMeaning = "நிலையத்தை அடைய எவ்வளவு நேரம் ஆகும்?"
                )
            ),
            teacherTipTamil = "தனம் டீச்சர் குறிப்பு: கேள்வி கேட்கும்போது எப்போதும் துணை வினைச்சொல்லை (am/is/are/do/does/did/will/can) Subject-க்கு முன்னால் போட மறக்காதீர்கள்!"
        ),

        // PAGE 12: UNIT 8 - PREPOSITIONS & CONJUNCTIONS
        PdfCoursePage(
            pageNumber = 12,
            unitTag = "UNIT 8 • இணைப்புச் சொற்கள்",
            dayRange = "Days 22 – 23",
            titleEnglish = "Prepositions (In, On, At, To, From, With) & Conjunctions",
            titleTamil = "முன்னிடைச் சொற்கள் (Prepositions) & இணைப்புச் சொற்கள் (Conjunctions)",
            introExplanationTamil = "தமிழில் 'பள்ளியில்', 'மேசை மேல்', '6 மணிக்கு' என்று சொல்லின் கடைசியில் உருபுகளைச் சேர்ப்போம். ஆங்கிலத்தில் இவற்றை வார்த்தைக்கு முன்னால் (in school, on the table, at 6 o'clock) போட வேண்டும். அதேபோல் இரண்டு சிறிய வாக்கியங்களை இணைத்துப் பேச Conjunctions (and, but, because, so) உதவுகின்றன.",
            formulaBoxTitle = "📐 IN, ON, AT பயன்படுத்தும் எளிய விதி",
            formulas = listOf(
                "AT (குறிப்பிட்ட நேரம் & சிறிய இடம்): at 7 AM, at night, at home, at the bus stop, at school",
                "ON (கிழமைகள், தேதிகள் & மேற்பரப்பு): on Monday, on June 15th, on my birthday, on the table",
                "IN (மாதங்கள், வருடங்கள், பெரிய ஊர்கள் & உள்ளே): in May, in 2026, in Chennai, in the morning, in my bag",
                "Conjunctions: and (மற்றும்), but (ஆனால்), because (ஏனென்றால்), so (ஆகையால்), if (என்றால்), when (பொழுது)"
            ),
            tableHeaders = listOf("Word", "தமிழ் அர்த்தம்", "Usage / Rule", "Example Sentence"),
            tableRows = listOf(
                PdfTableRow("in", "உள்ளே / இல்", "மாதம், வருடம், பெரிய இடம்", "The books are in my bag."),
                PdfTableRow("on", "மேலே / அன்று", "கிழமை, தேதி, மேற்பரப்பு", "The apple is on the table."),
                PdfTableRow("at", "இல் / மணிக்கு", "துல்லியமான நேரம், இடம்", "Class starts at 9 o'clock."),
                PdfTableRow("to / from", "க்கு / இருந்து", "இடம் நோக்கி / இடத்திலிருந்து", "I go to school from home."),
                PdfTableRow("with / for", "உடன் / க்காக", "யாருடன் / யாருக்காக", "I live with my parents."),
                PdfTableRow("because", "ஏனென்றால்", "காரணம் சொல்ல", "I am happy because I won."),
                PdfTableRow("but / so", "ஆனால் / அதனால்", "முரண்பாடு / விளைவு", "It rained, so I stayed home.")
            ),
            sentencesTitle = "🗣️ நீளமான வாக்கியங்களை இணைத்துப் பேசும் பயிற்சி",
            sentences = listOf(
                PdfCourseSentence(
                    english = "My school starts at 8:30 AM in the morning.",
                    tamilPronunciation = "மை ஸ்கூல் ஸ்டார்ட்ஸ் அட் எய்ட் தர்ட்டி ஏ.எம் இன் தி மார்னிங்.",
                    tamilMeaning = "என் பள்ளி காலை 8:30 மணிக்குத் தொடங்குகிறது."
                ),
                PdfCourseSentence(
                    english = "We have a special English class on Friday.",
                    tamilPronunciation = "வீ ஹேவ் எ ஸ்பெஷல் இங்கிலீஷ் கிளாஸ் ஆன் ஃபிரைடே.",
                    tamilMeaning = "வெள்ளிக்கிழமை அன்று எங்களுக்குச் சிறப்பு ஆங்கில வகுப்பு உள்ளது."
                ),
                PdfCourseSentence(
                    english = "I bought this gift for my mother with my savings.",
                    tamilPronunciation = "ஐ பாட் திஸ் கிஃப்ட் ஃபார் மை மதர் வித் மை சேவிங்ஸ்.",
                    tamilMeaning = "என் சேமிப்புப் பணத்தைக் கொண்டு என் அம்மாவிற்காக இந்தப் பரிசை வாங்கினேன்."
                ),
                PdfCourseSentence(
                    english = "I wanted to play outside, but it started raining.",
                    tamilPronunciation = "ஐ வான்டட் டு பிளே அவுட்சைடு, பட் இட் ஸ்டார்ட்டட் ரெயினிங்.",
                    tamilMeaning = "நான் வெளியே விளையாட விரும்பினேன், ஆனால் மழை பெய்யத் தொடங்கிவிட்டது."
                ),
                PdfCourseSentence(
                    english = "Everyone likes Subi because she is always polite and cheerful.",
                    tamilPronunciation = "எவ்ரிஒன் லைக்ஸ் சுபி பிகாஸ் ஷி இஸ் ஆல்வேஸ் பொலைட் அண்ட் சியர்ஃபுல்.",
                    tamilMeaning = "சுபி எப்போதும் பணிவாகவும் உற்சாகமாகவும் இருப்பதால் எல்லோருக்கும் அவளைப் பிடிக்கும்."
                )
            ),
            teacherTipTamil = "தனம் டீச்சர் குறிப்பு: 'because', 'so', 'but', 'and' ஆகிய 4 வார்த்தைகளைப் பயன்படுத்தினால், சிறிய வாக்கியங்களை இணைத்துப் பெரியவர்கள்போல் அழகாகப் பேசலாம்!"
        ),

        // PAGE 13: UNIT 9 - IMPERATIVES, LET ME & LET US
        PdfCoursePage(
            pageNumber = 13,
            unitTag = "UNIT 9 • கட்டளை & வேண்டுகோள்",
            dayRange = "Days 24 – 25",
            titleEnglish = "Imperatives (Commands), Polite Requests, 'Let me' & 'Let's'",
            titleTamil = "கட்டளை வாக்கியங்கள், பணிவான வேண்டுகோள்கள் மற்றும் 'Let' பயன்பாடு",
            introExplanationTamil = "எதிரில் இருப்பவரிடம் ஒரு செயலைச் செய்யச் சொல்லும்போது Subject ('You') இல்லாமல் நேரடியாக வினைச்சொல்லில் (V1) தொடங்க வேண்டும். மரியாதையாகக் கேட்க 'Please' சேர்க்கவும். செய்ய வேண்டாம் என்று தடுக்க 'Don't + V1' பயன்படுத்தவும்.",
            formulaBoxTitle = "📐 கட்டளை மற்றும் 'Let' பார்முலாக்கள்",
            formulas = listOf(
                "1. நேர்மறைக் கட்டளை (Command): V1 + Object (எ.கா: Open your book = உன் புத்தகத்தைத் திற).",
                "2. பணிவான வேண்டுகோள் (Polite): Please + V1 (எ.கா: Please sit down = தயவுசெய்து உட்காருங்கள்).",
                "3. எதிர்மறைக் கட்டளை (Negative): Don't + V1 (எ.கா: Don't make noise = சத்தம் போடாதே).",
                "4. Let me + V1 (நான் செய்கிறேனே / என்னைச் செய்ய விடு): Let me speak = என்னைப் பேச விடு / நான் சொல்கிறேன்.",
                "5. Let's (Let us) + V1 (நாம் செய்யலாம் வாங்க): Let's start the lesson = நாம் பாடத்தைத் தொடங்குவோம்!"
            ),
            tableHeaders = listOf("Pattern", "English Expression", "தமிழ் உச்சரிப்பு", "தமிழ் அர்த்தம்"),
            tableRows = listOf(
                PdfTableRow("V1", "Come here quickly.", "கம் ஹியர் குயிக்லி.", "சீக்கிரம் இங்கே வா."),
                PdfTableRow("Please + V1", "Please wait a minute.", "ப்ளீஸ் வெயிட் எ மினிட்.", "தயவுசெய்து ஒரு நிமிடம் காத்திருங்கள்."),
                PdfTableRow("Don't + V1", "Don't waste your time.", "டோன்ட் வேஸ்ட் யுவர் டைம்.", "உன் நேரத்தை வீணாக்காதே."),
                PdfTableRow("Never + V1", "Never tell a lie.", "நெவர் டெல் எ லை.", "ஒருபோதும் பொய் சொல்லாதே."),
                PdfTableRow("Let me + V1", "Let me check the answer.", "லெட் மீ செக் தி ஆன்சர்.", "நான் பதிலைச் சரிபார்க்கிறேன்."),
                PdfTableRow("Let's + V1", "Let's speak in English!", "லெட்ஸ் ஸ்பீக் இன் இங்கிலீஷ்!", "நாம் ஆங்கிலத்தில் பேசுவோம் வாங்க!")
            ),
            sentencesTitle = "🗣️ வகுப்பறை மற்றும் வீட்டுக் கட்டளை வாக்கியங்கள்",
            sentences = listOf(
                PdfCourseSentence(
                    english = "Please listen to Dhanam Teacher carefully.",
                    tamilPronunciation = "ப்ளீஸ் லிசன் டு தனம் டீச்சர் கேர்ஃபுல்லி.",
                    tamilMeaning = "தயவுசெய்து தனம் டீச்சர் சொல்வதைக் கவனமாகக் கேளுங்கள்."
                ),
                PdfCourseSentence(
                    english = "Read this paragraph aloud with clear pronunciation.",
                    tamilPronunciation = "ரீட் திஸ் பேரகிராஃப் அலௌட் வித் கிளியர் புரொனன்சியேஷன்.",
                    tamilMeaning = "இந்தப் பத்தியைத் தெளிவான உச்சரிப்புடன் சத்தமாகப் படி."
                ),
                PdfCourseSentence(
                    english = "Don't be afraid of speaking in front of others.",
                    tamilPronunciation = "டோன்ட் பி அஃப்ரைட் ஆஃப் ஸ்பீக்கிங் இன் ஃபிரன்ட் ஆஃப் அதர்ஸ்.",
                    tamilMeaning = "மற்றவர்கள் முன்னால் பேசுவதற்குப் பயப்படாதே."
                ),
                PdfCourseSentence(
                    english = "Let me try to solve this puzzle myself.",
                    tamilPronunciation = "லெட் மீ டிரை டு சால்வ் திஸ் பஸில் மைசெல்ஃப்.",
                    tamilMeaning = "நானே இந்தப் புதிரைத் தீர்க்க முயற்சி செய்கிறேன்."
                ),
                PdfCourseSentence(
                    english = "Let's revise all the lessons before dinner.",
                    tamilPronunciation = "லெட்ஸ் ரிவைஸ் ஆல் தி லெசன்ஸ் பிஃபோர் டின்னர்.",
                    tamilMeaning = "இரவு உணவுக்கு முன்பு நாம் எல்லாப் பாடங்களையும் திருப்புதல் செய்வோம்."
                )
            ),
            teacherTipTamil = "தனம் டீச்சர் குறிப்பு: 'Let me see' (நான் பார்க்கிறேன்), 'Let me think' (நான் யோசிக்கிறேன்), 'Let me tell you' (நான் உனக்குச் சொல்கிறேன்) ஆகிய மூன்றும் பேச்சில் மிக அதிகம் பயன்படுபவை!"
        ),

        // PAGE 14: UNIT 10 (PART A) - CONVERSATIONS AT HOME & SCHOOL
        PdfCoursePage(
            pageNumber = 14,
            unitTag = "UNIT 10 (PART A) • வீடு & பள்ளி உரையாடல்",
            dayRange = "Day 26",
            titleEnglish = "Daily Spoken English at Home & School (Subiksha & Dhanam Teacher)",
            titleTamil = "வீட்டிலும் பள்ளியிலும் தினமும் பேசும் ஆங்கில உரையாடல்",
            introExplanationTamil = "காலையில் எழுந்தது முதல் இரவு தூங்கும் வரை வீட்டில் அம்மா-அப்பாவுடனும், பள்ளியில் தனம் டீச்சர் மற்றும் தோழிகளுடனும் சுபிக்சா (சுபி) பேச வேண்டிய மிக முக்கியமான வாக்கியங்கள் இங்கே கொடுக்கப்பட்டுள்ளன.",
            formulaBoxTitle = "🏠 வீட்டிலும் 🏫 பள்ளியிலும் உடனடியாகப் பேசும் பழக்கம்",
            formulas = listOf(
                "காலையில்: Good morning Amma! Did you sleep well? (காலை வணக்கம் அம்மா! நன்றாகத் தூங்கினீர்களா?)",
                "பள்ளியில்: Good morning Dhanam Teacher! May I come in? (காலை வணக்கம் தனம் டீச்சர்! நான் உள்ளே வரலாமா?)",
                "சந்தேகம் கேட்க: Excuse me Teacher, could you please explain this again? (இதை மீண்டும் விளக்க முடியுமா?)"
            ),
            tableHeaders = listOf("சூழல் (Context)", "English Sentence", "தமிழ் உச்சரிப்பு", "தமிழ் அர்த்தம்"),
            tableRows = listOf(
                PdfTableRow("காலை", "Mom, what is for breakfast?", "மாம், வாட் இஸ் ஃபார் பிரேக்ஃபாஸ்ட்?", "அம்மா, காலை உணவு என்ன?"),
                PdfTableRow("கிளம்பும்போது", "My school van has arrived.", "மை ஸ்கூல் வேன் ஹேஸ் அரைவ்ட்.", "என் பள்ளி வேன் வந்துவிட்டது."),
                PdfTableRow("வகுப்பறையில்", "I have completed my homework.", "ஐ ஹேவ் கம்ப்ளீட்டட் மை ஹோம்வொர்க்.", "நான் வீட்டுப்பாடத்தை முடித்துவிட்டேன்."),
                PdfTableRow("தோழியுடன்", "Can I borrow your ruler?", "கேன் ஐ பாரோ யுவர் ரூலர்?", "உன் ஸ்கேலை இரவல் வாங்கலாமா?"),
                PdfTableRow("மாலை", "I am back from school, Mom!", "ஐ அம் பேக் ஃபிரம் ஸ்கூல், மாம்!", "அம்மா, நான் பள்ளியிலிருந்து வந்துவிட்டேன்!"),
                PdfTableRow("இரவு", "Good night! Sweet dreams.", "குட் நைட்! ஸ்வீட் டிரீம்ஸ்.", "இனிய இரவு வணக்கம்!")
            ),
            sentencesTitle = "🗣️ தனம் டீச்சர் & சுபிக்சா வகுப்பறை உரையாடல் (Classroom Dialogue)",
            sentences = listOf(
                PdfCourseSentence(
                    english = "Dhanam Teacher: Good morning, Subiksha! How are you today?",
                    tamilPronunciation = "தனம் டீச்சர்: குட் மார்னிங், சுபிக்சா! ஹவ் ஆர் யூ டுடே?",
                    tamilMeaning = "தனம் டீச்சர்: காலை வணக்கம் சுபிக்சா! இன்று நீ எப்படி இருக்கிறாய்?"
                ),
                PdfCourseSentence(
                    english = "Subiksha (Subi): Good morning, Dhanam Teacher! I am doing great, thank you.",
                    tamilPronunciation = "சுபிக்சா: குட் மார்னிங், தனம் டீச்சர்! ஐ அம் டூயிங் கிரேட், தேங்க் யூ.",
                    tamilMeaning = "சுபிக்சா: காலை வணக்கம் தனம் டீச்சர்! நான் மிகவும் நன்றாக இருக்கிறேன், நன்றி."
                ),
                PdfCourseSentence(
                    english = "Dhanam Teacher: Did you practice yesterday's spoken English sentences?",
                    tamilPronunciation = "தனம் டீச்சர்: டிட் யூ பிராக்டிஸ் எஸ்டர்டேஸ் ஸ்போக்கன் இங்கிலீஷ் சென்டென்சஸ்?",
                    tamilMeaning = "தனம் டீச்சர்: நேற்றைய ஸ்போக்கன் இங்கிலீஷ் வாக்கியங்களைப் பயிற்சி செய்தாயா?"
                ),
                PdfCourseSentence(
                    english = "Subiksha (Subi): Yes Teacher! I practiced them aloud with my mother.",
                    tamilPronunciation = "சுபிக்சா: யெஸ் டீச்சர்! ஐ பிராக்டிஸ்ட் தெம் அலௌட் வித் மை மதர்.",
                    tamilMeaning = "சுபிக்சா: ஆமாம் டீச்சர்! நான் என் அம்மாவுடன் சத்தமாகப் பேசிப் பயிற்சி செய்தேன்."
                ),
                PdfCourseSentence(
                    english = "Dhanam Teacher: Wonderful job, Subi! Keep up the great effort!",
                    tamilPronunciation = "தனம் டீச்சர்: வொண்டர்ஃபுல் ஜாப், சுபி! கீப் அப் தி கிரேட் எஃபர்ட்!",
                    tamilMeaning = "தனம் டீச்சர்: மிகச் சிறப்பு சுபி! உன் சிறந்த முயற்சியைத் தொடர்ந்து செய்!"
                )
            ),
            teacherTipTamil = "தனம் டீச்சர் குறிப்பு: தினமும் வீட்டில் அம்மா, அப்பாவிடம் குறைந்தது 5 வாக்கியங்களாவது ஆங்கிலத்தில் பேசிப் பழகுங்கள்!"
        ),

        // PAGE 15: UNIT 10 (PART B) - SHOPPING, TRAVEL, PHONE & GUESTS
        PdfCoursePage(
            pageNumber = 15,
            unitTag = "UNIT 10 (PART B) • வெளி இடங்களில் உரையாடல்",
            dayRange = "Day 27",
            titleEnglish = "Real-Life English: Shopping, Travel, Phone Calls & Guests",
            titleTamil = "கடை, பயணம், தொலைபேசி மற்றும் விருந்தினரிடம் பேசும் ஆங்கிலம்",
            introExplanationTamil = "நாம் கடைக்குச் செல்லும்போதும், பயணம் செய்யும்போதும், தொலைபேசியில் பேசும்போதும், வீட்டுக்கு விருந்தினர்கள் வரும்போதும் பயன்படுத்த வேண்டிய இயல்பான ஆங்கில வாக்கியங்கள்.",
            formulaBoxTitle = "📞 தொலைபேசி & 🛍️ கடைகளில் பேசும் முக்கியத் தொடர்கள்",
            formulas = listOf(
                "Phone: Hello, may I speak to...? / Please hold on a moment. / I will call you back later.",
                "Shopping: Do you have...? / How much is this? / Please pack this for me.",
                "Guests: Welcome! Please come in and have a seat. / What would you like to have — tea or coffee?"
            ),
            tableHeaders = listOf("இடம்", "English Sentence", "தமிழ் உச்சரிப்பு", "தமிழ் அர்த்தம்"),
            tableRows = listOf(
                PdfTableRow("விருந்தினர்", "Please come in and sit down.", "ப்ளீஸ் கம் இன் அண்ட் சிட் டவுன்.", "உள்ளே வாருங்கள், உட்காருங்கள்."),
                PdfTableRow("கடையில்", "How much does this notebook cost?", "ஹவ் மச் டஸ் திஸ் நோட்புக் காஸ்ட்?", "இந்த நோட்புக் விலை எவ்வளவு?"),
                PdfTableRow("கடையில்", "Do you have change for 100 rupees?", "டூ யூ ஹேவ் சேஞ்ச் ஃபார் 100 ருபீஸ்?", "100 ரூபாய்க்கு சில்லறை உள்ளதா?"),
                PdfTableRow("பயணத்தில்", "Which bus goes to Anna Nagar?", "விச் பஸ் கோஸ் டு அண்ணா நகர்?", "அண்ணா நகருக்கு எந்தப் பேருந்து செல்லும்?"),
                PdfTableRow("போனில்", "Your voice is breaking up.", "யுவர் வாய்ஸ் இஸ் பிரேக்கிங் அப்.", "உங்கள் குரல் விட்டு விட்டுக் கேட்கிறது."),
                PdfTableRow("மருத்துவர்", "I have had a cold since yesterday.", "ஐ ஹேவ் ஹேட் எ கோல்ட் சின்ஸ் எஸ்டர்டே.", "நேற்றிலிருந்து எனக்குச் சளி பிடித்துள்ளது.")
            ),
            sentencesTitle = "🗣️ நடைமுறை உரையாடல் வாக்கியங்கள் (Practical Sentences)",
            sentences = listOf(
                PdfCourseSentence(
                    english = "Excuse me, could you tell me the way to the library?",
                    tamilPronunciation = "எக்ஸ்கியூஸ் மீ, குட் யூ டெல் மீ தி வே டு தி லைப்ரரி?",
                    tamilMeaning = "மன்னிக்கவும், நூலகத்திற்குச் செல்லும் வழியை எனக்குச் சொல்ல முடியுமா?"
                ),
                PdfCourseSentence(
                    english = "Go straight and turn left at the traffic signal.",
                    tamilPronunciation = "கோ ஸ்ட்ரெய்ட் அண்ட் டர்ன் லெஃப்ட் அட் தி டிராஃபிக் சிக்னல்.",
                    tamilMeaning = "நேராகச் சென்று டிராஃபிக் சிக்னலில் இடது பக்கம் திரும்புங்கள்."
                ),
                PdfCourseSentence(
                    english = "Could you please show me another color in this dress?",
                    tamilPronunciation = "குட் யூ ப்ளீஸ் ஷோ மீ அனதர் கலர் இன் திஸ் டிரஸ்?",
                    tamilMeaning = "இந்த உடையில் வேறு நிறம் இருந்தால் தயவுசெய்து காட்ட முடியுமா?"
                ),
                PdfCourseSentence(
                    english = "Mom is busy right now. Can I take a message?",
                    tamilPronunciation = "மாம் இஸ் பிஸி ரைட் நவ். கேன் ஐ டேக் எ மெசேஜ்?",
                    tamilMeaning = "அம்மா இப்போது வேலையாக இருக்கிறார். நான் ஏதேனும் தகவல் சொல்லட்டுமா?"
                ),
                PdfCourseSentence(
                    english = "Thank you so much for visiting our home! See you again.",
                    tamilPronunciation = "தேங்க் யூ சோ மச் ஃபார் விசிட்டிங் அவர் ஹோம்! சீ யூ அகைன்.",
                    tamilMeaning = "எங்கள் வீட்டிற்கு வந்ததற்கு மிக்க நன்றி! மீண்டும் சந்திப்போம்."
                )
            ),
            teacherTipTamil = "தனம் டீச்சர் குறிப்பு: தெரியாதவர்களிடம் பேச்சைத் தொடங்கும்போது எப்போதும் 'Excuse me' என்று கூறித் தொடங்குவது சிறந்த நாகரிகம்!"
        ),

        // PAGE 16: UNIT 10 (PART C) - 15 COMMON MISTAKES BY TAMIL SPEAKERS
        PdfCoursePage(
            pageNumber = 16,
            unitTag = "UNIT 10 (PART C) • தவறுகளும் திருத்தங்களும்",
            dayRange = "Day 28",
            titleEnglish = "15 Common English Mistakes by Tamil Speakers & Corrections",
            titleTamil = "தமிழர்கள் ஆங்கிலம் பேசும்போது செய்யும் 15 பொதுவான தவறுகளும் திருத்தங்களும்",
            introExplanationTamil = "நாம் தமிழில் சிந்திப்பதை அப்படியே வார்த்தைக்கு வார்த்தை ஆங்கிலத்தில் மொழிபெயர்க்கும்போது சில பொதுவான தவறுகள் ஏற்படுகின்றன. அவற்றைத் தவிர்த்துச் சரியான ஆங்கிலத்தில் பேச இந்த அட்டவணை உதவும்.",
            formulaBoxTitle = "⚠️ நினைவில் கொள்ள வேண்டிய முக்கிய விதிகள்",
            formulas = listOf(
                "1. 'Myself Subiksha' என்று சுய அறிமுகம் செய்யக்கூடாது -> 'I am Subiksha' அல்லது 'My name is Subiksha' என்பதே சரி.",
                "2. 'Return back', 'Repeat again', 'Cousin brother/sister' ஆகியவற்றில் இரண்டாவது வார்த்தை தேவையற்றது!",
                "3. 'Didn't' வந்தால் அடுத்த வினைச்சொல் எப்போதும் V1 ஆகத்தான் இருக்க வேண்டும் (didn't go, didn't eat)."
            ),
            tableHeaders = listOf("❌ தவறான வாக்கியம் (Wrong)", "✅ சரியான வாக்கியம் (Correct)", "தமிழ் விளக்கம் (Why)"),
            tableRows = listOf(
                PdfTableRow("Myself Subiksha.", "I am Subiksha.", "சுய அறிமுகத்திற்கு 'I am' பயன்படுத்தவும்."),
                PdfTableRow("I didn't went there.", "I didn't go there.", "didn't-க்குப் பின் V1 (go) மட்டுமே வரும்."),
                PdfTableRow("Does she has a book?", "Does she have a book?", "Does வந்தால் 'have' மட்டுமே வரும்."),
                PdfTableRow("She is my cousin sister.", "She is my cousin.", "Cousin என்றாலே சகோதரன்/சகோதரிதான்."),
                PdfTableRow("Please return back my pen.", "Please return my pen.", "Return என்றாலே திருப்பிக் கொடுப்பதுதான்."),
                PdfTableRow("Please repeat it again.", "Please repeat it.", "Repeat என்றாலே மீண்டும் சொல்வதுதான்."),
                PdfTableRow("One of my friend came.", "One of my friends came.", "'One of' வந்தால் பன்மை (friends) வர வேண்டும்."),
                PdfTableRow("I am having two brothers.", "I have two brothers.", "உறவுகள்/உடைமைக்கு 'have' பயன்படுத்தவும்."),
                PdfTableRow("Discuss about the topic.", "Discuss the topic.", "Discuss-க்குப் பிறகு 'about' வராது."),
                PdfTableRow("I prefer coffee than tea.", "I prefer coffee to tea.", "Prefer உடன் எப்போதும் 'to' வரும்.")
            ),
            sentencesTitle = "🗣️ சரியான வாக்கியங்களை உரக்கப் பழகுவோம் (Speak Correctly)",
            sentences = listOf(
                PdfCourseSentence(
                    english = "One of my friends lives in Madurai.",
                    tamilPronunciation = "ஒன் ஆஃப் மை ஃபிரண்ட்ஸ் லிவ்ஸ் இன் மதுரை.",
                    tamilMeaning = "என் நண்பர்களில் ஒருவர் மதுரையில் வசிக்கிறார்."
                ),
                PdfCourseSentence(
                    english = "I prefer reading story books to watching TV.",
                    tamilPronunciation = "ஐ பிரிஃபர் ரீடிங் ஸ்டோரி புக்ஸ் டு வாட்சிங் டிவி.",
                    tamilMeaning = "டிவி பார்ப்பதை விட கதைப் புத்தகங்கள் படிப்பதையே நான் விரும்புகிறேன்."
                ),
                PdfCourseSentence(
                    english = "According to me ❌ -> In my opinion, this is a great book! ✅",
                    tamilPronunciation = "இன் மை ஒப்பீனியன், திஸ் இஸ் எ கிரேட் புக்!",
                    tamilMeaning = "என் கருத்தின்படி, இது ஒரு சிறந்த புத்தகம்!"
                ),
                PdfCourseSentence(
                    english = "We enjoyed the school trip very much.",
                    tamilPronunciation = "வீ என்ஜாய்ட் தி ஸ்கூல் டிரிப் வெரி மச்.",
                    tamilMeaning = "நாங்கள் பள்ளிச் சுற்றுலாவை மிகவும் மகிழ்ச்சியாகக் கொண்டாடினோம்."
                )
            ),
            teacherTipTamil = "தனம் டீச்சர் குறிப்பு: மேலே உள்ள '✅ சரியான வாக்கியம்' பகுதியை மட்டும் 3 முறை சத்தமாக வாசித்து மனதில் பதிய வைத்துக் கொள்ளுங்கள்!"
        ),

        // PAGE 17: UNIT 10 (PART D) - 30 ESSENTIAL DAILY VERBS (V1, V2, V3)
        PdfCoursePage(
            pageNumber = 17,
            unitTag = "UNIT 10 (PART D) • முக்கிய வினைச்சொற்கள்",
            dayRange = "Day 29",
            titleEnglish = "Essential Daily Action Verbs (V1 - V2 - V3) with Tamil Meaning",
            titleTamil = "தினமும் ஆங்கிலம் பேசத் தேவையான முக்கிய வினைச்சொற்கள் (V1 - V2 - V3)",
            introExplanationTamil = "காலங்களை (Tenses) மாற்றிப் பேசுவதற்கு வினைச்சொல்லின் மூன்று வடிவங்கள் (V1 - நிகழ்காலம், V2 - கடந்த காலம், V3 - முற்றுப்பெற்ற காலம்) தெரிந்திருக்க வேண்டும். அன்றாடம் அதிகம் பயன்படும் முக்கிய வினைச்சொற்கள் இங்கே கொடுக்கப்பட்டுள்ளன.",
            formulaBoxTitle = "📐 எங்கு எதைப் பயன்படுத்துவது?",
            formulas = listOf(
                "V1 (Base Form): Simple Present (I go), Future (I will go), Modals (I can go), Didn't (I didn't go).",
                "V2 (Past Form): Simple Past Positive வாக்கியங்களில் மட்டும் (I went, She ate, We saw).",
                "V3 (Past Participle): Have / Has / Had வரும் முற்றுப்பெற்ற காலங்களில் (I have gone, She has eaten)."
            ),
            tableHeaders = listOf("V1 (Present)", "V2 (Past)", "V3 (Perfect)", "தமிழ் அர்த்தம் & உச்சரிப்பு"),
            tableRows = listOf(
                PdfTableRow("go", "went", "gone", "போ (கோ - வென்ட் - கான்)"),
                PdfTableRow("come", "came", "come", "வா (கம் - கேம் - கம்)"),
                PdfTableRow("eat", "ate", "eaten", "சாப்பிடு (ஈட் - ஏட் - ஈட்டன்)"),
                PdfTableRow("drink", "drank", "drunk", "குடி (டிரிங்க் - டிராங்க் - டிரங்க்)"),
                PdfTableRow("speak", "spoke", "spoken", "பேசு (ஸ்பீக் - ஸ்போக் - ஸ்போக்கன்)"),
                PdfTableRow("read", "read (ரெட்)", "read (ரெட்)", "படி (ரீட் - ரெட் - ரெட்)"),
                PdfTableRow("write", "wrote", "written", "எழுது (ரைட் - ரோட் - ரிட்டன்)"),
                PdfTableRow("see", "saw", "seen", "பார் (சீ - ஸா - சீன்)"),
                PdfTableRow("give", "gave", "given", "கொடு (கிவ் - கேவ் - கிவ்வன்)"),
                PdfTableRow("take", "took", "taken", "எடு (டேக் - டுக் - டேக்கன்)"),
                PdfTableRow("know", "knew", "known", "தெரிந்துகொள் (நோ - நியூ - நோன்)"),
                PdfTableRow("think", "thought", "thought", "நினை / யோசி (திங்க் - தாட் - தாட்)"),
                PdfTableRow("buy", "bought", "bought", "வாங்கு (பை - பாட் - பாட்)"),
                PdfTableRow("bring", "brought", "brought", "கொண்டு வா (பிரிங் - பிராட் - பிராட்)"),
                PdfTableRow("teach", "taught", "taught", "கற்றுக்கொடு (டீச் - டாட் - டாட்)"),
                PdfTableRow("learn", "learnt / learned", "learnt / learned", "கற்றுக்கொள் (லேர்ன் - லேர்ன்ட்)"),
                PdfTableRow("sleep", "slept", "slept", "தூங்கு (ஸ்லீப் - ஸ்லெப்ட் - ஸ்லெப்ட்)"),
                PdfTableRow("keep", "kept", "kept", "வை (கீப் - கெப்ட் - கெப்ட்)"),
                PdfTableRow("tell", "told", "told", "சொல் (டெல் - டோல்ட் - டோல்ட்)"),
                PdfTableRow("make", "made", "made", "உருவாக்கு (மேக் - மேட் - மேட்)")
            ),
            sentencesTitle = "🗣️ V1 - V2 - V3 ஒப்பீட்டு வாக்கியங்கள்",
            sentences = listOf(
                PdfCourseSentence(
                    english = "I write neatly every day (V1). Yesterday I wrote a story (V2). I have written three pages (V3).",
                    tamilPronunciation = "ஐ ரைட் நீட்லி எவ்ரி டே. எஸ்டர்டே ஐ ரோட் எ ஸ்டோரி. ஐ ஹேவ் ரிட்டன் த்ரீ பேஜஸ்.",
                    tamilMeaning = "நான் தினமும் அழகாக எழுதுகிறேன். நேற்று ஒரு கதை எழுதினேன். நான் மூன்று பக்கங்கள் எழுதிவிட்டேன்."
                ),
                PdfCourseSentence(
                    english = "Dhanam Teacher teaches well (V1). She taught us tenses yesterday (V2). She has taught 10 units (V3).",
                    tamilPronunciation = "தனம் டீச்சர் டீச்சஸ் வெல். ஷி டாட் அஸ் டென்சஸ் எஸ்டர்டே. ஷி ஹேஸ் டாட் டென் யூனிட்ஸ்.",
                    tamilMeaning = "தனம் டீச்சர் நன்றாகக் கற்பிக்கிறார். நேற்று எங்களுக்குக் காலங்களைக் கற்பித்தார். அவர் 10 யூனிட்கள் கற்பித்துவிட்டார்."
                )
            ),
            teacherTipTamil = "தனம் டீச்சர் குறிப்பு: 'read' (படி) என்ற வார்த்தைக்கு மூன்று வடிவங்களிலும் எழுத்து (r-e-a-d) மாறாது; ஆனால் உச்சரிப்பு மட்டும் 'ரீட் - ரெட் - ரெட்' என மாறும்!"
        ),

        // PAGE 18: UNIT 10 (PART E) - SUBIKSHA'S SPECIAL CORNER & GRADUATION
        PdfCoursePage(
            pageNumber = 18,
            unitTag = "UNIT 10 (PART E) • சுபியின் மேடைப் பேச்சு & சான்றிதழ்",
            dayRange = "Day 30",
            titleEnglish = "Subiksha's (Subi, Age 9) Stage Speech, Self-Intro & Fluency Routine",
            titleTamil = "சுபிக்சாவின் (சுபி, 9 வயது) சுய அறிமுகம், மேடைப் பேச்சு & வெற்றிப் பட்டியல்",
            introExplanationTamil = "வாழ்த்துகள் சுபிக்சா (சுபி)! 30 நாட்கள் கொண்ட இந்த 'Spoken English via Tamil' பாடப்புத்தகத்தின் நிறைவுப் பகுதிக்கு வந்துவிட்டாய். பள்ளியிலும் மேடையிலும் தன்னம்பிக்கையோடு பேசுவதற்கான உன்னுடைய சிறப்பு உரை இதோ!",
            formulaBoxTitle = "🏅 சுபியின் தினசரி 15-நிமிட ஆங்கிலப் பழக்கம் (Daily 15-Min Routine)",
            formulas = listOf(
                "🌅 காலை 5 நிமிடம்: கண்ணாடி முன் நின்று அன்றைய திட்டங்களை 5 ஆங்கில வாக்கியங்களில் சொல்வது.",
                "🏫 மதியம் 5 நிமிடம்: பள்ளியில் அல்லது வீட்டில் 5 புதிய ஆங்கில வாக்கியங்களைப் பயன்படுத்திப் பேசுவது.",
                "🌙 இரவு 5 நிமிடம்: இன்று நடந்த நிகழ்வுகளை தனம் டீச்சரிடம் (Voice Bot / Call) ஆங்கிலத்தில் பகிர்வது!",
                "🎓 சான்றிதழ்: மாணவி சுபிக்சா (சுபி, 9 வயது) 10 யூனிட்களையும் வெற்றிகரமாகப் பயின்றுள்ளார் — வாழ்த்துகளுடன் தனம் டீச்சர்!"
            ),
            tableHeaders = listOf("பகுதி (Section)", "Topic", "Key Expression", "தமிழ் அர்த்தம்"),
            tableRows = listOf(
                PdfTableRow("1. Greeting", "வணக்கம்", "Good morning everyone!", "அனைவருக்கும் காலை வணக்கம்!"),
                PdfTableRow("2. Name & Age", "பெயர் & வயது", "I am Subiksha (Subi), 9 years old.", "நான் சுபிக்சா (சுபி), 9 வயது."),
                PdfTableRow("3. Teacher", "ஆசிரியர்", "Dhanam Teacher guides me daily.", "தனம் டீச்சர் தினமும் வழிகாட்டுகிறார்."),
                PdfTableRow("4. Hobby", "விருப்பம்", "I love reading stories and speaking English.", "கதைகள் படிக்கவும் ஆங்கிலம் பேசவும் பிடிக்கும்."),
                PdfTableRow("5. Closing", "நன்றி", "Thank you all and have a great day!", "அனைவருக்கும் நன்றி, இனிய நாளாகட்டும்!")
            ),
            sentencesTitle = "🎤 சுபிக்சாவின் முழு மேடை உரை (Subiksha's Complete Speech)",
            sentences = listOf(
                PdfCourseSentence(
                    english = "Good morning respected teachers and my dear friends!",
                    tamilPronunciation = "குட் மார்னிங் ரெஸ்பெக்டட் டீச்சர்ஸ் அண்ட் மை டியர் ஃபிரண்ட்ஸ்!",
                    tamilMeaning = "மதிப்பிற்குரிய ஆசிரியர்களுக்கும் என் அன்பு நண்பர்களுக்கும் காலை வணக்கம்!"
                ),
                PdfCourseSentence(
                    english = "My name is Subiksha, and everyone lovingly calls me Subi.",
                    tamilPronunciation = "மை நேம் இஸ் சுபிக்சா, அண்ட் எவ்ரிஒன் லவ்விங்லி கால்ஸ் மீ சுபி.",
                    tamilMeaning = "என் பெயர் சுபிக்சா, எல்லோரும் என்னை அன்போடு சுபி என்று அழைப்பார்கள்."
                ),
                PdfCourseSentence(
                    english = "I am a 9-year-old girl, and I love learning new things every day.",
                    tamilPronunciation = "ஐ அம் எ நைன் இயர் ஓல்ட் கேர்ள், அண்ட் ஐ லவ் லேர்னிங் நியூ திங்ஸ் எவ்ரி டே.",
                    tamilMeaning = "நான் 9 வயது சிறுமி, தினமும் புதிய விஷயங்களைக் கற்றுக்கொள்வது எனக்கு மிகவும் பிடிக்கும்."
                ),
                PdfCourseSentence(
                    english = "My favorite teacher is Dhanam Teacher because she teaches me Spoken English so kindly.",
                    tamilPronunciation = "மை ஃபேவரைட் டீச்சர் இஸ் தனம் டீச்சர் பிகாஸ் ஷி டீச்சஸ் மீ ஸ்போக்கன் இங்கிலீஷ் சோ கைண்ட்லி.",
                    tamilMeaning = "எனக்குப் பிடித்த ஆசிரியர் தனம் டீச்சர், ஏனென்றால் அவர் எனக்கு மிகவும் அன்பாக ஸ்போக்கன் இங்கிலீஷ் கற்றுத் தருகிறார்."
                ),
                PdfCourseSentence(
                    english = "Now I can speak English confidently without any fear. Thank you very much!",
                    tamilPronunciation = "நவ் ஐ கேன் ஸ்பீக் இங்கிலீஷ் கான்ஃபிடன்ட்லி வித்தவுட் எனி ஃபியர். தேங்க் யூ வெரி மச்!",
                    tamilMeaning = "இப்போது என்னால் எந்தப் பயமும் இல்லாமல் தன்னம்பிக்கையுடன் ஆங்கிலம் பேச முடியும். மிக்க நன்றி!"
                )
            ),
            teacherTipTamil = "தனம் டீச்சர் வாழ்த்து: அருமை சுபி பாப்பா! இந்த 18 பக்கப் பாடப்புத்தகத்தை எப்போது வேண்டுமானாலும் PDF-ஆகப் படிக்கலாம், டவுன்லோட் செய்யலாம், ஆடியோவில் கேட்டு மகிழலாம்!"
        )
    )
}
