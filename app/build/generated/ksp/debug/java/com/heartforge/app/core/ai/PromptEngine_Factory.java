package com.heartforge.app.core.ai;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
    "deprecation"
})
public final class PromptEngine_Factory implements Factory<PromptEngine> {
  @Override
  public PromptEngine get() {
    return newInstance();
  }

  public static PromptEngine_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PromptEngine newInstance() {
    return new PromptEngine();
  }

  private static final class InstanceHolder {
    private static final PromptEngine_Factory INSTANCE = new PromptEngine_Factory();
  }
}
