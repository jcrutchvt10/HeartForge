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
    val negative_prompt: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val steps: Int? = null,
    val cfg_scale: Float? = null,
    val seed: Int? = null,
    @SerializedName("response_format") val responseFormat: String = "b64_json"
)

data class ImageGenerationResponse(
    val data: List<ImageData>
)

data class ImageData(
    val b64_json: String?,
    val url: String?
)
