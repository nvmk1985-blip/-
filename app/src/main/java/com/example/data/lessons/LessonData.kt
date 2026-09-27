package com.example.data.lessons

data class LessonPhrase(
    val english: String,
    val tamil: String,
    val tanglish: String,
    val explanation: String = ""
)

data class LessonCategory(
    val id: String,
    val titleEnglish: String,
    val titleTamil: String,
    val iconName: String,
    val descriptionTamil: String,
    val phrases: List<LessonPhrase>
)

data class CommonMistake(
    val wrong: String,
    val correct: String,
    val tamilMeaning: String,
    val whyTamil: String
)

data class GrammarRule(
    val title: String,
    val titleTamil: String,
    val summaryTamil: String,
    val formula: String,
    val examples: List<LessonPhrase>,
    val proTipTamil: String
)

data class PronunciationExercise(
    val id: String,
    val title: String,
    val soundFocus: String,
    val soundFocusTamil: String,
    val text: String,
    val tamilMeaning: String,
    val tipTamil: String
)

data class RoleplayScenario(
    val id: String,
    val title: String,
    val titleTamil: String,
    val botRole: String,
    val userRole: String,
    val starterMessage: String,
    val starterTamil: String
)

object LessonDataSource {

    val categories = listOf(
        LessonCategory(
            id = "greetings",
            titleEnglish = "Self Introduction & Greetings",
            titleTamil = "சுய அறிமுகம் & வாழ்த்துகள்",
            iconName = "Handshake",
            descriptionTamil = "புதிய நபர்களை சந்திக்கும் போது தைரியமாக பேசுங்கள்",
            phrases = listOf(
                LessonPhrase(
                    english = "Hi, nice to meet you. I am Karthik.",
                    tamil = "வணக்கம், உங்களை சந்தித்ததில் மகிழ்ச்சி. நான் கார்த்திக்.",
                    tanglish = "Hi, nais tu meet yu. Ai am Karthik.",
                    explanation = "புதிய நபரிடம் பேச தொடங்கும் போது 'Nice to meet you' என கூறலாம்."
                ),
                LessonPhrase(
                    english = "Where are you from?",
                    tamil = "நீங்கள் எந்த ஊர் / எங்கிருந்து வருகிறீர்கள்?",
                    tanglish = "Ver aar yu phram?",
                    explanation = "அவர் வசிக்கும் இடத்தை கேட்க உதவும் எளிய வாக்கியம்."
                ),
                LessonPhrase(
                    english = "I work as a software engineer in Chennai.",
                    tamil = "நான் சென்னையில் மென்பொருள் பொறியாளராக பணிபுரிகிறேன்.",
                    tanglish = "Ai vork az a saaftver enjineeyar in Chennai.",
                    explanation = "'I work as a...' என்று உங்களின் தொழிலை எளிதாக கூறலாம்."
                ),
                LessonPhrase(
                    english = "Could you tell me a little bit about yourself?",
                    tamil = "உங்களை பற்றி கொஞ்சம் சொல்ல முடியுமா?",
                    tanglish = "Kud yu tel mi a litil bit abowt yorself?",
                    explanation = "மரியாதையாக பிறரை பற்றி கேட்க 'Could you tell me' என தொடங்குங்கள்."
                ),
                LessonPhrase(
                    english = "Have a great day ahead!",
                    tamil = "இன்றைய நாள் உங்களுக்கு இனிதாக அமையட்டும்!",
                    tanglish = "Haev a greyt dey ahed!",
                    explanation = "விடைபெறும் போது வாழ்த்து சொல்ல அருமையான வாக்கியம்."
                )
            )
        ),
        LessonCategory(
            id = "tea_food",
            titleEnglish = "Tea Stall & Restaurant",
            titleTamil = "டீக்கடை & உணவகம்",
            iconName = "Coffee",
            descriptionTamil = "உணவகங்களில் சரளமாக ஆர்டர் செய்வது எப்படி?",
            phrases = listOf(
                LessonPhrase(
                    english = "One strong tea with less sugar, please.",
                    tamil = "சர்க்கரை கம்மியா ஒரு ஸ்ட்ராங் டீ குடுங்க, ப்ளீஸ்.",
                    tanglish = "Van straang tee vidh les shugar, pleez.",
                    explanation = "சுவையாகவும் மரியாதையாகவும் ஆர்டர் செய்ய 'please' சேருங்கள்."
                ),
                LessonPhrase(
                    english = "Could I see the menu card?",
                    tamil = "மெனு கார்டு கொஞ்சம் பார்க்கலாமா?",
                    tanglish = "Kud ai see the menyu kaard?",
                    explanation = "ஹோட்டலில் நுழைந்ததும் மெனு கேட்கும் வாக்கியம்."
                ),
                LessonPhrase(
                    english = "What do you recommend here?",
                    tamil = "இங்கு எது ரொம்ப நல்லா இருக்கும்? (பரிந்துரைப்பீர்கள்)",
                    tanglish = "Vaat du yu rekamend heer?",
                    explanation = "சிறந்த உணவை பரிந்துரைக்க சர்வரைக் கேட்கலாம்."
                ),
                LessonPhrase(
                    english = "Could you please pack this for takeaway?",
                    tamil = "இதை பார்சல் பண்ணி தர முடியுமா?",
                    tanglish = "Kud yu pleez paek this phor teykavey?",
                    explanation = "பார்சல் கேட்பதற்கு 'takeaway' அல்லது 'to-go' என ஆங்கிலத்தில் சொல்வார்கள்."
                ),
                LessonPhrase(
                    english = "Could we get the bill, please?",
                    tamil = "பில் கொண்டுவர முடியுமா?",
                    tanglish = "Kud vee get the bil, pleez?",
                    explanation = "சாப்பிட்டு முடித்த பின் பில் கேட்கும் நாகரீகமான முறை."
                )
            )
        ),
        LessonCategory(
            id = "travel",
            titleEnglish = "Travel, Auto & Bus",
            titleTamil = "பஸ், ஆட்டோ & ரயில் பயணம்",
            iconName = "DirectionsBus",
            descriptionTamil = "பயணங்களில் வழிகேட்க மற்றும் டிக்கெட் எடுக்க",
            phrases = listOf(
                LessonPhrase(
                    english = "Does this bus go to Central Railway Station?",
                    tamil = "இந்த பஸ் சென்ட்ரல் ரயில் நிலையத்திற்கு போகுமா?",
                    tanglish = "Daz this bas go tu Sentral Reyilvey Steyshan?",
                    explanation = "ஒரு வாகனம் குறிப்பிட்ட இடத்திற்கு செல்லுமா என உறுதிப்படுத்த."
                ),
                LessonPhrase(
                    english = "How much for T. Nagar by meter?",
                    tamil = "டி.நகருக்கு மீட்டருக்கு எவ்வளவு ஆகும்?",
                    tanglish = "How mach phor T. Nagar bai meetar?",
                    explanation = "ஆட்டோ அல்லது டாக்சியில் கட்டணம் கேட்க."
                ),
                LessonPhrase(
                    english = "Please let me know when my stop arrives.",
                    tamil = "என் நிறுத்தம் வரும்போது கொஞ்சம் சொல்லுங்க.",
                    tanglish = "Pleez let mi no ven mai staap araivs.",
                    explanation = "கண்டக்டரிடம் ஸ்டாப் சொல்லுமாறு வேண்டுகோள் வைக்க."
                ),
                LessonPhrase(
                    english = "Excuse me, which platform does the Bangalore train leave from?",
                    tamil = "மன்னிக்கவும், பெங்களூர் ரயில் எந்த பிளாட்பாரத்திலிருந்து புறப்படும்?",
                    tanglish = "Ekskyooz mi, vich plaatform daz the Bangalore treyn leev phram?",
                    explanation = "ரயில் நிலையத்தில் பிளாட்பாரத்தை விசாரிக்க."
                ),
                LessonPhrase(
                    english = "How far is the nearest metro station from here?",
                    tamil = "இங்கிருந்து மிக அருகில் உள்ள மெட்ரோ நிலையம் எவ்வளவு தூரம்?",
                    tanglish = "How phaar iz the neyarest metro steyshan phram heer?",
                    explanation = "'How far' என்றால் 'எவ்வளவு தூரம்' என்று அர்த்தம்."
                )
            )
        ),
        LessonCategory(
            id = "office",
            titleEnglish = "Office & Professional English",
            titleTamil = "அலுவலகம் & வேலை வாய்ப்பு",
            iconName = "BusinessCenter",
            descriptionTamil = "வேலை செய்யும் இடத்தில் தன்னம்பிக்கையுடன் பேச",
            phrases = listOf(
                LessonPhrase(
                    english = "Could we schedule a quick call to discuss this?",
                    tamil = "இதை பற்றி விவாதிக்க ஒரு சிறிய கால் ஏற்பாடு செய்யலாமா?",
                    tanglish = "Kud vee skejyool a kvik kaal tu diskas this?",
                    explanation = "அலுவலகத்தில் மீட்டிங் திட்டமிட அருமையான வாக்கியம்."
                ),
                LessonPhrase(
                    english = "I will share the updated report by end of the day.",
                    tamil = "இன்று மாலைக்குள் புதுப்பிக்கப்பட்ட அறிக்கையை பகிர்ந்து கொள்கிறேன்.",
                    tanglish = "Ai vil sheyr the apdeyted riport bai end aav the dey.",
                    explanation = "'End of the day' (EOD) என்பது பணி முடியும் மாலை நேரத்தை குறிக்கும்."
                ),
                LessonPhrase(
                    english = "Could you please elaborate on that point?",
                    tamil = "அந்த விஷயத்தை பற்றி கொஞ்சம் விளக்கமாக சொல்ல முடியுமா?",
                    tanglish = "Kud yu pleez elaaboreyt aan that paaint?",
                    explanation = "புரியாத ஒன்றை மேலும் விளக்க கேட்க 'elaborate' பயன்படுத்தலாம்."
                ),
                LessonPhrase(
                    english = "I am writing to request one day of leave tomorrow.",
                    tamil = "நாளை ஒரு நாள் விடுமுறை வேண்டி இந்த மின்னஞ்சலை எழுதுகிறேன்.",
                    tanglish = "Ai am raiting tu rikvest van dey aav leev tumaarro.",
                    explanation = "விடுமுறை கேட்கும் போது இவ்வாறு கூறலாம்."
                ),
                LessonPhrase(
                    english = "Thank you for your valuable feedback.",
                    tamil = "உங்களின் பயனுள்ள கருத்துக்களுக்கு மிக்க நன்றி.",
                    tanglish = "Thaenk yu phor yor vaelyoobul pheedbaek.",
                    explanation = "பிறர் விமர்சனம் அல்லது ஆலோசனை தந்தால் நன்றி சொல்ல."
                )
            )
        ),
        LessonCategory(
            id = "daily_talk",
            titleEnglish = "Everyday Situations",
            titleTamil = "தினசரி உரையாடல்கள்",
            iconName = "Chat",
            descriptionTamil = "நண்பர்கள் மற்றும் அண்டை வீட்டாருடன் பேச",
            phrases = listOf(
                LessonPhrase(
                    english = "What's going on? How have you been?",
                    tamil = "என்ன விசேஷம்? எப்படி இருக்கீங்க?",
                    tanglish = "Vaats goying aan? How haev yu been?",
                    explanation = "நெருங்கிய நண்பர்களிடம் நலம் விசாரிக்க உதவும் பேச்சு வழக்கு."
                ),
                LessonPhrase(
                    english = "I am a bit tied up right now. Can I call you back?",
                    tamil = "இப்போ கொஞ்சம் வேலையா மாட்டிக்கிட்டு இருக்கேன். அப்புறம் கூப்பிடவா?",
                    tanglish = "Ai am a bit taiyd ap rait now. Kaen ai kaal yu baek?",
                    explanation = "'Tied up' என்றால் பரபரப்பாக அல்லது வேலையாக இருத்தல்."
                ),
                LessonPhrase(
                    english = "Don't worry about it, it happens to everyone.",
                    tamil = "கவலைப்படாதீங்க, இது எல்லாருக்கும் நடப்பது தான்.",
                    tanglish = "Dont vurri abowt it, it haepens tu evrivan.",
                    explanation = "ஒருவரை ஆறுதல்படுத்த மிகச்சிறந்த ஆங்கில வாக்கியம்."
                ),
                LessonPhrase(
                    english = "I didn't quite catch what you said. Could you repeat?",
                    tamil = "நீங்க சொன்னது சரியா விளங்கல. திரும்ப சொல்ல முடியுமா?",
                    tanglish = "Ai didint kvait kaetch vaat yu sed. Kud yu ripeet?",
                    explanation = "பேச்சு புரியாத போது 'I didn't catch' என சொல்லலாம்."
                )
            )
        )
    )

    val commonMistakes = listOf(
        CommonMistake(
            wrong = "I am having two brothers.",
            correct = "I have two brothers.",
            tamilMeaning = "எனக்கு இரண்டு சகோதரர்கள் உள்ளனர்.",
            whyTamil = "தமிழில் 'எனக்கு இருக்கு' என்பதை ஆங்கிலத்தில் சொல்ல 'having' போடக்கூடாது. உடைமை அல்லது உறவுக்கு 'have' மட்டுமே போதும்."
        ),
        CommonMistake(
            wrong = "Did you ate breakfast?",
            correct = "Did you eat breakfast?",
            tamilMeaning = "நீ காலை உணவு சாப்பிட்டாயா?",
            whyTamil = "'Did' ஏற்கனவே கடந்த காலம் (Past). எனவே Did-க்கு பிறகு வரும் வினைச்சொல் (Verb) அடிப்படை வடிவில் (eat) தான் இருக்க வேண்டும்."
        ),
        CommonMistake(
            wrong = "He is my cousin brother.",
            correct = "He is my cousin.",
            tamilMeaning = "அவன் என் பெரியப்பா/சித்தப்பா/மாமா மகன்.",
            whyTamil = "ஆங்கிலத்தில் 'Cousin' என்ற சொல்லே ஆணா பெண்ணா என காட்டும். 'Cousin brother' அல்லது 'Cousin sister' என்பது தவறான இந்திய வழக்கு."
        ),
        CommonMistake(
            wrong = "What is your good name?",
            correct = "May I know your name, please?",
            tamilMeaning = "உங்கள் பெயர் என்ன?",
            whyTamil = "தமிழில் 'சுப நாமம்' / 'நல்ல பெயர்' என்று கேட்பதை ஆங்கிலத்தில் 'good name' என நேரடி மொழிபெயர்ப்பு செய்யக்கூடாது."
        ),
        CommonMistake(
            wrong = "I will revert back to you.",
            correct = "I will revert to you. / I will get back to you.",
            tamilMeaning = "நான் உங்களை மீண்டும் தொடர்பு கொள்கிறேன்.",
            whyTamil = "'Revert' என்றாலே மீண்டும் பதில் தருவது என்றுதான் பொருள். கூடவே 'back' போடுவது தேவையில்லாத கூடுதல் சொல்."
        ),
        CommonMistake(
            wrong = "Today morning I woke up early.",
            correct = "This morning I woke up early.",
            tamilMeaning = "இன்று காலை நான் சீக்கிரம் எழுந்தேன்.",
            whyTamil = "ஆங்கிலத்தில் 'Today morning' என கூறாமல் 'This morning' அல்லது 'Yesterday night' என்பதற்கு பதில் 'Last night' என கூறவேண்டும்."
        )
    )

    val grammarRules = listOf(
        GrammarRule(
            title = "Past, Present & Future Made Easy",
            titleTamil = "காலங்கள் (Tenses) எளிய தமிழில்",
            summaryTamil = "தமிழில் நாம் பேசுவது போன்றே ஆங்கிலத்திலும் 3 முக்கிய நிலைகள் உள்ளன",
            formula = "Past (நேற்று) -> Did / Went | Present (இன்று) -> Do / Go | Future (நாளை) -> Will go",
            examples = listOf(
                LessonPhrase(
                    english = "I went to the market yesterday.",
                    tamil = "நேற்று நான் சந்தைக்கு சென்றேன்.",
                    tanglish = "Ai vent tu the maarket yestardey."
                ),
                LessonPhrase(
                    english = "I go to office by train every day.",
                    tamil = "நான் தினமும் ரயிலில் அலுவலகம் செல்கிறேன்.",
                    tanglish = "Ai go tu aaphis bai treyn evridey."
                ),
                LessonPhrase(
                    english = "I will meet you tomorrow at 5 PM.",
                    tamil = "நாளை மாலை 5 மணிக்கு உங்களை சந்திக்கிறேன்.",
                    tanglish = "Ai vil meet yu tumaarro aet phayv PM."
                )
            ),
            proTipTamil = "குறிப்பு: எதிர்காலத்திற்கு (Future) யோசிக்காமல் 'will' சேர்த்துக் கொண்டால் எளிதாக பேசிவிடலாம்!"
        ),
        GrammarRule(
            title = "Do / Does / Did - The Question Formula",
            titleTamil = "கேள்வி கேட்கும் எளிய பார்முலா",
            summaryTamil = "யாருடன் பேசுகிறீர்கள் என்பதை வைத்து Do, Does, Did முடிவெடுக்கலாம்",
            formula = "I/You/We/They -> DO | He/She/It -> DOES | கடந்த காலம் (Past) -> DID",
            examples = listOf(
                LessonPhrase(
                    english = "Do you speak English?",
                    tamil = "நீ ஆங்கிலம் பேசுகிறாயா?",
                    tanglish = "Du yu speek Inglish?"
                ),
                LessonPhrase(
                    english = "Does he live in Madurai?",
                    tamil = "அவன் மதுரையில் வசிக்கிறானா?",
                    tanglish = "Daz hee liv in Madurai?"
                ),
                LessonPhrase(
                    english = "Did you understand what I said?",
                    tamil = "நான் சொன்னது உனக்கு புரிந்ததா?",
                    tanglish = "Did yu andarstaand vaat ai sed?"
                )
            ),
            proTipTamil = "Do அல்லது Does கொண்டு கேள்வி கேட்கும்போது வினைச்சொல்லில் 's' சேர்க்க கூடாது (Does he lives ❌ -> Does he live ✅)."
        )
    )

    val pronunciationExercises = listOf(
        PronunciationExercise(
            id = "vw_sound",
            title = "V vs W Distinction",
            soundFocus = "/v/ (பற்கள் உதட்டை தொட வேண்டும்) vs /w/ (உதடுகள் குவிய வேண்டும்)",
            soundFocusTamil = "V ஒலிக்கும் போது மேல் பற்கள் கீழ் உதட்டில் பட வேண்டும்",
            text = "Very well, we will visit the village on Wednesday.",
            tamilMeaning = "மிக நல்லது, நாங்கள் புதன்கிழமை அன்று கிராமத்திற்கு செல்வோம்.",
            tipTamil = "'Very' சொல்லும்போது V ஒலி, 'Well' சொல்லும்போது W உதட்டுக் குவிப்பு!"
        ),
        PronunciationExercise(
            id = "pf_sound",
            title = "P vs F Sound",
            soundFocus = "/p/ (இரு உதடுகளும் மூடி திறக்க வேண்டும்) vs /f/ (காற்று உராய வேண்டும்)",
            soundFocusTamil = "காற்றை மெதுவாக ஊதி F ஒலிக்க வேண்டும்",
            text = "Four friends prepared fresh fruit juice perfectly.",
            tamilMeaning = "நான்கு நண்பர்கள் புதிய பழச்சாற்றை மிகச்சரியாக தயாரித்தனர்.",
            tipTamil = "'Fruit' சொல்லும்போது 'புரூட்' என உதட்டை மூடாமல் 'ஃப்ரூட்' என காற்றை வெளிவிடுங்கள்."
        ),
        PronunciationExercise(
            id = "s_cluster",
            title = "Initial 'S' Cluster (School, Station)",
            soundFocus = "No 'E' or 'I' sound before 'S' (Not 'Iskool')",
            soundFocusTamil = "'இஸ்கூல்' என சொல்லாமல் 'ஸ்கூல்' என 'ஸ்ஸ்ஸ்' ஒலியுடன் தொடங்கவும்",
            text = "Smart students speak softly at school and station.",
            tamilMeaning = "புத்திசாலி மாணவர்கள் பள்ளியிலும் ரயில் நிலையத்திலும் மென்மையாக பேசுகிறார்கள்.",
            tipTamil = "வார்த்தையின் ஆரம்பத்தில் 'இ' சத்தம் வராமல் பாம்பின் 'ஸ்ஸ்' சத்தத்துடன் தொடங்குங்கள்."
        ),
        PronunciationExercise(
            id = "th_sound",
            title = "The 'TH' Sound (Think vs This)",
            soundFocus = "/θ/ and /ð/ (நாக்கு பற்களுக்கு நடுவே வர வேண்டும்)",
            soundFocusTamil = "நாக்கின் நுனியை லேசாக பற்களுக்கு இடையே வைக்க வேண்டும்",
            text = "They think that this Thursday is their mother's birthday.",
            tamilMeaning = "இந்த வியாழக்கிழமை அவர்களுடைய அம்மாவின் பிறந்தநாள் என்று அவர்கள் நினைக்கிறார்கள்.",
            tipTamil = "'They', 'This' சொல்லும்போது 'தே', 'திஸ்' என்று தடித்த தமிழ்த் 'த' போடாமல் மென்மையாக பற்களுக்கு நடுவே நாக்கை வைக்கவும்."
        )
    )

    val roleplayScenarios = listOf(
        RoleplayScenario(
            id = "interview",
            title = "Job Interview Warmup",
            titleTamil = "வேலை நேர்காணல் பயிற்சி",
            botRole = "HR Manager (Mr. Raghav)",
            userRole = "Job Candidate",
            starterMessage = "Good morning! Welcome to the interview. Could you please start by introducing yourself?",
            starterTamil = "காலை வணக்கம்! நேர்காணலுக்கு நல்வரவு. உங்களை அறிமுகம் செய்து தொடங்குங்கள்."
        ),
        RoleplayScenario(
            id = "coffee_shop",
            title = "At the Coffee Shop",
            titleTamil = "காபி கடையில் ஆர்டர் செய்தல்",
            botRole = "Barista (Cafe Staff)",
            userRole = "Customer",
            starterMessage = "Hello! Welcome to Cafe Aroma. What can I get started for you today?",
            starterTamil = "வணக்கம்! கஃபே அரோமாவிற்கு நல்வரவு. இன்று உங்களுக்கு என்ன ஆர்டர் கொண்டுவரட்டும்?"
        ),
        RoleplayScenario(
            id = "doctor",
            title = "Doctor Consultation",
            titleTamil = "மருத்துவரிடம் பேசுதல்",
            botRole = "Doctor",
            userRole = "Patient",
            starterMessage = "Hello, please take a seat. What brings you in today? How are you feeling?",
            starterTamil = "வணக்கம், உட்காருங்கள். இன்று என்ன உடல்நிலை பிரச்சினை? எப்படி உணர்கிறீர்கள்?"
        )
    )
}
