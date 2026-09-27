package com.example.ui.components

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.AppScreen
import com.example.voice.SttLanguage

fun hasAudioRecordingPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED
}

fun openAppPermissionSettings(context: Context) {
    try {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
    }
}

@Composable
fun PersistentPushToTalkBottomBar(
    currentScreen: AppScreen,
    isListening: Boolean,
    speechRms: Float,
    partialSpeechText: String,
    selectedSttLanguage: SttLanguage,
    onSelectScreen: (AppScreen) -> Unit,
    onToggleSttLanguage: (SttLanguage) -> Unit,
    onStartPushToTalk: () -> Unit,
    onStopPushToTalk: () -> Unit,
    onCancelPushToTalk: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("bottom_nav_bar")
    ) {
        // Active Recording Banner docked above the bottom bar
        AnimatedVisibility(
            visible = isListening,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            Surface(
                color = Color(0xFF1E1B4B),
                tonalElevation = 10.dp,
                shadowElevation = 8.dp,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ptt_active_recording_banner")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Pulsing REC indicator + Language Switcher
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RecordingPulseDot()
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (selectedSttLanguage == SttLanguage.TAMIL) {
                                    "REC • தமிழில் பேசுங்கள்"
                                } else {
                                    "REC • Speak English"
                                },
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Quick Language Toggle Pill inside Banner
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            SttLangMiniChip(
                                label = "🇬🇧 EN",
                                selected = selectedSttLanguage == SttLanguage.ENGLISH,
                                onClick = { onToggleSttLanguage(SttLanguage.ENGLISH) }
                            )
                            SttLangMiniChip(
                                label = "🇮🇳 த",
                                selected = selectedSttLanguage == SttLanguage.TAMIL,
                                onClick = { onToggleSttLanguage(SttLanguage.TAMIL) }
                            )
                            IconButton(
                                onClick = onCancelPushToTalk,
                                modifier = Modifier
                                    .size(28.dp)
                                    .testTag("ptt_cancel_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cancel Recording",
                                    tint = Color.White.copy(alpha = 0.8f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Real-time partial speech transcription
                    Text(
                        text = if (partialSpeechText.isNotBlank()) {
                            "\"$partialSpeechText\""
                        } else {
                            "Listening... Release or tap mic when done (பேசி முடித்ததும் விடுங்கள்)"
                        },
                        color = if (partialSpeechText.isNotBlank()) Color(0xFFFDE68A) else Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        fontWeight = if (partialSpeechText.isNotBlank()) FontWeight.Bold else FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.testTag("ptt_live_transcript")
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    AudioWaveVisualizer(
                        isListening = true,
                        isSpeaking = false,
                        rms = speechRms,
                        barCount = 20,
                        activeColor = Color(0xFFEF4444),
                        modifier = Modifier.height(28.dp)
                    )
                }
            }
        }

        // Bottom App Bar with Navigation Items + Central Persistent Push-to-Talk Button
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            shadowElevation = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .height(80.dp)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val leftNavItems = listOf(
                    AppScreen.HOME to Icons.Default.Home,
                    AppScreen.BOT to Icons.AutoMirrored.Filled.Chat
                )
                val rightNavItems = listOf(
                    AppScreen.LESSONS to Icons.AutoMirrored.Filled.MenuBook,
                    AppScreen.HOW_TO_SAY to Icons.Default.Translate,
                    AppScreen.SAVED to Icons.Default.Bookmark
                )

                leftNavItems.forEach { (screen, icon) ->
                    val isSelected = currentScreen == screen
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onSelectScreen(screen) },
                        icon = { Icon(imageVector = icon, contentDescription = screen.title) },
                        label = {
                            Text(
                                text = screen.title,
                                fontSize = 9.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.testTag("nav_item_${screen.name.lowercase()}")
                    )
                }

                // Center Persistent Push-to-Talk Button
                PersistentPushToTalkButton(
                    isListening = isListening,
                    speechRms = speechRms,
                    onStartPushToTalk = onStartPushToTalk,
                    onStopPushToTalk = onStopPushToTalk,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .testTag("persistent_ptt_button")
                )

                rightNavItems.forEach { (screen, icon) ->
                    val isSelected = currentScreen == screen
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onSelectScreen(screen) },
                        icon = { Icon(imageVector = icon, contentDescription = screen.title) },
                        label = {
                            Text(
                                text = screen.title,
                                fontSize = 9.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.testTag("nav_item_${screen.name.lowercase()}")
                    )
                }
            }
        }
    }
}

@Composable
fun PersistentPushToTalkButton(
    isListening: Boolean,
    speechRms: Float,
    onStartPushToTalk: () -> Unit,
    onStopPushToTalk: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "ptt_recording_pulse")
    val outerRingScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.45f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "outer_ring_scale"
    )
    val outerRingAlpha by infiniteTransition.animateFloat(
        initialValue = if (isListening) 0.55f else 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "outer_ring_alpha"
    )

    val innerRingScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.25f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "inner_ring_scale"
    )

    val rmsBoost = if (isListening) (speechRms / 10f).coerceIn(0f, 1f) * 0.14f else 0f
    val buttonScale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.92f
            isListening -> 1.08f + rmsBoost
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "ptt_button_scale"
    )

    val containerColor by animateColorAsState(
        targetValue = if (isListening) Color(0xFFEF4444) else MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 250),
        label = "ptt_color"
    )

    val currentIsListening by androidx.compose.runtime.rememberUpdatedState(isListening)
    val currentOnStart by androidx.compose.runtime.rememberUpdatedState(onStartPushToTalk)
    val currentOnStop by androidx.compose.runtime.rememberUpdatedState(onStopPushToTalk)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.minimumInteractiveComponentSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(56.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true
                            val pressStartTime = System.currentTimeMillis()
                            val wasAlreadyListening = currentIsListening

                            if (!wasAlreadyListening) {
                                currentOnStart()
                            }

                            val released = tryAwaitRelease()
                            isPressed = false
                            val holdDuration = System.currentTimeMillis() - pressStartTime

                            if (released) {
                                if (wasAlreadyListening) {
                                    // Tapping while already listening stops recording
                                    currentOnStop()
                                } else if (holdDuration >= 1200L) {
                                    // True long hold & release (1.2s+)
                                    currentOnStop()
                                }
                            }
                        }
                    )
                }
        ) {
            // Outer Expanding Ripple Ring during active recording
            if (isListening) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .scale(outerRingScale)
                        .clip(CircleShape)
                        .background(Color(0xFFEF4444).copy(alpha = outerRingAlpha))
                )
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .scale(innerRingScale)
                        .clip(CircleShape)
                        .background(Color(0xFFEF4444).copy(alpha = 0.28f))
                )
            }

            // Core Push-to-Talk Circle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .scale(buttonScale)
                    .clip(CircleShape)
                    .background(
                        brush = if (isListening) {
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFEF4444), Color(0xFFDC2626))
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(
                                    containerColor,
                                    MaterialTheme.colorScheme.tertiary
                                )
                            )
                        }
                    )
                    .border(
                        width = 2.dp,
                        color = if (isListening) Color(0xFFFCA5A5) else Color.White.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (isListening) Icons.Default.GraphicEq else Icons.Default.Mic,
                    contentDescription = if (isListening) {
                        "Active Recording - Release or tap to stop"
                    } else {
                        "Push to Talk with Dhanam Teacher"
                    },
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Text(
            text = if (isListening) "REC" else "Push-Talk",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = if (isListening) Color(0xFFEF4444) else MaterialTheme.colorScheme.primary,
            maxLines = 1
        )
    }
}

@Composable
private fun RecordingPulseDot() {
    val infiniteTransition = rememberInfiniteTransition(label = "rec_dot")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(550, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rec_dot_alpha"
    )
    Box(
        modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(Color(0xFFEF4444).copy(alpha = alpha))
    )
}

@Composable
private fun SttLangMiniChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (selected) Color(0xFF4F46E5) else Color.White.copy(alpha = 0.14f),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
        )
    }
}

@Composable
fun AudioPermissionRationaleDialog(
    onConfirmRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text = "மைக் அனுமதி தேவை (Microphone Permission)",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        },
        text = {
            Text(
                text = "மலருடன் ஆங்கிலம் மற்றும் தமிழில் பேசிப் பயிற்சி செய்ய மைக்ரோஃபோன் (RECORD_AUDIO) அனுமதி அவசியம்.\n\nTo use Push-to-Talk and practice spoken English or Tamil, please allow microphone recording access.",
                fontSize = 13.5.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirmRequestPermission,
                modifier = Modifier.testTag("permission_allow_button")
            ) {
                Text("Allow Mic (அனுமதி)")
            }
        },
        dismissButton = {
            Row {
                TextButton(
                    onClick = onOpenSettings,
                    modifier = Modifier.testTag("permission_settings_button")
                ) {
                    Text("Settings")
                }
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("permission_dismiss_button")
                ) {
                    Text("Later")
                }
            }
        }
    )
}
