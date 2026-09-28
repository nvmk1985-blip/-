package com.example.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.lessons.PdfCoursePage
import com.example.data.lessons.PdfCourseSentence
import com.example.data.lessons.RoleplayScenario
import com.example.data.lessons.SpokenEnglishPdfCourseData
import com.example.ui.components.ScoreBadge
import com.example.ui.components.TtsSpeakerButton
import com.example.ui.viewmodel.MainViewModel
import com.example.util.CoursePdfGenerator
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SpokenEnglishPdfBookSection(
    viewModel: MainViewModel,
    onStartRoleplay: (RoleplayScenario) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var viewMode by remember { mutableIntStateOf(0) } // 0 = PDF Book Reader, 1 = Interactive Course, 2 = Table of Contents
    var currentPageIndex by remember { mutableIntStateOf(0) }
    var pdfFile by remember { mutableStateOf<File?>(null) }
    var isCustomPdf by remember { mutableStateOf(false) }
    var customPdfName by remember { mutableStateOf("") }
    var totalPageCount by remember { mutableIntStateOf(SpokenEnglishPdfCourseData.pages.size) }
    var currentPageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoadingPdf by remember { mutableStateOf(true) }
    var statusBannerMessage by remember { mutableStateOf<String?>(null) }

    // Zoom & pan states for PDF page
    var zoomScale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    // Initialize built-in Spoken English via Tamil PDF Course Book
    LaunchedEffect(Unit) {
        isLoadingPdf = true
        withContext(Dispatchers.IO) {
            val generated = CoursePdfGenerator.getOrCreateCoursePdfFile(context)
            val count = CoursePdfGenerator.getPdfPageCount(generated).coerceAtLeast(1)
            val bmp = CoursePdfGenerator.renderPdfPageToBitmap(generated, 0)
            withContext(Dispatchers.Main) {
                pdfFile = generated
                totalPageCount = count
                currentPageBitmap = bmp
                isLoadingPdf = false
            }
        }
    }

    // Re-render page bitmap when currentPageIndex or pdfFile changes
    LaunchedEffect(currentPageIndex, pdfFile) {
        val activeFile = pdfFile ?: return@LaunchedEffect
        zoomScale = 1f
        offsetX = 0f
        offsetY = 0f
        isLoadingPdf = true
        withContext(Dispatchers.IO) {
            val bmp = CoursePdfGenerator.renderPdfPageToBitmap(activeFile, currentPageIndex)
            withContext(Dispatchers.Main) {
                currentPageBitmap = bmp
                isLoadingPdf = false
            }
        }
    }

    // Save PDF As Launcher (Zero-permission system document saver)
    val savePdfAsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { targetUri ->
        if (targetUri != null && pdfFile != null) {
            coroutineScope.launch(Dispatchers.IO) {
                val ok = CoursePdfGenerator.copyPdfToUri(context, pdfFile!!, targetUri)
                withContext(Dispatchers.Main) {
                    statusBannerMessage = if (ok) {
                        "✅ PDF புத்தகம் உங்கள் போனில் வெற்றிகரமாகச் சேமிக்கப்பட்டது!"
                    } else {
                        "⚠️ PDF சேமிப்பதில் சிக்கல் ஏற்பட்டது."
                    }
                }
            }
        }
    }

    // Open Custom PDF from Device Launcher
    val openCustomPdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { sourceUri ->
        if (sourceUri != null) {
            coroutineScope.launch(Dispatchers.IO) {
                val copied = CoursePdfGenerator.copyUriToCustomPdfFile(context, sourceUri)
                if (copied != null) {
                    val count = CoursePdfGenerator.getPdfPageCount(copied)
                    if (count > 0) {
                        val bmp = CoursePdfGenerator.renderPdfPageToBitmap(copied, 0)
                        withContext(Dispatchers.Main) {
                            pdfFile = copied
                            isCustomPdf = true
                            customPdfName = "Custom PDF Book ($count Pages)"
                            totalPageCount = count
                            currentPageIndex = 0
                            currentPageBitmap = bmp
                            viewMode = 0
                            statusBannerMessage = "📖 நீங்கள் தேர்வு செய்த PDF புத்தகம் ($count பக்கங்கள்) திறக்கப்பட்டது!"
                        }
                    }
                }
            }
        }
    }

    val modes = listOf(
        "📄 PDF புத்தகப் பக்கங்கள் (18 Pages)",
        "🎓 ஊடாடும் பாடப் பயிற்சி",
        "📑 பொருளடக்கம் (Index)"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Sub-mode switcher chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(modes) { index, title ->
                val selected = viewMode == index
                FilterChip(
                    selected = selected,
                    onClick = { viewMode = index },
                    label = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            maxLines = 1,
                            softWrap = false
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("spoken_english_pdf_mode_$index")
                )
            }
        }

        // Status Toast / Banner if user downloaded or opened PDF
        if (statusBannerMessage != null) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = statusBannerMessage!!,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF065F46),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { statusBannerMessage = null },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss",
                            tint = Color(0xFF065F46),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        when (viewMode) {
            0 -> PdfPageReaderView(
                viewModel = viewModel,
                currentPageIndex = currentPageIndex,
                totalPageCount = totalPageCount,
                currentPageBitmap = currentPageBitmap,
                isLoadingPdf = isLoadingPdf,
                isCustomPdf = isCustomPdf,
                customPdfName = customPdfName,
                zoomScale = zoomScale,
                offsetX = offsetX,
                offsetY = offsetY,
                onSelectPage = { currentPageIndex = it.coerceIn(0, (totalPageCount - 1).coerceAtLeast(0)) },
                onUpdateZoom = { newScale, newX, newY ->
                    zoomScale = newScale
                    offsetX = newX
                    offsetY = newY
                },
                onDownloadPdf = {
                    val activeFile = pdfFile
                    if (activeFile != null) {
                        coroutineScope.launch(Dispatchers.IO) {
                            val res = CoursePdfGenerator.savePdfToDownloads(context, activeFile)
                            withContext(Dispatchers.Main) {
                                res.onSuccess { savedPath ->
                                    statusBannerMessage = "✅ PDF சேமிக்கப்பட்டது: $savedPath"
                                }.onFailure {
                                    savePdfAsLauncher.launch(SpokenEnglishPdfCourseData.PDF_FILE_NAME)
                                }
                            }
                        }
                    }
                },
                onSharePdf = {
                    val activeFile = pdfFile
                    if (activeFile != null) {
                        val opened = CoursePdfGenerator.shareOrOpenPdf(context, activeFile, openDirectly = false)
                        if (!opened) {
                            savePdfAsLauncher.launch(SpokenEnglishPdfCourseData.PDF_FILE_NAME)
                        }
                    }
                },
                onOpenCustomPdf = {
                    openCustomPdfLauncher.launch(arrayOf("application/pdf"))
                },
                onResetToBuiltInPdf = {
                    coroutineScope.launch(Dispatchers.IO) {
                        val generated = CoursePdfGenerator.getOrCreateCoursePdfFile(context)
                        val count = CoursePdfGenerator.getPdfPageCount(generated).coerceAtLeast(1)
                        val bmp = CoursePdfGenerator.renderPdfPageToBitmap(generated, 0)
                        withContext(Dispatchers.Main) {
                            pdfFile = generated
                            isCustomPdf = false
                            totalPageCount = count
                            currentPageIndex = 0
                            currentPageBitmap = bmp
                            statusBannerMessage = "📕 தனம் டீச்சரின் 'Spoken English via Tamil' PDF புத்தகத்திற்குத் திரும்பியது!"
                        }
                    }
                },
                onStartRoleplay = onStartRoleplay
            )
            1 -> InteractivePdfCourseUnitsView(
                viewModel = viewModel,
                currentPageIndex = currentPageIndex,
                onSelectPage = { currentPageIndex = it },
                onOpenInPdfView = { pageIdx ->
                    currentPageIndex = pageIdx
                    viewMode = 0
                },
                onStartRoleplay = onStartRoleplay
            )
            2 -> PdfTableOfContentsView(
                viewModel = viewModel,
                onJumpToPdfPage = { pageIdx ->
                    currentPageIndex = pageIdx
                    viewMode = 0
                },
                onJumpToInteractivePage = { pageIdx ->
                    currentPageIndex = pageIdx
                    viewMode = 1
                }
            )
        }
    }
}

@Composable
private fun PdfPageReaderView(
    viewModel: MainViewModel,
    currentPageIndex: Int,
    totalPageCount: Int,
    currentPageBitmap: Bitmap?,
    isLoadingPdf: Boolean,
    isCustomPdf: Boolean,
    customPdfName: String,
    zoomScale: Float,
    offsetX: Float,
    offsetY: Float,
    onSelectPage: (Int) -> Unit,
    onUpdateZoom: (Float, Float, Float) -> Unit,
    onDownloadPdf: () -> Unit,
    onSharePdf: () -> Unit,
    onOpenCustomPdf: () -> Unit,
    onResetToBuiltInPdf: () -> Unit,
    onStartRoleplay: (RoleplayScenario) -> Unit
) {
    val pages = SpokenEnglishPdfCourseData.pages
    val activeCoursePage: PdfCoursePage? = if (!isCustomPdf && currentPageIndex in pages.indices) {
        pages[currentPageIndex]
    } else {
        null
    }

    var practicingSentence by remember { mutableStateOf<String?>(null) }
    val practiceResult by viewModel.lastPracticeResult.collectAsState()
    val isListening by viewModel.voiceManager.isListening.collectAsState()
    val partialSpeechText by viewModel.voiceManager.partialSpeechText.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Top Book Banner & PDF Actions (Download / Share / Open Custom PDF)
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF0F766E),
                                Color(0xFF1E3A8A)
                            )
                        ),
                        shape = RoundedCornerShape(18.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.PictureAsPdf,
                                    contentDescription = "PDF Course Book",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isCustomPdf) customPdfName else SpokenEnglishPdfCourseData.BOOK_TITLE_TA,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (isCustomPdf) {
                                    "உங்கள் போனிலிருந்து திறக்கப்பட்ட PDF புத்தகம்"
                                } else {
                                    "${SpokenEnglishPdfCourseData.BOOK_AUTHOR} • சுபிக்சா (சுபி, 9 வயது) பதிப்பு"
                                },
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 11.5.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // PDF File Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Button(
                            onClick = onDownloadPdf,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFDE68A),
                                contentColor = Color(0xFF1E293B)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("download_pdf_button")
                        ) {
                            Icon(Icons.Default.Download, contentDescription = "Save PDF", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("PDF சேமி", fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                        }

                        Button(
                            onClick = onSharePdf,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.2f),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("share_pdf_button")
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Share PDF", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("பகிர் / திற", fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                        }

                        Button(
                            onClick = if (isCustomPdf) onResetToBuiltInPdf else onOpenCustomPdf,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.2f),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("open_custom_pdf_button")
                        ) {
                            Icon(
                                imageVector = if (isCustomPdf) Icons.AutoMirrored.Filled.MenuBook else Icons.Default.FolderOpen,
                                contentDescription = "Open PDF",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isCustomPdf) "பாடப்புத்தகம்" else "வேறு PDF",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }
        }

        // 2. Quick Page Jump Chips (Page 1 .. Page 18)
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(totalPageCount) { pageIdx ->
                    val isSelected = pageIdx == currentPageIndex
                    val label = if (!isCustomPdf && pageIdx in pages.indices) {
                        "P.${pageIdx + 1}: ${pages[pageIdx].unitTag.substringBefore("•").trim()}"
                    } else {
                        "பக்கம் ${pageIdx + 1}"
                    }
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectPage(pageIdx) },
                        label = {
                            Text(
                                text = label,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    )
                }
            }
        }

        // 3. Page Navigation & Zoom Bar + Teacher Audio Bar
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { if (currentPageIndex > 0) onSelectPage(currentPageIndex - 1) },
                            enabled = currentPageIndex > 0,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Page", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("முந்தைய", fontSize = 11.sp)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "📄 PDF பக்கம் ${currentPageIndex + 1} / $totalPageCount",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            if (activeCoursePage != null) {
                                Text(
                                    text = activeCoursePage.dayRange,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = { if (currentPageIndex < totalPageCount - 1) onSelectPage(currentPageIndex + 1) },
                            enabled = currentPageIndex < totalPageCount - 1,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("அடுத்த", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Page", modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Zoom & Audio Teaching Controls Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    val nextZoom = (zoomScale - 0.25f).coerceAtLeast(1f)
                                    onUpdateZoom(nextZoom, if (nextZoom == 1f) 0f else offsetX, if (nextZoom == 1f) 0f else offsetY)
                                },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(Icons.Default.ZoomOut, contentDescription = "Zoom Out", modifier = Modifier.size(18.dp))
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.clickable { onUpdateZoom(1f, 0f, 0f) }
                            ) {
                                Text(
                                    text = "${(zoomScale * 100).toInt()}%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            IconButton(
                                onClick = {
                                    val nextZoom = (zoomScale + 0.35f).coerceAtMost(2.8f)
                                    onUpdateZoom(nextZoom, offsetX, offsetY)
                                },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(Icons.Default.ZoomIn, contentDescription = "Zoom In", modifier = Modifier.size(18.dp))
                            }
                        }

                        if (activeCoursePage != null) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(
                                    onClick = {
                                        val engSpeech = activeCoursePage.titleEnglish + ". " +
                                            activeCoursePage.sentences.joinToString(". ") { it.english }
                                        val tamSpeech = activeCoursePage.titleTamil + ". " +
                                            activeCoursePage.introExplanationTamil + ". " +
                                            activeCoursePage.sentences.joinToString(". ") { it.tamilMeaning }
                                        viewModel.voiceManager.speakBilingual(engSpeech, tamSpeech)
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Icon(Icons.Default.School, contentDescription = "Read Page Aloud", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("🔊 பக்கத்தை வாசி", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = {
                                        val firstSentence = activeCoursePage.sentences.firstOrNull()
                                        val scenario = RoleplayScenario(
                                            id = "pdf_page_${activeCoursePage.pageNumber}",
                                            title = activeCoursePage.titleEnglish,
                                            titleTamil = activeCoursePage.titleTamil,
                                            botRole = "Dhanam Teacher (தனம் டீச்சர்)",
                                            userRole = "Student Subiksha (Subi, Age 9 / சுபிக்சா)",
                                            starterMessage = firstSentence?.english
                                                ?: "Let's practice ${activeCoursePage.titleEnglish}, Subi!",
                                            starterTamil = firstSentence?.tamilMeaning
                                                ?: "வா சுபி! ${activeCoursePage.titleTamil} பாடத்தைப் பேசிப் பழகுவோம்."
                                        )
                                        viewModel.startScenario(scenario)
                                        onStartRoleplay(scenario)
                                    },
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.RecordVoiceOver, contentDescription = "Practice", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("பயில்", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Rendered PDF Page Canvas Card (with Pinch-to-Zoom & Pan)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                    .testTag("pdf_page_canvas_card")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(794f / 1160f)
                        .clipToBounds()
                        .background(Color.White)
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                val newScale = (zoomScale * zoom).coerceIn(1f, 3f)
                                val maxOffset = (newScale - 1f) * 450f
                                val newX = if (newScale > 1f) (offsetX + pan.x).coerceIn(-maxOffset, maxOffset) else 0f
                                val newY = if (newScale > 1f) (offsetY + pan.y).coerceIn(-maxOffset, maxOffset) else 0f
                                onUpdateZoom(newScale, newX, newY)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoadingPdf || currentPageBitmap == null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "PDF பக்கம் ${currentPageIndex + 1} தயாராகிறது...",
                                fontSize = 13.sp,
                                color = Color.DarkGray
                            )
                        }
                    } else {
                        Image(
                            bitmap = currentPageBitmap.asImageBitmap(),
                            contentDescription = "PDF Page ${currentPageIndex + 1}",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer(
                                    scaleX = zoomScale,
                                    scaleY = zoomScale,
                                    translationX = offsetX,
                                    translationY = offsetY
                                )
                        )
                    }
                }
            }
        }

        // 5. Interactive Audio & Speaking Cards for the Current PDF Page
        if (activeCoursePage != null) {
            if (practiceResult != null && practicingSentence != null) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (practiceResult!!.score >= 70) Color(0xFFECFDF5) else Color(0xFFFEF3C7)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🎤 சுபியின் உச்சரிப்பு மதிப்பீடு (Speaking Score)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                IconButton(
                                    onClick = {
                                        viewModel.clearPracticeResult()
                                        practicingSentence = null
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Close")
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            ScoreBadge(score = practiceResult!!.score)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "நீங்கள் பேசியது: \"${practiceResult!!.spokenText}\"",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = practiceResult!!.feedbackTamil,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "🔊 பக்கம் ${activeCoursePage.pageNumber} வாக்கியங்களைத் தொட்டுக் கேட்க & பேசிப் பழக:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            items(activeCoursePage.sentences) { sentence ->
                PdfSentenceInteractiveCard(
                    sentence = sentence,
                    isPracticing = isListening && practicingSentence == sentence.english,
                    partialSpeechText = partialSpeechText,
                    onSpeakBoth = {
                        viewModel.voiceManager.speakBilingual(sentence.english, sentence.tamilMeaning)
                    },
                    onSpeakEnglish = {
                        viewModel.voiceManager.speakEnglish(sentence.english)
                    },
                    onSpeakTamil = {
                        viewModel.voiceManager.speakTamil(sentence.tamilMeaning)
                    },
                    onTogglePractice = {
                        if (isListening && practicingSentence == sentence.english) {
                            viewModel.voiceManager.stopListening()
                        } else {
                            practicingSentence = sentence.english
                            viewModel.voiceManager.startListening(
                                languageCode = "en-IN",
                                onResult = { spoken ->
                                    viewModel.evaluateSpeakingPractice(sentence.english, spoken)
                                }
                            )
                        }
                    }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun InteractivePdfCourseUnitsView(
    viewModel: MainViewModel,
    currentPageIndex: Int,
    onSelectPage: (Int) -> Unit,
    onOpenInPdfView: (Int) -> Unit,
    onStartRoleplay: (RoleplayScenario) -> Unit
) {
    val pages = SpokenEnglishPdfCourseData.pages
    val safeIndex = currentPageIndex.coerceIn(pages.indices)
    val pageData = pages[safeIndex]

    var practicingSentence by remember { mutableStateOf<String?>(null) }
    val practiceResult by viewModel.lastPracticeResult.collectAsState()
    val isListening by viewModel.voiceManager.isListening.collectAsState()
    val partialSpeechText by viewModel.voiceManager.partialSpeechText.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Horizontal Unit / Page selector
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(pages) { idx, pg ->
                    val isSelected = idx == safeIndex
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectPage(idx) },
                        label = {
                            Text(
                                text = "பக்கம் ${pg.pageNumber}: ${pg.dayRange}",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    )
                }
            }
        }

        // Lesson Overview & Formula Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = "${pageData.unitTag} • ${pageData.dayRange}",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.clickable { onOpenInPdfView(safeIndex) }
                        ) {
                            Text(
                                text = "📄 PDF பக்கம் ${pageData.pageNumber} திற",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = pageData.titleEnglish,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = pageData.titleTamil,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = pageData.introExplanationTamil,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                    )

                    if (pageData.formulas.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = pageData.formulaBoxTitle,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                pageData.formulas.forEach { formula ->
                                    Text(
                                        text = "• $formula",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val eng = pageData.sentences.joinToString(". ") { it.english }
                                val tam = pageData.introExplanationTamil + ". " +
                                    pageData.sentences.joinToString(". ") { it.tamilMeaning }
                                viewModel.voiceManager.speakBilingual(eng, tam)
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Teach Lesson", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("பாடத்தை நடத்து", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = {
                                val firstSent = pageData.sentences.firstOrNull()
                                val scenario = RoleplayScenario(
                                    id = "course_unit_${pageData.pageNumber}",
                                    title = pageData.titleEnglish,
                                    titleTamil = pageData.titleTamil,
                                    botRole = "Dhanam Teacher (தனம் டீச்சர்)",
                                    userRole = "Student Subiksha (Subi, Age 9 / சுபிக்சா)",
                                    starterMessage = firstSent?.english ?: "Let's practice ${pageData.titleEnglish}!",
                                    starterTamil = firstSent?.tamilMeaning ?: "வா சுபி! இந்தப் பாடத்தைப் பேசிப் பழகுவோம்."
                                )
                                viewModel.startScenario(scenario)
                                onStartRoleplay(scenario)
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.RecordVoiceOver, contentDescription = "Practice", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("தனம் டீச்சருடன் பேசு", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Structured Course Table Card
        if (pageData.tableHeaders.isNotEmpty() && pageData.tableRows.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                                .padding(vertical = 8.dp, horizontal = 10.dp)
                        ) {
                            pageData.tableHeaders.forEach { hdr ->
                                Text(
                                    text = hdr,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.width(150.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        pageData.tableRows.forEachIndexed { rIdx, row ->
                            val cols = listOf(row.col1, row.col2, row.col3, row.col4).take(pageData.tableHeaders.size)
                            Row(
                                modifier = Modifier
                                    .background(
                                        if (rIdx % 2 == 0) MaterialTheme.colorScheme.surface else Color.Transparent,
                                        RoundedCornerShape(6.dp)
                                    )
                                    .clickable {
                                        viewModel.voiceManager.speakEnglish("${row.col1}. ${row.col2}. ${row.col3}")
                                    }
                                    .padding(vertical = 7.dp, horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                cols.forEachIndexed { cIdx, cell ->
                                    Text(
                                        text = cell,
                                        fontWeight = if (cIdx == 0) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 12.sp,
                                        color = if (cIdx == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.width(150.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Sentences Section
        item {
            Text(
                text = pageData.sentencesTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(pageData.sentences) { sentence ->
            PdfSentenceInteractiveCard(
                sentence = sentence,
                isPracticing = isListening && practicingSentence == sentence.english,
                partialSpeechText = partialSpeechText,
                onSpeakBoth = {
                    viewModel.voiceManager.speakBilingual(sentence.english, sentence.tamilMeaning)
                },
                onSpeakEnglish = {
                    viewModel.voiceManager.speakEnglish(sentence.english)
                },
                onSpeakTamil = {
                    viewModel.voiceManager.speakTamil(sentence.tamilMeaning)
                },
                onTogglePractice = {
                    if (isListening && practicingSentence == sentence.english) {
                        viewModel.voiceManager.stopListening()
                    } else {
                        practicingSentence = sentence.english
                        viewModel.voiceManager.startListening(
                            languageCode = "en-IN",
                            onResult = { spoken ->
                                viewModel.evaluateSpeakingPractice(sentence.english, spoken)
                            }
                        )
                    }
                }
            )
        }

        // Teacher Tip Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "👩‍🏫 ${pageData.teacherTipTamil}",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF78350F),
                    modifier = Modifier.padding(14.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PdfTableOfContentsView(
    viewModel: MainViewModel,
    onJumpToPdfPage: (Int) -> Unit,
    onJumpToInteractivePage: (Int) -> Unit
) {
    val pages = SpokenEnglishPdfCourseData.pages

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "📑 முழுப் பாடத்திட்டப் பொருளடக்கம் (18 PDF Pages)",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "எந்தப் பக்கத்தையும் நேரடியாக PDF வடிவத்திலோ அல்லது ஆடியோ பயிற்சி வடிவத்திலோ திறக்கலாம்.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                }
            }
        }

        itemsIndexed(pages) { index, page ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = "பக்கம் ${page.pageNumber} • ${page.dayRange}",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Text(
                            text = page.unitTag.substringBefore("•").trim(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = page.titleEnglish,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = page.titleTamil,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onJumpToPdfPage(index) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = "Open PDF Page", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("PDF பக்கம் திற", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = { onJumpToInteractivePage(index) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.School, contentDescription = "Interactive Lesson", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("ஆடியோ பயிற்சி", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun PdfSentenceInteractiveCard(
    sentence: PdfCourseSentence,
    isPracticing: Boolean,
    partialSpeechText: String,
    onSpeakBoth: () -> Unit,
    onSpeakEnglish: () -> Unit,
    onSpeakTamil: () -> Unit,
    onTogglePractice: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sentence.english,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                        modifier = Modifier.clickable { onSpeakBoth() }
                    ) {
                        Text(
                            text = "🔊 Both",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TtsSpeakerButton(
                        textToSpeak = sentence.english,
                        onSpeak = { onSpeakEnglish() },
                        modifier = Modifier.size(34.dp)
                    )
                    IconButton(
                        onClick = onSpeakTamil,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Speak Tamil",
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "🗣️ உச்சரிப்பு: ${sentence.tamilPronunciation}",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "🇮🇳 தமிழ் அர்த்தம்: ${sentence.tamilMeaning}",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (sentence.grammarNote.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "💡 குறிப்பு: ${sentence.grammarNote}",
                    fontSize = 11.5.sp,
                    color = Color(0xFFD97706),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onTogglePractice,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPracticing) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = if (isPracticing) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = "Speak and practice",
                    modifier = Modifier.size(17.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isPracticing) {
                        if (partialSpeechText.isNotBlank()) "\"$partialSpeechText\"" else "Listening... சுபி பேசிப் பார்க்கவும்"
                    } else {
                        "🎤 Speak & Test Pronunciation (பேசிப் பழகு)"
                    },
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
