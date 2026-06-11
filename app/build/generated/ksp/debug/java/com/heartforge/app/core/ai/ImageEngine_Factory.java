package com.heartforge.app.core.ai;

import com.heartforge.app.core.util.ImageStorage;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
    "deprecation"
})
public final class ImageEngine_Factory implements Factory<ImageEngine> {
  private final Provider<AIProvider> aiProvider;

  private final Provider<ImageStorage> imageStorageProvider;

  public ImageEngine_Factory(Provider<AIProvider> aiProvider,
      Provider<ImageStorage> imageStorageProvider) {
    this.aiProvider = aiProvider;
    this.imageStorageProvider = imageStorageProvider;
  }

  @Override
  public ImageEngine get() {
    return newInstance(aiProvider.get(), imageStorageProvider.get());
  }

  public static ImageEngine_Factory create(Provider<AIProvider> aiProvider,
      Provider<ImageStorage> imageStorageProvider) {
    return new ImageEngine_Factory(aiProvider, imageStorageProvider);
  }

  public static ImageEngine newInstance(AIProvider aiProvider, ImageStorage imageStorage) {
    return new ImageEngine(aiProvider, imageStorage);
  }
}
