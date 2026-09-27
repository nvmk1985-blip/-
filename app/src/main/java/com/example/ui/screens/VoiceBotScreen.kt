package com.example.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.ChatMessageEntity
import com.example.data.lessons.LessonDataSource
import com.example.ui.components.AudioWaveVisualizer
import com.example.ui.components.BilingualAudioButtons
import com.example.ui.components.SpeechRateChip
import com.example.ui.components.TtsModeSelector
import com.example.ui.components.TtsSpeakerButton
import com.example.ui.components.hasAudioRecordingPermission
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.viewmodel.MainViewModel
import com.example.voice.CurrentlySpeakingLanguage
import com.example.voice.SttLanguage
import com.example.voice.TtsMode

@Composable
fun VoiceBotScreen(
    viewModel: MainViewModel,
    onNavigateToCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatHistory.collectAsState()
    val isListening by viewModel.voiceManager.isListening.collectAsState()
    val isSpeaking by viewModel.voiceManager.isSpeaking.collectAsState()
    val currentlySpeakingLang by viewModel.voiceManager.currentlySpeakingLang.collectAsState()
    val currentPlayingMessageId by viewModel.voiceManager.currentPlayingMessageId.collectAsState()
    val ttsMode by viewModel.voiceManager.ttsMode.collectAsState()
    val isTamilTtsAvailable by viewModel.voiceManager.isTamilTtsAvailable.collectAsState()
    val speechRms by viewModel.voiceManager.speechRms.collectAsState()
    val speechRate by viewModel.voiceManager.speechRate.collectAsState()
    val selectedSttLanguage by viewModel.voiceManager.selectedSttLanguage.collectAsState()
    val partialSpeechText by viewModel.voiceManager.partialSpeechText.collectAsState()
    val speechError by viewModel.voiceManager.speechError.collectAsState()
    val isBotThinking by viewModel.isBotThinking.collectAsState()
    val activeScenario by viewModel.activeScenario.collectAsState()
    val context = LocalContext.current

    val systemSpeechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val matches = result.data?.getStringArrayListExtra(android.speech.RecognizerIntent.EXTRA_RESULTS)
            val spoken = matches?.firstOrNull()
            if (!spoken.isNullOrBlank()) {
                viewModel.voiceManager.deliverExternalSpeechResult(spoken)
            }
        }
    }

    val launchSystemVoiceInput = {
        try {
            viewModel.voiceManager.stopSpeaking()
            val intent = android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE, selectedSttLanguage.code)
                putExtra(
                    android.speech.RecognizerIntent.EXTRA_PROMPT,
                    if (selectedSttLanguage == SttLanguage.TAMIL) "தமிழில் பேசுங்கள்..." else "Speak in English..."
                )
            }
            systemSpeechLauncher.launch(intent)
        } catch (_: Exception) {}
    }

    val botPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.voiceManager.startListening(selectedSttLanguage.code)
        }
    }

    var textInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar info & Scenario indicator
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldSuccess)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Malar (மலர்) - Voice Tutor",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        Text(
                            text = if (isSpeaking) {
                                if (currentlySpeakingLang == CurrentlySpeakingLanguage.TAMIL) {
                                    "🔊 மலர் தமிழில் பேசுகிறார் (Speaking Tamil)..."
                                } else {
                                    "🔊 Speaking in English..."
                                }
                            } else if (isListening) "Listening to you..." else "Ready to converse",
                            fontSize = 11.sp,
                            color = if (isSpeaking) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onNavigateToCall,
                            modifier = Modifier.testTag("call_mode_icon")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Switch to Live Call",
                                tint = EmeraldSuccess
                            )
                        }
                        IconButton(
                            onClick = { viewModel.clearChat() },
                            modifier = Modifier.testTag("clear_chat_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Clear Chat",
                                tint = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }

                // TTS Reading Mode Selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "வாசிக்கும் விதம்:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TtsModeSelector(
                        currentMode = ttsMode,
                        onModeChange = { viewModel.setTtsMode(it) }
                    )
                }

                // Scenario indicator banner if active
                if (activeScenario != null) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Roleplay: ${activeScenario?.title} (${activeScenario?.titleTamil})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            IconButton(
                                onClick = { viewModel.clearScenario() },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Exit Roleplay",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // Controls row (Speed & Audio Language)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SpeechRateChip(
                        currentRate = speechRate,
                        onRateChange = { viewModel.voiceManager.setSpeechRate(it) }
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Mic: ",
                            fontSize = 11.sp,
                            maxLines = 1,
                            softWrap = false,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        FilterChip(
                            selected = selectedSttLanguage == SttLanguage.ENGLISH,
                            onClick = { viewModel.setSttLanguage(SttLanguage.ENGLISH) },
                            label = { Text("🇬🇧 Eng", fontSize = 10.5.sp, maxLines = 1, softWrap = false) },
                            modifier = Modifier.height(28.dp).testTag("stt_lang_en")
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        FilterChip(
                            selected = selectedSttLanguage == SttLanguage.TAMIL,
                            onClick = { viewModel.setSttLanguage(SttLanguage.TAMIL) },
                            label = { Text("🇮🇳 தமிழ்", fontSize = 10.5.sp, maxLines = 1, softWrap = false) },
                            modifier = Modifier.height(28.dp).testTag("stt_lang_ta")
                        )
                    }
                }
            }
        }

        // Chat Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                val isThisMessagePlaying = isSpeaking && (currentPlayingMessageId == msg.id)
                ChatMessageItem(
                    message = msg,
                    isSpeakingThis = isThisMessagePlaying,
                    speakingLanguage = if (isThisMessagePlaying) currentlySpeakingLang else CurrentlySpeakingLanguage.NONE,
                    onPlayBoth = {
                        viewModel.voiceManager.speakBilingual(
                            englishText = msg.englishText,
                            tamilText = msg.tamilText,
                            messageId = msg.id
                        )
                    },
                    onPlayEnglish = {
                        viewModel.voiceManager.speakEnglish(
                            text = msg.englishText,
                            messageId = msg.id
                        )
                    },
                    onPlayTamil = {
                        viewModel.voiceManager.speakTamil(
                            text = msg.tamilText,
                            messageId = msg.id
                        )
                    },
                    onStop = { viewModel.voiceManager.stopSpeaking() },
                    onSavePhrase = {
                        viewModel.saveDirectPhrase(
                            english = msg.englishText,
                            tamil = msg.tamilText,
                            tanglish = msg.tanglishText
                        )
                    }
                )
            }

            if (isBotThinking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "மலர் பதிலளிக்கிறார் (Thinking in English & Tamil)...",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Live Audio Waveform & Real-time Transcription when listening
        if (isListening) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.error)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "🎤 ${if (selectedSttLanguage == SttLanguage.TAMIL) "தமிழில் பேசுகிறீர்கள் (Speaking in Tamil)..." else "Speaking in English (en-IN)..."}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (partialSpeechText.isNotBlank()) "\"$partialSpeechText\"" else "பேசத் தொடங்குங்கள்... (Speak now)",
                        fontSize = 14.sp,
                        fontWeight = if (partialSpeechText.isNotBlank()) FontWeight.Bold else FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.testTag("live_speech_transcription")
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    AudioWaveVisualizer(
                        isListening = true,
                        isSpeaking = false,
                        rms = speechRms,
                        activeColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
        } else if (isSpeaking) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (currentlySpeakingLang == CurrentlySpeakingLanguage.TAMIL) "🔊 மலர் தமிழில் விளக்குகிறார்..." else "🔊 Malar is speaking in English...",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        TextButton(
                            onClick = { viewModel.voiceManager.stopSpeaking() },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Stop speaking",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Stop (நிறுத்து)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    AudioWaveVisualizer(
                        isListening = false,
                        isSpeaking = true,
                        rms = speechRms,
                        activeColor = EmeraldSuccess
                    )
                }
            }
        }

        // Error message banner if speech recognition encountered an error
        if (speechError != null && !isListening) {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.92f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable {
                        if (hasAudioRecordingPermission(context)) {
                            viewModel.voiceManager.startListening(selectedSttLanguage.code)
                        } else {
                            botPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "⚠️ $speechError",
                        fontSize = 11.5.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = { launchSystemVoiceInput() },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "System Voice Input",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Popup Mic",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = { viewModel.voiceManager.clearSpeechError() },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss",
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Bottom input bar: Large Mic button + text field
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text("Type in Tamil or English...", fontSize = 13.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("bot_text_input"),
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    if (textInput.isNotBlank()) {
                        FilledIconButton(
                            onClick = {
                                viewModel.sendUserMessage(textInput)
                                textInput = ""
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                        }
                    } else {
                        // Mic Button
                        FloatingActionButton(
                            onClick = {
                                if (isListening) {
                                    viewModel.voiceManager.stopListening()
                                } else if (hasAudioRecordingPermission(context)) {
                                    viewModel.voiceManager.startListening(selectedSttLanguage.code)
                                } else {
                                    botPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            },
                            containerColor = if (isListening) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            contentColor = Color.White,
                            shape = CircleShape,
                            modifier = Modifier
                                .size(52.dp)
                                .testTag("main_mic_button")
                        ) {
                            Icon(
                                imageVector = if (isListening) Icons.Default.Stop else Icons.Default.Mic,
                                contentDescription = if (isListening) "Stop Listening" else "Speak with Malar",
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(
    message: ChatMessageEntity,
    isSpeakingThis: Boolean,
    speakingLanguage: CurrentlySpeakingLanguage,
    onPlayBoth: () -> Unit,
    onPlayEnglish: () -> Unit,
    onPlayTamil: () -> Unit,
    onStop: () -> Unit,
    onSavePhrase: () -> Unit
) {
    val isUser = message.sender == "user"

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        if (isUser) {
            Card(
                shape = RoundedCornerShape(16.dp, 16.dp, 2.dp, 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth(0.85f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = message.englishText,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            Card(
                shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth(0.92f)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // English Response + Bookmark
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = message.englishText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = onSavePhrase,
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("save_phrase_${message.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkAdd,
                                contentDescription = "Save phrase",
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Audio Playback Bar for Bot Message
                    Spacer(modifier = Modifier.height(6.dp))
                    BilingualAudioButtons(
                        englishText = message.englishText,
                        tamilText = message.tamilText,
                        isSpeakingThis = isSpeakingThis,
                        speakingLanguage = speakingLanguage,
                        onPlayBoth = onPlayBoth,
                        onPlayEnglish = onPlayEnglish,
                        onPlayTamil = onPlayTamil,
                        onStop = onStop
                    )

                    // Tanglish pronunciation guide if present
                    if (message.tanglishText.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "🗣️ " + message.tanglishText,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Tamil Explanation + Speak Tamil button
                    if (message.tamilText.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🇮🇳 " + message.tamilText,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = onPlayTamil,
                                    modifier = Modifier
                                        .size(28.dp)
                                        .testTag("play_tamil_explanation_${message.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = "Speak Tamil explanation",
                                        tint = Color(0xFFD97706),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Coaching Tip
                    if (message.coachingTip.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = "Coaching tip",
                                tint = Color(0xFFD97706),
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = message.coachingTip,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }
                }
            }
        }
    }
}
