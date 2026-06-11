package com.heartforge.app.feature.settings;

import com.heartforge.app.core.network.nvidia.NVIDIAApiService;
import com.heartforge.app.core.repository.SettingsRepository;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<NVIDIAApiService> apiServiceProvider;

  public SettingsViewModel_Factory(Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<NVIDIAApiService> apiServiceProvider) {
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(settingsRepositoryProvider.get(), apiServiceProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<NVIDIAApiService> apiServiceProvider) {
    return new SettingsViewModel_Factory(settingsRepositoryProvider, apiServiceProvider);
  }

  public static SettingsViewModel newInstance(SettingsRepository settingsRepository,
      NVIDIAApiService apiService) {
    return new SettingsViewModel(settingsRepository, apiService);
  }
}
