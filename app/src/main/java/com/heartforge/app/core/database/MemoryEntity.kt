package com.heartforge.app.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.heartforge.app.core.model.Memory
import com.heartforge.app.core.model.MemoryCategory
import com.heartforge.app.core.model.Sentiment
import java.time.Instant

@Entity(tableName = "memories")
data class MemoryEntity(
    @PrimaryKey val id: String,
    val characterId: String,
    val content: String,
    val importance: Int,
    val category: MemoryCategory,
    val sentiment: Sentiment,
    val timestamp: Instant
)

fun MemoryEntity.toExternal() = Memory(
    id = id,
    characterId = characterId,
    content = content,
    importance = importance,
    category = category,
    sentiment = sentiment,
    timestamp = timestamp
)

fun Memory.toEntity() = MemoryEntity(
    id = id,
    characterId = characterId,
    content = content,
    importance = importance,
    category = category,
    sentiment = sentiment,
    timestamp = timestamp
)
