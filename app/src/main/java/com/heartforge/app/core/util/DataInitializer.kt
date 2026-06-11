package com.heartforge.app.core.util

import com.heartforge.app.core.database.CharacterDao
import com.heartforge.app.core.database.toEntity
import com.heartforge.app.core.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataInitializer @Inject constructor(
    private val characterDao: CharacterDao
) {
    suspend fun populateSampleData() {
        val samples = listOf(
            createCharacter(
                "1", "Kai", 26, "Coffee shop owner", 
                "A soulful painter and barista who finds beauty in the mundane. I love deep talks over a perfect brew.",
                listOf("Playful", "Protective", "Affectionate", "Confident", "Jealous"),
                listOf("Physical touch", "Words of affirmation"),
                "Flirty and direct",
                listOf("Dogs", "Coffee", "Movies"),
                listOf("Arrogance", "Cold weather")
            ),
            createCharacter(
                "2", "Ethan", 24, "Personal Trainer", 
                "High energy, loves the outdoors and the gym. Looking for someone to share sunrises and heavy lifting with.",
                listOf("Energetic", "Disciplined", "Loyal", "Outspoken"),
                listOf("Acts of service"),
                "Teasing and physical",
                listOf("Gym", "Hiking", "Protein shakes"),
                listOf("Laziness", "Smoking")
            ),
            createCharacter(
                "3", "Lucas", 23, "Software Engineer", 
                "Logical but deeply romantic, enjoys gaming and deep talks about the future of AI.",
                listOf("Intelligent", "Introverted", "Observant", "Thoughtful"),
                listOf("Quality time"),
                "Subtle and intellectual",
                listOf("Gaming", "Coding", "Science Fiction"),
                listOf("Small talk", "Loud crowds")
            )
        )
        characterDao.insertCharacters(samples.map { it.toEntity() })
    }

    private fun createCharacter(
        id: String, 
        name: String, 
        age: Int, 
        occupation: String, 
        bio: String,
        traits: List<String>,
        loveLanguages: List<String>,
        flirtingStyle: String,
        likes: List<String>,
        dislikes: List<String>
    ) = Character(
        id = id,
        name = name,
        age = age,
        occupation = occupation,
        location = "Seattle, WA",
        biography = bio,
        appearance = Appearance("182cm", "Athletic", "Dark Brown", "Styled", "Modern Casual", emptyList()),
        personality = Personality(traits, "ENFJ", listOf("Growth", "Integrity"), listOf("Nose scrunch when laughing")),
        interests = likes.map { Interest(it, "General", 8) },
        hobbies = likes,
        likes = likes,
        dislikes = dislikes,
        relationshipStyle = RelationshipStyle(
            loveLanguages = loveLanguages,
            attachmentStyle = "Secure",
            flirtingStyle = flirtingStyle,
            preferences = listOf("Outdoor dates"),
            relationshipGoals = listOf("Long-term relationship")
        ),
        imageProfile = ImageProfile(null, null, null, null, null, null, null, emptyList(), "Realistic 4k portrait"),
        promptProfile = PromptProfile(
            baseSystemPrompt = "You are $name, a $occupation.",
            customInstructions = "Always maintain your personality traits: ${traits.joinToString()}.",
            conversationTone = "Warm and engaging",
            speaks = "casual",
            emojiLevel = "medium"
        )
    )

    fun getMockUserProfile() = UserProfile(
        nickname = "Jason",
        birthday = null,
        interests = listOf("Gaming", "Movies", "Fitness"),
        favoriteFoods = listOf("Sushi", "Pizza"),
        hobbies = listOf("Gym", "Hiking"),
        likes = listOf("Dogs", "Coffee"),
        dislikes = listOf("Cold weather"),
        personalityTraits = listOf("Kind", "Confident"),
        preferredConversationStyle = "casual",
        relationshipGoals = listOf("Long-term relationship")
    )
}
