package com.heartforge.app.feature.stories;

import com.heartforge.app.core.repository.CharacterRepository;
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

  private final Provider<CharacterRepository> characterRepositoryProvider;

  private final Provider<RelationshipRepository> relationshipRepositoryProvider;

  private StoryViewModel_Factory(Provider<StoryRepository> storyRepositoryProvider,
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<RelationshipRepository> relationshipRepositoryProvider) {
    this.storyRepositoryProvider = storyRepositoryProvider;
    this.characterRepositoryProvider = characterRepositoryProvider;
    this.relationshipRepositoryProvider = relationshipRepositoryProvider;
  }

  @Override
  public StoryViewModel get() {
    return newInstance(storyRepositoryProvider.get(), characterRepositoryProvider.get(), relationshipRepositoryProvider.get());
  }

  public static StoryViewModel_Factory create(Provider<StoryRepository> storyRepositoryProvider,
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<RelationshipRepository> relationshipRepositoryProvider) {
    return new StoryViewModel_Factory(storyRepositoryProvider, characterRepositoryProvider, relationshipRepositoryProvider);
  }

  public static StoryViewModel newInstance(StoryRepository storyRepository,
      CharacterRepository characterRepository, RelationshipRepository relationshipRepository) {
    return new StoryViewModel(storyRepository, characterRepository, relationshipRepository);
  }
}
