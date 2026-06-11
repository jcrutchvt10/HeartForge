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
fun BondStep(
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
        Text("Your relationship", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = draft.relationshipStyle.loveLanguages.joinToString(", "),
            onValueChange = { onUpdate(draft.copy(relationshipStyle = draft.relationshipStyle.copy(loveLanguages = it.split(",").map { t -> t.trim() }))) },
            label = { Text("Love Languages") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = draft.relationshipStyle.flirtingStyle,
            onValueChange = { onUpdate(draft.copy(relationshipStyle = draft.relationshipStyle.copy(flirtingStyle = it))) },
            label = { Text("Flirting Style") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = draft.relationshipStyle.relationshipGoals.joinToString(", "),
            onValueChange = { onUpdate(draft.copy(relationshipStyle = draft.relationshipStyle.copy(relationshipGoals = it.split(",").map { t -> t.trim() }))) },
            label = { Text("Relationship Goals") },
            modifier = Modifier.fillMaxWidth()
        )
        
        OutlinedTextField(
            value = draft.likes.joinToString(", "),
            onValueChange = { onUpdate(draft.copy(likes = it.split(",").map { t -> t.trim() }, hobbies = it.split(",").map { t -> t.trim() })) },
            label = { Text("His Likes / Hobbies") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = draft.dislikes.joinToString(", "),
            onValueChange = { onUpdate(draft.copy(dislikes = it.split(",").map { t -> t.trim() })) },
            label = { Text("His Dislikes") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
