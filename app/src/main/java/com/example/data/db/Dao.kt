package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM saved_phrases ORDER BY id DESC")
    fun getAllSavedPhrases(): Flow<List<SavedPhraseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedPhrase(phrase: SavedPhraseEntity): Long

    @Query("DELETE FROM saved_phrases WHERE id = :id")
    suspend fun deleteSavedPhrase(id: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_phrases WHERE englishText = :englishText LIMIT 1)")
    suspend fun isPhraseSaved(englishText: String): Boolean

    @Query("SELECT * FROM chat_messages ORDER BY id ASC")
    fun getAllChatMessages(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessageEntity): Long

    @Query("DELETE FROM chat_messages")
    suspend fun clearChatHistory()

    @Query("SELECT * FROM practice_stats WHERE id = 1")
    fun getPracticeStats(): Flow<PracticeStatEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePracticeStats(stats: PracticeStatEntity)
}
