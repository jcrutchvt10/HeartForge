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

@Composable
fun BondStep(
    draft: DraftCharacter,
    onUpdate: (DraftCharacter) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Forge Your Bond",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        GlassSurface(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = draft.relationshipStyle.loveLanguages.joinToString(", "),
                    onValueChange = { onUpdate(draft.copy(relationshipStyle = draft.relationshipStyle.copy(loveLanguages = it.split(",").map { t -> t.trim() }.filter { it.isNotEmpty() }))) },
                    label = { Text("Love Languages (e.g. Physical Touch)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = draft.relationshipStyle.flirtingStyle,
                    onValueChange = { onUpdate(draft.copy(relationshipStyle = draft.relationshipStyle.copy(flirtingStyle = it))) },
                    label = { Text("Flirting Style (e.g. Teasing)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = draft.relationshipStyle.relationshipGoals.joinToString(", "),
                    onValueChange = { onUpdate(draft.copy(relationshipStyle = draft.relationshipStyle.copy(relationshipGoals = it.split(",").map { t -> t.trim() }.filter { it.isNotEmpty() }))) },
                    label = { Text("Relationship Goals (e.g. Long-term)") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = draft.likes.joinToString(", "),
                    onValueChange = { onUpdate(draft.copy(likes = it.split(",").map { t -> t.trim() }.filter { it.isNotEmpty() }, hobbies = it.split(",").map { t -> t.trim() }.filter { it.isNotEmpty() })) },
                    label = { Text("His Hobbies / Likes") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = draft.dislikes.joinToString(", "),
                    onValueChange = { onUpdate(draft.copy(dislikes = it.split(",").map { t -> t.trim() }.filter { it.isNotEmpty() })) },
                    label = { Text("His Dislikes") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
