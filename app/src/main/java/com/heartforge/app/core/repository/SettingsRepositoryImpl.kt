package com.heartforge.app.core.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.heartforge.app.BuildConfig
import com.heartforge.app.core.util.SecureSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val secureSettings: SecureSettings
) : SettingsRepository {

    private val _apiKeyFlow = MutableStateFlow(secureSettings.getApiKey() ?: BuildConfig.NVIDIA_API_KEY)
    override val apiKey: Flow<String> = _apiKeyFlow.asStateFlow()

    override val endpoint: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_ENDPOINT] ?: "https://integrate.api.nvidia.com/v1"
    }

    override val chatModel: Flow<String> = context.dataStore.data.map { preferences ->
        val current = preferences[KEY_CHAT_MODEL]
        if (current == null || current.contains("llama") || current.contains("step-3.5")) {
            "stepfun-ai/step-3.7-flash"
        } else {
            current
        }
    }

    override val imageModel: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_IMAGE_MODEL] ?: "black-forest-labs/flux-1-dev"
    }

    override val temperature: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[KEY_TEMPERATURE] ?: 0.7f
    }

    override val isStreamingEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_STREAMING] ?: true
    }

    override suspend fun updateEndpoint(url: String) {
        context.dataStore.edit { it[KEY_ENDPOINT] = url }
    }

    override suspend fun updateChatModel(model: String) {
        context.dataStore.edit { it[KEY_CHAT_MODEL] = model }
    }

    override suspend fun updateImageModel(model: String) {
        context.dataStore.edit { it[KEY_IMAGE_MODEL] = model }
    }

    override suspend fun updateTemperature(temp: Float) {
        context.dataStore.edit { it[KEY_TEMPERATURE] = temp }
    }

    override suspend fun updateStreaming(enabled: Boolean) {
        context.dataStore.edit { it[KEY_STREAMING] = enabled }
    }

    override suspend fun saveApiKey(key: String) {
        secureSettings.saveApiKey(key)
        _apiKeyFlow.value = key
    }

    override suspend fun getApiKey(): String? {
        return secureSettings.getApiKey() ?: BuildConfig.NVIDIA_API_KEY.takeIf { it.isNotBlank() }
    }

    companion object {
        private val KEY_ENDPOINT = stringPreferencesKey("endpoint")
        private val KEY_CHAT_MODEL = stringPreferencesKey("chat_model")
        private val KEY_IMAGE_MODEL = stringPreferencesKey("image_model")
        private val KEY_TEMPERATURE = floatPreferencesKey("temperature")
        private val KEY_STREAMING = booleanPreferencesKey("streaming")
    }
}
