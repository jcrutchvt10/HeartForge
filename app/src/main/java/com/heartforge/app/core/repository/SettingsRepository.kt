package com.heartforge.app.core.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val endpoint: Flow<String>
    val chatModel: Flow<String>
    val apiKey: Flow<String>
    val imageModel: Flow<String>
    val temperature: Flow<Float>
    val isStreamingEnabled: Flow<Boolean>
    val isDarkModeEnabled: Flow<Boolean>
    val favoriteCharacterIds: Flow<Set<String>>

    suspend fun updateEndpoint(url: String)
    suspend fun updateChatModel(model: String)
    suspend fun updateImageModel(model: String)
    suspend fun updateTemperature(temp: Float)
    suspend fun updateStreaming(enabled: Boolean)
    suspend fun updateDarkMode(enabled: Boolean)
    suspend fun addFavorite(id: String)
    suspend fun removeFavorite(id: String)

    suspend fun saveApiKey(key: String)
    suspend fun getApiKey(): String?
}
