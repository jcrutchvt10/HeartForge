package com.heartforge.app.feature.chat;

import androidx.lifecycle.SavedStateHandle;
import com.heartforge.app.core.repository.CharacterRepository;
import com.heartforge.app.core.repository.ChatRepository;
import com.heartforge.app.core.repository.RelationshipRepository;
import com.heartforge.app.core.repository.StoryRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class ChatViewModel_Factory implements Factory<ChatViewModel> {
  private final Provider<ChatRepository> chatRepositoryProvider;

  private final Provider<CharacterRepository> characterRepositoryProvider;

  private final Provider<RelationshipRepository> relationshipRepositoryProvider;

  private final Provider<StoryRepository> storyRepositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public ChatViewModel_Factory(Provider<ChatRepository> chatRepositoryProvider,
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<RelationshipRepository> relationshipRepositoryProvider,
      Provider<StoryRepository> storyRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.chatRepositoryProvider = chatRepositoryProvider;
    this.characterRepositoryProvider = characterRepositoryProvider;
    this.relationshipRepositoryProvider = relationshipRepositoryProvider;
    this.storyRepositoryProvider = storyRepositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public ChatViewModel get() {
    return newInstance(chatRepositoryProvider.get(), characterRepositoryProvider.get(), relationshipRepositoryProvider.get(), storyRepositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static ChatViewModel_Factory create(Provider<ChatRepository> chatRepositoryProvider,
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<RelationshipRepository> relationshipRepositoryProvider,
      Provider<StoryRepository> storyRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new ChatViewModel_Factory(chatRepositoryProvider, characterRepositoryProvider, relationshipRepositoryProvider, storyRepositoryProvider, savedStateHandleProvider);
  }

  public static ChatViewModel newInstance(ChatRepository chatRepository,
      CharacterRepository characterRepository, RelationshipRepository relationshipRepository,
      StoryRepository storyRepository, SavedStateHandle savedStateHandle) {
    return new ChatViewModel(chatRepository, characterRepository, relationshipRepository, storyRepository, savedStateHandle);
  }
}
