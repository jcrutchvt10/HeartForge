package com.heartforge.app.feature.memories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heartforge.app.core.database.MemoryDao
import com.heartforge.app.core.database.toExternal
import com.heartforge.app.core.model.Character
import com.heartforge.app.core.model.Memory
import com.heartforge.app.core.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MemoryState(
    val character: Character? = null,
    val memories: List<Memory> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class MemoryViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val memoryDao: MemoryDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterId: String = checkNotNull(savedStateHandle["characterId"])

    private val _uiState = MutableStateFlow(MemoryState())
    val uiState: StateFlow<MemoryState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val character = characterRepository.getCharacter(characterId)
            _uiState.update { it.copy(character = character) }

            memoryDao.getMemories(characterId).collect { entities ->
                _uiState.update { it.copy(memories = entities.map { it.toExternal() }, isLoading = false) }
            }
        }
    }
}
