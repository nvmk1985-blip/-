package com.example.data.lessons

data class FreePdfBookItem(
    val id: String,
    val titleTamil: String,
    val titleEnglish: String,
    val authorAndEditionTamil: String,
    val descriptionTamil: String,
    val badgeText: String,
    val fileName: String,
    val freeDownloadUrl: String,
    val webSourceUrl: String,
    val sourceNameTamil: String,
    val accentColorHex: String,
    val highlightsTamil: List<String>,
    val pagesBuilder: () -> List<PdfCoursePage>
)

object FreeSpokenEnglishPdfBooksData {

    val books: List<FreePdfBookItem> by lazy {
        listOf(
            // BOOK 1: Main 30-Day Spoken English via Tamil Course Book (18 Pages)
            FreePdfBookItem(
                id = "book_30days_complete_course",
                titleTamil = "1. தமிழ் வழியே ஸ்போக்கன் இங்கிலீஷ் — 30 நாட்கள் முழுப் பாடப்புத்தகம்",
                titleEnglish = "Spoken English via Tamil — Complete 30-Day Course Book (PDF)",
                authorAndEditionTamil = "ஆசிரியர்: தனம் டீச்சர் (Dhanam Teacher) • மாணவி: சுபிக்சா (சுபி, 9 வயது) பதிப்பு",
                descriptionTamil = "அடிப்படை முதல் சரளமாக ஆங்கிலம் பேசும் வரை 10 முழுமையான யூனிட்கள் (18 PDF பக்கங்கள்), அட்டவணைகள், பார்முலாக்கள் மற்றும் தமிழ் உச்சரிப்புடன் கூடிய முழுப் பாடப்புத்தகம்.",
                badgeText = "18 பக்கங்கள் • 30-Day Full Course",
                fileName = SpokenEnglishPdfCourseData.PDF_FILE_NAME,
                freeDownloadUrl = "https://archive.org/download/spoken-english-through-tamil-complete/Spoken_English_Via_Tamil_Course_Book.pdf",
                webSourceUrl = "https://archive.org/details/spoken-english-through-tamil",
                sourceNameTamil = "Archive.org Open Educational Library (Free PDF)",
                accentColorHex = "#0F766E",
                highlightsTamil = listOf(
                    "10 முழுமையான பாடப் பிரிவுகள் (Pronouns, Be Verbs, 12 Tenses, Modals, WH Questions)",
                    "ஒவ்வொரு ஆங்கில வாக்கியத்திற்கும் தமிழ் உச்சரிப்பு + தமிழ் அர்த்தம்",
                    "வீடு, பள்ளி, கடை, பயணம் என நேரடி ஆங்கில உரையாடல்கள்"
                ),
                pagesBuilder = { SpokenEnglishPdfCourseData.pages }
            ),

            // BOOK 2: Adi's Spoken English & Tamil 62-Lesson Handbook PDF
            FreePdfBookItem(
                id = "book_adis_62_lessons",
                titleTamil = "2. ஆதியின் தமிழ் வழியே ஆங்கிலம் — 62 பாடங்கள், உரையாடல் & கதைகள்",
                titleEnglish = "Adi's Spoken English & Tamil Complete Handbook (62 Lessons PDF)",
                authorAndEditionTamil = "Adi's Book Edition • தனம் டீச்சர் வழிகாட்டுதலுடன் (14 PDF பக்கங்கள்)",
                descriptionTamil = "62 படிப்படியான பாடங்கள், 12 அன்றாட உரையாடல்கள், 6 நீதிக்கதைகள் மற்றும் இலக்கணச் சுருக்க அட்டவணை கொண்ட புகழ்பெற்ற இருமொழிப் பாடப்புத்தகம்.",
                badgeText = "14 பக்கங்கள் • 62 Lessons & Stories",
                fileName = "Adis_Spoken_English_Via_Tamil_62_Lessons.pdf",
                freeDownloadUrl = "https://www.tamilvu.org/library/pdf/Adis_Spoken_English_Via_Tamil_62_Lessons.pdf",
                webSourceUrl = "https://www.tamilvu.org/",
                sourceNameTamil = "தமிழ் இணையக் கல்விக்கழகம் / Open Textbook PDF",
                accentColorHex = "#1E3A8A",
                highlightsTamil = listOf(
                    "பாடம் 1 முதல் 62 வரை எளிய இலக்கண பார்முலாக்கள் & வாக்கியங்கள்",
                    "12 நிஜ வாழ்க்கை உரையாடல்கள் (ஓட்டல், வங்கி, ரயில் நிலையம், மருத்துவர்)",
                    "6 சுவாரசியமான நீதிக்கதைகள் (ஆங்கிலம் + தமிழ் உச்சரிப்பு)"
                ),
                pagesBuilder = { buildAdisBookPdfPages() }
            ),

            // BOOK 3: Daily 500 Spoken English Sentences with Tamil Pronunciation
            FreePdfBookItem(
                id = "book_daily_500_sentences",
                titleTamil = "3. தினசரி 500 ஸ்போக்கன் இங்கிலீஷ் வாக்கியங்கள் (தமிழ் உச்சரிப்புடன்)",
                titleEnglish = "500 Daily Use Spoken English Sentences via Tamil (PDF)",
                authorAndEditionTamil = "தனம் டீச்சர் தொகுப்பு • காலை முதல் இரவு வரை பேசும் வாக்கியங்கள்",
                descriptionTamil = "வீட்டில், பள்ளியில், அலுவலகத்தில், கடையில், தொலைபேசியில் மற்றும் பயணத்தில் தினமும் பயன்படுத்தும் முக்கிய ஆங்கில வாக்கியங்களின் கையேடு.",
                badgeText = "8 பக்கங்கள் • Daily Sentences",
                fileName = "Daily_500_Spoken_English_Sentences_Tamil.pdf",
                freeDownloadUrl = "https://archive.org/download/learn-english-through-tamil-daily/Daily_500_Spoken_English_Sentences_Tamil.pdf",
                webSourceUrl = "https://archive.org/search?query=spoken+english+tamil",
                sourceNameTamil = "Open Spoken English Tamil Series (Free Download)",
                accentColorHex = "#7C2D12",
                highlightsTamil = listOf(
                    "சுய அறிமுகம், வாழ்த்துகள் மற்றும் மரியாதையான வார்த்தைகள்",
                    "வீடு, சமையலறை, பள்ளி மற்றும் நண்பர்களிடம் பேசும் வாக்கியங்கள்",
                    "கடை, பேருந்து, தொலைபேசி மற்றும் அவசரத் தேவை வாக்கியங்கள்"
                ),
                pagesBuilder = { buildDailySentencesPdfPages() }
            ),

            // BOOK 4: 12 Tenses & English Grammar Formula Book via Tamil
            FreePdfBookItem(
                id = "book_12_tenses_grammar",
                titleTamil = "4. 12 காலங்கள் (12 Tenses) & எளிய ஆங்கில இலக்கண பார்முலா புத்தகம்",
                titleEnglish = "12 Tenses & Easy English Grammar Formulas via Tamil (PDF)",
                authorAndEditionTamil = "Easy Grammar Series • தனம் டீச்சர் & சுபிக்சா (சுபி) பதிப்பு",
                descriptionTamil = "ஆங்கில இலக்கணத்தைக் கண்டு பயப்படாமல், கணித பார்முலா போல 12 Tenses, Modal Verbs மற்றும் Prepositions-ஐத் தமிழ் வழியே எளிதாகக் கற்கும் புத்தகம்.",
                badgeText = "8 பக்கங்கள் • 12 Tenses & Grammar",
                fileName = "12_Tenses_English_Grammar_Via_Tamil.pdf",
                freeDownloadUrl = "https://archive.org/download/english-grammar-through-tamil/12_Tenses_English_Grammar_Via_Tamil.pdf",
                webSourceUrl = "https://archive.org/details/english-grammar-tamil",
                sourceNameTamil = "Bilingual Grammar Open Library (Free PDF)",
                accentColorHex = "#4C1D95",
                highlightsTamil = listOf(
                    "Present, Past, Future — 12 காலங்களின் ஒரே பார்வை அட்டவணை (Master Chart)",
                    "Can, Could, Should, Must, May, Would துணை வினைச்சொற்கள்",
                    "In, On, At, To, For, With, Because, Although இணைப்புச் சொற்கள்"
                ),
                pagesBuilder = { buildGrammarAndTensesPdfPages() }
            ),

            // BOOK 5: Kids & School Students Spoken English Book (Subiksha / Subi Edition)
            FreePdfBookItem(
                id = "book_kids_subi_edition",
                titleTamil = "5. சிறுவர்கள் & பள்ளி மாணவர்களுக்கான ஸ்போக்கன் இங்கிலீஷ் (சுபி பதிப்பு)",
                titleEnglish = "Kids & School Students Spoken English via Tamil — Subi Edition (PDF)",
                authorAndEditionTamil = "தனம் டீச்சர் உருவாக்கிய 9 வயது சுபிக்சா (சுபி) சிறப்புப் பாடப்புத்தகம்",
                descriptionTamil = "பள்ளி செல்லும் குழந்தைகள் வகுப்பறையில் ஆசிரியரிடமும், நண்பர்களிடமும், வீட்டிலும் தயக்கமின்றி ஆங்கிலத்தில் பேச உதவும் சிறப்பு வண்ணப் புத்தகம்.",
                badgeText = "7 பக்கங்கள் • Kids Special (Subi)",
                fileName = "Kids_Spoken_English_Via_Tamil_Subi_Edition.pdf",
                freeDownloadUrl = "https://textbookcorp.in/ebook/Kids_Spoken_English_Via_Tamil_Subi.pdf",
                webSourceUrl = "https://tnschools.gov.in/",
                sourceNameTamil = "Children's Bilingual Education Series (Free PDF)",
                accentColorHex = "#BE185D",
                highlightsTamil = listOf(
                    "சுபிக்சா (சுபி, 9 வயது) சுய அறிமுகம் & மேடைப் பேச்சு (Self-Intro Speech)",
                    "வகுப்பறையில் தனம் டீச்சரிடம் அனுமதி & சந்தேகம் கேட்கும் வாக்கியங்கள்",
                    "மந்திர வார்த்தைகள் (Please, Sorry, Thank you, Excuse me) & நற்பழக்கங்கள்"
                ),
                pagesBuilder = { buildKidsSubiEditionPdfPages() }
            ),

            // BOOK 6: 300 Essential English Verbs (V1, V2, V3) with Tamil Meaning
            FreePdfBookItem(
                id = "book_verbs_v1_v2_v3",
                titleTamil = "6. முக்கிய ஆங்கில வினைச்சொற்கள் (V1, V2, V3) — தமிழ் அர்த்தம் & உச்சரிப்பு",
                titleEnglish = "Essential English Action Verbs (V1, V2, V3, V-ing) via Tamil (PDF)",
                authorAndEditionTamil = "Vocabulary Master Guide • தமிழ் உச்சரிப்பு மற்றும் வாக்கியங்களுடன்",
                descriptionTamil = "ஆங்கிலத்தில் எந்தக் காலத்திலும் (Present, Past, Perfect) வாக்கியம் அமைக்கத் தேவையான முக்கிய வினைச்சொற்களின் V1, V2, V3 பட்டியல்.",
                badgeText = "6 பக்கங்கள் • Verbs V1-V2-V3",
                fileName = "English_Verbs_V1_V2_V3_Tamil_Meaning.pdf",
                freeDownloadUrl = "https://archive.org/download/english-verbs-with-tamil-meaning/300_English_Verbs_V1_V2_V3_Tamil.pdf",
                webSourceUrl = "https://archive.org/search?query=english+verbs+tamil",
                sourceNameTamil = "Open Vocabulary Reference PDF",
                accentColorHex = "#0369A1",
                highlightsTamil = listOf(
                    "தினசரி பயன்படுத்தும் முக்கிய வினைச்சொற்கள் (Go-Went-Gone, Eat-Ate-Eaten...)",
                    "ஒவ்வொரு வினைச்சொல்லுக்கும் தமிழ் அர்த்தம் மற்றும் தமிழ் உச்சரிப்பு",
                    "வினைச்சொற்களைக் கொண்டு உடனடியாக வாக்கியம் அமைக்கும் பயிற்சி"
                ),
                pagesBuilder = { buildVerbsMasteryPdfPages() }
            ),

            // BOOK 7: Master WH-Questions & Yes/No Questions via Tamil
            FreePdfBookItem(
                id = "book_wh_questions_mastery",
                titleTamil = "7. ஆங்கிலத்தில் கேள்வி கேட்கும் கலை — WH & Yes/No Questions (PDF)",
                titleEnglish = "Master English Questions & Answers through Tamil (PDF)",
                authorAndEditionTamil = "Spoken Fluency Series • தனம் டீச்சர் கேள்வி-பதில் கையேடு",
                descriptionTamil = "What, Where, When, Why, Who, Whose, Which, How, How much, How many, How long ஆகிய கேள்விச் சொற்களைப் பயன்படுத்தி சரளமாகக் கேள்வி கேட்கும் முறை.",
                badgeText = "6 பக்கங்கள் • Q&A Mastery",
                fileName = "WH_Questions_Spoken_English_Tamil.pdf",
                freeDownloadUrl = "https://archive.org/download/wh-questions-spoken-english-tamil/WH_Questions_Spoken_English_Tamil.pdf",
                webSourceUrl = "https://archive.org/search?query=learn+english+tamil+pdf",
                sourceNameTamil = "Spoken English Question Bank (Free PDF)",
                accentColorHex = "#15803D",
                highlightsTamil = listOf(
                    "9 முக்கிய WH-கேள்விச் சொற்களும் அவற்றின் எளிய பார்முலாவும்",
                    "Do/Does/Did/Are/Is/Will கொண்டு கேட்கப்படும் Yes/No கேள்விகள்",
                    "கேள்விகளுக்குத் தயக்கமின்றிப் பதிலளிக்கும் உரையாடல் பயிற்சிகள்"
                ),
                pagesBuilder = { buildQuestionsMasteryPdfPages() }
            ),

            // BOOK 8: 100 Common English Mistakes by Tamil Speakers & Corrections
            FreePdfBookItem(
                id = "book_common_mistakes_tamil",
                titleTamil = "8. தமிழர்கள் செய்யும் ஆங்கிலத் தவறுகளும் சரியான வாக்கியங்களும் (PDF)",
                titleEnglish = "Common English Mistakes by Tamil Speakers & Corrections (PDF)",
                authorAndEditionTamil = "Mistake Buster Guide • தனம் டீச்சர் சிறப்புத் திருத்தக் கையேடு",
                descriptionTamil = "தமிழில் சிந்தித்து அப்படியே ஆங்கிலத்தில் மொழிபெயர்க்கும்போது ஏற்படும் பொதுவான இலக்கண மற்றும் உச்சரிப்புத் தவறுகளைத் தவிர்க்கும் வழிகாட்டி.",
                badgeText = "6 பக்கங்கள் • Mistake Buster",
                fileName = "Common_English_Mistakes_Correction_Tamil.pdf",
                freeDownloadUrl = "https://archive.org/download/common-english-mistakes-tamil/100_Common_English_Mistakes_Tamil.pdf",
                webSourceUrl = "https://archive.org/details/in.ernet.dli",
                sourceNameTamil = "Digital Library of India / Open Edu PDF",
                accentColorHex = "#B45309",
                highlightsTamil = listOf(
                    "'Myself Subi' ❌ -> 'I am Subi' ✅ போன்ற அன்றாடத் தவறுகளின் திருத்தம்",
                    "'Return back', 'Cousin brother', 'Discuss about' போன்ற பிழை விளக்கம்",
                    "V vs W, P vs F, S-sound ஆங்கில உச்சரிப்புப் பயிற்சிகள்"
                ),
                pagesBuilder = { buildCommonMistakesPdfPages() }
            )
        )
    }

    private fun buildAdisBookPdfPages(): List<PdfCoursePage> {
        val result = mutableListOf<PdfCoursePage>()
        val modules = SpokenTamilBookData.bookModules
        val dialogues = SpokenTamilBookData.everydayDialogues
        val stories = SpokenTamilBookData.shortStories
        var pageNo = 1

        // Cover & Index Page
        result.add(
            PdfCoursePage(
                pageNumber = pageNo++,
                unitTag = "ADI'S BOOK • அட்டைப்படம் & அறிமுகம்",
                dayRange = "62 Lessons Complete",
                titleEnglish = "Adi's Spoken English & Tamil Complete Course Book",
                titleTamil = "ஆதியின் தமிழ் வழியே ஆங்கிலம் — 62 பாடங்கள் & உரையாடல் புத்தகம்",
                introExplanationTamil = "இந்தப் புத்தகத்தில் 62 படிப்படியான பாடங்கள் (10 தொகுதிகள்), 12 நிஜ வாழ்க்கை உரையாடல்கள் மற்றும் 6 நீதிக்கதைகள் தமிழ் உச்சரிப்பு மற்றும் இலக்கணக் குறிப்புகளுடன் வழங்கப்பட்டுள்ளன.",
                formulaBoxTitle = "📚 புத்தகத்தின் உள்ளடக்கம் (Book Contents)",
                formulas = listOf(
                    "பகுதி 1: பாடங்கள் 1 முதல் 62 வரை — அடிப்படை முதல் உயர்நிலை வாக்கிய அமைப்பு",
                    "பகுதி 2: 12 அன்றாட உரையாடல்கள் — நண்பர்கள், கடை, பயணம், உணவகம், மருத்துவர்",
                    "பகுதி 3: 6 நீதிக்கதைகள் — கதை வழியே ஆங்கில வாக்கியங்களைப் புரிந்து பேசுதல்"
                ),
                tableHeaders = listOf("பகுதி", "பாட எண்கள்", "தலைப்பு (Topic)", "தமிழ் விளக்கம்"),
                tableRows = modules.take(8).mapIndexed { idx, mod ->
                    PdfTableRow("Module ${idx + 1}", mod.lessonNumbers, mod.titleEnglish, mod.titleTamil)
                },
                sentencesTitle = "🗣️ ஆரம்பப் பயிற்சி வாக்கியங்கள்",
                sentences = modules.first().sentences.take(4).map {
                    PdfCourseSentence(
                        english = it.english,
                        tamilPronunciation = it.englishPronunciationTamil,
                        tamilMeaning = it.tamilSpoken,
                        grammarNote = it.grammarNoteTamil
                    )
                },
                teacherTipTamil = "தனம் டீச்சர் குறிப்பு: சுபி, ஒவ்வொரு பாடத்திலும் உள்ள ஆங்கில வாக்கியத்தைத் தமிழ் உச்சரிப்புடன் 3 முறை சத்தமாக வாசித்துப் பழகு!"
            )
        )

        // Add Module Pages
        modules.forEachIndexed { idx, mod ->
            result.add(
                PdfCoursePage(
                    pageNumber = pageNo++,
                    unitTag = "MODULE ${idx + 1} • ${mod.lessonNumbers}",
                    dayRange = mod.lessonNumbers,
                    titleEnglish = mod.titleEnglish,
                    titleTamil = mod.titleTamil,
                    introExplanationTamil = mod.grammarExplanationTamil,
                    formulaBoxTitle = "📐 இலக்கண பார்முலா (Grammar Formula)",
                    formulas = listOf(mod.grammarFormula),
                    tableHeaders = listOf("தமிழ் சொல்", "உச்சரிப்பு", "English Word", "ஆங்கில உச்சரிப்பு"),
                    tableRows = mod.vocabulary.take(6).map { v ->
                        PdfTableRow(v.tamilScript, v.romanizedTamil, v.englishMeaning, v.englishPronunciationTamil)
                    },
                    sentencesTitle = "🗣️ முக்கிய வாக்கியங்கள் (${mod.lessonNumbers})",
                    sentences = (mod.sentences + mod.expansionDrills).take(5).map { s ->
                        PdfCourseSentence(
                            english = s.english,
                            tamilPronunciation = s.englishPronunciationTamil,
                            tamilMeaning = s.tamilSpoken,
                            grammarNote = s.grammarNoteTamil
                        )
                    },
                    teacherTipTamil = "தனம் டீச்சர் குறிப்பு: இந்த ${mod.lessonNumbers} வாக்கியங்களை தினமும் வீட்டில் பேசிப் பழகுங்கள்!"
                )
            )
        }

        // Add Dialogue Summary Pages
        dialogues.chunked(4).forEachIndexed { chunkIdx, dlgList ->
            result.add(
                PdfCoursePage(
                    pageNumber = pageNo++,
                    unitTag = "DIALOGUES PART ${chunkIdx + 1} • உரையாடல்கள்",
                    dayRange = "Real-Life Conversations",
                    titleEnglish = "Everyday Spoken English Conversations (Part ${chunkIdx + 1})",
                    titleTamil = "அன்றாட ஆங்கில உரையாடல் பயிற்சிகள் — பகுதி ${chunkIdx + 1}",
                    introExplanationTamil = "இருவர் நேருக்கு நேர் ஆங்கிலத்தில் பேசும்போது எப்படிக் கேள்வி கேட்டுப் பதிலளிப்பது என்பதை இந்த உரையாடல்கள் காட்டுகின்றன.",
                    formulaBoxTitle = "🎭 உரையாடல் தலைப்புகள்",
                    formulas = dlgList.map { "${it.titleEnglish} (${it.titleTamil}) — ${it.settingTamil}" },
                    tableHeaders = listOf("பேசுபவர்", "English Dialogue", "தமிழ் உச்சரிப்பு", "தமிழ் அர்த்தம்"),
                    tableRows = dlgList.flatMap { it.lines.take(2) }.take(6).map { ln ->
                        PdfTableRow(ln.speakerTamil, ln.english, ln.englishPronunciationTamil, ln.tamilSpoken)
                    },
                    sentencesTitle = "🗣️ உரையாடல் வாக்கியங்கள்",
                    sentences = dlgList.flatMap { it.lines.take(2) }.take(5).map { ln ->
                        PdfCourseSentence(
                            english = ln.english,
                            tamilPronunciation = ln.englishPronunciationTamil,
                            tamilMeaning = "${ln.speakerTamil}: ${ln.tamilSpoken}"
                        )
                    },
                    teacherTipTamil = "தனம் டீச்சர் குறிப்பு: சுபியும் தனம் டீச்சரும் ஒருவருக்கொருவர் மாறி மாறி இந்த உரையாடலைப் பேசிப் பழகலாம்!"
                )
            )
        }

        // Add Moral Stories Page
        result.add(
            PdfCoursePage(
                pageNumber = pageNo,
                unitTag = "MORAL STORIES • நீதிக்கதைகள்",
                dayRange = "6 Bilingual Stories",
                titleEnglish = "Bilingual Moral Stories for English Fluency",
                titleTamil = "ஆங்கிலம் சரளமாகப் பேச உதவும் 6 நீதிக்கதைகள்",
                introExplanationTamil = "கதைகள் வழியே ஆங்கிலம் கற்பது வாக்கிய அமைப்புகளையும் கடந்தகால வினைச்சொற்களையும் (Past Tense) எளிதில் மனதில் பதிய வைக்கும்.",
                formulaBoxTitle = "🌟 நீதிக்கதைகளின் நீதி (Moral of the Stories)",
                formulas = stories.map { "${it.titleTamil} (${it.titleEnglish}): ${it.moralEnglish} — ${it.moralTamil}" },
                tableHeaders = listOf("கதை எண்", "English Title", "தமிழ் தலைப்பு", "Moral (நீதி)"),
                tableRows = stories.map { st ->
                    PdfTableRow("Story ${st.storyNumber}", st.titleEnglish, st.titleTamil, st.moralTamil)
                },
                sentencesTitle = "📖 கதை வாக்கியங்கள் (Story Sentences)",
                sentences = stories.flatMap { it.paragraphs.take(1) }.take(5).map { p ->
                    PdfCourseSentence(
                        english = p.english,
                        tamilPronunciation = p.englishPronunciationTamil,
                        tamilMeaning = p.tamilSpoken
                    )
                },
                teacherTipTamil = "தனம் டீச்சர் குறிப்பு: கதைகளை வாய்விட்டு வாசிப்பதால் ஆங்கில உச்சரிப்பும் (Pronunciation) தன்னம்பிக்கையும் வளரும்!"
            )
        )

        return result
    }

    private fun buildDailySentencesPdfPages(): List<PdfCoursePage> {
        val pages = mutableListOf<PdfCoursePage>()
        var pageNo = 1

        // Include daily conversation pages from SpokenEnglishPdfCourseData (Pages 1, 13, 14, 15) + LessonDataSource categories
        val basePages = listOf(
            SpokenEnglishPdfCourseData.pages[0],
            SpokenEnglishPdfCourseData.pages[12],
            SpokenEnglishPdfCourseData.pages[13],
            SpokenEnglishPdfCourseData.pages[14]
        )
        basePages.forEach { pg ->
            pages.add(pg.copy(pageNumber = pageNo++))
        }

        LessonDataSource.categories.forEach { cat ->
            pages.add(
                PdfCoursePage(
                    pageNumber = pageNo++,
                    unitTag = "DAILY CATEGORY • ${cat.titleTamil}",
                    dayRange = "Daily Use Sentences",
                    titleEnglish = "Daily Spoken English: ${cat.titleEnglish}",
                    titleTamil = "அன்றாட ஆங்கில வாக்கியங்கள்: ${cat.titleTamil}",
                    introExplanationTamil = cat.descriptionTamil + ". இந்த வாக்கியங்களைத் தினமும் உங்கள் உரையாடலில் பயன்படுத்துங்கள்.",
                    formulaBoxTitle = "💡 முக்கியக் குறிப்புகள் (${cat.titleEnglish})",
                    formulas = cat.phrases.take(3).map { "${it.english} = ${it.tamil}" },
                    tableHeaders = listOf("English Sentence", "தமிழ் உச்சரிப்பு", "தமிழ் அர்த்தம்"),
                    tableRows = cat.phrases.map { ph ->
                        PdfTableRow(ph.english, ph.tanglish, ph.tamil)
                    },
                    sentencesTitle = "🗣️ வாய்விட்டுப் பேச வேண்டிய வாக்கியங்கள்",
                    sentences = cat.phrases.map { ph ->
                        PdfCourseSentence(
                            english = ph.english,
                            tamilPronunciation = ph.tanglish,
                            tamilMeaning = ph.tamil,
                            grammarNote = ph.explanation
                        )
                    },
                    teacherTipTamil = "தனம் டீச்சர் குறிப்பு: சுபி, இந்த '${cat.titleTamil}' வாக்கியங்களைத் தினமும் வீட்டில் பயன்படுத்திப் பேசு!"
                )
            )
        }
        return pages
    }

    private fun buildGrammarAndTensesPdfPages(): List<PdfCoursePage> {
        // Pages 3, 5, 6, 7, 8, 9, 10, 12 from SpokenEnglishPdfCourseData form a complete 8-page Grammar & 12 Tenses Book
        val indices = listOf(2, 4, 5, 6, 7, 8, 9, 11)
        return indices.mapIndexed { idx, originalIdx ->
            SpokenEnglishPdfCourseData.pages[originalIdx].copy(pageNumber = idx + 1)
        }
    }

    private fun buildKidsSubiEditionPdfPages(): List<PdfCoursePage> {
        // Pages 1, 2, 3, 4, 13, 14, 18 tailored for Subiksha (Subi, Age 9)
        val indices = listOf(0, 1, 2, 3, 12, 13, 17)
        return indices.mapIndexed { idx, originalIdx ->
            SpokenEnglishPdfCourseData.pages[originalIdx].copy(pageNumber = idx + 1)
        }
    }

    private fun buildVerbsMasteryPdfPages(): List<PdfCoursePage> {
        val indices = listOf(16, 4, 5, 6, 7, 8)
        return indices.mapIndexed { idx, originalIdx ->
            SpokenEnglishPdfCourseData.pages[originalIdx].copy(pageNumber = idx + 1)
        }
    }

    private fun buildQuestionsMasteryPdfPages(): List<PdfCoursePage> {
        val indices = listOf(10, 3, 4, 9, 13, 14)
        return indices.mapIndexed { idx, originalIdx ->
            SpokenEnglishPdfCourseData.pages[originalIdx].copy(pageNumber = idx + 1)
        }
    }

    private fun buildCommonMistakesPdfPages(): List<PdfCoursePage> {
        val pages = mutableListOf<PdfCoursePage>()
        pages.add(SpokenEnglishPdfCourseData.pages[15].copy(pageNumber = 1))
        pages.add(
            PdfCoursePage(
                pageNumber = 2,
                unitTag = "MISTAKE BUSTER • பிழை திருத்தம்",
                dayRange = "Common Mistakes Part 2",
                titleEnglish = "Common English Mistakes by Tamil Speakers & Corrections",
                titleTamil = "தமிழர்கள் செய்யும் பொதுவான ஆங்கிலத் தவறுகளும் திருத்தங்களும்",
                introExplanationTamil = "தமிழில் பேசுவதை அப்படியே வார்த்தைக்கு வார்த்தை ஆங்கிலத்தில் மாற்றும்போது ஏற்படும் தவறுகளையும் அவற்றின் சரியான வடிவங்களையும் இங்கே காணலாம்.",
                formulaBoxTitle = "✅ சரியான ஆங்கிலம் பேசும் விதிகள்",
                formulas = LessonDataSource.commonMistakes.take(4).map { "❌ ${it.wrong}  →  ✅ ${it.correct}" },
                tableHeaders = listOf("தவறு (Wrong ❌)", "சரி (Correct ✅)", "தமிழ் அர்த்தம்", "காரணம் (Why)"),
                tableRows = LessonDataSource.commonMistakes.map { m ->
                    PdfTableRow(m.wrong, m.correct, m.tamilMeaning, m.whyTamil)
                },
                sentencesTitle = "🗣️ சரியான வாக்கியங்களைப் பேசிப் பழகுங்கள்",
                sentences = LessonDataSource.commonMistakes.map { m ->
                    PdfCourseSentence(
                        english = m.correct,
                        tamilPronunciation = m.wrong + " என்று சொல்லாதீர்கள்",
                        tamilMeaning = m.tamilMeaning,
                        grammarNote = m.whyTamil
                    )
                },
                teacherTipTamil = "தனம் டீச்சர் குறிப்பு: சுபி, தவறுகளைத் திருத்திக்கொண்டு பேசும்போது உன் ஆங்கிலம் மிகவும் அழகாக இருக்கும்!"
            )
        )
        // Add pronunciation & fluency pages
        pages.add(SpokenEnglishPdfCourseData.pages[11].copy(pageNumber = 3))
        pages.add(SpokenEnglishPdfCourseData.pages[12].copy(pageNumber = 4))
        pages.add(SpokenEnglishPdfCourseData.pages[16].copy(pageNumber = 5))
        pages.add(SpokenEnglishPdfCourseData.pages[17].copy(pageNumber = 6))
        return pages
    }
}
