package com.heartforge.app.di;

import com.heartforge.app.core.database.HeartForgeDatabase;
import com.heartforge.app.core.database.MessageDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideMessageDaoFactory implements Factory<MessageDao> {
  private final Provider<HeartForgeDatabase> databaseProvider;

  public DatabaseModule_ProvideMessageDaoFactory(Provider<HeartForgeDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public MessageDao get() {
    return provideMessageDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideMessageDaoFactory create(
      Provider<HeartForgeDatabase> databaseProvider) {
    return new DatabaseModule_ProvideMessageDaoFactory(databaseProvider);
  }

  public static MessageDao provideMessageDao(HeartForgeDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMessageDao(database));
  }
}
