package com.heartforge.app.core.ai

import com.heartforge.app.core.model.Character
import com.heartforge.app.core.util.ImageStorage
import javax.inject.Inject
import javax.inject.Singleton

enum class SceneType {
    Portrait,
    Casual,
    Gym,
    Beach,
    Formal,
    Sleepwear,
    Vacation,
    Selfie
}

@Singleton
class ImageEngine @Inject constructor(
    private val aiProvider: AIProvider,
    private val imageStorage: ImageStorage
) {
    /**
     * Generates a contextual image for a character.
     * @return The local file path of the generated image.
     */
    suspend fun generateContextualImage(
        character: Character,
        sceneType: SceneType,
        customContext: String? = null
    ): ImageResult {
        val prompt = buildPrompt(character, sceneType, customContext)
        val negativePrompt = "blurry, low quality, distorted, extra limbs, bad anatomy, text, watermark"

        val result = aiProvider.generateImage(prompt, negativePrompt)
        
        return if (result is ImageResult.Success) {
            val localPath = imageStorage.saveBase64Image(result.base64)
            if (localPath != null) {
                ImageResult.Success(localPath)
            } else {
                ImageResult.Error("Failed to save image locally")
            }
        } else {
            result
        }
    }

    private fun buildPrompt(
        character: Character,
        sceneType: SceneType,
        customContext: String?
    ): String {
        val appearance = character.appearance
        val baseDescription = "A highly realistic 4k photo of a ${character.age} year old man named ${character.name}. " +
            "He has ${appearance.hairStyle} ${appearance.eyeColor} hair and eyes. " +
            "His build is ${appearance.build}. " +
            "Distinguishing features: ${appearance.distinguishingFeatures.joinToString()}. "

        val sceneDescription = when (sceneType) {
            SceneType.Portrait -> "A professional headshot portrait, neutral background, soft studio lighting."
            SceneType.Casual -> "Relaxed pose in a modern living room, wearing ${appearance.clothingStyle}, natural daylight."
            SceneType.Gym -> "At the gym, sweaty, wearing athletic workout gear, lifting weights, intense focus."
            SceneType.Beach -> "At the beach, shirtless, sunny day, ocean in the background, blue sky."
            SceneType.Formal -> "Attending a gala, wearing a sharp tailored tuxedo, elegant indoor setting."
            SceneType.Sleepwear -> "In bed, wearing comfortable pajamas, cozy bedroom atmosphere, dim warm lighting."
            SceneType.Vacation -> "In a beautiful European city, holding a camera, tourist outfit, historical architecture background."
            SceneType.Selfie -> "A casual mirror selfie, smartphone visible, relaxed expression, bathroom or bedroom background."
        }

        val style = "Cinematic lighting, hyper-realistic, masterpiece, sharp focus, 8k resolution. "
        
        return "$baseDescription $sceneDescription ${customContext ?: ""} $style"
    }
}
