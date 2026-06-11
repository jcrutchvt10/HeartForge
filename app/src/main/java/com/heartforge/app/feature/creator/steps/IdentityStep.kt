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
fun IdentityStep(
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
            "Forge His Identity",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        GlassSurface(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = draft.name,
                    onValueChange = { onUpdate(draft.copy(name = it)) },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Column {
                    Text("Age: ${draft.age}", style = MaterialTheme.typography.labelLarge)
                    Slider(
                        value = draft.age.toFloat(),
                        onValueChange = { onUpdate(draft.copy(age = it.toInt())) },
                        valueRange = 18f..99f,
                        steps = 81
                    )
                }

                OutlinedTextField(
                    value = draft.occupation,
                    onValueChange = { onUpdate(draft.copy(occupation = it)) },
                    label = { Text("Occupation") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = draft.location,
                    onValueChange = { onUpdate(draft.copy(location = it)) },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = draft.biography,
                    onValueChange = { onUpdate(draft.copy(biography = it)) },
                    label = { Text("Biography / Backstory") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )
            }
        }
    }
}
