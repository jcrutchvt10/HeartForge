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

    private data class ModelConfig(
        val endpoint: String,
        val defaultCfg: Int = 5,
        val defaultSteps: Int = 25,
        val supportsMode: Boolean = true
    )

    private val generativeChain = listOf(
        ModelConfig("v1/genai/black-forest-labs/flux.1-dev"),
        ModelConfig("google/diffusiongemma-26b-a4b-it", defaultCfg = 7, defaultSteps = 30),
        ModelConfig("v1/genai/black-forest-labs/flux.2-klein-4b", defaultCfg = 1, defaultSteps = 4, supportsMode = false),
        ModelConfig("v1/genai/black-forest-labs/flux.1-schnell", defaultCfg = 0, defaultSteps = 4),
        ModelConfig("stabilityai/stable-diffusion-3-5-large", defaultCfg = 5, defaultSteps = 40)
    )

    private val editingChain = listOf(
        ModelConfig("v1/genai/black-forest-labs/flux.1-dev"), 
        ModelConfig("v1/genai/black-forest-labs/flux.1-kontext-dev"),
        ModelConfig("google/diffusiongemma-26b-a4b-it"),
        ModelConfig("qwen/qwen-image-edit")
    )

    override suspend fun generateImage(
        prompt: String,
        negativePrompt: String?,
        seed: Int?,
        diversityIndex: Int?
    ): ImageResult {
        val apiKey = settingsRepository.getApiKey() ?: return ImageResult.Error("API Key not configured")
        
        val chain = if (diversityIndex != null) {
            val idx = diversityIndex % generativeChain.size
            generativeChain.drop(idx) + generativeChain.take(idx)
        } else {
            generativeChain.shuffled()
        }
        
        for (config in chain) {
            Log.d(TAG, "Trying generative model: ${config.endpoint}")
            
            try {
                val response = if (config.endpoint.startsWith("v1/genai")) {
                    val request = FluxImageRequest(
                        prompt = prompt,
                        mode = if (config.supportsMode) "base" else null,
                        cfg_scale = config.defaultCfg,
                        steps = config.defaultSteps,
                        seed = seed
                    )
                    imageApiService.generateImage(config.endpoint, "Bearer $apiKey", request)
                } else {
                    // Use generic OpenAI-compatible endpoint on ai.api.nvidia.com
                    val request = ImageGenerationRequest(
                        model = config.endpoint,
                        prompt = prompt,
                        negative_prompt = negativePrompt,
                        steps = config.defaultSteps,
                        cfg_scale = config.defaultCfg.toFloat(),
                        seed = seed,
                        safetyChecker = false, // Uncensored mode if supported by NIM
                        responseFormat = "b64_json"
                    )
                    val genericUrl = "https://integrate.api.nvidia.com/v1/images/generations"
                    val resp = imageApiService.generateGenericImage(genericUrl, "Bearer $apiKey", request)
                    if (resp.isSuccessful) {
                        val b64 = resp.body()?.data?.firstOrNull()?.b64_json
                        if (b64 != null) {
                            // Convert to Flux-like response
                            retrofit2.Response.success(FluxImageResponse(listOf(FluxArtifact(b64, "SUCCESS"))))
                        } else {
                            retrofit2.Response.error(404, okhttp3.ResponseBody.create(null, "No image"))
                        }
                    } else {
                        retrofit2.Response.error(resp.code(), resp.errorBody() ?: okhttp3.ResponseBody.create(null, "Error"))
                    }
                }

                if (response.isSuccessful) {
                    val artifact = response.body()?.artifacts?.firstOrNull()
                    val b64 = artifact?.base64
                    val fr = artifact?.finishReason
                    
                    if (b64 != null && fr != "CONTENT_FILTERED") {
                        Log.d(TAG, "${config.endpoint} SUCCESS. Reason: $fr")
                        return ImageResult.Success(b64)
                    } else {
                        Log.w(TAG, "${config.endpoint} generation finished but was filtered or empty. Reason: $fr")
                    }
                } else {
                    val err = response.errorBody()?.string()
                    Log.w(TAG, "${config.endpoint} failed: ${response.code()} - $err")
                }
            } catch (e: Exception) {
                Log.w(TAG, "${config.endpoint} threw exception: ${e.message}")
            }
        }
        return ImageResult.Error("All generative models in chain failed or filtered content")
    }

    override suspend fun generateImg2Img(
        prompt: String,
        referenceImage: String,
        strength: Float,
        negativePrompt: String?
    ): ImageResult {
        val apiKey = settingsRepository.getApiKey() ?: return ImageResult.Error("API Key not configured")

        val formattedImage = if (!referenceImage.startsWith("data:")) {
            "data:image/jpeg;base64,$referenceImage"
        } else {
            referenceImage
        }

        for (config in editingChain) {
            Log.d(TAG, "Trying editing model: ${config.endpoint}")
            
            try {
                val response = if (config.endpoint.startsWith("v1/genai")) {
                    val request = FluxImageRequest(
                        prompt = prompt,
                        mode = if (!config.supportsMode) null 
                               else if (config.endpoint.contains("flux.1-dev")) "depth" 
                               else null,
                        cfg_scale = config.defaultCfg,
                        steps = config.defaultSteps,
                        image = formattedImage
                    )
                    imageApiService.generateImage(config.endpoint, "Bearer $apiKey", request)
                } else {
                    // Use generic OpenAI-compatible endpoint in apiService
                    val request = ImageGenerationRequest(
                        model = config.endpoint,
                        prompt = prompt,
                        negative_prompt = negativePrompt,
                        steps = config.defaultSteps,
                        cfg_scale = config.defaultCfg.toFloat(),
                        image = formattedImage,
                        responseFormat = "b64_json"
                    )
                    val genericUrl = "https://integrate.api.nvidia.com/v1/images/generations"
                    val resp = imageApiService.generateGenericImage(genericUrl, "Bearer $apiKey", request)
                    if (resp.isSuccessful) {
                        val b64 = resp.body()?.data?.firstOrNull()?.b64_json
                        if (b64 != null) {
                            retrofit2.Response.success(FluxImageResponse(listOf(FluxArtifact(b64, "SUCCESS"))))
                        } else {
                            retrofit2.Response.error(404, okhttp3.ResponseBody.create(null, "No image"))
                        }
                    } else {
                        retrofit2.Response.error(resp.code(), resp.errorBody() ?: okhttp3.ResponseBody.create(null, "Error"))
                    }
                }

                if (response.isSuccessful) {
                    val artifact = response.body()?.artifacts?.firstOrNull()
                    val b64 = artifact?.base64
                    val fr = artifact?.finishReason
                    
                    if (b64 != null && fr != "CONTENT_FILTERED") {
                        Log.d(TAG, "${config.endpoint} img2img SUCCESS. Reason: $fr")
                        return ImageResult.Success(b64)
                    } else {
                        Log.w(TAG, "${config.endpoint} img2img finished but was filtered or empty. Reason: $fr")
                    }
                } else {
                    val err = response.errorBody()?.string()
                    Log.w(TAG, "${config.endpoint} img2img failed: ${response.code()} - $err")
                }
            } catch (e: Exception) {
                Log.w(TAG, "${config.endpoint} img2img threw exception: ${e.message}")
            }
        }
        return ImageResult.Error("All editing models in chain failed")
    }

    override suspend fun analyzeImage(imageBase64: String, prompt: String): AIResult {
        val apiKey = settingsRepository.getApiKey() ?: return AIResult.Error("API Key not configured")
        
        // Ensure image is formatted
        val formattedImage = if (!imageBase64.startsWith("data:")) {
            "data:image/jpeg;base64,$imageBase64"
        } else {
            imageBase64
        }

        val request = ChatCompletionRequest(
            model = "qwen/qwen-image",
            messages = listOf(
                ChatMessage("user", content = "User uploaded image. $prompt $formattedImage")
            ),
            maxTokens = 512
        )

        return try {
            val response = apiService.createChatCompletion("Bearer $apiKey", request)
            if (response.isSuccessful) {
                val content = response.body()?.choices?.firstOrNull()?.message?.content ?: ""
                AIResult.Success(content)
            } else {
                AIResult.Error("Vision Error: ${response.code()}")
            }
        } catch (e: Exception) {
            AIResult.Error("Vision Exception: ${e.message}")
        }
    }
}
