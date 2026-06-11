package com.heartforge.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heartforge.app.core.model.Character
import com.heartforge.app.core.database.toExternal
import com.heartforge.app.core.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val activeCharacter: Character? = null,
    val recommendedMatches: List<Character> = emptyList(),
    val recentMemories: List<com.heartforge.app.core.model.Memory> = emptyList(),
    val galleryPreviews: List<String> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    val characterRepository: com.heartforge.app.core.repository.CharacterRepository,
    private val dataInitializer: com.heartforge.app.core.util.DataInitializer,
    private val memoryDao: com.heartforge.app.core.database.MemoryDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            var characters = characterRepository.getCharacters()
            // Force re-population for Edgy Roster v10 (Verified Photos & Gallery)
            if (characters.size < 20 || !characters.any { it.imageProfile.casualId != null }) {
                dataInitializer.populateSampleData()
                characters = characterRepository.getCharacters()
            }

            val memories = characters.firstOrNull()?.let { 
                memoryDao.getRelevantMemories(it.id, 1).map { m -> m.toExternal() }
            } ?: emptyList()

            val previews = characters.flatMap { character ->
                listOfNotNull(character.imageProfile.portraitId)
            }.take(8)

            _uiState.value = HomeState(
                activeCharacter = characters.firstOrNull(),
                recommendedMatches = characters.shuffled().take(4),
                recentMemories = memories,
                galleryPreviews = previews,
                isLoading = false
            )
        }
    }
}
