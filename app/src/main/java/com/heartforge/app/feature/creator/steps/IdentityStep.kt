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
fun IdentityStep(
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
        Text("Tell us about him", style = MaterialTheme.typography.headlineSmall)
        
        OutlinedTextField(
            value = draft.name,
            onValueChange = { onUpdate(draft.copy(name = it)) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = draft.age.toString(),
            onValueChange = { onUpdate(draft.copy(age = it.toIntOrNull() ?: 0)) },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = draft.occupation,
            onValueChange = { onUpdate(draft.copy(occupation = it)) },
            label = { Text("Occupation") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = draft.location,
            onValueChange = { onUpdate(draft.copy(location = it)) },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = draft.biography,
            onValueChange = { onUpdate(draft.copy(biography = it)) },
            label = { Text("Biography") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
    }
}
