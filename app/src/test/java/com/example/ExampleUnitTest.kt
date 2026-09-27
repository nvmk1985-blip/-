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
}
