package com.heartforge.app.feature.settings;

import android.content.Context;
import com.heartforge.app.core.network.nvidia.NVIDIAApiService;
import com.heartforge.app.core.repository.SettingsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<NVIDIAApiService> apiServiceProvider;

  private SettingsViewModel_Factory(Provider<Context> contextProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<NVIDIAApiService> apiServiceProvider) {
    this.contextProvider = contextProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(contextProvider.get(), settingsRepositoryProvider.get(), apiServiceProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<Context> contextProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<NVIDIAApiService> apiServiceProvider) {
    return new SettingsViewModel_Factory(contextProvider, settingsRepositoryProvider, apiServiceProvider);
  }

  public static SettingsViewModel newInstance(Context context,
      SettingsRepository settingsRepository, NVIDIAApiService apiService) {
    return new SettingsViewModel(context, settingsRepository, apiService);
  }
}
