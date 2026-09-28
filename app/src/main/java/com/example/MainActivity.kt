package com.example

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AudioPermissionRationaleDialog
import com.example.ui.components.PersistentPushToTalkBottomBar
import com.example.ui.components.hasAudioRecordingPermission
import com.example.ui.components.openAppPermissionSettings
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HowToSayScreen
import com.example.ui.screens.LessonsScreen
import com.example.ui.screens.PronunciationScreen
import com.example.ui.screens.SavedScreen
import com.example.ui.screens.VoiceBotScreen
import com.example.ui.screens.VoiceCallScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

enum class AppScreen(val title: String, val tamil: String) {
    HOME("Home", "முகப்பு"),
    BOT("Voice Bot", "தோழன்"),
    CALL("Live Call", "அழைப்பு"),
    LESSONS("Lessons", "பாடங்கள்"),
    HOW_TO_SAY("How to Say", "எப்படி சொல்வது"),
    PRONOUNCE("Pronounce", "உச்சரிப்பு"),
    SAVED("Saved", "சேமித்தவை")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp(mainViewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var currentScreen by remember { mutableStateOf(AppScreen.HOME) }

    val isListening by mainViewModel.voiceManager.isListening.collectAsState()
    val speechRms by mainViewModel.voiceManager.speechRms.collectAsState()
    val partialSpeechText by mainViewModel.voiceManager.partialSpeechText.collectAsState()
    val selectedSttLanguage by mainViewModel.voiceManager.selectedSttLanguage.collectAsState()

    var showPermissionRationaleDialog by remember { mutableStateOf(false) }
    var pendingPushToTalkStart by remember { mutableStateOf(false) }

    // Audio Permission Request
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showPermissionRationaleDialog = false
            if (pendingPushToTalkStart) {
                pendingPushToTalkStart = false
                if (currentScreen != AppScreen.BOT && currentScreen != AppScreen.CALL) {
                    currentScreen = AppScreen.BOT
                }
                mainViewModel.startPushToTalk()
            }
        } else {
            pendingPushToTalkStart = false
            showPermissionRationaleDialog = true
            coroutineScope.launch {
                snackbarHostState.showSnackbar("குரல் மூலம் பேச ஆடியோ அனுமதி (Microphone permission) அவசியம்.")
            }
        }
    }

    LaunchedEffect(Unit) {
        if (!hasAudioRecordingPermission(context)) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    if (showPermissionRationaleDialog) {
        AudioPermissionRationaleDialog(
            onConfirmRequestPermission = {
                showPermissionRationaleDialog = false
                pendingPushToTalkStart = true
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            },
            onOpenSettings = {
                showPermissionRationaleDialog = false
                openAppPermissionSettings(context)
            },
            onDismiss = {
                showPermissionRationaleDialog = false
            }
        )
    }

    // Back handling for sub-screens
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        if (currentScreen == AppScreen.CALL) {
            mainViewModel.endCallMode()
        }
        currentScreen = AppScreen.HOME
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (currentScreen != AppScreen.CALL) {
                PersistentPushToTalkBottomBar(
                    currentScreen = currentScreen,
                    isListening = isListening,
                    speechRms = speechRms,
                    partialSpeechText = partialSpeechText,
                    selectedSttLanguage = selectedSttLanguage,
                    onSelectScreen = { currentScreen = it },
                    onToggleSttLanguage = { mainViewModel.setSttLanguage(it) },
                    onStartPushToTalk = {
                        if (hasAudioRecordingPermission(context)) {
                            if (currentScreen != AppScreen.BOT) {
                                currentScreen = AppScreen.BOT
                            }
                            mainViewModel.startPushToTalk()
                        } else {
                            pendingPushToTalkStart = true
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    },
                    onStopPushToTalk = {
                        mainViewModel.stopPushToTalk()
                    },
                    onCancelPushToTalk = {
                        mainViewModel.cancelPushToTalk()
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> HomeScreen(
                    viewModel = mainViewModel,
                    onNavigateToBot = { currentScreen = AppScreen.BOT },
                    onNavigateToCall = { currentScreen = AppScreen.CALL },
                    onNavigateToLessons = { currentScreen = AppScreen.LESSONS },
                    onNavigateToHowToSay = { currentScreen = AppScreen.HOW_TO_SAY },
                    onNavigateToPronounce = { currentScreen = AppScreen.PRONOUNCE }
                )
                AppScreen.BOT -> VoiceBotScreen(
                    viewModel = mainViewModel,
                    onNavigateToCall = { currentScreen = AppScreen.CALL }
                )
                AppScreen.CALL -> VoiceCallScreen(
                    viewModel = mainViewModel,
                    onEndCall = { currentScreen = AppScreen.BOT }
                )
                AppScreen.LESSONS -> LessonsScreen(
                    viewModel = mainViewModel,
                    onStartRoleplay = {
                        currentScreen = AppScreen.BOT
                    },
                    onNavigateToSavedBooks = {
                        currentScreen = AppScreen.SAVED
                    }
                )
                AppScreen.HOW_TO_SAY -> HowToSayScreen(viewModel = mainViewModel)
                AppScreen.PRONOUNCE -> PronunciationScreen(viewModel = mainViewModel)
                AppScreen.SAVED -> SavedScreen(viewModel = mainViewModel)
            }
        }
    }
}

