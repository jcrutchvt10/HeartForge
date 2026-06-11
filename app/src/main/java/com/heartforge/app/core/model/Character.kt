package com.heartforge.app.core.model

data class Character(
    val id: String,
    val name: String,
    val age: Int,
    val occupation: String,
    val location: String,
    val biography: String,
    val appearance: Appearance,
    val personality: Personality,
    val interests: List<Interest>,
    val hobbies: List<String>,
    val likes: List<String>,
    val dislikes: List<String>,
    val relationshipStyle: RelationshipStyle,
    val imageProfile: ImageProfile,
    val promptProfile: PromptProfile
)

data class Appearance(
    val height: String,
    val build: String,
    val eyeColor: String,
    val hairStyle: String,
    val clothingStyle: String,
    val distinguishingFeatures: List<String>
)

data class Personality(
    val traits: List<String>,
    val MBTI: String,
    val values: List<String>,
    val quirks: List<String>
)

data class Interest(
    val name: String,
    val category: String,
    val level: Int // 1-10
)

data class RelationshipStyle(
    val loveLanguages: List<String>,
    val attachmentStyle: String,
    val flirtingStyle: String,
    val preferences: List<String>,
    val relationshipGoals: List<String>
)

data class ImageProfile(
    val portraitId: String?,
    val casualId: String?,
    val gymId: String?,
    val beachId: String?,
    val formalId: String?,
    val sleepwearId: String?,
    val vacationId: String?,
    val selfieIds: List<String>,
    val styleDescription: String,
    val nsfwGallery: List<String>? = emptyList()
)

data class PromptProfile(
    val baseSystemPrompt: String,
    val customInstructions: String,
    val conversationTone: String,
    val speaks: String = "casual",
    val emojiLevel: String = "medium"
)
