package com.heartforge.app.feature.gallery;

import com.heartforge.app.core.ai.CasualPhotoGenerator;
import com.heartforge.app.core.ai.NSFWGenerator;
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

  private final Provider<NSFWGenerator> nsfwGeneratorProvider;

  private final Provider<CasualPhotoGenerator> casualPhotoGeneratorProvider;

  public GalleryViewModel_Factory(Provider<CharacterRepository> characterRepositoryProvider,
      Provider<NSFWGenerator> nsfwGeneratorProvider,
      Provider<CasualPhotoGenerator> casualPhotoGeneratorProvider) {
    this.characterRepositoryProvider = characterRepositoryProvider;
    this.nsfwGeneratorProvider = nsfwGeneratorProvider;
    this.casualPhotoGeneratorProvider = casualPhotoGeneratorProvider;
  }

  @Override
  public GalleryViewModel get() {
    return newInstance(characterRepositoryProvider.get(), nsfwGeneratorProvider.get(), casualPhotoGeneratorProvider.get());
  }

  public static GalleryViewModel_Factory create(
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<NSFWGenerator> nsfwGeneratorProvider,
      Provider<CasualPhotoGenerator> casualPhotoGeneratorProvider) {
    return new GalleryViewModel_Factory(characterRepositoryProvider, nsfwGeneratorProvider, casualPhotoGeneratorProvider);
  }

  public static GalleryViewModel newInstance(CharacterRepository characterRepository,
      NSFWGenerator nsfwGenerator, CasualPhotoGenerator casualPhotoGenerator) {
    return new GalleryViewModel(characterRepository, nsfwGenerator, casualPhotoGenerator);
  }
}
