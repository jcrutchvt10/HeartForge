package com.heartforge.app.core.ai;

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
public final class MatchmakingEngine_Factory implements Factory<MatchmakingEngine> {
  private final Provider<AIProvider> aiProvider;

  private MatchmakingEngine_Factory(Provider<AIProvider> aiProvider) {
    this.aiProvider = aiProvider;
  }

  @Override
  public MatchmakingEngine get() {
    return newInstance(aiProvider.get());
  }

  public static MatchmakingEngine_Factory create(Provider<AIProvider> aiProvider) {
    return new MatchmakingEngine_Factory(aiProvider);
  }

  public static MatchmakingEngine newInstance(AIProvider aiProvider) {
    return new MatchmakingEngine(aiProvider);
  }
}
