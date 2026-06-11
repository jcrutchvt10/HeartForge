package com.heartforge.app.feature.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heartforge.app.core.model.Character
import com.heartforge.app.core.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GalleryState(
    val images: List<GalleryImage> = emptyList(),
    val isLoading: Boolean = true
)

data class GalleryImage(
    val url: String,
    val characterName: String,
    val characterId: String
)

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GalleryState())
    val uiState: StateFlow<GalleryState> = _uiState.asStateFlow()

    init {
        loadImages()
    }

    private fun loadImages() {
        viewModelScope.launch {
            val characters = characterRepository.getCharacters()
            val allImages = characters.flatMap { character ->
                val profile = character.imageProfile
                listOfNotNull(
                    profile.portraitId,
                    profile.casualId,
                    profile.gymId,
                    profile.beachId,
                    profile.formalId,
                    profile.sleepwearId,
                    profile.vacationId
                ).map { url -> GalleryImage(url, character.name, character.id) } +
                profile.selfieIds.map { url -> GalleryImage(url, character.name, character.id) }
            }
            
            _uiState.value = GalleryState(
                images = allImages,
                isLoading = false
            )
        }
    }
}
