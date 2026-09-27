package com.example.data.repository

import com.example.data.db.AppDao
import com.example.data.db.ChatMessageEntity
import com.example.data.db.PracticeStatEntity
import com.example.data.db.SavedPhraseEntity
import kotlinx.coroutines.flow.Flow

class AppRepository(private val dao: AppDao) {
    val savedPhrases: Flow<List<SavedPhraseEntity>> = dao.getAllSavedPhrases()
    val chatMessages: Flow<List<ChatMessageEntity>> = dao.getAllChatMessages()
    val practiceStats: Flow<PracticeStatEntity?> = dao.getPracticeStats()

    suspend fun savePhrase(phrase: SavedPhraseEntity) = dao.insertSavedPhrase(phrase)
    suspend fun deletePhrase(id: Long) = dao.deleteSavedPhrase(id)
    suspend fun isPhraseSaved(englishText: String): Boolean = dao.isPhraseSaved(englishText)

    suspend fun addChatMessage(message: ChatMessageEntity) = dao.insertChatMessage(message)
    suspend fun clearChat() = dao.clearChatHistory()

    suspend fun updateStats(stats: PracticeStatEntity) = dao.updatePracticeStats(stats)
}
