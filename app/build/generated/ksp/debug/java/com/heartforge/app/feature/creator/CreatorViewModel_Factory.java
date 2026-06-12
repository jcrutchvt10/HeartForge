package com.heartforge.app.feature.creator;

import com.heartforge.app.core.ai.ImageEngine;
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
public final class CreatorViewModel_Factory implements Factory<CreatorViewModel> {
  private final Provider<CharacterRepository> characterRepositoryProvider;

  private final Provider<ImageEngine> imageEngineProvider;

  private CreatorViewModel_Factory(Provider<CharacterRepository> characterRepositoryProvider,
      Provider<ImageEngine> imageEngineProvider) {
    this.characterRepositoryProvider = characterRepositoryProvider;
    this.imageEngineProvider = imageEngineProvider;
  }

  @Override
  public CreatorViewModel get() {
    return newInstance(characterRepositoryProvider.get(), imageEngineProvider.get());
  }

  public static CreatorViewModel_Factory create(
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<ImageEngine> imageEngineProvider) {
    return new CreatorViewModel_Factory(characterRepositoryProvider, imageEngineProvider);
  }

  public static CreatorViewModel newInstance(CharacterRepository characterRepository,
      ImageEngine imageEngine) {
    return new CreatorViewModel(characterRepository, imageEngine);
  }
}
