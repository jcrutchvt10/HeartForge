package com.heartforge.app.core.ai;

import com.heartforge.app.core.repository.MemoryRepository;
import com.heartforge.app.core.repository.RelationshipRepository;
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
public final class EvolutionaryEngine_Factory implements Factory<EvolutionaryEngine> {
  private final Provider<AIProvider> aiProvider;

  private final Provider<RelationshipRepository> relationshipRepositoryProvider;

  private final Provider<MemoryRepository> memoryRepositoryProvider;

  private EvolutionaryEngine_Factory(Provider<AIProvider> aiProvider,
      Provider<RelationshipRepository> relationshipRepositoryProvider,
      Provider<MemoryRepository> memoryRepositoryProvider) {
    this.aiProvider = aiProvider;
    this.relationshipRepositoryProvider = relationshipRepositoryProvider;
    this.memoryRepositoryProvider = memoryRepositoryProvider;
  }

  @Override
  public EvolutionaryEngine get() {
    return newInstance(aiProvider.get(), relationshipRepositoryProvider.get(), memoryRepositoryProvider.get());
  }

  public static EvolutionaryEngine_Factory create(Provider<AIProvider> aiProvider,
      Provider<RelationshipRepository> relationshipRepositoryProvider,
      Provider<MemoryRepository> memoryRepositoryProvider) {
    return new EvolutionaryEngine_Factory(aiProvider, relationshipRepositoryProvider, memoryRepositoryProvider);
  }

  public static EvolutionaryEngine newInstance(AIProvider aiProvider,
      RelationshipRepository relationshipRepository, MemoryRepository memoryRepository) {
    return new EvolutionaryEngine(aiProvider, relationshipRepository, memoryRepository);
  }
}
