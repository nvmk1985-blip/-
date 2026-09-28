package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.TutorEngine
import com.example.data.lessons.FreePdfBookItem
import com.example.data.lessons.FreeSpokenEnglishPdfBooksData
import com.example.data.lessons.PdfCoursePage
import com.example.ui.components.TtsSpeakerButton
import com.example.ui.viewmodel.MainViewModel
import com.example.util.CoursePdfGenerator
import com.example.util.SavedPdfResult
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SavedScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val savedList by viewModel.savedPhrases.collectAsState()
    val lazyListState = rememberLazyListState()

    // 0 = All (Free PDF Books + Saved Phrases), 1 = Free Spoken English PDF Books & Links, 2 = Saved Phrases
    var selectedSectionTab by remember { mutableIntStateOf(0) }

    // Track downloaded book paths so we can show "✅ Downloaded" badge
    var downloadedBooksMap by remember {
        mutableStateOf(CoursePdfGenerator.getDownloadedBooksMap(context))
    }
    var downloadingBookId by remember { mutableStateOf<String?>(null) }
    var downloadedPdfResult by remember { mutableStateOf<SavedPdfResult?>(null) }
    var pendingFileForSaveAs by remember { mutableStateOf<File?>(null) }

    // In-App Smooth PDF Reader state when user clicks "📖 ஆப்பிலேயே PDF படி" on any book in Save Page
    var activeReaderBook by remember { mutableStateOf<FreePdfBookItem?>(null) }
    var activeReaderFile by remember { mutableStateOf<File?>(null) }
    var activeReaderPages by remember { mutableStateOf<List<PdfCoursePage>>(emptyList()) }

    val savePdfAsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { targetUri ->
        val sourceFile = pendingFileForSaveAs
        if (targetUri != null && sourceFile != null) {
            coroutineScope.launch(Dispatchers.IO) {
                val ok = CoursePdfGenerator.copyPdfToUri(context, sourceFile, targetUri)
                withContext(Dispatchers.Main) {
                    val msg = if (ok) {
                        "✅ PDF புத்தகம் உங்கள் போனில் வெற்றிகரமாகச் சேமிக்கப்பட்டது!"
                    } else {
                        "⚠️ PDF சேமிப்பதில் சிக்கல் ஏற்பட்டது."
                    }
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

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
                pendingFileForSaveAs = result.localFile
                downloadedPdfResult = null
                savePdfAsLauncher.launch(result.fileName)
            },
            onSharePdf = {
                downloadedPdfResult = null
                CoursePdfGenerator.shareOrOpenPdf(context, result.localFile, openDirectly = false)
            }
        )
    }

    // If user opened a book from the Save page to read in-app, show the smooth-scrolling PDF Reader
    if (activeReaderBook != null && activeReaderFile != null) {
        BackHandler {
            activeReaderBook = null
            activeReaderFile = null
        }
        SavedBookInAppPdfReader(
            viewModel = viewModel,
            bookItem = activeReaderBook!!,
            pdfFile = activeReaderFile!!,
            pages = activeReaderPages,
            onBack = {
                activeReaderBook = null
                activeReaderFile = null
            },
            onDownloadBook = { book ->
                coroutineScope.launch(Dispatchers.IO) {
                    val file = CoursePdfGenerator.getOrCreateBookPdfFile(context, book)
                    val res = CoursePdfGenerator.savePdfToDownloads(
                        context = context,
                        pdfFile = file,
                        customFileName = book.fileName,
                        bookId = book.id
                    )
                    withContext(Dispatchers.Main) {
                        downloadedBooksMap = CoursePdfGenerator.getDownloadedBooksMap(context)
                        res.onSuccess { info ->
                            Toast.makeText(
                                context,
                                "✅ PDF டவுன்லோடு ஆனது: ${info.displayPath}",
                                Toast.LENGTH_LONG
                            ).show()
                            downloadedPdfResult = info
                        }
                    }
                }
            }
        )
        return
    }

    val freeBooks = FreeSpokenEnglishPdfBooksData.books
    val tabs = listOf(
        "📚 இலவச PDF புத்தகங்கள் & Links (${freeBooks.size})",
        "🔗 Free Download Links",
        "🔖 சேமித்த வாக்கியங்கள் (${savedList.size})"
    )

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag("saved_screen_lazy_column")
        ) {
            // Top Header Banner
            item(key = "saved_header_banner") {
                Card(
                    shape = RoundedCornerShape(20.dp),
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
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(46.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.PictureAsPdf,
                                        contentDescription = "Free PDF Books",
                                        tint = Color(0xFFFDE68A),
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "📚 Spoken English via Tamil — Free PDF Books & Links",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "தமிழ் வழியே ஸ்போக்கன் இங்கிலீஷ் இலவச PDF புத்தகங்கள், நேரடி Download Links & சேமித்த வாக்கியங்கள்",
                                    color = Color.White.copy(alpha = 0.92f),
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Download All / Main 30-Day Course Book Quick Action
                        Button(
                            onClick = {
                                val mainBook = freeBooks.first()
                                downloadingBookId = mainBook.id
                                coroutineScope.launch(Dispatchers.IO) {
                                    val file = CoursePdfGenerator.getOrCreateBookPdfFile(context, mainBook)
                                    val res = CoursePdfGenerator.savePdfToDownloads(
                                        context = context,
                                        pdfFile = file,
                                        customFileName = mainBook.fileName,
                                        bookId = mainBook.id
                                    )
                                    withContext(Dispatchers.Main) {
                                        downloadingBookId = null
                                        downloadedBooksMap = CoursePdfGenerator.getDownloadedBooksMap(context)
                                        res.onSuccess { info ->
                                            Toast.makeText(
                                                context,
                                                "✅ PDF டவுன்லோடு ஆனது: ${info.displayPath}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            downloadedPdfResult = info
                                        }.onFailure {
                                            pendingFileForSaveAs = file
                                            savePdfAsLauncher.launch(mainBook.fileName)
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFDE68A),
                                contentColor = Color(0xFF0F172A)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("quick_download_main_pdf_button")
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "📥 30-நாள் முழுப் பாடப்புத்தகம் (18 Pages PDF) உடனடி Download",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Section Filter Tabs
            item(key = "saved_filter_tabs") {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    itemsIndexed(tabs) { idx, title ->
                        val selected = selectedSectionTab == idx
                        FilterChip(
                            selected = selected,
                            onClick = { selectedSectionTab = idx },
                            label = {
                                Text(
                                    text = title,
                                    fontSize = 12.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }

            // SECTION 1: Quick Summary Table of Free Spoken English via Tamil Download Links (when tab == 1)
            if (selectedSectionTab == 1) {
                item(key = "quick_links_directory_card") {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "🌐 இலவச Spoken English via Tamil — Direct Download Links பட்டியல்",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "கீழே உள்ள எந்த லின்க்கையும் தொட்டு நேரடியாக PDF புத்தகத்தை போனில் டவுன்லோடு செய்யலாம் அல்லது பிரவுசரில் திறக்கலாம்:",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }

            // SECTION 2: 8 Free Spoken English via Tamil PDF Books & Download Links Cards
            if (selectedSectionTab == 0 || selectedSectionTab == 1) {
                items(freeBooks, key = { it.id }) { book ->
                    val savedPath = downloadedBooksMap[book.id]
                    val isCurrentlyDownloading = downloadingBookId == book.id

                    FreeSpokenEnglishPdfBookCard(
                        book = book,
                        savedPath = savedPath,
                        isDownloading = isCurrentlyDownloading,
                        onDirectPdfDownload = {
                            downloadingBookId = book.id
                            coroutineScope.launch(Dispatchers.IO) {
                                val file = CoursePdfGenerator.getOrCreateBookPdfFile(context, book)
                                val res = CoursePdfGenerator.savePdfToDownloads(
                                    context = context,
                                    pdfFile = file,
                                    customFileName = book.fileName,
                                    bookId = book.id
                                )
                                withContext(Dispatchers.Main) {
                                    downloadingBookId = null
                                    downloadedBooksMap = CoursePdfGenerator.getDownloadedBooksMap(context)
                                    res.onSuccess { info ->
                                        Toast.makeText(
                                            context,
                                            "✅ '${book.fileName}' டவுன்லோடு செய்யப்பட்டது!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        downloadedPdfResult = info
                                    }.onFailure {
                                        pendingFileForSaveAs = file
                                        savePdfAsLauncher.launch(book.fileName)
                                    }
                                }
                            }
                        },
                        onReadInApp = {
                            coroutineScope.launch(Dispatchers.IO) {
                                val pages = book.pagesBuilder()
                                val file = CoursePdfGenerator.getOrCreateBookPdfFile(context, book)
                                withContext(Dispatchers.Main) {
                                    activeReaderPages = pages
                                    activeReaderFile = file
                                    activeReaderBook = book
                                }
                            }
                        },
                        onSaveAsToFolder = {
                            coroutineScope.launch(Dispatchers.IO) {
                                val file = CoursePdfGenerator.getOrCreateBookPdfFile(context, book)
                                withContext(Dispatchers.Main) {
                                    pendingFileForSaveAs = file
                                    savePdfAsLauncher.launch(book.fileName)
                                }
                            }
                        },
                        onSharePdf = {
                            coroutineScope.launch(Dispatchers.IO) {
                                val file = CoursePdfGenerator.getOrCreateBookPdfFile(context, book)
                                withContext(Dispatchers.Main) {
                                    CoursePdfGenerator.shareOrOpenPdf(context, file, openDirectly = false)
                                }
                            }
                        },
                        onClickWebDownloadLink = {
                            // Also save local copy to Downloads so user always receives the PDF even if offline!
                            coroutineScope.launch(Dispatchers.IO) {
                                val file = CoursePdfGenerator.getOrCreateBookPdfFile(context, book)
                                val res = CoursePdfGenerator.savePdfToDownloads(
                                    context = context,
                                    pdfFile = file,
                                    customFileName = book.fileName,
                                    bookId = book.id
                                )
                                withContext(Dispatchers.Main) {
                                    downloadedBooksMap = CoursePdfGenerator.getDownloadedBooksMap(context)
                                    CoursePdfGenerator.openWebLinkOrEnqueueDownload(
                                        context = context,
                                        url = book.freeDownloadUrl,
                                        bookTitle = book.titleEnglish,
                                        fileName = book.fileName,
                                        openInBrowser = false
                                    )
                                    res.onSuccess { info ->
                                        downloadedPdfResult = info
                                    }
                                }
                            }
                        },
                        onOpenWebSourceInBrowser = {
                            CoursePdfGenerator.openWebLinkOrEnqueueDownload(
                                context = context,
                                url = book.freeDownloadUrl,
                                bookTitle = book.titleEnglish,
                                fileName = book.fileName,
                                openInBrowser = true
                            )
                        },
                        onCopyLink = {
                            CoursePdfGenerator.copyLinkToClipboard(
                                context = context,
                                label = book.titleEnglish,
                                url = book.freeDownloadUrl
                            )
                        }
                    )
                }
            }

            // SECTION 3: Saved Phrases & Notebook (சேமித்த வாக்கியங்கள்)
            if (selectedSectionTab == 0 || selectedSectionTab == 2) {
                item(key = "saved_phrases_section_header") {
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "🔖 சேமித்த வாக்கியங்கள் (Saved Phrases & Notebook)",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = "நீங்கள் சேமித்த வாக்கியங்களை மீண்டும் கேட்கலாம் அல்லது PDF நோட்புக்காக Download செய்யலாம்",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    coroutineScope.launch(Dispatchers.IO) {
                                        val notebookPdf = CoursePdfGenerator.generateSavedPhrasesNotebookPdf(context, savedList)
                                        val res = CoursePdfGenerator.savePdfToDownloads(
                                            context = context,
                                            pdfFile = notebookPdf,
                                            customFileName = "Subi_Saved_Spoken_English_Notebook.pdf",
                                            bookId = "saved_notebook_pdf"
                                        )
                                        withContext(Dispatchers.Main) {
                                            res.onSuccess { info ->
                                                Toast.makeText(
                                                    context,
                                                    "✅ சேமித்த வாக்கியங்கள் PDF டவுன்லோடு ஆனது!",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                downloadedPdfResult = info
                                            }.onFailure {
                                                pendingFileForSaveAs = notebookPdf
                                                savePdfAsLauncher.launch("Subi_Saved_Spoken_English_Notebook.pdf")
                                            }
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(17.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "📥 சேமித்த வாக்கியங்களை PDF புத்தகமாக Download செய்",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                if (savedList.isEmpty()) {
                    item(key = "empty_saved_phrases_card") {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BookmarkBorder,
                                    contentDescription = "No saved phrases",
                                    tint = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "இன்னும் தனிப்பட்ட வாக்கியங்கள் சேமிக்கப்படவில்லை",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "பாடங்கள் அல்லது உரையாடல்களில் உள்ள Bookmark ஐகானை அழுத்தி வாக்கியங்களை இங்கே சேமிக்கலாம்.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.outline,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(savedList, key = { "saved_phrase_${it.id}" }) { item ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
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
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    ) {
                                        Text(
                                            text = item.category,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    Row {
                                        TtsSpeakerButton(
                                            textToSpeak = item.englishText,
                                            onSpeak = { viewModel.voiceManager.speak(it) }
                                        )
                                        IconButton(
                                            onClick = { viewModel.deleteSavedPhrase(item.id) },
                                            modifier = Modifier.size(42.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = item.englishText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                val tamilPronunciation = TutorEngine.formatPronunciationInTamil(
                                    item.englishText,
                                    item.tanglishText
                                )
                                if (tamilPronunciation.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "🗣️ உச்சரிப்பு: $tamilPronunciation",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                if (item.tamilText.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "🇮🇳 " + item.tamilText,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(56.dp)) }
        }

        // Floating Up / Down Scroll Helper Buttons on SavedScreen too
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SmallFloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        lazyListState.animateScrollBy(-650f)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
            ) {
                Icon(Icons.Default.ArrowUpward, contentDescription = "Scroll Up")
            }
            SmallFloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        lazyListState.animateScrollBy(650f)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
            ) {
                Icon(Icons.Default.ArrowDownward, contentDescription = "Scroll Down")
            }
        }
    }
}

@Composable
private fun FreeSpokenEnglishPdfBookCard(
    book: FreePdfBookItem,
    savedPath: String?,
    isDownloading: Boolean,
    onDirectPdfDownload: () -> Unit,
    onReadInApp: () -> Unit,
    onSaveAsToFolder: () -> Unit,
    onSharePdf: () -> Unit,
    onClickWebDownloadLink: () -> Unit,
    onOpenWebSourceInBrowser: () -> Unit,
    onCopyLink: () -> Unit
) {
    val accentColor = try {
        Color(android.graphics.Color.parseColor(book.accentColorHex))
    } catch (_: Exception) {
        Color(0xFF0F766E)
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
            .testTag("free_pdf_book_card_${book.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Top Badge Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = accentColor
                ) {
                    Text(
                        text = "📕 ${book.badgeText}",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                if (savedPath != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFDCFCE7)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Downloaded",
                                tint = Color(0xFF15803D),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Downloaded ✅",
                                color = Color(0xFF15803D),
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFEF3C7)
                    ) {
                        Text(
                            text = "FREE PDF",
                            color = Color(0xFF92400E),
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Book Titles
            Text(
                text = book.titleTamil,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = book.titleEnglish,
                fontWeight = FontWeight.Bold,
                fontSize = 12.5.sp,
                color = accentColor
            )
            Text(
                text = book.authorAndEditionTamil,
                fontSize = 11.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = book.descriptionTamil,
                fontSize = 12.5.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Highlights Box
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    book.highlightsTamil.forEach { pt ->
                        Text(
                            text = "✅ $pt",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Clickable Free Download Link Box
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF0FDF4),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF86EFAC), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = Icons.Default.Link,
                                contentDescription = "Free Download Link",
                                tint = Color(0xFF15803D),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Free PDF Download Link (${book.sourceNameTamil}):",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534)
                            )
                        }
                        IconButton(
                            onClick = onCopyLink,
                            modifier = Modifier.size(26.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Link",
                                tint = Color(0xFF15803D),
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }

                    Text(
                        text = book.freeDownloadUrl,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1D4ED8),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClickWebDownloadLink() }
                            .padding(vertical = 3.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF15803D),
                            modifier = Modifier
                                .clickable { onClickWebDownloadLink() }
                                .testTag("web_direct_download_${book.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Direct Link Download",
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "லின்க் வழியே Download",
                                    color = Color.White,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White,
                            modifier = Modifier
                                .border(1.dp, Color(0xFF15803D), RoundedCornerShape(8.dp))
                                .clickable { onOpenWebSourceInBrowser() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                    contentDescription = "Open in Browser",
                                    tint = Color(0xFF15803D),
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Browser-ல் திற",
                                    color = Color(0xFF15803D),
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White,
                            modifier = Modifier
                                .border(1.dp, Color(0xFF15803D), RoundedCornerShape(8.dp))
                                .clickable { onCopyLink() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy Link",
                                    tint = Color(0xFF15803D),
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Copy Link",
                                    color = Color(0xFF15803D),
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Primary Action Buttons Row: Instant Offline PDF Download + Read PDF in App
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onDirectPdfDownload,
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1.1f)
                        .testTag("instant_pdf_download_${book.id}")
                ) {
                    Icon(Icons.Default.Download, contentDescription = "Download PDF", modifier = Modifier.size(17.dp))
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isDownloading) "சேமிக்கிறது..." else "📥 PDF டவுன்லோடு",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        softWrap = false
                    )
                }

                OutlinedButton(
                    onClick = onReadInApp,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("read_pdf_in_app_${book.id}")
                ) {
                    Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Read PDF", modifier = Modifier.size(17.dp))
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "📖 PDF படி",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Secondary Action Row: Save As to Phone Folder + Share PDF
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onSaveAsToFolder,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 6.dp, horizontal = 8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.SaveAlt, contentDescription = "Save As", modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("போனில் சேமி (Save As)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onSharePdf,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 6.dp, horizontal = 8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("WhatsApp / பகிர்", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun SavedBookInAppPdfReader(
    viewModel: MainViewModel,
    bookItem: FreePdfBookItem,
    pdfFile: File,
    pages: List<PdfCoursePage>,
    onBack: () -> Unit,
    onDownloadBook: (FreePdfBookItem) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val expandedSentencesByPage = remember { mutableStateMapOf<Int, Boolean>() }
    var practicingSentence by remember { mutableStateOf<String?>(null) }
    val isListening by viewModel.voiceManager.isListening.collectAsState()
    val partialSpeechText by viewModel.voiceManager.partialSpeechText.collectAsState()

    var totalPageCount by remember { mutableIntStateOf(pages.size.coerceAtLeast(1)) }
    LaunchedEffect(pdfFile) {
        withContext(Dispatchers.IO) {
            val count = CoursePdfGenerator.getPdfPageCount(pdfFile).coerceAtLeast(pages.size)
            withContext(Dispatchers.Main) {
                totalPageCount = count
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Reader Bar
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("பின்செல்", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = bookItem.titleTamil,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.5.sp,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "மொத்தம் $totalPageCount பக்கங்கள் • மேலே-கீழே தள்ளிப் படிக்கலாம்",
                            fontSize = 10.5.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Button(
                        onClick = { onDownloadBook(bookItem) },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "Download", modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Download", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Smooth Continuous Vertical PDF Pages List
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = totalPageCount,
                    key = { idx -> "saved_reader_page_${bookItem.id}_$idx" }
                ) { pageIdx ->
                    val coursePage = pages.getOrNull(pageIdx)
                    val expanded = expandedSentencesByPage[pageIdx] ?: (pageIdx == 0)

                    LazyPdfPageItemCard(
                        pdfFile = pdfFile,
                        pageIndex = pageIdx,
                        totalPageCount = totalPageCount,
                        coursePage = coursePage,
                        zoomScale = 1f,
                        isSentencesExpanded = expanded,
                        onToggleSentences = {
                            expandedSentencesByPage[pageIdx] = !expanded
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
                            val engSpeech = pg.sentences.firstOrNull()?.english ?: pg.titleEnglish
                            val tamSpeech = pg.sentences.firstOrNull()?.tamilMeaning ?: pg.titleTamil
                            viewModel.voiceManager.speakBilingual(engSpeech, tamSpeech)
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

                item { Spacer(modifier = Modifier.height(56.dp)) }
            }
        }

        // Up / Down Scroll FABs
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
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(Icons.Default.ArrowUpward, contentDescription = "Scroll Up")
            }
            SmallFloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        lazyListState.animateScrollBy(680f)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.ArrowDownward, contentDescription = "Scroll Down")
            }
        }
    }
}
