package com.heartforge.app.feature.matches

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heartforge.app.core.model.Character
import com.heartforge.app.core.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val character: Character? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class CharacterProfileViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterId: String = checkNotNull(savedStateHandle["characterId"])

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val character = characterRepository.getCharacter(characterId)
            _uiState.value = ProfileState(character, isLoading = false)
        }
    }
}
