package com.heartforge.app.feature.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heartforge.app.core.ai.MatchmakingEngine
import com.heartforge.app.core.model.Character
import com.heartforge.app.core.repository.CharacterRepository
import com.heartforge.app.core.util.DataInitializer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MatchState(
    val currentProfiles: List<MatchProfile> = emptyList(),
    val history: List<MatchProfile> = emptyList(),
    val swipeDirection: Int = 1,
    val isLoading: Boolean = true
)

data class MatchProfile(
    val character: Character,
    val compatibilityScore: Int,
    val aiAudit: com.heartforge.app.core.ai.MatchmakingEngine.MatchmakingAudit? = null
)

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val matchmakingEngine: MatchmakingEngine,
    private val dataInitializer: DataInitializer,
    private val userProfileRepository: com.heartforge.app.core.repository.UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchState())
    val uiState: StateFlow<MatchState> = _uiState.asStateFlow()

    init {
        loadProfiles()
    }

    private fun loadProfiles() {
        viewModelScope.launch {
            val characters = characterRepository.getCharacters()
            val user = userProfileRepository.getProfile()
            
            // Refined heuristic match for initial stack
            val profiles = characters.map { 
                MatchProfile(it, matchmakingEngine.calculateHeuristicCompatibility(user, it))
            }.sortedByDescending { it.compatibilityScore }

            _uiState.value = MatchState(
                currentProfiles = profiles,
                isLoading = false
            )
            
            // Trigger AI audit for top profile
            profiles.firstOrNull()?.let { performAiAudit(it) }
        }
    }

    private fun performAiAudit(profile: MatchProfile) {
        viewModelScope.launch {
            val user = userProfileRepository.getProfile()
            val audit = matchmakingEngine.calculateAIDrivenCompatibility(user, profile.character)
            
            // Only update if audit has actual content (skip silent fallback audits)
            if (audit.summary.isBlank()) return@launch
            
            _uiState.update { state ->
                val updatedProfiles = state.currentProfiles.map { p ->
                    if (p.character.id == profile.character.id) {
                        p.copy(compatibilityScore = audit.score, aiAudit = audit)
                    } else p
                }
                state.copy(currentProfiles = updatedProfiles)
            }
        }
    }

    fun onLike(profile: MatchProfile) {
        _uiState.update {
            it.copy(
                currentProfiles = it.currentProfiles.filter { p -> p != profile },
                history = it.history + profile,
                swipeDirection = 1
            )
        }
    }

    fun onPass(profile: MatchProfile) {
        val historyProfile = _uiState.value.history.lastOrNull()
        if (historyProfile != null) {
            _uiState.update {
                it.copy(
                    currentProfiles = listOf(historyProfile) + it.currentProfiles,
                    history = it.history.dropLast(1),
                    swipeDirection = -1
                )
            }
        }
    }
}
