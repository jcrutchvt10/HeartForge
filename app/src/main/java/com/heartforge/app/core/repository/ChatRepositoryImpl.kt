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
import kotlinx.coroutines.withContext
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
    private val imageEngine: com.heartforge.app.core.ai.ImageEngine,
    private val userProfileRepository: com.heartforge.app.core.repository.UserProfileRepository,
    private val notificationHelper: com.heartforge.app.core.util.NotificationHelper,
    private val foregroundState: com.heartforge.app.core.util.AppForegroundState
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
        val userProfile = userProfileRepository.getProfile()
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
            scope.launch {
                val sceneDescription = generateSceneDescription(character, userProfile, relationship, content)
                val imageResult = imageEngine.generateContextualImage(
                    character = character,
                    sceneType = SceneType.Selfie,
                    relationship = relationship,
                    customContext = sceneDescription
                )
                
                if (imageResult is ImageResult.Success) {
                    val photoMessage = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        characterId = characterId,
                        role = MessageRole.Assistant,
                        content = "Just for you... $sceneDescription 😉",
                        imageUrl = imageResult.base64
                    )
                    messageDao.insertMessage(photoMessage.toEntity())
                } else {
                    val fallbackMsg = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        characterId = characterId,
                        role = MessageRole.Assistant,
                        content = "I'd love to send you a photo, but my camera's acting up right now. Tell me what you'd like me to describe instead. 😏"
                    )
                    messageDao.insertMessage(fallbackMsg.toEntity())
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

        // 3. Call AI (on IO dispatcher to avoid blocking Main thread)
        withContext(Dispatchers.IO) {
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
                        
                        // Fire local notification if app is in background
                        if (!foregroundState.isInForeground) {
                            notificationHelper.showMessageNotification(
                                characterId = characterId,
                                characterName = character.name,
                                preview = result.content.take(120)
                            )
                        }
                        
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
    }

    override suspend fun sendImage(characterId: String, base64: String) {
        val character = characterRepository.getCharacter(characterId) ?: return
        
        // 1. Save User's Photo Message
        val userPhotoMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            characterId = characterId,
            role = MessageRole.User,
            content = "[Sent a photo]",
            imageUrl = base64 
        )
        messageDao.insertMessage(userPhotoMessage.toEntity())

        // 2. Analyze with Vision AI
        val visionResult = aiProvider.analyzeImage(base64)
        if (visionResult is AIResult.Success) {
            val context = "The user just sent you a photo. Here is what you see: ${visionResult.content}. React to it in character."
            
            // 3. Generate Character Reaction
            val aiPrompt = promptEngine.buildPrompt(
                character = character,
                userProfile = userProfileRepository.getProfile(),
                relationship = relationshipRepository.getRelationship(characterId).first() ?: Relationship(characterId, 50, 20, 50, 30, 0, 10, 10, 20, 10),
                relevantMemories = emptyList(),
                conversationSummary = context,
                recentMessages = emptyList(),
                currentUserMessage = "Check out this photo I just took."
            )

            aiProvider.chat(aiPrompt, stream = false).collect { result ->
                if (result is AIResult.Success) {
                    val reactionMessage = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        characterId = characterId,
                        role = MessageRole.Assistant,
                        content = result.content
                    )
                    messageDao.insertMessage(reactionMessage.toEntity())
                }
            }
        }
    }

    override suspend fun clearHistory(characterId: String) {
        messageDao.deleteMessagesForCharacter(characterId)
    }

    private suspend fun generateSceneDescription(
        character: Character,
        userProfile: UserProfile,
        relationship: Relationship,
        request: String
    ): String {
        val prompt = """
            The user (${userProfile.nickname}) is asking for a photo from their AI boyfriend (${character.name}).
            Request: "$request"
            Relationship: Trust ${relationship.trust}%, Romance ${relationship.romance}%, Intimacy ${relationship.intimacy}%
            
            Describe a single, highly detailed, and seductive visual scene for an AI image generator.
            Focus on the character's pose, clothing (or lack thereof), lighting, and mood.
            The description should match the current intimacy level. 
            Keep it under 60 words.
            
            Return ONLY the description.
        """.trimIndent()

        val messages = listOf(AIMessage("system", "You are a master of visual storytelling and erotic photography descriptions."), AIMessage("user", prompt))
        
        var description = "A seductive selfie showing his masculine features."
        aiProvider.chat(messages, stream = false).collect { result ->
            if (result is AIResult.Success) {
                description = result.content.trim()
            }
        }
        return description
    }

    private fun isPhotoRequest(content: String): Boolean {
        // Expanded detection using a regex to capture common photo‑related intents.
        val pattern = Regex("\\b(photo|picture|selfie|image|pic|snapshot|snapshot|snapshot|photograph|show|see)\\b", RegexOption.IGNORE_CASE)
        return pattern.containsMatchIn(content)
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
