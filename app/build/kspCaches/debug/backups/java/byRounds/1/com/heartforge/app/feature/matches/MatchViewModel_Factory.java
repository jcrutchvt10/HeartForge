package com.heartforge.app.feature.matches;

import com.heartforge.app.core.ai.MatchmakingEngine;
import com.heartforge.app.core.repository.CharacterRepository;
import com.heartforge.app.core.repository.UserProfileRepository;
import com.heartforge.app.core.util.DataInitializer;
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
public final class MatchViewModel_Factory implements Factory<MatchViewModel> {
  private final Provider<CharacterRepository> characterRepositoryProvider;

  private final Provider<MatchmakingEngine> matchmakingEngineProvider;

  private final Provider<DataInitializer> dataInitializerProvider;

  private final Provider<UserProfileRepository> userProfileRepositoryProvider;

  public MatchViewModel_Factory(Provider<CharacterRepository> characterRepositoryProvider,
      Provider<MatchmakingEngine> matchmakingEngineProvider,
      Provider<DataInitializer> dataInitializerProvider,
      Provider<UserProfileRepository> userProfileRepositoryProvider) {
    this.characterRepositoryProvider = characterRepositoryProvider;
    this.matchmakingEngineProvider = matchmakingEngineProvider;
    this.dataInitializerProvider = dataInitializerProvider;
    this.userProfileRepositoryProvider = userProfileRepositoryProvider;
  }

  @Override
  public MatchViewModel get() {
    return newInstance(characterRepositoryProvider.get(), matchmakingEngineProvider.get(), dataInitializerProvider.get(), userProfileRepositoryProvider.get());
  }

  public static MatchViewModel_Factory create(
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<MatchmakingEngine> matchmakingEngineProvider,
      Provider<DataInitializer> dataInitializerProvider,
      Provider<UserProfileRepository> userProfileRepositoryProvider) {
    return new MatchViewModel_Factory(characterRepositoryProvider, matchmakingEngineProvider, dataInitializerProvider, userProfileRepositoryProvider);
  }

  public static MatchViewModel newInstance(CharacterRepository characterRepository,
      MatchmakingEngine matchmakingEngine, DataInitializer dataInitializer,
      UserProfileRepository userProfileRepository) {
    return new MatchViewModel(characterRepository, matchmakingEngine, dataInitializer, userProfileRepository);
  }
}
