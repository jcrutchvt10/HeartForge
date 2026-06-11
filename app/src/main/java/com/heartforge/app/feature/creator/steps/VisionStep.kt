package com.heartforge.app.feature.creator.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun VisionStep(
    draft: DraftCharacter,
    isGenerating: Boolean,
    error: String?,
    onUpdate: (DraftCharacter) -> Unit,
    onGenerate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Visualize him", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.align(Alignment.Start))

        Box(
            modifier = Modifier
                .size(250.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (isGenerating) {
                CircularProgressIndicator()
            } else if (draft.imageProfile.portraitId != null) {
                AsyncImage(
                    model = draft.imageProfile.portraitId,
                    contentDescription = "Generated Portrait",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text("No image generated yet", style = MaterialTheme.typography.bodyMedium)
            }
        }

        OutlinedTextField(
            value = draft.appearance.hairStyle,
            onValueChange = { onUpdate(draft.copy(appearance = draft.appearance.copy(hairStyle = it))) },
            label = { Text("Hair Style (e.g. Messy dark brown)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = draft.appearance.eyeColor,
            onValueChange = { onUpdate(draft.copy(appearance = draft.appearance.copy(eyeColor = it))) },
            label = { Text("Eye Color") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = draft.appearance.build,
            onValueChange = { onUpdate(draft.copy(appearance = draft.appearance.copy(build = it))) },
            label = { Text("Body Build (e.g. Athletic)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onGenerate,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !isGenerating
        ) {
            Text(if (draft.imageProfile.portraitId == null) "Forge His Face" else "Regenerate")
        }

        if (error != null) {
            Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
        }
    }
}
