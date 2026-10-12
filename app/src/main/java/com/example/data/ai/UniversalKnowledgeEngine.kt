package com.example.data.ai

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.sqrt

/**
 * Comprehensive offline/instant Knowledge, Q&A, Math, Grammar, Translation, GK, Science & Story Engine.
 * Used when Gemini API key is not configured or network is unreachable so that
 * the Voice Bot STILL answers ANY question asked in Tamil, English, or Tanglish.
 */
object UniversalKnowledgeEngine {

    data class KnowledgeEntry(
        val keywords: List<String>,
        val english: String,
        val tamil: String,
        val tanglish: String,
        val tip: String
    )

    private val knowledgeBase: List<KnowledgeEntry> = listOf(
        // --- SCIENCE & NATURE ---
        KnowledgeEntry(
            keywords = listOf("sky blue", "வானம் ஏன் நீல", "வானம் நீலம்", "why is the sky blue", "color of sky"),
            english = "The sky looks blue because sunlight has many colors, and Earth's atmosphere scatters blue light in all directions more than other colors. This is called Rayleigh scattering!",
            tamil = "சூரிய ஒளியில் ஏழு நிறங்கள் உள்ளன. பூமியின் வளிமண்டலத்தில் உள்ள காற்று மூலக்கூறுகள் நீல நிற ஒளியை மற்ற நிறங்களை விட அதிகமாகச் சிதறடிக்கின்றன (Rayleigh Scattering). அதனால்தான் வானம் நீல நிறமாகத் தெரிகிறது!",
            tanglish = "தி ஸ்கை லுக்ஸ் ப்ளூ பிகாஸ் சன்லைட் ஹேஸ் மெனி கலர்ஸ், அண்ட் எர்த்ஸ் அட்மாஸ்பியர் ஸ்கேட்டர்ஸ் ப்ளூ லைட் இன் ஆல் டைரக்ஷன்ஸ்!",
            tip = "'Why is the sky blue?' என்று ஆங்கிலத்தில் காரணத்தைக் கேட்க 'Why is...?' என்ற அமைப்பைப் பயன்படுத்துங்கள்."
        ),
        KnowledgeEntry(
            keywords = listOf("photosynthesis", "ஒளிச்சேர்க்கை", "plants make food", "தாவரங்கள் உணவு"),
            english = "Photosynthesis is the process by which green plants use sunlight, water, and carbon dioxide to make their own food (glucose) and release oxygen for us to breathe!",
            tamil = "ஒளிச்சேர்க்கை (Photosynthesis) என்பது பச்சைத் தாவரங்கள் சூரிய ஒளி, நீர் மற்றும் கார்பன் டை ஆக்சைடைப் பயன்படுத்தி தமக்குத் தேவையான உணவைத் தயாரித்து, நாம் சுவாசிக்க ஆக்சிஜனை வெளியிடும் நிகழ்வாகும்!",
            tanglish = "ஃபோட்டோசிந்தசிஸ் இஸ் தி பிராசஸ் பை விச் கிரீன் பிளான்ட்ஸ் யூஸ் சன்லைட், வாட்டர், அண்ட் கார்பன் டை ஆக்சைடு டூ மேக் தேர் ஓன் ஃபுட்!",
            tip = "அறிவியல் சொல்: Photosynthesis (ஃபோட்டோசிந்தசிஸ்) = ஒளிச்சேர்க்கை. Oxygen (ஆக்சிஜன்) = உயிர்வளி."
        ),
        KnowledgeEntry(
            keywords = listOf("solar system", "planets", "சூரிய குடும்பம்", "கோள்கள்", "how many planets", "எத்தனை கோள்கள்"),
            english = "There are 8 planets in our Solar System orbiting the Sun: Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, and Neptune. Jupiter is the largest planet, and Earth is our home!",
            tamil = "நமது சூரிய குடும்பத்தில் சூரியனைச் சுற்றி 8 கோள்கள் உள்ளன: புதன் (Mercury), வெள்ளி (Venus), பூமி (Earth), செவ்வாய் (Mars), வியாழன் (Jupiter), சனி (Saturn), யுரேனஸ் (Uranus), நெப்டியூன் (Neptune). இதில் வியாழன் மிகப்பெரிய கோள் ஆகும்!",
            tanglish = "தேர் ஆர் எய்ட் பிளானட்ஸ் இன் அவர் சோலார் சிஸ்டம் ஆர்பிட்டிங் தி சன்: மெர்குரி, வீனஸ், எர்த், மார்ஸ், ஜூபிட்டர், சேட்டர்ன், யுரேனஸ், அண்ட் நெப்டியூன்.",
            tip = "8 கோள்களை நினைவில் வைக்க: 'My Very Educated Mother Just Served Us Noodles' என்ற வாக்கியத்தின் முதல் எழுத்துக்களைப் பயன்படுத்தலாம்!"
        ),
        KnowledgeEntry(
            keywords = listOf("water cycle", "rain", "மழை எப்படி", "நீர் சுழற்சி", "how does it rain", "மழை வருவது எப்படி"),
            english = "Rain happens through the Water Cycle! Heat from the Sun evaporates water from oceans and rivers into vapor. It rises, cools into clouds (condensation), and falls back as rain (precipitation).",
            tamil = "மழை 'நீர் சுழற்சி' (Water Cycle) மூலம் பெய்கிறது! சூரிய வெப்பத்தால் கடல் மற்றும் ஆற்று நீர் ஆவியாகி (Evaporation) மேலே சென்று, குளிர்ந்து மேகமாக மாறி (Condensation), பின்னர் மழையாகப் பொழிகிறது (Precipitation)!",
            tanglish = "ரெயின் ஹேப்பன்ஸ் த்ரூ தி வாட்டர் சைக்கிள்! ஹீட் ஃப்ரம் தி சன் எவாப்பரேட்ஸ் வாட்டர் இன்டு கிளவுட்ஸ் அண்ட் ஃபால்ஸ் பேக் அஸ் ரெயின்.",
            tip = "Evaporation = ஆவியாதல், Condensation = குளிர்வித்தல், Precipitation = மழைப்பொழிவு."
        ),
        KnowledgeEntry(
            keywords = listOf("gravity", "ஈர்ப்பு விசை", "புவியீர்ப்பு", "newton", "நியூட்டன்"),
            english = "Gravity is the invisible pulling force that attracts objects toward each other. Earth's gravity keeps us on the ground and makes things fall down when dropped. Sir Isaac Newton discovered gravity!",
            tamil = "புவியீர்ப்பு விசை (Gravity) என்பது பொருட்களைத் தன் நோக்கி இழுக்கும் கண்ணுக்குத் தெரியாத விசையாகும். பூமியின் ஈர்ப்பு விசைதான் நம்மைத் தரையில் நிற்க வைக்கிறது. இதை சர் ஐசக் நியூட்டன் கண்டுபிடித்தார்!",
            tanglish = "கிராவிட்டி இஸ் தி இன்விசிபிள் புல்லிங் ஃபோர்ஸ் தட் கீப்ஸ் அஸ் ஆன் தி கிரவுண்ட். சர் ஐசக் நியூட்டன் டிஸ்கவர்ட் கிராவிட்டி!",
            tip = "'Gravity keeps our feet on the ground' என்றால் 'புவியீர்ப்பு விசை நம் கால்களைத் தரையில் நிலைநிறுத்துகிறது' என்று பொருள்."
        ),
        KnowledgeEntry(
            keywords = listOf("rainbow", "வானவில்", "seven colors", "ஏழு நிறங்கள்"),
            english = "A rainbow appears when sunlight passes through raindrops in the sky. It has 7 colors — VIBGYOR: Violet, Indigo, Blue, Green, Yellow, Orange, and Red!",
            tamil = "மழைத்துளிகள் வழியாக சூரிய ஒளி ஊடுருவும்போது வானவில் தோன்றுகிறது. வானவில்லில் 7 நிறங்கள் உள்ளன (VIBGYOR): ஊதா, கருநீலம், நீலம், பச்சை, மஞ்சள், ஆரஞ்சு மற்றும் சிவப்பு!",
            tanglish = "எ ரெயின்போ அப்பியர்ஸ் வென் சன்லைட் பாஸஸ் த்ரூ ரெயின்டிராப்ஸ். இட் ஹேஸ் செவன் கலர்ஸ்: வயலட், இண்டிகோ, ப்ளூ, கிரீன், எல்லோ, ஆரஞ்சு, அண்ட் ரெட்!",
            tip = "VIBGYOR என்ற சுருக்கத்தின் மூலம் வானவில்லின் 7 நிறங்களையும் ஆங்கிலத்தில் எளிதாகச் சொல்லலாம்!"
        ),
        KnowledgeEntry(
            keywords = listOf("human body", "bones", "heart", "brain", "மனித உடல்", "எலும்புகள்", "இதயம்", "மூளை"),
            english = "An adult human body has 206 bones! Our heart beats about 72 times per minute to pump blood, and the brain is the control center that helps us think, learn, and remember.",
            tamil = "வளர்ந்த மனித உடலில் 206 எலும்புகள் உள்ளன! நமது இதயம் நிமிடத்திற்கு சுமார் 72 முறை துடித்து இரத்தத்தை உடல் முழுவதும் பாய்ச்சுகிறது. மூளை நமது உடலின் கட்டுப்பாட்டு மையமாகச் செயல்படுகிறது!",
            tanglish = "அன் அடல்ட் ஹியூமன் பாடி ஹேஸ் டூ ஹண்ட்ரட் அண்ட் சிக்ஸ் போன்ஸ்! அவர் ஹார்ட் பீட்ஸ் அபௌட் செவன்ட்டி டூ டைம்ஸ் பெர் மினிட்.",
            tip = "Bones = எலும்புகள், Heart = இதயம், Brain = மூளை, Lungs = நுரையீரல்."
        ),

        // --- INDIA, TAMIL NADU & GREAT LEADERS ---
        KnowledgeEntry(
            keywords = listOf("kalam", "abdul kalam", "அப்துல் கலாம்", "missile man", "ஏவுகணை நாயகன்"),
            english = "Dr. A.P.J. Abdul Kalam was the 11th President of India and a great aerospace scientist known as the 'Missile Man of India'. Born in Rameswaram, Tamil Nadu, he inspired millions of children to dream big!",
            tamil = "டாக்டர் ஏ.பி.ஜே. அப்துல் கலாம் இந்தியாவின் 11-வது குடியரசுத் தலைவரும், 'இந்தியாவின் ஏவுகணை நாயகன்' எனப் போற்றப்படும் மாபெரும் விஞ்ஞானியும் ஆவார். தமிழ்நாட்டின் இராமேஸ்வரத்தில் பிறந்த அவர், மாணவர்களைக் கனவு காணத் தூண்டினார்!",
            tanglish = "டாக்டர் ஏ.பி.ஜே. அப்துல் கலாம் வாஸ் தி லெவன்த் பிரசிடெண்ட் ஆஃப் இந்தியா அண்ட் தி மிசைல் மேன் ஆஃப் இந்தியா. ஹி வாஸ் பார்ன் இன் ராமேஸ்வரம்!",
            tip = "கலாம் ஐயாவின் பொன்மொழி: 'Dream, dream, dream. Dreams transform into thoughts and thoughts result in action!'"
        ),
        KnowledgeEntry(
            keywords = listOf("thiruvalluvar", "thirukkural", "திருக்குறள்", "திருவள்ளுவர்", "kural", "குறள்"),
            english = "Thiruvalluvar composed the Thirukkural, a classic Tamil masterpiece containing 1,330 couplets across 133 chapters on virtue (Aram), wealth (Porul), and love (Inbam). First Kural: 'Agara Mudhala Ezhuthellam Aathi Bhagavan Mudhatre Ulagu.'",
            tamil = "திருவள்ளுவர் இயற்றிய உலகப் பொதுமறையாம் 'திருக்குறள்' 133 அதிகாரங்களையும் 1330 குறட்பாக்களையும் கொண்டது. அறம், பொருள், இன்பம் எனும் முப்பால் கொண்ட இதன் முதல் குறள்: 'அகர முதல எழுத்தெல்லாம் ஆதி பகவன் முதற்றே உலகு.'",
            tanglish = "திருவள்ளுவர் கம்போஸ்ட் தி திருக்குறள், எ கிளாசிக் தமிழ் மாஸ்டர்பீஸ் கண்டெய்னிங் 1330 கப்லெட்ஸ் ஆன் விர்ச்சு, வெல்த், அண்ட் லவ்.",
            tip = "Thirukkural is called the 'Universal Veda' (உலகப் பொதுமறை) in English!"
        ),
        KnowledgeEntry(
            keywords = listOf("capital of india", "national animal", "national bird", "இந்தியாவின் தலைநகரம்", "தேசிய விலங்கு", "தேசிய பறவை"),
            english = "New Delhi is the capital of India! India's national animal is the Royal Bengal Tiger, national bird is the Peacock, national flower is the Lotus, and national river is the Ganga.",
            tamil = "இந்தியாவின் தலைநகரம் புது டெல்லி (New Delhi)! இந்தியாவின் தேசிய விலங்கு வங்காளப் புலி (Tiger), தேசியப் பறவை மயில் (Peacock), தேசிய மலர் தாமரை (Lotus), தேசிய நதி கங்கை ஆகும்!",
            tanglish = "நியூ டெல்லி இஸ் தி கேப்பிட்டல் ஆஃப் இந்தியா! இந்தியாஸ் நேஷனல் அனிமல் இஸ் தி பெங்கால் டைகர், அண்ட் நேஷனல் பேர்ட் இஸ் தி பீகாக்.",
            tip = "Capital = தலைநகரம், National Anthem = தேசிய கீதம் (ஜன கண மன), National Song = வந்தே மாதரம்."
        ),
        KnowledgeEntry(
            keywords = listOf("capital of tamil nadu", "state animal", "state bird", "தமிழ்நாட்டின் தலைநகரம்", "மாநில விலங்கு", "மாநில பறவை"),
            english = "Chennai is the capital of Tamil Nadu! Tamil Nadu's state animal is the Nilgiri Tahr (வரையாடு), state bird is the Emerald Dove (மரகதப் புறா), state tree is the Palm Tree (பனை மரம்), and state sport is Kabaddi!",
            tamil = "தமிழ்நாட்டின் தலைநகரம் சென்னை! தமிழ்நாட்டின் மாநில விலங்கு நீலகிரி வரையாடு, மாநிலப் பறவை மரகதப் புறா, மாநில மரம் பனை மரம், மாநில விளையாட்டு கபடி ஆகும்!",
            tanglish = "சென்னை இஸ் தி கேப்பிட்டல் ஆஃப் தமிழ்நாடு! தமிழ்நாடுஸ் ஸ்டேட் அனிமல் இஸ் தி நீலகிரி தார் அண்ட் ஸ்டேட் ட்ரீ இஸ் தி பாம் ட்ரீ.",
            tip = "'I am proud to be from Tamil Nadu' என்றால் 'நான் தமிழ்நாட்டைச் சேர்ந்தவன் என்பதில் பெருமை கொள்கிறேன்' என்று பொருள்."
        ),
        KnowledgeEntry(
            keywords = listOf("kamarajar", "kamaraj", "காமராஜர்", "கர்மவீரர்", "கல்வி கண் திறந்த"),
            english = "K. Kamarajar, known as 'Karmaveerar' and 'Kalvi Kan Thirantha Kamarajar', was the former Chief Minister of Tamil Nadu who introduced the free Midday Meal Scheme and opened thousands of schools across villages!",
            tamil = "'கர்மவீரர்' மற்றும் 'கல்விக் கண் திறந்த காமராஜர்' எனப் போற்றப்படும் கு. காமராஜர், தமிழ்நாட்டின் முன்னாள் முதலமைச்சர் ஆவார். ஏழைக் குழந்தைகள் படிக்க இலவச மதிய உணவுத் திட்டத்தையும் ஆயிரக்கணக்கான பள்ளிகளையும் உருவாக்கினார்!",
            tanglish = "காமராஜர், நோன் அஸ் கர்மவீரர், வாஸ் தி ஃபார்மர் சீஃப் மினிஸ்டர் ஆஃப் தமிழ்நாடு ஹூ இன்ட்ரடியூஸ்டு தி ஃப்ரீ மிட்டே மீல் ஸ்கீம்!",
            tip = "ஜூலை 15-ஆம் தேதி காமராஜரின் பிறந்தநாள் தமிழ்நாட்டில் 'கல்வி வளர்ச்சி நாள்' (Educational Development Day) ஆகக் கொண்டாடப்படுகிறது!"
        ),
        KnowledgeEntry(
            keywords = listOf("bharathiyar", "subramania bharathi", "பாரதியார்", "மகாகவி"),
            english = "Mahakavi Subramania Bharathiyar was a legendary Tamil poet, freedom fighter, and social reformer who wrote inspiring patriotic songs like 'Achamillai Achamillai' and 'Odi Vilaiyadu Papa'!",
            tamil = "மகாகவி சுப்பிரமணிய பாரதியார் இந்திய விடுதலைப் போராட்ட வீரரும் மாபெரும் தமிழ்க் கவிஞரும் ஆவார். 'அச்சமில்லை அச்சமில்லை', 'ஓடி விளையாடு பாப்பா' போன்ற எழுச்சிமிகு பாடல்களைப் பாடியவர்!",
            tanglish = "மகாகவி சுப்பிரமணிய பாரதியார் வாஸ் எ லெஜண்டரி தமிழ் போயட் அண்ட் ஃப்ரீடம் ஃபைட்டர்!",
            tip = "Poet = கவிஞர், Freedom Fighter = சுதந்திரப் போராட்ட வீரர்."
        ),
        KnowledgeEntry(
            keywords = listOf("mahatma gandhi", "gandhiji", "மகாத்மா காந்தி", "காந்திஜி", "father of our nation", "தேசப்பிதா"),
            english = "Mahatma Gandhi (Mohandas Karamchand Gandhi) is the Father of our Nation, India. Born on October 2, 1869, he led India to independence through truth (Satyagraha) and non-violence (Ahimsa)!",
            tamil = "மகாத்மா காந்தி (மோகன்தாஸ் கரம்சந்த் காந்தி) நமது இந்தியாவின் தேசப்பிதா (Father of the Nation) ஆவார். அக்டோபர் 2, 1869-இல் பிறந்த இவர், உண்மை (சத்தியாகிரகம்) மற்றும் அகிம்சை வழியில் இந்தியாவிற்குச் சுதந்திரம் பெற்றுத் தந்தார்!",
            tanglish = "மகாத்மா காந்தி இஸ் தி ஃபாதர் ஆஃப் அவர் நேஷன், இந்தியா. ஹி லெட் இந்தியா டு இன்டிபென்டன்ஸ் த்ரூ ட்ரூத் அண்ட் நான்-வயலன்ஸ்!",
            tip = "Non-violence = அகிம்சை, Independence = சுதந்திரம், Truth = உண்மை."
        ),
        KnowledgeEntry(
            keywords = listOf("computer", "what is computer", "கம்ப்யூட்டர்", "கணினி என்றால் என்ன", "கணினி"),
            english = "A computer is a smart electronic machine that takes input data, processes it quickly using its CPU (brain of the computer), and gives accurate output. Charles Babbage is called the Father of the Computer!",
            tamil = "கணினி (Computer) என்பது தகவல்களை உள்ளீடாகப் (Input) பெற்று, அதிவேகமாகச் செயல்படுத்தி (Process), துல்லியமான வெளியீட்டைத் (Output) தரும் மின்னணு இயந்திரமாகும். சார்லஸ் பாபேஜ் கணினியின் தந்தை எனப்படுகிறார்!",
            tanglish = "எ கம்ப்யூட்டர் இஸ் எ ஸ்மார்ட் எலக்ட்ரானிக் மெஷின் தட் டேக்ஸ் இன்புட் டேட்டா அண்ட் கிவ்ஸ் அக்யூரேட் அவுட்புட். சார்லஸ் பாபேஜ் இஸ் தி ஃபாதர் ஆஃப் தி கம்ப்யூட்டர்!",
            tip = "CPU = Central Processing Unit (கணினியின் மூளை), RAM = Random Access Memory."
        ),
        KnowledgeEntry(
            keywords = listOf("artificial intelligence", "what is ai", "செயற்கை நுண்ணறிவு"),
            english = "Artificial Intelligence (AI) is computer technology that enables machines to think, learn, understand languages, analyze photos, and solve problems like humans — just like Gemini 3.8 Flash!",
            tamil = "செயற்கை நுண்ணறிவு (Artificial Intelligence - AI) என்பது மனிதர்களைப் போலவே சிந்திக்கவும், மொழிகளைப் புரிந்துகொள்ளவும், படங்களை ஆராய்ந்து கேள்விகளுக்குப் பதிலளிக்கவும் கணினிகளுக்கு உதவும் நவீன தொழில்நுட்பமாகும்!",
            tanglish = "ஆர்டிஃபிஷியல் இன்டெலிஜென்ஸ் இஸ் கம்ப்யூட்டர் டெக்னாலஜி தட் எனேபிள்ஸ் மெஷின்ஸ் டு திங்க், லேர்ன், அண்ட் சால்வ் பிராப்ளம்ஸ் லைக் ஹியூமன்ஸ்!",
            tip = "AI என்பதன் விரிவாக்கம்: Artificial Intelligence (செயற்கை நுண்ணறிவு)."
        ),

        // --- ENGLISH GRAMMAR & TENSES ---
        KnowledgeEntry(
            keywords = listOf("tense", "tenses", "காலங்கள்", "past tense", "present tense", "future tense", "இறந்த காலம்", "நிகழ்காலம்", "எதிர்காலம்", "12 tenses"),
            english = "In English, there are 3 main tenses: 1) Present Tense (நிகழ்காலம் - I speak English), 2) Past Tense (இறந்தகாலம் - I spoke English yesterday), and 3) Future Tense (எதிர்காலம் - I will speak English tomorrow). Each has 4 forms, making 12 tenses!",
            tamil = "ஆங்கிலத்தில் முக்கியமாக 3 காலங்கள் (Tenses) உள்ளன:\n1. Present Tense (நிகழ்காலம்): I eat (நான் சாப்பிடுகிறேன்)\n2. Past Tense (இறந்தகாலம்): I ate (நான் சாப்பிட்டேன்)\n3. Future Tense (எதிர்காலம்): I will eat (நான் சாப்பிடுவேன்).\nஇவை ஒவ்வொன்றிலும் 4 வகைகள் என மொத்தம் 12 Tenses உள்ளன!",
            tanglish = "இன் இங்கிலீஷ், தேர் ஆர் த்ரீ மெயின் டென்சஸ்: பிரசன்ட் டென்ஸ் (ஐ ஸ்பீக்), பாஸ்ட் டென்ஸ் (ஐ ஸ்போக்), அண்ட் ஃபியூச்சர் டென்ஸ் (ஐ வில் ஸ்பீக்).",
            tip = "நேற்று நடந்ததைச் சொல்ல V2 (Past - went, ate, saw), நாளை நடப்பதைச் சொல்ல 'will + V1' (will go, will eat) பயன்படுத்துங்கள்!"
        ),
        KnowledgeEntry(
            keywords = listOf("noun", "verb", "adjective", "adverb", "pronoun", "preposition", "articles", "grammar", "இலக்கணம்", "பெயர்ச்சொல்", "வினைச்சொல்"),
            english = "Parts of Speech in English: 1) Noun (பெயர்ச்சொல் - Subi, school, book), 2) Pronoun (பிரதி பெயர்ச்சொல் - I, you, he, she), 3) Verb (வினைச்சொல் - run, read, speak), 4) Adjective (பெயரடை - smart girl), 5) Adverb (வினையடை - speak fluently).",
            tamil = "ஆங்கில இலக்கணத்தின் முக்கியப் பகுதிகள்:\n• Noun (பெயர்ச்சொல்): பெயர், இடம், பொருள் (Subi, Chennai, Book)\n• Verb (வினைச்சொல்): செயலைக் குறிப்பது (Eat, Speak, Run)\n• Adjective (பெயரடை): பெயர்ச்சொல்லை விவரிப்பது (Good, Smart, Big)\n• Articles: A, An (உயிரெழுத்து ஒலிக்கு முன்: an apple), The.",
            tanglish = "பார்ட்ஸ் ஆஃப் ஸ்பீச் இன் இங்கிலீஷ்: நவுன், புரொனவுன், வெர்ப், அட்ஜெக்டிவ், அண்ட் அட்வெர்ப்.",
            tip = "ஆங்கில வாக்கிய அமைப்பு: Subject (யார்) + Verb (என்ன செயல்) + Object (எதை) — உ.ம்: Subi (S) reads (V) a book (O)."
        ),

        // --- STORIES, RIDDLES & SPEECHES ---
        KnowledgeEntry(
            keywords = listOf("lion and mouse", "lion story", "சிங்கம் மற்றும் எலி", "எலி கதை", "moral story", "நீதிக்கதை", "tell me a story", "கதை சொல்"),
            english = "Story: The Lion and the Mouse! Once a sleeping lion caught a tiny mouse. The mouse begged, 'Please forgive me, I may help you one day!' Later, the lion was trapped in a hunter's net. The little mouse chewed the ropes and set the lion free! Moral: Even a small friend can be a big help.",
            tamil = "கதை: சிங்கமும் சுண்டெலியும்! ஒரு காட்டில் தூங்கிக்கொண்டிருந்த சிங்கத்தின் மீது சிறிய எலி ஓடியது. சிங்கம் அதைப் பிடித்தபோது, 'என்னை விட்டுவிடுங்கள், ஒருநாள் நான் உங்களுக்கு உதவுவேன்' என்றது எலி. சில நாட்களில் சிங்கம் வேடன் வலையில் சிக்கியது. உடனே அந்தச் சுண்டெலி வலையைக் கடித்து சிங்கத்தைக் காப்பாற்றியது! நீதி: சிறியவர்களின் உதவியும் பெரிய நன்மையைத் தரும்.",
            tanglish = "ஸ்டோரி: தி லயன் அண்ட் தி மவுஸ்! தி லிட்டில் மவுஸ் சியூட் தி ரோப்ஸ் அண்ட் செட் தி லயன் ஃப்ரீ! மாரல்: ஈவன் எ ஸ்மால் ஃப்ரெண்ட் கேன் பீ எ பிக் ஹெல்ப்.",
            tip = "கதை சொல்லும்போது 'Once upon a time...' (முன்னொரு காலத்தில்...) என்று தொடங்குவது ஆங்கில மரபு!"
        ),
        KnowledgeEntry(
            keywords = listOf("thirsty crow", "crow story", "காகம் கதை", "தாகம் கொண்ட காகம்"),
            english = "Story: The Thirsty Crow! On a hot day, a thirsty crow found a pitcher with very little water at the bottom. It dropped small pebbles one by one into the pitcher. The water level rose up, and the smart crow drank the water happily! Moral: Where there is a will, there is a way.",
            tamil = "கதை: தாகம் கொண்ட காகம்! ஒரு கோடை நாளில் தாகத்தால் தவித்த காகம் ஒரு பானையில் அடியில் மட்டும் சிறிது தண்ணீர் இருப்பதைக் கண்டது. புத்திசாலி காகம் சிறு கற்களை ஒவ்வொன்றாகப் பானையில் போட்டது. தண்ணீர் மேலே வந்ததும் மகிழ்ச்சியாகக் குடித்துப் பறந்தது! நீதி: முயற்சி திருவினையாக்கும் (Where there is a will, there is a way).",
            tanglish = "தி தேர்ஸ்டி குரோ டிராப்ட் ஸ்மால் பெப்பிள்ஸ் ஒன் பை ஒன் இன்டு தி பிட்சர். தி வாட்டர் ரோஸ் அப் அண்ட் தி குரோ டிராங்க் இட் ஹேப்பிலி!",
            tip = "'Where there is a will, there is a way' என்றால் 'மனம் இருந்தால் மார்க்கம் உண்டு / முயற்சி திருவினையாக்கும்'!"
        ),
        KnowledgeEntry(
            keywords = listOf("riddle", "விடுகதை", "puzzle", "புதிர்", "joke", "ஜோக்"),
            english = "Here is a fun riddle for you, Subi! 'What has hands and a face, but cannot hold anything or smile?' ... Answer: A Clock (கடிகாரம்)! Because a clock has a face and hands that show the time!",
            tamil = "உனக்காக ஒரு சுவாரசியமான விடுகதை சுபி! 'முகம் இருக்கும் ஆனால் சிரிக்க முடியாது, கைகள் இருக்கும் ஆனால் எதையும் பிடிக்க முடியாது — அது என்ன?' ... விடை: கடிகாரம் (A Clock)! ஏனென்றால் கடிகாரத்திற்கு முகப்பும் (Face), முட்களும் (Hands) உள்ளன!",
            tanglish = "வாட் ஹேஸ் ஹேண்ட்ஸ் அண்ட் எ ஃபேஸ், பட் கேன்னாட் ஹோல்ட் எனிதிங் ஆர் ஸ்மைல்? ஆன்சர்: எ கிளாக்!",
            tip = "ஆங்கிலத்தில் கடிகாரத்தின் முட்களை 'Hands of a clock' (Hour hand, Minute hand) என்று அழைப்பார்கள்!"
        ),
        KnowledgeEntry(
            keywords = listOf("self introduction", "introduce myself", "என்னை அறிமுகப்படுத்த", "சுய அறிமுகம்"),
            english = "Here is a complete Self-Introduction: 'Good morning everyone! My name is Subiksha, and you can call me Subi. I am 9 years old and studying in 4th standard. My favorite subject is English, and I love reading stories and learning new things. Thank you!'",
            tamil = "அழகான சுய அறிமுகம் (Self-Introduction): 'அனைவருக்கும் காலை வணக்கம்! என் பெயர் சுபிக்சா (சுபி). எனக்கு 9 வயது, நான் 4-ஆம் வகுப்பு படிக்கிறேன். எனக்கு மிகவும் பிடித்த பாடம் ஆங்கிலம். கதைகள் படிப்பதும் புதிய விஷயங்களைக் கற்பதும் எனக்குப் பிடிக்கும். நன்றி!'",
            tanglish = "குட் மார்னிங் எவ்ரிஒன்! மை நேம் இஸ் சுபிக்சா, அண்ட் யூ கேன் கால் மீ சுபி. ஐ அம் நைன் இயர்ஸ் ஓல்ட். மை ஃபேவரைட் சப்ஜெக்ட் இஸ் இங்கிலீஷ். தேங்க் யூ!",
            tip = "மேடையிலோ வகுப்பறையிலோ பேசத் தொடங்கும்போது புன்னகையுடன் 'Good morning everyone!' என்று தொடங்குங்கள்."
        )
    )

    // Rich Bilingual Dictionary for single-word lookup
    private val bilingualDictionary: Map<String, Pair<String, String>> = mapOf(
        "மழை" to ("Rain" to "It is raining outside. (வெளியே மழை பெய்கிறது.)"),
        "வெயில்" to ("Sunny / Sunshine" to "It is very sunny today. (இன்று வெயில் அதிகமாக உள்ளது.)"),
        "தண்ணீர்" to ("Water" to "May I have some water, please? (எனக்குக் கொஞ்சம் தண்ணீர் தருகிறீர்களா?)"),
        "சாப்பாடு" to ("Food / Meal" to "Did you have your meal? (நீங்கள் சாப்பிட்டீர்களா?)"),
        "பள்ளிக்கூடம்" to ("School" to "I go to school every day at 8 AM. (நான் தினமும் காலை 8 மணிக்கு பள்ளிக்குச் செல்கிறேன்.)"),
        "புத்தகம்" to ("Book" to "I love reading story books. (எனக்குக் கதைப் புத்தகங்கள் படிக்கப் பிடிக்கும்.)"),
        "ஆசிரியர்" to ("Teacher" to "Dhanam is my favorite English teacher. (தனம் எனக்குப் பிடித்த ஆங்கில ஆசிரியை.)"),
        "நண்பன்" to ("Friend" to "She is my best friend. (அவள் என் உயிர் தோழி.)"),
        "தோழி" to ("Friend" to "She is my best friend. (அவள் என் உயிர் தோழி.)"),
        "அம்மா" to ("Mother / Mom" to "My mother cooks delicious food. (என் அம்மா சுவையாகச் சமைப்பார்.)"),
        "அப்பா" to ("Father / Dad" to "My father is very kind and hardworking. (என் அப்பா மிகவும் அன்பானவர்.)"),
        "வீடு" to ("House / Home" to "Welcome to our home! (எங்கள் வீட்டிற்கு வரவேற்கிறோம்!)"),
        "மகிழ்ச்சி" to ("Happy / Happiness" to "I am very happy today! (நான் இன்று மிகவும் மகிழ்ச்சியாக இருக்கிறேன்!)"),
        "கோபம்" to ("Angry / Anger" to "Please don't be angry with me. (தயவுசெய்து என் மீது கோபப்படாதீர்கள்.)"),
        "பயம்" to ("Fear / Afraid" to "Do not be afraid to speak English! (ஆங்கிலம் பேசப் பயப்படாதீர்கள்!)"),
        "தூக்கம்" to ("Sleep / Sleepy" to "I am feeling sleepy now. (எனக்கு இப்போது தூக்கம் வருகிறது.)"),
        "பசி" to ("Hungry / Hunger" to "Mom, I am feeling hungry! (அம்மா, எனக்குப் பசிக்கிறது!)"),
        "தாகம்" to ("Thirsty / Thirst" to "I am very thirsty, I need water. (எனக்குத் தாகமாக இருக்கிறது, தண்ணீர் வேண்டும்.)"),
        "நேரம்" to ("Time" to "What is the time now? (இப்போது மணி என்ன?)"),
        "நாளை" to ("Tomorrow" to "See you tomorrow at school! (நாளை பள்ளியில் சந்திப்போம்!)"),
        "நேற்று" to ("Yesterday" to "Yesterday I learned ten new English words. (நேற்று நான் 10 புதிய ஆங்கில வார்த்தைகள் கற்றேன்.)"),
        "இன்று" to ("Today" to "Today is a wonderful day! (இன்று ஒரு அருமையான நாள்!)"),
        "உதவி" to ("Help" to "Could you please help me? (தயவுசெய்து எனக்கு உதவ முடியுமா?)"),
        "மன்னிப்பு" to ("Sorry / Apology" to "I am sorry for being late. (தாமதத்திற்கு மன்னிக்கவும்.)"),
        "வாழ்த்துகள்" to ("Congratulations / Best wishes" to "Congratulations on your great success! (உங்கள் வெற்றிக்கு வாழ்த்துகள்!)"),
        "வெற்றி" to ("Success / Victory" to "Hard work always brings success. (கடின உழைப்பு எப்போதும் வெற்றியைத் தரும்.)"),
        "முயற்சி" to ("Effort / Try" to "Keep trying, you will succeed! (தொடர்ந்து முயற்சி செய், நீ வெற்றி பெறுவாய்!)"),
        "கனவு" to ("Dream" to "Follow your dreams with confidence. (நம்பிக்கையுடன் உன் கனவுகளைத் தொடர்.)"),
        "அறிவு" to ("Knowledge / Wisdom" to "Knowledge is power. (அறிவே ஆற்றல்.)")
    )

    fun answerAnyQuestion(userInput: String, scenario: String? = null): TutorResponse? {
        val cleaned = userInput.trim()
        if (cleaned.isEmpty()) return null
        val lower = cleaned.lowercase()

        // 1. Check Math / Arithmetic Questions first
        val mathReply = trySolveMath(cleaned, lower)
        if (mathReply != null) return mathReply

        // 2. Check Live Date / Time / Day Questions
        if (lower.contains("what time") || lower.contains("time now") || lower.contains("மணி என்ன") ||
            lower.contains("today's date") || lower.contains("what date") || lower.contains("இன்று தேதி") ||
            lower.contains("what day is today") || lower.contains("என்ன கிழமை")
        ) {
            val now = Date()
            val timeStr = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(now)
            val dateStr = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH).format(now)
            val eng = "Right now, the time is $timeStr, and today is $dateStr!"
            return TutorResponse(
                englishText = eng,
                tamilText = "இப்போது நேரம் $timeStr மற்றும் இன்றைய தேதி $dateStr ஆகும்! ஆங்கிலத்தில் நேரம் கேட்க 'What time is it now?' என்று கேட்கலாம்.",
                tanglishText = TutorEngine.formatPronunciationInTamil(eng, ""),
                coachingTip = "நேரம் சொல்லும்போது காலை நேரத்திற்கு AM (Ante Meridiem) என்றும், மதியம் 12 மணிக்கு மேல் PM (Post Meridiem) என்றும் சொல்ல வேண்டும்."
            )
        }

        // 3. Check Knowledge Base topics (Science, GK, Grammar, Stories, Riddles, Leaders)
        for (entry in knowledgeBase) {
            if (entry.keywords.any { kw -> lower.contains(kw) }) {
                return TutorResponse(
                    englishText = entry.english,
                    tamilText = entry.tamil,
                    tanglishText = entry.tanglish,
                    coachingTip = entry.tip
                )
            }
        }

        // 4. Check Bilingual Dictionary lookup ONLY for single-word queries so full sentences/questions go to LiveWebKnowledgeEngine
        val words = cleaned.replace("?", "").trim().split(Regex("\\s+"))
        if (words.size <= 2) {
            for ((tamilWord, pair) in bilingualDictionary) {
                val (engWord, sample) = pair
                if (cleaned == tamilWord || lower == engWord.lowercase().substringBefore(" /")) {
                    val eng = "In English, '$tamilWord' is called '$engWord'. Example sentence: $sample"
                    return TutorResponse(
                        englishText = eng,
                        tamilText = "'$tamilWord' என்பதை ஆங்கிலத்தில் '$engWord' என்று சொல்வார்கள். வாக்கிய உதாரணம்: $sample",
                        tanglishText = TutorEngine.formatPronunciationInTamil(eng, ""),
                        coachingTip = "புதிய வார்த்தையைக் கற்கும்போது அதைத் தனியாகப் படிக்காமல், இதுபோல் ஒரு முழு வாக்கியத்தில் (Full Sentence) வைத்துப் பேசிப் பழகுங்கள்!"
                    )
                }
            }
        }

        return null
    }

    private fun trySolveMath(raw: String, lower: String): TutorResponse? {
        // Multiplication table request (e.g., "5 table", "5 வாய்ப்பாடு")
        val tableRegex = Regex("""(\d+)\s*(?:th\s*)?(?:table|tables|வாய்ப்பாடு)""")
        val tableMatch = tableRegex.find(lower)
        if (tableMatch != null) {
            val n = tableMatch.groupValues[1].toIntOrNull()
            if (n != null && n in 1..100) {
                val linesEn = (1..5).joinToString(", ") { "$n times $it is ${n * it}" }
                val linesFull = (1..10).joinToString(" | ") { "$n×$it=${n * it}" }
                val eng = "Here is the multiplication table of $n: $linesEn. ($linesFull)"
                return TutorResponse(
                    englishText = eng,
                    tamilText = "$n-ஆம் வாய்ப்பாடு இதோ: $linesFull. ஆங்கிலத்தில் பெருக்கல் வாய்ப்பாட்டைச் சொல்லும்போது '$n times 1 is $n, $n times 2 is ${n * 2}' என்று வாசிக்க வேண்டும்!",
                    tanglishText = TutorEngine.formatPronunciationInTamil(eng, ""),
                    coachingTip = "பெருக்கலை ஆங்கிலத்தில் '$n multiplied by 2' அல்லது சுருக்கமாக '$n times 2' என்று சொல்லலாம்!"
                )
            }
        }

        // Square root
        val sqrtRegex = Regex("""(?:square root of|sqrt|வர்க்கமூலம்)\s*(\d+(?:\.\d+)?)""")
        val sqrtMatch = sqrtRegex.find(lower)
        if (sqrtMatch != null) {
            val num = sqrtMatch.groupValues[1].toDoubleOrNull()
            if (num != null && num >= 0) {
                val res = sqrt(num)
                val formatted = if (res % 1.0 == 0.0) res.toLong().toString() else String.format(Locale.US, "%.2f", res)
                val eng = "The square root of ${sqrtMatch.groupValues[1]} is $formatted."
                return TutorResponse(
                    englishText = eng,
                    tamilText = "${sqrtMatch.groupValues[1]}-இன் வர்க்கமூலம் (Square Root) $formatted ஆகும்!",
                    tanglishText = TutorEngine.formatPronunciationInTamil(eng, ""),
                    coachingTip = "வர்க்கமூலம் = Square Root, வர்க்கம் = Square (உ.ம்: 5 squared is 25)."
                )
            }
        }

        // General arithmetic expression: e.g. 25 + 37, 15 * 12, 100 / 4, 50 - 18, "25 plus 30"
        val normalized = lower
            .replace("plus", "+").replace("கூட்டல்", "+")
            .replace("minus", "-").replace("கழித்தல்", "-")
            .replace("multiplied by", "*").replace("times", "*").replace("into", "*").replace("பெருக்கல்", "*").replace("×", "*").replace("x", "*")
            .replace("divided by", "/").replace("வகுத்தல்", "/").replace("÷", "/")

        val exprRegex = Regex("""(-?\d+(?:\.\d+)?)\s*([+\-*/])\s*(-?\d+(?:\.\d+)?)""")
        val match = exprRegex.find(normalized)
        if (match != null) {
            val a = match.groupValues[1].toDoubleOrNull() ?: return null
            val op = match.groupValues[2]
            val b = match.groupValues[3].toDoubleOrNull() ?: return null

            if (op == "/" && b == 0.0) {
                return TutorResponse(
                    englishText = "Division by zero is undefined in mathematics!",
                    tamilText = "கணிதத்தில் எந்த எண்ணையும் பூஜ்ஜியத்தால் (0) வகுக்க முடியாது (Undefined)!",
                    tanglishText = "டிவிஷன் பை ஜீரோ இஸ் அன்டிஃபைன்ட் இன் மேத்தமேட்டிக்ஸ்!",
                    coachingTip = "Zero = பூஜ்ஜியம், Division = வகுத்தல்."
                )
            }

            val result = when (op) {
                "+" -> a + b
                "-" -> a - b
                "*" -> a * b
                "/" -> a / b
                else -> return null
            }

            val aStr = if (a % 1.0 == 0.0) a.toLong().toString() else a.toString()
            val bStr = if (b % 1.0 == 0.0) b.toLong().toString() else b.toString()
            val resStr = if (result % 1.0 == 0.0) result.toLong().toString() else String.format(Locale.US, "%.2f", result)

            val (opWordEn, opWordTa) = when (op) {
                "+" -> "plus" to "கூட்டல் (+)"
                "-" -> "minus" to "கழித்தல் (-)"
                "*" -> "multiplied by" to "பெருக்கல் (×)"
                "/" -> "divided by" to "வகுத்தல் (÷)"
                else -> "plus" to ""
            }

            val eng = "The answer is $resStr! In English, we say: '$aStr $opWordEn $bStr equals $resStr.'"
            return TutorResponse(
                englishText = eng,
                tamilText = "சரியான விடை: $resStr! ($aStr $opWordTa $bStr = $resStr). ஆங்கிலத்தில் இதை '$aStr $opWordEn $bStr equals $resStr' என்று சொல்ல வேண்டும்!",
                tanglishText = TutorEngine.formatPronunciationInTamil(eng, ""),
                coachingTip = "கணிதக் குறியீடுகள் ஆங்கிலத்தில்: Plus (+), Minus (-), Multiplied by / Times (×), Divided by (÷), Equals (=)."
            )
        }

        return null
    }
}
