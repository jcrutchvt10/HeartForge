package com.heartforge.app.feature.creator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heartforge.app.core.ai.ImageEngine
import com.heartforge.app.core.ai.ImageResult
import com.heartforge.app.core.ai.SceneType
import com.heartforge.app.core.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class CreatorStep(val progress: Float) {
    Identity(0.2f),
    Essence(0.4f),
    Bond(0.6f),
    Vision(0.8f),
    Forge(1.0f)
}

data class CreatorState(
    val currentStep: CreatorStep = CreatorStep.Identity,
    val draft: DraftCharacter = DraftCharacter(),
    val isGeneratingImage: Boolean = false,
    val generationError: String? = null,
    val isSaving: Boolean = false,
    val canGoNext: Boolean = false
)

@HiltViewModel
class CreatorViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val imageEngine: ImageEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatorState())
    val uiState: StateFlow<CreatorState> = _uiState.asStateFlow()

    init {
        // Observe draft changes to update validation
        _uiState.update { it.copy(canGoNext = validateStep(it.currentStep, it.draft)) }
    }

    fun nextStep() {
        val next = when (uiState.value.currentStep) {
            CreatorStep.Identity -> CreatorStep.Essence
            CreatorStep.Essence -> CreatorStep.Bond
            CreatorStep.Bond -> CreatorStep.Vision
            CreatorStep.Vision -> CreatorStep.Forge
            CreatorStep.Forge -> CreatorStep.Forge
        }
        _uiState.update { it.copy(
            currentStep = next,
            canGoNext = validateStep(next, it.draft)
        ) }
    }

    fun prevStep() {
        val prev = when (uiState.value.currentStep) {
            CreatorStep.Identity -> CreatorStep.Identity
            CreatorStep.Essence -> CreatorStep.Identity
            CreatorStep.Bond -> CreatorStep.Essence
            CreatorStep.Vision -> CreatorStep.Bond
            CreatorStep.Forge -> CreatorStep.Vision
        }
        _uiState.update { it.copy(
            currentStep = prev,
            canGoNext = validateStep(prev, it.draft)
        ) }
    }

    fun updateDraft(update: (DraftCharacter) -> DraftCharacter) {
        _uiState.update { 
            val newDraft = update(it.draft)
            it.copy(
                draft = newDraft,
                canGoNext = validateStep(it.currentStep, newDraft)
            )
        }
    }

    private fun validateStep(step: CreatorStep, draft: DraftCharacter): Boolean {
        return when (step) {
            CreatorStep.Identity -> draft.name.isNotBlank() && draft.occupation.isNotBlank()
            CreatorStep.Essence -> draft.personality.traits.isNotEmpty() && draft.personality.MBTI.isNotBlank()
            CreatorStep.Bond -> draft.relationshipStyle.loveLanguages.isNotEmpty() && draft.relationshipStyle.relationshipGoals.isNotEmpty()
            CreatorStep.Vision -> true // Image is optional but encouraged
            CreatorStep.Forge -> true
        }
    }

    fun generatePortrait() {
        viewModelScope.launch {
            _uiState.update { it.copy(isGeneratingImage = true, generationError = null) }
            val result = imageEngine.generateContextualImage(uiState.value.draft.toCharacter(), SceneType.Portrait)
            
            when (result) {
                is ImageResult.Success -> {
                    _uiState.update { state -> 
                        state.copy(
                            isGeneratingImage = false,
                            draft = state.draft.copy(
                                imageProfile = state.draft.imageProfile.copy(portraitId = result.base64)
                            )
                        )
                    }
                }
                is ImageResult.Error -> {
                    _uiState.update { it.copy(isGeneratingImage = false, generationError = result.message) }
                }
            }
        }
    }

    fun saveCharacter() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            characterRepository.saveCharacter(uiState.value.draft.toCharacter())
            _uiState.update { it.copy(isSaving = false) }
        }
    }
}
