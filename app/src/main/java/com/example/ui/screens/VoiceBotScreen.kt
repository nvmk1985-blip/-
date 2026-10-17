package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.example.data.api.GeminiApiKeyManager
import com.example.data.api.GeminiModelOption
import com.example.data.db.ChatMessageEntity
import com.example.ui.components.AudioWaveVisualizer
import com.example.ui.components.BilingualAudioButtons
import com.example.ui.components.GeminiApiKeyDialog
import com.example.ui.components.SpeechRateChip
import com.example.ui.components.TtsModeSelector
import com.example.ui.components.VoiceToneChipsRow
import com.example.ui.components.VoiceToneDialog
import com.example.ui.components.hasAudioRecordingPermission
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.viewmodel.MainViewModel
import com.example.util.SampleTextbookPage
import com.example.util.TextbookImageHelper
import com.example.voice.CurrentlySpeakingLanguage
import com.example.voice.SttLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private data class QuickAskChipItem(
    val label: String,
    val prompt: String
)

private val quickAskChips = listOf(
    QuickAskChipItem("🌌 வானம் ஏன் நீலம்?", "Why is the sky blue? வானம் ஏன் நீல நிறமாக உள்ளது?"),
    QuickAskChipItem("🔬 ஒளிச்சேர்க்கை (Science)", "What is photosynthesis? ஒளிச்சேர்க்கை என்றால் என்ன?"),
    QuickAskChipItem("🪐 8 கோள்கள் (Space)", "How many planets are in our solar system? சூரிய குடும்பத்தில் எத்தனை கோள்கள் உள்ளன?"),
    QuickAskChipItem("📐 15 × 12 என்ன? (Math)", "What is 15 multiplied by 12?"),
    QuickAskChipItem("🔢 8-ஆம் வாய்ப்பாடு", "8 வாய்ப்பாடு சொல்லு (8 table)"),
    QuickAskChipItem("📖 12 Tenses (இலக்கணம்)", "Explain Past, Present and Future tense in English"),
    QuickAskChipItem("🗣️ மழை பெய்யுமா? (Translate)", "நாளைக்கு மழை பெய்யுமா என்பதை ஆங்கிலத்தில் எப்படி சொல்வது?"),
    QuickAskChipItem("🦁 சிங்கம் & எலி கதை", "Tell me the Lion and the Mouse story"),
    QuickAskChipItem("🚀 அப்துல் கலாம் (GK)", "Tell me about Dr. A.P.J. Abdul Kalam"),
    QuickAskChipItem("📜 திருக்குறள்", "Tell me about Thiruvalluvar and Thirukkural"),
    QuickAskChipItem("🧩 விடுகதை (Riddle)", "Ask me a fun riddle in English and Tamil"),
    QuickAskChipItem("🙋‍♀️ சுய அறிமுகம்", "How do I introduce myself in English at school?")
)

private val textbookPhotoDoubtChips = listOf(
    QuickAskChipItem(
        "📖 முழுப் பக்க விளக்கம் (Explain Page)",
        "இப்படத்தில் உள்ள பாடத்தை எளிய தமிழிலும் ஆங்கிலத்திலும் முழுமையாக விளக்கிச் சொல்லுங்கள் (Explain this entire textbook page clearly step-by-step)"
    ),
    QuickAskChipItem(
        "🔤 கடின வார்த்தைகள் (Word Meanings)",
        "இப்படத்தில் உள்ள கடினமான ஆங்கில வார்த்தைகளுக்கு தமிழ் அர்த்தம் மற்றும் உச்சரிப்பு சொல்லுங்கள் (List all difficult English words in this photo with Tamil meaning and pronunciation)"
    ),
    QuickAskChipItem(
        "🇮🇳 தமிழ் மொழிபெயர்ப்பு (Tamil Translation)",
        "இப்படத்தில் உள்ள அனைத்து ஆங்கில வாக்கியங்களையும் தமிழில் மொழிபெயர்த்துச் சொல்லுங்கள் (Translate every sentence in this textbook photo into Tamil)"
    ),
    QuickAskChipItem(
        "✍️ வினாக்களுக்கு விடை (Solve Questions)",
        "இப்படத்தில் உள்ள கேள்விகள் அல்லது பயிற்சிகளுக்கு சரியான விடைகளை விளக்கத்துடன் தாருங்கள் (Solve the questions or exercises shown in this textbook photo)"
    ),
    QuickAskChipItem(
        "🗣️ எப்படி வாசிப்பது? (Pronunciation)",
        "இப்படத்தில் உள்ள வரிகளை எப்படி சரியாக உச்சரித்து வாசிக்க வேண்டும் என்று தமிழ் உச்சரிப்புடன் சொல்லுங்கள்"
    )
)

@Composable
fun VoiceBotScreen(
    viewModel: MainViewModel,
    onNavigateToCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatHistory.collectAsState()
    val selectedGeminiModel by viewModel.selectedGeminiModel.collectAsState()
    val attachedImagePath by viewModel.attachedImagePath.collectAsState()
    val attachedSamplePage by viewModel.attachedSamplePage.collectAsState()
    val lastAnalyzedImagePath by viewModel.lastAnalyzedImagePath.collectAsState()
    val openCameraSheetOnLaunch by viewModel.openCameraSheetOnLaunch.collectAsState()

    val isListening by viewModel.voiceManager.isListening.collectAsState()
    val isSpeaking by viewModel.voiceManager.isSpeaking.collectAsState()
    val currentlySpeakingLang by viewModel.voiceManager.currentlySpeakingLang.collectAsState()
    val currentPlayingMessageId by viewModel.voiceManager.currentPlayingMessageId.collectAsState()
    val ttsMode by viewModel.voiceManager.ttsMode.collectAsState()
    val speechRms by viewModel.voiceManager.speechRms.collectAsState()
    val speechRate by viewModel.voiceManager.speechRate.collectAsState()
    val selectedSttLanguage by viewModel.voiceManager.selectedSttLanguage.collectAsState()
    val partialSpeechText by viewModel.voiceManager.partialSpeechText.collectAsState()
    val speechError by viewModel.voiceManager.speechError.collectAsState()
    val isBotThinking by viewModel.isBotThinking.collectAsState()
    val activeScenario by viewModel.activeScenario.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showCameraPickerDialog by remember { mutableStateOf(false) }
    var showVoiceToneDialog by remember { mutableStateOf(false) }
    var showApiKeyDialog by remember { mutableStateOf(false) }
    var pendingCameraFilePath by remember { mutableStateOf<String?>(null) }
    var fullScreenPreviewPath by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(openCameraSheetOnLaunch) {
        if (openCameraSheetOnLaunch) {
            showCameraPickerDialog = true
            viewModel.consumeOpenCameraSheet()
        }
    }

    // Full-resolution Camera launcher using FileProvider
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val path = pendingCameraFilePath
        if (success && !path.isNullOrBlank()) {
            viewModel.attachCapturedCameraFile(path)
            showCameraPickerDialog = false
            Toast.makeText(
                context,
                "📷 பாடப்புத்தக போட்டோ தயார்! இப்போது விளக்கம் அல்லது சந்தேகம் கேட்கலாம்.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Fallback bitmap preview camera launcher (in case FileProvider camera intent isn't supported on some virtual camera setups)
    val takePicturePreviewLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            coroutineScope.launch {
                val savedPath = withContext(Dispatchers.IO) {
                    TextbookImageHelper.saveBitmapToInternalFile(context, bitmap)
                }
                if (savedPath != null) {
                    viewModel.attachCapturedCameraFile(savedPath)
                    showCameraPickerDialog = false
                    Toast.makeText(
                        context,
                        "📷 பாடப்புத்தக போட்டோ தயார்! இப்போது சந்தேகம் கேட்கலாம்.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    val launchCameraCapture = {
        try {
            val (file, uri) = TextbookImageHelper.createTempCameraFileAndUri(context)
            pendingCameraFilePath = file.absolutePath
            takePictureLauncher.launch(uri)
        } catch (_: Exception) {
            try {
                takePicturePreviewLauncher.launch(null)
            } catch (_: Exception) {
                Toast.makeText(
                    context,
                    "கேமரா திறக்க இயலவில்லை. மாதிரி பாடப்புத்தகப் பக்கத்தை பயன்படுத்தலாம்!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCameraCapture()
        } else {
            Toast.makeText(
                context,
                "கேமரா அனுமதி தேவை (Camera permission is required to take textbook photos).",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Zero-permission Android Photo Picker for gallery textbook images
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.attachGalleryUri(uri)
            showCameraPickerDialog = false
            Toast.makeText(
                context,
                "🖼️ பாடப்புத்தக படம் இணைக்கப்பட்டது! உங்கள் சந்தேகத்தைக் கேளுங்கள்.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

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
                    if (selectedSttLanguage == SttLanguage.TAMIL) "தமிழில் எந்தக் கேள்வியும் கேளுங்கள்..." else "Ask any question in English..."
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
        // Top Bar info, Gemini 3.8 Flash Model Selector & Camera Button
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
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldSuccess)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Dhanam Teacher (தனம்) • ${selectedGeminiModel.displayName}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = if (isSpeaking) {
                                if (currentlySpeakingLang == CurrentlySpeakingLanguage.TAMIL) {
                                    "🔊 தனம் டீச்சர் தமிழில் பதிலளிக்கிறார்..."
                                } else {
                                    "🔊 Dhanam Teacher is answering in English..."
                                }
                            } else if (isListening) {
                                "🎤 சுபி கேட்பதைக் கவனிக்கிறார் (Listening)..."
                            } else {
                                "📷 புத்தக போட்டோ விளக்கம் & சந்தேகம் • எதை கேட்டாலும் பதிலளிக்கும்"
                            },
                            fontSize = 11.sp,
                            color = if (isSpeaking) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { showCameraPickerDialog = true },
                            modifier = Modifier.testTag("top_textbook_camera_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Textbook Camera Doubt Solver",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
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

                // Gemini 3.8 Flash Model Selector + Textbook Camera Banner Chip
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                ) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            onClick = { showCameraPickerDialog = true },
                            modifier = Modifier
                                .height(28.dp)
                                .testTag("open_camera_banner_chip")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddAPhoto,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "📷 புத்தக போட்டோ எடு (Textbook Scan)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }
                    }

                    items(GeminiModelOption.entries) { modelOption ->
                        val isSelected = selectedGeminiModel == modelOption
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setGeminiModel(modelOption) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = modelOption.displayName,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedLeadingIconColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .height(28.dp)
                                .testTag("model_chip_${modelOption.id}")
                        )
                    }

                    item {
                        val hasKey = GeminiApiKeyManager.hasValidApiKey(context)
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (hasKey) Color(0xFF065F46) else MaterialTheme.colorScheme.surfaceVariant,
                            onClick = { showApiKeyDialog = true },
                            modifier = Modifier
                                .height(28.dp)
                                .testTag("api_key_settings_chip")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Text(
                                    text = if (hasKey) "🔑 Gemini Live ✅" else "🔑 API Key",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (hasKey) Color(0xFF6EE7B7) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
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

                // 5 Voice Tones (Natural Tamil & English Pronunciation)
                VoiceToneChipsRow(
                    voiceManager = viewModel.voiceManager,
                    modifier = Modifier.padding(top = 6.dp),
                    onOpenFullSelector = { showVoiceToneDialog = true }
                )

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
                    modelBadge = selectedGeminiModel.displayName,
                    isSpeakingThis = isThisMessagePlaying,
                    speakingLanguage = if (isThisMessagePlaying) currentlySpeakingLang else CurrentlySpeakingLanguage.NONE,
                    onImageClick = { imagePath ->
                        fullScreenPreviewPath = imagePath
                    },
                    onAskFollowUpOnImage = { imagePath ->
                        viewModel.attachCapturedCameraFile(imagePath)
                    },
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
                            text = "⚡ ${selectedGeminiModel.displayName} மூலம் தனம் டீச்சர் ஆராய்ந்து பதிலளிக்கிறார்...",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Follow-up bar if user previously analyzed a textbook photo and no new photo is currently staged
        if (attachedImagePath == null && lastAnalyzedImagePath != null) {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.75f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "பாடப்புத்தகப் படம் தயாராக உள்ளது",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primary,
                            onClick = {
                                lastAnalyzedImagePath?.let { viewModel.attachCapturedCameraFile(it) }
                            }
                        ) {
                            Text(
                                text = "❓ மேலும் சந்தேகம் கேள்",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        IconButton(
                            onClick = { viewModel.clearActiveTextbookContext() },
                            modifier = Modifier.size(22.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear textbook context",
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }

        // Attached Textbook Photo Preview Panel (when a photo is staged for explanation/doubt)
        if (attachedImagePath != null) {
            val bitmap = remember(attachedImagePath) {
                TextbookImageHelper.loadScaledBitmap(attachedImagePath!!, maxDimension = 800)
            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.92f)
                ),
                shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("attached_textbook_panel")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = attachedSamplePage?.titleTamil
                                    ?: "📷 பாடப்புத்தகப் படம் இணைக்கப்பட்டுள்ளது (Textbook Photo Ready)",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        IconButton(
                            onClick = { viewModel.clearAttachedImage() },
                            modifier = Modifier.size(26.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove attached photo",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (bitmap != null) {
                            Box(
                                modifier = Modifier
                                    .size(width = 88.dp, height = 110.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(
                                        width = 1.5.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { fullScreenPreviewPath = attachedImagePath }
                            ) {
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = "Textbook photo preview",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(4.dp)
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(Color.Black.copy(alpha = 0.6f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ZoomIn,
                                        contentDescription = "Zoom",
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "கீழே உள்ள பட்டனை அழுத்தவும் அல்லது உங்கள் சந்தேகத்தை டைப் / மைக்கில் பேசவும்:",
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Button(
                                onClick = {
                                    viewModel.sendTextbookPhotoQuestion(
                                        question = textInput.ifBlank {
                                            "இப்படத்தில் உள்ள பாடத்தை எளிய தமிழிலும் ஆங்கிலத்திலும் முழுமையாக விளக்கிச் சொல்லுங்கள் (Explain this textbook page clearly in Tamil and English)"
                                        }
                                    )
                                    textInput = ""
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("explain_textbook_now_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "📖 முழுப் பக்க விளக்கம் கேள்",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick Doubt Chips specifically for the attached textbook photo
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(textbookPhotoDoubtChips) { chip ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surface,
                                onClick = {
                                    viewModel.sendTextbookPhotoQuestion(question = chip.prompt)
                                    textInput = ""
                                }
                            ) {
                                Text(
                                    text = chip.label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
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
                            text = if (attachedImagePath != null) {
                                "🎤 புத்தகப் படத்தைப் பற்றிய உங்கள் சந்தேகத்தைக் கேளுங்கள்..."
                            } else {
                                "🎤 ${if (selectedSttLanguage == SttLanguage.TAMIL) "தமிழில் எந்தக் கேள்வியும் கேளுங்கள் (Speaking in Tamil)..." else "Ask anything in English (en-IN)..."}"
                            },
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
                            text = if (currentlySpeakingLang == CurrentlySpeakingLanguage.TAMIL) "🔊 தனம் டீச்சர் தமிழில் விளக்குகிறார்..." else "🔊 Dhanam Teacher is speaking in English...",
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

        // Bottom input bar: Quick Ask Anything Chips + Camera button + text field + Mic button
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                // Quick Ask Anything chips (shown when no textbook photo is staged)
                if (attachedImagePath == null) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        items(quickAskChips) { item ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                                onClick = {
                                    viewModel.sendUserMessage(item.prompt)
                                }
                            ) {
                                Text(
                                    text = item.label,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Camera Button right in the input bar for instant Textbook Photo capture
                    FilledIconButton(
                        onClick = { showCameraPickerDialog = true },
                        colors = androidx.compose.material3.IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        ),
                        modifier = Modifier
                            .size(46.dp)
                            .testTag("input_bar_camera_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Take Textbook Photo",
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = {
                            Text(
                                text = if (attachedImagePath != null) {
                                    "இந்தப் படத்தைப் பற்றிய சந்தேகத்தைக் கேளுங்கள்..."
                                } else {
                                    "கேள்வி கேள் அல்லது 📷 புத்தக போட்டோ எடு..."
                                },
                                fontSize = 12.sp
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("bot_text_input"),
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (attachedImagePath != null || textInput.isNotBlank()) {
                                    viewModel.sendUserMessage(textInput)
                                    textInput = ""
                                }
                            }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    if (textInput.isNotBlank() || attachedImagePath != null) {
                        FilledIconButton(
                            onClick = {
                                viewModel.sendUserMessage(textInput)
                                textInput = ""
                            },
                            modifier = Modifier
                                .size(46.dp)
                                .testTag("send_message_button")
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                        }
                    }

                    // Mic Button always accessible
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
                            .size(48.dp)
                            .testTag("main_mic_button")
                    ) {
                        Icon(
                            imageVector = if (isListening) Icons.Default.Stop else Icons.Default.Mic,
                            contentDescription = if (isListening) "Stop Listening" else "Speak with Dhanam Teacher",
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }

    // Textbook Camera & Gallery & Sample Page Modal Dialog
    if (showCameraPickerDialog) {
        TextbookCameraPickerDialog(
            onDismiss = { showCameraPickerDialog = false },
            onTakeCameraPhoto = {
                val hasCamPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
                if (hasCamPermission) {
                    launchCameraCapture()
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            onPickFromGallery = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            onSelectSamplePage = { samplePage, autoExplain ->
                showCameraPickerDialog = false
                viewModel.attachSampleTextbookPage(samplePage, autoAnalyze = autoExplain)
            }
        )
    }

    // 5 Voice Tones Selection Dialog
    if (showVoiceToneDialog) {
        VoiceToneDialog(
            voiceManager = viewModel.voiceManager,
            onDismiss = { showVoiceToneDialog = false }
        )
    }

    // Gemini API Key Settings Dialog
    if (showApiKeyDialog) {
        GeminiApiKeyDialog(
            onDismiss = { showApiKeyDialog = false }
        )
    }

    // Full-screen Textbook Image Zoom Dialog
    if (fullScreenPreviewPath != null) {
        val fullBitmap = remember(fullScreenPreviewPath) {
            TextbookImageHelper.loadScaledBitmap(fullScreenPreviewPath!!, maxDimension = 1400)
        }
        Dialog(onDismissRequest = { fullScreenPreviewPath = null }) {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📖 பாடப்புத்தகப் பக்கம் (Textbook Photo)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { fullScreenPreviewPath = null }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                    if (fullBitmap != null) {
                        Image(
                            bitmap = fullBitmap.asImageBitmap(),
                            contentDescription = "Full textbook page",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 240.dp, max = 460.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            val path = fullScreenPreviewPath
                            fullScreenPreviewPath = null
                            if (path != null) {
                                viewModel.attachCapturedCameraFile(path)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("❓ இந்தப் பக்கத்தில் சந்தேகம் கேள் (Ask Doubt)")
                    }
                }
            }
        }
    }
}

@Composable
private fun TextbookCameraPickerDialog(
    onDismiss: () -> Unit,
    onTakeCameraPhoto: () -> Unit,
    onPickFromGallery: () -> Unit,
    onSelectSamplePage: (SampleTextbookPage, Boolean) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("textbook_camera_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "📷 பாடப்புத்தக போட்டோ & சந்தேகம்",
                                fontSize = 15.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Textbook Photo Explanation & Doubt Solver",
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Text(
                    text = "உங்கள் ஆங்கிலம் அல்லது பாடப்புத்தகப் பக்கத்தை போட்டோ எடுத்து, அதன் தமிழ் விளக்கம், உச்சரிப்பு மற்றும் சந்தேகங்களை உடனடியாகக் கேளுங்கள்!",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Option 1: Take Photo with Camera
                Button(
                    onClick = onTakeCameraPhoto,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(vertical = 12.dp, horizontal = 14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("take_camera_photo_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.weight(1f)) {
                        Text(
                            text = "📸 கேமராவில் போட்டோ எடு (Take Photo)",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "பாடப்புத்தகப் பக்கத்தை நேரடியாகப் படம் பிடிக்கவும்",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                        )
                    }
                }

                // Option 2: Pick Photo from Gallery
                FilledTonalButton(
                    onClick = onPickFromGallery,
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(vertical = 12.dp, horizontal = 14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("pick_gallery_photo_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🖼️ கேலரியில் இருந்து தேர்வு செய் (Gallery)",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "ஏற்கனவே எடுத்த புத்தகப் படத்தைத் தேர்ந்தெடுக்கவும்",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f)
                        )
                    }
                }

                // Option 3: Built-in Sample Textbook Pages for instant testing
                Text(
                    text = "📚 மாதிரி பாடப்புத்தகப் பக்கங்கள் (உடனடி சோதனை):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SampleTextbookPage.entries.forEach { samplePage ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                            onClick = { onSelectSamplePage(samplePage, true) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("sample_textbook_${samplePage.id}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = samplePage.titleTamil,
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = samplePage.titleEnglish,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "விளக்கம் கேள் →",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
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
    modelBadge: String = "Gemini 3.8 Flash",
    isSpeakingThis: Boolean,
    speakingLanguage: CurrentlySpeakingLanguage,
    onImageClick: (String) -> Unit = {},
    onAskFollowUpOnImage: (String) -> Unit = {},
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
                    if (message.imagePath.isNotBlank()) {
                        val userBitmap = remember(message.imagePath) {
                            TextbookImageHelper.loadScaledBitmap(message.imagePath, maxDimension = 800)
                        }
                        if (userBitmap != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { onImageClick(message.imagePath) }
                            ) {
                                Image(
                                    bitmap = userBitmap.asImageBitmap(),
                                    contentDescription = "Captured textbook photo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Surface(
                                    color = Color.Black.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(6.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ZoomIn,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "பெரிதாக்கு (Zoom)",
                                            color = Color.White,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    Text(
                        text = message.englishText,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )

                    if (message.imagePath.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White.copy(alpha = 0.2f),
                            onClick = { onAskFollowUpOnImage(message.imagePath) }
                        ) {
                            Text(
                                text = "❓ இந்தப் படத்தில் மேலும் சந்தேகம் கேள்",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        } else {
            Card(
                shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth(0.92f)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // AI Model Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "தனம் டீச்சர் • $modelBadge",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

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

                    // Tamil-script pronunciation guide for the English sentence
                    val tamilPronunciation = com.example.data.ai.TutorEngine.formatPronunciationInTamil(
                        message.englishText,
                        message.tanglishText
                    )
                    if (tamilPronunciation.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "🗣️ உச்சரிப்பு: $tamilPronunciation",
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
