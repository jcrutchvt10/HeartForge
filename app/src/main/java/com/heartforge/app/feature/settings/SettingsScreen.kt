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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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

            OutlinedTextField(
                value = state.chatModel,
                onValueChange = { viewModel.updateChatModel(it) },
                label = { Text("Chat Model") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (state.isRefreshingModels) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        TextButton(onClick = { viewModel.refreshModels() }) {
                            Text("Refresh")
                        }
                    }
                }
            )

            if (state.availableModels.isNotEmpty()) {
                Text("Available Models:", style = MaterialTheme.typography.labelMedium)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.availableModels.take(6).forEach { model ->
                        FilterChip(
                            selected = state.chatModel == model,
                            onClick = { viewModel.updateChatModel(model) },
                            label = { Text(model.split("/").last(), style = MaterialTheme.typography.labelSmall) }
                        )
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
