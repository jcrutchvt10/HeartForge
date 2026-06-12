package com.heartforge.app.core.repository;

import com.heartforge.app.core.database.RelationshipDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class RelationshipRepositoryImpl_Factory implements Factory<RelationshipRepositoryImpl> {
  private final Provider<RelationshipDao> relationshipDaoProvider;

  private RelationshipRepositoryImpl_Factory(Provider<RelationshipDao> relationshipDaoProvider) {
    this.relationshipDaoProvider = relationshipDaoProvider;
  }

  @Override
  public RelationshipRepositoryImpl get() {
    return newInstance(relationshipDaoProvider.get());
  }

  public static RelationshipRepositoryImpl_Factory create(
      Provider<RelationshipDao> relationshipDaoProvider) {
    return new RelationshipRepositoryImpl_Factory(relationshipDaoProvider);
  }

  public static RelationshipRepositoryImpl newInstance(RelationshipDao relationshipDao) {
    return new RelationshipRepositoryImpl(relationshipDao);
  }
}
