package com.heartforge.app.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.heartforge.app.core.model.*

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: String,
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

fun CharacterEntity.toExternal() = Character(
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

fun Character.toEntity() = CharacterEntity(
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
