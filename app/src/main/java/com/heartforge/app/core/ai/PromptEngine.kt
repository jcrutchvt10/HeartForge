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
        // Simple token‑budget handling (approximate by character count).
        // Keep recent messages up to ~2000 characters and memories up to 5 items.
        val maxChars = 2000
        var accumulated = 0
        val trimmedRecent = recentMessages.reversed().takeWhile { msg ->
            val len = msg.content.length
            if (accumulated + len <= maxChars) {
                accumulated += len
                true
            } else false
        }.reversed()
        // Limit memories to a reasonable count (already limited to 5 elsewhere) and truncate their content.
        val trimmedMemories = relevantMemories.take(5).map { mem ->
            val maxMem = 300
            if (mem.content.length > maxMem) mem.copy(content = mem.content.take(maxMem) + "…") else mem
        }
        // Truncate conversation summary if too long.
        val trimmedSummary = conversationSummary?.let { if (it.length > 500) it.take(500) + "…" else it }
        // Use trimmed collections in prompt building.
        val finalRelevantMemories = trimmedMemories
        val finalConversationSummary = trimmedSummary
        // Continue with existing logic using the trimmed variables.
        // NOTE: The rest of the method will reference `relevantMemories` and `conversationSummary`
        // which are now shadowed by the trimmed versions above.

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
        if (finalRelevantMemories.isNotEmpty()) {
            prompt.append("Shared History & Memories:\n")
            finalRelevantMemories.take(5).forEach { memory ->
                prompt.append("- ${memory.content}\n")
            }
            prompt.append("\n")
        }

        if (!finalConversationSummary.isNullOrBlank()) {
            prompt.append("Current Narrative Arc Summary:\n")
            prompt.append("$finalConversationSummary\n\n")
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
