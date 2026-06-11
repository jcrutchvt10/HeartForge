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
        // Simple in‑memory cache: prompt hash -> saved image path.
        private val promptCache = mutableMapOf<String, String>()

    /**
     * Fallback Unsplash image IDs for when AI generation fails.
     * These are the same IDs used in DataInitializer for character portraits,
     * providing the same aesthetic: edgy, attractive Caucasian men 18-39.
     */
    private val fallbackPortraits = listOf(
        "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d",
        "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d",
        "https://images.unsplash.com/photo-1500648767791-00dcc994a43e",
        "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e",
        "https://images.unsplash.com/photo-1501196354995-cbb51c65aaea",
        "https://images.unsplash.com/photo-1504257432389-52343af06ae3",
        "https://images.unsplash.com/photo-1504593811423-6dd665756598",
        "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7",
        "https://images.unsplash.com/photo-1519058082700-08a0b56da9b4",
        "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce"
    )

    private val fallbackForScene = mapOf(
        SceneType.Casual to "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7",
        SceneType.Gym to "https://images.unsplash.com/photo-1534438327276-14e5300c3a48",
        SceneType.Beach to "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d",
        SceneType.Formal to "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d",
        SceneType.Sleepwear to "https://images.unsplash.com/photo-1500648767791-00dcc994a43e",
        SceneType.Vacation to "https://images.unsplash.com/photo-1501196354995-cbb51c65aaea",
        SceneType.Selfie to "https://images.unsplash.com/photo-1504257432389-52343af06ae3"
    )

    private var portraitIndex = 0

    /**
     * Generates a contextual image for a character.
     * Falls back to an Unsplash placeholder if AI generation fails.
     * @return The local file path of the generated image.
     */
    suspend fun generateContextualImage(
        character: Character,
        sceneType: SceneType,
        customContext: String? = null
    ): ImageResult {
        // Build a deterministic cache key based on character, scene and optional context.
        val cacheKey = "${character.id}_${sceneType}_" + (customContext?.hashCode() ?: 0)
        promptCache[cacheKey]?.let { cachedPath ->
            return ImageResult.Success(cachedPath)
        }

        val prompt = buildPrompt(character, sceneType, customContext)
        val negativePrompt = "blurry, low quality, distorted, extra limbs, bad anatomy, text, watermark"

        val result = aiProvider.generateImage(prompt, negativePrompt)
        
        return if (result is ImageResult.Success) {
            val localPath = imageStorage.saveBase64Image(result.base64)
            if (localPath != null) {
                // Cache the successful path for future requests.
                promptCache[cacheKey] = localPath
                ImageResult.Success(localPath)
            } else {
                getFallback(sceneType)
            }
        } else {
            getFallback(sceneType)
        }
    }

    /**
     * Returns a cached/fallback image URL when AI generation fails.
     * For Portrait scenes, cycles through the pool; for others, maps to the best fit.
     */
    fun getFallbackImageUrl(sceneType: SceneType? = null): String {
        return if (sceneType == null) {
            // Cycle through portrait collection
            val url = fallbackPortraits[portraitIndex % fallbackPortraits.size]
            portraitIndex++
            "$url?w=600&h=800&fit=crop"
        } else {
            val base = fallbackForScene[sceneType]
                ?: fallbackPortraits[portraitIndex++ % fallbackPortraits.size]
            "$base?w=600&h=800&fit=crop"
        }
    }

    private fun getFallback(sceneType: SceneType): ImageResult {
        val url = getFallbackImageUrl(sceneType)
        return ImageResult.Error("Using fallback image: $url")
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
