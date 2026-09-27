package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_phrases")
data class SavedPhraseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val englishText: String,
    val tamilText: String,
    val tanglishText: String = "",
    val category: String = "General",
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sender: String, // "user" or "bot"
    val englishText: String,
    val tamilText: String = "",
    val tanglishText: String = "",
    val coachingTip: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "practice_stats")
data class PracticeStatEntity(
    @PrimaryKey
    val id: Int = 1,
    val totalWordsSpoken: Int = 0,
    val completedLessons: Int = 0,
    val streakDays: Int = 1,
    val lastPracticeDate: Long = System.currentTimeMillis(),
    val averagePronunciationScore: Int = 85
)
