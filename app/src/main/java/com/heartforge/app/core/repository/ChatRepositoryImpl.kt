package com.heartforge.app.core.repository

import com.heartforge.app.core.ai.*
import com.heartforge.app.core.database.MessageDao
import com.heartforge.app.core.database.toExternal
import com.heartforge.app.core.database.toEntity
import com.heartforge.app.core.model.*
import com.heartforge.app.core.util.DataInitializer
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton
import java.util.UUID

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao,
    private val aiProvider: AIProvider,
    private val promptEngine: PromptEngine,
    private val evolutionaryEngine: EvolutionaryEngine,
    private val characterRepository: CharacterRepository,
    private val relationshipRepository: RelationshipRepository,
    private val dataInitializer: DataInitializer,
    private val memoryDao: com.heartforge.app.core.database.MemoryDao
) : ChatRepository {

    override fun getMessages(characterId: String): Flow<List<ChatMessage>> {
        return messageDao.getMessages(characterId).map { list -> list.map { it.toExternal() } }
    }

    override suspend fun sendMessage(characterId: String, content: String) {
        val character = characterRepository.getCharacter(characterId) ?: return
        val userProfile = dataInitializer.getMockUserProfile()
        val relationship = relationshipRepository.getRelationship(characterId).first() 
            ?: Relationship(characterId, 50, 20, 50, 30, 0, 10, 10, 20, 10)

        // 1. Save User Message
        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            characterId = characterId,
            role = MessageRole.User,
            content = content
        )
        messageDao.insertMessage(userMessage.toEntity())

        // 2. Prepare Context
        val recentHistory = messageDao.getRecentMessages(characterId, 10)
            .map { AIMessage(it.role.name.lowercase(), it.content) }
            .reversed()
        
        val relevantMemories = memoryDao.getRelevantMemories(characterId, 1).map { it.toExternal() }

        val aiPrompt = promptEngine.buildPrompt(
            character = character,
            userProfile = userProfile,
            relationship = relationship,
            relevantMemories = relevantMemories,
            conversationSummary = null,
            recentMessages = recentHistory,
            currentUserMessage = content
        )

        // 3. Call AI
        // For M3, we just get the first result. Streaming UI will be handled in ViewModel.
        aiProvider.chat(aiPrompt).collect { result ->
            if (result is AIResult.Success) {
                val assistantMessage = ChatMessage(
                    id = UUID.randomUUID().toString(),
                    characterId = characterId,
                    role = MessageRole.Assistant,
                    content = result.content
                )
                messageDao.insertMessage(assistantMessage.toEntity())
                
                // 4. Evolve Relationship
                evolutionaryEngine.evolveRelationship(character, content, result.content)
            }
        }
    }

    override suspend fun clearHistory(characterId: String) {
        messageDao.deleteMessagesForCharacter(characterId)
    }
}
