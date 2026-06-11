package com.heartforge.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heartforge.app.core.model.Character
import com.heartforge.app.core.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val activeCharacter: Character? = null,
    val recommendedMatches: List<Character> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val characterRepository: com.heartforge.app.core.repository.CharacterRepository,
    private val dataInitializer: com.heartforge.app.core.util.DataInitializer
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            var characters = characterRepository.getCharacters()
            if (characters.isEmpty()) {
                dataInitializer.populateSampleData()
                characters = characterRepository.getCharacters()
            }
            _uiState.value = HomeState(
                activeCharacter = characters.firstOrNull(),
                recommendedMatches = characters.drop(1).take(4),
                isLoading = false
            )
        }
    }
}
