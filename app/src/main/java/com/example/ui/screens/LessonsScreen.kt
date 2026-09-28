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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.lessons.LessonCategory
import com.example.data.lessons.LessonDataSource
import com.example.data.lessons.LessonPhrase
import com.example.data.lessons.RoleplayScenario
import com.example.ui.components.ScoreBadge
import com.example.ui.components.TtsSpeakerButton
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonsScreen(
    viewModel: MainViewModel,
    onStartRoleplay: (RoleplayScenario) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val savedPhrases by viewModel.savedPhrases.collectAsState()
    val practiceResult by viewModel.lastPracticeResult.collectAsState()

    val tabs = listOf(
        "PDF Course Book" to "PDF பாடப்புத்தகம்",
        "Adi's Book" to "நூல் பயிற்சி",
        "Phrases" to "வாக்கியம்",
        "Roleplay" to "உரையாடல்",
        "Grammar" to "இலக்கணம்",
        "Mistakes" to "தவறுகள்"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PrimaryScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            edgePadding = 8.dp
        ) {
            tabs.forEachIndexed { index, (en, ta) ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = en,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                softWrap = false
                            )
                            Text(
                                text = ta,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.outline,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> SpokenEnglishPdfBookSection(
                viewModel = viewModel,
                onStartRoleplay = onStartRoleplay
            )
            1 -> BookCourseTab(
                viewModel = viewModel,
                onStartRoleplay = onStartRoleplay
            )
            2 -> ConversationsTab(
                viewModel = viewModel,
                selectedCategory = selectedCategory,
                savedPhrases = savedPhrases.map { it.englishText }.toSet(),
                onSelectCategory = { viewModel.selectCategory(it) }
            )
            3 -> RoleplayTab(
                onStartScenario = { scenario ->
                    viewModel.startScenario(scenario)
                    onStartRoleplay(scenario)
                }
            )
            4 -> GrammarTab(viewModel = viewModel)
            5 -> MistakesTab(viewModel = viewModel)
        }
    }
}

@Composable
fun ConversationsTab(
    viewModel: MainViewModel,
    selectedCategory: LessonCategory?,
    savedPhrases: Set<String>,
    onSelectCategory: (LessonCategory?) -> Unit
) {
    val activeCategory = selectedCategory ?: LessonDataSource.categories.first()
    var practicingPhrase by remember { mutableStateOf<LessonPhrase?>(null) }
    val practiceResult by viewModel.lastPracticeResult.collectAsState()
    val isListening by viewModel.voiceManager.isListening.collectAsState()
    val partialSpeechText by viewModel.voiceManager.partialSpeechText.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Category horizontal picker
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(LessonDataSource.categories) { cat ->
                    val isSelected = cat.id == activeCategory.id
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectCategory(cat) },
                        label = {
                            Text(
                                text = "${cat.titleEnglish} (${cat.titleTamil})",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = activeCategory.titleEnglish,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = activeCategory.descriptionTamil,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                }
            }
        }

        // Practice Result card if available
        if (practiceResult != null && practicingPhrase != null) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (practiceResult!!.score >= 70) Color(0xFFECFDF5) else Color(0xFFFEF3C7)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "உச்சரிப்பு மதிப்பீடு (Speaking Score)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            IconButton(
                                onClick = {
                                    viewModel.clearPracticeResult()
                                    practicingPhrase = null
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        ScoreBadge(score = practiceResult!!.score)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "நீங்கள் பேசியது: \"${practiceResult!!.spokenText}\"",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = practiceResult!!.feedbackTamil,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Phrases list
        items(activeCategory.phrases) { phrase ->
            val isSaved = savedPhrases.contains(phrase.english)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // English Phrase + Audio
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = phrase.english,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                modifier = Modifier
                                    .clickable {
                                        viewModel.voiceManager.speakBilingual(phrase.english, phrase.tamil)
                                    }
                                    .testTag("play_both_${phrase.english.hashCode()}")
                            ) {
                                Text(
                                    text = "Both",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            TtsSpeakerButton(
                                textToSpeak = phrase.english,
                                onSpeak = { viewModel.voiceManager.speakEnglish(it) }
                            )
                            IconButton(
                                onClick = { viewModel.toggleSavePhrase(phrase) },
                                modifier = Modifier.size(38.dp)
                            ) {
                                Icon(
                                    imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Save phrase",
                                    tint = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }

                    // Pronunciation (in Tamil script)
                    Text(
                        text = "🗣️ உச்சரிப்பு: " + phrase.tanglish,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )

                    // Tamil translation + Speak Tamil button
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🇮🇳 " + phrase.tamil,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { viewModel.voiceManager.speakTamil(phrase.tamil) },
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("play_tamil_${phrase.english.hashCode()}")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Speak Tamil translation",
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }

                    // Explanation
                    if (phrase.explanation.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "💡 " + phrase.explanation,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // "Speak & Test" button
                    val isThisPhrasePracticing = isListening && (practicingPhrase?.english == phrase.english)
                    Button(
                        onClick = {
                            if (isThisPhrasePracticing) {
                                viewModel.voiceManager.stopListening()
                            } else {
                                practicingPhrase = phrase
                                viewModel.voiceManager.startListening(
                                    languageCode = "en-IN",
                                    onResult = { spoken ->
                                        viewModel.evaluateSpeakingPractice(phrase.english, spoken)
                                    }
                                )
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isThisPhrasePracticing) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = if (isThisPhrasePracticing) Icons.Default.Stop else Icons.Default.Mic,
                            contentDescription = "Practice speaking",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isThisPhrasePracticing) {
                                if (partialSpeechText.isNotBlank()) "\"$partialSpeechText\"" else "Listening... பேசிப் பார்க்கவும்"
                            } else {
                                "Speak & Test Pronunciation (பேசிப் பார்க்க)"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RoleplayTab(
    onStartScenario: (RoleplayScenario) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Interactive Roleplay Scenarios",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "உண்மையான சூழ்நிலைகளில் தனம் டீச்சருடன் சுபி ஆடியோவில் பேசிப் பழகலாம்",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }

        items(LessonDataSource.roleplayScenarios) { scenario ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = scenario.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = scenario.titleTamil,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "👩‍🏫 தனம் டீச்சரின் பாத்திரம்: ${scenario.botRole}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "👧 சுபிக்சா (சுபி) பாத்திரம்: ${scenario.userRole}",
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "ஆரம்ப வாக்கியம்: \"${scenario.starterMessage}\"",
                                fontSize = 12.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { onStartScenario(scenario) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Start Conversation with Dhanam (தனம் டீச்சருடன் பேசு)")
                    }
                }
            }
        }
    }
}

@Composable
fun GrammarTab(viewModel: MainViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(LessonDataSource.grammarRules) { rule ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = rule.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = rule.titleTamil,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = rule.summaryTamil,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    // Formula box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "📐 பார்முலா: " + rule.formula,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "எடுத்துக்காட்டுகள் (Examples):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    rule.examples.forEach { ex ->
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "• " + ex.english,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = ex.tamil,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                            TtsSpeakerButton(
                                textToSpeak = ex.english,
                                onSpeak = { viewModel.voiceManager.speak(it) },
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "⭐ " + rule.proTipTamil,
                        fontSize = 11.sp,
                        color = Color(0xFFD97706),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun MistakesTab(viewModel: MainViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Common Tamil Speaker English Mistakes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "நாம் அன்றாடம் செய்யும் தவறுகளும் அதற்கான சரியான அமைப்புகளும்",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }

        items(LessonDataSource.commonMistakes) { mistake ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "❌ " + mistake.wrong,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✅ " + mistake.correct,
                            fontSize = 15.sp,
                            color = EmeraldSuccess,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        TtsSpeakerButton(
                            textToSpeak = mistake.correct,
                            onSpeak = { viewModel.voiceManager.speak(it) },
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "🇮🇳 " + mistake.tamilMeaning,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "💡 காரணம்: " + mistake.whyTamil,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}
