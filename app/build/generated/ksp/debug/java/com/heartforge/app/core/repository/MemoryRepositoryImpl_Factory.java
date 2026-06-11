package com.heartforge.app.core.repository;

import com.heartforge.app.core.database.MemoryDao;
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
public final class MemoryRepositoryImpl_Factory implements Factory<MemoryRepositoryImpl> {
  private final Provider<MemoryDao> memoryDaoProvider;

  public MemoryRepositoryImpl_Factory(Provider<MemoryDao> memoryDaoProvider) {
    this.memoryDaoProvider = memoryDaoProvider;
  }

  @Override
  public MemoryRepositoryImpl get() {
    return newInstance(memoryDaoProvider.get());
  }

  public static MemoryRepositoryImpl_Factory create(Provider<MemoryDao> memoryDaoProvider) {
    return new MemoryRepositoryImpl_Factory(memoryDaoProvider);
  }

  public static MemoryRepositoryImpl newInstance(MemoryDao memoryDao) {
    return new MemoryRepositoryImpl(memoryDao);
  }
}
