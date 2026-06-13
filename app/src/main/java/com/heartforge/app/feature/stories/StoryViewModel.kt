package com.heartforge.app.feature.stories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heartforge.app.core.model.Relationship
import com.heartforge.app.core.model.StoryArc
import com.heartforge.app.core.model.StoryProgress
import com.heartforge.app.core.model.Character
import com.heartforge.app.core.repository.CharacterRepository
import com.heartforge.app.core.repository.RelationshipRepository
import com.heartforge.app.core.repository.StoryRepository
import com.heartforge.app.core.repository.StoryRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StoryState(
    val characterId: String = "",
    val character: Character? = null,
    val availableArcs: List<StoryArc> = emptyList(),
    val progress: List<StoryProgress> = emptyList(),
    val relationship: Relationship? = null,
    val isLoading: Boolean = true,
    val isGeneratingSecret: Boolean = false
)

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
    private val characterRepository: CharacterRepository,
    private val relationshipRepository: RelationshipRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoryState())
    val uiState: StateFlow<StoryState> = _uiState.asStateFlow()

    fun loadStories(characterId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(characterId = characterId, isLoading = true) }
            
            val character = characterRepository.getCharacter(characterId)
            _uiState.update { it.copy(character = character) }

            combine(
                storyRepository.getStoriesForCharacter(characterId),
                storyRepository.getProgressForCharacter(characterId),
                relationshipRepository.getRelationship(characterId)
            ) { arcs, progress, rel ->
                StoryState(
                    characterId = characterId,
                    character = character,
                    availableArcs = arcs,
                    progress = progress,
                    relationship = rel,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }
    
    fun generateSecretArc() {
        val character = uiState.value.character ?: return
        _uiState.update { it.copy(isGeneratingSecret = true) }
        
        viewModelScope.launch {
            (storyRepository as? StoryRepositoryImpl)?.generateSecretArc(character)
            _uiState.update { it.copy(isGeneratingSecret = false) }
        }
    }
    
    fun startChapter(arcId: String, chapterId: String) {
        viewModelScope.launch {
            val currentProgress = _uiState.value.progress.find { it.arcId == arcId }
                ?: StoryProgress(_uiState.value.characterId, arcId, emptyList(), null)
            
            storyRepository.updateProgress(currentProgress.copy(activeChapterId = chapterId))
        }
    }
}
