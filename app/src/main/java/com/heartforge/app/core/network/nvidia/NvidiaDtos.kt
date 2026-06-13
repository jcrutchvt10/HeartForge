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
    @SerializedName("finish_reason") val finishReason: String?
)

data class ChatUsage(
    @SerializedName("prompt_tokens") val promptTokens: Int,
    @SerializedName("completion_tokens") val completionTokens: Int,
    @SerializedName("total_tokens") val totalTokens: Int
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
    val image: String? = null,
    val strength: Float? = null,
    @SerializedName("safety_checker") val safetyChecker: Boolean? = null,
    @SerializedName("response_format") val responseFormat: String = "b64_json"
)

data class ImageGenerationResponse(
    val data: List<ImageData>
)

data class ImageData(
    val b64_json: String?,
    val url: String?
)

data class ChatCompletionChunk(
    val id: String,
    val choices: List<ChatChunkChoice>
)

data class ChatChunkChoice(
    val delta: ChatDelta,
    @SerializedName("finish_reason") val finishReason: String?
)

data class ChatDelta(
    val content: String?
)

data class FluxImageRequest(
    val prompt: String,
    val mode: String? = null,
    val cfg_scale: Int,
    val steps: Int,
    val image: String? = null,
    @SerializedName("preprocess_image") val preprocessImage: Boolean? = null,
    val seed: Int? = null
)

data class FluxImageResponse(
    val artifacts: List<FluxArtifact>
)

data class FluxArtifact(
    val base64: String,
    val finishReason: String
)
