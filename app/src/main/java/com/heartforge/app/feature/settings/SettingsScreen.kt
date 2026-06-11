package com.heartforge.app.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.heartforge.app.ui.theme.RoseRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("AI Configuration", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = state.editingApiKey,
                onValueChange = { viewModel.updateApiKey(it) },
                label = { Text("NVIDIA API Key") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            OutlinedTextField(
                value = state.endpoint,
                onValueChange = { viewModel.updateEndpoint(it) },
                label = { Text("NVIDIA Endpoint") },
                modifier = Modifier.fillMaxWidth()
            )

            // Model dropdown — shows all available models from the API
            var modelExpanded by remember { mutableStateOf(false) }
            val selectedModel = state.chatModel

            ExposedDropdownMenuBox(
                expanded = modelExpanded,
                onExpandedChange = { modelExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedModel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Chat Model") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    trailingIcon = {
                        if (state.isRefreshingModels) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            Row {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = modelExpanded)
                                TextButton(onClick = {
                                    viewModel.refreshModels()
                                    modelExpanded = false
                                }) {
                                    Text("Refresh", color = RoseRed)
                                }
                            }
                        }
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = modelExpanded,
                    onDismissRequest = { modelExpanded = false }
                ) {
                    if (state.availableModels.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No models loaded. Tap Refresh.") },
                            onClick = { modelExpanded = false }
                        )
                    } else {
                        state.availableModels.forEach { model ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = model,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (model == selectedModel) RoseRed else MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                onClick = {
                                    viewModel.updateChatModel(model)
                                    modelExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = state.imageModel,
                onValueChange = { viewModel.updateImageModel(it) },
                label = { Text("Image Model") },
                modifier = Modifier.fillMaxWidth()
            )

            Column {
                Text("Temperature: ${String.format("%.2f", state.temperature)}")
                Slider(
                    value = state.temperature,
                    onValueChange = { viewModel.updateTemperature(it) },
                    valueRange = 0f..1f
                )
            }

            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text("Enable Streaming", modifier = Modifier.weight(1f))
                Switch(
                    checked = state.isStreamingEnabled,
                    onCheckedChange = { viewModel.updateStreaming(it) }
                )
            }
        }
    }
}
