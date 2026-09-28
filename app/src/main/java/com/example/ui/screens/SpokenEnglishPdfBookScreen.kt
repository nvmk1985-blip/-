package com.example.ui.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.lessons.FreePdfBookItem
import com.example.data.lessons.FreeSpokenEnglishPdfBooksData
import com.example.data.lessons.PdfCoursePage
import com.example.data.lessons.PdfCourseSentence
import com.example.data.lessons.RoleplayScenario
import com.example.data.lessons.SpokenEnglishPdfCourseData
import com.example.ui.components.ScoreBadge
import com.example.ui.components.TtsSpeakerButton
import com.example.ui.viewmodel.MainViewModel
import com.example.util.CoursePdfGenerator
import com.example.util.SavedPdfResult
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SpokenEnglishPdfBookSection(
    viewModel: MainViewModel,
    onStartRoleplay: (RoleplayScenario) -> Unit,
    onNavigateToSavedBooks: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var viewMode by remember { mutableIntStateOf(0) } // 0 = PDF Book Reader, 1 = Interactive Course, 2 = Table of Contents
    var currentPageIndex by remember { mutableIntStateOf(0) }
    var selectedBook by remember { mutableStateOf(FreeSpokenEnglishPdfBooksData.books.first()) }
    var activeBookPages by remember { mutableStateOf(SpokenEnglishPdfCourseData.pages) }
    var pdfFile by remember { mutableStateOf<File?>(null) }
    var isCustomPdf by remember { mutableStateOf(false) }
    var customPdfName by remember { mutableStateOf("") }
    var totalPageCount by remember { mutableIntStateOf(SpokenEnglishPdfCourseData.pages.size) }
    var isLoadingPdf by remember { mutableStateOf(true) }
    var statusBannerMessage by remember { mutableStateOf<String?>(null) }
    var isDownloadingPdf by remember { mutableStateOf(false) }
    var downloadedPdfResult by remember { mutableStateOf<SavedPdfResult?>(null) }

    // Continuous vertical scroll (all pages) vs single page mode
    var continuousScrollAllPages by remember { mutableStateOf(true) }
    var zoomScale by remember { mutableFloatStateOf(1f) }

    // Initialize selected Spoken English via Tamil PDF Course Book
    LaunchedEffect(selectedBook, isCustomPdf) {
        if (!isCustomPdf) {
            isLoadingPdf = true
            withContext(Dispatchers.IO) {
                val pages = selectedBook.pagesBuilder()
                val generated = CoursePdfGenerator.getOrCreateBookPdfFile(context, selectedBook)
                val count = CoursePdfGenerator.getPdfPageCount(generated).coerceAtLeast(pages.size)
                withContext(Dispatchers.Main) {
                    activeBookPages = pages
                    pdfFile = generated
                    totalPageCount = count
                    currentPageIndex = 0
                    isLoadingPdf = false
                }
            }
        }
    }

    // Save PDF As Launcher (System Document Picker so user can pick any folder on phone/Drive)
    val savePdfAsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { targetUri ->
        if (targetUri != null) {
            coroutineScope.launch(Dispatchers.IO) {
                val fileToSave = pdfFile ?: CoursePdfGenerator.getOrCreateBookPdfFile(context, selectedBook)
                val ok = CoursePdfGenerator.copyPdfToUri(context, fileToSave, targetUri)
                withContext(Dispatchers.Main) {
                    val msg = if (ok) {
                        "✅ PDF புத்தகம் நீங்கள் தேர்வு செய்த ஃபோல்டரில் சேமிக்கப்பட்டது!"
                    } else {
                        "⚠️ PDF சேமிப்பதில் சிக்கல் ஏற்பட்டது."
                    }
                    statusBannerMessage = msg
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
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
                        withContext(Dispatchers.Main) {
                            pdfFile = copied
                            isCustomPdf = true
                            customPdfName = "Custom PDF Book ($count Pages)"
                            totalPageCount = count
                            currentPageIndex = 0
                            viewMode = 0
                            statusBannerMessage = "📖 நீங்கள் தேர்வு செய்த PDF புத்தகம் ($count பக்கங்கள்) திறக்கப்பட்டது!"
                        }
                    }
                }
            }
        }
    }

    // Download Confirmation Dialog
    if (downloadedPdfResult != null) {
        val result = downloadedPdfResult!!
        PdfDownloadSuccessDialog(
            savedResult = result,
            onDismiss = { downloadedPdfResult = null },
            onOpenPdfExternally = {
                downloadedPdfResult = null
                CoursePdfGenerator.shareOrOpenPdf(context, result.localFile, openDirectly = true)
            },
            onSaveToCustomFolder = {
                downloadedPdfResult = null
                savePdfAsLauncher.launch(result.fileName)
            },
            onSharePdf = {
                downloadedPdfResult = null
                CoursePdfGenerator.shareOrOpenPdf(context, result.localFile, openDirectly = false)
            }
        )
    }

    val modes = listOf(
        "📄 PDF புத்தகப் பக்கங்கள் ($totalPageCount Pages)",
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
                .padding(horizontal = 16.dp, vertical = 6.dp),
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
                pdfFile = pdfFile,
                selectedBook = selectedBook,
                allBooks = FreeSpokenEnglishPdfBooksData.books,
                activeBookPages = activeBookPages,
                currentPageIndex = currentPageIndex,
                totalPageCount = totalPageCount,
                isLoadingPdf = isLoadingPdf,
                isDownloadingPdf = isDownloadingPdf,
                isCustomPdf = isCustomPdf,
                customPdfName = customPdfName,
                continuousScrollAllPages = continuousScrollAllPages,
                zoomScale = zoomScale,
                onSelectBook = { book ->
                    isCustomPdf = false
                    selectedBook = book
                },
                onToggleContinuousScroll = { continuousScrollAllPages = it },
                onSelectPage = { currentPageIndex = it.coerceIn(0, (totalPageCount - 1).coerceAtLeast(0)) },
                onUpdateZoom = { newScale -> zoomScale = newScale },
                onDownloadPdf = {
                    if (!isDownloadingPdf) {
                        isDownloadingPdf = true
                        coroutineScope.launch(Dispatchers.IO) {
                            val fileToSave = pdfFile ?: CoursePdfGenerator.getOrCreateBookPdfFile(context, selectedBook)
                            val desiredName = if (isCustomPdf) "Custom_Spoken_English_Book.pdf" else selectedBook.fileName
                            val res = CoursePdfGenerator.savePdfToDownloads(
                                context = context,
                                pdfFile = fileToSave,
                                customFileName = desiredName,
                                bookId = selectedBook.id
                            )
                            withContext(Dispatchers.Main) {
                                isDownloadingPdf = false
                                res.onSuccess { savedInfo ->
                                    statusBannerMessage = "✅ PDF டவுன்லோடு முடிந்தது: ${savedInfo.displayPath}"
                                    Toast.makeText(
                                        context,
                                        "✅ PDF டவுன்லோடு ஆனது: ${savedInfo.displayPath}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    downloadedPdfResult = savedInfo
                                }.onFailure {
                                    savePdfAsLauncher.launch(desiredName)
                                }
                            }
                        }
                    }
                },
                onSavePdfAs = {
                    val desiredName = if (isCustomPdf) "Custom_Spoken_English_Book.pdf" else selectedBook.fileName
                    savePdfAsLauncher.launch(desiredName)
                },
                onSharePdf = {
                    coroutineScope.launch(Dispatchers.IO) {
                        val fileToShare = pdfFile ?: CoursePdfGenerator.getOrCreateBookPdfFile(context, selectedBook)
                        withContext(Dispatchers.Main) {
                            val opened = CoursePdfGenerator.shareOrOpenPdf(context, fileToShare, openDirectly = false)
                            if (!opened) {
                                savePdfAsLauncher.launch(selectedBook.fileName)
                            }
                        }
                    }
                },
                onOpenCustomPdf = {
                    openCustomPdfLauncher.launch(arrayOf("application/pdf"))
                },
                onResetToBuiltInPdf = {
                    isCustomPdf = false
                    selectedBook = FreeSpokenEnglishPdfBooksData.books.first()
                },
                onNavigateToSavedBooks = onNavigateToSavedBooks,
                onStartRoleplay = onStartRoleplay
            )
            1 -> InteractivePdfCourseUnitsView(
                viewModel = viewModel,
                pages = activeBookPages,
                currentPageIndex = currentPageIndex,
                onSelectPage = { currentPageIndex = it },
                onOpenInPdfView = { pageIdx ->
                    currentPageIndex = pageIdx
                    viewMode = 0
                },
                onStartRoleplay = onStartRoleplay
            )
            2 -> PdfTableOfContentsView(
                pages = activeBookPages,
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
    pdfFile: File?,
    selectedBook: FreePdfBookItem,
    allBooks: List<FreePdfBookItem>,
    activeBookPages: List<PdfCoursePage>,
    currentPageIndex: Int,
    totalPageCount: Int,
    isLoadingPdf: Boolean,
    isDownloadingPdf: Boolean,
    isCustomPdf: Boolean,
    customPdfName: String,
    continuousScrollAllPages: Boolean,
    zoomScale: Float,
    onSelectBook: (FreePdfBookItem) -> Unit,
    onToggleContinuousScroll: (Boolean) -> Unit,
    onSelectPage: (Int) -> Unit,
    onUpdateZoom: (Float) -> Unit,
    onDownloadPdf: () -> Unit,
    onSavePdfAs: () -> Unit,
    onSharePdf: () -> Unit,
    onOpenCustomPdf: () -> Unit,
    onResetToBuiltInPdf: () -> Unit,
    onNavigateToSavedBooks: (() -> Unit)?,
    onStartRoleplay: (RoleplayScenario) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    // Track which pages have their interactive sentences expanded in continuous scroll mode
    val expandedSentencesByPage = remember { mutableStateMapOf<Int, Boolean>() }

    var practicingSentence by remember { mutableStateOf<String?>(null) }
    val practiceResult by viewModel.lastPracticeResult.collectAsState()
    val isListening by viewModel.voiceManager.isListening.collectAsState()
    val partialSpeechText by viewModel.voiceManager.partialSpeechText.collectAsState()

    // Header items count before the PDF page items in LazyColumn (Book selector + Top Hero Card + Scroll/Zoom bar)
    val headerItemsCount = 3

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag("pdf_reader_lazy_column")
        ) {
            // ITEM 0: Switch between the 8 Free Spoken English via Tamil PDF Books + Link to Saved Page
            item(key = "book_selector_row") {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📚 PDF பாடப்புத்தகம் தேர்வு (8 Free Books):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (onNavigateToSavedBooks != null) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.clickable { onNavigateToSavedBooks() }
                            ) {
                                Text(
                                    text = "📥 Save Page-ல் Free Links →",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(allBooks, key = { it.id }) { book ->
                            val isSelected = !isCustomPdf && book.id == selectedBook.id
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSelectBook(book) },
                                label = {
                                    Text(
                                        text = book.titleTamil.substringBefore("—").trim(),
                                        fontSize = 11.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // ITEM 1: Top Book Banner & Instant PDF Download / Save As / Share Buttons
            item(key = "top_pdf_hero_card") {
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
                                        tint = Color(0xFFFDE68A),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isCustomPdf) customPdfName else selectedBook.titleTamil,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.5.sp
                                )
                                Text(
                                    text = if (isCustomPdf) {
                                        "உங்கள் போனிலிருந்து திறக்கப்பட்ட PDF புத்தகம் ($totalPageCount பக்கங்கள்)"
                                    } else {
                                        selectedBook.authorAndEditionTamil
                                    },
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Primary Download & Save Row
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
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                                modifier = Modifier
                                    .weight(1.15f)
                                    .testTag("download_pdf_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Download PDF",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isDownloadingPdf) "சேமிக்கிறது..." else "📥 PDF Download",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }

                            Button(
                                onClick = onSavePdfAs,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White.copy(alpha = 0.22f),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("save_as_pdf_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SaveAlt,
                                    contentDescription = "Save PDF to Folder",
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "போனில் சேமி",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }

                            Button(
                                onClick = onSharePdf,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White.copy(alpha = 0.22f),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                                modifier = Modifier
                                    .weight(0.85f)
                                    .testTag("share_pdf_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share PDF",
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "பகிர்",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Secondary Row: Open custom PDF from device or reset
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "💡 மேலே-கீழே தள்ளி (Scroll Up/Down) எல்லாப் பக்கங்களையும் படிக்கலாம்",
                                fontSize = 10.5.sp,
                                color = Color(0xFFFEF08A),
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White.copy(alpha = 0.18f),
                                modifier = Modifier
                                    .clickable {
                                        if (isCustomPdf) onResetToBuiltInPdf() else onOpenCustomPdf()
                                    }
                                    .testTag("open_custom_pdf_button")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isCustomPdf) Icons.AutoMirrored.Filled.MenuBook else Icons.Default.FolderOpen,
                                        contentDescription = "Open PDF",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isCustomPdf) "பாடப்புத்தகம்" else "வேறு PDF திற",
                                        color = Color.White,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ITEM 2: Page Jump Chips + Scroll Mode Toggle (All Pages Vertical Scroll vs Single Page) + Zoom Controls
            item(key = "page_jump_and_scroll_controls") {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        // Quick Page Jump Chips
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(totalPageCount) { pageIdx ->
                                val isSelected = pageIdx == currentPageIndex
                                val label = if (!isCustomPdf && pageIdx in activeBookPages.indices) {
                                    "P.${pageIdx + 1}: ${activeBookPages[pageIdx].unitTag.substringBefore("•").trim()}"
                                } else {
                                    "பக்கம் ${pageIdx + 1}"
                                }
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        onSelectPage(pageIdx)
                                        if (continuousScrollAllPages) {
                                            coroutineScope.launch {
                                                lazyListState.animateScrollToItem(headerItemsCount + pageIdx)
                                            }
                                        }
                                    },
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

                        Spacer(modifier = Modifier.height(6.dp))

                        // Scroll Mode Switcher + Zoom Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                FilterChip(
                                    selected = continuousScrollAllPages,
                                    onClick = { onToggleContinuousScroll(true) },
                                    label = {
                                        Text(
                                            text = "📜 தொடர் பக்கங்கள் (Scroll Up/Down)",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                )
                                FilterChip(
                                    selected = !continuousScrollAllPages,
                                    onClick = { onToggleContinuousScroll(false) },
                                    label = {
                                        Text(
                                            text = "📄 தனிப் பக்கம்",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                )
                            }

                            // Zoom Controls (Never blocks vertical scrolling!)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        val nextZoom = (zoomScale - 0.25f).coerceAtLeast(1f)
                                        onUpdateZoom(nextZoom)
                                    },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(Icons.Default.ZoomOut, contentDescription = "Zoom Out", modifier = Modifier.size(17.dp))
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier.clickable { onUpdateZoom(1f) }
                                ) {
                                    Text(
                                        text = "${(zoomScale * 100).toInt()}%",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        val nextZoom = (zoomScale + 0.25f).coerceAtMost(2.25f)
                                        onUpdateZoom(nextZoom)
                                    },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(Icons.Default.ZoomIn, contentDescription = "Zoom In", modifier = Modifier.size(17.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Speaking Practice Score Banner if active
            if (practiceResult != null && practicingSentence != null) {
                item(key = "speaking_score_card") {
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

            // CONTINUOUS VERTICAL SCROLL MODE: All pages in LazyColumn for effortless Up & Down scrolling!
            if (continuousScrollAllPages) {
                items(
                    count = totalPageCount,
                    key = { pageIdx -> "continuous_pdf_page_${selectedBook.id}_${isCustomPdf}_$pageIdx" }
                ) { pageIdx ->
                    val pageCourseData = if (!isCustomPdf && pageIdx in activeBookPages.indices) {
                        activeBookPages[pageIdx]
                    } else {
                        null
                    }
                    val showSentences = expandedSentencesByPage[pageIdx] ?: (pageIdx == 0)

                    LazyPdfPageItemCard(
                        pdfFile = pdfFile,
                        pageIndex = pageIdx,
                        totalPageCount = totalPageCount,
                        coursePage = pageCourseData,
                        zoomScale = zoomScale,
                        isSentencesExpanded = showSentences,
                        onToggleSentences = {
                            expandedSentencesByPage[pageIdx] = !showSentences
                        },
                        onSpeakPage = { pg ->
                            val engSpeech = pg.titleEnglish + ". " +
                                pg.sentences.joinToString(". ") { it.english }
                            val tamSpeech = pg.titleTamil + ". " +
                                pg.introExplanationTamil + ". " +
                                pg.sentences.joinToString(". ") { it.tamilMeaning }
                            viewModel.voiceManager.speakBilingual(engSpeech, tamSpeech)
                        },
                        onStartPageRoleplay = { pg ->
                            val firstSentence = pg.sentences.firstOrNull()
                            val scenario = RoleplayScenario(
                                id = "pdf_page_${pg.pageNumber}",
                                title = pg.titleEnglish,
                                titleTamil = pg.titleTamil,
                                botRole = "Dhanam Teacher (தனம் டீச்சர்)",
                                userRole = "Student Subiksha (Subi, Age 9 / சுபிக்சா)",
                                starterMessage = firstSentence?.english
                                    ?: "Let's practice ${pg.titleEnglish}, Subi!",
                                starterTamil = firstSentence?.tamilMeaning
                                    ?: "வா சுபி! ${pg.titleTamil} பாடத்தைப் பேசிப் பழகுவோம்."
                            )
                            viewModel.startScenario(scenario)
                            onStartRoleplay(scenario)
                        },
                        isListening = isListening,
                        practicingSentence = practicingSentence,
                        partialSpeechText = partialSpeechText,
                        onSpeakBoth = { s ->
                            viewModel.voiceManager.speakBilingual(s.english, s.tamilMeaning)
                        },
                        onSpeakEnglish = { s ->
                            viewModel.voiceManager.speakEnglish(s.english)
                        },
                        onSpeakTamil = { s ->
                            viewModel.voiceManager.speakTamil(s.tamilMeaning)
                        },
                        onToggleSentencePractice = { s ->
                            if (isListening && practicingSentence == s.english) {
                                viewModel.voiceManager.stopListening()
                            } else {
                                practicingSentence = s.english
                                viewModel.voiceManager.startListening(
                                    languageCode = "en-IN",
                                    onResult = { spoken ->
                                        viewModel.evaluateSpeakingPractice(s.english, spoken)
                                    }
                                )
                            }
                        }
                    )
                }
            } else {
                // SINGLE PAGE VIEW MODE (Also 100% free of gesture blockers so vertical scrolling works!)
                val safePageIdx = currentPageIndex.coerceIn(0, (totalPageCount - 1).coerceAtLeast(0))
                val activeCoursePage = if (!isCustomPdf && safePageIdx in activeBookPages.indices) {
                    activeBookPages[safePageIdx]
                } else {
                    null
                }

                item(key = "single_page_nav_bar") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { if (safePageIdx > 0) onSelectPage(safePageIdx - 1) },
                            enabled = safePageIdx > 0,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("முந்தைய", fontSize = 11.sp)
                        }

                        Text(
                            text = "📄 பக்கம் ${safePageIdx + 1} / $totalPageCount",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.5.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        OutlinedButton(
                            onClick = { if (safePageIdx < totalPageCount - 1) onSelectPage(safePageIdx + 1) },
                            enabled = safePageIdx < totalPageCount - 1,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("அடுத்த", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next", modifier = Modifier.size(16.dp))
                        }
                    }
                }

                item(key = "single_pdf_page_card_$safePageIdx") {
                    LazyPdfPageItemCard(
                        pdfFile = pdfFile,
                        pageIndex = safePageIdx,
                        totalPageCount = totalPageCount,
                        coursePage = activeCoursePage,
                        zoomScale = zoomScale,
                        isSentencesExpanded = true,
                        onToggleSentences = {},
                        onSpeakPage = { pg ->
                            val engSpeech = pg.titleEnglish + ". " +
                                pg.sentences.joinToString(". ") { it.english }
                            val tamSpeech = pg.titleTamil + ". " +
                                pg.introExplanationTamil + ". " +
                                pg.sentences.joinToString(". ") { it.tamilMeaning }
                            viewModel.voiceManager.speakBilingual(engSpeech, tamSpeech)
                        },
                        onStartPageRoleplay = { pg ->
                            val firstSentence = pg.sentences.firstOrNull()
                            val scenario = RoleplayScenario(
                                id = "pdf_page_${pg.pageNumber}",
                                title = pg.titleEnglish,
                                titleTamil = pg.titleTamil,
                                botRole = "Dhanam Teacher (தனம் டீச்சர்)",
                                userRole = "Student Subiksha (Subi, Age 9 / சுபிக்சா)",
                                starterMessage = firstSentence?.english
                                    ?: "Let's practice ${pg.titleEnglish}, Subi!",
                                starterTamil = firstSentence?.tamilMeaning
                                    ?: "வா சுபி! ${pg.titleTamil} பாடத்தைப் பேசிப் பழகுவோம்."
                            )
                            viewModel.startScenario(scenario)
                            onStartRoleplay(scenario)
                        },
                        isListening = isListening,
                        practicingSentence = practicingSentence,
                        partialSpeechText = partialSpeechText,
                        onSpeakBoth = { s ->
                            viewModel.voiceManager.speakBilingual(s.english, s.tamilMeaning)
                        },
                        onSpeakEnglish = { s ->
                            viewModel.voiceManager.speakEnglish(s.english)
                        },
                        onSpeakTamil = { s ->
                            viewModel.voiceManager.speakTamil(s.tamilMeaning)
                        },
                        onToggleSentencePractice = { s ->
                            if (isListening && practicingSentence == s.english) {
                                viewModel.voiceManager.stopListening()
                            } else {
                                practicingSentence = s.english
                                viewModel.voiceManager.startListening(
                                    languageCode = "en-IN",
                                    onResult = { spoken ->
                                        viewModel.evaluateSpeakingPractice(s.english, spoken)
                                    }
                                )
                            }
                        }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(56.dp)) }
        }

        // Floating Up / Down Scroll Helper Buttons on Right Edge for 1-Tap Vertical Scrolling (மேலே / கீழே)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SmallFloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        lazyListState.animateScrollBy(-680f)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp),
                modifier = Modifier.testTag("pdf_scroll_up_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = "Scroll Up (மேலே செல்)"
                )
            }

            SmallFloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        lazyListState.animateScrollBy(680f)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp),
                modifier = Modifier.testTag("pdf_scroll_down_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = "Scroll Down (கீழே செல்)"
                )
            }
        }
    }
}

@Composable
fun LazyPdfPageItemCard(
    pdfFile: File?,
    pageIndex: Int,
    totalPageCount: Int,
    coursePage: PdfCoursePage?,
    zoomScale: Float,
    isSentencesExpanded: Boolean,
    onToggleSentences: () -> Unit,
    onSpeakPage: (PdfCoursePage) -> Unit,
    onStartPageRoleplay: (PdfCoursePage) -> Unit,
    isListening: Boolean,
    practicingSentence: String?,
    partialSpeechText: String,
    onSpeakBoth: (PdfCourseSentence) -> Unit,
    onSpeakEnglish: (PdfCourseSentence) -> Unit,
    onSpeakTamil: (PdfCourseSentence) -> Unit,
    onToggleSentencePractice: (PdfCourseSentence) -> Unit
) {
    val pageBitmap by produceState<Bitmap?>(initialValue = null, pdfFile, pageIndex) {
        val activeFile = pdfFile
        if (activeFile != null) {
            value = withContext(Dispatchers.IO) {
                CoursePdfGenerator.renderPdfPageToBitmap(activeFile, pageIndex)
            }
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .testTag("pdf_page_canvas_card_$pageIndex")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Page Top Bar with Page Number + Teacher Audio Read Aloud Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "📄 PDF பக்கம் ${pageIndex + 1} / $totalPageCount" +
                            (coursePage?.let { " • ${it.dayRange}" } ?: ""),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.5.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (coursePage != null) {
                        Text(
                            text = coursePage.titleTamil,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }

                if (coursePage != null) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { onSpeakPage(coursePage) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = "Read Page",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "🔊 வாசி",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.clickable { onStartPageRoleplay(coursePage) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RecordVoiceOver,
                                    contentDescription = "Roleplay",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "பயில்",
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Rendered PDF Page Image — NO gesture blocker so vertical scrolling is 100% smooth!
            if (zoomScale <= 1.01f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(794f / 1160f)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    if (pageBitmap == null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "PDF பக்கம் ${pageIndex + 1} தயாராகிறது...",
                                fontSize = 12.sp,
                                color = Color.DarkGray
                            )
                        }
                    } else {
                        Image(
                            bitmap = pageBitmap!!.asImageBitmap(),
                            contentDescription = "PDF Page ${pageIndex + 1}",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            } else {
                // When zoomed in (> 100%), use horizontalScroll so vertical scroll (LazyColumn) still works smoothly!
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    val zoomedWidthDp = (350f * zoomScale).dp
                    Box(
                        modifier = Modifier
                            .width(zoomedWidthDp)
                            .aspectRatio(794f / 1160f),
                        contentAlignment = Alignment.Center
                    ) {
                        if (pageBitmap != null) {
                            Image(
                                bitmap = pageBitmap!!.asImageBitmap(),
                                contentDescription = "PDF Page ${pageIndex + 1} Zoomed",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            // Expandable / Inline Interactive Speaking Cards for this Page
            if (coursePage != null && coursePage.sentences.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleSentences() },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🎤 பக்கம் ${coursePage.pageNumber} ஆடியோ & உச்சரிப்பு வாக்கியங்கள் (${coursePage.sentences.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = if (isSentencesExpanded) "சுருக்கு ▲" else "திற ▼",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    if (isSentencesExpanded) {
                        coursePage.sentences.forEach { sentence ->
                            PdfSentenceInteractiveCard(
                                sentence = sentence,
                                isPracticing = isListening && practicingSentence == sentence.english,
                                partialSpeechText = partialSpeechText,
                                onSpeakBoth = { onSpeakBoth(sentence) },
                                onSpeakEnglish = { onSpeakEnglish(sentence) },
                                onSpeakTamil = { onSpeakTamil(sentence) },
                                onTogglePractice = { onToggleSentencePractice(sentence) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PdfDownloadSuccessDialog(
    savedResult: SavedPdfResult,
    onDismiss: () -> Unit,
    onOpenPdfExternally: () -> Unit,
    onSaveToCustomFolder: () -> Unit,
    onSharePdf: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "PDF Downloaded",
                tint = Color(0xFF059669),
                modifier = Modifier.size(42.dp)
            )
        },
        title = {
            Text(
                text = "✅ PDF புத்தகம் டவுன்லோடு ஆனது!",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 17.sp,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "உங்கள் 'Spoken English via Tamil' PDF பாடப்புத்தகம் போனில் வெற்றிகரமாகச் சேமிக்கப்பட்டது.",
                    fontSize = 13.sp
                )
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "📄 கோப்பு: ${savedResult.fileName}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "📂 இடம்: ${savedResult.displayPath} (${savedResult.fileSizeKb} KB)",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = onOpenPdfExternally,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(17.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("📂 டவுன்லோடு ஆன PDF-ஐத் திற (Open PDF)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onSaveToCustomFolder,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.SaveAlt, contentDescription = null, modifier = Modifier.size(17.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("💾 வேறு ஃபோல்டரில் சேமி (Save As...)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onSharePdf,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(17.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("📤 WhatsApp / Drive-ல் பகிர் (Share)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("மூடு (Close)", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun InteractivePdfCourseUnitsView(
    viewModel: MainViewModel,
    pages: List<PdfCoursePage>,
    currentPageIndex: Int,
    onSelectPage: (Int) -> Unit,
    onOpenInPdfView: (Int) -> Unit,
    onStartRoleplay: (RoleplayScenario) -> Unit
) {
    val safeIndex = currentPageIndex.coerceIn(pages.indices)
    val pageData = pages[safeIndex]

    var practicingSentence by remember { mutableStateOf<String?>(null) }
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
    pages: List<PdfCoursePage>,
    onJumpToPdfPage: (Int) -> Unit,
    onJumpToInteractivePage: (Int) -> Unit
) {
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
                        text = "📑 முழுப் பாடத்திட்டப் பொருளடக்கம் (${pages.size} PDF Pages)",
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
fun PdfSentenceInteractiveCard(
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sentence.english,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.5.sp,
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
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "🇮🇳 தமிழ் அர்த்தம்: ${sentence.tamilMeaning}",
                fontSize = 13.sp,
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
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
