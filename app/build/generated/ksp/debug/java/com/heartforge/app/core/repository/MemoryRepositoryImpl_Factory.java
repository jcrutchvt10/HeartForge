package com.heartforge.app.core.repository;

import com.heartforge.app.core.ai.AIProvider;
import com.heartforge.app.core.database.MemoryDao;
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
public final class MemoryRepositoryImpl_Factory implements Factory<MemoryRepositoryImpl> {
  private final Provider<MemoryDao> memoryDaoProvider;

  private final Provider<AIProvider> aiProvider;

  private final Provider<CharacterRepository> characterRepositoryProvider;

  private final Provider<UserProfileRepository> userProfileRepositoryProvider;

  private MemoryRepositoryImpl_Factory(Provider<MemoryDao> memoryDaoProvider,
      Provider<AIProvider> aiProvider, Provider<CharacterRepository> characterRepositoryProvider,
      Provider<UserProfileRepository> userProfileRepositoryProvider) {
    this.memoryDaoProvider = memoryDaoProvider;
    this.aiProvider = aiProvider;
    this.characterRepositoryProvider = characterRepositoryProvider;
    this.userProfileRepositoryProvider = userProfileRepositoryProvider;
  }

  @Override
  public MemoryRepositoryImpl get() {
    return newInstance(memoryDaoProvider.get(), aiProvider.get(), characterRepositoryProvider.get(), userProfileRepositoryProvider.get());
  }

  public static MemoryRepositoryImpl_Factory create(Provider<MemoryDao> memoryDaoProvider,
      Provider<AIProvider> aiProvider, Provider<CharacterRepository> characterRepositoryProvider,
      Provider<UserProfileRepository> userProfileRepositoryProvider) {
    return new MemoryRepositoryImpl_Factory(memoryDaoProvider, aiProvider, characterRepositoryProvider, userProfileRepositoryProvider);
  }

  public static MemoryRepositoryImpl newInstance(MemoryDao memoryDao, AIProvider aiProvider,
      CharacterRepository characterRepository, UserProfileRepository userProfileRepository) {
    return new MemoryRepositoryImpl(memoryDao, aiProvider, characterRepository, userProfileRepository);
  }
}
