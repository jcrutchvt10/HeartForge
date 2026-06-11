package com.heartforge.app.core.repository;

import com.heartforge.app.core.ai.AIProvider;
import com.heartforge.app.core.ai.EvolutionaryEngine;
import com.heartforge.app.core.ai.ImageEngine;
import com.heartforge.app.core.ai.PromptEngine;
import com.heartforge.app.core.database.MemoryDao;
import com.heartforge.app.core.database.MessageDao;
import com.heartforge.app.core.util.DataInitializer;
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
public final class ChatRepositoryImpl_Factory implements Factory<ChatRepositoryImpl> {
  private final Provider<MessageDao> messageDaoProvider;

  private final Provider<AIProvider> aiProvider;

  private final Provider<PromptEngine> promptEngineProvider;

  private final Provider<EvolutionaryEngine> evolutionaryEngineProvider;

  private final Provider<CharacterRepository> characterRepositoryProvider;

  private final Provider<RelationshipRepository> relationshipRepositoryProvider;

  private final Provider<DataInitializer> dataInitializerProvider;

  private final Provider<MemoryDao> memoryDaoProvider;

  private final Provider<ImageEngine> imageEngineProvider;

  public ChatRepositoryImpl_Factory(Provider<MessageDao> messageDaoProvider,
      Provider<AIProvider> aiProvider, Provider<PromptEngine> promptEngineProvider,
      Provider<EvolutionaryEngine> evolutionaryEngineProvider,
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<RelationshipRepository> relationshipRepositoryProvider,
      Provider<DataInitializer> dataInitializerProvider, Provider<MemoryDao> memoryDaoProvider,
      Provider<ImageEngine> imageEngineProvider) {
    this.messageDaoProvider = messageDaoProvider;
    this.aiProvider = aiProvider;
    this.promptEngineProvider = promptEngineProvider;
    this.evolutionaryEngineProvider = evolutionaryEngineProvider;
    this.characterRepositoryProvider = characterRepositoryProvider;
    this.relationshipRepositoryProvider = relationshipRepositoryProvider;
    this.dataInitializerProvider = dataInitializerProvider;
    this.memoryDaoProvider = memoryDaoProvider;
    this.imageEngineProvider = imageEngineProvider;
  }

  @Override
  public ChatRepositoryImpl get() {
    return newInstance(messageDaoProvider.get(), aiProvider.get(), promptEngineProvider.get(), evolutionaryEngineProvider.get(), characterRepositoryProvider.get(), relationshipRepositoryProvider.get(), dataInitializerProvider.get(), memoryDaoProvider.get(), imageEngineProvider.get());
  }

  public static ChatRepositoryImpl_Factory create(Provider<MessageDao> messageDaoProvider,
      Provider<AIProvider> aiProvider, Provider<PromptEngine> promptEngineProvider,
      Provider<EvolutionaryEngine> evolutionaryEngineProvider,
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<RelationshipRepository> relationshipRepositoryProvider,
      Provider<DataInitializer> dataInitializerProvider, Provider<MemoryDao> memoryDaoProvider,
      Provider<ImageEngine> imageEngineProvider) {
    return new ChatRepositoryImpl_Factory(messageDaoProvider, aiProvider, promptEngineProvider, evolutionaryEngineProvider, characterRepositoryProvider, relationshipRepositoryProvider, dataInitializerProvider, memoryDaoProvider, imageEngineProvider);
  }

  public static ChatRepositoryImpl newInstance(MessageDao messageDao, AIProvider aiProvider,
      PromptEngine promptEngine, EvolutionaryEngine evolutionaryEngine,
      CharacterRepository characterRepository, RelationshipRepository relationshipRepository,
      DataInitializer dataInitializer, MemoryDao memoryDao, ImageEngine imageEngine) {
    return new ChatRepositoryImpl(messageDao, aiProvider, promptEngine, evolutionaryEngine, characterRepository, relationshipRepository, dataInitializer, memoryDao, imageEngine);
  }
}
