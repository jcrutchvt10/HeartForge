package com.heartforge.app.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.heartforge.app.core.model.ChatMessage
import com.heartforge.app.core.model.MessageRole
import com.heartforge.app.core.model.MessageStatus
import java.time.Instant

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val characterId: String,
    val role: MessageRole,
    val content: String,
    val imageUrl: String?,
    val timestamp: Instant,
    val status: MessageStatus
)

fun MessageEntity.toExternal() = ChatMessage(
    id = id,
    characterId = characterId,
    role = role,
    content = content,
    imageUrl = imageUrl,
    timestamp = timestamp,
    status = status
)

fun ChatMessage.toEntity() = MessageEntity(
    id = id,
    characterId = characterId,
    role = role,
    content = content,
    imageUrl = imageUrl,
    timestamp = timestamp,
    status = status
)
