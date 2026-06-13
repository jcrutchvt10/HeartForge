package com.heartforge.app.core.repository

import com.heartforge.app.core.ai.AIProvider
import com.heartforge.app.core.ai.AIResult
import com.heartforge.app.core.ai.AIMessage
import com.heartforge.app.core.database.MemoryDao
import com.heartforge.app.core.database.toEntity
import com.heartforge.app.core.database.toExternal
import com.heartforge.app.core.database.toDomain
import com.heartforge.app.core.model.LoveLetter
import com.heartforge.app.core.model.Memory
import com.heartforge.app.core.model.MemoryCategory
import com.heartforge.app.core.model.Sentiment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoryRepositoryImpl @Inject constructor(
    private val memoryDao: MemoryDao,
    private val aiProvider: AIProvider,
    private val characterRepository: CharacterRepository,
    private val userProfileRepository: UserProfileRepository
) : MemoryRepository {

    override suspend fun remember(memory: Memory) {
        memoryDao.insertMemory(memory.toEntity())
    }

    override suspend fun forget(memoryId: String) {
        memoryDao.deleteMemory(memoryId)
    }

    override suspend fun editMemory(
        memoryId: String,
        content: String,
        importance: Int,
        category: MemoryCategory,
        sentiment: Sentiment
    ) {
        memoryDao.updateMemory(memoryId, content, importance, category, sentiment)
    }

    override suspend fun synthesizeLoveLetter(characterId: String): String {
        val character = characterRepository.getCharacter(characterId) ?: return "I'm thinking of you."
        val userProfile = userProfileRepository.getProfile()
        val highImportanceMemories = memoryDao.getRelevantMemories(characterId, 3).map { it.toExternal() }
        
        val memoriesText = if (highImportanceMemories.isEmpty()) {
            "our connection and the moments we've shared so far."
        } else {
            highImportanceMemories.joinToString("\n") { "- ${it.content}" }
        }

        val prompt = """
            You are ${character.name}, a ${character.occupation}.
            Personality: ${character.personality.traits.joinToString()}.
            User: ${userProfile.nickname}.
            
            Based on these shared memories and your deep connection:
            $memoriesText
            
            Write a raw, seductive, and deeply emotional love letter to the user.
            It should feel personal, referencing specific shared topics if possible.
            Keep it under 150 words. Use a mature, noir-inspired intimate tone.
            
            Return ONLY the letter text.
        """.trimIndent()

        val messages = listOf(AIMessage("system", "You are an intimate, seductive companion."), AIMessage("user", prompt))
        
        var letter = "I've been thinking about everything we've shared... you're always on my mind."
        aiProvider.chat(messages, stream = false).collect { result ->
            if (result is com.heartforge.app.core.ai.AIResult.Success) {
                letter = result.content.trim()
            }
        }
        return letter
    }

    override fun getLoveLetters(characterId: String): Flow<List<LoveLetter>> {
        return memoryDao.getLoveLetters(characterId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
