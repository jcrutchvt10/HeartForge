package com.heartforge.app.feature.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heartforge.app.core.model.*
import com.heartforge.app.core.repository.CharacterRepository
import com.heartforge.app.core.repository.ChatRepository
import com.heartforge.app.core.repository.RelationshipRepository
import com.heartforge.app.core.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatState(
    val character: Character? = null,
    val relationship: Relationship? = null,
    val activeStoryChapter: StoryChapter? = null,
    val messages: List<ChatMessage> = emptyList(),
    val isAssistantTyping: Boolean = false,
    val currentInput: String = ""
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val characterRepository: CharacterRepository,
    private val relationshipRepository: RelationshipRepository,
    private val storyRepository: StoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterId: String = checkNotNull(savedStateHandle["characterId"])

    private val _uiState = MutableStateFlow(ChatState())
    val uiState: StateFlow<ChatState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val character = characterRepository.getCharacter(characterId)
            _uiState.update { it.copy(character = character) }

            launch {
                chatRepository.getMessages(characterId).collect { messages ->
                    _uiState.update { it.copy(messages = messages) }
                }
            }

            launch {
                relationshipRepository.getRelationship(characterId).collect { relationship ->
                    _uiState.update { it.copy(relationship = relationship) }
                }
            }

            launch {
                storyRepository.getProgressForCharacter(characterId).collect { progressList ->
                    val activeId = progressList.firstOrNull()?.activeChapterId
                    if (activeId != null) {
                        val arc = storyRepository.getStoriesForCharacter(characterId).first().firstOrNull()
                        val chapter = arc?.chapters?.find { it.id == activeId }
                        _uiState.update { it.copy(activeStoryChapter = chapter) }
                    } else {
                        _uiState.update { it.copy(activeStoryChapter = null) }
                    }
                }
            }
        }
    }

    fun onInputChange(input: String) {
        _uiState.update { it.copy(currentInput = input) }
    }

    fun sendMessage() {
        val message = uiState.value.currentInput
        if (message.isBlank()) return

        val storyContext = uiState.value.activeStoryChapter?.let { 
            "Current Narrative Scene: ${it.title}. Background: ${it.startingPrompt}"
        }

        _uiState.update { it.copy(currentInput = "", isAssistantTyping = true) }

        viewModelScope.launch {
            chatRepository.sendMessage(characterId, message, storyContext)
            _uiState.update { it.copy(isAssistantTyping = false) }
        }
    }

    fun retryLastMessage() {
        val messages = _uiState.value.messages
        // Find the last user message that isn't followed by a successful assistant response
        val lastUserMsg = messages.lastOrNull { it.role == MessageRole.User } ?: return
        val msgsAfter = messages.dropWhile { it.id != lastUserMsg.id }.drop(1)
        val hasAssistantResponse = msgsAfter.any { it.role == MessageRole.Assistant }
        if (!hasAssistantResponse) {
            sendMessage()
        }
    }
}
