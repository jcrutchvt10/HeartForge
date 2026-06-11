package com.heartforge.app.core.util;

import android.content.Context;
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
public final class SecureSettings_Factory implements Factory<SecureSettings> {
  private final Provider<Context> contextProvider;

  public SecureSettings_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SecureSettings get() {
    return newInstance(contextProvider.get());
  }

  public static SecureSettings_Factory create(Provider<Context> contextProvider) {
    return new SecureSettings_Factory(contextProvider);
  }

  public static SecureSettings newInstance(Context context) {
    return new SecureSettings(context);
  }
}
