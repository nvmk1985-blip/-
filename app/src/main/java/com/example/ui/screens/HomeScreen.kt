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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PictureAsPdf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.VoiceToneDialog
import com.example.ui.viewmodel.MainViewModel

/**
 * Compact, single-screen Home Screen (fits mobile screen without scrolling):
 * 1) குழந்தைகளும் ரோபோவும் உள்ள தலைப்பு படம் (Kids & Robot Hero Banner)
 * 2) பாடப்புத்தக ஸ்கேனர் (Textbook Camera Scanner)
 * 3) மனக் கணக்கு (Mental Math 100 Secrets & Tables)
 * 4) Spoken English via தமிழ் (Daily lessons)
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
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // ================= ITEM 1: குழந்தைகள் & ரோபோ கற்றல் தலைப்பு (Gemini AI நேரடி உரையாடல்) =================
        Card(
            onClick = onNavigateToBot,
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            border = BorderStroke(1.2.dp, Color(0xFF38BDF8).copy(alpha = 0.45f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("item_pesalam_ai_logo")
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                // Kids & Robot Hero Image (replaces the lady image)
                Image(
                    painter = painterResource(id = R.drawable.img_kids_robo_learn),
                    contentDescription = "குழந்தைகள் மற்றும் ரோபோ கற்றல்",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark gradient overlay for text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF0F172A).copy(alpha = 0.35f),
                                    Color(0xFF0F172A).copy(alpha = 0.88f)
                                )
                            )
                        )
                )

                // Header content on top of image
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Surface(
                            color = Color(0xFF4338CA).copy(alpha = 0.9f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "🤖 Gemini AI உரையாடல் • தட்டவும்",
                                color = Color(0xFFFDE68A),
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "தமிழ் வழி சுலப ஆங்கிலம் & கணிதம்",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }

                    // Compact Voice Tone Selector Chip
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF0F172A).copy(alpha = 0.92f),
                        border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.6f)),
                        modifier = Modifier
                            .clickable { showVoiceToneDialog = true }
                            .padding(bottom = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = selectedVoiceTone.emoji, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = selectedVoiceTone.titleTa,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "▼", fontSize = 8.sp, color = Color(0xFF94A3B8))
                        }
                    }
                }
            }
        }

        // ================= ITEM 2: பாடப்புத்தக ஸ்கேனர் =================
        HomeNavPill(
            title = "பாடப்புத்தக ஸ்கேனர்",
            subtitle = "புத்தக பக்கங்களை படம் பிடித்து நேரடி சந்தேகம் தீர்க்க",
            tag = "CAMERA AI",
            icon = Icons.Default.CameraAlt,
            accentColor = Color(0xFFEC4899),
            containerColor = Color(0xFF1E293B),
            testTag = "item_textbook_scanner",
            modifier = Modifier.weight(1f, fill = false),
            onClick = {
                viewModel.requestOpenCameraSheet()
                onNavigateToBot()
            }
        )

        // ================= ITEM 3: மனக் கணக்கு (Mental Math) =================
        HomeNavPill(
            title = "மனக் கணக்கு (Mental Math)",
            subtitle = "1-7 ஆம் வகுப்பு 100 ரகசியங்கள், 1-20 வாய்ப்பாடு & பயிற்சி",
            tag = "100 SECRETS",
            icon = Icons.Default.Calculate,
            accentColor = Color(0xFFF59E0B),
            containerColor = Color(0xFF1E293B),
            testTag = "item_mental_math",
            modifier = Modifier.weight(1f, fill = false),
            onClick = onNavigateToMentalMath
        )

        // ================= ITEM 4: Spoken English via தமிழ் =================
        HomeNavPill(
            title = "Spoken English via தமிழ்",
            subtitle = "அன்றாட தமிழ் வழி ஆங்கில உரையாடல், பாடங்கள் & உச்சரிப்பு",
            tag = "DAILY LESSONS",
            icon = Icons.AutoMirrored.Filled.MenuBook,
            accentColor = Color(0xFF10B981),
            containerColor = Color(0xFF1E293B),
            testTag = "item_spoken_english",
            modifier = Modifier.weight(1f, fill = false),
            onClick = onNavigateToLessons
        )

        // ================= ITEM 5: PDF புத்தக அடுக்கு =================
        HomeNavPill(
            title = "PDF புத்தக அடுக்கு",
            subtitle = "100 மனக்கணக்கு ரகசியங்கள், வாய்ப்பாடுகள் & E-Books",
            tag = "PDF E-BOOKS",
            icon = Icons.Default.PictureAsPdf,
            accentColor = Color(0xFF8B5CF6),
            containerColor = Color(0xFF1E293B),
            testTag = "item_pdf_bookshelf",
            modifier = Modifier.weight(1f, fill = false),
            onClick = onNavigateToPdfBookshelf
        )

        // ================= ITEM 6: குரல் அலைவரிசை (Voice wave) =================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .scale(pulseScale)
        ) {
            HomeNavPill(
                title = "குரல் அலைவரிசை (Voice wave)",
                subtitle = "நேரடி அனிமேஷன் குரல் அலைவரிசை உரையாடல்",
                tag = "LIVE CALL",
                icon = Icons.Default.GraphicEq,
                accentColor = Color(0xFF06B6D4),
                containerColor = Color(0xFF1E1B4B),
                testTag = "item_voice_wave",
                onClick = onNavigateToCall
            )
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
 * Compact pill button matching single screen layout with no scrolling needed.
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
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.2.dp, accentColor.copy(alpha = 0.45f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = accentColor.copy(alpha = 0.18f),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(5.dp),
                        color = accentColor.copy(alpha = 0.25f)
                    ) {
                        Text(
                            text = tag,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 10.5.sp,
                    color = Color(0xFF94A3B8),
                    maxLines = 1
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open $title",
                tint = accentColor.copy(alpha = 0.8f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
