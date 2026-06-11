package com.heartforge.app.feature.memories;

import androidx.lifecycle.SavedStateHandle;
import com.heartforge.app.core.database.MemoryDao;
import com.heartforge.app.core.repository.CharacterRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
    "deprecation"
})
public final class MemoryViewModel_Factory implements Factory<MemoryViewModel> {
  private final Provider<CharacterRepository> characterRepositoryProvider;

  private final Provider<MemoryDao> memoryDaoProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public MemoryViewModel_Factory(Provider<CharacterRepository> characterRepositoryProvider,
      Provider<MemoryDao> memoryDaoProvider, Provider<SavedStateHandle> savedStateHandleProvider) {
    this.characterRepositoryProvider = characterRepositoryProvider;
    this.memoryDaoProvider = memoryDaoProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public MemoryViewModel get() {
    return newInstance(characterRepositoryProvider.get(), memoryDaoProvider.get(), savedStateHandleProvider.get());
  }

  public static MemoryViewModel_Factory create(
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<MemoryDao> memoryDaoProvider, Provider<SavedStateHandle> savedStateHandleProvider) {
    return new MemoryViewModel_Factory(characterRepositoryProvider, memoryDaoProvider, savedStateHandleProvider);
  }

  public static MemoryViewModel newInstance(CharacterRepository characterRepository,
      MemoryDao memoryDao, SavedStateHandle savedStateHandle) {
    return new MemoryViewModel(characterRepository, memoryDao, savedStateHandle);
  }
}
