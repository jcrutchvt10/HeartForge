package com.heartforge.app.feature.stories;

import com.heartforge.app.core.repository.RelationshipRepository;
import com.heartforge.app.core.repository.StoryRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class StoryViewModel_Factory implements Factory<StoryViewModel> {
  private final Provider<StoryRepository> storyRepositoryProvider;

  private final Provider<RelationshipRepository> relationshipRepositoryProvider;

  private StoryViewModel_Factory(Provider<StoryRepository> storyRepositoryProvider,
      Provider<RelationshipRepository> relationshipRepositoryProvider) {
    this.storyRepositoryProvider = storyRepositoryProvider;
    this.relationshipRepositoryProvider = relationshipRepositoryProvider;
  }

  @Override
  public StoryViewModel get() {
    return newInstance(storyRepositoryProvider.get(), relationshipRepositoryProvider.get());
  }

  public static StoryViewModel_Factory create(Provider<StoryRepository> storyRepositoryProvider,
      Provider<RelationshipRepository> relationshipRepositoryProvider) {
    return new StoryViewModel_Factory(storyRepositoryProvider, relationshipRepositoryProvider);
  }

  public static StoryViewModel newInstance(StoryRepository storyRepository,
      RelationshipRepository relationshipRepository) {
    return new StoryViewModel(storyRepository, relationshipRepository);
  }
}
