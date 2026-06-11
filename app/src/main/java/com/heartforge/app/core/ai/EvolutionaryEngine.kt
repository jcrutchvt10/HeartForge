package com.heartforge.app.core.ai

import com.heartforge.app.core.model.*
import com.heartforge.app.core.repository.RelationshipRepository
import com.heartforge.app.core.repository.MemoryRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton
import java.util.UUID

@Singleton
class EvolutionaryEngine @Inject constructor(
    private val aiProvider: AIProvider,
    private val relationshipRepository: RelationshipRepository,
    private val memoryRepository: MemoryRepository
) {
    private val gson = Gson()

    /**
     * Analyzes a recent exchange and updates the relationship stats using AI sentiment evaluation.
     */
    suspend fun evolveRelationship(
        character: Character,
        userMessage: String,
        aiResponse: String
    ) {
        val currentRelationship = relationshipRepository.getRelationship(character.id).first() 
            ?: Relationship(character.id, 50, 20, 50, 30, 0, 10, 10, 20, 10)
        
        val analysisPrompt = """
            Analyze the following exchange between a user and their AI partner (${character.name}).
            Current Mood: ${currentRelationship.mood}
            User: "$userMessage"
            AI: "$aiResponse"
            
            Based on this, suggest adjustments to these stats (integer range -5 to +5):
            - trust, romance, comfort, affection, jealousy, loyalty, intimacy, playfulness, excitement
            
            Also:
            1. Suggest a new Mood string (one word).
            2. Identify any new 'Inside Jokes', 'Shared Activities', or 'Future Plans' mentioned.
            3. Decide if a new long-term memory should be formed.
            
            Return ONLY a valid JSON object in this format:
            {
              "deltas": {"trust": 1, "romance": 0, "comfort": 1, "affection": 2, "jealousy": 0, "loyalty": 0, "intimacy": 0, "playfulness": 0, "excitement": 0},
              "mood": "Happy",
              "newJoke": null,
              "newActivity": "Drinking coffee",
              "newPlan": "Visit Paris",
              "memory": "User likes dark roast coffee."
            }
        """.trimIndent()

        aiProvider.chat(listOf(AIMessage("system", analysisPrompt)), stream = false).collect { result ->
            if (result is AIResult.Success) {
                try {
                    val analysis = gson.fromJson(result.content, RelationshipAnalysis::class.java)
                    applyAnalysis(character.id, currentRelationship, analysis)
                } catch (e: Exception) {
                    // Fallback to basic heuristics if JSON parsing fails
                    applyFallbackEvolution(character.id, currentRelationship, userMessage, aiResponse)
                }
            }
        }
    }

    private suspend fun applyAnalysis(
        characterId: String,
        current: Relationship,
        analysis: RelationshipAnalysis
    ) {
        val updated = current.copy(
            trust = (current.trust + analysis.deltas.trust).coerceIn(0, 100),
            romance = (current.romance + analysis.deltas.romance).coerceIn(0, 100),
            comfort = (current.comfort + analysis.deltas.comfort).coerceIn(0, 100),
            affection = (current.affection + analysis.deltas.affection).coerceIn(0, 100),
            jealousy = (current.jealousy + analysis.deltas.jealousy).coerceIn(0, 100),
            loyalty = (current.loyalty + analysis.deltas.loyalty).coerceIn(0, 100),
            intimacy = (current.intimacy + analysis.deltas.intimacy).coerceIn(0, 100),
            playfulness = (current.playfulness + analysis.deltas.playfulness).coerceIn(0, 100),
            excitement = (current.excitement + analysis.deltas.excitement).coerceIn(0, 100),
            mood = analysis.mood ?: current.mood,
            insideJokes = (current.insideJokes + listOfNotNull(analysis.newJoke)).distinct(),
            sharedActivities = (current.sharedActivities + listOfNotNull(analysis.newActivity)).distinct(),
            futurePlans = (current.futurePlans + listOfNotNull(analysis.newPlan)).distinct()
        )
        
        relationshipRepository.updateRelationship(updated)

        analysis.memory?.let { content ->
            memoryRepository.remember(
                Memory(
                    id = UUID.randomUUID().toString(),
                    characterId = characterId,
                    content = content,
                    importance = 3,
                    category = MemoryCategory.Relationship,
                    sentiment = Sentiment.Positive
                )
            )
        }
    }

    private suspend fun applyFallbackEvolution(
        characterId: String,
        current: Relationship,
        userMsg: String,
        aiRes: String
    ) {
        val deltaAffection = if (aiRes.contains("❤️")) 2 else 1
        relationshipRepository.updateRelationship(
            current.copy(affection = (current.affection + deltaAffection).coerceIn(0, 100))
        )
    }

    data class RelationshipAnalysis(
        val deltas: StatDeltas,
        val mood: String?,
        val newJoke: String?,
        val newActivity: String?,
        val newPlan: String?,
        val memory: String?
    )

    data class StatDeltas(
        val trust: Int = 0,
        val romance: Int = 0,
        val comfort: Int = 0,
        val affection: Int = 0,
        val jealousy: Int = 0,
        val loyalty: Int = 0,
        val intimacy: Int = 0,
        val playfulness: Int = 0,
        val excitement: Int = 0
    )
}
