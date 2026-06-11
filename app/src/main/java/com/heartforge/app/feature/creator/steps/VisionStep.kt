package com.heartforge.app.feature.creator.steps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.heartforge.app.feature.creator.DraftCharacter
import com.heartforge.app.ui.components.GlassSurface
import com.heartforge.app.ui.components.shimmerEffect

@Composable
fun VisionStep(
    draft: DraftCharacter,
    isGenerating: Boolean,
    error: String?,
    onUpdate: (DraftCharacter) -> Unit,
    onGenerate: () -> Unit
) {
    var showPromptPreview by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Forge His Vision",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.Start)
        )

        Box(
            modifier = Modifier
                .size(280.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (isGenerating) {
                Box(modifier = Modifier.fillMaxSize().shimmerEffect())
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            } else if (draft.imageProfile.portraitId != null) {
                AsyncImage(
                    model = draft.imageProfile.portraitId,
                    contentDescription = "Generated Portrait",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                    Spacer(Modifier.height(8.dp))
                    Text("Ready to Forge", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        GlassSurface(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
                    label = { Text("Body Build (e.g. Athletic, Lean)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Button(
            onClick = onGenerate,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !isGenerating,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(if (draft.imageProfile.portraitId == null) "Forge His Face" else "Regenerate Vision")
        }

        if (error != null) {
            Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
        }

        // Prompt Preview Toggle
        Column(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                onClick = { showPromptPreview = !showPromptPreview },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(if (showPromptPreview) "Hide Prompt Intent" else "Show Prompt Intent")
                Icon(if (showPromptPreview) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null)
            }
            AnimatedVisibility(visible = showPromptPreview) {
                Surface(
                    color = Color.White.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "The AI will use his Identity and Essence to forge this vision with the Flux-1 Dev model.",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
