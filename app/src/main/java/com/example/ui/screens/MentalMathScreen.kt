package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.math.MathDifficulty
import com.example.data.math.MathOperation
import com.example.data.math.MathVocabularyItem
import com.example.data.math.MentalMathDataSource
import com.example.data.math.MentalMathQuestion
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.delay

enum class MentalMathTab(val titleEn: String, val titleTa: String) {
    PRACTICE("Practice Drill", "பயிற்சி"),
    SPEED_TEST("Speed Quiz (60s)", "வேகத் தேர்வு"),
    GLOSSARY("Math Glossary", "கணித சொற்கள்")
}

@Composable
fun MentalMathScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(MentalMathTab.PRACTICE) }
    var selectedOperation by remember { mutableStateOf(MathOperation.ALL) }
    var selectedDifficulty by remember { mutableStateOf(MathDifficulty.BEGINNER) }

    // Session stats
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var questionsAnswered by remember { mutableIntStateOf(0) }
    var correctCount by remember { mutableIntStateOf(0) }

    // Current question
    var currentQuestion by remember {
        mutableStateOf(MentalMathDataSource.curatedQuestions.first())
    }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }

    // Question picker helper
    fun loadNextQuestion() {
        selectedOption = null
        isAnswerSubmitted = false
        // Try curated matching or generate dynamic
        val matchingCurated = MentalMathDataSource.curatedQuestions.filter {
            (selectedOperation == MathOperation.ALL || it.operation == selectedOperation) &&
                    it.difficulty == selectedDifficulty &&
                    it.id != currentQuestion.id
        }

        currentQuestion = if (matchingCurated.isNotEmpty() && kotlin.random.Random.nextBoolean()) {
            matchingCurated.random()
        } else {
            MentalMathDataSource.generateDynamicQuestion(selectedOperation, selectedDifficulty)
        }
    }

    // Auto-update when category or difficulty changes
    LaunchedEffect(selectedOperation, selectedDifficulty) {
        loadNextQuestion()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        Surface(
            tonalElevation = 3.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Home",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "🧠 Mental Math • மனக் கணக்கு",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "English Arithmetic + தமிழ் வழி படிமுறை விளக்கம்",
                        fontSize = 11.5.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Stats badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = EmeraldSuccess.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.4f)),
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🔥 $streak", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = EmeraldSuccess)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "⭐ $score", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = AmberAccent)
                    }
                }
            }
        }

        // Mode Tabs
        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            MentalMathTab.entries.forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = tab.titleEn,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal
                            )
                            Text(
                                text = tab.titleTa,
                                fontSize = 10.sp,
                                color = if (selectedTab == tab) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    modifier = Modifier.testTag("tab_${tab.name.lowercase()}")
                )
            }
        }

        when (selectedTab) {
            MentalMathTab.PRACTICE -> {
                PracticeTabContent(
                    viewModel = viewModel,
                    question = currentQuestion,
                    selectedOperation = selectedOperation,
                    onSelectOperation = { selectedOperation = it },
                    selectedDifficulty = selectedDifficulty,
                    onSelectDifficulty = { selectedDifficulty = it },
                    selectedOption = selectedOption,
                    isAnswerSubmitted = isAnswerSubmitted,
                    onSelectOption = { option ->
                        if (!isAnswerSubmitted) {
                            selectedOption = option
                            isAnswerSubmitted = true
                            questionsAnswered++
                            val isCorrect = option == currentQuestion.correctAnswer
                            if (isCorrect) {
                                score += 10
                                streak++
                                correctCount++
                                viewModel.incrementWordsSpoken(currentQuestion.questionEn.split(" ").size)
                            } else {
                                streak = 0
                            }
                        }
                    },
                    onNextQuestion = { loadNextQuestion() }
                )
            }
            MentalMathTab.SPEED_TEST -> {
                SpeedTestTabContent(
                    viewModel = viewModel,
                    selectedDifficulty = selectedDifficulty,
                    onFinishQuiz = { finalScore ->
                        score += finalScore
                    }
                )
            }
            MentalMathTab.GLOSSARY -> {
                GlossaryTabContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun PracticeTabContent(
    viewModel: MainViewModel,
    question: MentalMathQuestion,
    selectedOperation: MathOperation,
    onSelectOperation: (MathOperation) -> Unit,
    selectedDifficulty: MathDifficulty,
    onSelectDifficulty: (MathDifficulty) -> Unit,
    selectedOption: Int?,
    isAnswerSubmitted: Boolean,
    onSelectOption: (Int) -> Unit,
    onNextQuestion: () -> Unit
) {
    val isSpeaking by viewModel.voiceManager.isSpeaking.collectAsState()
    var explanationTab by remember { mutableIntStateOf(0) } // 0 = Both/English, 1 = Tamil

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))

            // Operations Filter Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(MathOperation.entries) { op ->
                    FilterChip(
                        selected = selectedOperation == op,
                        onClick = { onSelectOperation(op) },
                        label = {
                            Text(
                                text = "${op.symbol} ${op.titleTa}",
                                fontSize = 12.sp,
                                fontWeight = if (selectedOperation == op) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier.testTag("filter_op_${op.name.lowercase()}")
                    )
                }
            }
        }

        item {
            // Difficulty row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MathDifficulty.entries.forEach { diff ->
                    val isSelected = selectedDifficulty == diff
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSelectDifficulty(diff) }
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${diff.icon} ${diff.titleEn}",
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = diff.titleTa,
                                color = if (isSelected) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        // Question Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mental_math_question_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Question Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = "${question.operation.symbol} ${question.operation.titleEn}",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        // Listen to question audio button
                        Surface(
                            shape = CircleShape,
                            color = if (isSpeaking) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    viewModel.voiceManager.speakBilingual(
                                        englishText = question.questionEn,
                                        tamilText = question.questionTa
                                    )
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = "Speak Question",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Listen (கேள்)",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Big Question in English
                    Text(
                        text = question.questionEn,
                        fontSize = if (question.questionEn.length > 35) 18.sp else 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        lineHeight = if (question.questionEn.length > 35) 24.sp else 32.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Tamil translation
                    Text(
                        text = "🇮🇳 ${question.questionTa}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Tanglish pronunciation guide
                    Text(
                        text = "🗣️ ${question.tanglishPronunciation}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }

        // 4 Options Grid (2x2)
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val row1 = question.options.take(2)
                val row2 = question.options.drop(2)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row1.forEach { option ->
                        OptionButton(
                            option = option,
                            isCorrect = option == question.correctAnswer,
                            isSelected = selectedOption == option,
                            isAnswerSubmitted = isAnswerSubmitted,
                            onClick = { onSelectOption(option) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row2.forEach { option ->
                        OptionButton(
                            option = option,
                            isCorrect = option == question.correctAnswer,
                            isSelected = selectedOption == option,
                            isAnswerSubmitted = isAnswerSubmitted,
                            onClick = { onSelectOption(option) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Bot Explanation Card (Appears immediately after submitting an answer or can be manually toggled)
        item {
            AnimatedVisibility(
                visible = isAnswerSubmitted,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("bot_explanation_card")
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        // Feedback banner (Correct vs Try again)
                        val isUserCorrect = selectedOption == question.correctAnswer
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isUserCorrect) EmeraldSuccess.copy(alpha = 0.15f) else Color(0xFFEF4444).copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isUserCorrect) EmeraldSuccess else Color(0xFFEF4444)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isUserCorrect) Icons.Default.Check else Icons.Default.Close,
                                    contentDescription = null,
                                    tint = if (isUserCorrect) EmeraldSuccess else Color(0xFFEF4444),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = if (isUserCorrect) "🎉 Correct! Super Subi! (+10 pts)" else "💡 Good try! Correct Answer is ${question.correctAnswer}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isUserCorrect) EmeraldSuccess else Color(0xFFDC2626)
                                    )
                                    Text(
                                        text = if (isUserCorrect) "மிகச் சரி! கீழே உள்ள படிமுறை விளக்கத்தைப் படித்துப் பாருங்கள்." else "இதோ தனம் டீச்சரின் இருமொழி படிமுறை விளக்கம்:",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Dhanam Teacher Header with Listen Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(text = "👩‍🏫", fontSize = 16.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "தனம் டீச்சரின் படிமுறை விளக்கம்",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Dhanam Teacher's Step-by-Step Solution",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            // Speak explanation button
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.clickable {
                                    val fullEn = question.stepsEn.joinToString(" ") + " " + question.mentalTrickEn
                                    val fullTa = question.stepsTa.joinToString(" ") + " " + question.mentalTrickTa
                                    viewModel.voiceManager.speakBilingual(fullEn, fullTa)
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = "Speak Steps",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("விளக்கம் கேள்", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Steps List (Bilingual parallel view)
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            question.stepsEn.indices.forEach { index ->
                                val stepEn = question.stepsEn.getOrNull(index) ?: ""
                                val stepTa = question.stepsTa.getOrNull(index) ?: ""

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "🇬🇧 $stepEn",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "🇮🇳 $stepTa",
                                            fontSize = 12.5.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Mental Math Shortcut Trick Box
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = AmberAccent.copy(alpha = 0.12f)
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AmberAccent.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = "Mental Trick",
                                        tint = AmberAccent,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "⚡ Mental Math Trick (மனக் கணக்கு உத்தி)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = AmberAccent
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = question.mentalTrickEn,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.5.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = question.mentalTrickTa,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Vocabulary Chips
                        if (question.vocabulary.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "📚 Math Vocabulary (கணித ஆங்கிலச் சொற்கள்):",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                question.vocabulary.forEach { vocab ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        modifier = Modifier.clickable {
                                            viewModel.voiceManager.speakBilingual(
                                                englishText = "${vocab.english}. ${vocab.exampleEn}",
                                                tamilText = "${vocab.tamil}. ${vocab.exampleTa}"
                                            )
                                        }
                                    ) {
                                        Text(
                                            text = "🔊 ${vocab.english} = ${vocab.tamil}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Next Question Button
                        Button(
                            onClick = onNextQuestion,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("next_question_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Next Question",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Next Question • அடுத்த கணக்கு ➡️",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun OptionButton(
    option: Int,
    isCorrect: Boolean,
    isSelected: Boolean,
    isAnswerSubmitted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isAnswerSubmitted && isCorrect -> EmeraldSuccess
            isAnswerSubmitted && isSelected && !isCorrect -> Color(0xFFEF4444)
            isSelected -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.surface
        },
        label = "option_bg"
    )

    val textColor by animateColorAsState(
        targetValue = when {
            isAnswerSubmitted && (isCorrect || (isSelected && !isCorrect)) -> Color.White
            isSelected -> Color.White
            else -> MaterialTheme.colorScheme.onSurface
        },
        label = "option_text"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        shadowElevation = if (isSelected) 4.dp else 2.dp,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isAnswerSubmitted && isCorrect) 2.dp else 1.dp,
            color = if (isAnswerSubmitted && isCorrect) EmeraldSuccess else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clickable(enabled = !isAnswerSubmitted) { onClick() }
            .testTag("option_button_$option")
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 18.dp, horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$option",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SpeedTestTabContent(
    viewModel: MainViewModel,
    selectedDifficulty: MathDifficulty,
    onFinishQuiz: (Int) -> Unit
) {
    var isRunning by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableIntStateOf(60) }
    var currentQuestion by remember {
        mutableStateOf(MentalMathDataSource.generateDynamicQuestion(MathOperation.ALL, selectedDifficulty))
    }
    var score by remember { mutableIntStateOf(0) }
    var correctCount by remember { mutableIntStateOf(0) }
    var totalQuestions by remember { mutableIntStateOf(0) }
    var isFinished by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            timeLeft = 60
            score = 0
            correctCount = 0
            totalQuestions = 0
            isFinished = false
            currentQuestion = MentalMathDataSource.generateDynamicQuestion(MathOperation.ALL, selectedDifficulty)

            while (timeLeft > 0 && isRunning) {
                delay(1000L)
                timeLeft--
            }
            if (isRunning) {
                isRunning = false
                isFinished = true
                onFinishQuiz(score)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isRunning && !isFinished) {
            // Welcome Card for Speed Test
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "⚡", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "60-Second Mental Math Challenge!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "60 வினாடிகளில் முடிந்தவரை அதிக மனக் கணக்குகளுக்கு சரியான விடையளித்து சாதனை புரியுங்கள்!",
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = { isRunning = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("start_speed_quiz_button")
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Start Speed Quiz • தொடங்கு 🚀", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        } else if (isRunning) {
            // Running Quiz
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Timer, contentDescription = null, tint = AmberAccent)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "$timeLeft s", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = AmberAccent)
                }

                Text(text = "Score: $score", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = EmeraldSuccess)
            }

            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { timeLeft / 60f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Question Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentQuestion.questionEn,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currentQuestion.questionTa,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Options
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val row1 = currentQuestion.options.take(2)
                val row2 = currentQuestion.options.drop(2)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row1.forEach { option ->
                        Button(
                            onClick = {
                                totalQuestions++
                                if (option == currentQuestion.correctAnswer) {
                                    score += 10
                                    correctCount++
                                }
                                currentQuestion = MentalMathDataSource.generateDynamicQuestion(MathOperation.ALL, selectedDifficulty)
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                        ) {
                            Text(text = "$option", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row2.forEach { option ->
                        Button(
                            onClick = {
                                totalQuestions++
                                if (option == currentQuestion.correctAnswer) {
                                    score += 10
                                    correctCount++
                                }
                                currentQuestion = MentalMathDataSource.generateDynamicQuestion(MathOperation.ALL, selectedDifficulty)
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                        ) {
                            Text(text = "$option", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // Results Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🏆", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Quiz Completed! மிக அருமை!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Score: $score Points • $correctCount / $totalQuestions Correct",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "சுபி, நீங்கள் சிறப்பாகச் செய்தீர்கள்! தனம் டீச்சர் உங்களை வாழ்த்துகிறார்! 🌟",
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = { isRunning = true },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Play Again • மீண்டும் விளையாடு")
                    }
                }
            }
        }
    }
}

@Composable
fun GlossaryTabContent(viewModel: MainViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "📚 Essential Math English Vocabulary (கணித ஆங்கிலச் சொற்கள்):",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "ஒவ்வொரு சொல்லையும் தட்டி உச்சரிப்பையும் ஆங்கில விளக்கத்தையும் கேட்டுப் பழகுங்கள்.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(MentalMathDataSource.mathGlossary) { item ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = item.english,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "• ${item.tamil}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "🇬🇧 \"${item.exampleEn}\"",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                        )
                        Text(
                            text = "🇮🇳 \"${item.exampleTa}\"",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = {
                            viewModel.voiceManager.speakBilingual(
                                englishText = "${item.english}. ${item.exampleEn}",
                                tamilText = "${item.tamil}. ${item.exampleTa}"
                            )
                        },
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Speak ${item.english}",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
