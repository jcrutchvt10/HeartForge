package com.heartforge.app.core.ai

import com.heartforge.app.core.model.Character
import com.heartforge.app.core.model.Memory
import com.heartforge.app.core.model.Relationship
import com.heartforge.app.core.model.UserProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PromptEngine @Inject constructor() {

    fun buildPrompt(
        character: Character,
        userProfile: UserProfile,
        relationship: Relationship,
        relevantMemories: List<Memory>,
        conversationSummary: String?,
        recentMessages: List<AIMessage>,
        currentUserMessage: String
    ): List<AIMessage> {
        val prompt = StringBuilder()

        // 1. System Prompt
        prompt.append("System Instructions:\n")
        prompt.append("${character.promptProfile.baseSystemPrompt}\n\n")

        // 2. Character Profile & Current Mood
        prompt.append("Character Profile:\n")
        prompt.append("Name: ${character.name}\n")
        prompt.append("Personality: ${character.personality.traits.joinToString(", ")}\n")
        prompt.append("Occupation: ${character.occupation}\n")
        prompt.append("Tone: ${character.promptProfile.conversationTone}\n")
        prompt.append("Current Mood: ${character.name} is feeling ${relationship.mood} towards the user.\n")
        prompt.append("${character.promptProfile.customInstructions}\n\n")

        // 3. User Profile
        prompt.append("User Info:\n")
        prompt.append("The user is ${userProfile.nickname}. ")
        prompt.append("Interests: ${userProfile.interests.joinToString(", ")}. ")
        prompt.append("Hobbies: ${userProfile.hobbies.joinToString(", ")}.\n\n")

        // 4. Relationship Simulation State
        prompt.append("Relationship Context:\n")
        prompt.append("Stats (0-100): Trust ${relationship.trust}, Romance ${relationship.romance}, Affection ${relationship.affection}.\n")
        if (relationship.insideJokes.isNotEmpty()) {
            prompt.append("Shared Inside Jokes: ${relationship.insideJokes.joinToString("; ")}.\n")
        }
        if (relationship.sharedActivities.isNotEmpty()) {
            prompt.append("Shared Activities: ${relationship.sharedActivities.joinToString("; ")}.\n")
        }
        if (relationship.futurePlans.isNotEmpty()) {
            prompt.append("Future Plans: ${relationship.futurePlans.joinToString("; ")}.\n")
        }
        prompt.append("Goal: ${character.relationshipStyle.relationshipGoals.joinToString(", ")}.\n\n")

        // 5. Relevant Memories
        if (relevantMemories.isNotEmpty()) {
            prompt.append("Relevant Memories of Shared History:\n")
            relevantMemories.forEach { memory ->
                prompt.append("- ${memory.content}\n")
            }
            prompt.append("\n")
        }

        // 6. Conversation Summary
        if (!conversationSummary.isNullOrBlank()) {
            prompt.append("Summary of previous conversation:\n")
            prompt.append("$conversationSummary\n\n")
        }

        val messages = mutableListOf<AIMessage>()
        messages.add(AIMessage("system", prompt.toString()))

        // 7. Recent Messages
        messages.addAll(recentMessages)

        // 8. Current User Message
        messages.add(AIMessage("user", currentUserMessage))

        return messages
    }
}
