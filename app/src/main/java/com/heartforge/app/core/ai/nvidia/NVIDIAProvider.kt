package com.heartforge.app.core.ai.nvidia

import android.util.Log
import com.heartforge.app.core.ai.*
import com.heartforge.app.core.network.nvidia.*
import com.heartforge.app.core.repository.SettingsRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NVIDIAProvider @Inject constructor(
    private val apiService: NVIDIAApiService,
    private val settingsRepository: SettingsRepository
) : AIProvider {

    private val TAG = "NVIDIAProvider"

    override suspend fun chat(
        messages: List<AIMessage>,
        temperature: Float?,
        stream: Boolean
    ): Flow<AIResult> = flow {
        emit(AIResult.Loading())
        
        val apiKey = settingsRepository.getApiKey()
        if (apiKey.isNullOrBlank()) {
            Log.e(TAG, "Chat failed: API Key is missing")
            emit(AIResult.Error("API Key not configured. Please set it in Settings."))
            return@flow
        }

        val chatModel = settingsRepository.chatModel.first()
        val temp = temperature ?: settingsRepository.temperature.first()

        Log.d(TAG, "Sending chat request. Model: $chatModel, Temp: $temp, Messages: ${messages.size}")

        val request = ChatCompletionRequest(
            model = chatModel,
            messages = messages.map { ChatMessage(it.role, it.content) },
            temperature = temp,
            stream = stream
        )

        try {
            if (stream) {
                val response = apiService.createChatCompletionStream("Bearer $apiKey", request)
                if (response.isSuccessful) {
                    val reader = response.body()?.charStream()?.buffered()
                    var fullContent = ""
                    reader?.useLines { lines ->
                        lines.forEach { line ->
                            if (line.startsWith("data: ") && line != "data: [DONE]") {
                                try {
                                    val json = line.substring(6)
                                    val chunk = com.google.gson.Gson().fromJson(json, ChatCompletionChunk::class.java)
                                    val content = chunk.choices.firstOrNull()?.delta?.content ?: ""
                                    if (content.isNotEmpty()) {
                                        fullContent += content
                                        emit(AIResult.Loading(fullContent))
                                    }
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error parsing chunk: $line", e)
                                }
                            }
                        }
                    }
                    emit(AIResult.Success(fullContent))
                } else {
                    emit(AIResult.Error("API Error: ${response.code()}"))
                }
            } else {
                val response = apiService.createChatCompletion("Bearer $apiKey", request)
                if (response.isSuccessful) {
                    val content = response.body()?.choices?.firstOrNull()?.message?.content ?: ""
                    Log.d(TAG, "Chat success. Received content length: ${content.length}")
                    emit(AIResult.Success(content))
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "API Error: ${response.code()} - $errorBody")
                    emit(AIResult.Error("API Error: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Network Error: ${e.message}", e)
            emit(AIResult.Error("Network Error: ${e.message}"))
        }
    }

    override suspend fun generateImage(
        prompt: String,
        negativePrompt: String?
    ): ImageResult {
        val apiKey = settingsRepository.getApiKey() ?: return ImageResult.Error("API Key not configured")
        val imageModel = runCatching { settingsRepository.imageModel.first() }.getOrDefault("black-forest-labs/flux-1-dev")

        val request = ImageGenerationRequest(
            model = imageModel,
            prompt = prompt,
            negative_prompt = negativePrompt
        )

        return try {
            val response = apiService.generateImage("Bearer $apiKey", request)
            if (response.isSuccessful) {
                val base64 = response.body()?.data?.firstOrNull()?.b64_json
                if (base64 != null) ImageResult.Success(base64) else ImageResult.Error("No image generated")
            } else {
                ImageResult.Error("API Error: ${response.code()}")
            }
        } catch (e: Exception) {
            ImageResult.Error("Network Error: ${e.message}")
        }
    }
}
