package com.heartforge.app.feature.gallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heartforge.app.core.ai.CasualPhotoGenerator
import com.heartforge.app.core.ai.NSFWGenerator
import com.heartforge.app.core.model.Character
import com.heartforge.app.core.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "GalleryViewModel"

data class GalleryState(
    val characters: List<Character> = emptyList(),
    val selectedCharacterId: String? = null,
    val characterImages: List<GalleryImage> = emptyList(),
    val nsfwImages: List<GalleryImage> = emptyList(),
    val showNSFW: Boolean = false,
    val isLoading: Boolean = true,
    val isGeneratingNSFW: Boolean = false,
    val nsfwGenerationError: String? = null,
    val isRegeneratingCasual: Boolean = false,
    val casualGenResult: String? = null
)

data class GalleryImage(
    val url: String,
    val label: String
)

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val nsfwGenerator: NSFWGenerator,
    private val casualPhotoGenerator: CasualPhotoGenerator
) : ViewModel() {

    private val _uiState = MutableStateFlow(GalleryState())
    val uiState: StateFlow<GalleryState> = _uiState.asStateFlow()

    init {
        loadCharacters()
    }

    private fun loadCharacters() {
        viewModelScope.launch {
            val characters = characterRepository.getCharacters()
            _uiState.value = GalleryState(
                characters = characters,
                isLoading = false
            )
        }
    }

    fun selectCharacter(characterId: String?) {
        if (characterId == null) {
            _uiState.update { it.copy(selectedCharacterId = null, characterImages = emptyList(), nsfwImages = emptyList()) }
            return
        }
        viewModelScope.launch {
            val character = _uiState.value.characters.find { it.id == characterId }
            if (character != null) {
                val profile = character.imageProfile
                val images = listOfNotNull(
                    profile.portraitId?.let { GalleryImage(it, "Portrait") },
                    profile.casualId?.let { GalleryImage(it, "Casual") },
                    profile.gymId?.let { GalleryImage(it, "Gym") },
                    profile.beachId?.let { GalleryImage(it, "Beach") },
                    profile.formalId?.let { GalleryImage(it, "Formal") },
                    profile.sleepwearId?.let { GalleryImage(it, "Sleepwear") },
                    profile.vacationId?.let { GalleryImage(it, "Vacation") },
                ) + profile.selfieIds.map { GalleryImage(it, "Selfie") }

                val nsfw = (profile.nsfwGallery ?: emptyList()).map { GalleryImage(it, "NSFW") }

                _uiState.update {
                    it.copy(
                        selectedCharacterId = characterId,
                        characterImages = images,
                        nsfwImages = nsfw,
                        showNSFW = false
                    )
                }
                Log.d(TAG, "Selected ${character.name}, ${profile.casualId?.let { "has casual" } ?: "NO casual"}")
            }
        }
    }

    fun toggleNSFW() {
        _uiState.update { it.copy(showNSFW = !it.showNSFW) }
    }

    fun generateNSFW() {
        val characterId = _uiState.value.selectedCharacterId ?: return
        _uiState.update { it.copy(isGeneratingNSFW = true, nsfwGenerationError = null) }

        viewModelScope.launch {
            val result = nsfwGenerator.generateForCharacter(characterId)
            result.fold(
                onSuccess = { paths ->
                    val nsfwImages = paths.map { GalleryImage(it, "NSFW") }
                    _uiState.update {
                        it.copy(
                            isGeneratingNSFW = false,
                            nsfwImages = it.nsfwImages + nsfwImages,
                            showNSFW = true
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isGeneratingNSFW = false,
                            nsfwGenerationError = error.message ?: "Generation failed"
                        )
                    }
                }
            )
        }
    }

    fun regenerateCasualPhoto() {
        val characterId = _uiState.value.selectedCharacterId ?: return
        _uiState.update { it.copy(isRegeneratingCasual = true, casualGenResult = null) }

        viewModelScope.launch {
            val result = casualPhotoGenerator.generateCasualForCharacter(characterId)
            result.fold(
                onSuccess = { path ->
                    val updatedImages = _uiState.value.characterImages.map { img ->
                        if (img.label == "Casual") img.copy(url = path) else img
                    }
                    _uiState.update {
                        it.copy(
                            isRegeneratingCasual = false,
                            casualGenResult = "Casual photo updated!",
                            characterImages = updatedImages
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isRegeneratingCasual = false,
                            casualGenResult = "Failed: ${error.message?.take(100)}"
                        )
                    }
                }
            )
        }
    }
}
