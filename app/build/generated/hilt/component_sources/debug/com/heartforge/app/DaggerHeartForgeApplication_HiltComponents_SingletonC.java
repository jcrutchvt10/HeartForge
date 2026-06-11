package com.heartforge.app;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.heartforge.app.core.ai.CasualPhotoGenerator;
import com.heartforge.app.core.ai.EvolutionaryEngine;
import com.heartforge.app.core.ai.ImageEngine;
import com.heartforge.app.core.ai.MatchmakingEngine;
import com.heartforge.app.core.ai.NSFWGenerator;
import com.heartforge.app.core.ai.PromptEngine;
import com.heartforge.app.core.ai.StoryBranchGenerator;
import com.heartforge.app.core.ai.nvidia.NVIDIAProvider;
import com.heartforge.app.core.database.CharacterDao;
import com.heartforge.app.core.database.HeartForgeDatabase;
import com.heartforge.app.core.database.MemoryDao;
import com.heartforge.app.core.database.MessageDao;
import com.heartforge.app.core.database.RelationshipDao;
import com.heartforge.app.core.database.StoryDao;
import com.heartforge.app.core.network.nvidia.NVIDIAApiService;
import com.heartforge.app.core.network.nvidia.NVIDIAImageApiService;
import com.heartforge.app.core.repository.CharacterRepositoryImpl;
import com.heartforge.app.core.repository.ChatRepositoryImpl;
import com.heartforge.app.core.repository.MemoryRepositoryImpl;
import com.heartforge.app.core.repository.RelationshipRepositoryImpl;
import com.heartforge.app.core.repository.SettingsRepositoryImpl;
import com.heartforge.app.core.repository.StoryRepositoryImpl;
import com.heartforge.app.core.repository.UserProfileRepositoryImpl;
import com.heartforge.app.core.util.AppForegroundState;
import com.heartforge.app.core.util.DataInitializer;
import com.heartforge.app.core.util.ImageStorage;
import com.heartforge.app.core.util.NotificationHelper;
import com.heartforge.app.core.util.SecureSettings;
import com.heartforge.app.di.DatabaseModule_ProvideCharacterDaoFactory;
import com.heartforge.app.di.DatabaseModule_ProvideDatabaseFactory;
import com.heartforge.app.di.DatabaseModule_ProvideMemoryDaoFactory;
import com.heartforge.app.di.DatabaseModule_ProvideMessageDaoFactory;
import com.heartforge.app.di.DatabaseModule_ProvideRelationshipDaoFactory;
import com.heartforge.app.di.DatabaseModule_ProvideStoryDaoFactory;
import com.heartforge.app.di.NetworkModule_ProvideNvidiaApiServiceFactory;
import com.heartforge.app.di.NetworkModule_ProvideNvidiaImageApiServiceFactory;
import com.heartforge.app.di.NetworkModule_ProvideOkHttpClientFactory;
import com.heartforge.app.feature.chat.ChatListViewModel;
import com.heartforge.app.feature.chat.ChatListViewModel_HiltModules;
import com.heartforge.app.feature.chat.ChatViewModel;
import com.heartforge.app.feature.chat.ChatViewModel_HiltModules;
import com.heartforge.app.feature.creator.CreatorViewModel;
import com.heartforge.app.feature.creator.CreatorViewModel_HiltModules;
import com.heartforge.app.feature.gallery.GalleryViewModel;
import com.heartforge.app.feature.gallery.GalleryViewModel_HiltModules;
import com.heartforge.app.feature.home.HomeViewModel;
import com.heartforge.app.feature.home.HomeViewModel_HiltModules;
import com.heartforge.app.feature.matches.CharacterProfileViewModel;
import com.heartforge.app.feature.matches.CharacterProfileViewModel_HiltModules;
import com.heartforge.app.feature.matches.MatchViewModel;
import com.heartforge.app.feature.matches.MatchViewModel_HiltModules;
import com.heartforge.app.feature.memories.MemoryViewModel;
import com.heartforge.app.feature.memories.MemoryViewModel_HiltModules;
import com.heartforge.app.feature.profile.ProfileSettingsViewModel;
import com.heartforge.app.feature.profile.ProfileSettingsViewModel_HiltModules;
import com.heartforge.app.feature.settings.SettingsViewModel;
import com.heartforge.app.feature.settings.SettingsViewModel_HiltModules;
import com.heartforge.app.feature.stories.StoryPlayViewModel;
import com.heartforge.app.feature.stories.StoryPlayViewModel_HiltModules;
import com.heartforge.app.feature.stories.StoryViewModel;
import com.heartforge.app.feature.stories.StoryViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;

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
public final class DaggerHeartForgeApplication_HiltComponents_SingletonC {
  private DaggerHeartForgeApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public HeartForgeApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements HeartForgeApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public HeartForgeApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements HeartForgeApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public HeartForgeApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements HeartForgeApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public HeartForgeApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements HeartForgeApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public HeartForgeApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements HeartForgeApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public HeartForgeApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements HeartForgeApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public HeartForgeApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements HeartForgeApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public HeartForgeApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends HeartForgeApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends HeartForgeApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends HeartForgeApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends HeartForgeApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(12).put(LazyClassKeyProvider.com_heartforge_app_feature_matches_CharacterProfileViewModel, CharacterProfileViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_heartforge_app_feature_chat_ChatListViewModel, ChatListViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_heartforge_app_feature_chat_ChatViewModel, ChatViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_heartforge_app_feature_creator_CreatorViewModel, CreatorViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_heartforge_app_feature_gallery_GalleryViewModel, GalleryViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_heartforge_app_feature_home_HomeViewModel, HomeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_heartforge_app_feature_matches_MatchViewModel, MatchViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_heartforge_app_feature_memories_MemoryViewModel, MemoryViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_heartforge_app_feature_profile_ProfileSettingsViewModel, ProfileSettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_heartforge_app_feature_settings_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_heartforge_app_feature_stories_StoryPlayViewModel, StoryPlayViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_heartforge_app_feature_stories_StoryViewModel, StoryViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_heartforge_app_feature_creator_CreatorViewModel = "com.heartforge.app.feature.creator.CreatorViewModel";

      static String com_heartforge_app_feature_stories_StoryViewModel = "com.heartforge.app.feature.stories.StoryViewModel";

      static String com_heartforge_app_feature_matches_CharacterProfileViewModel = "com.heartforge.app.feature.matches.CharacterProfileViewModel";

      static String com_heartforge_app_feature_chat_ChatListViewModel = "com.heartforge.app.feature.chat.ChatListViewModel";

      static String com_heartforge_app_feature_home_HomeViewModel = "com.heartforge.app.feature.home.HomeViewModel";

      static String com_heartforge_app_feature_matches_MatchViewModel = "com.heartforge.app.feature.matches.MatchViewModel";

      static String com_heartforge_app_feature_memories_MemoryViewModel = "com.heartforge.app.feature.memories.MemoryViewModel";

      static String com_heartforge_app_feature_gallery_GalleryViewModel = "com.heartforge.app.feature.gallery.GalleryViewModel";

      static String com_heartforge_app_feature_profile_ProfileSettingsViewModel = "com.heartforge.app.feature.profile.ProfileSettingsViewModel";

      static String com_heartforge_app_feature_settings_SettingsViewModel = "com.heartforge.app.feature.settings.SettingsViewModel";

      static String com_heartforge_app_feature_chat_ChatViewModel = "com.heartforge.app.feature.chat.ChatViewModel";

      static String com_heartforge_app_feature_stories_StoryPlayViewModel = "com.heartforge.app.feature.stories.StoryPlayViewModel";

      @KeepFieldType
      CreatorViewModel com_heartforge_app_feature_creator_CreatorViewModel2;

      @KeepFieldType
      StoryViewModel com_heartforge_app_feature_stories_StoryViewModel2;

      @KeepFieldType
      CharacterProfileViewModel com_heartforge_app_feature_matches_CharacterProfileViewModel2;

      @KeepFieldType
      ChatListViewModel com_heartforge_app_feature_chat_ChatListViewModel2;

      @KeepFieldType
      HomeViewModel com_heartforge_app_feature_home_HomeViewModel2;

      @KeepFieldType
      MatchViewModel com_heartforge_app_feature_matches_MatchViewModel2;

      @KeepFieldType
      MemoryViewModel com_heartforge_app_feature_memories_MemoryViewModel2;

      @KeepFieldType
      GalleryViewModel com_heartforge_app_feature_gallery_GalleryViewModel2;

      @KeepFieldType
      ProfileSettingsViewModel com_heartforge_app_feature_profile_ProfileSettingsViewModel2;

      @KeepFieldType
      SettingsViewModel com_heartforge_app_feature_settings_SettingsViewModel2;

      @KeepFieldType
      ChatViewModel com_heartforge_app_feature_chat_ChatViewModel2;

      @KeepFieldType
      StoryPlayViewModel com_heartforge_app_feature_stories_StoryPlayViewModel2;
    }
  }

  private static final class ViewModelCImpl extends HeartForgeApplication_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<CharacterProfileViewModel> characterProfileViewModelProvider;

    private Provider<ChatListViewModel> chatListViewModelProvider;

    private Provider<ChatViewModel> chatViewModelProvider;

    private Provider<CreatorViewModel> creatorViewModelProvider;

    private Provider<GalleryViewModel> galleryViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<MatchViewModel> matchViewModelProvider;

    private Provider<MemoryViewModel> memoryViewModelProvider;

    private Provider<ProfileSettingsViewModel> profileSettingsViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<StoryPlayViewModel> storyPlayViewModelProvider;

    private Provider<StoryViewModel> storyViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.characterProfileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.chatListViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.chatViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.creatorViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.galleryViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.matchViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.memoryViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.profileSettingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
      this.storyPlayViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 10);
      this.storyViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 11);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(12).put(LazyClassKeyProvider.com_heartforge_app_feature_matches_CharacterProfileViewModel, ((Provider) characterProfileViewModelProvider)).put(LazyClassKeyProvider.com_heartforge_app_feature_chat_ChatListViewModel, ((Provider) chatListViewModelProvider)).put(LazyClassKeyProvider.com_heartforge_app_feature_chat_ChatViewModel, ((Provider) chatViewModelProvider)).put(LazyClassKeyProvider.com_heartforge_app_feature_creator_CreatorViewModel, ((Provider) creatorViewModelProvider)).put(LazyClassKeyProvider.com_heartforge_app_feature_gallery_GalleryViewModel, ((Provider) galleryViewModelProvider)).put(LazyClassKeyProvider.com_heartforge_app_feature_home_HomeViewModel, ((Provider) homeViewModelProvider)).put(LazyClassKeyProvider.com_heartforge_app_feature_matches_MatchViewModel, ((Provider) matchViewModelProvider)).put(LazyClassKeyProvider.com_heartforge_app_feature_memories_MemoryViewModel, ((Provider) memoryViewModelProvider)).put(LazyClassKeyProvider.com_heartforge_app_feature_profile_ProfileSettingsViewModel, ((Provider) profileSettingsViewModelProvider)).put(LazyClassKeyProvider.com_heartforge_app_feature_settings_SettingsViewModel, ((Provider) settingsViewModelProvider)).put(LazyClassKeyProvider.com_heartforge_app_feature_stories_StoryPlayViewModel, ((Provider) storyPlayViewModelProvider)).put(LazyClassKeyProvider.com_heartforge_app_feature_stories_StoryViewModel, ((Provider) storyViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_heartforge_app_feature_stories_StoryPlayViewModel = "com.heartforge.app.feature.stories.StoryPlayViewModel";

      static String com_heartforge_app_feature_chat_ChatViewModel = "com.heartforge.app.feature.chat.ChatViewModel";

      static String com_heartforge_app_feature_settings_SettingsViewModel = "com.heartforge.app.feature.settings.SettingsViewModel";

      static String com_heartforge_app_feature_chat_ChatListViewModel = "com.heartforge.app.feature.chat.ChatListViewModel";

      static String com_heartforge_app_feature_matches_CharacterProfileViewModel = "com.heartforge.app.feature.matches.CharacterProfileViewModel";

      static String com_heartforge_app_feature_profile_ProfileSettingsViewModel = "com.heartforge.app.feature.profile.ProfileSettingsViewModel";

      static String com_heartforge_app_feature_gallery_GalleryViewModel = "com.heartforge.app.feature.gallery.GalleryViewModel";

      static String com_heartforge_app_feature_matches_MatchViewModel = "com.heartforge.app.feature.matches.MatchViewModel";

      static String com_heartforge_app_feature_stories_StoryViewModel = "com.heartforge.app.feature.stories.StoryViewModel";

      static String com_heartforge_app_feature_creator_CreatorViewModel = "com.heartforge.app.feature.creator.CreatorViewModel";

      static String com_heartforge_app_feature_memories_MemoryViewModel = "com.heartforge.app.feature.memories.MemoryViewModel";

      static String com_heartforge_app_feature_home_HomeViewModel = "com.heartforge.app.feature.home.HomeViewModel";

      @KeepFieldType
      StoryPlayViewModel com_heartforge_app_feature_stories_StoryPlayViewModel2;

      @KeepFieldType
      ChatViewModel com_heartforge_app_feature_chat_ChatViewModel2;

      @KeepFieldType
      SettingsViewModel com_heartforge_app_feature_settings_SettingsViewModel2;

      @KeepFieldType
      ChatListViewModel com_heartforge_app_feature_chat_ChatListViewModel2;

      @KeepFieldType
      CharacterProfileViewModel com_heartforge_app_feature_matches_CharacterProfileViewModel2;

      @KeepFieldType
      ProfileSettingsViewModel com_heartforge_app_feature_profile_ProfileSettingsViewModel2;

      @KeepFieldType
      GalleryViewModel com_heartforge_app_feature_gallery_GalleryViewModel2;

      @KeepFieldType
      MatchViewModel com_heartforge_app_feature_matches_MatchViewModel2;

      @KeepFieldType
      StoryViewModel com_heartforge_app_feature_stories_StoryViewModel2;

      @KeepFieldType
      CreatorViewModel com_heartforge_app_feature_creator_CreatorViewModel2;

      @KeepFieldType
      MemoryViewModel com_heartforge_app_feature_memories_MemoryViewModel2;

      @KeepFieldType
      HomeViewModel com_heartforge_app_feature_home_HomeViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.heartforge.app.feature.matches.CharacterProfileViewModel 
          return (T) new CharacterProfileViewModel(singletonCImpl.characterRepositoryImplProvider.get(), viewModelCImpl.savedStateHandle);

          case 1: // com.heartforge.app.feature.chat.ChatListViewModel 
          return (T) new ChatListViewModel(singletonCImpl.messageDao(), singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.chatRepositoryImplProvider.get());

          case 2: // com.heartforge.app.feature.chat.ChatViewModel 
          return (T) new ChatViewModel(singletonCImpl.chatRepositoryImplProvider.get(), singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.relationshipRepositoryImplProvider.get(), singletonCImpl.storyRepositoryImplProvider.get(), viewModelCImpl.savedStateHandle);

          case 3: // com.heartforge.app.feature.creator.CreatorViewModel 
          return (T) new CreatorViewModel(singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.imageEngineProvider.get());

          case 4: // com.heartforge.app.feature.gallery.GalleryViewModel 
          return (T) new GalleryViewModel(singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.nSFWGeneratorProvider.get(), singletonCImpl.casualPhotoGeneratorProvider.get());

          case 5: // com.heartforge.app.feature.home.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.dataInitializerProvider.get(), singletonCImpl.memoryDao());

          case 6: // com.heartforge.app.feature.matches.MatchViewModel 
          return (T) new MatchViewModel(singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.matchmakingEngineProvider.get(), singletonCImpl.dataInitializerProvider.get(), singletonCImpl.userProfileRepositoryImplProvider.get());

          case 7: // com.heartforge.app.feature.memories.MemoryViewModel 
          return (T) new MemoryViewModel(singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.memoryDao(), singletonCImpl.memoryRepositoryImplProvider.get(), viewModelCImpl.savedStateHandle);

          case 8: // com.heartforge.app.feature.profile.ProfileSettingsViewModel 
          return (T) new ProfileSettingsViewModel(singletonCImpl.userProfileRepositoryImplProvider.get(), singletonCImpl.settingsRepositoryImplProvider.get(), singletonCImpl.provideNvidiaApiServiceProvider.get());

          case 9: // com.heartforge.app.feature.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.settingsRepositoryImplProvider.get(), singletonCImpl.provideNvidiaApiServiceProvider.get());

          case 10: // com.heartforge.app.feature.stories.StoryPlayViewModel 
          return (T) new StoryPlayViewModel(singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.storyRepositoryImplProvider.get(), singletonCImpl.storyBranchGeneratorProvider.get(), singletonCImpl.relationshipRepositoryImplProvider.get(), viewModelCImpl.savedStateHandle);

          case 11: // com.heartforge.app.feature.stories.StoryViewModel 
          return (T) new StoryViewModel(singletonCImpl.storyRepositoryImplProvider.get(), singletonCImpl.relationshipRepositoryImplProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends HeartForgeApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends HeartForgeApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends HeartForgeApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<AppForegroundState> appForegroundStateProvider;

    private Provider<HeartForgeDatabase> provideDatabaseProvider;

    private Provider<CharacterRepositoryImpl> characterRepositoryImplProvider;

    private Provider<OkHttpClient> provideOkHttpClientProvider;

    private Provider<SecureSettings> secureSettingsProvider;

    private Provider<SettingsRepositoryImpl> settingsRepositoryImplProvider;

    private Provider<NVIDIAApiService> provideNvidiaApiServiceProvider;

    private Provider<NVIDIAImageApiService> provideNvidiaImageApiServiceProvider;

    private Provider<NVIDIAProvider> nVIDIAProvider;

    private Provider<PromptEngine> promptEngineProvider;

    private Provider<RelationshipRepositoryImpl> relationshipRepositoryImplProvider;

    private Provider<MemoryRepositoryImpl> memoryRepositoryImplProvider;

    private Provider<EvolutionaryEngine> evolutionaryEngineProvider;

    private Provider<DataInitializer> dataInitializerProvider;

    private Provider<ImageStorage> imageStorageProvider;

    private Provider<ImageEngine> imageEngineProvider;

    private Provider<UserProfileRepositoryImpl> userProfileRepositoryImplProvider;

    private Provider<NotificationHelper> notificationHelperProvider;

    private Provider<ChatRepositoryImpl> chatRepositoryImplProvider;

    private Provider<StoryRepositoryImpl> storyRepositoryImplProvider;

    private Provider<NSFWGenerator> nSFWGeneratorProvider;

    private Provider<CasualPhotoGenerator> casualPhotoGeneratorProvider;

    private Provider<MatchmakingEngine> matchmakingEngineProvider;

    private Provider<StoryBranchGenerator> storyBranchGeneratorProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private CharacterDao characterDao() {
      return DatabaseModule_ProvideCharacterDaoFactory.provideCharacterDao(provideDatabaseProvider.get());
    }

    private MessageDao messageDao() {
      return DatabaseModule_ProvideMessageDaoFactory.provideMessageDao(provideDatabaseProvider.get());
    }

    private RelationshipDao relationshipDao() {
      return DatabaseModule_ProvideRelationshipDaoFactory.provideRelationshipDao(provideDatabaseProvider.get());
    }

    private MemoryDao memoryDao() {
      return DatabaseModule_ProvideMemoryDaoFactory.provideMemoryDao(provideDatabaseProvider.get());
    }

    private StoryDao storyDao() {
      return DatabaseModule_ProvideStoryDaoFactory.provideStoryDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.appForegroundStateProvider = DoubleCheck.provider(new SwitchingProvider<AppForegroundState>(singletonCImpl, 0));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<HeartForgeDatabase>(singletonCImpl, 2));
      this.characterRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<CharacterRepositoryImpl>(singletonCImpl, 1));
      this.provideOkHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 6));
      this.secureSettingsProvider = DoubleCheck.provider(new SwitchingProvider<SecureSettings>(singletonCImpl, 8));
      this.settingsRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<SettingsRepositoryImpl>(singletonCImpl, 7));
      this.provideNvidiaApiServiceProvider = DoubleCheck.provider(new SwitchingProvider<NVIDIAApiService>(singletonCImpl, 5));
      this.provideNvidiaImageApiServiceProvider = DoubleCheck.provider(new SwitchingProvider<NVIDIAImageApiService>(singletonCImpl, 9));
      this.nVIDIAProvider = DoubleCheck.provider(new SwitchingProvider<NVIDIAProvider>(singletonCImpl, 4));
      this.promptEngineProvider = DoubleCheck.provider(new SwitchingProvider<PromptEngine>(singletonCImpl, 10));
      this.relationshipRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<RelationshipRepositoryImpl>(singletonCImpl, 12));
      this.memoryRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<MemoryRepositoryImpl>(singletonCImpl, 13));
      this.evolutionaryEngineProvider = DoubleCheck.provider(new SwitchingProvider<EvolutionaryEngine>(singletonCImpl, 11));
      this.dataInitializerProvider = DoubleCheck.provider(new SwitchingProvider<DataInitializer>(singletonCImpl, 14));
      this.imageStorageProvider = DoubleCheck.provider(new SwitchingProvider<ImageStorage>(singletonCImpl, 16));
      this.imageEngineProvider = DoubleCheck.provider(new SwitchingProvider<ImageEngine>(singletonCImpl, 15));
      this.userProfileRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<UserProfileRepositoryImpl>(singletonCImpl, 17));
      this.notificationHelperProvider = DoubleCheck.provider(new SwitchingProvider<NotificationHelper>(singletonCImpl, 18));
      this.chatRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ChatRepositoryImpl>(singletonCImpl, 3));
      this.storyRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<StoryRepositoryImpl>(singletonCImpl, 19));
      this.nSFWGeneratorProvider = DoubleCheck.provider(new SwitchingProvider<NSFWGenerator>(singletonCImpl, 20));
      this.casualPhotoGeneratorProvider = DoubleCheck.provider(new SwitchingProvider<CasualPhotoGenerator>(singletonCImpl, 21));
      this.matchmakingEngineProvider = DoubleCheck.provider(new SwitchingProvider<MatchmakingEngine>(singletonCImpl, 22));
      this.storyBranchGeneratorProvider = DoubleCheck.provider(new SwitchingProvider<StoryBranchGenerator>(singletonCImpl, 23));
    }

    @Override
    public void injectHeartForgeApplication(HeartForgeApplication heartForgeApplication) {
      injectHeartForgeApplication2(heartForgeApplication);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private HeartForgeApplication injectHeartForgeApplication2(HeartForgeApplication instance) {
      HeartForgeApplication_MembersInjector.injectForegroundState(instance, appForegroundStateProvider.get());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.heartforge.app.core.util.AppForegroundState 
          return (T) new AppForegroundState();

          case 1: // com.heartforge.app.core.repository.CharacterRepositoryImpl 
          return (T) new CharacterRepositoryImpl(singletonCImpl.characterDao());

          case 2: // com.heartforge.app.core.database.HeartForgeDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.heartforge.app.core.repository.ChatRepositoryImpl 
          return (T) new ChatRepositoryImpl(singletonCImpl.messageDao(), singletonCImpl.nVIDIAProvider.get(), singletonCImpl.promptEngineProvider.get(), singletonCImpl.evolutionaryEngineProvider.get(), singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.relationshipRepositoryImplProvider.get(), singletonCImpl.dataInitializerProvider.get(), singletonCImpl.memoryDao(), singletonCImpl.imageEngineProvider.get(), singletonCImpl.userProfileRepositoryImplProvider.get(), singletonCImpl.notificationHelperProvider.get(), singletonCImpl.appForegroundStateProvider.get());

          case 4: // com.heartforge.app.core.ai.nvidia.NVIDIAProvider 
          return (T) new NVIDIAProvider(singletonCImpl.provideNvidiaApiServiceProvider.get(), singletonCImpl.provideNvidiaImageApiServiceProvider.get(), singletonCImpl.settingsRepositoryImplProvider.get());

          case 5: // com.heartforge.app.core.network.nvidia.NVIDIAApiService 
          return (T) NetworkModule_ProvideNvidiaApiServiceFactory.provideNvidiaApiService(singletonCImpl.provideOkHttpClientProvider.get(), singletonCImpl.settingsRepositoryImplProvider.get());

          case 6: // okhttp3.OkHttpClient 
          return (T) NetworkModule_ProvideOkHttpClientFactory.provideOkHttpClient();

          case 7: // com.heartforge.app.core.repository.SettingsRepositoryImpl 
          return (T) new SettingsRepositoryImpl(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.secureSettingsProvider.get());

          case 8: // com.heartforge.app.core.util.SecureSettings 
          return (T) new SecureSettings(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 9: // com.heartforge.app.core.network.nvidia.NVIDIAImageApiService 
          return (T) NetworkModule_ProvideNvidiaImageApiServiceFactory.provideNvidiaImageApiService(singletonCImpl.provideOkHttpClientProvider.get());

          case 10: // com.heartforge.app.core.ai.PromptEngine 
          return (T) new PromptEngine();

          case 11: // com.heartforge.app.core.ai.EvolutionaryEngine 
          return (T) new EvolutionaryEngine(singletonCImpl.nVIDIAProvider.get(), singletonCImpl.relationshipRepositoryImplProvider.get(), singletonCImpl.memoryRepositoryImplProvider.get());

          case 12: // com.heartforge.app.core.repository.RelationshipRepositoryImpl 
          return (T) new RelationshipRepositoryImpl(singletonCImpl.relationshipDao());

          case 13: // com.heartforge.app.core.repository.MemoryRepositoryImpl 
          return (T) new MemoryRepositoryImpl(singletonCImpl.memoryDao());

          case 14: // com.heartforge.app.core.util.DataInitializer 
          return (T) new DataInitializer(singletonCImpl.characterDao());

          case 15: // com.heartforge.app.core.ai.ImageEngine 
          return (T) new ImageEngine(singletonCImpl.nVIDIAProvider.get(), singletonCImpl.imageStorageProvider.get());

          case 16: // com.heartforge.app.core.util.ImageStorage 
          return (T) new ImageStorage(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 17: // com.heartforge.app.core.repository.UserProfileRepositoryImpl 
          return (T) new UserProfileRepositoryImpl(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 18: // com.heartforge.app.core.util.NotificationHelper 
          return (T) new NotificationHelper(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 19: // com.heartforge.app.core.repository.StoryRepositoryImpl 
          return (T) new StoryRepositoryImpl(singletonCImpl.storyDao());

          case 20: // com.heartforge.app.core.ai.NSFWGenerator 
          return (T) new NSFWGenerator(singletonCImpl.nVIDIAProvider.get(), singletonCImpl.imageStorageProvider.get(), singletonCImpl.characterRepositoryImplProvider.get());

          case 21: // com.heartforge.app.core.ai.CasualPhotoGenerator 
          return (T) new CasualPhotoGenerator(singletonCImpl.nVIDIAProvider.get(), singletonCImpl.imageStorageProvider.get(), singletonCImpl.characterRepositoryImplProvider.get());

          case 22: // com.heartforge.app.core.ai.MatchmakingEngine 
          return (T) new MatchmakingEngine(singletonCImpl.nVIDIAProvider.get());

          case 23: // com.heartforge.app.core.ai.StoryBranchGenerator 
          return (T) new StoryBranchGenerator(singletonCImpl.nVIDIAProvider.get(), singletonCImpl.relationshipRepositoryImplProvider.get(), singletonCImpl.settingsRepositoryImplProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
