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

        // 2. Character Prompt
        prompt.append("Character Profile:\n")
        prompt.append("Name: ${character.name}\n")
        prompt.append("Personality: ${character.personality.traits.joinToString(", ")}\n")
        prompt.append("Occupation: ${character.occupation}\n")
        prompt.append("Bio: ${character.biography}\n")
        prompt.append("Tone: ${character.promptProfile.conversationTone}\n")
        prompt.append("${character.promptProfile.customInstructions}\n\n")

        // 3. User Profile
        prompt.append("User Info:\n")
        prompt.append("The user is ${userProfile.nickname}. ")
        prompt.append("Interests: ${userProfile.interests.joinToString(", ")}. ")
        prompt.append("Hobbies: ${userProfile.hobbies.joinToString(", ")}.\n\n")

        // 4. Relationship Memory
        prompt.append("Relationship Context:\n")
        prompt.append("Trust: ${relationship.trust}/100, Romance: ${relationship.romance}/100. ")
        prompt.append("Relationship Goals: ${character.relationshipStyle.relationshipGoals.joinToString(", ")}.\n\n")

        // 5. Relevant Memories
        if (relevantMemories.isNotEmpty()) {
            prompt.append("Relevant Memories of Shared History:\n")
            relevantMemories.forEach { memory ->
                prompt.append("- ${memory.content} (Importance: ${memory.importance})\n")
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
