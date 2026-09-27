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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.lessons.BookDialogueLesson
import com.example.data.lessons.BookLessonModule
import com.example.data.lessons.BookSentenceItem
import com.example.data.lessons.BookStory
import com.example.data.lessons.BookVocabItem
import com.example.data.lessons.RoleplayScenario
import com.example.data.lessons.SpokenTamilBookData
import com.example.ui.components.ScoreBadge
import com.example.ui.components.TtsSpeakerButton
import com.example.ui.viewmodel.MainViewModel

@Composable
fun BookCourseTab(
    viewModel: MainViewModel,
    onStartRoleplay: (RoleplayScenario) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableIntStateOf(0) }
    val sections = listOf(
        "📚 பாடங்கள் (1–50)",
        "💬 உரையாடல் (51–62)",
        "📖 6 கதைகள்",
        "⚡ இலக்கணச் சுருக்கம்"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Sub-section selector row
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(sections) { index, title ->
                val selected = selectedSection == index
                FilterChip(
                    selected = selected,
                    onClick = { selectedSection = index },
                    label = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            maxLines = 1,
                            softWrap = false
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("book_section_$index")
                )
            }
        }

        when (selectedSection) {
            0 -> BookGrammarModulesView(
                viewModel = viewModel,
                onStartRoleplay = onStartRoleplay
            )
            1 -> BookDialoguesView(
                viewModel = viewModel,
                onStartRoleplay = onStartRoleplay
            )
            2 -> BookStoriesView(viewModel = viewModel)
            3 -> BookGrammarGlanceView(viewModel = viewModel)
        }
    }
}

@Composable
private fun BookGrammarModulesView(
    viewModel: MainViewModel,
    onStartRoleplay: (RoleplayScenario) -> Unit
) {
    val modules = SpokenTamilBookData.bookModules
    var selectedModuleIndex by remember { mutableIntStateOf(0) }
    val activeModule = modules[selectedModuleIndex]

    var practicingSentence by remember { mutableStateOf<String?>(null) }
    val practiceResult by viewModel.lastPracticeResult.collectAsState()
    val isListening by viewModel.voiceManager.isListening.collectAsState()
    val partialSpeechText by viewModel.voiceManager.partialSpeechText.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Book Hero Banner
        item {
            BookHeroBanner(
                title = "Common Spoken Tamil & English Made Easy",
                subtitle = "T. V. Adikesavalu (CMC Vellore) — முழுப் பாடத்திட்டம் & ஆடியோ பயிற்சி"
            )
        }

        // Module horizontal chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(modules) { index, mod ->
                    val isSelected = index == selectedModuleIndex
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedModuleIndex = index },
                        label = {
                            Text(
                                text = "${mod.lessonNumbers}: ${mod.titleTamil}",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    )
                }
            }
        }

        // Active Module Teaching Card
        item {
            ModuleOverviewCard(
                module = activeModule,
                onAutoTeach = {
                    val combinedEnglish = activeModule.sentences.joinToString(". ") { it.english }
                    val combinedTamil = activeModule.grammarExplanationTamil + ". " +
                        activeModule.sentences.joinToString(". ") { it.tamilSpoken }
                    viewModel.voiceManager.speakBilingual(combinedEnglish, combinedTamil)
                },
                onPracticeWithMalar = {
                    val scenario = RoleplayScenario(
                        id = "book_${activeModule.id}",
                        title = "${activeModule.lessonNumbers}: ${activeModule.titleEnglish}",
                        titleTamil = "${activeModule.lessonNumbers}: ${activeModule.titleTamil}",
                        botRole = "Adi's Book Teacher (Dhanam / தனம் டீச்சர்)",
                        userRole = "Student Subiksha (Subi, Age 9 / சுபிக்சா)",
                        starterMessage = "Welcome Subi to ${activeModule.lessonNumbers}: ${activeModule.titleEnglish}! Let's practice with Dhanam Teacher: \"${activeModule.sentences.first().english}\"",
                        starterTamil = "வா சுபி! ${activeModule.titleTamil} பாடத்தைப் பயிலுவோம். முதல் வாக்கியம்: \"${activeModule.sentences.first().tamilSpoken}\""
                    )
                    viewModel.startScenario(scenario)
                    onStartRoleplay(scenario)
                }
            )
        }

        // Practice Score Banner if available
        if (practiceResult != null && practicingSentence != null) {
            item {
                PracticeScoreBanner(
                    score = practiceResult!!.score,
                    spokenText = practiceResult!!.spokenText,
                    feedbackTamil = practiceResult!!.feedbackTamil,
                    onClose = {
                        viewModel.clearPracticeResult()
                        practicingSentence = null
                    }
                )
            }
        }

        // Vocabulary Section
        item {
            Text(
                text = "🔤 முக்கிய வார்த்தைகள் (Key Vocabulary)",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(activeModule.vocabulary) { vocab ->
            BookVocabCard(
                vocab = vocab,
                onSpeakBoth = {
                    viewModel.voiceManager.speakBilingual(vocab.englishMeaning, vocab.tamilScript)
                },
                onSpeakEnglish = {
                    viewModel.voiceManager.speakEnglish(vocab.englishMeaning)
                },
                onSpeakTamil = {
                    viewModel.voiceManager.speakTamil(vocab.tamilScript)
                }
            )
        }

        // Short Sentences Section
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "🗣️ வாக்கியப் பயிற்சி (Short Sentences & Teaching)",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(activeModule.sentences) { sentence ->
            BookSentenceTeachCard(
                sentence = sentence,
                isPracticing = isListening && practicingSentence == sentence.english,
                partialSpeechText = partialSpeechText,
                onSpeakBoth = {
                    viewModel.voiceManager.speakBilingual(sentence.english, sentence.tamilSpoken)
                },
                onSpeakEnglish = {
                    viewModel.voiceManager.speakEnglish(sentence.english)
                },
                onSpeakTamil = {
                    viewModel.voiceManager.speakTamil(sentence.tamilSpoken)
                },
                onTogglePractice = {
                    if (isListening && practicingSentence == sentence.english) {
                        viewModel.voiceManager.stopListening()
                    } else {
                        practicingSentence = sentence.english
                        viewModel.voiceManager.startListening(
                            languageCode = "en-IN",
                            onResult = { spoken ->
                                viewModel.evaluateSpeakingPractice(sentence.english, spoken)
                            }
                        )
                    }
                }
            )
        }

        // Expansion Drills
        if (activeModule.expansionDrills.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "🔄 விரிவாக்கப் பயிற்சிகள் (Expansion Drills)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            items(activeModule.expansionDrills) { drill ->
                BookSentenceTeachCard(
                    sentence = drill,
                    isPracticing = isListening && practicingSentence == drill.english,
                    partialSpeechText = partialSpeechText,
                    onSpeakBoth = {
                        viewModel.voiceManager.speakBilingual(drill.english, drill.tamilSpoken)
                    },
                    onSpeakEnglish = {
                        viewModel.voiceManager.speakEnglish(drill.english)
                    },
                    onSpeakTamil = {
                        viewModel.voiceManager.speakTamil(drill.tamilSpoken)
                    },
                    onTogglePractice = {
                        if (isListening && practicingSentence == drill.english) {
                            viewModel.voiceManager.stopListening()
                        } else {
                            practicingSentence = drill.english
                            viewModel.voiceManager.startListening(
                                languageCode = "en-IN",
                                onResult = { spoken ->
                                    viewModel.evaluateSpeakingPractice(drill.english, spoken)
                                }
                            )
                        }
                    }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun BookDialoguesView(
    viewModel: MainViewModel,
    onStartRoleplay: (RoleplayScenario) -> Unit
) {
    val dialogues = SpokenTamilBookData.everydayDialogues
    var selectedIndex by remember { mutableIntStateOf(0) }
    val activeDialogue: BookDialogueLesson = dialogues[selectedIndex]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(dialogues) { idx, dlg ->
                    val isSelected = idx == selectedIndex
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedIndex = idx },
                        label = {
                            Text(
                                text = dlg.titleTamil,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    )
                }
            }
        }

        // Dialogue Header Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = activeDialogue.titleEnglish,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = activeDialogue.titleTamil,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "📍 சூழல்: ${activeDialogue.settingTamil}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val allEng = activeDialogue.lines.joinToString(". ") { it.english }
                                val allTa = activeDialogue.lines.joinToString(". ") { it.tamilSpoken }
                                viewModel.voiceManager.speakBilingual(allEng, allTa)
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Play all", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("முழுவதும் கேள்", fontSize = 12.sp, maxLines = 1, softWrap = false)
                        }
                        OutlinedButton(
                            onClick = {
                                val firstLine = activeDialogue.lines.first()
                            val scenario = RoleplayScenario(
                                    id = activeDialogue.id,
                                    title = activeDialogue.titleEnglish,
                                    titleTamil = activeDialogue.titleTamil,
                                    botRole = "${firstLine.speakerTamil} (தனம் டீச்சர்)",
                                    userRole = "மாணவி சுபிக்சா (சுபி, 9 வயது)",
                                    starterMessage = firstLine.english,
                                    starterTamil = firstLine.tamilSpoken
                                )
                                viewModel.startScenario(scenario)
                                onStartRoleplay(scenario)
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.RecordVoiceOver, contentDescription = "Practice", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("தனம் டீச்சருடன் பேசு", fontSize = 12.sp, maxLines = 1, softWrap = false)
                        }
                    }
                }
            }
        }

        // Vocabulary chips
        items(activeDialogue.vocabulary) { vocab ->
            BookVocabCard(
                vocab = vocab,
                onSpeakBoth = { viewModel.voiceManager.speakBilingual(vocab.englishMeaning, vocab.tamilScript) },
                onSpeakEnglish = { viewModel.voiceManager.speakEnglish(vocab.englishMeaning) },
                onSpeakTamil = { viewModel.voiceManager.speakTamil(vocab.tamilScript) }
            )
        }

        // Dialogue Lines
        itemsIndexed(activeDialogue.lines) { idx, line ->
            val isFirstSpeaker = idx % 2 == 0
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isFirstSpeaker) {
                        MaterialTheme.colorScheme.surfaceVariant
                    } else {
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f)
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "${line.speaker} • ${line.speakerTamil}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                modifier = Modifier.clickable {
                                    viewModel.voiceManager.speakBilingual(line.english, line.tamilSpoken)
                                }
                            ) {
                                Text(
                                    text = "🔊 Both",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            TtsSpeakerButton(
                                textToSpeak = line.english,
                                onSpeak = { viewModel.voiceManager.speakEnglish(it) },
                                modifier = Modifier.size(34.dp)
                            )
                            IconButton(
                                onClick = { viewModel.voiceManager.speakTamil(line.tamilSpoken) },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = "Speak Tamil",
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = line.english,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "🗣️ உச்சரிப்பு: ${line.englishPronunciationTamil}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "🇮🇳 ${line.tamilSpoken}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "📖 Book Romanized: ${line.romanizedTamil}",
                        fontSize = 11.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun BookStoriesView(viewModel: MainViewModel) {
    val stories = SpokenTamilBookData.shortStories
    var selectedStoryIndex by remember { mutableIntStateOf(0) }
    val activeStory: BookStory = stories[selectedStoryIndex]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(stories) { idx, story ->
                    val isSelected = idx == selectedStoryIndex
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedStoryIndex = idx },
                        label = {
                            Text(
                                text = story.titleTamil,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    )
                }
            }
        }

        // Story Header + Listen Full Story Button
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = activeStory.titleTamil,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "${activeStory.titleEnglish} (${activeStory.titleRomanized})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            val fullEng = activeStory.paragraphs.joinToString(" ") { it.english }
                            val fullTa = activeStory.paragraphs.joinToString(" ") { it.tamilSpoken }
                            viewModel.voiceManager.speakBilingual(fullEng, fullTa)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Narrate Story")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "🔊 முழு கதையையும் ஆங்கிலம் + தமிழில் கேள்",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Story Vocabulary
        items(activeStory.vocabulary) { vocab ->
            BookVocabCard(
                vocab = vocab,
                onSpeakBoth = { viewModel.voiceManager.speakBilingual(vocab.englishMeaning, vocab.tamilScript) },
                onSpeakEnglish = { viewModel.voiceManager.speakEnglish(vocab.englishMeaning) },
                onSpeakTamil = { viewModel.voiceManager.speakTamil(vocab.tamilScript) }
            )
        }

        // Story Paragraphs
        itemsIndexed(activeStory.paragraphs) { idx, para ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "பகுதி ${idx + 1} (Part ${idx + 1})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                modifier = Modifier.clickable {
                                    viewModel.voiceManager.speakBilingual(para.english, para.tamilSpoken)
                                }
                            ) {
                                Text(
                                    text = "🔊 Both",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            TtsSpeakerButton(
                                textToSpeak = para.english,
                                onSpeak = { viewModel.voiceManager.speakEnglish(it) },
                                modifier = Modifier.size(34.dp)
                            )
                            IconButton(
                                onClick = { viewModel.voiceManager.speakTamil(para.tamilSpoken) },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = "Speak Tamil",
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = para.english,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "🗣️ உச்சரிப்பு: ${para.englishPronunciationTamil}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "🇮🇳 ${para.tamilSpoken}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "📖 ${para.romanizedTamil}",
                        fontSize = 11.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }

        // Moral Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "🌟 நீதி (Moral of the Story):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF065F46)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = activeStory.moralTamil,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF047857)
                    )
                    Text(
                        text = activeStory.moralEnglish,
                        fontSize = 13.sp,
                        color = Color(0xFF065F46)
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun BookGrammarGlanceView(viewModel: MainViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "⚡ Grammar at a Glance (Page 225)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "பேச்சுத் தமிழ் & ஆங்கில இலக்கண விதிகள் அனைத்தும் ஒரே பார்வையில் (ஆடியோவுடன்)",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                }
            }
        }

        items(SpokenTamilBookData.grammarAtAGlance) { item ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.principle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "உருபு / விதி: ${item.tamilSuffixOrRule}  •  ${item.lessonRef}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            modifier = Modifier.clickable {
                                viewModel.voiceManager.speakBilingual(item.exampleEnglish, item.exampleTamil)
                            }
                        ) {
                            Text(
                                text = "🔊 கேள்",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "🇬🇧 ${item.exampleEnglish}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "🗣️ உச்சரிப்பு: ${item.englishPronunciationTamil}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "🇮🇳 ${item.exampleTamil}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun BookHeroBanner(
    title: String,
    subtitle: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.tertiary
                    )
                )
            )
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.2f),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = "Book",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )
                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun ModuleOverviewCard(
    module: BookLessonModule,
    onAutoTeach: () -> Unit,
    onPracticeWithMalar: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${module.lessonNumbers} • ${module.titleEnglish}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = module.titleTamil,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = module.grammarExplanationTamil,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(10.dp)
            ) {
                Text(
                    text = "📐 விதி (Rule): ${module.grammarFormula}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onAutoTeach,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.School, contentDescription = "Teach", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("பாடத்தை நடத்து", fontSize = 12.sp, maxLines = 1, softWrap = false)
                }
                OutlinedButton(
                    onClick = onPracticeWithMalar,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.RecordVoiceOver, contentDescription = "Practice", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("தனம் டீச்சருடன் பயில்", fontSize = 12.sp, maxLines = 1, softWrap = false)
                }
            }
        }
    }
}

@Composable
private fun BookVocabCard(
    vocab: BookVocabItem,
    onSpeakBoth: () -> Unit,
    onSpeakEnglish: () -> Unit,
    onSpeakTamil: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${vocab.englishMeaning}  •  ${vocab.tamilScript}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "🗣️ ஆங்கில உச்சரிப்பு: ${vocab.englishPronunciationTamil}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "📖 Spoken Tamil: ${vocab.romanizedTamil}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    modifier = Modifier.clickable { onSpeakBoth() }
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
                    textToSpeak = vocab.englishMeaning,
                    onSpeak = { onSpeakEnglish() },
                    modifier = Modifier.size(34.dp)
                )
                IconButton(
                    onClick = onSpeakTamil,
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Speak Tamil",
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BookSentenceTeachCard(
    sentence: BookSentenceItem,
    isPracticing: Boolean,
    partialSpeechText: String,
    onSpeakBoth: () -> Unit,
    onSpeakEnglish: () -> Unit,
    onSpeakTamil: () -> Unit,
    onTogglePractice: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sentence.english,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        modifier = Modifier.clickable { onSpeakBoth() }
                    ) {
                        Text(
                            text = "Both",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TtsSpeakerButton(
                        textToSpeak = sentence.english,
                        onSpeak = { onSpeakEnglish() }
                    )
                    IconButton(
                        onClick = onSpeakTamil,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Speak Tamil",
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "🗣️ உச்சரிப்பு: ${sentence.englishPronunciationTamil}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "🇮🇳 ${sentence.tamilSpoken}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "📖 Book Romanized: ${sentence.romanizedTamil}",
                fontSize = 11.sp,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.outline
            )

            if (sentence.grammarNoteTamil.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "💡 ${sentence.grammarNoteTamil}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onTogglePractice,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPracticing) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = if (isPracticing) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = "Practice",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isPracticing) {
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

@Composable
private fun PracticeScoreBanner(
    score: Int,
    spokenText: String,
    feedbackTamil: String,
    onClose: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (score >= 70) Color(0xFFECFDF5) else Color(0xFFFEF3C7)
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
                IconButton(onClick = onClose, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            ScoreBadge(score = score)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "நீங்கள் பேசியது: \"$spokenText\"",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = feedbackTamil,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
