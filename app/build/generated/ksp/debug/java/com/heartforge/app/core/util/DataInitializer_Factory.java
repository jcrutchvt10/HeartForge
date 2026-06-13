package com.heartforge.app.core.util;

import com.heartforge.app.core.ai.ImageEngine;
import com.heartforge.app.core.database.CharacterDao;
import com.heartforge.app.core.database.MemoryDao;
import com.heartforge.app.core.database.MessageDao;
import com.heartforge.app.core.database.RelationshipDao;
import com.heartforge.app.core.database.StoryDao;
import com.heartforge.app.core.network.nvidia.NVIDIAApiService;
import com.heartforge.app.core.repository.SettingsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class DataInitializer_Factory implements Factory<DataInitializer> {
  private final Provider<CharacterDao> characterDaoProvider;

  private final Provider<MessageDao> messageDaoProvider;

  private final Provider<MemoryDao> memoryDaoProvider;

  private final Provider<RelationshipDao> relationshipDaoProvider;

  private final Provider<StoryDao> storyDaoProvider;

  private final Provider<ImageEngine> imageEngineProvider;

  private final Provider<NVIDIAApiService> apiServiceProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private DataInitializer_Factory(Provider<CharacterDao> characterDaoProvider,
      Provider<MessageDao> messageDaoProvider, Provider<MemoryDao> memoryDaoProvider,
      Provider<RelationshipDao> relationshipDaoProvider, Provider<StoryDao> storyDaoProvider,
      Provider<ImageEngine> imageEngineProvider, Provider<NVIDIAApiService> apiServiceProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.characterDaoProvider = characterDaoProvider;
    this.messageDaoProvider = messageDaoProvider;
    this.memoryDaoProvider = memoryDaoProvider;
    this.relationshipDaoProvider = relationshipDaoProvider;
    this.storyDaoProvider = storyDaoProvider;
    this.imageEngineProvider = imageEngineProvider;
    this.apiServiceProvider = apiServiceProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public DataInitializer get() {
    return newInstance(characterDaoProvider.get(), messageDaoProvider.get(), memoryDaoProvider.get(), relationshipDaoProvider.get(), storyDaoProvider.get(), imageEngineProvider.get(), apiServiceProvider.get(), settingsRepositoryProvider.get());
  }

  public static DataInitializer_Factory create(Provider<CharacterDao> characterDaoProvider,
      Provider<MessageDao> messageDaoProvider, Provider<MemoryDao> memoryDaoProvider,
      Provider<RelationshipDao> relationshipDaoProvider, Provider<StoryDao> storyDaoProvider,
      Provider<ImageEngine> imageEngineProvider, Provider<NVIDIAApiService> apiServiceProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new DataInitializer_Factory(characterDaoProvider, messageDaoProvider, memoryDaoProvider, relationshipDaoProvider, storyDaoProvider, imageEngineProvider, apiServiceProvider, settingsRepositoryProvider);
  }

  public static DataInitializer newInstance(CharacterDao characterDao, MessageDao messageDao,
      MemoryDao memoryDao, RelationshipDao relationshipDao, StoryDao storyDao,
      ImageEngine imageEngine, NVIDIAApiService apiService, SettingsRepository settingsRepository) {
    return new DataInitializer(characterDao, messageDao, memoryDao, relationshipDao, storyDao, imageEngine, apiService, settingsRepository);
  }
}
