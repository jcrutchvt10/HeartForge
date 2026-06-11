package com.heartforge.app.core.model

import java.time.Instant

data class ChatMessage(
    val id: String,
    val characterId: String,
    val role: MessageRole,
    val content: String,
    val imageUrl: String? = null,
    val timestamp: Instant = Instant.now(),
    val status: MessageStatus = MessageStatus.Sent
)

enum class MessageRole {
    User,
    Assistant,
    System
}

enum class MessageStatus {
    Sending,
    Sent,
    Error,
    Read
}
