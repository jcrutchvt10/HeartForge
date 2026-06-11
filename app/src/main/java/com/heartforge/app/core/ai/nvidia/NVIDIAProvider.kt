package com.heartforge.app.core.ai.nvidia

import android.util.Log
import com.heartforge.app.core.ai.*
import com.heartforge.app.core.network.nvidia.*
import com.heartforge.app.core.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NVIDIAProvider @Inject constructor(
    private val apiService: NVIDIAApiService,
    private val imageApiService: NVIDIAImageApiService,
    private val settingsRepository: SettingsRepository
) : AIProvider {

    private val TAG = "NVIDIAProvider"

    private suspend fun <T> retryCall(
        attempts: Int = 3,
        block: suspend () -> retrofit2.Response<T>
    ): retrofit2.Response<T> {
        var lastError: Exception? = null
        repeat(attempts) { attempt ->
            try {
                val resp = block()
                if (resp.isSuccessful) return resp
                Log.w(TAG, "Attempt ${attempt + 1} failed with code ${resp.code()}")
            } catch (e: Exception) {
                lastError = e
                Log.w(TAG, "Attempt ${attempt + 1} threw exception: ${e.message}")
            }
            kotlinx.coroutines.delay(500L * (attempt + 1))
        }
        throw lastError ?: IllegalStateException("Request failed after $attempts attempts")
    }

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
                val response = retryCall { apiService.createChatCompletionStream("Bearer $apiKey", request) }
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
                val response = retryCall { apiService.createChatCompletion("Bearer $apiKey", request) }
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
        
        // Use FLUX.1-dev with base mode (txt2img)
        val request = FluxImageRequest(
            prompt = prompt,
            mode = "base",
            cfg_scale = 5,
            steps = 25  // FLUX.1-dev default steps
        )

        return try {
            val response = imageApiService.generateFluxDev("Bearer $apiKey", request)
            if (response.isSuccessful) {
                val artifact = response.body()?.artifacts?.firstOrNull()
                val b64 = artifact?.base64
                val fr = artifact?.finishReason
                if (b64 != null && fr != "CONTENT_FILTERED") {
                    ImageResult.Success(b64)
                } else if (fr == "CONTENT_FILTERED") {
                    ImageResult.Error("Content filtered by NSFW safety checker")
                } else {
                    ImageResult.Error("No image generated")
                }
            } else {
                val err = response.errorBody()?.string()
                ImageResult.Error("FLUX.1-dev: ${response.code()} - ${err?.take(200)}")
            }
        } catch (e: Exception) {
            ImageResult.Error("FLUX.1-dev failed: ${e.message}")
        }
    }

    override suspend fun generateImg2Img(
        prompt: String,
        referenceImage: String,
        strength: Float,
        negativePrompt: String?
    ): ImageResult {
        val apiKey = settingsRepository.getApiKey() ?: return ImageResult.Error("API Key not configured")

        Log.d(TAG, "Attempting img2img with FLUX.1-dev depth mode, reference image length: ${referenceImage.length}")

        // Use FLUX.1-dev with depth mode directly - reference image is passed as base64 in the image field
        val request = FluxImageRequest(
            prompt = prompt,
            mode = "depth",
            cfg_scale = 5,
            steps = 30,
            image = referenceImage  // Pass base64 directly, no asset upload needed
        )

        try {
            val response = imageApiService.generateFluxDev("Bearer $apiKey", request)
            if (response.isSuccessful) {
                val artifact = response.body()?.artifacts?.firstOrNull()
                val b64 = artifact?.base64
                val fr = artifact?.finishReason
                if (b64 != null && fr != "CONTENT_FILTERED") {
                    Log.d(TAG, "img2img SUCCESS with FLUX.1-dev depth, generated ${b64.length} chars")
                    return ImageResult.Success(b64)
                } else if (fr == "CONTENT_FILTERED") {
                    Log.w(TAG, "img2img: Content filtered by NSFW safety checker")
                    return ImageResult.Error("Content filtered by NSFW safety checker")
                } else {
                    Log.w(TAG, "img2img: No image generated, finishReason=$fr")
                    return ImageResult.Error("No image generated")
                }
            } else {
                val err = response.errorBody()?.string()
                Log.e(TAG, "img2img: FLUX.1-dev depth failed: ${response.code()} - ${err?.take(200)}")
                return ImageResult.Error("FLUX.1-dev depth: ${response.code()} - ${err?.take(200)}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "img2img: FLUX.1-dev depth failed: ${e.message}")
            return ImageResult.Error("FLUX.1-dev depth failed: ${e.message}")
        }
    }
}