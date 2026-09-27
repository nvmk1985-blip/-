package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.lessons.LessonDataSource
import com.example.ui.components.AudioWaveVisualizer
import com.example.ui.components.ScoreBadge
import com.example.ui.components.SpeechRateChip
import com.example.ui.components.TtsSpeakerButton
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.viewmodel.MainViewModel

@Composable
fun PronunciationScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val selectedExercise by viewModel.selectedPronunciation.collectAsState()
    val practiceResult by viewModel.lastPracticeResult.collectAsState()
    val isListening by viewModel.voiceManager.isListening.collectAsState()
    val isSpeaking by viewModel.voiceManager.isSpeaking.collectAsState()
    val speechRms by viewModel.voiceManager.speechRms.collectAsState()
    val speechRate by viewModel.voiceManager.speechRate.collectAsState()
    val partialSpeechText by viewModel.voiceManager.partialSpeechText.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Pronunciation Arena (உச்சரிப்பு)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "தமிழ் பேசும் நபர்களுக்கு சவாலான ஆங்கில ஒலிகளை துல்லியமாக உச்சரிக்க பழகலாம்",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }

        // Horizontal sound selector
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(LessonDataSource.pronunciationExercises) { ex ->
                    val isSelected = ex.id == selectedExercise.id
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectPronunciationExercise(ex) },
                        label = {
                            Text(
                                text = ex.title,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        // Active Sound Drill Card
        item {
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "Focus: " + selectedExercise.soundFocus,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        SpeechRateChip(
                            currentRate = speechRate,
                            onRateChange = { viewModel.voiceManager.setSpeechRate(it) }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = selectedExercise.text,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "🇮🇳 " + selectedExercise.tamilMeaning,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = "💡 எப்படி உச்சரிக்க வேண்டும்?",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = selectedExercise.tipTamil,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Audio listen buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.voiceManager.speak(selectedExercise.text) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Listen (கேளுங்கள்)")
                        }

                        Button(
                            onClick = {
                                if (isListening) {
                                    viewModel.voiceManager.stopListening()
                                } else {
                                    viewModel.voiceManager.startListening(
                                        languageCode = "en-IN",
                                        onResult = { spoken ->
                                            viewModel.evaluateSpeakingPractice(selectedExercise.text, spoken)
                                        }
                                    )
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isListening) MaterialTheme.colorScheme.error else EmeraldSuccess
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("pronounce_mic_button")
                        ) {
                            Icon(imageVector = if (isListening) Icons.Default.Stop else Icons.Default.Mic, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isListening) "Stop (நிறுத்து)" else "Record (பேசுக)")
                        }
                    }

                    if (isListening || isSpeaking) {
                        Spacer(modifier = Modifier.height(12.dp))
                        if (isListening && partialSpeechText.isNotBlank()) {
                            Text(
                                text = "🎤 \"$partialSpeechText\"",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                        AudioWaveVisualizer(
                            isListening = isListening,
                            isSpeaking = isSpeaking,
                            rms = speechRms
                        )
                    }
                }
            }
        }

        // Speaking Evaluation Result
        if (practiceResult != null) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (practiceResult!!.score >= 75) Color(0xFFECFDF5) else Color(0xFFFEF3C7)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ScoreBadge(score = practiceResult!!.score)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "You said: \"${practiceResult!!.spokenText}\"",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = practiceResult!!.feedbackTamil,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
