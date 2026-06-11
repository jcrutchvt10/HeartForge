package com.heartforge.app.core.repository

import com.heartforge.app.core.ai.AIProvider
import com.heartforge.app.core.ai.AIResult
import com.heartforge.app.core.ai.PromptEngine
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val aiProvider: AIProvider,
    private val promptEngine: PromptEngine,
    private val characterRepository: CharacterRepository,
    private val memoryRepository: MemoryRepository,
    private val relationshipRepository: RelationshipRepository
) : ChatRepository {

    override suspend fun sendMessage(characterId: String, message: String) {
        // Implementation logic for M1: Just a placeholder or simple call
        // In M3 we will implement the full context building.
    }
}
