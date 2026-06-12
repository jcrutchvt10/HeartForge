package com.heartforge.app.feature.memories;

import androidx.lifecycle.SavedStateHandle;
import com.heartforge.app.core.database.MemoryDao;
import com.heartforge.app.core.repository.CharacterRepository;
import com.heartforge.app.core.repository.MemoryRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class MemoryViewModel_Factory implements Factory<MemoryViewModel> {
  private final Provider<CharacterRepository> characterRepositoryProvider;

  private final Provider<MemoryDao> memoryDaoProvider;

  private final Provider<MemoryRepository> memoryRepositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

<<<<<<< Updated upstream
  public MemoryViewModel_Factory(Provider<CharacterRepository> characterRepositoryProvider,
      Provider<MemoryDao> memoryDaoProvider, Provider<MemoryRepository> memoryRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
=======
  private MemoryViewModel_Factory(Provider<CharacterRepository> characterRepositoryProvider,
      Provider<MemoryDao> memoryDaoProvider, Provider<SavedStateHandle> savedStateHandleProvider) {
>>>>>>> Stashed changes
    this.characterRepositoryProvider = characterRepositoryProvider;
    this.memoryDaoProvider = memoryDaoProvider;
    this.memoryRepositoryProvider = memoryRepositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public MemoryViewModel get() {
    return newInstance(characterRepositoryProvider.get(), memoryDaoProvider.get(), memoryRepositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static MemoryViewModel_Factory create(
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<MemoryDao> memoryDaoProvider, Provider<MemoryRepository> memoryRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new MemoryViewModel_Factory(characterRepositoryProvider, memoryDaoProvider, memoryRepositoryProvider, savedStateHandleProvider);
  }

  public static MemoryViewModel newInstance(CharacterRepository characterRepository,
      MemoryDao memoryDao, MemoryRepository memoryRepository, SavedStateHandle savedStateHandle) {
    return new MemoryViewModel(characterRepository, memoryDao, memoryRepository, savedStateHandle);
  }
}
