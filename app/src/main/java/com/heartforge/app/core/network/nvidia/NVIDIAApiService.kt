package com.heartforge.app.core.network.nvidia

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface NVIDIAApiService {

    @POST("chat/completions")
    suspend fun createChatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: ChatCompletionRequest
    ): Response<ChatCompletionResponse>

    @POST("images/generations")
    suspend fun generateImage(
        @Header("Authorization") authorization: String,
        @Body request: ImageGenerationRequest
    ): Response<ImageGenerationResponse>

    @retrofit2.http.GET("models")
    suspend fun getModels(
        @Header("Authorization") authorization: String
    ): Response<ModelsResponse>
}

data class ModelsResponse(
    val data: List<ModelData>
)

data class ModelData(
    val id: String,
    @SerializedName("owned_by") val ownedBy: String
)
