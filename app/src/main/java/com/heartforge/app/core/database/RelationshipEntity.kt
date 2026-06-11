package com.heartforge.app.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.heartforge.app.core.model.Relationship

@Entity(tableName = "relationships")
data class RelationshipEntity(
    @PrimaryKey val characterId: String,
    val trust: Int,
    val romance: Int,
    val comfort: Int,
    val affection: Int,
    val jealousy: Int,
    val loyalty: Int,
    val intimacy: Int,
    val playfulness: Int,
    val excitement: Int,
    val mood: String,
    val insideJokes: List<String>,
    val sharedActivities: List<String>,
    val futurePlans: List<String>
)

fun RelationshipEntity.toExternal() = Relationship(
    characterId = characterId,
    trust = trust,
    romance = romance,
    comfort = comfort,
    affection = affection,
    jealousy = jealousy,
    loyalty = loyalty,
    intimacy = intimacy,
    playfulness = playfulness,
    excitement = excitement,
    mood = mood,
    insideJokes = insideJokes,
    sharedActivities = sharedActivities,
    futurePlans = futurePlans
)

fun Relationship.toEntity() = RelationshipEntity(
    characterId = characterId,
    trust = trust,
    romance = romance,
    comfort = comfort,
    affection = affection,
    jealousy = jealousy,
    loyalty = loyalty,
    intimacy = intimacy,
    playfulness = playfulness,
    excitement = excitement,
    mood = mood,
    insideJokes = insideJokes,
    sharedActivities = sharedActivities,
    futurePlans = futurePlans
)
