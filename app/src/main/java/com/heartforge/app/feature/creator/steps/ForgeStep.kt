package com.heartforge.app.feature.creator.steps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.heartforge.app.feature.creator.DraftCharacter

@Composable
fun ForgeStep(
    draft: DraftCharacter,
    isSaving: Boolean,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = draft.imageProfile.portraitId,
            contentDescription = null,
            modifier = Modifier.size(150.dp).clip(RoundedCornerShape(75.dp)),
            contentScale = ContentScale.Crop
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(draft.name, style = MaterialTheme.typography.headlineMedium)
            Text("${draft.age} • ${draft.occupation}", style = MaterialTheme.typography.bodyMedium)
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Personality", style = MaterialTheme.typography.titleMedium)
                Text(draft.personality.traits.joinToString(", "))
                
                Divider()
                
                Text("Relationship Style", style = MaterialTheme.typography.titleMedium)
                Text("Goal: ${draft.relationshipStyle.relationshipGoals.joinToString()}")
                Text("Flirting: ${draft.relationshipStyle.flirtingStyle}")
            }
        }

        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !isSaving
        ) {
            if (isSaving) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Forge Character")
            }
        }
    }
}
