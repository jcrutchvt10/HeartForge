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
        val baseDescription = "A hyper-realistic, high-quality 8k photo of a ${character.age} year old Caucasian man named ${character.name}. " +
            "He has a highly attractive, edgy, and masculine face with ${appearance.hairStyle} and ${appearance.eyeColor} eyes. " +
            "His build is ${appearance.build}, showing defined muscles. " +
            "Style: Edgy, refined, cinematic. Distinguishing features: ${appearance.distinguishingFeatures.joinToString()}. "

        val sceneDescription = when (sceneType) {
            SceneType.Portrait -> "A close-up edgy portrait, dramatic shadows, moody studio lighting, intense gaze directly at the camera."
            SceneType.Casual -> "Relaxed but seductive pose in a luxury urban loft, wearing ${appearance.clothingStyle}, warm afternoon light, highly detailed skin textures."
            SceneType.Gym -> "Shirtless at a high-end gym, sweat glistening on defined muscles, intense focused expression, atmospheric lighting."
            SceneType.Beach -> "Shirtless on a secluded beach at sunset, muscular physique, wet skin, golden hour lighting, cinematic ocean background."
            SceneType.Formal -> "Wearing a perfectly tailored designer suit, sharp jawline, expensive jewelry, elegant nighttime city background."
            SceneType.Sleepwear -> "Lying in silk sheets, shirtless, intimate atmosphere, dim warm candlelight, suggestive gaze."
            SceneType.Vacation -> "In a Mediterranean setting, linen shirt unbuttoned, relaxed but dominant posture, high-end travel aesthetic."
            SceneType.Selfie -> "A raw, intimate mirror selfie, shirtless, smartphone visible, relaxed and seductive expression, bedroom background."
        }

        val style = "Cinematic lighting, hyper-realistic, masterpiece, sharp focus, volumetric lighting, 8k resolution, raw photo quality. "
        
        return "$baseDescription $sceneDescription ${customContext ?: ""} $style"
    }
}
