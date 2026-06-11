package com.heartforge.app.core.ai.nvidia

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

    override suspend fun chat(
        messages: List<AIMessage>,
        temperature: Float?,
        stream: Boolean
    ): Flow<AIResult> = flow {
        emit(AIResult.Loading())
        
        val apiKey = settingsRepository.getApiKey()
        if (apiKey == null) {
            emit(AIResult.Error("API Key not configured. Please set it in Settings."))
            return@flow
        }

        val chatModel = settingsRepository.chatModel.first()
        val temp = temperature ?: settingsRepository.temperature.first()

        val request = ChatCompletionRequest(
            model = chatModel,
            messages = messages.map { ChatMessage(it.role, it.content) },
            temperature = temp,
            stream = stream
        )

        try {
            val response = apiService.createChatCompletion("Bearer $apiKey", request)
            if (response.isSuccessful) {
                val content = response.body()?.choices?.firstOrNull()?.message?.content ?: ""
                emit(AIResult.Success(content))
            } else {
                emit(AIResult.Error("API Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(AIResult.Error("Network Error: ${e.message}"))
        }
    }

    override suspend fun generateImage(
        prompt: String,
        negativePrompt: String?
    ): ImageResult {
        val apiKey = settingsRepository.getApiKey() ?: return ImageResult.Error("API Key not configured")
        val imageModel = runCatching { settingsRepository.imageModel.first() }.getOrDefault("nvidia/sdxl-turbo")

        val request = ImageGenerationRequest(
            model = imageModel,
            prompt = prompt,
            negativePrompt = negativePrompt
        )

        return try {
            val response = apiService.generateImage("Bearer $apiKey", request)
            if (response.isSuccessful) {
                val base64 = response.body()?.artifacts?.firstOrNull()?.base64
                if (base64 != null) ImageResult.Success(base64) else ImageResult.Error("No image generated")
            } else {
                ImageResult.Error("API Error: ${response.code()}")
            }
        } catch (e: Exception) {
            ImageResult.Error("Network Error: ${e.message}")
        }
    }
}
