package com.heartforge.app.core.repository

import android.util.Log
import com.heartforge.app.core.ai.*
import com.heartforge.app.core.database.MessageDao
import com.heartforge.app.core.database.toExternal
import com.heartforge.app.core.database.toEntity
import com.heartforge.app.core.model.*
import com.heartforge.app.core.util.DataInitializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
    private val memoryDao: com.heartforge.app.core.database.MemoryDao,
    private val imageEngine: com.heartforge.app.core.ai.ImageEngine
) : ChatRepository {

    private val TAG = "ChatRepositoryImpl"
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun getMessages(characterId: String): Flow<List<ChatMessage>> {
        return messageDao.getMessages(characterId).map { list -> list.map { it.toExternal() } }
    }

    override suspend fun sendMessage(characterId: String, content: String, storyContext: String?) {
        Log.d(TAG, "sendMessage: characterId=$characterId, content='$content'")
        val character = characterRepository.getCharacter(characterId) ?: run {
            Log.e(TAG, "sendMessage failed: Character not found")
            return
        }
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

        // 2. Detect Photo Intent
        if (isPhotoRequest(content)) {
            val scene = determineScene(content)
            scope.launch {
                val imageResult = imageEngine.generateContextualImage(character, scene)
                if (imageResult is ImageResult.Success) {
                    val photoMessage = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        characterId = characterId,
                        role = MessageRole.Assistant,
                        content = "Here's a photo for you! 😉",
                        imageUrl = imageResult.base64 // This is actually the local path saved by ImageEngine
                    )
                    messageDao.insertMessage(photoMessage.toEntity())
                }
            }
        }

        // 3. Prepare Context
        val recentHistory = messageDao.getRecentMessages(characterId, 10)
            .map { AIMessage(it.role.name.lowercase(), it.content) }
            .reversed()
        
        val relevantMemories = memoryDao.getRelevantMemories(characterId, 1).map { it.toExternal() }

        val aiPrompt = promptEngine.buildPrompt(
            character = character,
            userProfile = userProfile,
            relationship = relationship,
            relevantMemories = relevantMemories,
            conversationSummary = storyContext, // Inject story context here
            recentMessages = recentHistory,
            currentUserMessage = content
        )

        // 3. Call AI
        // For M3, we just get the first result. Streaming UI will be handled in ViewModel.
        aiProvider.chat(aiPrompt).collect { result ->
            when (result) {
                is AIResult.Success -> {
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
                is AIResult.Error -> {
                    val errorMessage = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        characterId = characterId,
                        role = MessageRole.System,
                        content = "Error: ${result.message}"
                    )
                    messageDao.insertMessage(errorMessage.toEntity())
                }
                else -> {}
            }
        }
    }

    override suspend fun clearHistory(characterId: String) {
        messageDao.deleteMessagesForCharacter(characterId)
    }

    private fun isPhotoRequest(content: String): Boolean {
        val keywords = listOf("photo", "picture", "selfie", "pic", "image", "see you")
        return keywords.any { content.contains(it, ignoreCase = true) }
    }

    private fun determineScene(content: String): com.heartforge.app.core.ai.SceneType {
        return when {
            content.contains("gym", ignoreCase = true) -> com.heartforge.app.core.ai.SceneType.Gym
            content.contains("beach", ignoreCase = true) -> com.heartforge.app.core.ai.SceneType.Beach
            content.contains("formal", ignoreCase = true) -> com.heartforge.app.core.ai.SceneType.Formal
            content.contains("sleep", ignoreCase = true) -> com.heartforge.app.core.ai.SceneType.Sleepwear
            content.contains("vacation", ignoreCase = true) -> com.heartforge.app.core.ai.SceneType.Vacation
            content.contains("selfie", ignoreCase = true) -> com.heartforge.app.core.ai.SceneType.Selfie
            else -> com.heartforge.app.core.ai.SceneType.Casual
        }
    }
}
