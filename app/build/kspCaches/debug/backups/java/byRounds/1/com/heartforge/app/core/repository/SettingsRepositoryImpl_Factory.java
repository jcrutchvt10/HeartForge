package com.heartforge.app.core.repository;

import android.content.Context;
import com.heartforge.app.core.util.SecureSettings;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
    "deprecation"
})
public final class SettingsRepositoryImpl_Factory implements Factory<SettingsRepositoryImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<SecureSettings> secureSettingsProvider;

  public SettingsRepositoryImpl_Factory(Provider<Context> contextProvider,
      Provider<SecureSettings> secureSettingsProvider) {
    this.contextProvider = contextProvider;
    this.secureSettingsProvider = secureSettingsProvider;
  }

  @Override
  public SettingsRepositoryImpl get() {
    return newInstance(contextProvider.get(), secureSettingsProvider.get());
  }

  public static SettingsRepositoryImpl_Factory create(Provider<Context> contextProvider,
      Provider<SecureSettings> secureSettingsProvider) {
    return new SettingsRepositoryImpl_Factory(contextProvider, secureSettingsProvider);
  }

  public static SettingsRepositoryImpl newInstance(Context context, SecureSettings secureSettings) {
    return new SettingsRepositoryImpl(context, secureSettings);
  }
}
