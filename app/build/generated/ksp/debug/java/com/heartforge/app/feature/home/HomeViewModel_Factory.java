package com.heartforge.app.feature.home;

import com.heartforge.app.core.database.MemoryDao;
import com.heartforge.app.core.repository.CharacterRepository;
import com.heartforge.app.core.repository.SettingsRepository;
import com.heartforge.app.core.util.DataInitializer;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<CharacterRepository> characterRepositoryProvider;

  private final Provider<DataInitializer> dataInitializerProvider;

  private final Provider<MemoryDao> memoryDaoProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private HomeViewModel_Factory(Provider<CharacterRepository> characterRepositoryProvider,
      Provider<DataInitializer> dataInitializerProvider, Provider<MemoryDao> memoryDaoProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.characterRepositoryProvider = characterRepositoryProvider;
    this.dataInitializerProvider = dataInitializerProvider;
    this.memoryDaoProvider = memoryDaoProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(characterRepositoryProvider.get(), dataInitializerProvider.get(), memoryDaoProvider.get(), settingsRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<DataInitializer> dataInitializerProvider, Provider<MemoryDao> memoryDaoProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new HomeViewModel_Factory(characterRepositoryProvider, dataInitializerProvider, memoryDaoProvider, settingsRepositoryProvider);
  }

  public static HomeViewModel newInstance(CharacterRepository characterRepository,
      DataInitializer dataInitializer, MemoryDao memoryDao, SettingsRepository settingsRepository) {
    return new HomeViewModel(characterRepository, dataInitializer, memoryDao, settingsRepository);
  }
}
