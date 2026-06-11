package com.heartforge.app.core.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class HeartForgeDatabase_Impl extends HeartForgeDatabase {
  private volatile CharacterDao _characterDao;

  private volatile RelationshipDao _relationshipDao;

  private volatile MemoryDao _memoryDao;

  private volatile MessageDao _messageDao;

  private volatile StoryDao _storyDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `characters` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `age` INTEGER NOT NULL, `occupation` TEXT NOT NULL, `location` TEXT NOT NULL, `biography` TEXT NOT NULL, `appearance` TEXT NOT NULL, `personality` TEXT NOT NULL, `interests` TEXT NOT NULL, `hobbies` TEXT NOT NULL, `likes` TEXT NOT NULL, `dislikes` TEXT NOT NULL, `relationshipStyle` TEXT NOT NULL, `imageProfile` TEXT NOT NULL, `promptProfile` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `relationships` (`characterId` TEXT NOT NULL, `trust` INTEGER NOT NULL, `romance` INTEGER NOT NULL, `comfort` INTEGER NOT NULL, `affection` INTEGER NOT NULL, `jealousy` INTEGER NOT NULL, `loyalty` INTEGER NOT NULL, `intimacy` INTEGER NOT NULL, `playfulness` INTEGER NOT NULL, `excitement` INTEGER NOT NULL, `mood` TEXT NOT NULL, `insideJokes` TEXT NOT NULL, `sharedActivities` TEXT NOT NULL, `futurePlans` TEXT NOT NULL, PRIMARY KEY(`characterId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `memories` (`id` TEXT NOT NULL, `characterId` TEXT NOT NULL, `content` TEXT NOT NULL, `importance` INTEGER NOT NULL, `category` TEXT NOT NULL, `sentiment` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `messages` (`id` TEXT NOT NULL, `characterId` TEXT NOT NULL, `role` TEXT NOT NULL, `content` TEXT NOT NULL, `imageUrl` TEXT, `timestamp` INTEGER NOT NULL, `status` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `story_progress` (`characterId` TEXT NOT NULL, `arcId` TEXT NOT NULL, `completedChapterIds` TEXT NOT NULL, `activeChapterId` TEXT, PRIMARY KEY(`characterId`, `arcId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'd287937a7ca30bbd04b322c1d26a23e2')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `characters`");
        db.execSQL("DROP TABLE IF EXISTS `relationships`");
        db.execSQL("DROP TABLE IF EXISTS `memories`");
        db.execSQL("DROP TABLE IF EXISTS `messages`");
        db.execSQL("DROP TABLE IF EXISTS `story_progress`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsCharacters = new HashMap<String, TableInfo.Column>(15);
        _columnsCharacters.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("age", new TableInfo.Column("age", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("occupation", new TableInfo.Column("occupation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("location", new TableInfo.Column("location", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("biography", new TableInfo.Column("biography", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("appearance", new TableInfo.Column("appearance", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("personality", new TableInfo.Column("personality", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("interests", new TableInfo.Column("interests", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("hobbies", new TableInfo.Column("hobbies", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("likes", new TableInfo.Column("likes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("dislikes", new TableInfo.Column("dislikes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("relationshipStyle", new TableInfo.Column("relationshipStyle", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("imageProfile", new TableInfo.Column("imageProfile", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("promptProfile", new TableInfo.Column("promptProfile", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCharacters = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCharacters = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCharacters = new TableInfo("characters", _columnsCharacters, _foreignKeysCharacters, _indicesCharacters);
        final TableInfo _existingCharacters = TableInfo.read(db, "characters");
        if (!_infoCharacters.equals(_existingCharacters)) {
          return new RoomOpenHelper.ValidationResult(false, "characters(com.heartforge.app.core.database.CharacterEntity).\n"
                  + " Expected:\n" + _infoCharacters + "\n"
                  + " Found:\n" + _existingCharacters);
        }
        final HashMap<String, TableInfo.Column> _columnsRelationships = new HashMap<String, TableInfo.Column>(14);
        _columnsRelationships.put("characterId", new TableInfo.Column("characterId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRelationships.put("trust", new TableInfo.Column("trust", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRelationships.put("romance", new TableInfo.Column("romance", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRelationships.put("comfort", new TableInfo.Column("comfort", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRelationships.put("affection", new TableInfo.Column("affection", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRelationships.put("jealousy", new TableInfo.Column("jealousy", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRelationships.put("loyalty", new TableInfo.Column("loyalty", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRelationships.put("intimacy", new TableInfo.Column("intimacy", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRelationships.put("playfulness", new TableInfo.Column("playfulness", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRelationships.put("excitement", new TableInfo.Column("excitement", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRelationships.put("mood", new TableInfo.Column("mood", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRelationships.put("insideJokes", new TableInfo.Column("insideJokes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRelationships.put("sharedActivities", new TableInfo.Column("sharedActivities", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRelationships.put("futurePlans", new TableInfo.Column("futurePlans", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRelationships = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRelationships = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRelationships = new TableInfo("relationships", _columnsRelationships, _foreignKeysRelationships, _indicesRelationships);
        final TableInfo _existingRelationships = TableInfo.read(db, "relationships");
        if (!_infoRelationships.equals(_existingRelationships)) {
          return new RoomOpenHelper.ValidationResult(false, "relationships(com.heartforge.app.core.database.RelationshipEntity).\n"
                  + " Expected:\n" + _infoRelationships + "\n"
                  + " Found:\n" + _existingRelationships);
        }
        final HashMap<String, TableInfo.Column> _columnsMemories = new HashMap<String, TableInfo.Column>(7);
        _columnsMemories.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("characterId", new TableInfo.Column("characterId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("importance", new TableInfo.Column("importance", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("sentiment", new TableInfo.Column("sentiment", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMemories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMemories = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMemories = new TableInfo("memories", _columnsMemories, _foreignKeysMemories, _indicesMemories);
        final TableInfo _existingMemories = TableInfo.read(db, "memories");
        if (!_infoMemories.equals(_existingMemories)) {
          return new RoomOpenHelper.ValidationResult(false, "memories(com.heartforge.app.core.database.MemoryEntity).\n"
                  + " Expected:\n" + _infoMemories + "\n"
                  + " Found:\n" + _existingMemories);
        }
        final HashMap<String, TableInfo.Column> _columnsMessages = new HashMap<String, TableInfo.Column>(7);
        _columnsMessages.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("characterId", new TableInfo.Column("characterId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessages.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMessages = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMessages = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMessages = new TableInfo("messages", _columnsMessages, _foreignKeysMessages, _indicesMessages);
        final TableInfo _existingMessages = TableInfo.read(db, "messages");
        if (!_infoMessages.equals(_existingMessages)) {
          return new RoomOpenHelper.ValidationResult(false, "messages(com.heartforge.app.core.database.MessageEntity).\n"
                  + " Expected:\n" + _infoMessages + "\n"
                  + " Found:\n" + _existingMessages);
        }
        final HashMap<String, TableInfo.Column> _columnsStoryProgress = new HashMap<String, TableInfo.Column>(4);
        _columnsStoryProgress.put("characterId", new TableInfo.Column("characterId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStoryProgress.put("arcId", new TableInfo.Column("arcId", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStoryProgress.put("completedChapterIds", new TableInfo.Column("completedChapterIds", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStoryProgress.put("activeChapterId", new TableInfo.Column("activeChapterId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStoryProgress = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStoryProgress = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStoryProgress = new TableInfo("story_progress", _columnsStoryProgress, _foreignKeysStoryProgress, _indicesStoryProgress);
        final TableInfo _existingStoryProgress = TableInfo.read(db, "story_progress");
        if (!_infoStoryProgress.equals(_existingStoryProgress)) {
          return new RoomOpenHelper.ValidationResult(false, "story_progress(com.heartforge.app.core.database.StoryProgressEntity).\n"
                  + " Expected:\n" + _infoStoryProgress + "\n"
                  + " Found:\n" + _existingStoryProgress);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "d287937a7ca30bbd04b322c1d26a23e2", "1e249cb64b5d4929604342dda5840521");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "characters","relationships","memories","messages","story_progress");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `characters`");
      _db.execSQL("DELETE FROM `relationships`");
      _db.execSQL("DELETE FROM `memories`");
      _db.execSQL("DELETE FROM `messages`");
      _db.execSQL("DELETE FROM `story_progress`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(CharacterDao.class, CharacterDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RelationshipDao.class, RelationshipDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MemoryDao.class, MemoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MessageDao.class, MessageDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StoryDao.class, StoryDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public CharacterDao characterDao() {
    if (_characterDao != null) {
      return _characterDao;
    } else {
      synchronized(this) {
        if(_characterDao == null) {
          _characterDao = new CharacterDao_Impl(this);
        }
        return _characterDao;
      }
    }
  }

  @Override
  public RelationshipDao relationshipDao() {
    if (_relationshipDao != null) {
      return _relationshipDao;
    } else {
      synchronized(this) {
        if(_relationshipDao == null) {
          _relationshipDao = new RelationshipDao_Impl(this);
        }
        return _relationshipDao;
      }
    }
  }

  @Override
  public MemoryDao memoryDao() {
    if (_memoryDao != null) {
      return _memoryDao;
    } else {
      synchronized(this) {
        if(_memoryDao == null) {
          _memoryDao = new MemoryDao_Impl(this);
        }
        return _memoryDao;
      }
    }
  }

  @Override
  public MessageDao messageDao() {
    if (_messageDao != null) {
      return _messageDao;
    } else {
      synchronized(this) {
        if(_messageDao == null) {
          _messageDao = new MessageDao_Impl(this);
        }
        return _messageDao;
      }
    }
  }

  @Override
  public StoryDao storyDao() {
    if (_storyDao != null) {
      return _storyDao;
    } else {
      synchronized(this) {
        if(_storyDao == null) {
          _storyDao = new StoryDao_Impl(this);
        }
        return _storyDao;
      }
    }
  }
}
