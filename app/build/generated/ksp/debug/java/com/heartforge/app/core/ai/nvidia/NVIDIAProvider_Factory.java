package com.heartforge.app.core.ai.nvidia;

import com.heartforge.app.core.network.nvidia.NVIDIAApiService;
import com.heartforge.app.core.network.nvidia.NVIDIAImageApiService;
import com.heartforge.app.core.repository.SettingsRepository;
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
public final class NVIDIAProvider_Factory implements Factory<NVIDIAProvider> {
  private final Provider<NVIDIAApiService> apiServiceProvider;

  private final Provider<NVIDIAImageApiService> imageApiServiceProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

<<<<<<< Updated upstream
  public NVIDIAProvider_Factory(Provider<NVIDIAApiService> apiServiceProvider,
      Provider<NVIDIAImageApiService> imageApiServiceProvider,
=======
  private NVIDIAProvider_Factory(Provider<NVIDIAApiService> apiServiceProvider,
>>>>>>> Stashed changes
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.apiServiceProvider = apiServiceProvider;
    this.imageApiServiceProvider = imageApiServiceProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public NVIDIAProvider get() {
    return newInstance(apiServiceProvider.get(), imageApiServiceProvider.get(), settingsRepositoryProvider.get());
  }

  public static NVIDIAProvider_Factory create(Provider<NVIDIAApiService> apiServiceProvider,
      Provider<NVIDIAImageApiService> imageApiServiceProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new NVIDIAProvider_Factory(apiServiceProvider, imageApiServiceProvider, settingsRepositoryProvider);
  }

  public static NVIDIAProvider newInstance(NVIDIAApiService apiService,
      NVIDIAImageApiService imageApiService, SettingsRepository settingsRepository) {
    return new NVIDIAProvider(apiService, imageApiService, settingsRepository);
  }
}
