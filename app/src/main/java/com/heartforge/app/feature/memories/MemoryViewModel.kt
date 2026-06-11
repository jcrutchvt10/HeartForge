package com.heartforge.app.feature.memories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heartforge.app.core.database.MemoryDao
import com.heartforge.app.core.database.toExternal
import com.heartforge.app.core.model.*
import com.heartforge.app.core.repository.CharacterRepository
import com.heartforge.app.core.repository.MemoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

data class MemoryState(
    val character: Character? = null,
    val memories: List<Memory> = emptyList(),
    val isLoading: Boolean = true,
    // Edit dialog state
    val editingMemory: Memory? = null,
    val editContent: String = "",
    val editImportance: Int = 3,
    val editCategory: MemoryCategory = MemoryCategory.Personal,
    val editSentiment: Sentiment = Sentiment.Neutral,
    val showEditDialog: Boolean = false
)

@HiltViewModel
class MemoryViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val memoryDao: MemoryDao,
    private val memoryRepository: MemoryRepository,
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
                _uiState.update { it.copy(memories = entities.map { e -> e.toExternal() }, isLoading = false) }
            }
        }
    }

    // --- Add new memory ---
    fun showAddDialog() {
        _uiState.update {
            it.copy(
                showEditDialog = true,
                editingMemory = null,
                editContent = "",
                editImportance = 3,
                editCategory = MemoryCategory.Personal,
                editSentiment = Sentiment.Neutral
            )
        }
    }

    // --- Edit existing memory ---
    fun showEditDialog(memory: Memory) {
        _uiState.update {
            it.copy(
                showEditDialog = true,
                editingMemory = memory,
                editContent = memory.content,
                editImportance = memory.importance,
                editCategory = memory.category,
                editSentiment = memory.sentiment
            )
        }
    }

    fun dismissEditDialog() {
        _uiState.update { it.copy(showEditDialog = false) }
    }

    fun updateEditContent(v: String) { _uiState.update { it.copy(editContent = v) } }
    fun updateEditImportance(v: Int) { _uiState.update { it.copy(editImportance = v) } }
    fun updateEditCategory(v: MemoryCategory) { _uiState.update { it.copy(editCategory = v) } }
    fun updateEditSentiment(v: Sentiment) { _uiState.update { it.copy(editSentiment = v) } }

    fun saveMemory() {
        val state = _uiState.value
        val content = state.editContent.trim()
        if (content.isBlank()) return

        viewModelScope.launch {
            val existing = state.editingMemory
            if (existing != null) {
                memoryRepository.editMemory(
                    memoryId = existing.id,
                    content = content,
                    importance = state.editImportance,
                    category = state.editCategory,
                    sentiment = state.editSentiment
                )
            } else {
                memoryRepository.remember(
                    Memory(
                        id = UUID.randomUUID().toString(),
                        characterId = characterId,
                        content = content,
                        importance = state.editImportance,
                        category = state.editCategory,
                        sentiment = state.editSentiment,
                        timestamp = Instant.now()
                    )
                )
            }
            _uiState.update { it.copy(showEditDialog = false) }
        }
    }

    // --- Delete memory (swipe) ---
    fun deleteMemory(memory: Memory) {
        viewModelScope.launch {
            memoryRepository.forget(memory.id)
        }
    }
}
