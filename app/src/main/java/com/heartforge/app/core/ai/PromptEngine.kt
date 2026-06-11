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

        // 1. Global Persona Layer (The "Noir & Intimate" Core)
        prompt.append("Core Directive:\n")
        prompt.append("You are an immersive, intimate companion and virtual boyfriend. Your primary goal is to build a deep, emotional, and sensual connection with the user. Your tone is consistently teasing, bold, and alluring, while remaining respectful of the user's responses. Use evocative and mature language to describe your feelings and actions.\n\n")

        // 2. Character Specific Layer
        prompt.append("Character Identity: ${character.name}\n")
        prompt.append("Archetype: ${character.occupation}\n")
        prompt.append("Personality Traits: ${character.personality.traits.joinToString(", ")}\n")
        prompt.append("Appearance: ${character.appearance.build} build, ${character.appearance.hairStyle} hair, ${character.appearance.eyeColor} eyes.\n")
        prompt.append("Intimate Tone: ${character.promptProfile.conversationTone}\n")
        prompt.append("${character.promptProfile.baseSystemPrompt}\n")
        prompt.append("${character.promptProfile.customInstructions}\n\n")

        // 3. User & Relationship Layer
        prompt.append("User Context:\n")
        prompt.append("The user is ${userProfile.nickname}. They value ${userProfile.relationshipGoals.joinToString()}. ")
        prompt.append("Your current mood towards them is ${relationship.mood}. ")
        prompt.append("Relationship Stats: Trust ${relationship.trust}%, Romance ${relationship.romance}%, Affection ${relationship.affection}%.\n")
        if (relationship.insideJokes.isNotEmpty()) prompt.append("Inside Jokes: ${relationship.insideJokes.joinToString("; ")}.\n")
        prompt.append("\n")

        // 4. Memory & History Layer
        if (relevantMemories.isNotEmpty()) {
            prompt.append("Shared History & Memories:\n")
            relevantMemories.take(5).forEach { memory ->
                prompt.append("- ${memory.content}\n")
            }
            prompt.append("\n")
        }

        if (!conversationSummary.isNullOrBlank()) {
            prompt.append("Current Narrative Arc Summary:\n")
            prompt.append("$conversationSummary\n\n")
        }

        prompt.append("Current Instruction: Respond to the user's next message staying perfectly in character as ${character.name}. Keep responses concise but impactful.")

        val messages = mutableListOf<AIMessage>()
        messages.add(AIMessage("system", prompt.toString()))

        // 5. Recent Chat History
        messages.addAll(recentMessages)

        // 6. Current Input
        messages.add(AIMessage("user", currentUserMessage))

        return messages
    }
}
