package com.heartforge.app.feature.matches;

import androidx.lifecycle.SavedStateHandle;
import com.heartforge.app.core.repository.CharacterRepository;
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
public final class CharacterProfileViewModel_Factory implements Factory<CharacterProfileViewModel> {
  private final Provider<CharacterRepository> characterRepositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private CharacterProfileViewModel_Factory(
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.characterRepositoryProvider = characterRepositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public CharacterProfileViewModel get() {
    return newInstance(characterRepositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static CharacterProfileViewModel_Factory create(
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new CharacterProfileViewModel_Factory(characterRepositoryProvider, savedStateHandleProvider);
  }

  public static CharacterProfileViewModel newInstance(CharacterRepository characterRepository,
      SavedStateHandle savedStateHandle) {
    return new CharacterProfileViewModel(characterRepository, savedStateHandle);
  }
}
