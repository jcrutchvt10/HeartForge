package com.heartforge.app.feature.creator

import com.heartforge.app.core.model.*
import java.util.UUID

data class DraftCharacter(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val age: Int = 24,
    val occupation: String = "",
    val location: String = "",
    val biography: String = "",
    val appearance: Appearance = Appearance("", "", "", "", "", emptyList()),
    val personality: Personality = Personality(emptyList(), "ENFJ", emptyList(), emptyList()),
    val interests: List<Interest> = emptyList(),
    val hobbies: List<String> = emptyList(),
    val likes: List<String> = emptyList(),
    val dislikes: List<String> = emptyList(),
    val relationshipStyle: RelationshipStyle = RelationshipStyle(emptyList(), "Secure", "Friendly", emptyList(), emptyList()),
    val imageProfile: ImageProfile = ImageProfile(null, null, null, null, null, null, null, emptyList(), "Realistic portrait"),
    val promptProfile: PromptProfile = PromptProfile("", "", "Warm")
)

fun DraftCharacter.toCharacter() = Character(
    id = id,
    name = name,
    age = age,
    occupation = occupation,
    location = location,
    biography = biography,
    appearance = appearance,
    personality = personality,
    interests = interests,
    hobbies = hobbies,
    likes = likes,
    dislikes = dislikes,
    relationshipStyle = relationshipStyle,
    imageProfile = imageProfile,
    promptProfile = promptProfile
)
