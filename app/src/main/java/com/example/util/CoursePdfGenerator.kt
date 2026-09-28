package com.example.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.FileProvider
import com.example.data.lessons.PdfCoursePage
import com.example.data.lessons.SpokenEnglishPdfCourseData
import java.io.File
import java.io.FileOutputStream

object CoursePdfGenerator {

    private const val PAGE_WIDTH = 794
    private const val PAGE_HEIGHT = 1160
    private const val MARGIN_X = 34f
    private const val CONTENT_WIDTH = PAGE_WIDTH - (MARGIN_X * 2)

    fun getOrCreateCoursePdfFile(context: Context, forceRegenerate: Boolean = false): File {
        val pdfFile = File(context.filesDir, SpokenEnglishPdfCourseData.PDF_FILE_NAME)
        if (!pdfFile.exists() || pdfFile.length() < 4096L || forceRegenerate) {
            generateCoursePdfFile(pdfFile)
        }
        return pdfFile
    }

    private fun generateCoursePdfFile(outputFile: File) {
        val document = PdfDocument()
        val pages = SpokenEnglishPdfCourseData.pages
        val totalPages = pages.size

        try {
            pages.forEachIndexed { index, coursePage ->
                val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, index + 1).create()
                val page = document.startPage(pageInfo)
                drawCoursePage(page.canvas, coursePage, totalPages)
                document.finishPage(page)
            }
            FileOutputStream(outputFile).use { out ->
                document.writeTo(out)
            }
        } finally {
            document.close()
        }
    }

    private fun drawCoursePage(canvas: Canvas, pageData: PdfCoursePage, totalPages: Int) {
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FFFDF9")
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), PAGE_HEIGHT.toFloat(), bgPaint)

        // Outer decorative page border
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#CBD5E1")
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
        canvas.drawRoundRect(
            RectF(16f, 16f, PAGE_WIDTH - 16f, PAGE_HEIGHT - 16f),
            14f,
            14f,
            borderPaint
        )

        var currentY = 28f

        // 1. Top Textbook Header Banner
        val headerBannerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = if (pageData.pageNumber == 1) Color.parseColor("#1E3A8A") else Color.parseColor("#0F766E")
            style = Paint.Style.FILL
        }
        val headerHeight = if (pageData.pageNumber == 1) 136f else 112f
        val headerRect = RectF(MARGIN_X, currentY, PAGE_WIDTH - MARGIN_X, currentY + headerHeight)
        canvas.drawRoundRect(headerRect, 14f, 14f, headerBannerPaint)

        val badgeTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FDE68A")
            textSize = 13f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val topMeta = "${pageData.unitTag}   •   ${pageData.dayRange}   •   பக்கம் ${pageData.pageNumber} / $totalPages"
        canvas.drawText(topMeta, MARGIN_X + 16f, currentY + 24f, badgeTextPaint)

        val headerTitleEnPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = if (pageData.pageNumber == 1) 20f else 17.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val enHeight = drawWrappedText(
            canvas = canvas,
            text = pageData.titleEnglish,
            paint = headerTitleEnPaint,
            x = MARGIN_X + 16f,
            y = currentY + 32f,
            width = (CONTENT_WIDTH - 32f).toInt()
        )

        val headerTitleTaPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#E0F2FE")
            textSize = 14.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        drawWrappedText(
            canvas = canvas,
            text = pageData.titleTamil,
            paint = headerTitleTaPaint,
            x = MARGIN_X + 16f,
            y = currentY + 36f + enHeight,
            width = (CONTENT_WIDTH - 32f).toInt()
        )

        if (pageData.pageNumber == 1) {
            val authorPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#FEF08A")
                textSize = 13f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            canvas.drawText(
                "${SpokenEnglishPdfCourseData.BOOK_AUTHOR}  |  ${SpokenEnglishPdfCourseData.BOOK_STUDENT}",
                MARGIN_X + 16f,
                currentY + headerHeight - 14f,
                authorPaint
            )
        }

        currentY += headerHeight + 12f

        // 2. Intro Explanation Box
        val introTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#1E293B")
            textSize = 13f
        }
        val introTextHeight = measureWrappedTextHeight(
            text = pageData.introExplanationTamil,
            paint = introTextPaint,
            width = (CONTENT_WIDTH - 24f).toInt()
        )
        val introBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#F1F5F9")
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(
            RectF(MARGIN_X, currentY, PAGE_WIDTH - MARGIN_X, currentY + introTextHeight + 16f),
            10f,
            10f,
            introBoxPaint
        )
        drawWrappedText(
            canvas = canvas,
            text = pageData.introExplanationTamil,
            paint = introTextPaint,
            x = MARGIN_X + 12f,
            y = currentY + 8f,
            width = (CONTENT_WIDTH - 24f).toInt()
        )
        currentY += introTextHeight + 26f

        // 3. Grammar Formula / Highlights Box
        if (pageData.formulas.isNotEmpty()) {
            val formulaTitlePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#9A3412")
                textSize = 13.5f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            val formulaItemPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#431407")
                textSize = 12.5f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }

            val combinedFormulas = pageData.formulas.joinToString("\n") { "• $it" }
            val formulaBodyHeight = measureWrappedTextHeight(
                text = combinedFormulas,
                paint = formulaItemPaint,
                width = (CONTENT_WIDTH - 28f).toInt()
            )
            val boxHeight = formulaBodyHeight + 38f

            val formulaBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#FFF7ED")
                style = Paint.Style.FILL
            }
            val formulaStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#F97316")
                style = Paint.Style.STROKE
                strokeWidth = 1.5f
            }
            val formulaRect = RectF(MARGIN_X, currentY, PAGE_WIDTH - MARGIN_X, currentY + boxHeight)
            canvas.drawRoundRect(formulaRect, 10f, 10f, formulaBgPaint)
            canvas.drawRoundRect(formulaRect, 10f, 10f, formulaStrokePaint)

            canvas.drawText(pageData.formulaBoxTitle, MARGIN_X + 14f, currentY + 20f, formulaTitlePaint)
            drawWrappedText(
                canvas = canvas,
                text = combinedFormulas,
                paint = formulaItemPaint,
                x = MARGIN_X + 14f,
                y = currentY + 28f,
                width = (CONTENT_WIDTH - 28f).toInt()
            )
            currentY += boxHeight + 12f
        }

        // 4. Structured Course Table
        if (pageData.tableHeaders.isNotEmpty() && pageData.tableRows.isNotEmpty()) {
            val colCount = pageData.tableHeaders.size
            val colWidth = CONTENT_WIDTH / colCount

            val tableHeaderBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#1E293B")
                style = Paint.Style.FILL
            }
            val tableHeaderTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                textSize = 12f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }

            val headerRowHeight = 28f
            canvas.drawRect(MARGIN_X, currentY, PAGE_WIDTH - MARGIN_X, currentY + headerRowHeight, tableHeaderBgPaint)
            pageData.tableHeaders.forEachIndexed { colIndex, headerText ->
                val cellX = MARGIN_X + (colIndex * colWidth) + 6f
                drawWrappedText(
                    canvas = canvas,
                    text = headerText,
                    paint = tableHeaderTextPaint,
                    x = cellX,
                    y = currentY + 6f,
                    width = (colWidth - 12f).toInt()
                )
            }
            currentY += headerRowHeight

            val cellTextPaintBold = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#0F172A")
                textSize = 11.5f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            val cellTextPaintRegular = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#334155")
                textSize = 11.5f
            }
            val rowAltPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#F8FAFC")
                style = Paint.Style.FILL
            }
            val rowWhitePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                style = Paint.Style.FILL
            }
            val gridLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#E2E8F0")
                style = Paint.Style.STROKE
                strokeWidth = 1f
            }

            pageData.tableRows.forEachIndexed { rowIndex, row ->
                val cols = listOf(row.col1, row.col2, row.col3, row.col4).take(colCount)
                var maxCellHeight = 18f
                cols.forEachIndexed { colIdx, cellText ->
                    val paint = if (colIdx == 0) cellTextPaintBold else cellTextPaintRegular
                    val h = measureWrappedTextHeight(cellText, paint, (colWidth - 12f).toInt())
                    if (h > maxCellHeight) maxCellHeight = h
                }
                val rowHeight = maxCellHeight + 10f
                if (currentY + rowHeight < PAGE_HEIGHT - 88f) {
                    canvas.drawRect(
                        MARGIN_X,
                        currentY,
                        PAGE_WIDTH - MARGIN_X,
                        currentY + rowHeight,
                        if (rowIndex % 2 == 0) rowWhitePaint else rowAltPaint
                    )
                    canvas.drawRect(
                        MARGIN_X,
                        currentY,
                        PAGE_WIDTH - MARGIN_X,
                        currentY + rowHeight,
                        gridLinePaint
                    )
                    cols.forEachIndexed { colIdx, cellText ->
                        val cellX = MARGIN_X + (colIdx * colWidth) + 6f
                        val paint = if (colIdx == 0) cellTextPaintBold else cellTextPaintRegular
                        drawWrappedText(
                            canvas = canvas,
                            text = cellText,
                            paint = paint,
                            x = cellX,
                            y = currentY + 5f,
                            width = (colWidth - 12f).toInt()
                        )
                    }
                    currentY += rowHeight
                }
            }
            currentY += 12f
        }

        // 5. Practice Sentences Section
        if (pageData.sentences.isNotEmpty() && currentY < PAGE_HEIGHT - 120f) {
            val sectionHeaderPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#0F766E")
                textSize = 14f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            canvas.drawText(pageData.sentencesTitle, MARGIN_X, currentY + 14f, sectionHeaderPaint)
            currentY += 22f

            val engPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#0F172A")
                textSize = 12.5f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            val pronPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#0369A1")
                textSize = 11.5f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            val tamPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#334155")
                textSize = 11.5f
            }
            val cardBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#F0FDFA")
                style = Paint.Style.FILL
            }
            val cardStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#99F6E4")
                style = Paint.Style.STROKE
                strokeWidth = 1f
            }

            pageData.sentences.forEachIndexed { idx, sent ->
                val line1 = "${idx + 1}. ${sent.english}"
                val line2 = "   🗣️ உச்சரிப்பு: ${sent.tamilPronunciation}"
                val line3 = "   🇮🇳 அர்த்தம்: ${sent.tamilMeaning}" +
                    if (sent.grammarNote.isNotBlank()) "  (${sent.grammarNote})" else ""

                val w = (CONTENT_WIDTH - 20f).toInt()
                val h1 = measureWrappedTextHeight(line1, engPaint, w)
                val h2 = measureWrappedTextHeight(line2, pronPaint, w)
                val h3 = measureWrappedTextHeight(line3, tamPaint, w)
                val totalCardH = h1 + h2 + h3 + 14f

                if (currentY + totalCardH < PAGE_HEIGHT - 82f) {
                    val rect = RectF(MARGIN_X, currentY, PAGE_WIDTH - MARGIN_X, currentY + totalCardH)
                    canvas.drawRoundRect(rect, 8f, 8f, cardBgPaint)
                    canvas.drawRoundRect(rect, 8f, 8f, cardStrokePaint)

                    var textY = currentY + 5f
                    textY += drawWrappedText(canvas, line1, engPaint, MARGIN_X + 10f, textY, w)
                    textY += drawWrappedText(canvas, line2, pronPaint, MARGIN_X + 10f, textY + 1f, w)
                    drawWrappedText(canvas, line3, tamPaint, MARGIN_X + 10f, textY + 1f, w)

                    currentY += totalCardH + 6f
                }
            }
        }

        // 6. Teacher Tip Box at Bottom
        val tipY = PAGE_HEIGHT - 76f
        val tipBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FEF3C7")
            style = Paint.Style.FILL
        }
        val tipBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#F59E0B")
            style = Paint.Style.STROKE
            strokeWidth = 1.2f
        }
        val tipRect = RectF(MARGIN_X, tipY, PAGE_WIDTH - MARGIN_X, PAGE_HEIGHT - 34f)
        canvas.drawRoundRect(tipRect, 8f, 8f, tipBgPaint)
        canvas.drawRoundRect(tipRect, 8f, 8f, tipBorderPaint)

        val tipTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#78350F")
            textSize = 11.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        drawWrappedText(
            canvas = canvas,
            text = "👩‍🏫 ${pageData.teacherTipTamil}",
            paint = tipTextPaint,
            x = MARGIN_X + 10f,
            y = tipY + 6f,
            width = (CONTENT_WIDTH - 20f).toInt()
        )

        // 7. Page Footer Line
        val footerPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#64748B")
            textSize = 10.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText(
            "${SpokenEnglishPdfCourseData.BOOK_TITLE_TA}  •  தனம் டீச்சர் & சுபிக்சா (சுபி, 9 வயது)",
            MARGIN_X,
            PAGE_HEIGHT - 19f,
            footerPaint
        )
        val pageLabel = "Page ${pageData.pageNumber} of $totalPages"
        val pageLabelWidth = footerPaint.measureText(pageLabel)
        canvas.drawText(
            pageLabel,
            PAGE_WIDTH - MARGIN_X - pageLabelWidth,
            PAGE_HEIGHT - 19f,
            footerPaint
        )
    }

    private fun measureWrappedTextHeight(text: String, paint: TextPaint, width: Int): Float {
        val safeWidth = width.coerceAtLeast(100)
        val layout = StaticLayout.Builder
            .obtain(text, 0, text.length, paint, safeWidth)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(1.5f, 1.0f)
            .setIncludePad(false)
            .build()
        return layout.height.toFloat()
    }

    private fun drawWrappedText(
        canvas: Canvas,
        text: String,
        paint: TextPaint,
        x: Float,
        y: Float,
        width: Int
    ): Float {
        val safeWidth = width.coerceAtLeast(100)
        val layout = StaticLayout.Builder
            .obtain(text, 0, text.length, paint, safeWidth)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(1.5f, 1.0f)
            .setIncludePad(false)
            .build()
        canvas.save()
        canvas.translate(x, y)
        layout.draw(canvas)
        canvas.restore()
        return layout.height.toFloat()
    }

    fun getPdfPageCount(pdfFile: File): Int {
        if (!pdfFile.exists()) return 0
        return try {
            ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY).use { pfd ->
                PdfRenderer(pfd).use { renderer ->
                    renderer.pageCount
                }
            }
        } catch (e: Exception) {
            0
        }
    }

    fun renderPdfPageToBitmap(pdfFile: File, pageIndex: Int, scale: Float = 2.0f): Bitmap? {
        if (!pdfFile.exists()) return null
        return try {
            ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY).use { pfd ->
                PdfRenderer(pfd).use { renderer ->
                    if (pageIndex < 0 || pageIndex >= renderer.pageCount) return null
                    renderer.openPage(pageIndex).use { page ->
                        val width = (page.width * scale).toInt().coerceAtLeast(1)
                        val height = (page.height * scale).toInt().coerceAtLeast(1)
                        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        bitmap.eraseColor(Color.WHITE)
                        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                        bitmap
                    }
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun copyUriToCustomPdfFile(context: Context, sourceUri: Uri): File? {
        return try {
            val destFile = File(context.cacheDir, "user_imported_book.pdf")
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            if (destFile.exists() && destFile.length() > 100L) destFile else null
        } catch (e: Exception) {
            null
        }
    }

    fun copyPdfToUri(context: Context, pdfFile: File, targetUri: Uri): Boolean {
        return try {
            context.contentResolver.openOutputStream(targetUri)?.use { out ->
                pdfFile.inputStream().use { input ->
                    input.copyTo(out)
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun savePdfToDownloads(context: Context, pdfFile: File): Result<String> {
        return try {
            val fileName = SpokenEnglishPdfCourseData.PDF_FILE_NAME
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                    ?: return Result.failure(IllegalStateException("Unable to create Downloads entry"))
                resolver.openOutputStream(uri)?.use { out ->
                    pdfFile.inputStream().use { input ->
                        input.copyTo(out)
                    }
                }
                Result.success("Downloads/$fileName")
            } else {
                val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: context.filesDir
                val target = File(downloadsDir, fileName)
                pdfFile.copyTo(target, overwrite = true)
                Result.success(target.absolutePath)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun shareOrOpenPdf(context: Context, pdfFile: File, openDirectly: Boolean): Boolean {
        return try {
            val authority = "${context.packageName}.fileprovider"
            val uri = FileProvider.getUriForFile(context, authority, pdfFile)
            if (openDirectly) {
                val viewIntent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/pdf")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(Intent.createChooser(viewIntent, "PDF புத்தகத்தைத் திற (Open PDF Book)"))
            } else {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    putExtra(
                        Intent.EXTRA_SUBJECT,
                        "தமிழ் வழியே ஸ்போக்கன் இங்கிலீஷ் - முழுப் பாடப்புத்தகம் (PDF)"
                    )
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "தனம் டீச்சர் உருவாக்கிய சுபிக்சாவின் (சுபி, 9 வயது) 'Spoken English via Tamil' 30-Day Course PDF Book."
                    )
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(shareIntent, "PDF புத்தகத்தைப் பகிர் (Share PDF Book)"))
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}
