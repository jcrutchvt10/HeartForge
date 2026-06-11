package com.heartforge.app.feature.creator.steps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.heartforge.app.feature.creator.DraftCharacter
import com.heartforge.app.ui.components.GlassSurface

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
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Ready to Forge",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.Start)
        )

        AsyncImage(
            model = draft.imageProfile.portraitId,
            contentDescription = null,
            modifier = Modifier.size(160.dp).clip(RoundedCornerShape(80.dp)),
            contentScale = ContentScale.Crop
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(draft.name, style = MaterialTheme.typography.headlineMedium)
            Text("${draft.age} • ${draft.occupation}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        GlassSurface(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Manifest", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                
                DetailPreview(label = "Personality", value = draft.personality.traits.joinToString(", "))
                DetailPreview(label = "Goal", value = draft.relationshipStyle.relationshipGoals.joinToString())
                DetailPreview(label = "Mood Tone", value = draft.promptProfile.conversationTone)
            }
        }

        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !isSaving,
            shape = RoundedCornerShape(16.dp)
        ) {
            if (isSaving) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Finalize the Forge")
            }
        }
    }
}

@Composable
private fun DetailPreview(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
