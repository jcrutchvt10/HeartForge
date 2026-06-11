package com.heartforge.app.core.model

import java.time.Instant

data class Memory(
    val id: String,
    val characterId: String,
    val content: String,
    val importance: Int, // 1-5
    val category: MemoryCategory,
    val sentiment: Sentiment,
    val timestamp: Instant = Instant.now()
)

enum class MemoryCategory {
    Personal,
    Entertainment,
    Relationship,
    Activity,
    Fact,
    Unknown
}

enum class Sentiment {
    Positive,
    Negative,
    Neutral,
    Conflict,
    Deepening
}
