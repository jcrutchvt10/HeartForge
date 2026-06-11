package com.heartforge.app.core.ai

import com.heartforge.app.core.model.Character
import com.heartforge.app.core.model.UserProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchmakingEngine @Inject constructor() {

    /**
     * Calculates compatibility score (0-100) based on:
     * - Interests & Hobbies
     * - Personality Traits
     * - Conversation Style
     * - Relationship Goals
     */
    fun calculateCompatibility(user: UserProfile, character: Character): Int {
        var score = 0.0
        
        // 1. Interests & Hobbies (30%)
        val commonInterests = user.interests.intersect(character.interests.map { it.name }.toSet()).size
        val commonHobbies = user.hobbies.intersect(character.hobbies.toSet()).size
        val interestMatch = (commonInterests + commonHobbies).toDouble() / 
            (user.interests.size + user.hobbies.size).coerceAtLeast(1)
        score += interestMatch * 30

        // 2. Personality Alignment (25%)
        val personalityMatch = user.personalityTraits.intersect(character.personality.traits.toSet()).size.toDouble() /
            user.personalityTraits.size.coerceAtLeast(1)
        score += personalityMatch * 25

        // 3. Conversation Style (20%)
        if (user.preferredConversationStyle.equals(character.promptProfile.speaks, ignoreCase = true)) {
            score += 20
        } else {
            score += 10 // Partial match for "near" styles
        }

        // 4. Relationship Goals (25%)
        val goalMatch = user.relationshipGoals.intersect(character.relationshipStyle.relationshipGoals.toSet()).size.toDouble() /
            user.relationshipGoals.size.coerceAtLeast(1)
        score += goalMatch * 25

        // Base minimum for visibility
        return score.toInt().coerceIn(60, 99)
    }
}
