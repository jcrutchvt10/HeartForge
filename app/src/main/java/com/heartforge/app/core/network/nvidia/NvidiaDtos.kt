package com.heartforge.app.core.network.nvidia

import com.google.gson.annotations.SerializedName

data class ChatCompletionRequest(
    val model: String,
    val messages: List<ChatMessage>,
    val temperature: Float = 0.7f,
    val topP: Float = 1.0f,
    val maxTokens: Int = 1024,
    val stream: Boolean = false
)

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatCompletionResponse(
    val id: String,
    val choices: List<ChatChoice>,
    val usage: ChatUsage
)

data class ChatChoice(
    val index: Int,
    val message: ChatMessage,
    val finishReason: String?
)

data class ChatUsage(
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int
)

data class ImageGenerationRequest(
    val model: String,
    val prompt: String,
    val negativePrompt: String? = null,
    val sampler: String? = null,
    val steps: Int = 30,
    val cfgScale: Float = 7.0f,
    val width: Int = 1024,
    val height: Int = 1024,
    val seed: Long? = null
)

data class ImageGenerationResponse(
    val artifacts: List<ImageArtifact>
)

data class ImageArtifact(
    val base64: String,
    val seed: Long,
    val finishReason: String
)
