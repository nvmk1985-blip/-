package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.lessons.FreeSpokenEnglishPdfBooksData
import com.example.data.math.MentalMathPdfGenerator
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.viewmodel.MainViewModel

data class PdfBookEntry(
    val id: String,
    val titleTa: String,
    val titleEn: String,
    val categoryTa: String,
    val badge: String,
    val descriptionTa: String,
    val features: List<String>,
    val onOpen: (android.content.Context) -> Unit,
    val onShare: (android.content.Context) -> Unit
)

@Composable
fun PdfBookshelfScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onNavigateToMentalMath: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("அனைத்தும்") }

    val categories = listOf("அனைத்தும்", "மனக் கணக்கு", "வாய்ப்பாடுகள்", "Spoken English")

    val books = remember {
        listOf(
            PdfBookEntry(
                id = "book_mental_math_100",
                titleTa = "100 மனக் கணக்கு ரகசியங்கள் கையேடு",
                titleEn = "100 Mental Maths Secrets & Shortcuts Guide",
                categoryTa = "மனக் கணக்கு",
                badge = "100 ரகசியங்கள் • 1-7 ஆம் வகுப்பு",
                descriptionTa = "கூட்டல், கழித்தல், பெருக்கல், வகுத்தல், பின்னங்கள் & கணித மேஜிக் என மொத்தம் 100 வேக குறுக்குவழிகள் கொண்ட முழுமையான தமிழ் கையேடு.",
                features = listOf(
                    "7-ஆம் வகுப்புக்கு உட்பட்ட மாணவர்களுக்கு ஏற்ற எளிய தமிழ் விளக்கம்",
                    "ஒவ்வொரு ரகசியத்திற்கும் சூத்திரம், 3 படிகள் மற்றும் நிஜ எடுத்துக்காட்டுகள்",
                    "சுய பரிசோதனைக்கான பயிற்சி வினாக்கள்"
                ),
                onOpen = { ctx -> MentalMathPdfGenerator.openPdf(ctx) },
                onShare = { ctx -> MentalMathPdfGenerator.sharePdf(ctx) }
            ),
            PdfBookEntry(
                id = "book_tables_1_to_20",
                titleTa = "1 முதல் 20 பெருக்கல் வாய்ப்பாடுகள் புத்தகம்",
                titleEn = "Multiplication Tables 1 to 20 Complete Book",
                categoryTa = "வாய்ப்பாடுகள்",
                badge = "1-20 வாய்ப்பாடு • A4 வண்ணப் புத்தகம்",
                descriptionTa = "1 முதல் 20 வரையிலான அனைத்து பெருக்கல் வாய்ப்பாடுகளும் எளிதில் மனனம் செய்ய உதவும் வண்ணமயமான அட்டவணைப் புத்தகம்.",
                features = listOf(
                    "1 முதல் 10 வாய்ப்பாடுகள் & 11 முதல் 20 வாய்ப்பாடுகள் தனித்தனி பக்கங்கள்",
                    "சுபி மற்றும் தனம் டீச்சரின் தமிழ் உச்சரிப்பு வழிகாட்டி",
                    "பிரிண்ட் எடுத்து சுவரில் ஒட்டக்கூடிய A4 வடிவமைப்பு"
                ),
                onOpen = { ctx -> MentalMathPdfGenerator.openPdf(ctx) },
                onShare = { ctx -> MentalMathPdfGenerator.sharePdf(ctx) }
            ),
            PdfBookEntry(
                id = "book_spoken_english_30days",
                titleTa = "தமிழ் வழியே ஸ்போக்கன் இங்கிலீஷ் — 30 நாட்கள் புத்தகம்",
                titleEn = "Spoken English via Tamil — Complete 30-Day Course Book",
                categoryTa = "Spoken English",
                badge = "18 பக்கங்கள் • 30 Days Full Course",
                descriptionTa = "அடிப்படை முதல் சரளமாக ஆங்கிலம் பேசும் வரை 10 யூனிட்கள், அட்டவணைகள், தமிழ் உச்சரிப்புடன் கூடிய முழுப் பாடப்புத்தகம்.",
                features = listOf(
                    "10 முழுமையான பாடப் பிரிவுகள் (Pronouns, 12 Tenses, WH Questions)",
                    "தமிழ் அர்த்தம் மற்றும் தமிழ் எழுத்துக்களில் ஆங்கில உச்சரிப்பு",
                    "பள்ளி, வீடு, கடை உரையாடல் எடுத்துக்காட்டுகள்"
                ),
                onOpen = { ctx ->
                    val book = FreeSpokenEnglishPdfBooksData.books.firstOrNull()
                    if (book != null) {
                        try {
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                data = android.net.Uri.parse(book.freeDownloadUrl)
                                addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            ctx.startActivity(intent)
                        } catch (_: Exception) {
                            MentalMathPdfGenerator.openPdf(ctx)
                        }
                    } else {
                        MentalMathPdfGenerator.openPdf(ctx)
                    }
                },
                onShare = { ctx ->
                    MentalMathPdfGenerator.sharePdf(ctx)
                }
            ),
            PdfBookEntry(
                id = "book_adis_62_lessons",
                titleTa = "ஆதியின் தமிழ் வழியே ஆங்கிலம் — 62 பாடங்கள் & கதைகள்",
                titleEn = "Adi's Spoken English & Tamil Handbook (62 Lessons)",
                categoryTa = "Spoken English",
                badge = "14 பக்கங்கள் • 62 Lessons & Stories",
                descriptionTa = "62 படிப்படியான பாடங்கள், 12 அன்றாட உரையாடல்கள், 6 நீதிக்கதைகள் கொண்ட புகழ்பெற்ற இருமொழிப் பாடப்புத்தகம்.",
                features = listOf(
                    "பாடம் 1 முதல் 62 வரை எளிய இலக்கண பார்முலாக்கள்",
                    "12 நிஜ வாழ்க்கை உரையாடல்கள் (ஓட்டல், வங்கி, மருத்துவர்)",
                    "6 சுவாரசியமான நீதிக்கதைகள்"
                ),
                onOpen = { ctx ->
                    try {
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                            data = android.net.Uri.parse("https://www.tamilvu.org/library/pdf/Adis_Spoken_English_Via_Tamil_62_Lessons.pdf")
                            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        ctx.startActivity(intent)
                    } catch (_: Exception) {
                        MentalMathPdfGenerator.openPdf(ctx)
                    }
                },
                onShare = { ctx ->
                    MentalMathPdfGenerator.sharePdf(ctx)
                }
            )
        )
    }

    val filteredBooks = remember(selectedCategory) {
        if (selectedCategory == "அனைத்தும்") {
            books
        } else {
            books.filter { it.categoryTa == selectedCategory }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        // Top App Bar
        Surface(
            color = Color(0xFF1E293B),
            shadowElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("pdf_bookshelf_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Home",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "📚 PDF புத்தக அடுக்கு (Bookshelf)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "அனைத்து தமிழ் PDF புத்தகங்கள் & கையேடுகளின் தொகுப்பு",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
                IconButton(
                    onClick = {
                        viewModel.voiceManager.speakTamil(
                            "வணக்கம்! இது உங்கள் PDF புத்தக அடுக்கு. 100 மனக் கணக்கு ரகசியங்கள், 1 முதல் 20 வாய்ப்பாடுகள் மற்றும் ஆங்கிலப் புத்தகங்கள் அனைத்தும் இங்கே உள்ளன!"
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Listen Introduction",
                        tint = Color(0xFF38BDF8)
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Info Banner
            item {
                Spacer(modifier = Modifier.height(10.dp))
                ElevatedCard(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1E1B4B)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF4338CA),
                                modifier = Modifier.size(46.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Book,
                                        contentDescription = null,
                                        tint = Color(0xFFFDE68A),
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "📖 இலவச மின்-நூலகம் (E-Library)",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "ஒரே தட்டலில் வாசிக்கலாம் • பிரிண்ட் எடுக்கலாம்",
                                    fontSize = 12.sp,
                                    color = Color(0xFFC7D2FE)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "மாணவர்கள் எளிதாகப் படிக்கும் வகையில் அனைத்து PDF புத்தகங்களும் இந்த ஒரே அடுக்கில் ஒழுங்கமைக்கப்பட்டுள்ளன.",
                            fontSize = 12.5.sp,
                            color = Color(0xFFE0E7FF).copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // Category Filter Chips
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = category },
                            label = {
                                Text(
                                    text = category,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF4338CA),
                                selectedLabelColor = Color.White,
                                containerColor = Color(0xFF1E293B),
                                labelColor = Color(0xFF94A3B8)
                            )
                        )
                    }
                }
            }

            // Books Count Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "கிடைக்கும் புத்தகங்கள்:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF334155)
                    ) {
                        Text(
                            text = "${filteredBooks.size} புத்தகங்கள்",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF38BDF8),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            // Books Cards
            items(filteredBooks) { book ->
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1E293B)
                    ),
                    border = BorderStroke(1.dp, Color(0xFF334155)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("pdf_book_card_${book.id}")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF4338CA).copy(alpha = 0.35f),
                                    modifier = Modifier.padding(bottom = 6.dp)
                                ) {
                                    Text(
                                        text = book.badge,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF818CF8),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Text(
                                    text = book.titleTa,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = book.titleEn,
                                    fontSize = 12.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF0F172A),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.PictureAsPdf,
                                        contentDescription = null,
                                        tint = Color(0xFFEF4444),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = book.descriptionTa,
                            fontSize = 12.5.sp,
                            color = Color(0xFFCBD5E1),
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        book.features.forEach { feature ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "✓", color = EmeraldSuccess, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = feature, color = Color(0xFF94A3B8), fontSize = 11.5.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { book.onOpen(context) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("open_book_button_${book.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PictureAsPdf,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("PDF திறக்க", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            OutlinedButton(
                                onClick = { book.onShare(context) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("share_book_button_${book.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("PDF பகிர்க", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
