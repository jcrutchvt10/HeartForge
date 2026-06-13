package com.heartforge.app;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.heartforge.app.core.ai.CasualPhotoGenerator;
import com.heartforge.app.core.ai.EvolutionaryEngine;
import com.heartforge.app.core.ai.ImageEngine;
import com.heartforge.app.core.ai.MatchmakingEngine;
import com.heartforge.app.core.ai.NSFWGenerator;
import com.heartforge.app.core.ai.PromptEngine;
import com.heartforge.app.core.ai.StoryEngine;
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
import com.heartforge.app.core.worker.ProactiveNudgeWorker;
import com.heartforge.app.core.worker.ProactiveNudgeWorker_AssistedFactory;
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
import com.heartforge.app.feature.chat.ChatListViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.heartforge.app.feature.chat.ChatListViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.heartforge.app.feature.chat.ChatViewModel;
import com.heartforge.app.feature.chat.ChatViewModel_HiltModules;
import com.heartforge.app.feature.chat.ChatViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.heartforge.app.feature.chat.ChatViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.heartforge.app.feature.chronicle.ChronicleViewModel;
import com.heartforge.app.feature.chronicle.ChronicleViewModel_HiltModules;
import com.heartforge.app.feature.chronicle.ChronicleViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.heartforge.app.feature.chronicle.ChronicleViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.heartforge.app.feature.creator.CreatorViewModel;
import com.heartforge.app.feature.creator.CreatorViewModel_HiltModules;
import com.heartforge.app.feature.creator.CreatorViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.heartforge.app.feature.creator.CreatorViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.heartforge.app.feature.gallery.GalleryViewModel;
import com.heartforge.app.feature.gallery.GalleryViewModel_HiltModules;
import com.heartforge.app.feature.gallery.GalleryViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.heartforge.app.feature.gallery.GalleryViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.heartforge.app.feature.home.HomeViewModel;
import com.heartforge.app.feature.home.HomeViewModel_HiltModules;
import com.heartforge.app.feature.home.HomeViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.heartforge.app.feature.home.HomeViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.heartforge.app.feature.matches.CharacterProfileViewModel;
import com.heartforge.app.feature.matches.CharacterProfileViewModel_HiltModules;
import com.heartforge.app.feature.matches.CharacterProfileViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.heartforge.app.feature.matches.CharacterProfileViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.heartforge.app.feature.matches.MatchViewModel;
import com.heartforge.app.feature.matches.MatchViewModel_HiltModules;
import com.heartforge.app.feature.matches.MatchViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.heartforge.app.feature.matches.MatchViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.heartforge.app.feature.memories.MemoryViewModel;
import com.heartforge.app.feature.memories.MemoryViewModel_HiltModules;
import com.heartforge.app.feature.memories.MemoryViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.heartforge.app.feature.memories.MemoryViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.heartforge.app.feature.settings.SettingsViewModel;
import com.heartforge.app.feature.settings.SettingsViewModel_HiltModules;
import com.heartforge.app.feature.settings.SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.heartforge.app.feature.settings.SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.heartforge.app.feature.stories.StoryViewModel;
import com.heartforge.app.feature.stories.StoryViewModel_HiltModules;
import com.heartforge.app.feature.stories.StoryViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.heartforge.app.feature.stories.StoryViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
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
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SingleCheck;
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
    "deprecation",
    "nullness:initialization.field.uninitialized"
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

    ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
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

    FragmentCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, Fragment fragmentParam) {
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

    ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
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

    ActivityCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    Map keySetMapOfClassOfAndBooleanBuilder() {
      MapBuilder mapBuilder = MapBuilder.<String, Boolean>newMapBuilder(11);
      mapBuilder.put(CharacterProfileViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, CharacterProfileViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(ChatListViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ChatListViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(ChatViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ChatViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(ChronicleViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ChronicleViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(CreatorViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, CreatorViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(GalleryViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, GalleryViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(HomeViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, HomeViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(MatchViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, MatchViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(MemoryViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, MemoryViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, SettingsViewModel_HiltModules.KeyModule.provide());
      mapBuilder.put(StoryViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, StoryViewModel_HiltModules.KeyModule.provide());
      return mapBuilder.build();
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
      return LazyClassKeyMap.<Boolean>of(keySetMapOfClassOfAndBooleanBuilder());
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
  }

  private static final class ViewModelCImpl extends HeartForgeApplication_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    Provider<CharacterProfileViewModel> characterProfileViewModelProvider;

    Provider<ChatListViewModel> chatListViewModelProvider;

    Provider<ChatViewModel> chatViewModelProvider;

    Provider<ChronicleViewModel> chronicleViewModelProvider;

    Provider<CreatorViewModel> creatorViewModelProvider;

    Provider<GalleryViewModel> galleryViewModelProvider;

    Provider<HomeViewModel> homeViewModelProvider;

    Provider<MatchViewModel> matchViewModelProvider;

    Provider<MemoryViewModel> memoryViewModelProvider;

    Provider<SettingsViewModel> settingsViewModelProvider;

    Provider<StoryViewModel> storyViewModelProvider;

    ViewModelCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        SavedStateHandle savedStateHandleParam, ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    Map hiltViewModelMapMapOfClassOfAndProviderOfViewModelBuilder() {
      MapBuilder mapBuilder = MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(11);
      mapBuilder.put(CharacterProfileViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (characterProfileViewModelProvider)));
      mapBuilder.put(ChatListViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (chatListViewModelProvider)));
      mapBuilder.put(ChatViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (chatViewModelProvider)));
      mapBuilder.put(ChronicleViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (chronicleViewModelProvider)));
      mapBuilder.put(CreatorViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (creatorViewModelProvider)));
      mapBuilder.put(GalleryViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (galleryViewModelProvider)));
      mapBuilder.put(HomeViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (homeViewModelProvider)));
      mapBuilder.put(MatchViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (matchViewModelProvider)));
      mapBuilder.put(MemoryViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (memoryViewModelProvider)));
      mapBuilder.put(SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (settingsViewModelProvider)));
      mapBuilder.put(StoryViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) (storyViewModelProvider)));
      return mapBuilder.build();
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.characterProfileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.chatListViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.chatViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.chronicleViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.creatorViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.galleryViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.matchViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.memoryViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
      this.storyViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 10);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(hiltViewModelMapMapOfClassOfAndProviderOfViewModelBuilder());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
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

      @Override
      @SuppressWarnings("unchecked")
      public T get() {
        switch (id) {
          case 0: // com.heartforge.app.feature.matches.CharacterProfileViewModel
          return (T) new CharacterProfileViewModel(singletonCImpl.characterRepositoryImplProvider.get(), viewModelCImpl.savedStateHandle);

          case 1: // com.heartforge.app.feature.chat.ChatListViewModel
          return (T) new ChatListViewModel(singletonCImpl.characterRepositoryImplProvider.get());

          case 2: // com.heartforge.app.feature.chat.ChatViewModel
          return (T) new ChatViewModel(singletonCImpl.chatRepositoryImplProvider.get(), singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.relationshipRepositoryImplProvider.get(), singletonCImpl.storyRepositoryImplProvider.get(), viewModelCImpl.savedStateHandle);

          case 3: // com.heartforge.app.feature.chronicle.ChronicleViewModel
          return (T) new ChronicleViewModel(singletonCImpl.memoryRepositoryImplProvider.get());

          case 4: // com.heartforge.app.feature.creator.CreatorViewModel
          return (T) new CreatorViewModel(singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.imageEngineProvider.get());

          case 5: // com.heartforge.app.feature.gallery.GalleryViewModel
          return (T) new GalleryViewModel(singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.nSFWGeneratorProvider.get(), singletonCImpl.casualPhotoGeneratorProvider.get(), viewModelCImpl.savedStateHandle);

          case 6: // com.heartforge.app.feature.home.HomeViewModel
          return (T) new HomeViewModel(singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.dataInitializerProvider.get(), singletonCImpl.memoryDao(), singletonCImpl.settingsRepositoryImplProvider.get());

          case 7: // com.heartforge.app.feature.matches.MatchViewModel
          return (T) new MatchViewModel(singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.matchmakingEngineProvider.get(), singletonCImpl.dataInitializerProvider.get(), singletonCImpl.userProfileRepositoryImplProvider.get());

          case 8: // com.heartforge.app.feature.memories.MemoryViewModel
          return (T) new MemoryViewModel(singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.memoryDao(), singletonCImpl.memoryRepositoryImplProvider.get(), viewModelCImpl.savedStateHandle);

          case 9: // com.heartforge.app.feature.settings.SettingsViewModel
          return (T) new SettingsViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.settingsRepositoryImplProvider.get(), singletonCImpl.provideNvidiaApiServiceProvider.get());

          case 10: // com.heartforge.app.feature.stories.StoryViewModel
          return (T) new StoryViewModel(singletonCImpl.storyRepositoryImplProvider.get(), singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.relationshipRepositoryImplProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends HeartForgeApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
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

      @Override
      @SuppressWarnings("unchecked")
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

    ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends HeartForgeApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    Provider<AppForegroundState> appForegroundStateProvider;

    Provider<HeartForgeDatabase> provideDatabaseProvider;

    Provider<CharacterRepositoryImpl> characterRepositoryImplProvider;

    Provider<RelationshipRepositoryImpl> relationshipRepositoryImplProvider;

    Provider<UserProfileRepositoryImpl> userProfileRepositoryImplProvider;

    Provider<OkHttpClient> provideOkHttpClientProvider;

    Provider<SecureSettings> secureSettingsProvider;

    Provider<SettingsRepositoryImpl> settingsRepositoryImplProvider;

    Provider<NVIDIAApiService> provideNvidiaApiServiceProvider;

    Provider<NVIDIAImageApiService> provideNvidiaImageApiServiceProvider;

    Provider<NVIDIAProvider> nVIDIAProvider;

    Provider<NotificationHelper> notificationHelperProvider;

    Provider<ProactiveNudgeWorker_AssistedFactory> proactiveNudgeWorker_AssistedFactoryProvider;

    Provider<PromptEngine> promptEngineProvider;

    Provider<MemoryRepositoryImpl> memoryRepositoryImplProvider;

    Provider<EvolutionaryEngine> evolutionaryEngineProvider;

    Provider<ImageStorage> imageStorageProvider;

    Provider<ImageEngine> imageEngineProvider;

    Provider<DataInitializer> dataInitializerProvider;

    Provider<ChatRepositoryImpl> chatRepositoryImplProvider;

    Provider<StoryEngine> storyEngineProvider;

    Provider<StoryRepositoryImpl> storyRepositoryImplProvider;

    Provider<NSFWGenerator> nSFWGeneratorProvider;

    Provider<CasualPhotoGenerator> casualPhotoGeneratorProvider;

    Provider<MatchmakingEngine> matchmakingEngineProvider;

    SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    CharacterDao characterDao() {
      return DatabaseModule_ProvideCharacterDaoFactory.provideCharacterDao(provideDatabaseProvider.get());
    }

    RelationshipDao relationshipDao() {
      return DatabaseModule_ProvideRelationshipDaoFactory.provideRelationshipDao(provideDatabaseProvider.get());
    }

    Map<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>> mapOfStringAndProviderOfWorkerAssistedFactoryOf(
        ) {
      return Collections.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>singletonMap("com.heartforge.app.core.worker.ProactiveNudgeWorker", ((Provider) (proactiveNudgeWorker_AssistedFactoryProvider)));
    }

    HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(mapOfStringAndProviderOfWorkerAssistedFactoryOf());
    }

    MessageDao messageDao() {
      return DatabaseModule_ProvideMessageDaoFactory.provideMessageDao(provideDatabaseProvider.get());
    }

    MemoryDao memoryDao() {
      return DatabaseModule_ProvideMemoryDaoFactory.provideMemoryDao(provideDatabaseProvider.get());
    }

    StoryDao storyDao() {
      return DatabaseModule_ProvideStoryDaoFactory.provideStoryDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.appForegroundStateProvider = DoubleCheck.provider(new SwitchingProvider<AppForegroundState>(singletonCImpl, 0));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<HeartForgeDatabase>(singletonCImpl, 3));
      this.characterRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<CharacterRepositoryImpl>(singletonCImpl, 2));
      this.relationshipRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<RelationshipRepositoryImpl>(singletonCImpl, 4));
      this.userProfileRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<UserProfileRepositoryImpl>(singletonCImpl, 5));
      this.provideOkHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 8));
      this.secureSettingsProvider = DoubleCheck.provider(new SwitchingProvider<SecureSettings>(singletonCImpl, 10));
      this.settingsRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<SettingsRepositoryImpl>(singletonCImpl, 9));
      this.provideNvidiaApiServiceProvider = DoubleCheck.provider(new SwitchingProvider<NVIDIAApiService>(singletonCImpl, 7));
      this.provideNvidiaImageApiServiceProvider = DoubleCheck.provider(new SwitchingProvider<NVIDIAImageApiService>(singletonCImpl, 11));
      this.nVIDIAProvider = DoubleCheck.provider(new SwitchingProvider<NVIDIAProvider>(singletonCImpl, 6));
      this.notificationHelperProvider = DoubleCheck.provider(new SwitchingProvider<NotificationHelper>(singletonCImpl, 12));
      this.proactiveNudgeWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<ProactiveNudgeWorker_AssistedFactory>(singletonCImpl, 1));
      this.promptEngineProvider = DoubleCheck.provider(new SwitchingProvider<PromptEngine>(singletonCImpl, 14));
      this.memoryRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<MemoryRepositoryImpl>(singletonCImpl, 16));
      this.evolutionaryEngineProvider = DoubleCheck.provider(new SwitchingProvider<EvolutionaryEngine>(singletonCImpl, 15));
      this.imageStorageProvider = DoubleCheck.provider(new SwitchingProvider<ImageStorage>(singletonCImpl, 19));
      this.imageEngineProvider = DoubleCheck.provider(new SwitchingProvider<ImageEngine>(singletonCImpl, 18));
      this.dataInitializerProvider = DoubleCheck.provider(new SwitchingProvider<DataInitializer>(singletonCImpl, 17));
      this.chatRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ChatRepositoryImpl>(singletonCImpl, 13));
      this.storyEngineProvider = DoubleCheck.provider(new SwitchingProvider<StoryEngine>(singletonCImpl, 21));
      this.storyRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<StoryRepositoryImpl>(singletonCImpl, 20));
      this.nSFWGeneratorProvider = DoubleCheck.provider(new SwitchingProvider<NSFWGenerator>(singletonCImpl, 22));
      this.casualPhotoGeneratorProvider = DoubleCheck.provider(new SwitchingProvider<CasualPhotoGenerator>(singletonCImpl, 23));
      this.matchmakingEngineProvider = DoubleCheck.provider(new SwitchingProvider<MatchmakingEngine>(singletonCImpl, 24));
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
      HeartForgeApplication_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @Override
      @SuppressWarnings("unchecked")
      public T get() {
        switch (id) {
          case 0: // com.heartforge.app.core.util.AppForegroundState
          return (T) new AppForegroundState();

          case 1: // com.heartforge.app.core.worker.ProactiveNudgeWorker_AssistedFactory
          return (T) new ProactiveNudgeWorker_AssistedFactory() {
            @Override
            public ProactiveNudgeWorker create(Context appContext, WorkerParameters workerParams) {
              return new ProactiveNudgeWorker(appContext, workerParams, singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.relationshipRepositoryImplProvider.get(), singletonCImpl.userProfileRepositoryImplProvider.get(), singletonCImpl.nVIDIAProvider.get(), singletonCImpl.notificationHelperProvider.get());
            }
          };

          case 2: // com.heartforge.app.core.repository.CharacterRepositoryImpl
          return (T) new CharacterRepositoryImpl(singletonCImpl.characterDao());

          case 3: // com.heartforge.app.core.database.HeartForgeDatabase
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.heartforge.app.core.repository.RelationshipRepositoryImpl
          return (T) new RelationshipRepositoryImpl(singletonCImpl.relationshipDao());

          case 5: // com.heartforge.app.core.repository.UserProfileRepositoryImpl
          return (T) new UserProfileRepositoryImpl();

          case 6: // com.heartforge.app.core.ai.nvidia.NVIDIAProvider
          return (T) new NVIDIAProvider(singletonCImpl.provideNvidiaApiServiceProvider.get(), singletonCImpl.provideNvidiaImageApiServiceProvider.get(), singletonCImpl.settingsRepositoryImplProvider.get());

          case 7: // com.heartforge.app.core.network.nvidia.NVIDIAApiService
          return (T) NetworkModule_ProvideNvidiaApiServiceFactory.provideNvidiaApiService(singletonCImpl.provideOkHttpClientProvider.get(), singletonCImpl.settingsRepositoryImplProvider.get());

          case 8: // okhttp3.OkHttpClient
          return (T) NetworkModule_ProvideOkHttpClientFactory.provideOkHttpClient();

          case 9: // com.heartforge.app.core.repository.SettingsRepositoryImpl
          return (T) new SettingsRepositoryImpl(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.secureSettingsProvider.get());

          case 10: // com.heartforge.app.core.util.SecureSettings
          return (T) new SecureSettings(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 11: // com.heartforge.app.core.network.nvidia.NVIDIAImageApiService
          return (T) NetworkModule_ProvideNvidiaImageApiServiceFactory.provideNvidiaImageApiService(singletonCImpl.provideOkHttpClientProvider.get());

          case 12: // com.heartforge.app.core.util.NotificationHelper
          return (T) new NotificationHelper(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 13: // com.heartforge.app.core.repository.ChatRepositoryImpl
          return (T) new ChatRepositoryImpl(singletonCImpl.messageDao(), singletonCImpl.nVIDIAProvider.get(), singletonCImpl.promptEngineProvider.get(), singletonCImpl.evolutionaryEngineProvider.get(), singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.relationshipRepositoryImplProvider.get(), singletonCImpl.dataInitializerProvider.get(), singletonCImpl.memoryDao(), singletonCImpl.imageEngineProvider.get(), singletonCImpl.userProfileRepositoryImplProvider.get(), singletonCImpl.notificationHelperProvider.get(), singletonCImpl.appForegroundStateProvider.get());

          case 14: // com.heartforge.app.core.ai.PromptEngine
          return (T) new PromptEngine();

          case 15: // com.heartforge.app.core.ai.EvolutionaryEngine
          return (T) new EvolutionaryEngine(singletonCImpl.nVIDIAProvider.get(), singletonCImpl.relationshipRepositoryImplProvider.get(), singletonCImpl.memoryRepositoryImplProvider.get());

          case 16: // com.heartforge.app.core.repository.MemoryRepositoryImpl
          return (T) new MemoryRepositoryImpl(singletonCImpl.memoryDao(), singletonCImpl.nVIDIAProvider.get(), singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.userProfileRepositoryImplProvider.get());

          case 17: // com.heartforge.app.core.util.DataInitializer
          return (T) new DataInitializer(singletonCImpl.characterDao(), singletonCImpl.messageDao(), singletonCImpl.memoryDao(), singletonCImpl.relationshipDao(), singletonCImpl.storyDao(), singletonCImpl.imageEngineProvider.get(), singletonCImpl.provideNvidiaApiServiceProvider.get(), singletonCImpl.settingsRepositoryImplProvider.get());

          case 18: // com.heartforge.app.core.ai.ImageEngine
          return (T) new ImageEngine(singletonCImpl.nVIDIAProvider.get(), singletonCImpl.imageStorageProvider.get());

          case 19: // com.heartforge.app.core.util.ImageStorage
          return (T) new ImageStorage(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 20: // com.heartforge.app.core.repository.StoryRepositoryImpl
          return (T) new StoryRepositoryImpl(singletonCImpl.storyDao(), singletonCImpl.storyEngineProvider.get(), singletonCImpl.memoryDao(), singletonCImpl.relationshipRepositoryImplProvider.get(), singletonCImpl.userProfileRepositoryImplProvider.get());

          case 21: // com.heartforge.app.core.ai.StoryEngine
          return (T) new StoryEngine(singletonCImpl.nVIDIAProvider.get(), singletonCImpl.memoryRepositoryImplProvider.get());

          case 22: // com.heartforge.app.core.ai.NSFWGenerator
          return (T) new NSFWGenerator(singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.relationshipRepositoryImplProvider.get(), singletonCImpl.imageEngineProvider.get());

          case 23: // com.heartforge.app.core.ai.CasualPhotoGenerator
          return (T) new CasualPhotoGenerator(singletonCImpl.characterRepositoryImplProvider.get(), singletonCImpl.relationshipRepositoryImplProvider.get(), singletonCImpl.imageEngineProvider.get());

          case 24: // com.heartforge.app.core.ai.MatchmakingEngine
          return (T) new MatchmakingEngine(singletonCImpl.nVIDIAProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
