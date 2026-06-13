package com.heartforge.app.feature.gallery

import android.util.Log
import androidx.lifecycle.SavedStateHandle
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
    private val casualPhotoGenerator: CasualPhotoGenerator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialCharacterId: String? = savedStateHandle.get<String>("characterId")
    private val initialAutoGenerate: Boolean = savedStateHandle.get<Boolean>("autoGenerate") ?: false

    private val _uiState = MutableStateFlow(GalleryState())
    val uiState: StateFlow<GalleryState> = _uiState.asStateFlow()

    init {
        loadCharacters()
        if (initialCharacterId != null) {
            selectCharacter(initialCharacterId)
            if (initialAutoGenerate) {
                generateNSFW()
            }
        }
    }

    private fun loadCharacters() {
        viewModelScope.launch {
            val characters = characterRepository.getCharacters()
            _uiState.update { it.copy(
                characters = characters,
                isLoading = false
            ) }
        }
    }

    fun selectCharacter(characterId: String?) {
        if (characterId == null) {
            _uiState.update { it.copy(selectedCharacterId = null, characterImages = emptyList(), nsfwImages = emptyList()) }
            return
        }
        viewModelScope.launch {
            val character = characterRepository.getCharacter(characterId)
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
        val newState = !_uiState.value.showNSFW
        _uiState.update { it.copy(showNSFW = newState) }

        if (newState && _uiState.value.nsfwImages.isEmpty() && !_uiState.value.isGeneratingNSFW) {
            Log.d(TAG, "Triggering automatic NSFW generation as gallery is empty")
            generateNSFW()
        }
    }

    fun generateNSFW() {
        val characterId = _uiState.value.selectedCharacterId ?: return
        if (_uiState.value.isGeneratingNSFW) return

        val count = if (_uiState.value.nsfwImages.isEmpty()) 8 else 4
        Log.d(TAG, "Starting NSFW generation flow for $characterId (count: $count)")
        _uiState.update { it.copy(isGeneratingNSFW = true, nsfwGenerationError = null) }

        viewModelScope.launch {
            nsfwGenerator.generateForCharacter(characterId, count)
                .onStart {
                    Log.d(TAG, "NSFW generation flow started")
                }
                .catch { error ->
                    Log.e(TAG, "NSFW generation flow failed", error)
                    _uiState.update {
                        it.copy(
                            isGeneratingNSFW = false,
                            nsfwGenerationError = error.message ?: "Generation failed"
                        )
                    }
                }
                .onCompletion {
                    Log.d(TAG, "NSFW generation flow completed")
                    _uiState.update { it.copy(isGeneratingNSFW = false) }
                    loadCharacters()
                }
                .collect { path ->
                    Log.d(TAG, "Received new NSFW image: $path")
                    val newImage = GalleryImage(path, "NSFW")
                    _uiState.update {
                        it.copy(
                            nsfwImages = it.nsfwImages + newImage,
                            showNSFW = true
                        )
                    }
                }
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
                    loadCharacters()
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
