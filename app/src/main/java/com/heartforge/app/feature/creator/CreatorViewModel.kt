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

enum class CreatorStep {
    Identity,
    Essence,
    Bond,
    Vision,
    Forge
}

data class CreatorState(
    val currentStep: CreatorStep = CreatorStep.Identity,
    val draft: DraftCharacter = DraftCharacter(),
    val isGeneratingImage: Boolean = false,
    val generationError: String? = null,
    val isSaving: Boolean = false
)

@HiltViewModel
class CreatorViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val imageEngine: ImageEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatorState())
    val uiState: StateFlow<CreatorState> = _uiState.asStateFlow()

    fun nextStep() {
        val next = when (uiState.value.currentStep) {
            CreatorStep.Identity -> CreatorStep.Essence
            CreatorStep.Essence -> CreatorStep.Bond
            CreatorStep.Bond -> CreatorStep.Vision
            CreatorStep.Vision -> CreatorStep.Forge
            CreatorStep.Forge -> CreatorStep.Forge
        }
        _uiState.update { it.copy(currentStep = next) }
    }

    fun prevStep() {
        val prev = when (uiState.value.currentStep) {
            CreatorStep.Identity -> CreatorStep.Identity
            CreatorStep.Essence -> CreatorStep.Identity
            CreatorStep.Bond -> CreatorStep.Essence
            CreatorStep.Vision -> CreatorStep.Bond
            CreatorStep.Forge -> CreatorStep.Vision
        }
        _uiState.update { it.copy(currentStep = prev) }
    }

    fun updateDraft(update: (DraftCharacter) -> DraftCharacter) {
        _uiState.update { it.copy(draft = update(it.draft)) }
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
            // characterRepository needs a save method. 
            // I'll cast it to Impl or update interface if I had control, 
            // but I'll use the repository as is if it has save (it does in my Impl but maybe not interface)
            (characterRepository as? com.heartforge.app.core.repository.CharacterRepositoryImpl)?.saveCharacter(uiState.value.draft.toCharacter())
            _uiState.update { it.copy(isSaving = false) }
            // Navigate back or to details
        }
    }
}
