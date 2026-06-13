package com.heartforge.app.feature.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.heartforge.app.core.repository.SettingsRepository
import com.heartforge.app.core.worker.ProactiveNudgeWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState(
    val apiKey: String = "",
    val editingApiKey: String = "",
    val endpoint: String = "",
    val chatModel: String = "",
    val imageModel: String = "",
    val temperature: Float = 0.7f,
    val isStreamingEnabled: Boolean = true,
    val isDarkModeEnabled: Boolean = false,
    val availableModels: List<String> = emptyList(),
    val isRefreshingModels: Boolean = false,
    val isTestingNudge: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: android.content.Context,
    private val settingsRepository: SettingsRepository,
    private val apiService: com.heartforge.app.core.network.nvidia.NVIDIAApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine<Any?, SettingsState>(
                settingsRepository.endpoint,
                settingsRepository.chatModel,
                settingsRepository.imageModel,
                settingsRepository.temperature,
                settingsRepository.isStreamingEnabled,
                settingsRepository.apiKey
            ) { args ->
                val endpoint = args[0] as String
                val chatModel = args[1] as String
                val imageModel = args[2] as String
                val temperature = args[3] as Float
                val isStreamingEnabled = args[4] as Boolean
                val apiKey = args[5] as String
                SettingsState(
                    apiKey = apiKey,
                    editingApiKey = apiKey,
                    endpoint = endpoint,
                    chatModel = chatModel,
                    imageModel = imageModel,
                    temperature = temperature,
                    isStreamingEnabled = isStreamingEnabled
                )
            }.collect { newState ->
                _uiState.update { current ->
                    newState.copy(
                        editingApiKey = if (current.editingApiKey == current.apiKey) newState.apiKey else current.editingApiKey,
                        availableModels = current.availableModels,
                        isRefreshingModels = current.isRefreshingModels
                    )
                }
                if (newState.apiKey.isNotBlank() && _uiState.value.availableModels.isEmpty()) {
                    refreshModels()
                }
            }
        }
    }

    fun updateApiKey(key: String) {
        _uiState.update { it.copy(editingApiKey = key) }
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

    fun testNudge() {
        _uiState.update { it.copy(isTestingNudge = true) }
        val workRequest = OneTimeWorkRequestBuilder<ProactiveNudgeWorker>()
            .addTag("test_nudge")
            .build()
        
        WorkManager.getInstance(context).enqueue(workRequest)
        
        viewModelScope.launch {
            kotlinx.coroutines.delay(2000)
            _uiState.update { it.copy(isTestingNudge = false) }
        }
    }

    fun logAvailableModels() {
        viewModelScope.launch {
            val apiKey = settingsRepository.getApiKey()
            if (apiKey != null) {
                val response = apiService.getModels("Bearer $apiKey")
                if (response.isSuccessful) {
                    Log.d("SettingsViewModel", "Available Models: ${response.body()?.data?.map { it.id }}")
                }
            }
        }
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
