package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AudioWaveVisualizer
import com.example.ui.components.BilingualAudioButtons
import com.example.ui.components.SpeechRateChip
import com.example.ui.components.TtsModeSelector
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.viewmodel.MainViewModel
import com.example.voice.CurrentlySpeakingLanguage
import com.example.voice.SttLanguage
import com.example.voice.TtsMode

@Composable
fun VoiceCallScreen(
    viewModel: MainViewModel,
    onEndCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isListening by viewModel.voiceManager.isListening.collectAsState()
    val isSpeaking by viewModel.voiceManager.isSpeaking.collectAsState()
    val currentlySpeakingLang by viewModel.voiceManager.currentlySpeakingLang.collectAsState()
    val ttsMode by viewModel.voiceManager.ttsMode.collectAsState()
    val speechRms by viewModel.voiceManager.speechRms.collectAsState()
    val speechRate by viewModel.voiceManager.speechRate.collectAsState()
    val selectedSttLanguage by viewModel.voiceManager.selectedSttLanguage.collectAsState()
    val partialSpeechText by viewModel.voiceManager.partialSpeechText.collectAsState()
    val isBotThinking by viewModel.isBotThinking.collectAsState()
    val callStatus by viewModel.callStatus.collectAsState()
    val chatHistory by viewModel.chatHistory.collectAsState()

    val lastBotMessage = chatHistory.lastOrNull { it.sender == "bot" }
    val lastUserMessage = chatHistory.lastOrNull { it.sender == "user" }

    LaunchedEffect(Unit) {
        viewModel.startCallMode()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isSpeaking || isListening) 1.15f else 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "avatar_pulse"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E1B4B),
                        Color(0xFF0F172A),
                        Color(0xFF090D16)
                    )
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top status
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.15f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(EmeraldSuccess)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Live English Voice Session",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            TtsModeSelector(
                currentMode = ttsMode,
                onModeChange = { viewModel.setTtsMode(it) }
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Malar (மலர்)",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Spoken English Voice Tutor",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
            SpeechRateChip(
                currentRate = speechRate,
                onRateChange = { viewModel.voiceManager.setSpeechRate(it) }
            )
        }

        // Center Pulsing Avatar & Waveform
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(190.dp)
            ) {
                // Outer glow halo
                Box(
                    modifier = Modifier
                        .size(175.dp)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(
                            if (isSpeaking) EmeraldSuccess.copy(alpha = 0.25f)
                            else if (isListening) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            else Color.White.copy(alpha = 0.08f)
                        )
                )

                // Avatar image
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .border(
                            3.dp,
                            if (isSpeaking) EmeraldSuccess else MaterialTheme.colorScheme.primary,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_app_foreground_1790321107787),
                        contentDescription = "Malar AI voice tutor",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Audio wave visualizer
            AudioWaveVisualizer(
                isListening = isListening,
                isSpeaking = isSpeaking,
                rms = speechRms,
                activeColor = if (isSpeaking) EmeraldSuccess else Color(0xFF6366F1),
                modifier = Modifier.height(36.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Call Status text with real-time speech feedback
            Text(
                text = if (isBotThinking) {
                    "மலர் யோசிக்கிறார்... (Thinking)"
                } else if (isSpeaking) {
                    if (currentlySpeakingLang == CurrentlySpeakingLanguage.TAMIL) {
                        "🔊 மலர் தமிழில் விளக்குகிறார் (Speaking Tamil)..."
                    } else {
                        "🔊 Malar is speaking in English..."
                    }
                } else if (isListening) {
                    if (partialSpeechText.isNotBlank()) {
                        "🎤 \"$partialSpeechText\""
                    } else {
                        "🎤 நீங்கள் பேசலாம் (${selectedSttLanguage.labelTamil} / ${selectedSttLanguage.label})..."
                    }
                } else {
                    callStatus
                },
                color = if (isListening && partialSpeechText.isNotBlank()) Color(0xFFFDE68A) else Color.White.copy(alpha = 0.9f),
                fontSize = if (isListening && partialSpeechText.isNotBlank()) 16.sp else 14.sp,
                fontWeight = if (isListening && partialSpeechText.isNotBlank()) FontWeight.Bold else FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            // Transcript bubble of last bot response
            if (lastBotMessage != null && lastBotMessage.englishText.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "\"${lastBotMessage.englishText}\"",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        if (lastBotMessage.tamilText.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = lastBotMessage.tamilText,
                                color = Color(0xFFFDE68A),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        BilingualAudioButtons(
                            englishText = lastBotMessage.englishText,
                            tamilText = lastBotMessage.tamilText,
                            isSpeakingThis = isSpeaking,
                            speakingLanguage = currentlySpeakingLang,
                            onPlayBoth = {
                                viewModel.voiceManager.speakBilingual(
                                    lastBotMessage.englishText,
                                    lastBotMessage.tamilText
                                )
                            },
                            onPlayEnglish = {
                                viewModel.voiceManager.speakEnglish(lastBotMessage.englishText)
                            },
                            onPlayTamil = {
                                viewModel.voiceManager.speakTamil(lastBotMessage.tamilText)
                            },
                            onStop = { viewModel.voiceManager.stopSpeaking() }
                        )
                    }
                }
            }
        }

        // Bottom Controls
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Quick suggested conversation prompts
            val suggestedPrompts = listOf(
                "Hello Malar!",
                "How to order tea in English?",
                "Can we practice job interview?",
                "சாப்பிட்டீங்களா?"
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(suggestedPrompts) { prompt ->
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        onClick = { viewModel.sendUserMessage(prompt) }
                    ) {
                        Text(
                            text = prompt,
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Language selector for Speech Recognition in Call
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = "Speaking: ",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                FilterChip(
                    selected = selectedSttLanguage == SttLanguage.ENGLISH,
                    onClick = { viewModel.setSttLanguage(SttLanguage.ENGLISH) },
                    label = { Text("🇬🇧 English", fontSize = 11.sp) },
                    modifier = Modifier.height(28.dp).testTag("call_stt_lang_en")
                )
                Spacer(modifier = Modifier.width(6.dp))
                FilterChip(
                    selected = selectedSttLanguage == SttLanguage.TAMIL,
                    onClick = { viewModel.setSttLanguage(SttLanguage.TAMIL) },
                    label = { Text("🇮🇳 தமிழ்", fontSize = 11.sp) },
                    modifier = Modifier.height(28.dp).testTag("call_stt_lang_ta")
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mic toggle
                FilledIconButton(
                    onClick = {
                        if (isListening) {
                            viewModel.voiceManager.stopListening()
                        } else {
                            viewModel.voiceManager.startListening(selectedSttLanguage.code)
                        }
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isListening) Color(0xFF22C55E) else Color.White.copy(alpha = 0.2f),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .size(56.dp)
                        .testTag("call_mic_toggle")
                ) {
                    Icon(
                        imageVector = if (isListening) Icons.Default.Mic else Icons.Default.MicOff,
                        contentDescription = "Toggle Mic",
                        modifier = Modifier.size(28.dp)
                    )
                }

                // End call red button
                FloatingActionButton(
                    onClick = {
                        viewModel.endCallMode()
                        onEndCall()
                    },
                    containerColor = Color(0xFFEF4444),
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(68.dp)
                        .testTag("end_call_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "End Call",
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Repeat response according to current TTS mode
                FilledIconButton(
                    onClick = {
                        lastBotMessage?.let {
                            viewModel.voiceManager.speakBotResponse(
                                englishText = it.englishText,
                                tamilText = it.tamilText,
                                mode = ttsMode
                            )
                        }
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isSpeaking) EmeraldSuccess else Color.White.copy(alpha = 0.2f),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .size(56.dp)
                        .testTag("repeat_response_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Repeat audio in ${ttsMode.label}",
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
