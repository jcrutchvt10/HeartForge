package com.heartforge.app.core.util;

import com.heartforge.app.core.database.CharacterDao;
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
public final class DataInitializer_Factory implements Factory<DataInitializer> {
  private final Provider<CharacterDao> characterDaoProvider;

  public DataInitializer_Factory(Provider<CharacterDao> characterDaoProvider) {
    this.characterDaoProvider = characterDaoProvider;
  }

  @Override
  public DataInitializer get() {
    return newInstance(characterDaoProvider.get());
  }

  public static DataInitializer_Factory create(Provider<CharacterDao> characterDaoProvider) {
    return new DataInitializer_Factory(characterDaoProvider);
  }

  public static DataInitializer newInstance(CharacterDao characterDao) {
    return new DataInitializer(characterDao);
  }
}
