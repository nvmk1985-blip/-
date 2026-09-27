package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Badge
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldSuccess
import kotlin.random.Random

import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Translate
import com.example.voice.CurrentlySpeakingLanguage
import com.example.voice.TtsMode

@Composable
fun AudioWaveVisualizer(
    isListening: Boolean,
    isSpeaking: Boolean,
    rms: Float = 0f,
    modifier: Modifier = Modifier,
    barCount: Int = 18,
    activeColor: Color = MaterialTheme.colorScheme.primary
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave_transition")
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave_progress"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isActive = isListening || isSpeaking
        for (i in 0 until barCount) {
            val factor = if (isActive) {
                if (isListening && rms > 0.5f) {
                    (0.3f + (rms / 8f) * (0.5f + (i % 5) * 0.1f)).coerceIn(0.2f, 1f)
                } else {
                    val waveOffset = ((i * 0.1f + animatedProgress) % 1f)
                    (0.2f + 0.8f * waveOffset).coerceIn(0.2f, 1f)
                }
            } else {
                0.2f
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .width(4.dp)
                    .height((36 * factor).dp)
                    .clip(CircleShape)
                    .background(if (isActive) activeColor else MaterialTheme.colorScheme.outlineVariant)
            )
        }
    }
}

@Composable
fun TtsModeSelector(
    currentMode: TtsMode,
    onModeChange: (TtsMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = "Voice mode",
                modifier = Modifier.size(15.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TtsMode.values().forEach { mode ->
                val isSelected = mode == currentMode
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { onModeChange(mode) }
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = mode.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.testTag("tts_mode_${mode.name}")
                    )
                }
            }
        }
    }
}

@Composable
fun BilingualAudioButtons(
    englishText: String,
    tamilText: String,
    isSpeakingThis: Boolean,
    speakingLanguage: CurrentlySpeakingLanguage,
    onPlayBoth: () -> Unit,
    onPlayEnglish: () -> Unit,
    onPlayTamil: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (isSpeakingThis) {
            // Active speaking status badge + Stop button
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (speakingLanguage == CurrentlySpeakingLanguage.TAMIL) Color(0xFFFEF3C7) else Color(0xFFE0E7FF),
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (speakingLanguage == CurrentlySpeakingLanguage.TAMIL) Color(0xFFD97706) else Color(0xFF4F46E5))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (speakingLanguage == CurrentlySpeakingLanguage.TAMIL) "🔊 தமிழ் பேசுகிறது..." else "🔊 Speaking English...",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (speakingLanguage == CurrentlySpeakingLanguage.TAMIL) Color(0xFF92400E) else Color(0xFF312E81)
                    )
                }
            }

            IconButton(
                onClick = onStop,
                modifier = Modifier
                    .size(28.dp)
                    .testTag("tts_stop_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "Stop speech",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(18.dp)
                )
            }
        } else {
            // Button 1: Both English + Tamil
            if (englishText.isNotBlank() && tamilText.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    modifier = Modifier
                        .clickable { onPlayBoth() }
                        .testTag("play_both_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Play English and Tamil",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Both (Eng+தமிழ்)",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Button 2: English Only
            if (englishText.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .clickable { onPlayEnglish() }
                        .testTag("play_english_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "🇬🇧 Eng",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Button 3: Tamil Only
            if (tamilText.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFEF3C7),
                    modifier = Modifier
                        .clickable { onPlayTamil() }
                        .testTag("play_tamil_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "🇮🇳 தமிழ்",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TtsSpeakerButton(
    textToSpeak: String,
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier,
    isTamil: Boolean = false,
    contentDescription: String = "Listen audio"
) {
    FilledIconButton(
        onClick = { onSpeak(textToSpeak) },
        modifier = modifier
            .size(38.dp)
            .testTag("tts_button"),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = if (isTamil) Color(0xFFFEF3C7) else MaterialTheme.colorScheme.primaryContainer,
            contentColor = if (isTamil) Color(0xFF92400E) else MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
            contentDescription = contentDescription,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun SpeechRateChip(
    currentRate: Float,
    onRateChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Speed,
                contentDescription = "Audio speed",
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            val rates = listOf(0.75f to "0.75x (Slow)", 0.9f to "0.9x", 1.0f to "1.0x")
            rates.forEach { (rate, label) ->
                val isSelected = kotlin.math.abs(currentRate - rate) < 0.05f
                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { onRateChange(rate) }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.testTag("speed_chip_$label")
                    )
                }
            }
        }
    }
}

@Composable
fun ScoreBadge(score: Int, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when {
        score >= 85 -> EmeraldSuccess to Color.White
        score >= 60 -> Color(0xFFF59E0B) to Color.Black
        else -> MaterialTheme.colorScheme.error to Color.White
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Score: $score%",
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
