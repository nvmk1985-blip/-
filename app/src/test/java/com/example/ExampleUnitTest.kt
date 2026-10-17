package com.example

import com.example.data.ai.TutorEngine
import com.example.voice.SttLanguage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testSttLanguageCodes() {
    assertEquals("en-IN", SttLanguage.ENGLISH.code)
    assertEquals("ta-IN", SttLanguage.TAMIL.code)
    assertEquals("English (India)", SttLanguage.ENGLISH.label)
    assertEquals("Tamil (India)", SttLanguage.TAMIL.label)
  }

  @Test
  fun testPronunciationScoring_exactMatch() {
    val score = TutorEngine.calculatePronunciationScore("How are you?", "How are you?")
    assertTrue(score >= 95)
  }

  @Test
  fun testPronunciationScoring_similarSound() {
    val score = TutorEngine.calculatePronunciationScore("Good morning", "good morning")
    assertTrue(score >= 90)
  }

  @Test
  fun testPronunciationScoring_lowMatch() {
    val score = TutorEngine.calculatePronunciationScore("I am going to school", "completely different banana")
    assertTrue(score < 50)
  }

  @Test
  fun testMentalMath_curatedQuestionsHaveBilingualSteps() {
    val questions = com.example.data.math.MentalMathDataSource.curatedQuestions
    assertTrue(questions.isNotEmpty())
    for (q in questions) {
      assertTrue(q.options.contains(q.correctAnswer))
      assertEquals(4, q.options.size)
      assertTrue(q.stepsEn.isNotEmpty())
      assertTrue(q.stepsTa.isNotEmpty())
      assertTrue(q.mentalTrickEn.isNotBlank())
      assertTrue(q.mentalTrickTa.isNotBlank())
    }
  }

  @Test
  fun testMentalMath_dynamicGeneration() {
    val addQ = com.example.data.math.MentalMathDataSource.generateDynamicQuestion(
      com.example.data.math.MathOperation.ADDITION,
      com.example.data.math.MathDifficulty.BEGINNER
    )
    assertTrue(addQ.options.contains(addQ.correctAnswer))
    assertTrue(addQ.questionEn.contains("+"))
    assertTrue(addQ.stepsEn.isNotEmpty())
    assertTrue(addQ.stepsTa.isNotEmpty())

    val mulQ = com.example.data.math.MentalMathDataSource.generateDynamicQuestion(
      com.example.data.math.MathOperation.MULTIPLICATION,
      com.example.data.math.MathDifficulty.INTERMEDIATE
    )
    assertTrue(mulQ.options.contains(mulQ.correctAnswer))
    assertTrue(mulQ.stepsEn.isNotEmpty())
  }

  @Test
  fun testFiveVoiceTones_allDefinedAndDistinct() {
    val tones = com.example.voice.VoiceTone.entries
    assertEquals(5, tones.size)
    assertEquals(
      listOf("teacher", "heroine_nila", "subi", "rj_ananya", "kavitha_akka"),
      tones.map { it.id }
    )
    for (tone in tones) {
      assertTrue(tone.pitch in 0.8f..1.4f)
      assertTrue(tone.speedRate in 0.7f..1.3f)
      assertTrue(tone.titleTa.isNotBlank())
      assertTrue(tone.sampleTextTa.isNotBlank())
      assertTrue(tone.sampleTextEn.isNotBlank())
    }
  }

  @Test
  fun testOneHundredMentalMathSecrets() {
    val secrets = com.example.data.math.MentalMathPdfGenerator.allSecrets
    assertEquals(100, secrets.size)
    secrets.forEachIndexed { index, secret ->
      assertEquals(index + 1, secret.number)
      assertTrue(secret.titleTa.isNotBlank())
      assertTrue(secret.titleEn.isNotBlank())
      assertTrue(secret.formulaTa.isNotBlank())
      assertTrue(secret.explanationTa.isNotBlank())
      assertTrue(secret.examples.isNotEmpty())
      assertTrue(secret.practiceQuestions.isNotEmpty())
    }
  }

  @Test
  fun testNaturalTamilSpeechOptimizer() {
    val raw = "15 + 28 = 43. AI App Gemini Flash"
    val optimized = com.example.voice.NaturalTamilSpeechOptimizer.optimizeForTamilSpeech(raw)
    assertTrue(optimized.contains("கூட்டல்"))
    assertTrue(optimized.contains("சமம்"))
    assertTrue(optimized.contains("ஏஐ"))
    assertTrue(optimized.contains("ஆப்"))
    assertTrue(optimized.contains("ஜெமினி"))
    assertTrue(optimized.contains("ஃபிளாஷ்"))
  }
}

