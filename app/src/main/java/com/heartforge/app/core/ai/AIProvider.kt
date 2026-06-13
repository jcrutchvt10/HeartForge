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
        negativePrompt: String? = null,
        seed: Int? = null,
        diversityIndex: Int? = null
    ): ImageResult

    suspend fun generateImg2Img(
        prompt: String,
        referenceImage: String,
        strength: Float = 0.6f,
        negativePrompt: String? = null
    ): ImageResult

    suspend fun analyzeImage(
        imageBase64: String,
        prompt: String = "Describe this image in detail, focusing on the person's appearance and mood."
    ): AIResult
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
