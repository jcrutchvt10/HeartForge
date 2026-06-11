package com.heartforge.app.core.ai

import com.google.gson.Gson
import com.heartforge.app.core.model.Character
import com.heartforge.app.core.model.UserProfile
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchmakingEngine @Inject constructor(
    private val aiProvider: AIProvider
) {
    private val gson = Gson()

    /**
     * Calculates compatibility score (0-100) based on basic overlapping traits.
     * Use this as a fast fallback or initial sort.
     */
    fun calculateHeuristicCompatibility(user: UserProfile, character: Character): Int {
        var score = 0.0
        
        val commonInterests = user.interests.intersect(character.interests.map { it.name }.toSet()).size
        val commonHobbies = user.hobbies.intersect(character.hobbies.toSet()).size
        val interestMatch = (commonInterests + commonHobbies).toDouble() / 
            (user.interests.size + user.hobbies.size).coerceAtLeast(1)
        score += interestMatch * 30

        val personalityMatch = user.personalityTraits.intersect(character.personality.traits.toSet()).size.toDouble() /
            user.personalityTraits.size.coerceAtLeast(1)
        score += personalityMatch * 25

        if (user.preferredConversationStyle.equals(character.promptProfile.speaks, ignoreCase = true)) {
            score += 20
        } else {
            score += 10
        }

        val goalMatch = user.relationshipGoals.intersect(character.relationshipStyle.relationshipGoals.toSet()).size.toDouble() /
            user.relationshipGoals.size.coerceAtLeast(1)
        score += goalMatch * 25

        return score.toInt().coerceIn(60, 99)
    }

    /**
     * Performs a deep AI-driven compatibility audit using NVIDIA NIM API.
     */
    suspend fun calculateAIDrivenCompatibility(user: UserProfile, character: Character): MatchmakingAudit {
        val prompt = """
            Perform a deep relationship compatibility audit between a user and an AI character.
            
            User Profile:
            - Interests: ${user.interests.joinToString()}
            - Hobbies: ${user.hobbies.joinToString()}
            - Personality: ${user.personalityTraits.joinToString()}
            - Goals: ${user.relationshipGoals.joinToString()}
            
            Character Profile:
            - Name: ${character.name}
            - Bio: ${character.biography}
            - Personality: ${character.personality.traits.joinToString()}
            - Goals: ${character.relationshipStyle.relationshipGoals.joinToString()}
            
            Analyze their synergy and return ONLY a valid JSON object:
            {
              "score": 94,
              "summary": "Short 1-sentence summary of why they match.",
              "strengths": ["Reason 1", "Reason 2"],
              "challenges": ["Potential friction point"]
            }
        """.trimIndent()

        val messages = listOf(AIMessage("system", "You are a relationship expert AI."), AIMessage("user", prompt))
        
        return try {
            val result = aiProvider.chat(messages, stream = false).first { it is AIResult.Success || it is AIResult.Error }
            if (result is AIResult.Success) {
                gson.fromJson(result.content, MatchmakingAudit::class.java)
            } else {
                MatchmakingAudit(calculateHeuristicCompatibility(user, character), "Heuristic match based on shared interests.")
            }
        } catch (e: Exception) {
            MatchmakingAudit(calculateHeuristicCompatibility(user, character), "Error performing deep audit.")
        }
    }

    data class MatchmakingAudit(
        val score: Int,
        val summary: String,
        val strengths: List<String> = emptyList(),
        val challenges: List<String> = emptyList()
    )
}
