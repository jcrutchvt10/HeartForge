package com.heartforge.app.feature.creator.steps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heartforge.app.feature.creator.DraftCharacter

@Composable
fun EssenceStep(
    draft: DraftCharacter,
    onUpdate: (DraftCharacter) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("What's he like?", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = draft.personality.traits.joinToString(", "),
            onValueChange = { onUpdate(draft.copy(personality = draft.personality.copy(traits = it.split(",").map { t -> t.trim() }))) },
            label = { Text("Personality Traits (comma separated)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = draft.personality.MBTI,
            onValueChange = { onUpdate(draft.copy(personality = draft.personality.copy(MBTI = it))) },
            label = { Text("MBTI (e.g. ENFJ)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = draft.promptProfile.conversationTone,
            onValueChange = { onUpdate(draft.copy(promptProfile = draft.promptProfile.copy(conversationTone = it))) },
            label = { Text("Conversation Tone (e.g. Sarcastic, Warm)") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Text("Emoji Level: ", modifier = Modifier.weight(1f))
            listOf("Low", "Medium", "High").forEach { level ->
                FilterChip(
                    selected = draft.promptProfile.emojiLevel == level.lowercase(),
                    onClick = { onUpdate(draft.copy(promptProfile = draft.promptProfile.copy(emojiLevel = level.lowercase()))) },
                    label = { Text(level) },
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}
