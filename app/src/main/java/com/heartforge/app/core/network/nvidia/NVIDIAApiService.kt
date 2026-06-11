package com.heartforge.app.core.network.nvidia

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface NVIDIAApiService {

    @POST("chat/completions")
    suspend fun createChatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: ChatCompletionRequest
    ): Response<ChatCompletionResponse>

    @Streaming
    @POST("chat/completions")
    suspend fun createChatCompletionStream(
        @Header("Authorization") authorization: String,
        @Body request: ChatCompletionRequest
    ): Response<ResponseBody>

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
