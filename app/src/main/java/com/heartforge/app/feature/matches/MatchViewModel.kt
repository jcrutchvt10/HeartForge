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
    private val dataInitializer: DataInitializer
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchState())
    val uiState: StateFlow<MatchState> = _uiState.asStateFlow()

    init {
        loadProfiles()
    }

    private fun loadProfiles() {
        viewModelScope.launch {
            val characters = characterRepository.getCharacters()
            val user = dataInitializer.getMockUserProfile()
            
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
            val user = dataInitializer.getMockUserProfile()
            val audit = matchmakingEngine.calculateAIDrivenCompatibility(user, profile.character)
            
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
        // Logic for liking
        removeProfile(profile)
    }

    fun onPass(profile: MatchProfile) {
        // Logic for passing
        removeProfile(profile)
    }

    private fun removeProfile(profile: MatchProfile) {
        _uiState.update { it.copy(currentProfiles = it.currentProfiles.filter { p -> p != profile }) }
    }
}
