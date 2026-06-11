package com.heartforge.app.core.ai

import com.heartforge.app.core.model.*
import com.heartforge.app.core.repository.RelationshipRepository
import com.heartforge.app.core.repository.MemoryRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton
import java.util.UUID

@Singleton
class EvolutionaryEngine @Inject constructor(
    private val relationshipRepository: RelationshipRepository,
    private val memoryRepository: MemoryRepository
) {

    /**
     * Analyzes a recent exchange and updates the relationship stats.
     */
    suspend fun evolveRelationship(
        character: Character,
        userMessage: String,
        aiResponse: String
    ) {
        val currentRelationship = relationshipRepository.getRelationship(character.id).first() 
            ?: Relationship(character.id, 50, 20, 50, 30, 0, 10, 10, 20, 10)
        
        // Simple heuristic for M3
        val deltaTrust = if (userMessage.length > 30) 2 else 1
        val deltaRomance = if (aiResponse.contains("❤️") || aiResponse.contains("love")) 3 else 0
        val deltaAffection = 1

        val updatedRelationship = currentRelationship.copy(
            trust = (currentRelationship.trust + deltaTrust).coerceIn(0, 100),
            romance = (currentRelationship.romance + deltaRomance).coerceIn(0, 100),
            affection = (currentRelationship.affection + deltaAffection).coerceIn(0, 100)
        )
        
        relationshipRepository.updateRelationship(updatedRelationship)

        // Random memory creation for testing
        if (userMessage.contains("favorite", ignoreCase = true)) {
            memoryRepository.remember(
                Memory(
                    id = UUID.randomUUID().toString(),
                    characterId = character.id,
                    content = "User mentioned a favorite thing: $userMessage",
                    importance = 3,
                    category = MemoryCategory.Personal,
                    sentiment = Sentiment.Positive
                )
            )
        }
    }
}
