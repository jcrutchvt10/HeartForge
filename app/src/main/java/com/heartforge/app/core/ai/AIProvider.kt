package com.heartforge.app.core.ai

import kotlinx.coroutines.flow.Flow

interface AIProvider {
    suspend fun chat(
        messages: List<AIMessage>,
        temperature: Float? = null,
        stream: Boolean = false
    ): Flow<AIResult>

    suspend fun generateImage(
        prompt: String,
        negativePrompt: String? = null
    ): ImageResult
}

data class AIMessage(
    val role: String,
    val content: String
)

sealed class AIResult {
    data class Success(val content: String) : AIResult()
    data class Error(val message: String) : AIResult()
    data class Loading(val partialContent: String = "") : AIResult()
}

sealed class ImageResult {
    data class Success(val base64: String) : ImageResult()
    data class Error(val message: String) : ImageResult()
}
