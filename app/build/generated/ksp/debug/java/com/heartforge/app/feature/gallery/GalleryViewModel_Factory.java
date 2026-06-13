package com.heartforge.app.feature.gallery;

import androidx.lifecycle.SavedStateHandle;
import com.heartforge.app.core.ai.CasualPhotoGenerator;
import com.heartforge.app.core.ai.NSFWGenerator;
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
public final class GalleryViewModel_Factory implements Factory<GalleryViewModel> {
  private final Provider<CharacterRepository> characterRepositoryProvider;

  private final Provider<NSFWGenerator> nsfwGeneratorProvider;

  private final Provider<CasualPhotoGenerator> casualPhotoGeneratorProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private GalleryViewModel_Factory(Provider<CharacterRepository> characterRepositoryProvider,
      Provider<NSFWGenerator> nsfwGeneratorProvider,
      Provider<CasualPhotoGenerator> casualPhotoGeneratorProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.characterRepositoryProvider = characterRepositoryProvider;
    this.nsfwGeneratorProvider = nsfwGeneratorProvider;
    this.casualPhotoGeneratorProvider = casualPhotoGeneratorProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public GalleryViewModel get() {
    return newInstance(characterRepositoryProvider.get(), nsfwGeneratorProvider.get(), casualPhotoGeneratorProvider.get(), savedStateHandleProvider.get());
  }

  public static GalleryViewModel_Factory create(
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<NSFWGenerator> nsfwGeneratorProvider,
      Provider<CasualPhotoGenerator> casualPhotoGeneratorProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new GalleryViewModel_Factory(characterRepositoryProvider, nsfwGeneratorProvider, casualPhotoGeneratorProvider, savedStateHandleProvider);
  }

  public static GalleryViewModel newInstance(CharacterRepository characterRepository,
      NSFWGenerator nsfwGenerator, CasualPhotoGenerator casualPhotoGenerator,
      SavedStateHandle savedStateHandle) {
    return new GalleryViewModel(characterRepository, nsfwGenerator, casualPhotoGenerator, savedStateHandle);
  }
}
