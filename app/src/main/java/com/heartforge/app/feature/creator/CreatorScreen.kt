package com.heartforge.app.feature.creator

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.heartforge.app.feature.creator.steps.*
import com.heartforge.app.ui.components.GlassSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorScreen(
    navController: NavController,
    viewModel: CreatorViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            GlassSurface(
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp, 0.dp, 24.dp, 24.dp)
            ) {
                Column {
                    CenterAlignedTopAppBar(
                        title = { Text("Forge Partner", fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            IconButton(onClick = { 
                                if (state.currentStep == CreatorStep.Identity) navController.popBackStack() 
                                else viewModel.prevStep() 
                            }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                    )
                    LinearProgressIndicator(
                        progress = { state.currentStep.progress },
                        modifier = Modifier.fillMaxWidth().height(4.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                }
            }
        },
        bottomBar = {
            if (state.currentStep != CreatorStep.Forge) {
                GlassSurface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding(),
                    shape = androidx.compose.foundation.shape.CircleShape
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Step ${state.currentStep.ordinal + 1} of 5",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Button(
                            onClick = { viewModel.nextStep() },
                            modifier = Modifier.height(44.dp),
                            enabled = state.canGoNext,
                            shape = androidx.compose.foundation.shape.CircleShape
                        ) {
                            Text("Next")
                            Spacer(Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            AnimatedContent(
                targetState = state.currentStep,
                label = "StepTransition",
                transitionSpec = {
                    if (targetState.ordinal > initialState.ordinal) {
                        (slideInHorizontally(initialOffsetX = { it }) + fadeIn()).togetherWith(
                            slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
                        )
                    } else {
                        (slideInHorizontally(initialOffsetX = { -it }) + fadeIn()).togetherWith(
                            slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                        )
                    }
                }
            ) { step ->
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
                            navController.navigate(com.heartforge.app.navigation.Destination.Home.route) {
                                popUpTo(com.heartforge.app.navigation.Destination.Home.route) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}
