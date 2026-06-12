package com.heartforge.app.di;

import com.heartforge.app.core.database.CharacterDao;
import com.heartforge.app.core.database.HeartForgeDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideCharacterDaoFactory implements Factory<CharacterDao> {
  private final Provider<HeartForgeDatabase> databaseProvider;

  private DatabaseModule_ProvideCharacterDaoFactory(Provider<HeartForgeDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public CharacterDao get() {
    return provideCharacterDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideCharacterDaoFactory create(
      Provider<HeartForgeDatabase> databaseProvider) {
    return new DatabaseModule_ProvideCharacterDaoFactory(databaseProvider);
  }

  public static CharacterDao provideCharacterDao(HeartForgeDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideCharacterDao(database));
  }
}
