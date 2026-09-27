package com.example.data.lessons

data class BookVocabItem(
    val tamilScript: String,
    val romanizedTamil: String,
    val englishMeaning: String,
    val englishPronunciationTamil: String
)

data class BookSentenceItem(
    val english: String,
    val tamilSpoken: String,
    val romanizedTamil: String,
    val englishPronunciationTamil: String,
    val grammarNoteTamil: String = ""
)

data class BookLessonModule(
    val id: String,
    val lessonNumbers: String,
    val titleEnglish: String,
    val titleTamil: String,
    val grammarExplanationTamil: String,
    val grammarFormula: String,
    val vocabulary: List<BookVocabItem>,
    val sentences: List<BookSentenceItem>,
    val expansionDrills: List<BookSentenceItem>
)

data class BookDialogueLine(
    val speaker: String,
    val speakerTamil: String,
    val english: String,
    val tamilSpoken: String,
    val romanizedTamil: String,
    val englishPronunciationTamil: String
)

data class BookDialogueLesson(
    val id: String,
    val lessonNumber: Int,
    val titleEnglish: String,
    val titleTamil: String,
    val settingTamil: String,
    val vocabulary: List<BookVocabItem>,
    val lines: List<BookDialogueLine>
)

data class BookStory(
    val id: String,
    val storyNumber: Int,
    val titleRomanized: String,
    val titleTamil: String,
    val titleEnglish: String,
    val moralTamil: String,
    val moralEnglish: String,
    val vocabulary: List<BookVocabItem>,
    val paragraphs: List<BookSentenceItem>
)

data class GrammarGlanceItem(
    val principle: String,
    val tamilSuffixOrRule: String,
    val lessonRef: String,
    val exampleTamil: String,
    val exampleEnglish: String,
    val englishPronunciationTamil: String
)

object SpokenTamilBookData {

    val bookModules: List<BookLessonModule> = listOf(
        BookLessonModule(
            id = "lessons_1_4",
            lessonNumbers = "Lessons 1 – 4",
            titleEnglish = "Greetings, Pronouns & 'To Be' (Implied)",
            titleTamil = "வணக்கம், சுட்டுப்பெயர்கள் & 'இரு' (Implied Verb)",
            grammarExplanationTamil = "தமிழில் 'Is / Am / Are' (To Be) வினைச்சொல் பெரும்பாலான வாக்கியங்களில் மறைந்திருக்கும் (Implied). கேள்வி கேட்க வார்த்தையின் கடைசியில் '-ஆ' (-ā) சேர்க்க வேண்டும். மரியாதைக்கு '-ங்க' (-nga) சேர்க்கவும்.",
            grammarFormula = "Noun + Noun (idhu pusthaham = This is a book) | Question: Noun + ā? (idhu pēnāvā? = Is this a pen?)",
            vocabulary = listOf(
                BookVocabItem("வணக்கம் / நமஸ்காரம்", "vaṇakkam / namaskāram", "Greetings / Good morning", "கிரீட்டிங்ஸ் / குட் மார்னிங்"),
                BookVocabItem("வாங்க / உக்காருங்க", "vānga / ukkārunga", "Please come / Please sit", "ப்ளீஸ் கம் / ப்ளீஸ் சிட்"),
                BookVocabItem("நான் / நீங்க / அவர்", "nān / nīnga / avar", "I / You (polite) / He or She (polite)", "ஐ / யூ / ஹி ஆர் ஷி"),
                BookVocabItem("இது / அது / எங்கே", "idhu / adhu / enge", "This / That / Where", "திஸ் / தட் / வேர்"),
                BookVocabItem("நாற்காலி / மேசை / புத்தகம்", "nākkāli / mēse / pusthaham", "Chair / Table / Book", "சேர் / டேபிள் / புக்"),
                BookVocabItem("சுகம் / போயிட்டு வரேன்", "suham / pōitu varēn", "In good health / Goodbye (by guest)", "இன் குட் ஹெல்த் / குட்பை")
            ),
            sentences = listOf(
                BookSentenceItem(
                    english = "Good morning sir. Please come in and sit down.",
                    tamilSpoken = "ஐயா வணக்கம். வாங்க, உக்காருங்க.",
                    romanizedTamil = "aiyā vaṇakkam. vānga, ukkārunga.",
                    englishPronunciationTamil = "குட் மார்னிங் சார். ப்ளீஸ் கம் இன் அண்ட் சிட் டவுன்.",
                    grammarNoteTamil = "மரியாதையாக அழைக்க வினையுடன் '-ங்க' (vānga, ukkārunga) சேர்க்கப்படுகிறது."
                ),
                BookSentenceItem(
                    english = "Madam, what is this? Sir, that is a chair.",
                    tamilSpoken = "அம்மா, இது என்ன? ஐயா, அது நாற்காலி.",
                    romanizedTamil = "ammā, idhu enna? aiyā, adhu nākkāli.",
                    englishPronunciationTamil = "மேடம், வாட் இஸ் திஸ்? சார், தட் இஸ் எ சேர்.",
                    grammarNoteTamil = "'என்ன' (what) என்ற கேள்விச்சொல் வரும்போது கடைசியில் '-ஆ' வராது."
                ),
                BookSentenceItem(
                    english = "Is this a pen? No, that is not a pen, that is indeed a pencil.",
                    tamilSpoken = "இது பேனாவா? இல்லே, அது பேனா இல்லே, அது பென்சில்தான்.",
                    romanizedTamil = "idhu pēnāvā? ille, adhu pēnā ille, adhu pencildhān.",
                    englishPronunciationTamil = "இஸ் திஸ் எ பென்? நோ, தட் இஸ் நாட் எ பென், தட் இஸ் இன்டீட் எ பென்சில்.",
                    grammarNoteTamil = "'-தான்' (dhān) என்பது 'indeed / only' என அழுத்தம் கொடுக்கப் பயன்படுகிறது."
                ),
                BookSentenceItem(
                    english = "Who is that gentleman? He is a doctor.",
                    tamilSpoken = "அந்த ஐயா யார்? அவர் டாக்டர்.",
                    romanizedTamil = "andha aiyā yār? avar doctor.",
                    englishPronunciationTamil = "ஹூ இஸ் தட் ஜென்டில்மேன்? ஹி இஸ் எ டாக்டர்."
                ),
                BookSentenceItem(
                    english = "Madam, are you well? Yes, I am well, thank you.",
                    tamilSpoken = "அம்மா, நீங்க சுகமா? ஆமா, நான் சுகம்தான், நன்றி.",
                    romanizedTamil = "ammā, nīnga suhamā? āmā, nān suhamdhān, nandri.",
                    englishPronunciationTamil = "மேடம், ஆர் யூ வெல்? யெஸ், ஐ அம் வெல், தேங்க் யூ."
                ),
                BookSentenceItem(
                    english = "Goodbye! (said by guest leaving / host replying)",
                    tamilSpoken = "நான் போயிட்டு வரேன், வணக்கம். / போயிட்டு வாங்க, வணக்கம்.",
                    romanizedTamil = "nān pōituvarēn, vaṇakkam. / pōituvānga, vaṇakkam.",
                    englishPronunciationTamil = "குட்பை! (ஐ வில் கோ அண்ட் கம் பேக்)"
                )
            ),
            expansionDrills = listOf(
                BookSentenceItem(
                    english = "This is not a chair, it is a table.",
                    tamilSpoken = "இது நாற்காலி இல்லே, மேசை.",
                    romanizedTamil = "idhu nākkāli ille, mēse.",
                    englishPronunciationTamil = "திஸ் இஸ் நாட் எ சேர், இட் இஸ் எ டேபிள்."
                ),
                BookSentenceItem(
                    english = "She is not a doctor, she is a teacher.",
                    tamilSpoken = "இவர் டாக்டர் இல்லே, இவர் வாத்தியார்.",
                    romanizedTamil = "ivar doctor ille, ivar vādhyār.",
                    englishPronunciationTamil = "ஷி இஸ் நாட் எ டாக்டர், ஷி இஸ் எ டீச்சர்."
                )
            )
        ),
        BookLessonModule(
            id = "lessons_5_8",
            lessonNumbers = "Lessons 5 – 8",
            titleEnglish = "Numbers 1–10, Imperatives, Weak & Strong Verbs, Plurals",
            titleTamil = "எண்கள் 1–10, கட்டளை வினைகள் (செய் / செய்யாதே) & பன்மை",
            grammarExplanationTamil = "கட்டளை வாக்கியங்களில் (Imperatives): மரியாதைக்கு '+ங்க' (படிங்க, வாங்க). எதிர்மறைக் கட்டளைக்கு (Do not): மென்வினைக்கு (Weak Verb) '+ஆதீங்க' (போகாதீங்க), வன்வினைக்கு (Strong Verb) '+க்காதீங்க' (குடிக்காதீங்க). பெயர்ச்சொல் பன்மைக்கு '+ங்க' (மேசைங்க, புத்தகங்க).",
            grammarFormula = "Polite: Verb + nga (padinga = please read) | Negative: Weak + ādhinga (pōhādhinga) / Strong + kkādhinga (kudikkādhinga)",
            vocabulary = listOf(
                BookVocabItem("ஒண்ணு, ரெண்டு, மூணு, நாலு, அஞ்சு", "oṇṇu, reṇdu, mūnu, nālu, anju", "One, Two, Three, Four, Five", "ஒன், டூ, த்ரீ, ஃபோர், ஃபைவ்"),
                BookVocabItem("ஆறு, ஏழு, எட்டு, ஒன்பது, பத்து", "āru, ēṛu, ettu, ombathu, paththu", "Six, Seven, Eight, Nine, Ten", "சிக்ஸ், செவன், எய்ட், நைன், டென்"),
                BookVocabItem("உள்ளே / வெளியே", "uḷḷe / veḷiye", "Inside / Outside", "இன்சைடு / அவுட்சைடு"),
                BookVocabItem("எவ்வளவு / எத்தனை", "evḷavu / eththane", "How much / How many", "ஹவ் மச் / ஹவ் மெனி"),
                BookVocabItem("சீக்கிரம் / மெதுவா / திரும்ப", "sīkram / medhuvā / thirumba", "Quickly / Slowly / Again", "குயிக்லி / ஸ்லோலி / அகெய்ன்"),
                BookVocabItem("சாப்பிடு / கூப்பிடு / பாடு", "sāppidu / kūppidu / pādu", "Eat / Call / Sing", "ஈட் / கால் / சிங்")
            ),
            sentences = listOf(
                BookSentenceItem(
                    english = "How many chairs are there here? There are four chairs here.",
                    tamilSpoken = "இங்கே எத்தனை நாற்காலி இருக்குது? இங்கே நாலு நாற்காலி இருக்குது.",
                    romanizedTamil = "inge eththane nākkāli irukkuthu? inge nālu nākkāli irukkuthu.",
                    englishPronunciationTamil = "ஹவ் மெனி சேர்ஸ் ஆர் தேர் ஹியர்? தேர் ஆர் ஃபோர் சேர்ஸ் ஹியர்."
                ),
                BookSentenceItem(
                    english = "Wait a bit. Wait inside the room.",
                    tamilSpoken = "கொஞ்சம் இருங்க. அறை உள்ளே இருங்க.",
                    romanizedTamil = "konjam irunga. are uḷḷe irunga.",
                    englishPronunciationTamil = "வெயிட் எ பிட். வெயிட் இன்சைடு தி ரூம்."
                ),
                BookSentenceItem(
                    english = "Drink milk, do not drink water.",
                    tamilSpoken = "பால் குடிங்க, தண்ணி குடிக்காதீங்க.",
                    romanizedTamil = "pāl kudinga, thaṇṇi kudikkādhinga.",
                    englishPronunciationTamil = "டிரிங்க் மில்க், டூ நாட் டிரிங்க் வாட்டர்.",
                    grammarNoteTamil = "'குடி' என்பது வன்வினை (Strong Verb) என்பதால் '-க்காதீங்க' (kudikkādhinga) வருகிறது."
                ),
                BookSentenceItem(
                    english = "Don't work now, work afterwards. Do this work well.",
                    tamilSpoken = "இப்போ வேலை செய்யாதீங்க, அப்புறம் செய்யுங்க. இந்த வேலை நல்லா செய்யுங்க.",
                    romanizedTamil = "ippo vēle seiyādhinga, apram seinga. indha vēle nallā seinga.",
                    englishPronunciationTamil = "டோன்ட் ஒர்க் நவ், ஒர்க் ஆஃப்டர்வர்ட்ஸ். டூ திஸ் ஒர்க் வெல்."
                ),
                BookSentenceItem(
                    english = "Don't walk fast. Walk very slowly.",
                    tamilSpoken = "வேகமா நடக்காதீங்க. ரொம்ப மெதுவா நடங்க.",
                    romanizedTamil = "vehamā nadakkādhinga. romba medhuvā nadanga.",
                    englishPronunciationTamil = "டோன்ட் வாக் ஃபாஸ்ட். வாக் வெரி ஸ்லோலி."
                ),
                BookSentenceItem(
                    english = "Bring some new books. Don't leave the old book here.",
                    tamilSpoken = "சில புது புத்தகங்க கொண்டுவாங்க. பழைய புத்தகம் இங்கே வைக்காதே.",
                    romanizedTamil = "sila pudhu pusthahanga konduvānga. paṛaya pusthaham inge vaikkādhe.",
                    englishPronunciationTamil = "பிரிங் சம் நியூ புக்ஸ். டோன்ட் லீவ் தி ஓல்ட் புக் ஹியர்."
                )
            ),
            expansionDrills = listOf(
                BookSentenceItem(
                    english = "Take away the water and milk.",
                    tamilSpoken = "பாலும் தண்ணியும் கொண்டுபோங்க.",
                    romanizedTamil = "pālum thaṇṇiyum kondupōnga.",
                    englishPronunciationTamil = "டேக் அவே தி வாட்டர் அண்ட் மில்க்."
                ),
                BookSentenceItem(
                    english = "This is neither water nor milk.",
                    tamilSpoken = "இது தண்ணியுமில்லே பாலுமில்லே.",
                    romanizedTamil = "idhu thaṇṇiyumille pālumille.",
                    englishPronunciationTamil = "திஸ் இஸ் நைதர் வாட்டர் நார் மில்க்."
                )
            )
        ),
        BookLessonModule(
            id = "lessons_9_14",
            lessonNumbers = "Lessons 9 – 14",
            titleEnglish = "Cases (Accusative, Possessive, Dative), Infinitives & Defective Verbs",
            titleTamil = "வேற்றுமை உருபுகள் (-ஐ, -உடைய, -க்கு), Infinitives & 'வேணும் / தெரியும்'",
            grammarExplanationTamil = "1. செயப்படுபொருள் (Object): '-ஐ / -யே' (அவனை கூப்பிடுங்க, மேசையை கொண்டுவாங்க).\n2. உடைமை (Possessive): 'என் / என்னுடைய', 'உங்க / உங்களுடைய'.\n3. நான்காம் வேற்றுமை (Dative - to/for): '-க்கு / -க்காக' (எனக்கு, உங்களுக்கு, வீட்டுக்கு).\n4. Defective Verbs: 'வேணும்' (want), 'வேணாம்' (don't want), 'போதும்' (enough), 'தெரியும்' (know), 'புரியுது' (understand).",
            grammarFormula = "Dative + vēṇum/theriyum/puriyuthu: enakku thaṇṇi vēṇum (I want water) | ungaḷukku theriyumā? (Do you know?)",
            vocabulary = listOf(
                BookVocabItem("எனக்கு / உங்களுக்கு / அவனுக்கு", "enakku / ungaḷukku / avanukku", "To me / To you / To him", "டூ மீ / டூ யூ / டூ ஹிம்"),
                BookVocabItem("வேணும் / வேணாம்", "vēṇum / vēṇām", "Is wanted (want) / Is not wanted (don't want)", "வான்ட் / டோன்ட் வான்ட்"),
                BookVocabItem("போதும் / போதாது", "pōdhum / pōdhādhu", "Is enough / Is not enough", "இஸ் இனஃப் / இஸ் நாட் இனஃப்"),
                BookVocabItem("தெரியும் / தெரியாது", "theriyum / theriyādhu", "Know / Do not know", "நோ / டூ நாட் நோ"),
                BookVocabItem("புரியுது / புரியலே", "puriyuthu / puriyale", "Is understood / Is not understood", "இஸ் அண்டர்ஸ்டுட் / இஸ் நாட் அண்டர்ஸ்டுட்"),
                BookVocabItem("தயவுசெய்து / கஷ்டம்", "dhayavuseidhu / kashtam", "Please / Difficulty or trouble", "ப்ளீஸ் / டிஃபிகல்ட்டி")
            ),
            sentences = listOf(
                BookSentenceItem(
                    english = "Don't call me. Call him. Look at this house.",
                    tamilSpoken = "என்னை கூப்பிடாதீங்க. அவரை கூப்பிடுங்க. இந்த வீட்டை பாருங்க.",
                    romanizedTamil = "enne kūppidadhinga. avare kūppidunga. indha vītte pārunga.",
                    englishPronunciationTamil = "டோன்ட் கால் மீ. கால் ஹிம். லுக் அட் திஸ் ஹவுஸ்."
                ),
                BookSentenceItem(
                    english = "What is your name? My name is John.",
                    tamilSpoken = "உங்களுடைய பேர் என்ன? என் பேர் ஜான்.",
                    romanizedTamil = "ungaḷudeya pēr yenna? en pēr john.",
                    englishPronunciationTamil = "வாட் இஸ் யுவர் நேம்? மை நேம் இஸ் ஜான்."
                ),
                BookSentenceItem(
                    english = "The meal is ready, come to eat. Tell him to wait a little.",
                    tamilSpoken = "சாப்பாடு தயார், சாப்பிட வாங்க. அவரை கொஞ்சம் இருக்க சொல்லுங்க.",
                    romanizedTamil = "sappādu thayār, sāppida vānga. avare konjam irukka sollunga.",
                    englishPronunciationTamil = "தி மீல் இஸ் ரெடி, கம் டூ ஈட். டெல் ஹிம் டூ வெயிட் எ லிட்டில்."
                ),
                BookSentenceItem(
                    english = "What do you want? This tea is not enough, I want a little more.",
                    tamilSpoken = "உங்களுக்கு என்ன வேணும்? இந்த டீ போதாது, இன்னும் கொஞ்சம் வேணும்.",
                    romanizedTamil = "ungaḷukku enna vēṇum? indha tī pōdhādhu, innum konjam vēṇum.",
                    englishPronunciationTamil = "வாட் டூ யூ வான்ட்? திஸ் டீ இஸ் நாட் இனஃப், ஐ வான்ட் எ லிட்டில் மோர்."
                ),
                BookSentenceItem(
                    english = "Sir, please teach me Tamil. Is my Tamil very clear to you?",
                    tamilSpoken = "ஐயா, தயவுசெய்து எனக்கு தமிழ் சொல்லிக்கொடுங்க. உங்களுக்கு என் தமிழ் நல்லா விளங்குதா?",
                    romanizedTamil = "aiyā, thayavuseithu enakku thamiṛ sollikodunga. ungalukku en thamiṛ nallā vilanguthā?",
                    englishPronunciationTamil = "சார், ப்ளீஸ் டீச் மீ தமிழ். இஸ் மை தமிழ் வெரி கிளியர் டூ யூ?"
                ),
                BookSentenceItem(
                    english = "Do not worry, this is not a big problem.",
                    tamilSpoken = "கஷ்டப்படாதீங்க. இது பெரிய கஷ்டம் இல்லே.",
                    romanizedTamil = "kashtappadādhinga. idhu periya kashtam ille.",
                    englishPronunciationTamil = "டூ நாட் ஒர்ரி, திஸ் இஸ் நாட் எ பிக் பிராப்ளம்."
                )
            ),
            expansionDrills = listOf(
                BookSentenceItem(
                    english = "Do you have a book? Yes, I have a book.",
                    tamilSpoken = "உங்களுக்கு புத்தகம் இருக்குதா? ஆமா, எனக்கு புத்தகம் இருக்குது.",
                    romanizedTamil = "ungaḷukku pusthaham irukkuthā? āmā, enakku pusthaham irukkuthu.",
                    englishPronunciationTamil = "டூ யூ ஹேவ் எ புக்? யெஸ், ஐ ஹேவ் எ புக்."
                )
            )
        ),
        BookLessonModule(
            id = "lessons_15_20",
            lessonNumbers = "Lessons 15 – 20",
            titleEnglish = "Numbers 10–1000, Time, Present Tense, Adjectives, Post-Positions & Locative Case",
            titleTamil = "எண்கள் 10–1000, நேரம், நிகழ்காலம் (Present Tense), இட வேற்றுமை (-லே)",
            grammarExplanationTamil = "நிகழ்காலத்தில் (Present Tense): மென்வினைக்கு '-றேன் / -றீங்க / -றார்' (போறேன், போறீங்க, போறார்), வன்வினைக்கு '-க்கிறேன் / -க்கிறீங்க / -க்கிறார்' (படிக்கிறேன், படிக்கிறீங்க, படிக்கிறார்). இடத்தைக் குறிக்க (Locative - in/at) '-லே' (வீட்டிலே, கடையிலே, ஆஸ்பத்திரியிலே) சேர்க்கவும்.",
            grammarFormula = "Weak Present: Root + r + ending (pōrēn, pōringa) | Strong Present: Root + kr + ending (padikrēn, padikringa) | Locative: Noun + le (vīttle = in the house)",
            vocabulary = listOf(
                BookVocabItem("இருபது / முப்பது / நூறு / ஆயிரம்", "irubadhu / muppadhu / nūru / āyiram", "20 / 30 / 100 / 1000", "ட்வென்டி / தர்ட்டி / ஹண்ட்ரட் / தௌசண்ட்"),
                BookVocabItem("காலமே / சாயங்காலம் / ராத்திரி", "kālame / sāingālam / rāthri", "Morning / Evening / Night", "மார்னிங் / ஈவ்னிங் / நைட்"),
                BookVocabItem("சந்தோஷம் / சுலபம் / வருத்தம்", "sandhōsham / sulabam / varuththam", "Happiness / Easy / Sorrow", "ஹேப்பினஸ் / ஈஸி / சாரோ"),
                BookVocabItem("முன்னாலே / பின்னாலே / கிட்ட / வழியா", "munnāle / pinnāle / kitta / vaṛiyā", "In front of / Behind / Near / Via", "இன் ஃபிரன்ட் ஆஃப் / பிஹைண்ட் / நியர் / வையா"),
                BookVocabItem("வீட்டிலே / கடையிலே / ஆஸ்பத்திரியிலே", "vīttle / kadele / āspathrile", "At home / In the shop / At the hospital", "அட் ஹோம் / இன் தி ஷாப் / அட் தி ஹாஸ்பிடல்")
            ),
            sentences = listOf(
                BookSentenceItem(
                    english = "What is the time now? Now it is eight o'clock.",
                    tamilSpoken = "இப்போ மணி என்ன? இப்போ எட்டு மணி.",
                    romanizedTamil = "ippo maṇi enna? ippo ettu maṇi.",
                    englishPronunciationTamil = "வாட் இஸ் தி டைம் நவ்? நவ் இட் இஸ் எய்ட் ஓ கிளாக்."
                ),
                BookSentenceItem(
                    english = "Hello madam, where are you going? I am going to my house.",
                    tamilSpoken = "வாங்கம்மா, எங்கே போறீங்க? நான் என் வீட்டுக்கு போறேன்.",
                    romanizedTamil = "vangamma, enge pōringa? nan en vīttukku pōrēn.",
                    englishPronunciationTamil = "ஹலோ மேடம், வேர் ஆர் யூ கோயிங்? ஐ அம் கோயிங் டூ மை ஹவுஸ்."
                ),
                BookSentenceItem(
                    english = "I am coming to your house now. When are you coming to my house?",
                    tamilSpoken = "நான் இப்போ உங்க வீட்டுக்கு வரேன். நீங்க என் வீட்டுக்கு எப்போ வரீங்க?",
                    romanizedTamil = "nan ippo unga vīttukku varēn. nīnga en vīttukku yeppo varinga?",
                    englishPronunciationTamil = "ஐ அம் கமிங் டூ யுவர் ஹவுஸ் நவ். வென் ஆர் யூ கமிங் டூ மை ஹவுஸ்?"
                ),
                BookSentenceItem(
                    english = "Are you speaking Tamil well now? I speak a little, only a little.",
                    tamilSpoken = "இப்போ நீங்க தமிழ் நல்லா பேசுறீங்களா? கொஞ்சம் பேசுறேன், கொஞ்சம்தான்.",
                    romanizedTamil = "ippo nīnga thamiṛ nallā pēsringaḷā? konjam pēsrēn, konjamdhān.",
                    englishPronunciationTamil = "ஆர் யூ ஸ்பீக்கிங் தமிழ் வெல் நவ்? ஐ ஸ்பீக் எ லிட்டில், ஒன்லி எ லிட்டில்."
                ),
                BookSentenceItem(
                    english = "Go straight first, then turn left.",
                    tamilSpoken = "முன்னாலே நேரா போங்க, அப்புறம் இடது பக்கம் திரும்புங்க.",
                    romanizedTamil = "munnāle nera pōnga, apram idathu pakkam thirumbunga.",
                    englishPronunciationTamil = "கோ ஸ்ட்ரெய்ட் ஃபர்ஸ்ட், தென் டர்ன் லெஃப்ட்."
                ),
                BookSentenceItem(
                    english = "Is your father at home? He is working at the hospital.",
                    tamilSpoken = "வீட்டிலே உங்க அப்பா இருக்கிறாரா? அவர் ஆஸ்பத்திரியிலே வேலை செய்றார்.",
                    romanizedTamil = "vīttle unga appā irukrārā? avar āspathrile vēle seirār.",
                    englishPronunciationTamil = "இஸ் யுவர் ஃபாதர் அட் ஹோம்? ஹி இஸ் ஒர்க்கிங் அட் தி ஹாஸ்பிடல்."
                )
            ),
            expansionDrills = listOf(
                BookSentenceItem(
                    english = "It is raining now. Take an umbrella.",
                    tamilSpoken = "இப்போ மழை பெய்து. குடை கொண்டுபோங்க.",
                    romanizedTamil = "ippo maṛe peidhu. kude kondupōnga.",
                    englishPronunciationTamil = "இட் இஸ் ரெயினிங் நவ். டேக் ஆன் அம்ப்ரெல்லா."
                )
            )
        ),
        BookLessonModule(
            id = "lessons_21_27",
            lessonNumbers = "Lessons 21 – 27",
            titleEnglish = "Future Tense, Negatives, May (-lām), Should (-ṇum), With (-ōdu) & Can (mudiyum)",
            titleTamil = "எதிர்காலம், எதிர்மறை (-லே / -மாட்டேன்), -லாம், -ணும் / -கூடாது, முடியும் / முடியாது",
            grammarExplanationTamil = "1. எதிர்காலம் (Future): மென்வினை '+வேன்/+வீங்க' (போவேன்), வன்வினை '+ப்பேன்/+ப்பீங்க' (படிப்பேன்).\n2. நிகழ்/இறந்தகால எதிர்மறை: Infinitive + 'லே' (நான் போகலே = I did not go / do not go).\n3. எதிர்கால எதிர்மறை: Infinitive + 'மாட்டேன்' (போகமாட்டேன் = I will not go).\n4. அனுமதி (May): '+லாம்' (போகலாம்).\n5. கடமை (Should/Must): '+ணும் / +கூடாது' (படிக்கணும் / படிக்கக்கூடாது).\n6. திறன் (Can/Cannot): '+முடியும் / +முடியாது / +முடியலே'.",
            grammarFormula = "Future: pōvēn / padippēn | Neg: pōhale (didn't go), pōhamāttēn (won't go) | Should: padikkaṇum | Can: pesa mudiyum",
            vocabulary = listOf(
                BookVocabItem("ஞாயிற்றுக்கிழமை முதல் சனிக்கிழமை", "nāiththukiṛame – sanikiṛame", "Sunday to Saturday", "சண்டே டூ சாட்டர்டே"),
                BookVocabItem("நேத்து / இண்ணைக்கு / நாளைக்கு", "nēththu / iṇṇakki / nāḷakki", "Yesterday / Today / Tomorrow", "யெஸ்டர்டே / டுடே / டுமாரோ"),
                BookVocabItem("பிடிக்கும் / பிடிக்காது", "pidikkum / pidikkādhu", "Like / Dislike", "லைக் / டிஸ்லைக்"),
                BookVocabItem("கட்டாயம் / நிச்சயமா", "kattāyam / nichchayamā", "Must (certainly) / Definitely", "மஸ்ட் / டெஃபனிட்லி"),
                BookVocabItem("என்னோடு / உங்களோடு", "ennōdu / ungaḷōdu", "With me / With you", "வித் மீ / வித் யூ"),
                BookVocabItem("வீட்டிலிருந்து / சென்னையிலிருந்து", "vīttilirundhu / chennaiyilirundhu", "From the house / From Chennai", "ஃபிரம் தி ஹவுஸ் / ஃபிரம் சென்னை")
            ),
            sentences = listOf(
                BookSentenceItem(
                    english = "I will go to Chennai tomorrow. Will you come too?",
                    tamilSpoken = "நான் நாளைக்கு சென்னைக்கு போவேன். நீங்களும் வருவீங்களா?",
                    romanizedTamil = "nān nāḷakki chennaikku pōvēn. nīngaḷum varavingalā?",
                    englishPronunciationTamil = "ஐ வில் கோ டூ சென்னை டுமாரோ. வில் யூ கம் டூ?"
                ),
                BookSentenceItem(
                    english = "I am not eating now, I will eat afterwards. May we go home now?",
                    tamilSpoken = "நான் இப்போ சாப்பிடலே, அப்புறம் சாப்பிடுவேன். நாங்க இப்போ வீட்டுக்கு போகலாமா?",
                    romanizedTamil = "nān ippo sāppidale, apram sāppiduvēn. nānga ippo vīttukku pōhalāmā?",
                    englishPronunciationTamil = "ஐ அம் நாட் ஈட்டிங் நவ், ஐ வில் ஈட் ஆஃப்டர்வர்ட்ஸ். மே வி கோ ஹோம் நவ்?"
                ),
                BookSentenceItem(
                    english = "Which do you like, coffee or tea? He will not come here tomorrow.",
                    tamilSpoken = "உங்களுக்கு எது இஷ்டம், காப்பியா டீயா? அவர் நாளைக்கு இங்கே வரமாட்டார்.",
                    romanizedTamil = "ungaḷukku edhu ishtam, kāppiyā tīyā? avar nāḷakki inge varamāttār.",
                    englishPronunciationTamil = "விச் டூ யூ லைக், காஃபியா ஆர் டீ? ஹி வில் நாட் கம் ஹியர் டுமாரோ."
                ),
                BookSentenceItem(
                    english = "You should do this work carefully. You should not speak like that here.",
                    tamilSpoken = "நீங்க இந்த வேலையை கவனமா செய்யணும். இங்கே அப்படி பேசக்கூடாது.",
                    romanizedTamil = "nīnga indha vēleye ghavanamā seiyaṇum. inge apdi pēsakūdādhu.",
                    englishPronunciationTamil = "யூ ஷுட் டூ திஸ் ஒர்க் கேர்ஃபுல்லி. யூ ஷுட் நாட் ஸ்பீக் லைக் தட் ஹியர்."
                ),
                BookSentenceItem(
                    english = "Now you can speak Tamil well! Can you make tea in ten minutes?",
                    tamilSpoken = "நீங்க இப்போ நல்லா தமிழ் பேசமுடியும்! பத்து நிமிஷத்திலே டீ தயார் செய்யமுடியுமா?",
                    romanizedTamil = "nīnga ippo nallā thamiṛ pēsamudiyum! paththu nimishathle tī thayār seiyamudiyumā?",
                    englishPronunciationTamil = "நவ் யூ கேன் ஸ்பீக் தமிழ் வெல்! கேன் யூ மேக் டீ இன் டென் மினிட்ஸ்?"
                )
            ),
            expansionDrills = listOf(
                BookSentenceItem(
                    english = "Where are you coming from now? I am coming straight from my house.",
                    tamilSpoken = "நீங்க இப்போ எங்கிருந்து வரீங்க? நான் இப்போ நேரா என் வீட்டிலிருந்து வரேன்.",
                    romanizedTamil = "nīnga ippo engirundhu varinga? nān ippo nēra en vīttilirundhu varēn.",
                    englishPronunciationTamil = "வேர் ஆர் யூ கமிங் ஃபிரம் நவ்? ஐ அம் கமிங் ஸ்ட்ரெய்ட் ஃபிரம் மை ஹவுஸ்."
                )
            )
        ),
        BookLessonModule(
            id = "lessons_30_38",
            lessonNumbers = "Lessons 30 – 38",
            titleEnglish = "Past Tense Verbs (All 5 Medial Groups: thth, dh, ndh, in/n, tt)",
            titleTamil = "இறந்தகால வினைச்சொற்கள் (Past Tense - 5 இடைநிலை வகைகள்)",
            grammarExplanationTamil = "தமிழில் இறந்தகாலம் (Past Tense) உருவாக்க வினையடியுடன் 5 வகையான இடைநிலைகள் (Medials) சேரும்:\n1. 'thth' (-த்த்-): படி → படித்தேன், பார் → பார்த்தேன், கொடு → கொடுத்தேன்.\n2. 'dh' (-த்-): செய் → செய்தேன், அழு → அழுதேன்.\n3. 'ndh' (-ந்த்-): வா → வந்தேன், இரு → இருந்தேன், நட → நடந்தேன், உட்கார் → உட்கார்ந்தேன்.\n4. 'in / n' (-இன் / -ன்-): போ → போனேன், சொல் → சொன்னேன், வாங்கு → வாங்கினேன், தூங்கு → தூங்கினேன்.\n5. 'tt' (-ட்ட்-): சாப்பிடு → சாப்பிட்டேன், கூப்பிடு → கூப்பிட்டேன், கேள் → கேட்டேன்.",
            grammarFormula = "pār + thth + ēn = pārththēn (I saw) | vā + ndh + ēn = vandhēn (I came) | sāppidu -> sāppittēn (I ate)",
            vocabulary = listOf(
                BookVocabItem("பார்த்தேன் / படித்தேன் / கொடுத்தேன்", "pārththēn / padiththēn / koduththēn", "I saw / I studied / I gave", "ஐ சா / ஐ ஸ்டடீட் / ஐ கேவ்"),
                BookVocabItem("செய்தேன் / வந்தேன் / திறந்தேன்", "seidhēn / vandhēn / thirandhēn", "I did / I came / I opened", "ஐ டிட் / ஐ கேம் / ஐ ஓப்பன்ட்"),
                BookVocabItem("வாங்கினேன் / சொன்னேன் / போனேன்", "vānginēn / sonnēn / pōnēn", "I bought / I said / I went", "ஐ பாட் / ஐ செட் / ஐ வென்ட்"),
                BookVocabItem("சாப்பிட்டேன் / கேட்டேன் / கூப்பிட்டேன்", "sāppittēn / kēttēn / kūppittēn", "I ate / I asked (heard) / I called", "ஐ ஏட் / ஐ ஆஸ்க்ட் / ஐ கால்டு")
            ),
            sentences = listOf(
                BookSentenceItem(
                    english = "Yesterday I met a good man. Raman gave me a good book.",
                    tamilSpoken = "நேத்து நான் ஒரு நல்ல மனுஷரை சந்தித்தேன். ராமன் எனக்கு ஒரு நல்ல புத்தகம் கொடுத்தான்.",
                    romanizedTamil = "nēthu nān oru nalla manushare sandhittēn. rāman enakku oru nallā pusthaham koduththān.",
                    englishPronunciationTamil = "யெஸ்டர்டே ஐ மெட் எ குட் மேன். ராமன் கேவ் மீ எ குட் புக்."
                ),
                BookSentenceItem(
                    english = "Where did you put my spectacles? Who taught you Tamil?",
                    tamilSpoken = "என் மூக்கு-கண்ணாடியை எங்கே வைத்தீங்க? உங்களுக்கு யார் தமிழ் கத்துக்கொடுத்தாங்க?",
                    romanizedTamil = "en mūkku-kaṇṇādiyē enge vaiththinga? ungaḷukku yār thamiṛ kaththukoduththānga?",
                    englishPronunciationTamil = "வேர் டிட் யூ புட் மை ஸ்பெக்டக்கிள்ஸ்? ஹூ டாட் யூ தமிழ்?"
                ),
                BookSentenceItem(
                    english = "Yesterday I got up at four o'clock in the morning. I came to your house to see you.",
                    tamilSpoken = "நேத்து காலையிலே நான் சரியா நாலு மணிக்கு எழுந்தேன். நான் உங்களை பார்க்க உங்க வீட்டுக்கு வந்தேன்.",
                    romanizedTamil = "nēthu kālele nān sariyā nālu maṇikku eṛundhēn. nān ungale pārkka unga vīttukku vandhēn.",
                    englishPronunciationTamil = "யெஸ்டர்டே ஐ காட் அப் அட் ஃபோர் ஓ கிளாக் இன் தி மார்னிங். ஐ கேம் டூ யுவர் ஹவுஸ் டூ சீ யூ."
                ),
                BookSentenceItem(
                    english = "Did you sleep well last night? What did you say? Please say it again.",
                    tamilSpoken = "நீங்க ராத்திரி நல்லா தூங்கினீங்களா? என்ன சொன்னீங்க? தயவுசெய்து மறுபடியும் சொல்லுங்க.",
                    romanizedTamil = "nīnga rāthri nallā thūnginingaḷā? yenna sonninga? dhayavuseithu marupadiyum sollunga.",
                    englishPronunciationTamil = "டிட் யூ ஸ்லீப் வெல் லாஸ்ட் நைட்? வாட் டிட் யூ சே? ப்ளீஸ் சே இட் அகெய்ன்."
                ),
                BookSentenceItem(
                    english = "All of us ate just now; are you not eating? I tried hard to learn Tamil.",
                    tamilSpoken = "நாங்கெல்லாம் இப்போதான் சாப்பிட்டோம்; நீங்க சாப்பிடலையா? தமிழ் கத்துக்கொள்ள நான் ரொம்ப பிரயாசப்பட்டேன்.",
                    romanizedTamil = "nāngellām ippodhān sāppittōm; nīnga sāppidaleyā? thamiṛ kaththukkoḷḷa nān romba prayasappattēn.",
                    englishPronunciationTamil = "ஆல் ஆஃப் அஸ் ஏட் ஜஸ்ட் நவ்; ஆர் யூ நாட் ஈட்டிங்? ஐ ட்ரைடு ஹார்ட் டூ லேர்ன் தமிழ்."
                )
            ),
            expansionDrills = listOf(
                BookSentenceItem(
                    english = "My pen was lost. I searched for it all over the house.",
                    tamilSpoken = "என் பேனா காணாமல் போச்சு. வீடெல்லாம் தேடினேன்.",
                    romanizedTamil = "en pēna kāṇāmal pōchchu. vīdellām thēdinēn.",
                    englishPronunciationTamil = "மை பென் வாஸ் லாஸ்ட். ஐ சர்ச்ட் ஃபார் இட் ஆல் ஓவர் தி ஹவுஸ்."
                )
            )
        ),
        BookLessonModule(
            id = "lessons_39_50",
            lessonNumbers = "Lessons 39 – 50",
            titleEnglish = "Participles, Continuous/Perfect, Conditionals ('If' / 'Even If'), Time Clauses & Suffixes",
            titleTamil = "வினையெச்சம், தொடர் காலம் (-கொண்டிருக்கிறேன்), நிபந்தனை ('-ஆல்' / '-ஆலும்') & கால வாக்கியங்கள்",
            grammarExplanationTamil = "1. Past Participle (செய்து / வந்து / போய் / சாப்பிட்டு): தொடர் செயல்களை இணைக்க (சாப்பிட்டு வெளியே போனான்).\n2. Continuous Tense: Past Participle + 'கொண்டிருக்கிறேன் / கிட்டிருக்கிறேன்' (நான் படித்துக் கொண்டிருக்கிறேன் = I am studying).\n3. Conditional ('If'): '-ஆ / -ஆல்' (நீங்க வந்தா = If you come), எதிர்மறை: '-ஆவிட்டால் / -இல்லேன்னா' (நீங்க வரலேன்னா = If you don't come).\n4. 'Even if': '-ஆலும்' (மழை பெய்தாலும் = Even if it rains).\n5. Time Clauses: '-போது' (when/while), '-முன்' (before), '-பின் / -அப்புறம்' (after), '-உடனே' (as soon as), '-வரைக்கும்' (until).",
            grammarFormula = "Continuous: vandhu + kondirukrēn | If: vandhā (if comes) / varalenā (if not) | Even if: vandhālum | As soon as: vandhavudanē",
            vocabulary = listOf(
                BookVocabItem("வந்து / போய் / சாப்பிட்டு / பார்த்து", "vandhu / pōi / sāppittu / pārththu", "Having come / gone / eaten / seen", "ஹேவிங் கம் / கான் / ஈட்டன் / சீன்"),
                BookVocabItem("இல்லாமே / மறக்காமே", "illāme / marakkāme", "Without / Without forgetting", "வித்தவுட் / வித்தவுட் ஃபார்கெட்டிங்"),
                BookVocabItem("வந்தால் / வரலேன்னா / வந்தாலும்", "vandhā / varalenā / vandhālum", "If comes / If doesn't come / Even if comes", "இஃப் கம்ஸ் / இஃப் டஸன்ட் கம் / ஈவன் இஃப் கம்ஸ்"),
                BookVocabItem("விட / எல்லாரையும் விட", "-vida / ellāraiyum vida", "More than (Comparative) / Best of all (Superlative)", "மோர் தேன் / பெஸ்ட் ஆஃப் ஆல்"),
                BookVocabItem("உடனே / வரைக்கும் / படியாலே", "udanē / varaikkum / padiyāle", "As soon as / Until / Because of", "ஆஸ் சூன் ஆஸ் / அன்டில் / பிகாஸ் ஆஃப்")
            ),
            sentences = listOf(
                BookSentenceItem(
                    english = "Read and finish this book soon and give it to me.",
                    tamilSpoken = "இந்த புத்தகத்தை சீக்கிரம் படித்து முடித்து என்னிடம் கொடுங்க.",
                    romanizedTamil = "indha pusthahathe sīkram padiththu mudiththu ennidam kodunga.",
                    englishPronunciationTamil = "ரீட் அண்ட் ஃபினிஷ் திஸ் புக் சூன் அண்ட் கிவ் இட் டூ மீ."
                ),
                BookSentenceItem(
                    english = "In a few days you will be speaking Tamil fluently.",
                    tamilSpoken = "நீங்க இன்னும் கொஞ்சம் நாளிலே நல்லா தமிழ் பேசிக் கொண்டிருப்பீங்க.",
                    romanizedTamil = "nīnga innum konjam nāḷḷe nallā thamiṛ pēsi kondiruppinga.",
                    englishPronunciationTamil = "இன் எ ஃபியூ டேய்ஸ் யூ வில் பி ஸ்பீக்கிங் தமிழ் ஃபுளூயன்ட்லி."
                ),
                BookSentenceItem(
                    english = "If you study Tamil daily, you can speak Tamil well.",
                    tamilSpoken = "நீங்க நாள்தோறும் தமிழ் படித்தால், தமிழ் நல்லா பேசமுடியும்.",
                    romanizedTamil = "nīnga nāḷdhōrum thamiṛ padiththā, thamiṛ nalla pesamudiyum.",
                    englishPronunciationTamil = "இஃப் யூ ஸ்டடி தமிழ் டெய்லி, யூ கேன் ஸ்பீக் தமிழ் வெல்."
                ),
                BookSentenceItem(
                    english = "Even if this lesson is not clear to me, I can read it well.",
                    tamilSpoken = "இந்த பாடம் எனக்கு விளங்காவிட்டாலும் நான் அதை நல்லா படிக்கமுடியும்.",
                    romanizedTamil = "indha pādam enakku viḷangāvittālum nān adhe nalla padikkamudiyum.",
                    englishPronunciationTamil = "ஈவன் இஃப் திஸ் லெசன் இஸ் நாட் கிளியர் டூ மீ, ஐ கேன் ரீட் இட் வெல்."
                ),
                BookSentenceItem(
                    english = "Going together is better than going alone.",
                    tamilSpoken = "தனியா போவதைவிட எல்லாரும் சேர்ந்து போவது நல்லது.",
                    romanizedTamil = "thaniyā pōvadhevida ellārum serndhu pōvadhu nalladhu.",
                    englishPronunciationTamil = "கோயிங் டுகெதர் இஸ் பெட்டர் தேன் கோயிங் அலோன்."
                ),
                BookSentenceItem(
                    english = "Come and see me as soon as your work is finished.",
                    tamilSpoken = "உங்க வேலை முடிந்ததும் என்னை வந்து பாருங்க.",
                    romanizedTamil = "unga vēle mudindhadhum enne vandhu pārunga.",
                    englishPronunciationTamil = "கம் அண்ட் சீ மீ ஆஸ் சூன் ஆஸ் யுவர் ஒர்க் இஸ் ஃபினிஷ்ட்."
                )
            ),
            expansionDrills = listOf(
                BookSentenceItem(
                    english = "Because I walked very far, I am very tired.",
                    tamilSpoken = "நான் ரொம்ப தூரம் நடந்தபடியாலே எனக்கு ரொம்ப களைப்பா இருக்குது.",
                    romanizedTamil = "nān romba dhuram nadandhapadiyāle enakku romba kaḷeppā irukkudhu.",
                    englishPronunciationTamil = "பிகாஸ் ஐ வாக்ட் வெரி ஃபார், ஐ அம் வெரி டயர்ட்."
                )
            )
        )
    )

    val everydayDialogues: List<BookDialogueLesson> = listOf(
        BookDialogueLesson(
            id = "dialogue_51",
            lessonNumber = 51,
            titleEnglish = "Lesson 51: Greeting and Meeting People",
            titleTamil = "பாடம் 51: மனிதர்களைச் சந்தித்தலும் நலம் விசாரித்தலும்",
            settingTamil = "மருத்துவரின் வரவேற்பறையில் நோயாளியும் வரவேற்பாளரும் (Doctor's Reception Desk)",
            vocabulary = listOf(
                BookVocabItem("ஒண்ணுமில்லே / விசேஷமா", "oṇṇumille / visēshamā", "Nothing / Special", "நத்திங் / ஸ்பெஷல்"),
                BookVocabItem("அவசரம் / காத்திரு", "avasaram / kāththiru", "Urgency (hurry) / Wait", "அர்ஜென்சி / வெயிட்")
            ),
            lines = listOf(
                BookDialogueLine("R", "வரவேற்பாளர்", "Is that you? Come in. Take a seat.", "நீங்களா? உள்ளே வாங்க. நாற்காலியிலே உக்காருங்க.", "nīngalā? uḷḷe vānga. nākkālile ukkārunga.", "இஸ் தட் யூ? கம் இன். டேக் எ சீட்."),
                BookDialogueLine("P", "நோயாளி", "Thank you, many thanks.", "நன்றி, ரொம்ப நன்றி.", "nandri, romba nandri.", "தேங்க் யூ, மெனி தேங்க்ஸ்."),
                BookDialogueLine("R", "வரவேற்பாளர்", "How are you, are you well?", "எப்படி இருக்கிறீங்க, சுகமா?", "yepdi irukringa, suhamā?", "ஹவ் ஆர் யூ, ஆர் யூ வெல்?"),
                BookDialogueLine("P", "நோயாளி", "No, I am not well. I have been unwell for one week.", "இல்லே, நான் சுகமில்லே. நான் ஒரு வாரமா சுகமில்லே.", "ille, nān suhamille. nān oru vāramā suhamille.", "நோ, ஐ அம் நாட் வெல். ஐ ஹேவ் பீன் அன்வெல் ஃபார் ஒன் வீக்."),
                BookDialogueLine("R", "வரவேற்பாளர்", "Is your family well? Where do you live?", "உங்க குடும்பம் சுகமா? நீங்க எங்கே இருக்கிறீங்க?", "unga kudambam suhamā? nīnga enge irukringa?", "இஸ் யுவர் ஃபேமிலி வெல்? வேர் டூ யூ லிவ்?"),
                BookDialogueLine("P", "நோயாளி", "Yes, everyone in my family is well. My house is behind the temple.", "ஆமா, என் வீட்டிலே எல்லாரும் சுகம்தான். என் வீடு கோவிலுக்கு பின்னாலே இருக்குது.", "āmā, en vītle ellārum suhamdhān. yen vīdu kōvilukku pinnāle irukkudhu.", "யெஸ், எவ்ரிஒன் இன் மை ஃபேமிலி இஸ் வெல். மை ஹவுஸ் இஸ் பிஹைண்ட் தி டெம்பிள்."),
                BookDialogueLine("P", "நோயாளி", "I want to see the doctor. Is he in?", "நான் டாக்டரை பார்க்கணும். உள்ளே இருக்கிறாரா?", "nān doctore pārkkaṇum. uḷḷe irukrārā?", "ஐ வான்ட் டூ சீ தி டாக்டர். இஸ் ஹி இன்?"),
                BookDialogueLine("R", "வரவேற்பாளர்", "No, he has gone out. He will return soon.", "இல்லே, அவர் வெளியே போயிருக்கிறார். அவர் சீக்கிரம் திரும்ப வருவார்.", "ille avar veḷiye pōi irukrār. avar sīkram thirumba varuvār.", "நோ, ஹி ஹேஸ் கான் அவுட். ஹி வில் ரிட்டர்ன் சூன்."),
                BookDialogueLine("P", "நோயாளி", "May I wait for him for a little while?", "நான் அவருக்காக கொஞ்ச நேரம் காத்திருக்கலாமா?", "nān avarukkāha konja nēram kāththirukkalāmā?", "மே ஐ வெயிட் ஃபார் ஹிம் ஃபார் எ லிட்டில் வைல்?"),
                BookDialogueLine("R", "வரவேற்பாளர்", "Oh, by all means you may wait.", "ஓ, தாராளமா காத்திருக்கலாம்.", "ō, dhārālamā kāththirukkalām.", "ஓ, பை ஆல் மீன்ஸ் யூ மே வெயிட்.")
            )
        ),
        BookDialogueLesson(
            id = "dialogue_52",
            lessonNumber = 52,
            titleEnglish = "Lesson 52: Conversation About One's Work",
            titleTamil = "பாடம் 52: தொழில் மற்றும் வேலை பற்றிய உரையாடல்",
            settingTamil = "பல்வேறு தொழில்கள் மற்றும் வேலைகள் குறித்துப் பேசுதல்",
            vocabulary = listOf(
                BookVocabItem("விவசாயம் / குமாஸ்தா / தச்சர்", "vivasāyam / gumasthā / thachchar", "Agriculture / Clerk / Carpenter", "அக்ரிகல்ச்சர் / கிளார்க் / கார்ப்பென்டர்"),
                BookVocabItem("வியாபாரி / உழைப்பாளி", "vyābāri / uṛaippāli", "Merchant / Hard worker", "மெர்ச்சன்ட் / ஹார்ட் ஒர்க்கர்")
            ),
            lines = listOf(
                BookDialogueLine("A", "நபர் 1", "What is your occupation?", "உங்களுக்கு என்ன வேலை?", "ungalukku enna vēle?", "வாட் இஸ் யுவர் ஆக்குபேஷன்?"),
                BookDialogueLine("B", "நபர் 2", "I am working as a clerk.", "நான் குமாஸ்தா வேலை செய்றேன்.", "nān gumasthā vēle seirēn.", "ஐ அம் ஒர்க்கிங் ஆஸ் எ கிளார்க்."),
                BookDialogueLine("A", "நபர் 1", "What is the main occupation in the village?", "கிராமத்திலே எது முக்கியமான வேலை?", "grāmathle edhu mukyamāna vēle?", "வாட் இஸ் தி மெயின் ஆக்குபேஷன் இன் தி வில்லேஜ்?"),
                BookDialogueLine("B", "நபர் 2", "Agriculture is the main occupation. Our father is a merchant.", "விவசாயம் முக்கியமான வேலை. எங்கப்பா ஒரு வியாபாரி.", "vivasāyam mukyamāna vēle. engappā oru vyābāri.", "அக்ரிகல்ச்சர் இஸ் தி மெயின் ஆக்குபேஷன். அவர் ஃபாதர் இஸ் எ மெர்ச்சன்ட்.")
            )
        ),
        BookDialogueLesson(
            id = "dialogue_53",
            lessonNumber = 53,
            titleEnglish = "Lesson 53: Conversation With a Servant / Helper",
            titleTamil = "பாடம் 53: வீட்டு உதவியாளருடன் உரையாடல்",
            settingTamil = "வீட்டுப் பணிகள் மற்றும் அன்றாட அறிவுறுத்தல்கள்",
            vocabulary = listOf(
                BookVocabItem("செய்தித்தாள் / விசிறி போடு", "seidhi thāḷ / visiri pōdu", "Newspaper / Switch on the fan", "நியூஸ்பேப்பர் / ஸ்விட்ச் ஆன் தி ஃபேன்"),
                BookVocabItem("பத்திரமா இரு / ஜாக்கிரதையா இரு", "badhramā iru / jāgredheyā iru", "Be careful / Take care", "பி கேர்ஃபுல் / டேக் கேர்")
            ),
            lines = listOf(
                BookDialogueLine("M", "בעל வீடு", "Where is today's newspaper? Where is my room key?", "இண்ணைக்கு செய்தித்தாள் எங்கே? என் அறைக்கு சாவி எங்கே?", "iṇṇakki seidhi thāḷ enge? en arekku sāvi enge?", "வேர் இஸ் டுடேஸ் நியூஸ்பேப்பர்? வேர் இஸ் மை ரூம் கீ?"),
                BookDialogueLine("S", "உதவியாளர்", "Here it is sir. I am bringing the key as well sir.", "இதோ இருக்குதுங்க. சாவி கூட கொண்டுவரேங்க.", "idhō irukkudhunga. sāvi kūda koṇduvarēn’ga.", "ஹியர் இட் இஸ் சார். ஐ அம் பிரிங்கிங் தி கீ ஆஸ் வெல் சார்."),
                BookDialogueLine("M", "בעל வீடு", "Put on the light and the fan. Do not forget what I said.", "விளக்கும் விசிறியும் போடு. நான் சொன்னதை மறந்து போகாதே.", "viḷakkum visiriyum pōdu. nān sonnadhe marandhu pōhādhe.", "புட் ஆன் தி லைட் அண்ட் தி ஃபேன். டூ நாட் ஃபார்கெட் வாட் ஐ செட்."),
                BookDialogueLine("S", "உதவியாளர்", "Understood, I will not forget.", "தெரிந்தது, நான் மறக்கமாட்டேன்.", "therindhadhu, nān marakkamāttēn.", "அண்டர்ஸ்டுட், ஐ வில் நாட் ஃபார்கெட்.")
            )
        ),
        BookDialogueLesson(
            id = "dialogue_54",
            lessonNumber = 54,
            titleEnglish = "Lesson 54: Conversation Regarding Time",
            titleTamil = "பாடம் 54: நேரம் மற்றும் கடிகாரம் பற்றிய உரையாடல்",
            settingTamil = "மணி கேட்டல், கடிகாரம் மற்றும் வருகை நேரம்",
            vocabulary = listOf(
                BookVocabItem("கை-கடிகாரம் / விடுமுறை", "kai-gadihāram / vidumure", "Wristwatch / Holiday (leave)", "ரிஸ்ட் வாட்ச் / ஹாலிடே"),
                BookVocabItem("நேரம் ஆச்சு / அதிகாலையிலே", "nēram āchchu / adhi-kālaile", "It is time / Early in the morning", "இட் இஸ் டைம் / ஏர்லி இன் தி மார்னிங்")
            ),
            lines = listOf(
                BookDialogueLine("A", "நபர் 1", "What is the time by your watch? Is your watch correct?", "உங்க கடிகாரத்திலே என்ன மணி? உங்க கடிகாரம் சரியா போகுதா?", "unga gadihārathle enna maṇi? unga gadihāram sariyā pōhudhā?", "வாட் இஸ் தி டைம் பை யுவர் வாட்ச்? இஸ் யுவர் வாட்ச் கரெக்ட்?"),
                BookDialogueLine("B", "நபர் 2", "It is 2:45 by my watch. My watch is five minutes fast.", "என் கடிகாரத்திலே ரெண்டே முக்கால் மணி. என் கடிகாரம் அஞ்சு நிமிஷம் முந்தி போகுது.", "en gadihārathle reṇdē mukkāl maṇi. en gadihāram anju nimisham mundhi pōhudhu.", "இட் இஸ் டூ ஃபார்ட்டி-ஃபைவ் பை மை வாட்ச். மை வாட்ச் இஸ் ஃபைவ் மினிட்ஸ் ஃபாஸ்ட்."),
                BookDialogueLine("A", "நபர் 1", "Can you come at 5 o'clock this evening? Bring four samosas.", "நீங்க இண்ணைக்கு சாயங்காலம் அஞ்சு மணிக்கு வர முடியுமா? நாலு சமோசா கொண்டுவாங்க.", "nīnga iṇṇakki sāingālam anju manikku vara mudiyumā? nālu samōsā konduvānga.", "கேன் யூ கம் அட் ஃபைவ் ஓ கிளாக் திஸ் ஈவ்னிங்? பிரிங் ஃபோர் சமோசாஸ்."),
                BookDialogueLine("B", "நபர் 2", "Oh dear, it is time, I must go at once. I will see you tomorrow without fail.", "ஐயோ, நேரம் ஆச்சு, நான் உடனே போகணும். நாளைக்கு தவறாம பார்ப்பேன்.", "aiyō, nēram āipōchchu, nān udane pōhaṇum. nāḷakki thavarāma pārppēn.", "ஓ டியர், இட் இஸ் டைம், ஐ மஸ்ட் கோ அட் ஒன்ஸ். ஐ வில் சீ யூ டுமாரோ வித்தவுட் ஃபெயில்.")
            )
        ),
        BookDialogueLesson(
            id = "dialogue_55",
            lessonNumber = 55,
            titleEnglish = "Lesson 55: Conversation About Weather",
            titleTamil = "பாடம் 55: வானிலை (மழை, வெயில், குளிர்) பற்றிய உரையாடல்",
            settingTamil = "அன்றாட வானிலை, வெயில் மற்றும் மழை குறித்துப் பேசுதல்",
            vocabulary = listOf(
                BookVocabItem("பருவ நிலை / இடி / மின்னல்", "paruva nile / idi / minal", "Weather / Thunder / Lightning", "வெதர் / தண்டர் / லைட்னிங்"),
                BookVocabItem("புழுக்கம் / குளிர்ச்சி", "puṛukkam / kuḷirchchi", "Sultry (humid) / Coolness", "சல்ட்ரி / கூல்னஸ்")
            ),
            lines = listOf(
                BookDialogueLine("A", "நபர் 1", "How is the weather today? Will it rain in a short while?", "இண்ணைக்கு பருவ நிலை எப்படி இருக்குது? கொஞ்ச நேரத்திலே மழை பெய்யுமா?", "iṇṇakki paruva nile epdi irukkudhu? konja nērathle maṛe peiyumā?", "ஹவ் இஸ் தி வெதர் டுடே? வில் இட் ரெயின் இன் எ ஷார்ட் வைல்?"),
                BookDialogueLine("B", "நபர் 2", "Yes, it seems like it will rain. Have you had heavy rain in your town?", "ஆமா, மழை பெய்யும்போல இருக்குது. உங்க ஊரிலே நல்ல மழை பெய்ததா?", "āmā, maṛe peiyumpōla irukkudhu. unga urle nalla maṛe peidhadhā?", "யெஸ், இட் சீம்ஸ் லைக் இட் வில் ரெயின். ஹேவ் யூ ஹேட் ஹெவி ரெயின் இன் யுவர் டவுன்?"),
                BookDialogueLine("A", "நபர் 1", "Not much rain, it rained lightly. In the morning it was very cold.", "ஜாஸ்தி மழை இல்லே, லேசா பெய்தது. காலையிலே ரொம்ப குளிரா இருந்தது.", "jāsthi maṛe ille, lēsa peidhadhu. kālele romba kuḷirā irundhadhu.", "நாட் மச் ரெயின், இட் ரெயின்டு லைட்லி. இன் தி மார்னிங் இட் வாஸ் வெரி கோல்டு.")
            )
        ),
        BookDialogueLesson(
            id = "dialogue_56_57",
            lessonNumber = 56,
            titleEnglish = "Lessons 56 & 57: Tailor & Cloth Merchant",
            titleTamil = "பாடங்கள் 56 & 57: தையல்காரர் மற்றும் துணிக்கடை உரையாடல்",
            settingTamil = "சட்டை தைத்தல், அளவு எடுத்தல் மற்றும் புடவை/துணி வாங்குதல்",
            vocabulary = listOf(
                BookVocabItem("தையல் கூலி / திருப்தி / அளவு", "thaiyal kūli / thrupti / aḷavu", "Tailoring charge / Satisfaction / Measurement", "டெய்லரிங் சார்ஜ் / சாட்டிஸ்ஃபேக்ஷன் / மெஷர்மென்ட்"),
                BookVocabItem("பட்டு புடவை / நூல் புடவை / கெட்டி சாயம்", "pattu pudave / nūl pudave / getti sāyam", "Silk saree / Cotton saree / Fast colour", "சில்க் சாரி / காட்டன் சாரி / ஃபாஸ்ட் கலர்")
            ),
            lines = listOf(
                BookDialogueLine("C", "வாடிக்கையாளர்", "He needs a shirt, can you stitch it? Will you take my measurements?", "இவருக்கு ஒரு சட்டை வேணும், தைக்கமுடியுமா? என் அளவு எடுக்கிறீங்களா?", "ivarukku oru chatte vēṇum, thaikkamudiyumā? en aḷavu edukringaḷā?", "ஹி நீட்ஸ் எ ஷர்ட், கேன் யூ ஸ்டிட்ச் இட்? வில் யூ டேக் மை மெஷர்மென்ட்ஸ்?"),
                BookDialogueLine("T", "தையல்காரர்", "I can, I will stitch it and give it. Your shirt will be ready in three days.", "முடியுங்க, தைத்துத் தரேன். உங்க சட்டை மூணு நாளிலே தயாரா இருக்கும்.", "mudiyu’nga, thaiththu tharēn. unga chātte mūnu nāḷḷe thayārā irukkum.", "ஐ கேன், ஐ வில் ஸ்டிட்ச் இட் அண்ட் கிவ் இட். யுவர் ஷர்ட் வில் பி ரெடி இன் த்ரீ டேய்ஸ்."),
                BookDialogueLine("C", "வாடிக்கையாளர்", "Will the colour of this saree fade? Show me some good cotton sarees.", "இந்த புடவைக்கு சாயம் போய்விடுமா? சில நல்ல நூல் புடவைங்க காட்டுங்க.", "indha pudavekku sāyam pōividumā? sila nallā nūl selenga kāttunga.", "வில் தி கலர் ஆஃப் திஸ் சாரி ஃபேட்? ஷோ மீ சம் குட் காட்டன் சாரீஸ்."),
                BookDialogueLine("M", "கடைக்காரர்", "No, the colour will never fade. This saree has fast colour.", "இல்லேங்க, சாயம் போகவே போகாது. இந்த புடவைக்கு கெட்டி சாயம்.", "illenga, sāyam pōhavē pōhādhu. indha pudavekku getti sāyam.", "நோ, தி கலர் வில் நெவர் ஃபேட். திஸ் சாரி ஹேஸ் ஃபாஸ்ட் கலர்.")
            )
        ),
        BookDialogueLesson(
            id = "dialogue_58_61",
            lessonNumber = 58,
            titleEnglish = "Lessons 58 & 61: Fruit Merchant & Dining Table Food",
            titleTamil = "பாடங்கள் 58 & 61: பழக்கடை மற்றும் உணவு மேசை உரையாடல்",
            settingTamil = "பழங்கள் வாங்குதல் மற்றும் வீட்டுச் சமையல் / உணவு பரிமாறுதல்",
            vocabulary = listOf(
                BookVocabItem("மாம்பழம் / வாழைப்பழம் / கொய்யா", "māmbaṛam / vāṛapaṛam / goyyā", "Mango / Banana / Guava", "மேங்கோ / பனானா / குவாவா"),
                BookVocabItem("சாம்பார் / ரசம் / பொரியல் / மோர்", "sāmbār / rasam / poriyal / mōr", "Sambar / Pepper water / Fried veg / Buttermilk", "சாம்பார் / ரசம் / ஃப்ரைடு வெஜ் / பட்டர்மில்க்")
            ),
            lines = listOf(
                BookDialogueLine("C", "வாடிக்கையாளர்", "What other fruit do you have? How much is a kilo of black grapes?", "உங்களிடம் வேறென்ன பழம் இருக்குது? கருப்பு திராட்சை கிலோ என்ன விலை?", "ungaḷidam vērenna paṛam irukkudhu? karuppu dhrākshe kīlo enna vile?", "வாட் அதர் ஃபுரூட் டூ யூ ஹேவ்? ஹவ் மச் இஸ் எ கிலோ ஆஃப் பிளாக் கிரேப்ஸ்?"),
                BookDialogueLine("F", "பழக்கடைக்காரர்", "One kilo is Rs 40, but to you only, I will give them for Rs 35.", "ஒரு கிலோ நாற்பது ரூபா இருக்கும், ஆனா உங்களுக்கு தான் முப்பத்தஞ்சுக்கு கொடுக்கிறேன்.", "oru kīlo nāpaththu rūbā irukkum, ānā ungaḷukku dhān, muppaththanjukku kodukrēn.", "ஒன் கிலோ இஸ் ஃபார்ட்டி ருபீஸ், பட் டூ யூ ஒன்லி, ஐ வில் கிவ் தெம் ஃபார் தர்ட்டி-ஃபைவ் ருபீஸ்."),
                BookDialogueLine("H", "வீட்டுக்காரர்", "What did you cook today? Do we have pepper water also?", "இண்ணைக்கு என்ன சமையல் செய்தீங்க? மிளகு ரசம் கூட இருக்குதா?", "iṇṇakki enna samayal seidhinga? miḷahu rasam kūda irukkudhā?", "வாட் டிட் யூ குக் டுடே? டூ வி ஹேவ் பெப்பர் வாட்டர் ஆல்சோ?"),
                BookDialogueLine("K", "சமையல்காரர்", "Today, brinjal curry and fried potatoes. The chapatis and dhal curry are excellent.", "இண்ணைக்கு கத்திரிக்காய் சாம்பார், உருளைக்கிழங்கு பொரியல். சப்பாத்தியும் பருப்பு மசியலும் ரொம்ப ஜோரா இருக்குது.", "iṇṇakki kaththarikkāi sāmbār, uruḷaikkiṛangu poriyal. chappāththiyum paruppu masiyalum romba jōrā irukkudhu.", "டுடே, பிரிஞ்சால் கறி அண்ட் ஃப்ரைடு பொட்டேட்டோஸ். தி சப்பாத்தீஸ் அண்ட் தால் கறி ஆர் எக்ஸலன்ட்.")
            )
        ),
        BookDialogueLesson(
            id = "dialogue_62",
            lessonNumber = 62,
            titleEnglish = "Lesson 62: Conversation at a Railway Station",
            titleTamil = "பாடம் 62: ரயில் நிலையத்தில் உரையாடல்",
            settingTamil = "ரயில் டிக்கெட், பிளாட்பாரம் மற்றும் பயணம் குறித்த உரையாடல்",
            vocabulary = listOf(
                BookVocabItem("ரயில் வண்டி / பிரயாணி / சாமான்", "rail vaṇdi / prayāṇi / sāmān", "Train / Traveller / Luggage", "ட்ரெயின் / டிராவலர் / லக்கேஜ்"),
                BookVocabItem("நேர் வண்டி / தங்குமிடம்", "nēr vandi / thangumidam", "Through (direct) train / Waiting room", "டைரக்ட் ட்ரெயின் / வெயிட்டிங் ரூம்")
            ),
            lines = listOf(
                BookDialogueLine("A", "பயணி 1", "To which place are you going? How long does it take to go to Delhi from here?", "நீங்க எந்த ஊருக்கு போறீங்க? இங்கிருந்து டெல்லிக்கு போக எவ்வளவு நேரம் பிடிக்குது?", "nīnga endha ūrukku pōringa? ingerundhu dellikku pōha evḷavu nēram pidikkudhu?", "டூ விச் பிளேஸ் ஆர் யூ கோயிங்? ஹவ் லாங் டஸ் இட் டேக் டூ கோ டூ டெல்லி ஃபிரம் ஹியர்?"),
                BookDialogueLine("B", "பயணி 2", "I am going to Delhi. It takes 36 hours to go to Delhi.", "நான் டெல்லிக்கு போறேன். டெல்லிக்கு போக முப்பத்தாறு மணி நேரம் பிடிக்குது.", "nān dellikku pōrēn. dellikku pōha muppaththāru maṇi nēram pidikkudhu.", "ஐ அம் கோயிங் டூ டெல்லி. இட் டேக்ஸ் தர்ட்டி-சிக்ஸ் ஹவர்ஸ் டூ கோ டூ டெல்லி."),
                BookDialogueLine("A", "பயணி 1", "When will the train start? How long will the train stop here?", "வண்டி எப்போ புறப்படும்? வண்டி இங்கே எவ்வளவு நேரம் நிக்குது?", "vandi eppo purappadum? vandi inge evḷavu nēram nikkudhu?", "வென் வில் தி ட்ரெயின் ஸ்டார்ட்? ஹவ் லாங் வில் தி ட்ரெயின் ஸ்டாப் ஹியர்?"),
                BookDialogueLine("B", "பயணி 2", "It is time, climb in quickly! Here it will stop for ten minutes.", "நேரம் ஆச்சு, சீக்கிரம் ஏறுங்க! இங்கே பத்து நிமிஷம் நிக்கும்.", "nēram āchchu, sīkram ērunga! inge paththu nimisham nikkum.", "இட் இஸ் டைம், கிளைம்ப் இன் குயிக்லி! ஹியர் இட் வில் ஸ்டாப் ஃபார் டென் மினிட்ஸ்.")
            )
        )
    )

    val shortStories: List<BookStory> = listOf(
        BookStory(
            id = "story_1",
            storyNumber = 1,
            titleRomanized = "Vāththum Pon Mutteyum",
            titleTamil = "1. வாத்தும் பொன் முட்டையும்",
            titleEnglish = "The Goose and the Golden Eggs",
            moralTamil = "பேராசை பெரு நஷ்டம் தரும்.",
            moralEnglish = "Greed brings great loss.",
            vocabulary = listOf(
                BookVocabItem("பொன் / பேராசை / நஷ்டம்", "pon / pērāse / nashtam", "Gold / Greed / Loss", "கோல்டு / கிரீட் / லாஸ்"),
                BookVocabItem("முட்டை இடு / விபரீத யோசனை", "mutte idu / viparīdha yōsane", "Lay an egg / Perverse (foolish) idea", "லே ஆன் எக் / ஃபூலிஷ் ஐடியா")
            ),
            paragraphs = listOf(
                BookSentenceItem(
                    english = "In a village there was a farmer. He was very poor and had only a little money.",
                    tamilSpoken = "ஒரு ஊரிலே ஒரு குடியானவன் இருந்தான். அவன் ரொம்ப ஏழை. அவனிடம் கொஞ்சம்தான் பணம் இருந்தது.",
                    romanizedTamil = "oru urle oru kudiyānavan irundhān. avan romba ēre. avanidam konjamdhān paṇam irundhadhu.",
                    englishPronunciationTamil = "இன் எ வில்லேஜ் தேர் வாஸ் எ ஃபார்மர். ஹி வாஸ் வெரி புவர் அண்ட் ஹேட் ஒன்லி எ லிட்டில் மணி."
                ),
                BookSentenceItem(
                    english = "With that money he bought a goose. That goose laid one golden egg every day.",
                    tamilSpoken = "அவன் அந்த பணத்தைக்கொண்டு ஒரு வாத்து வாங்கினான். அந்த வாத்து தினந்தோறும் ஒரு பொன் முட்டை இட்டு வந்தது.",
                    romanizedTamil = "avan andha paṇaththekondu oru vāththu vānginān. andha vāththu dhinandhōrum oru pon mutte ittu vandhadhu.",
                    englishPronunciationTamil = "வித் தட் மணி ஹி பாட் எ கூஸ். தட் கூஸ் லெய்டு ஒன் கோல்டன் எக் எவ்ரி டே."
                ),
                BookSentenceItem(
                    english = "By selling the golden eggs, he became a rich man. One day a foolish idea occurred to him.",
                    tamilSpoken = "அவன் பொன் முட்டைகளை விற்று பணக்காரன் ஆனான். ஒரு நாள் ஒரு விபரீத யோசனை தோணினது.",
                    romanizedTamil = "avan pon muttengaḷe viththu paṇakkāran ānān. oru nāḷ oru viparīdha yōsane thōninadhu.",
                    englishPronunciationTamil = "பை செல்லிங் தி கோல்டன் எக்ஸ், ஹி பிகேம் எ ரிச் மேன். ஒன் டே எ ஃபூலிஷ் ஐடியா அக்கர்டு டூ ஹிம்."
                ),
                BookSentenceItem(
                    english = "Out of greed, he brought a knife and cut open its stomach. But inside there was only one golden egg! Greed brings great loss.",
                    tamilSpoken = "அவன் பேராசையாலே ஒரு கத்தியை கொண்டுவந்து அதன் வயிற்றை கிழித்துப் பார்த்தான். ஆனா உள்ளே ஒரே ஒரு பொன் முட்டைதான் இருந்தது! பேராசை பெரு நஷ்டம் தரும்.",
                    romanizedTamil = "avan pērāseyāle oru kaththiye konduvandhu adhan vayiththe kiṛiththu pārththān. ānā uḷḷe orē oru pon muttedhān irundhadhu! pērāse peru nashtam tharum.",
                    englishPronunciationTamil = "அவுட் ஆஃப் கிரீட், ஹி பிராட் எ நைஃப் அண்ட் கட் ஓப்பன் இட்ஸ் ஸ்டமக். பட் இன்சைடு தேர் வாஸ் ஒன்லி ஒன் கோல்டன் எக்! கிரீட் பிரிங்ஸ் கிரேட் லாஸ்."
                )
            )
        ),
        BookStory(
            id = "story_2",
            storyNumber = 2,
            titleRomanized = "Paṇakkāranum Nambikkai Uḷḷa Nāyum",
            titleTamil = "2. பணக்காரனும் நம்பிக்கை உள்ள நாயும்",
            titleEnglish = "The Rich Man and the Faithful Dog",
            moralTamil = "ஆத்திரக்காரனுக்கு புத்தி மட்டு.",
            moralEnglish = "A person of hasty temper has short wit.",
            vocabulary = listOf(
                BookVocabItem("நம்பிக்கை உள்ள / இளைப்பாறு", "nambikkai uḷḷa / iḷaippāru", "Faithful / Rest", "ஃபெய்த்ஃபுல் / ரெஸ்ட்"),
                BookVocabItem("ஆத்திரம் / பணப்பை", "āththiram / paṇappai", "Hasty temper / Money purse", "ஹேஸ்டி டெம்பர் / மணி பர்ஸ்")
            ),
            paragraphs = listOf(
                BookSentenceItem(
                    english = "Once upon a time there was a rich man who had a beautiful dog and a good horse.",
                    tamilSpoken = "முன் ஒரு காலத்திலே ஒரு பணக்காரன் இருந்தான். அவனுக்கு ஒரு அழகான நாயும் நல்ல குதிரை ஒண்ணும் இருந்தது.",
                    romanizedTamil = "mun oru kālathle oru paṇakkāran irundhān. avanukku oru aṛahāna nāyum nalla kudhire oṇṇum irundhadhu.",
                    englishPronunciationTamil = "ஒன்ஸ் அப்பான் எ டைம் தேர் வாஸ் எ ரிச் மேன் ஹூ ஹேட் எ பியூட்டிஃபுல் டாக் அண்ட் எ குட் ஹார்ஸ்."
                ),
                BookSentenceItem(
                    english = "After resting under a banyan tree, he forgot his money purse under the tree. His dog barked non-stop to warn him.",
                    tamilSpoken = "ஆலமரத்துக்கு கீழே உட்கார்ந்து இளைப்பாறினான். பணப்பையை மரத்துக்கு கீழே விட்டுவிட்டான். அவன் நாய் ஓயாமே குரைத்தது.",
                    romanizedTamil = "ālamaraththukku kīṛe ukkārndhu iḷaippārinān. than paṇappaiye maraththukku kīṛe vittuvittadhu. nāi ōyāme kulaiththadhu.",
                    englishPronunciationTamil = "ஆஃப்டர் ரெஸ்டிங் அண்டர் எ பானியன் ட்ரீ, ஹி ஃபார்காட் ஹிஸ் மணி பர்ஸ் அண்டர் தி ட்ரீ. ஹிஸ் டாக் பார்க்ட் நான்-ஸ்டாப் டூ வார்ன் ஹிம்."
                ),
                BookSentenceItem(
                    english = "In hasty anger, he shot the dog. Later he remembered his purse, ran back, and saw the faithful dog lying dead guarding his purse!",
                    tamilSpoken = "அவன் ஆத்திரத்திலே நாயை சுட்டுவிட்டான். பிறகு பணப்பை ஞாபகத்துக்கு வந்தது. திரும்பி வந்து பார்த்தால், நம்பிக்கை உள்ள நாய் பணப்பை மேலேயே செத்து கிடந்தது!",
                    romanizedTamil = "avan āththiraththāle nāye suttuvittu pōivittān.avanukku paṇappai nyābaththukku vandhadhu. nambikkai uḷḷa nāi avan paṇappai mēleyē sethu kidandhadhu!",
                    englishPronunciationTamil = "இன் ஹேஸ்டி ஆங்கர், ஹி ஷாட் தி டாக். லேட்டர் ஹி ரிமெம்பர்டு ஹிஸ் பர்ஸ், ரேன் பேக், அண்ட் சா தி ஃபெய்த்ஃபுல் டாக் லையிங் டெட் கார்டிங் ஹிஸ் பர்ஸ்!"
                )
            )
        ),
        BookStory(
            id = "story_3",
            storyNumber = 3,
            titleRomanized = "Thoppi Vyābāriyum Kurangaḷum",
            titleTamil = "3. தொப்பி வியாபாரியும் குரங்குகளும்",
            titleEnglish = "The Cap Merchant and the Monkeys",
            moralTamil = "ஆபத்தில் சமயோசித புத்தி (Clever thinking) உதவும்.",
            moralEnglish = "Quick wit solves difficult problems.",
            vocabulary = listOf(
                BookVocabItem("தொப்பி / நிழல் / குரங்கு", "thoppi / niṛal / kurangu", "Cap / Shade / Monkey", "கேப் / ஷேடு / மங்கி"),
                BookVocabItem("யோசனை செய் / சேர்த்து", "yōsane sei / sērththu", "Think of a plan / Having collected", "திங்க் ஆஃப் எ பிளான் / ஹேவிங் கலெக்டட்")
            ),
            paragraphs = listOf(
                BookSentenceItem(
                    english = "A cap merchant rested in the shade of a tree and fell asleep. Many monkeys on the tree took all his caps and wore them.",
                    tamilSpoken = "ஒரு தொப்பி வியாபாரி மரத்து நிழலிலே உட்கார்ந்து தூங்கிவிட்டான். மரத்திலே இருந்த குரங்குங்க எல்லா தொப்பிகளையும் எடுத்து தலையிலே போட்டுக்கொண்டது.",
                    romanizedTamil = "oru vyābāri maraththu niṛalle ukkārndhu thūngivittān. marathle irundha kurangunga thoppingaḷe eduththu thale mēle pōttukondhadhu.",
                    englishPronunciationTamil = "எ கேப் மெர்ச்சன்ட் ரெஸ்டட் இன் தி ஷேடு ஆஃப் எ ட்ரீ அண்ட் ஃபெல் அஸ்லீப். மெனி மங்கீஸ் ஆன் தி ட்ரீ டுக் ஆல் ஹிஸ் கேப்ஸ் அண்ட் வோர் தெம்."
                ),
                BookSentenceItem(
                    english = "He thought for a moment, took off the cap on his own head, and threw it down on the ground.",
                    tamilSpoken = "அவன் கொஞ்ச நேரம் யோசனை செய்தான். தன் தலையிலிருந்த தொப்பியை எடுத்து குரங்குங்க பார்க்கும்படி கீழே போட்டான்.",
                    romanizedTamil = "avan konja nēram yōsane seidhān. than thaleyilirundha thoppiye eduththu kurangunga pārkkumbadi kīṛe pōttān.",
                    englishPronunciationTamil = "ஹி தாட் ஃபார் எ மொமென்ட், டுக் ஆஃப் தி கேப் ஆன் ஹிஸ் ஓன் ஹெட், அண்ட் த்ரூ இட் டவுன் ஆன் தி கிரவுண்ட்."
                ),
                BookSentenceItem(
                    english = "Seeing this, all the monkeys imitated him and threw their caps down! He collected all his caps happily and left.",
                    tamilSpoken = "இதை பார்த்த குரங்குகளும் அப்படியே தங்கள் தலையிலிருந்த தொப்பியை எடுத்து கீழே போட்டதுங்க! வியாபாரி சந்தோஷமா எல்லா தொப்பிகளையும் சேர்த்து எடுத்துப் போனான்.",
                    romanizedTamil = "idhe parththa kurangugaḷum apdiyē thanga thale mēlirundha thoppiye eduththa kīṛe pōttadhunga! vyābāri ellā thoppingaḷum sērththu eduththukkondu pōivittān.",
                    englishPronunciationTamil = "சீயிங் திஸ், ஆல் தி மங்கீஸ் இமிடேட்டட் ஹிம் அண்ட் த்ரூ தேர் கேப்ஸ் டவுன்! ஹி கலெக்டட் ஆல் ஹிஸ் கேப்ஸ் ஹேப்பிலி அண்ட் லெஃப்ட்."
                )
            )
        ),
        BookStory(
            id = "story_4",
            storyNumber = 4,
            titleRomanized = "Oththumedhān Balam",
            titleTamil = "4. ஒத்துமைதான் பலம்",
            titleEnglish = "Unity is Strength (The Four Bulls and the Lion)",
            moralTamil = "ஒத்துமைதான் பலம் (ஒற்றுமையே வலிமை).",
            moralEnglish = "Unity is strength.",
            vocabulary = listOf(
                BookVocabItem("ஒத்துமை / பலம் / சிங்கம்", "oththume / balam / singam", "Unity / Strength / Lion", "யூனிட்டி / ஸ்ட்ரென்த் / லயன்"),
                BookVocabItem("விரோதம் / தனித்தனியா", "virōdham / thani-thaniyā", "Enmity (quarrel) / Separately", "என்மிட்டி / செப்பரேட்லி")
            ),
            paragraphs = listOf(
                BookSentenceItem(
                    english = "Near a forest lived four bulls who were very friendly and always stayed together.",
                    tamilSpoken = "ஒரு காட்டுக்கு கிட்ட நாலு மாடுங்க ரொம்ப சிநேகமா ஒண்ணா இருந்தது.",
                    romanizedTamil = "oru kāttukku kitta nālu mādunga romba snēhidamā oṇṇā irundhadhu.",
                    englishPronunciationTamil = "நியர் எ ஃபாரஸ்ட் லிவ்டு ஃபோர் புல்ஸ் ஹூ வர் வெரி ஃபிரண்ட்லி அண்ட் ஆல்வேஸ் ஸ்டேய்டு டுகெதர்."
                ),
                BookSentenceItem(
                    english = "Whenever a lion tried to attack one bull, the other three charged at the lion together, so the lion ran away.",
                    tamilSpoken = "சிங்கம் ஒரு மாட்டு மேலே பாய்ந்தபோது, மத்த மூணு மாடுகளும் சிங்கத்தை நோக்கி பாய்ந்தது. சிங்கம் ஓடிப்போய்விட்டது.",
                    romanizedTamil = "singam oru māttu mēle pāindhadhu. maththa mūnu madungaḷum singaththe nōkki pāindhadhu. singam ōdipōivittadhu.",
                    englishPronunciationTamil = "வென்னெவர் எ லயன் ட்ரைடு டூ அட்டாக் ஒன் புல், தி அதர் த்ரீ சார்ஜ்டு அட் தி லயன் டுகெதர், சோ தி லயன் ரேன் அவே."
                ),
                BookSentenceItem(
                    english = "One day a quarrel arose among the four bulls and they grazed separately. The lion killed them one by one. Unity is strength!",
                    tamilSpoken = "ஒரு சமயம் நாலு மாடுகளுக்கும் விரோதம் ஏற்பட்டு தனித்தனியா மேயத் தொடங்கினதுங்க. சிங்கம் மாடுகளை ஒவ்வொண்ணா அடித்து தின்னது. ஒத்துமைதான் பலம்!",
                    romanizedTamil = "nālu mādungaḷukkum virōdham ērpattu than-thaniyā pul mēya thodanginadhunga. singam mādungaḷe ovoṇṇa adithu thinnadhu. oththumedhān balam!",
                    englishPronunciationTamil = "ஒன் டே எ குவாரல் அரோஸ் அமங் தி ஃபோர் புல்ஸ் அண்ட் தே கிரேஸ்டு செப்பரேட்லி. தி லயன் கில்டு தெம் ஒன் பை ஒன். யூனிட்டி இஸ் ஸ்ட்ரென்த்!"
                )
            )
        ),
        BookStory(
            id = "story_5",
            storyNumber = 5,
            titleRomanized = "Thaiyalkāranum Yāneyum",
            titleTamil = "5. தையல்காரனும் யானையும்",
            titleEnglish = "The Tailor and the Elephant",
            moralTamil = "கெடுவான் கேடு நினைப்பான்.",
            moralEnglish = "He who thinks evil for others brings ruin upon himself.",
            vocabulary = listOf(
                BookVocabItem("யானை / தும்பிக்கை / ஊசி", "yāne / thumbikkai / ūsi", "Elephant / Trunk / Needle", "எலிஃபன்ட் / ட்ரங்க் / நீடில்"),
                BookVocabItem("சாக்கடை / கேடு", "sākkade / kēdu", "Gutter (muddy water) / Evil (harm)", "கட்டர் / ஈவில்")
            ),
            paragraphs = listOf(
                BookSentenceItem(
                    english = "An elephant used to walk past a tailor's shop every day to bathe in the river.",
                    tamilSpoken = "ஒரு பட்டணத்திலே ஒரு தையல்காரன் கடை வழியா ஒரு யானை தினந்தோறும் குளிக்க ஆத்துக்குப் போவது வழக்கம்.",
                    romanizedTamil = "oru pattaṇathle oru thayalkaran kade vaṛiyā oru yāne dhinandhōrum kuḷikka āththukku pōvadhu vaṛakkam.",
                    englishPronunciationTamil = "ஆன் எலிஃபன்ட் யூஸ்டு டூ வாக் பாஸ்ட் எ டெய்லர்ஸ் ஷாப் எவ்ரி டே டூ பேத் இன் தி ரிவர்."
                ),
                BookSentenceItem(
                    english = "One day the tailor hid a needle inside a coconut and gave it to the elephant. The needle pricked the elephant's mouth painfully.",
                    tamilSpoken = "ஒரு நாள் தையல்காரன் தேங்காய்க்கு உள்ளே ஒரு ஊசியை வைத்து யானைக்கு கொடுத்தான். ஊசி அதன் வாய்க்கு உள்ளே குத்திக்கொண்டது.",
                    romanizedTamil = "oru dhinam thaiyalkāran oru thēngāikku uḷḷe oru ūsiye vaiththu andha yāne thinna koduththān. ūsi adhan vaikku uḷḷe kuththikkondhadhu.",
                    englishPronunciationTamil = "ஒன் டே தி டெய்லர் ஹிட் எ நீடில் இன்சைடு எ கோக்கனட் அண்ட் கேவ் இட் டூ தி எலிஃபன்ட். தி நீடில் பிரிக்ட் தி எலிஃபன்ட்ஸ் மௌத் பெயின்ஃபுல்லி."
                ),
                BookSentenceItem(
                    english = "After bathing, the elephant sucked dirty water in its trunk and sprayed it on the tailor's face! He who plans harm for others suffers harm himself.",
                    tamilSpoken = "யானை குளித்துவிட்டு வந்து, அழுக்கு தண்ணியை தன் தும்பிக்கையாலே உறிஞ்சி தையல்காரன் முகத்து மேலே அடித்தது! கெடுவான் கேடு நினைப்பான்.",
                    romanizedTamil = "yāne kuḷiththuvittu vandhu, aṛukku sākkade thaṇṇiye than thumbikkaiyāle urinji thaiyalkāran muhaththu mēle adiththadhu! keduvān kēdu nineppān.",
                    englishPronunciationTamil = "ஆஃப்டர் பேதிங், தி எலிஃபன்ட் சக்ட் டர்ட்டி வாட்டர் இன் இட்ஸ் ட்ரங்க் அண்ட் ஸ்பிரேய்டு இட் ஆன் தி டெய்லர்ஸ் ஃபேஸ்! ஹி ஹூ பிளான்ஸ் ஹார்ம் ஃபார் அதர்ஸ் சஃபர்ஸ் ஹார்ம் ஹிம்செல்ஃப்."
                )
            )
        ),
        BookStory(
            id = "story_6",
            storyNumber = 6,
            titleRomanized = "Owvaiyār Yennum Pulavar",
            titleTamil = "6. ஔவையார் என்னும் புலவர் (சுட்ட பழம் கதை)",
            titleEnglish = "Avvaiyar the Poetess and the Jamoon Boy",
            moralTamil = "கற்றது கைம்மண் அளவு, கல்லாதது உலகளவு.",
            moralEnglish = "True wisdom comes with humility; even a child can teach a great scholar.",
            vocabulary = listOf(
                BookVocabItem("புலவர் / நாவல் பழம் / அறிவாளி", "pulavar / nāval paṛam / arivāḷi", "Poet / Jamoon fruit / Wise person", "போயட் / ஜாமூன் ஃபுரூட் / வைஸ் பர்சன்"),
                BookVocabItem("சுட்ட பழம் / சுடாத பழம் / ஊது", "sutta paṛam / sudādha paṛam / ūdhu", "Hot (sand-coated) fruit / Unheated fruit / Blow", "ஹாட் ஃபுரூட் / அன்ஹீட்டட் ஃபுரூட் / ப்ளோ")
            ),
            paragraphs = listOf(
                BookSentenceItem(
                    english = "Many years ago in Tamil Nadu lived the famous poetess Avvaiyar. One hot day, tired and thirsty, she rested under a jamoon tree.",
                    tamilSpoken = "தமிழ் நாட்டிலே ஔவையார் என்னும் பெண் புலவர் இருந்தார். ஒரு நாள் வெயிலிலே களைப்பா ஒரு நாவல் மரத்துக்கு கீழே நின்றார்.",
                    romanizedTamil = "thamiṛ nātle owaiyār yennum peṇ pulavar irundhār. veyyil romba kadumeyā irundhadhu, nāval maram kīṛe nindrār.",
                    englishPronunciationTamil = "மெனி இயர்ஸ் அகோ இன் தமிழ்நாடு லிவ்டு தி ஃபேமஸ் போயட்டஸ் அவ்வையார். ஒன் ஹாட் டே, டயர்ட் அண்ட் தர்ஸ்டி, ஷி ரெஸ்டட் அண்டர் எ ஜாமூன் ட்ரீ."
                ),
                BookSentenceItem(
                    english = "She asked a boy on the tree for some fruit. The clever boy asked: 'Grandmother, do you want hot fruit or unheated fruit?'",
                    tamilSpoken = "மரத்திலே இருந்த பையனை சில நாவல் பழங்களை போடும்படி கேட்டார். பையன், 'பாட்டி, உங்களுக்கு சுட்ட பழம் வேணுமா, சுடாத பழம் வேணுமா?' என்று கேட்டான்.",
                    romanizedTamil = "andha marathle oru paiyan irundhān. 'pātti, ungaḷukku sutta paṛam vēṇumā, suttādha paṛam vēṇumā' innu kēttān.",
                    englishPronunciationTamil = "ஷி ஆஸ்க்ட் எ பாய் ஆன் தி ட்ரீ ஃபார் சம் ஃபுரூட். தி கிளெவர் பாய் ஆஸ்க்ட்: 'கிராண்ட்மதர், டூ யூ வான்ட் ஹாட் ஃபுரூட் ஆர் அன்ஹீட்டட் ஃபுரூட்?'"
                ),
                BookSentenceItem(
                    english = "He shook the branch, and sand stuck to the fallen fruit. When Avvaiyar blew 'phoo-phoo' to remove the sand, the boy laughed and said: 'Grandmother, is the fruit very hot?'",
                    tamilSpoken = "பையன் மரக்கிளையை ஆட்டினான், கீழே விழுந்த பழங்களிலே மணல் ஒட்டிக்கொண்டது. ஔவையார் வாயாலே 'பூ-பூ' என்று ஊதினார். பையன் சிரித்து, 'ஏ பாட்டி, பழம் ரொம்ப சூடா இருக்குதா?' என்றான்!",
                    romanizedTamil = "nāval paṛangaḷḷe maṇal ottikondhadhu. owaiyār than vāyāle 'phū-phū' inna ūdhinār. 'ē pātti, paṛam romba sūdā irukkudhā?' innu kēttān!",
                    englishPronunciationTamil = "ஹி ஷுக் தி பிரான்ச், அண்ட் சாண்ட் ஸ்டக் டூ தி ஃபாலன் ஃபுரூட். வென் அவ்வையார் ப்ளூ 'ஃபூ-ஃபூ' டூ ரிமூவ் தி சாண்ட், தி பாய் லாஃப்டு அண்ட் செட்: 'கிராண்ட்மதர், இஸ் தி ஃபுரூட் வெரி ஹாட்?'"
                )
            )
        )
    )

    val grammarAtAGlance: List<GrammarGlanceItem> = listOf(
        GrammarGlanceItem("Question Suffix (-ā)", "-ஆ (-ā)", "Lesson 2", "இது பேனாவா? (idhu pēnāvā?)", "Is this a pen?", "இஸ் திஸ் எ பென்?"),
        GrammarGlanceItem("Polite Imperative (-nga)", "-ங்க (-nga)", "Lesson 6", "உள்ளே வாங்க, உக்காருங்க.", "Please come in, please sit down.", "ப்ளீஸ் கம் இன், ப்ளீஸ் சிட் டவுன்."),
        GrammarGlanceItem("Negative Imperative (Don't)", "-ஆதீங்க / -க்காதீங்க", "Lesson 7", "வேகமா நடக்காதீங்க.", "Do not walk fast.", "டூ நாட் வாக் ஃபாஸ்ட்."),
        GrammarGlanceItem("Accusative (Object -e/-ye)", "-ஐ / -யை (-e / -ye)", "Lesson 9", "அவரை கூப்பிடுங்க.", "Please call him.", "ப்ளீஸ் கால் ஹிம்."),
        GrammarGlanceItem("Possessive (Genitive)", "-உடைய (-udeya)", "Lesson 10", "இது என்னுடைய புத்தகம்.", "This is my book.", "திஸ் இஸ் மை புக்."),
        GrammarGlanceItem("Infinitive ('to do')", "-அ / -க்க (-a / -kka)", "Lesson 12", "சாப்பிட வாங்க.", "Come to eat.", "கம் டூ ஈட்."),
        GrammarGlanceItem("Dative ('to / for')", "-க்கு / -க்காக (-ukku)", "Lesson 13", "எனக்கு தண்ணி வேணும்.", "I want water.", "ஐ வான்ட் வாட்டர்."),
        GrammarGlanceItem("Present Tense (Positive)", "-றேன் / -க்கிறேன்", "Lesson 16", "நான் பாடம் படிக்கிறேன்.", "I am studying the lesson.", "ஐ அம் ஸ்டடியிங் தி லெசன்."),
        GrammarGlanceItem("Locative ('in / at')", "-லே (-le / -idathle)", "Lesson 19, 28", "அவர் ஆஸ்பத்திரியிலே இருக்கிறார்.", "He is at the hospital.", "ஹி இஸ் அட் தி ஹாஸ்பிடல்."),
        GrammarGlanceItem("Future Positive ('will')", "-வேன் / -ப்பேன்", "Lesson 21", "நான் நாளைக்கு வருவேன்.", "I will come tomorrow.", "ஐ வில் கம் டுமாரோ."),
        GrammarGlanceItem("Present/Past Negative", "Infinitive + -லே (-le)", "Lesson 22", "நான் நேத்து வரலே.", "I did not come yesterday.", "ஐ டிட் நாட் கம் யெஸ்டர்டே."),
        GrammarGlanceItem("Permission ('may')", "Infinitive + -லாம் (-lām)", "Lesson 22", "நீங்க இப்போ போகலாம்.", "You may go now.", "யூ மே கோ நவ்."),
        GrammarGlanceItem("Future Negative ('won't')", "Infinitive + -மாட்டேன்", "Lesson 24", "நான் அங்கே போகமாட்டேன்.", "I will not go there.", "ஐ வில் நாட் கோ தேர்."),
        GrammarGlanceItem("Should & Should Not", "-ணும் / -கூடாது", "Lesson 25", "நீங்க நல்லா படிக்கணும்.", "You should study well.", "யூ ஷுட் ஸ்டடி வெல்."),
        GrammarGlanceItem("Accompaniment ('with')", "-ஓடு (-ōdu)", "Lesson 26", "என்னோடு பேசுங்க.", "Speak with me.", "ஸ்பீக் வித் மீ."),
        GrammarGlanceItem("Ablative ('from') & Can", "-இலிருந்து / முடியும்", "Lesson 27", "நீங்க நல்லா தமிழ் பேசமுடியும்.", "You can speak Tamil well.", "யூ கேன் ஸ்பீக் தமிழ் வெல்."),
        GrammarGlanceItem("Past Tense (5 Medials)", "-த்த் / -த் / -ந்த் / -இன் / -ட்ட்", "Lessons 30–37", "நான் பார்த்தேன் / வந்தேன் / சாப்பிட்டேன்.", "I saw / I came / I ate.", "ஐ சா / ஐ கேம் / ஐ ஏட்."),
        GrammarGlanceItem("Continuous & Perfect", "-கொண்டிருக்கிறேன் / -இருக்கிறேன்", "Lesson 40", "நான் எழுதிக் கொண்டிருக்கிறேன்.", "I am writing now.", "ஐ அம் ரைட்டிங் நவ்."),
        GrammarGlanceItem("Conditional ('if / even if')", "-ஆல் / -ஆலும் (-ā / -ālum)", "Lessons 41–42", "மழை பெய்தாலும் நான் வருவேன்.", "Even if it rains I will come.", "ஈவன் இஃப் இட் ரெயின்ஸ் ஐ வில் கம்."),
        GrammarGlanceItem("Comparison ('than / best')", "-விட / எல்லாரையும் விட", "Lesson 44", "இதைவிட அது நல்லது.", "That is better than this.", "தட் இஸ் பெட்டர் தேன் திஸ்."),
        GrammarGlanceItem("Time Clauses ('when/as soon as')", "-போது / -உடனே / -வரைக்கும்", "Lesson 47", "நான் வந்தவுடனே நீங்க போகலாம்.", "As soon as I come you may go.", "ஆஸ் சூன் ஆஸ் ஐ கம் யூ மே கோ."),
        GrammarGlanceItem("Reason ('because / so that')", "-படியாலே / -படி", "Lesson 48", "நல்லா புரியும்படி எழுதுங்க.", "Write clearly so that everyone understands.", "ரைட் கிளியர்லி சோ தட் எவ்ரிஒன் அண்டர்ஸ்டாண்ட்ஸ்.")
    )
}
