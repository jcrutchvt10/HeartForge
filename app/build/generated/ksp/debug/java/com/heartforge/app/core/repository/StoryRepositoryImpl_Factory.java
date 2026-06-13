package com.heartforge.app.core.repository;

import com.heartforge.app.core.ai.StoryEngine;
import com.heartforge.app.core.database.MemoryDao;
import com.heartforge.app.core.database.StoryDao;
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
public final class StoryRepositoryImpl_Factory implements Factory<StoryRepositoryImpl> {
  private final Provider<StoryDao> storyDaoProvider;

  private final Provider<StoryEngine> storyEngineProvider;

  private final Provider<MemoryDao> memoryDaoProvider;

  private final Provider<RelationshipRepository> relationshipRepositoryProvider;

  private final Provider<UserProfileRepository> userProfileRepositoryProvider;

  private StoryRepositoryImpl_Factory(Provider<StoryDao> storyDaoProvider,
      Provider<StoryEngine> storyEngineProvider, Provider<MemoryDao> memoryDaoProvider,
      Provider<RelationshipRepository> relationshipRepositoryProvider,
      Provider<UserProfileRepository> userProfileRepositoryProvider) {
    this.storyDaoProvider = storyDaoProvider;
    this.storyEngineProvider = storyEngineProvider;
    this.memoryDaoProvider = memoryDaoProvider;
    this.relationshipRepositoryProvider = relationshipRepositoryProvider;
    this.userProfileRepositoryProvider = userProfileRepositoryProvider;
  }

  @Override
  public StoryRepositoryImpl get() {
    return newInstance(storyDaoProvider.get(), storyEngineProvider.get(), memoryDaoProvider.get(), relationshipRepositoryProvider.get(), userProfileRepositoryProvider.get());
  }

  public static StoryRepositoryImpl_Factory create(Provider<StoryDao> storyDaoProvider,
      Provider<StoryEngine> storyEngineProvider, Provider<MemoryDao> memoryDaoProvider,
      Provider<RelationshipRepository> relationshipRepositoryProvider,
      Provider<UserProfileRepository> userProfileRepositoryProvider) {
    return new StoryRepositoryImpl_Factory(storyDaoProvider, storyEngineProvider, memoryDaoProvider, relationshipRepositoryProvider, userProfileRepositoryProvider);
  }

  public static StoryRepositoryImpl newInstance(StoryDao storyDao, StoryEngine storyEngine,
      MemoryDao memoryDao, RelationshipRepository relationshipRepository,
      UserProfileRepository userProfileRepository) {
    return new StoryRepositoryImpl(storyDao, storyEngine, memoryDao, relationshipRepository, userProfileRepository);
  }
}
