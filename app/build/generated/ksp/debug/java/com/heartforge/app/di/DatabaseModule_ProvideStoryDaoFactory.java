package com.heartforge.app.di;

import com.heartforge.app.core.database.HeartForgeDatabase;
import com.heartforge.app.core.database.StoryDao;
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
public final class DatabaseModule_ProvideStoryDaoFactory implements Factory<StoryDao> {
  private final Provider<HeartForgeDatabase> databaseProvider;

  private DatabaseModule_ProvideStoryDaoFactory(Provider<HeartForgeDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public StoryDao get() {
    return provideStoryDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideStoryDaoFactory create(
      Provider<HeartForgeDatabase> databaseProvider) {
    return new DatabaseModule_ProvideStoryDaoFactory(databaseProvider);
  }

  public static StoryDao provideStoryDao(HeartForgeDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideStoryDao(database));
  }
}
