package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.VoiceToneChipsRow
import com.example.ui.components.VoiceToneDialog
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.viewmodel.MainViewModel

/**
 * Clean, centered launcher Home Screen strictly adhering to the user's layout specification:
 * 1) பேசலாம் AI சின்னம் (Pesalam AI Logo / Avatar Header)
 * 2) பாடப்புத்தக ஸ்கேனர் (Textbook Camera Scanner)
 * 3) மனக் கணக்கு (Mental Math)
 * 4) Spoken English via தமிழ்
 * 5) PDF புத்தக அடுக்கு (PDF Bookshelf)
 * 6) குரல் அலைவரிசை (Voice wave Live Call)
 */
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToBot: () -> Unit,
    onNavigateToCall: () -> Unit,
    onNavigateToLessons: () -> Unit,
    onNavigateToHowToSay: () -> Unit = {},
    onNavigateToPronounce: () -> Unit = {},
    onNavigateToMentalMath: () -> Unit = {},
    onNavigateToPdfBookshelf: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showVoiceToneDialog by remember { mutableStateOf(false) }
    val selectedVoiceTone by viewModel.voiceManager.selectedVoiceTone.collectAsState()

    // Breathing pulse for voice wave item
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ================= ITEM 1: பேசலாம் AI சின்னம் =================
        item {
            Card(
                onClick = {
                    viewModel.voiceManager.speakTamil(
                        "வணக்கம்! நான் உங்கள் தனம் டீச்சர். பேசலாம் ஏஐ செயலிக்கு அன்புடன் வரவேற்கிறேன். கீழே உள்ள பட்டியலில் நீங்கள் விரும்பியதைத் தேர்ந்தெடுக்கலாம்!"
                    )
                },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E293B)
                ),
                border = BorderStroke(1.5.dp, Color(0xFF38BDF8).copy(alpha = 0.4f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("item_pesalam_ai_logo")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo Avatar
                    Box(
                        modifier = Modifier
                            .size(86.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0xFF38BDF8).copy(alpha = 0.35f), Color(0xFF1E293B))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_pesalam_avatar),
                            contentDescription = "பேசலாம் AI சின்னம்",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(78.dp)
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = Color(0xFF4338CA),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "பேசலாம் AI சின்னம்",
                            color = Color(0xFFFDE68A),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "பேசலாம் AI • Pesalam AI",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Text(
                        text = "சுபி (9 வயது) & தனம் டீச்சர் • தமிழ் வழி கல்வி வழிகாட்டி",
                        fontSize = 12.5.sp,
                        color = Color(0xFF94A3B8),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Voice Tone Selector compact trigger
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF0F172A),
                        border = BorderStroke(1.dp, Color(0xFF334155)),
                        modifier = Modifier
                            .clickable { showVoiceToneDialog = true }
                            .padding(horizontal = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = selectedVoiceTone.emoji, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "குரல்: ${selectedVoiceTone.titleTa} (${selectedVoiceTone.titleEn})",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF38BDF8)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "▼", fontSize = 9.sp, color = Color(0xFF94A3B8))
                        }
                    }
                }
            }
        }

        // ================= ITEM 2: பாடப்புத்தக ஸ்கேனர் =================
        item {
            HomeNavPill(
                title = "பாடப்புத்தக ஸ்கேனர்",
                subtitle = "புத்தக பக்கங்களை படம் பிடித்து நேரடி சந்தேகம் தீர்க்க",
                tag = "CAMERA AI",
                icon = Icons.Default.CameraAlt,
                accentColor = Color(0xFFEC4899),
                containerColor = Color(0xFF1E293B),
                testTag = "item_textbook_scanner",
                onClick = {
                    viewModel.requestOpenCameraSheet()
                    onNavigateToBot()
                }
            )
        }

        // ================= ITEM 3: மனக் கணக்கு (Mental Math) =================
        item {
            HomeNavPill(
                title = "மனக் கணக்கு (Mental Math)",
                subtitle = "1-7 ஆம் வகுப்பு மாணவர்களுக்கான 100 ரகசியங்கள், வாய்ப்பாடுகள் & பயிற்சி",
                tag = "100 SECRETS",
                icon = Icons.Default.Calculate,
                accentColor = Color(0xFFF59E0B),
                containerColor = Color(0xFF1E293B),
                testTag = "item_mental_math",
                onClick = onNavigateToMentalMath
            )
        }

        // ================= ITEM 4: Spoken English via தமிழ் =================
        item {
            HomeNavPill(
                title = "Spoken English via தமிழ்",
                subtitle = "அன்றாட தமிழ் வழி ஆங்கில உரையாடல், பாடங்கள் & உச்சரிப்பு",
                tag = "DAILY LESSONS",
                icon = Icons.AutoMirrored.Filled.MenuBook,
                accentColor = Color(0xFF10B981),
                containerColor = Color(0xFF1E293B),
                testTag = "item_spoken_english",
                onClick = onNavigateToLessons
            )
        }

        // ================= ITEM 5: PDF புத்தக அடுக்கு =================
        item {
            HomeNavPill(
                title = "PDF புத்தக அடுக்கு",
                subtitle = "100 மனக்கணக்கு ரகசியங்கள், 1-20 வாய்ப்பாடுகள் & அனைத்து PDF புத்தகங்கள்",
                tag = "PDF E-BOOKS",
                icon = Icons.Default.PictureAsPdf,
                accentColor = Color(0xFF8B5CF6),
                containerColor = Color(0xFF1E293B),
                testTag = "item_pdf_bookshelf",
                onClick = onNavigateToPdfBookshelf
            )
        }

        // ================= ITEM 6: குரல் அலைவரிசை (Voice wave) =================
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(pulseScale)
            ) {
                HomeNavPill(
                    title = "குரல் அலைவரிசை (Voice wave)",
                    subtitle = "தனம் டீச்சர் & சுபியுடன் நேரடி அனிமேஷன் குரல் அலைவரிசை உரையாடல்",
                    tag = "LIVE CALL",
                    icon = Icons.Default.GraphicEq,
                    accentColor = Color(0xFF06B6D4),
                    containerColor = Color(0xFF1E1B4B),
                    testTag = "item_voice_wave",
                    onClick = onNavigateToCall
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Voice Tone Full Selection Dialog
    if (showVoiceToneDialog) {
        VoiceToneDialog(
            voiceManager = viewModel.voiceManager,
            onDismiss = { showVoiceToneDialog = false }
        )
    }
}

/**
 * Clean, centered pill button matching the user's sketch layout.
 */
@Composable
private fun HomeNavPill(
    title: String,
    subtitle: String,
    tag: String,
    icon: ImageVector,
    accentColor: Color,
    containerColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.2.dp, accentColor.copy(alpha = 0.45f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = accentColor.copy(alpha = 0.18f),
                modifier = Modifier.size(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = accentColor.copy(alpha = 0.25f)
                    ) {
                        Text(
                            text = tag,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.5.sp,
                    color = Color(0xFF94A3B8),
                    lineHeight = 16.sp
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open $title",
                tint = accentColor.copy(alpha = 0.8f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
