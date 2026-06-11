package com.heartforge.app.di;

import com.heartforge.app.core.network.nvidia.NVIDIAApiService;
import com.heartforge.app.core.repository.SettingsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

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
public final class NetworkModule_ProvideNvidiaApiServiceFactory implements Factory<NVIDIAApiService> {
  private final Provider<OkHttpClient> clientProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public NetworkModule_ProvideNvidiaApiServiceFactory(Provider<OkHttpClient> clientProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.clientProvider = clientProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public NVIDIAApiService get() {
    return provideNvidiaApiService(clientProvider.get(), settingsRepositoryProvider.get());
  }

  public static NetworkModule_ProvideNvidiaApiServiceFactory create(
      Provider<OkHttpClient> clientProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new NetworkModule_ProvideNvidiaApiServiceFactory(clientProvider, settingsRepositoryProvider);
  }

  public static NVIDIAApiService provideNvidiaApiService(OkHttpClient client,
      SettingsRepository settingsRepository) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideNvidiaApiService(client, settingsRepository));
  }
}
