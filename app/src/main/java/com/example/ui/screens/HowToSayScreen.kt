package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TtsSpeakerButton
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.viewmodel.MainViewModel

@Composable
fun HowToSayScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    val result by viewModel.howToSayResult.collectAsState()
    val isTranslating by viewModel.isTranslating.collectAsState()
    val isListening by viewModel.voiceManager.isListening.collectAsState()

    val presetQueries = listOf(
        "நாளைக்கு நான் லீவு எடுக்கலாமா?",
        "சாப்பிட்டீங்களா?",
        "இதை கொஞ்சம் செய்து தர முடியுமா?",
        "நான் அப்புறம் கூப்பிடுறேன்",
        "கொஞ்சம் நேரமாகிவிட்டது"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "How to Say in English? 🗣️",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "தமிழில் சொன்னால் சரளமான 3 ஆங்கில பாணிகளில் கற்றுத்தரும்",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }

        // Input Box with mic
        item {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Enter what you want to say in Tamil") },
                placeholder = { Text("எ.கா. நாளைக்கு நான் லீவு எடுக்க முடியுமா?") },
                trailingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (query.isNotBlank()) {
                            IconButton(onClick = { query = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                        IconButton(
                            onClick = {
                                if (isListening) {
                                    viewModel.voiceManager.stopListening()
                                } else {
                                    viewModel.voiceManager.startListening(
                                        languageCode = "ta-IN",
                                        onResult = { spoken ->
                                            query = spoken
                                            viewModel.translateHowToSay(spoken)
                                        }
                                    )
                                }
                            },
                            modifier = Modifier.testTag("how_to_say_mic")
                        ) {
                            Icon(
                                imageVector = if (isListening) Icons.Default.Stop else Icons.Default.Mic,
                                contentDescription = "Speak in Tamil",
                                tint = if (isListening) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("how_to_say_input"),
                shape = RoundedCornerShape(16.dp),
                minLines = 2
            )
        }

        item {
            Button(
                onClick = { viewModel.translateHowToSay(query) },
                enabled = query.isNotBlank() && !isTranslating,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("translate_button")
            ) {
                if (isTranslating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ஆங்கிலத்தில் மாற்றுகிறது...")
                } else {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("எப்படி சொல்வது என்று காட்டு (Convert)")
                }
            }
        }

        // Quick Examples
        item {
            Text(
                text = "உதாரணங்கள் (Try these):",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(presetQueries) { preset ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        onClick = {
                            query = preset
                            viewModel.translateHowToSay(preset)
                        }
                    ) {
                        Text(
                            text = preset,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Results Section: 3 Styles
        if (result != null) {
            item {
                Text(
                    text = "3 Ways to Say This in English:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Casual Style
            item {
                StyleResultCard(
                    title = "1. Casual / Friendly (நண்பர்களுடன்)",
                    phrase = result!!.casual,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    onPlay = { viewModel.voiceManager.speak(result!!.casual) },
                    onSave = {
                        viewModel.saveDirectPhrase(result!!.casual, query, "")
                    }
                )
            }

            // Formal Style
            item {
                StyleResultCard(
                    title = "2. Polite & Professional (அலுவலகம் / மரியாதையாக)",
                    phrase = result!!.formal,
                    color = Color(0xFFECFDF5),
                    textColor = EmeraldSuccess,
                    tanglish = result!!.tanglish,
                    onPlay = { viewModel.voiceManager.speak(result!!.formal) },
                    onSave = {
                        viewModel.saveDirectPhrase(result!!.formal, query, result!!.tanglish)
                    }
                )
            }

            // Short Style
            item {
                StyleResultCard(
                    title = "3. Quick & Short (சுருக்கமாக)",
                    phrase = result!!.short,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    onPlay = { viewModel.voiceManager.speak(result!!.short) },
                    onSave = {
                        viewModel.saveDirectPhrase(result!!.short, query, "")
                    }
                )
            }

            // Coaching Tip
            if (result!!.tip.isNotBlank()) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = "Tip",
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = result!!.tip,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StyleResultCard(
    title: String,
    phrase: String,
    color: Color,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    tanglish: String = "",
    onPlay: () -> Unit,
    onSave: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = phrase,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    TtsSpeakerButton(textToSpeak = phrase, onSpeak = { onPlay() })
                    IconButton(onClick = onSave, modifier = Modifier.size(42.dp)) {
                        Icon(
                            imageVector = Icons.Default.BookmarkAdd,
                            contentDescription = "Save phrase",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
            val tamilPronunciation = com.example.data.ai.TutorEngine.formatPronunciationInTamil(phrase, tanglish)
            if (tamilPronunciation.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "🗣️ உச்சரிப்பு: $tamilPronunciation",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
