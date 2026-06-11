package com.heartforge.app.feature.creator

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.heartforge.app.feature.creator.steps.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorScreen(
    navController: NavController,
    viewModel: CreatorViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(state.currentStep.name) },
                navigationIcon = {
                    if (state.currentStep != CreatorStep.Identity) {
                        IconButton(onClick = { viewModel.prevStep() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    } else {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Close")
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (state.currentStep != CreatorStep.Forge) {
                Surface(tonalElevation = 8.dp) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .navigationBarsPadding(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { viewModel.nextStep() },
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text("Next")
                            Spacer(Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null)
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            AnimatedContent(targetState = state.currentStep, label = "StepTransition") { step ->
                when (step) {
                    CreatorStep.Identity -> IdentityStep(
                        draft = state.draft,
                        onUpdate = { update -> viewModel.updateDraft { update } }
                    )
                    CreatorStep.Essence -> EssenceStep(
                        draft = state.draft,
                        onUpdate = { update -> viewModel.updateDraft { update } }
                    )
                    CreatorStep.Bond -> BondStep(
                        draft = state.draft,
                        onUpdate = { update -> viewModel.updateDraft { update } }
                    )
                    CreatorStep.Vision -> VisionStep(
                        draft = state.draft,
                        isGenerating = state.isGeneratingImage,
                        error = state.generationError,
                        onUpdate = { update -> viewModel.updateDraft { update } },
                        onGenerate = { viewModel.generatePortrait() }
                    )
                    CreatorStep.Forge -> ForgeStep(
                        draft = state.draft,
                        isSaving = state.isSaving,
                        onSave = { 
                            viewModel.saveCharacter()
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
