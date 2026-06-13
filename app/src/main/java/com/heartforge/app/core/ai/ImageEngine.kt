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
    private val promptCache = mutableMapOf<String, String>()

    private val fallbackPortraits = listOf(
        "https://images.unsplash.com/photo-1512485694743-9c9538b4e6e0", 
        "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d", 
        "https://images.unsplash.com/photo-1534030347209-467a5b0ad3e6", 
        "https://images.unsplash.com/photo-1500648767791-00dcc994a43e", 
        "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7", 
        "https://images.unsplash.com/photo-1501196354995-cbb51c65aaea", 
        "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d", 
        "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e"
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

    suspend fun generateContextualImage(
        character: Character,
        sceneType: SceneType,
        relationship: com.heartforge.app.core.model.Relationship? = null,
        customContext: String? = null,
        diversityIndex: Int? = null
    ): ImageResult {
        val cacheKey = "${character.id}_${sceneType}_" + (customContext?.hashCode() ?: 0)
        promptCache[cacheKey]?.let { return ImageResult.Success(it) }

        val prompt = buildPrompt(character, sceneType, relationship, customContext)
        val negativePrompt = "woman, female, girl, lady, old man, elderly, wrinkles, grey hair, glasses, blurry, low quality, distorted, extra limbs, bad anatomy, text, watermark, lipstick, makeup, breasts, female clothing"
        
        val result = aiProvider.generateImage(prompt, negativePrompt, character.id.hashCode(), diversityIndex)
        
        return if (result is ImageResult.Success) {
            val localPath = imageStorage.saveBase64Image(result.base64)
            if (localPath != null) {
                promptCache[cacheKey] = localPath
                ImageResult.Success(localPath)
            } else getFallback(sceneType)
        } else getFallback(sceneType)
    }

    suspend fun generateContextualImg2Img(
        character: Character,
        sceneType: SceneType,
        referenceImagePath: String,
        relationship: com.heartforge.app.core.model.Relationship? = null,
        strength: Float = 0.5f,
        customContext: String? = null
    ): ImageResult {
        return generateContextualImage(character, sceneType, relationship, customContext)
    }

    fun getFallbackImageUrl(sceneType: SceneType? = null): String {
        return if (sceneType == null || sceneType == SceneType.Portrait) {
            val url = fallbackPortraits[portraitIndex % fallbackPortraits.size]
            portraitIndex++
            "$url?w=600&h=800&fit=crop"
        } else {
            val base = fallbackForScene[sceneType] ?: fallbackPortraits[0]
            "$base?w=600&h=800&fit=crop"
        }
    }

    private fun getFallback(sceneType: SceneType): ImageResult {
        return ImageResult.Error("Using fallback: ${getFallbackImageUrl(sceneType)}")
    }

    private fun buildPrompt(
        character: Character,
        sceneType: SceneType,
        relationship: com.heartforge.app.core.model.Relationship?,
        customContext: String?
    ): String {
        val age = character.age.coerceIn(18, 27)
        val appearance = character.appearance
        
        val baseDescription = "A youthful, extremely handsome 8k RAW photo of a $age year old MALE named ${character.name}. " +
            "He is a MAN with masculine features, ${appearance.hairStyle} hair, and ${appearance.eyeColor} eyes. " +
            "Style: Youthful glow, smooth skin, boyish charm, high-end Gen-Z photography aesthetic."

        val sceneDescription = when (sceneType) {
            SceneType.Portrait -> "A close-up male professional headshot, sharp jawline, intense gaze."
            SceneType.Casual -> "Relaxed male pose, youthful energy, wearing ${appearance.clothingStyle}."
            SceneType.Gym -> "Male fitness photo, athletic youthful build, defined muscles."
            SceneType.Beach -> "Male on a beach, shirtless, energetic vibe."
            SceneType.Formal -> "Young man in a tailored suit, modern style."
            SceneType.Sleepwear -> "In a bedroom, intimate but youthful atmosphere."
            SceneType.Vacation -> "Young man on vacation, relaxed posture."
            SceneType.Selfie -> "A clean male selfie, social media style."
        }

        return "$baseDescription $sceneDescription ${customContext ?: ""} masterpiece, professional lighting."
    }
}
