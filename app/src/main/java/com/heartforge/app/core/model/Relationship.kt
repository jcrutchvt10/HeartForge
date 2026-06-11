package com.heartforge.app.core.model

data class Relationship(
    val characterId: String,
    val trust: Int,
    val romance: Int,
    val comfort: Int,
    val affection: Int,
    val jealousy: Int,
    val loyalty: Int,
    val intimacy: Int,
    val playfulness: Int,
    val excitement: Int,
    val mood: String = "Neutral",
    val insideJokes: List<String> = emptyList(),
    val sharedActivities: List<String> = emptyList(),
    val futurePlans: List<String> = emptyList()
)
