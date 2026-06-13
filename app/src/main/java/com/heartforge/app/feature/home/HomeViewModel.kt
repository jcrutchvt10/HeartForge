package com.heartforge.app.feature.home

import android.util.Log
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
    val isLoading: Boolean = true,
    val forgeProgress: com.heartforge.app.core.util.ForgeProgress = com.heartforge.app.core.util.ForgeProgress()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    val characterRepository: com.heartforge.app.core.repository.CharacterRepository,
    private val dataInitializer: com.heartforge.app.core.util.DataInitializer,
    private val memoryDao: com.heartforge.app.core.database.MemoryDao,
    private val settingsRepository: com.heartforge.app.core.repository.SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    init {
        loadData()
        observeProgress()
    }

    private fun observeProgress() {
        viewModelScope.launch {
            dataInitializer.forgeProgress.collect { progress ->
                val characters = characterRepository.getCharacters()
                val previews = characters.flatMap { c -> listOfNotNull(c.imageProfile.portraitId) }.take(8)
                _uiState.update { it.copy(
                    forgeProgress = progress,
                    activeCharacter = characters.firstOrNull(),
                    recommendedMatches = if (it.recommendedMatches.isEmpty() && characters.isNotEmpty()) characters.take(4) else it.recommendedMatches,
                    galleryPreviews = previews
                ) }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val currentVersion = settingsRepository.dataVersion.first()
            val targetVersion = 24 // Safe Initial (Zero Filter) + Erotic NSFW + Navigation Fix
            
            var characters = characterRepository.getCharacters()
            val needsReset = currentVersion < targetVersion || characters.isEmpty()
            
            Log.d("HomeViewModel", "Version: $currentVersion, Target: $targetVersion, Characters: ${characters.size}, needsReset: $needsReset")

            if (needsReset) {
                Log.d("HomeViewModel", "Triggering TOTAL RESET population...")
                dataInitializer.populateSampleData()
                settingsRepository.updateDataVersion(targetVersion)
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
