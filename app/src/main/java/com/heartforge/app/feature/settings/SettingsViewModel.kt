package com.heartforge.app.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heartforge.app.core.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState(
    val apiKey: String = "",
    val endpoint: String = "",
    val chatModel: String = "",
    val imageModel: String = "",
    val temperature: Float = 0.7f,
    val isStreamingEnabled: Boolean = true,
    val availableModels: List<String> = emptyList(),
    val isRefreshingModels: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val apiService: com.heartforge.app.core.network.nvidia.NVIDIAApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                settingsRepository.endpoint,
                settingsRepository.chatModel,
                settingsRepository.imageModel,
                settingsRepository.temperature,
                settingsRepository.isStreamingEnabled
            ) { endpoint, chatModel, imageModel, temp, streaming ->
                SettingsState(
                    apiKey = settingsRepository.getApiKey() ?: "",
                    endpoint = endpoint,
                    chatModel = chatModel,
                    imageModel = imageModel,
                    temperature = temp,
                    isStreamingEnabled = streaming
                )
            }.collect {
                _uiState.value = it
            }
        }
    }

    fun updateApiKey(key: String) {
        viewModelScope.launch { settingsRepository.saveApiKey(key) }
    }

    fun updateEndpoint(url: String) {
        viewModelScope.launch { settingsRepository.updateEndpoint(url) }
    }

    fun updateChatModel(model: String) {
        viewModelScope.launch { settingsRepository.updateChatModel(model) }
    }

    fun updateImageModel(model: String) {
        viewModelScope.launch { settingsRepository.updateImageModel(model) }
    }

    fun updateTemperature(temp: Float) {
        viewModelScope.launch { settingsRepository.updateTemperature(temp) }
    }

    fun updateStreaming(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateStreaming(enabled) }
    }

    fun refreshModels() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshingModels = true) }
            val apiKey = settingsRepository.getApiKey()
            if (apiKey != null) {
                try {
                    val response = apiService.getModels("Bearer $apiKey")
                    if (response.isSuccessful) {
                        val models = response.body()?.data?.map { it.id } ?: emptyList()
                        _uiState.update { it.copy(availableModels = models, isRefreshingModels = false) }
                    } else {
                        _uiState.update { it.copy(isRefreshingModels = false) }
                    }
                } catch (e: Exception) {
                    _uiState.update { it.copy(isRefreshingModels = false) }
                }
            } else {
                _uiState.update { it.copy(isRefreshingModels = false) }
            }
        }
    }
}
