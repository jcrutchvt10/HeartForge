package com.heartforge.app.core.repository

interface ChatRepository {
    suspend fun sendMessage(
        characterId: String,
        message: String
    )
}
