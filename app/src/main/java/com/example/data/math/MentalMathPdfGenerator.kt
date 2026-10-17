package com.example.data.math

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object MentalMathPdfGenerator {

    /**
     * All 100 Mental Math Secrets designed for students below 7th Standard (Classes 1 to 7).
     * Categorized into Addition (1-15), Subtraction (16-30), Multiplication (31-60),
     * Division (61-75), Fractions & Percentages (76-85), and Squares & Math Magic (86-100).
     */
    val allSecrets: List<MentalMathSecret> =
        MentalMathSecretsPart1.additionSecrets +
        MentalMathSecretsPart1.subtractionSecrets +
        MentalMathSecretsPart2.multiplicationSecrets +
        MentalMathSecretsPart3.divisionSecrets +
        MentalMathSecretsPart3.fractionsSecrets +
        MentalMathSecretsPart3.squaresMagicSecrets

    /**
     * Generates a multi-page, formatted PDF file with all 100 mental math secrets
     * AND 1 to 20 Multiplication Tables, and saves it to internal files directory.
     */
    fun generatePdfFile(context: Context): File {
        val file = File(context.filesDir, "Mental_Maths_100_Secrets_Tamil.pdf")
        val document = PdfDocument()

        val pageWidth = 595 // Standard A4 width in points (72 dpi)
        val pageHeight = 842 // Standard A4 height in points

        // Paints
        val titlePaint = Paint().apply {
            color = Color.rgb(30, 27, 75) // Deep Indigo
            textSize = 16f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        val subTitlePaint = Paint().apply {
            color = Color.rgb(79, 70, 229) // Indigo Accent
            textSize = 10.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        val headingPaint = Paint().apply {
            color = Color.rgb(15, 23, 42) // Slate 900
            textSize = 11.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        val bodyPaint = Paint().apply {
            color = Color.rgb(51, 65, 85) // Slate 700
            textSize = 9f
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }

        val formulaPaint = Paint().apply {
            color = Color.rgb(180, 83, 9) // Amber 700
            textSize = 9.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        val bannerPaint = Paint().apply {
            color = Color.rgb(238, 242, 255) // Indigo 50
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        val boxPaint = Paint().apply {
            color = Color.rgb(248, 250, 252) // Slate 50
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        val borderPaint = Paint().apply {
            color = Color.rgb(203, 213, 225) // Slate 300
            style = Paint.Style.STROKE
            strokeWidth = 1f
            isAntiAlias = true
        }

        var currentPageNumber = 1
        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPageNumber).create()
        var page = document.startPage(pageInfo)
        var canvas: Canvas = page.canvas
        var yPos: Float

        fun drawHeader(c: Canvas, subtitle: String) {
            val headerRect = RectF(24f, 18f, (pageWidth - 24).toFloat(), 80f)
            c.drawRoundRect(headerRect, 8f, 8f, bannerPaint)
            c.drawRoundRect(headerRect, 8f, 8f, borderPaint)

            c.drawText("பேசலாம் AI • 100 மனக் கணக்கு ரகசியங்கள் (1-7 ஆம் வகுப்பு மாணவர்களுக்கு)", 36f, 44f, titlePaint)
            c.drawText(subtitle, 36f, 65f, subTitlePaint)
        }

        // ================= SECTION 1: 100 SECRETS (Multi-page) =================
        drawHeader(canvas, "⚡ 100 சூப்பர் மனக் கணக்கு & வேத கணித குறுக்குவழிகள் • முழு கையேடு")
        yPos = 95f

        for (secret in allSecrets) {
            val estimatedSecretHeight = 135f
            if (yPos + estimatedSecretHeight > pageHeight - 40f) {
                // Footer
                bodyPaint.textSize = 8.5f
                canvas.drawText("பக்கம் $currentPageNumber • பேசலாம் AI • 100 மனக் கணக்கு ரகசியங்கள்", 36f, (pageHeight - 20).toFloat(), bodyPaint)
                bodyPaint.textSize = 9f

                document.finishPage(page)
                currentPageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPageNumber).create()
                page = document.startPage(pageInfo)
                canvas = page.canvas
                drawHeader(canvas, "⚡ 100 சூப்பர் மனக் கணக்கு & வேத கணித குறுக்குவழிகள் • முழு கையேடு")
                yPos = 95f
            }

            // Card Container
            val cardRect = RectF(24f, yPos, (pageWidth - 24).toFloat(), yPos + estimatedSecretHeight - 10f)
            canvas.drawRoundRect(cardRect, 6f, 6f, boxPaint)
            canvas.drawRoundRect(cardRect, 6f, 6f, borderPaint)

            // Secret Title & Number
            canvas.drawText("ரகசியம் ${secret.number}: ${secret.titleTa} (${secret.titleEn})", 34f, yPos + 18f, headingPaint)

            // Formula
            canvas.drawText("📌 சூத்திரம்: ${secret.formulaTa}", 34f, yPos + 35f, formulaPaint)

            // Explanation
            canvas.drawText("💡 விளக்கம்: ${secret.explanationTa}", 34f, yPos + 51f, bodyPaint)

            // Examples
            val ex1 = secret.examples.getOrNull(0)
            val ex2 = secret.examples.getOrNull(1)
            val exText = buildString {
                if (ex1 != null) append("எ.கா 1: ${ex1.first} ➔ ${ex1.second}   |   ")
                if (ex2 != null) append("எ.கா 2: ${ex2.first} ➔ ${ex2.second}")
            }
            bodyPaint.color = Color.rgb(30, 41, 59)
            canvas.drawText("✍️ $exText", 34f, yPos + 69f, bodyPaint)
            bodyPaint.color = Color.rgb(51, 65, 85)

            // Practice Questions
            val practiceStr = secret.practiceQuestions.take(3).joinToString(", ")
            canvas.drawText("🎯 பயிற்சி வினாக்கள்: $practiceStr", 34f, yPos + 87f, formulaPaint)

            yPos += estimatedSecretHeight
        }

        // Finish Secrets Section page
        bodyPaint.textSize = 8.5f
        canvas.drawText("பக்கம் $currentPageNumber • பேசலாம் AI • 100 மனக் கணக்கு ரகசியங்கள்", 36f, (pageHeight - 20).toFloat(), bodyPaint)
        document.finishPage(page)

        // ================= SECTION 2: 1 TO 20 MULTIPLICATION TABLES =================
        fun drawTableGrid(startNum: Int, endNum: Int, pageTitle: String) {
            currentPageNumber++
            pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPageNumber).create()
            page = document.startPage(pageInfo)
            canvas = page.canvas

            drawHeader(canvas, pageTitle)

            val cols = 5
            val tableWidth = (pageWidth - 48f) / cols
            val startY = 95f
            val tableRowHeight = 13.5f

            var index = 0
            for (num in startNum..endNum) {
                val colIndex = index % cols
                val rowIndex = index / cols

                val x = 24f + (colIndex * tableWidth)
                val y = startY + (rowIndex * 340f)

                // Table box
                val rect = RectF(x + 2f, y, x + tableWidth - 4f, y + 330f)
                canvas.drawRoundRect(rect, 6f, 6f, boxPaint)
                canvas.drawRoundRect(rect, 6f, 6f, borderPaint)

                // Table header
                val headerBg = RectF(x + 2f, y, x + tableWidth - 4f, y + 22f)
                canvas.drawRoundRect(headerBg, 6f, 6f, bannerPaint)

                headingPaint.textSize = 9.5f
                headingPaint.color = Color.rgb(67, 56, 202)
                canvas.drawText("$num-ஆம் வாய்ப்பாடு ($num×)", x + 8f, y + 15f, headingPaint)
                headingPaint.color = Color.rgb(15, 23, 42)

                // 1 to 10 rows
                bodyPaint.textSize = 8.5f
                for (row in 1..10) {
                    val lineY = y + 26f + (row * tableRowHeight)
                    val lineText = "$num × $row = ${num * row}"
                    canvas.drawText(lineText, x + 10f, lineY, bodyPaint)
                }

                index++
            }

            bodyPaint.textSize = 8.5f
            canvas.drawText("பக்கம் $currentPageNumber • பேசலாம் AI • வாய்ப்பாடுகள் 1 முதல் 20 வரை", 36f, (pageHeight - 20).toFloat(), bodyPaint)
            document.finishPage(page)
        }

        // Tables 1 to 10
        drawTableGrid(1, 10, "🔢 1 முதல் 10 வரையிலான பெருக்கல் வாய்ப்பாடுகள் (Tables 1 to 10)")

        // Tables 11 to 20
        drawTableGrid(11, 20, "🔢 11 முதல் 20 வரையிலான பெருக்கல் வாய்ப்பாடுகள் (Tables 11 to 20)")

        // Write to output file
        FileOutputStream(file).use { out ->
            document.writeTo(out)
        }
        document.close()

        return file
    }

    /**
     * Opens the generated Mental Math PDF using the system viewer
     */
    fun openPdf(context: Context) {
        try {
            val file = generatePdfFile(context)
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(intent, "100 மனக் கணக்கு PDF-ஐத் திறக்க (Open PDF)"))
        } catch (e: Exception) {
            Toast.makeText(context, "PDF திறப்பதில் சிக்கல்: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Shares the generated Mental Math PDF via WhatsApp, Gmail, etc.
     */
    fun sharePdf(context: Context) {
        try {
            val file = generatePdfFile(context)
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "100 Mental Maths Secrets & Tables 1-20 Tamil PDF • பேசலாம் AI")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "7-ஆம் வகுப்புக்கு உட்பட்ட மாணவர்களுக்கான 100 மனக் கணக்கு ரகசியங்கள் மற்றும் 1 முதல் 20 வரையிலான வாய்ப்பாடுகள் தமிழில் (100 Mental Maths Secrets & Tables 1 to 20 PDF) - பேசலாம் AI செயலி மூலம் உருவாக்கப்பட்டது!"
                )
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(intent, "100 மனக் கணக்கு PDF-ஐப் பகிர்க (Share PDF)"))
        } catch (e: Exception) {
            Toast.makeText(context, "PDF பகிர்வதில் சிக்கல்: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
