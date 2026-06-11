package com.heartforge.app.core.repository;

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
public final class CharacterRepositoryImpl_Factory implements Factory<CharacterRepositoryImpl> {
  private final Provider<CharacterDao> characterDaoProvider;

  public CharacterRepositoryImpl_Factory(Provider<CharacterDao> characterDaoProvider) {
    this.characterDaoProvider = characterDaoProvider;
  }

  @Override
  public CharacterRepositoryImpl get() {
    return newInstance(characterDaoProvider.get());
  }

  public static CharacterRepositoryImpl_Factory create(
      Provider<CharacterDao> characterDaoProvider) {
    return new CharacterRepositoryImpl_Factory(characterDaoProvider);
  }

  public static CharacterRepositoryImpl newInstance(CharacterDao characterDao) {
    return new CharacterRepositoryImpl(characterDao);
  }
}
