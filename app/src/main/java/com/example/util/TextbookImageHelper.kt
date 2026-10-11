package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import android.util.Base64
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

enum class SampleTextbookPage(
    val id: String,
    val titleTamil: String,
    val titleEnglish: String,
    val headerTag: String,
    val lines: List<String>,
    val questions: List<String>,
    val summaryEnglish: String,
    val summaryTamil: String,
    val keyWordsTamil: String
) {
    SCIENCE_PHOTOSYNTHESIS(
        id = "science_photosynthesis",
        titleTamil = "🔬 அறிவியல் பாடம்: ஒளிச்சேர்க்கை (Photosynthesis)",
        titleEnglish = "Class 4 Science • Chapter 3: How Green Plants Make Food",
        headerTag = "STANDARD IV • SCIENCE TEXTBOOK • PAGE 24",
        lines = listOf(
            "1. Green leaves are called the 'Kitchen of the Plant'.",
            "2. Leaves contain a green pigment called Chlorophyll.",
            "3. Plants take in Carbon Dioxide (CO2) from the air through tiny",
            "   pores called Stomata, and absorb Water from the soil through roots.",
            "4. Using Sunlight, leaves convert water and carbon dioxide into",
            "   food (Glucose) and release Oxygen into the air.",
            "5. Formula: Sunlight + Water + Carbon Dioxide -> Glucose + Oxygen."
        ),
        questions = listOf(
            "Q1. Which part of the plant is called the kitchen of the plant?",
            "Q2. What gas do plants release during photosynthesis?",
            "Q3. Why are leaves green in color?"
        ),
        summaryEnglish = "Textbook Page Explanation: This Class 4 Science page explains 'Photosynthesis'. Green leaves act as the kitchen of the plant using Chlorophyll, Sunlight, Water, and Carbon Dioxide to make food (Glucose) and release Oxygen. Answers to page questions: Q1) The green leaf is called the kitchen of the plant. Q2) Plants release Oxygen gas. Q3) Leaves are green because of a pigment called Chlorophyll.",
        summaryTamil = "📖 பாடப்புத்தகப் பக்க விளக்கம் (அறிவியல் - பக்கம் 24):\n• பச்சை இலைகள் 'தாவரத்தின் சமையலறை' (Kitchen of the Plant) எனப்படுகின்றன.\n• இலையில் உள்ள பச்சையம் (Chlorophyll), சூரிய ஒளி (Sunlight), நீர் (Water) மற்றும் கார்பன்-டை-ஆக்சைடு ஆகியவற்றைப் பயன்படுத்தி தாவரங்கள் உணவு (Glucose) தயாரித்து ஆக்சிஜனை (Oxygen) வெளியிடுகின்றன.\n\n✅ புத்தகக் கேள்விகளுக்கான விடைகள்:\n1. Green Leaf (பச்சை இலை)\n2. Oxygen (ஆக்சிஜன் / உயிர்வளி)\n3. Because of Chlorophyll (பச்சையம் இருப்பதால் இலைகள் பச்சையாக உள்ளன).",
        keyWordsTamil = "முக்கிய வார்த்தைகள்: Chlorophyll = பச்சையம், Stomata = இலைத்துளை, Glucose = உணவு சர்க்கரை, Oxygen = உயிர்வளி."
    ),
    ENGLISH_GRAMMAR_TENSES(
        id = "english_grammar_tenses",
        titleTamil = "📘 ஆங்கில இலக்கணம்: 3 முக்கிய காலங்கள் (Tenses)",
        titleEnglish = "Spoken English Reader • Unit 5: Present, Past & Future Tenses",
        headerTag = "SPOKEN ENGLISH TEXTBOOK • GRAMMAR UNIT 5 • PAGE 42",
        lines = listOf(
            "Rule 1: Simple Present Tense (Daily Habits & Facts)",
            "   • Subi reads a book every day. (சுபி தினமும் புத்தகம் படிக்கிறாள்)",
            "   • The sun rises in the east. (சூரியன் கிழக்கே உதிக்கிறது)",
            "Rule 2: Simple Past Tense (Completed Action Yesterday)",
            "   • Subi read a story yesterday. (சுபி நேற்று ஒரு கதை படித்தாள்)",
            "   • We went to school at 8 AM. (நாங்கள் காலை 8 மணிக்கு பள்ளிக்குச் சென்றோம்)",
            "Rule 3: Simple Future Tense (Action Tomorrow - will + Verb)",
            "   • Subi will speak English fluently. (சுபி சரளமாக ஆங்கிலம் பேசுவாள்)"
        ),
        questions = listOf(
            "Exercise 1: Change to Past Tense: 'I go to school.' -> 'I went to school.'",
            "Exercise 2: Fill in the blank: 'Tomorrow I ___ (will play / played) cricket.'",
            "Exercise 3: Translate: 'நான் நாளை பாடம் படிப்பேன்.'"
        ),
        summaryEnglish = "Textbook Page Explanation: This English Grammar page teaches the 3 main tenses: Simple Present (daily actions like 'Subi reads'), Simple Past (completed actions like 'We went'), and Simple Future (upcoming actions using 'will + verb'). Exercise Answers: 1) 'I went to school.' 2) 'Tomorrow I will play cricket.' 3) 'I will study the lesson tomorrow.'",
        summaryTamil = "📖 பாடப்புத்தகப் பக்க விளக்கம் (ஆங்கில இலக்கணம் - பக்கம் 42):\n1. Simple Present (நிகழ்காலம்): தினமும் நடக்கும் செயல்கள் (Subi reads a book).\n2. Simple Past (இறந்தகாலம்): முடிந்த செயல்கள் (We went to school).\n3. Simple Future (எதிர்காலம்): இனி நடக்கப்போகும் செயல்கள் - 'will + வினைச்சொல்' (Subi will speak).\n\n✅ பயிற்சி விடைகள்:\n1. I went to school.\n2. Tomorrow I will play cricket.\n3. I will study the lesson tomorrow.",
        keyWordsTamil = "பார்முலா: Present = V1 (go/goes), Past = V2 (went), Future = will + V1 (will go)."
    ),
    MATH_WORD_PROBLEMS(
        id = "math_word_problems",
        titleTamil = "📐 கணிதப் பாடம்: பெருக்கல் & வழிமுறைக் கணக்குகள்",
        titleEnglish = "Class 4 Mathematics • Chapter 4: Multiplication Word Problems",
        headerTag = "STANDARD IV • MATHEMATICS TEXTBOOK • PAGE 58",
        lines = listOf(
            "Example 1: One notebook costs Rs. 15. What is the cost of 12 notebooks?",
            "   Solution: Cost of 1 notebook = Rs. 15",
            "             Number of notebooks = 12",
            "             Total Cost = 15 x 12 = Rs. 180.",
            "Example 2: There are 24 students in one row. How many students",
            "   are there in 6 rows?",
            "   Solution: 24 x 6 = 144 students."
        ),
        questions = listOf(
            "Practice Q1: A box has 8 apples. How many apples are in 9 boxes?",
            "Practice Q2: Find the product of 25 x 4.",
            "Practice Q3: Subi reads 5 pages daily. How many pages in 7 days?"
        ),
        summaryEnglish = "Textbook Page Explanation: This Mathematics page explains Multiplication Word Problems. When we know the value of ONE item and need the value of MANY items, we multiply! Practice Answers: Q1) 8 x 9 = 72 apples. Q2) 25 x 4 = 100. Q3) 5 x 7 = 35 pages in 7 days!",
        summaryTamil = "📖 பாடப்புத்தகப் பக்க விளக்கம் (கணிதம் - பக்கம் 58):\nஒரு பொருளின் விலை கொடுத்து, பல பொருட்களின் மொத்த விலையைக் கேட்டால் பெருக்க வேண்டும் (Multiplication)!\n• உதாரணம் 1: 1 நோட்டு ₹15 எனில், 12 நோட்டுகளின் விலை = 15 × 12 = ₹180.\n\n✅ பயிற்சிக் கணக்குகளின் விடைகள்:\n1. 8 × 9 = 72 ஆப்பிள்கள் (72 apples)\n2. 25 × 4 = 100\n3. 5 × 7 = 35 பக்கங்கள் (35 pages).",
        keyWordsTamil = "கணித ஆங்கில வார்த்தைகள்: Cost = விலை, Total = மொத்தம், Product = பெருக்குத்தொகை, Row = வரிசை."
    ),
    MORAL_STORY_CROW(
        id = "moral_story_crow",
        titleTamil = "📕 ஆங்கிலப் பாடப்புத்தகக் கதை: புத்திசாலிக் காகம்",
        titleEnglish = "English Reader • Lesson 2: The Clever Thirsty Crow",
        headerTag = "STANDARD IV • ENGLISH READER • PAGE 16",
        lines = listOf(
            "One hot summer day, a crow became very thirsty. He flew here and",
            "there looking for water, but could not find any. At last, he saw",
            "a pot with a little water at the bottom. His beak could not reach it.",
            "The clever crow saw small pebbles nearby. He picked them up one by",
            "one and dropped them into the pot. Slowly, the water came up to the",
            "top. The crow drank the water happily and flew away!",
            "Moral: Where there is a will, there is a way."
        ),
        questions = listOf(
            "1. Why was the crow flying here and there?",
            "2. Why couldn't the crow drink the water at first?",
            "3. What did the crow drop into the pot?"
        ),
        summaryEnglish = "Textbook Page Explanation: This story is 'The Clever Thirsty Crow'. On a hot day, a thirsty crow found a pot with low water. By dropping pebbles one by one, the water rose up and he drank it! Answers: 1) The crow was looking for water because he was very thirsty. 2) The water was at the bottom of the pot, so his beak could not reach it. 3) The crow dropped small pebbles into the pot.",
        summaryTamil = "📖 பாடப்புத்தகப் பக்க விளக்கம் (ஆங்கிலக் கதை - பக்கம் 16):\nகோடை நாளில் தாகத்தால் தவித்த காகம் பானையின் அடியில் இருந்த நீரைக் குடிக்க, சிறிய கற்களை (Pebbles) பானைக்குள் போட்டது. நீர் மேலே வந்ததும் குடித்து மகிழ்ந்தது!\n\n✅ புத்தகக் கேள்விகளுக்கான விடைகள்:\n1. The crow was looking for water. (காகம் தண்ணீரைத் தேடிப் பறந்தது).\n2. Because the water was very low at the bottom. (தண்ணீர் அடியில் குறைவாக இருந்தது).\n3. The crow dropped small pebbles into the pot. (சிறிய கற்களைப் போட்டது).",
        keyWordsTamil = "முக்கிய வார்த்தைகள்: Thirsty = தாகம், Pitcher / Pot = பானை, Beak = அலகு, Pebbles = கூழாங்கற்கள்."
    )
}

object TextbookImageHelper {

    fun createCameraCaptureFile(context: Context): Pair<File, Uri> {
        val dir = File(context.filesDir, "textbook_photos").apply { mkdirs() }
        val file = File(dir, "camera_textbook_${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        return file to uri
    }

    fun createTempCameraFileAndUri(context: Context): Pair<File, Uri> =
        createCameraCaptureFile(context)

    fun copyUriToInternalFile(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            if (originalBitmap != null) {
                saveBitmapToInternalFile(context, originalBitmap)
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    fun saveBitmapToInternalFile(context: Context, bitmap: Bitmap): String? {
        return try {
            val scaled = scaleDownBitmap(bitmap, 1200)
            val dir = File(context.filesDir, "textbook_photos").apply { mkdirs() }
            val file = File(dir, "textbook_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out ->
                scaled.compress(Bitmap.CompressFormat.JPEG, 85, out)
            }
            file.absolutePath
        } catch (_: Exception) {
            null
        }
    }

    fun loadBitmapFromPath(path: String): Bitmap? {
        if (path.isBlank()) return null
        return try {
            val file = File(path)
            if (file.exists()) {
                BitmapFactory.decodeFile(file.absolutePath)
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    fun loadScaledBitmap(path: String, maxDimension: Int = 800): Bitmap? {
        val bmp = loadBitmapFromPath(path) ?: return null
        return scaleDownBitmap(bmp, maxDimension)
    }

    fun generateSampleTextbookBitmap(context: Context, sample: SampleTextbookPage): String? =
        createSampleTextbookPageFile(context, sample)

    fun fileToBase64Jpeg(path: String): String? {
        val bitmap = loadBitmapFromPath(path) ?: return null
        val scaled = scaleDownBitmap(bitmap, 1024)
        val output = ByteArrayOutputStream()
        scaled.compress(Bitmap.CompressFormat.JPEG, 82, output)
        return Base64.encodeToString(output.toByteArray(), Base64.NO_WRAP)
    }

    private fun scaleDownBitmap(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        if (width <= maxDimension && height <= maxDimension) return bitmap
        val ratio = width.toFloat() / height.toFloat()
        val (newW, newH) = if (width > height) {
            maxDimension to (maxDimension / ratio).toInt().coerceAtLeast(1)
        } else {
            (maxDimension * ratio).toInt().coerceAtLeast(1) to maxDimension
        }
        return Bitmap.createScaledBitmap(bitmap, newW, newH, true)
    }

    /**
     * Generates a realistic printed textbook page image and saves it to internal storage
     * so users can test textbook photo analysis & doubt asking with 1 tap!
     */
    fun createSampleTextbookPageFile(context: Context, sample: SampleTextbookPage): String? {
        val width = 900
        val height = 1160
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Cream textbook paper background
        canvas.drawColor(Color.parseColor("#FFFDF7"))

        // Outer page border
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#CBD5E1")
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }
        canvas.drawRoundRect(RectF(24f, 24f, width - 24f, height - 24f), 16f, 16f, borderPaint)

        // Top textbook header bar
        val headerBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#1E3A8A")
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(RectF(44f, 44f, width - 44f, 150f), 14f, 14f, headerBgPaint)

        val tagPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#93C5FD")
            textSize = 20f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText(sample.headerTag, 68f, 82f, tagPaint)

        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 27f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText(sample.titleEnglish, 68f, 124f, titlePaint)

        // Tamil subtitle banner
        val subBannerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#EFF6FF")
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(RectF(44f, 168f, width - 44f, 224f), 10f, 10f, subBannerPaint)

        val subTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#1D4ED8")
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText(sample.titleTamil, 64f, 204f, subTextPaint)

        // Lesson Content Section
        var y = 278f
        val sectionTitlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#0F172A")
            textSize = 26f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText("📖 LESSON CONTENT (பாடக் குறிப்புகள்):", 56f, y, sectionTitlePaint)
        y += 46f

        val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#1E293B")
            textSize = 23f
            typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
        }

        for (line in sample.lines) {
            canvas.drawText(line, 64f, y, bodyPaint)
            y += 46f
        }

        // Exercises / Questions Box at bottom of textbook page
        y += 28f
        val qBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FEF3C7")
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(RectF(48f, y, width - 48f, y + 270f), 14f, 14f, qBoxPaint)

        val qHeadingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#92400E")
            textSize = 25f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        y += 44f
        canvas.drawText("✏️ TEXTBOOK EXERCISES & QUESTIONS (பயிற்சி வினாக்கள்):", 70f, y, qHeadingPaint)
        y += 46f

        val qTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#78350F")
            textSize = 22f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        for (q in sample.questions) {
            canvas.drawText(q, 74f, y, qTextPaint)
            y += 48f
        }

        val dir = File(context.filesDir, "textbook_photos").apply { mkdirs() }
        val file = File(dir, "sample_${sample.id}_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        return file.absolutePath
    }
}
