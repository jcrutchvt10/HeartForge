package com.heartforge.app.di;

import com.heartforge.app.core.database.HeartForgeDatabase;
import com.heartforge.app.core.database.MemoryDao;
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
public final class DatabaseModule_ProvideMemoryDaoFactory implements Factory<MemoryDao> {
  private final Provider<HeartForgeDatabase> databaseProvider;

  private DatabaseModule_ProvideMemoryDaoFactory(Provider<HeartForgeDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public MemoryDao get() {
    return provideMemoryDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideMemoryDaoFactory create(
      Provider<HeartForgeDatabase> databaseProvider) {
    return new DatabaseModule_ProvideMemoryDaoFactory(databaseProvider);
  }

  public static MemoryDao provideMemoryDao(HeartForgeDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMemoryDao(database));
  }
}
