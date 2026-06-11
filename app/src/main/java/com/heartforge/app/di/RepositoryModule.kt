package com.heartforge.app.di

import com.heartforge.app.core.repository.SettingsRepository
import com.heartforge.app.core.repository.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindUserProfileRepository(
        userProfileRepositoryImpl: com.heartforge.app.core.repository.UserProfileRepositoryImpl
    ): com.heartforge.app.core.repository.UserProfileRepository

    @Binds
    @Singleton
    abstract fun bindAIProvider(
        nvidiaProvider: com.heartforge.app.core.ai.nvidia.NVIDIAProvider
    ): com.heartforge.app.core.ai.AIProvider

    @Binds
    @Singleton
    abstract fun bindCharacterRepository(
        characterRepositoryImpl: com.heartforge.app.core.repository.CharacterRepositoryImpl
    ): com.heartforge.app.core.repository.CharacterRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        chatRepositoryImpl: com.heartforge.app.core.repository.ChatRepositoryImpl
    ): com.heartforge.app.core.repository.ChatRepository

    @Binds
    @Singleton
    abstract fun bindMemoryRepository(
        memoryRepositoryImpl: com.heartforge.app.core.repository.MemoryRepositoryImpl
    ): com.heartforge.app.core.repository.MemoryRepository

    @Binds
    @Singleton
    abstract fun bindRelationshipRepository(
        relationshipRepositoryImpl: com.heartforge.app.core.repository.RelationshipRepositoryImpl
    ): com.heartforge.app.core.repository.RelationshipRepository

    @Binds
    @Singleton
    abstract fun bindStoryRepository(
        storyRepositoryImpl: com.heartforge.app.core.repository.StoryRepositoryImpl
    ): com.heartforge.app.core.repository.StoryRepository
}
