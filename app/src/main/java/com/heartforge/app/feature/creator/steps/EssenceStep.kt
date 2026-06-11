package com.heartforge.app.feature.creator.steps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heartforge.app.feature.creator.DraftCharacter
import com.heartforge.app.ui.components.GlassSurface

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EssenceStep(
    draft: DraftCharacter,
    onUpdate: (DraftCharacter) -> Unit
) {
    val commonTraits = listOf("Playful", "Protective", "Kind", "Ambitious", "Mysterious", "Sarcastic", "Intellectual", "Affectionate")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Forge His Essence",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        GlassSurface(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Personality Traits", style = MaterialTheme.typography.titleMedium)
                
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    commonTraits.forEach { trait ->
                        val isSelected = draft.personality.traits.contains(trait)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                val newTraits = if (isSelected) draft.personality.traits - trait else draft.personality.traits + trait
                                onUpdate(draft.copy(personality = draft.personality.copy(traits = newTraits)))
                            },
                            label = { Text(trait) }
                        )
                    }
                }

                OutlinedTextField(
                    value = draft.personality.traits.joinToString(", "),
                    onValueChange = { onUpdate(draft.copy(personality = draft.personality.copy(traits = it.split(",").map { t -> t.trim() }.filter { t -> t.isNotEmpty() }))) },
                    label = { Text("Custom Traits (comma separated)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = draft.personality.MBTI,
                    onValueChange = { onUpdate(draft.copy(personality = draft.personality.copy(MBTI = it.uppercase()))) },
                    label = { Text("MBTI (e.g. INFJ, ENFP)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text("Communication Style", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = draft.promptProfile.conversationTone,
                    onValueChange = { onUpdate(draft.copy(promptProfile = draft.promptProfile.copy(conversationTone = it))) },
                    label = { Text("Conversation Tone (e.g. Sarcastic, Warm)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Column {
                    Text("Emoji Level", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Low", "Medium", "High").forEach { level ->
                            FilterChip(
                                selected = draft.promptProfile.emojiLevel == level.lowercase(),
                                onClick = { onUpdate(draft.copy(promptProfile = draft.promptProfile.copy(emojiLevel = level.lowercase()))) },
                                label = { Text(level) }
                            )
                        }
                    }
                }
            }
        }
    }
}
