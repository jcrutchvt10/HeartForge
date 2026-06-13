package com.heartforge.app.core.repository

import com.heartforge.app.core.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getMessages(characterId: String): Flow<List<ChatMessage>>
    
    suspend fun sendMessage(
        characterId: String,
        content: String,
        storyContext: String? = null
    )

    suspend fun sendImage(
        characterId: String,
        base64: String
    )

    suspend fun clearHistory(characterId: String)
}
