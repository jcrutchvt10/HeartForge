package com.heartforge.app.core.repository;

import com.heartforge.app.core.database.StoryDao;
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
public final class StoryRepositoryImpl_Factory implements Factory<StoryRepositoryImpl> {
  private final Provider<StoryDao> storyDaoProvider;

  public StoryRepositoryImpl_Factory(Provider<StoryDao> storyDaoProvider) {
    this.storyDaoProvider = storyDaoProvider;
  }

  @Override
  public StoryRepositoryImpl get() {
    return newInstance(storyDaoProvider.get());
  }

  public static StoryRepositoryImpl_Factory create(Provider<StoryDao> storyDaoProvider) {
    return new StoryRepositoryImpl_Factory(storyDaoProvider);
  }

  public static StoryRepositoryImpl newInstance(StoryDao storyDao) {
    return new StoryRepositoryImpl(storyDao);
  }
}
