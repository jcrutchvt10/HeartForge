package com.heartforge.app.feature.gallery;

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
public final class GalleryViewModel_Factory implements Factory<GalleryViewModel> {
  private final Provider<CharacterRepository> characterRepositoryProvider;

  public GalleryViewModel_Factory(Provider<CharacterRepository> characterRepositoryProvider) {
    this.characterRepositoryProvider = characterRepositoryProvider;
  }

  @Override
  public GalleryViewModel get() {
    return newInstance(characterRepositoryProvider.get());
  }

  public static GalleryViewModel_Factory create(
      Provider<CharacterRepository> characterRepositoryProvider) {
    return new GalleryViewModel_Factory(characterRepositoryProvider);
  }

  public static GalleryViewModel newInstance(CharacterRepository characterRepository) {
    return new GalleryViewModel(characterRepository);
  }
}
