package com.heartforge.app.core.ai.nvidia;

import com.heartforge.app.core.network.nvidia.NVIDIAApiService;
import com.heartforge.app.core.repository.SettingsRepository;
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
public final class NVIDIAProvider_Factory implements Factory<NVIDIAProvider> {
  private final Provider<NVIDIAApiService> apiServiceProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public NVIDIAProvider_Factory(Provider<NVIDIAApiService> apiServiceProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.apiServiceProvider = apiServiceProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public NVIDIAProvider get() {
    return newInstance(apiServiceProvider.get(), settingsRepositoryProvider.get());
  }

  public static NVIDIAProvider_Factory create(Provider<NVIDIAApiService> apiServiceProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new NVIDIAProvider_Factory(apiServiceProvider, settingsRepositoryProvider);
  }

  public static NVIDIAProvider newInstance(NVIDIAApiService apiService,
      SettingsRepository settingsRepository) {
    return new NVIDIAProvider(apiService, settingsRepository);
  }
}
