package com.heartforge.app.core.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.heartforge.app.core.model.MemoryCategory;
import com.heartforge.app.core.model.Sentiment;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MemoryDao_Impl implements MemoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MemoryEntity> __insertionAdapterOfMemoryEntity;

  private final Converters __converters = new Converters();

  private final SharedSQLiteStatement __preparedStmtOfDeleteMemory;

  public MemoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMemoryEntity = new EntityInsertionAdapter<MemoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `memories` (`id`,`characterId`,`content`,`importance`,`category`,`sentiment`,`timestamp`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MemoryEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getCharacterId());
        statement.bindString(3, entity.getContent());
        statement.bindLong(4, entity.getImportance());
        final String _tmp = __converters.fromMemoryCategory(entity.getCategory());
        statement.bindString(5, _tmp);
        final String _tmp_1 = __converters.fromSentiment(entity.getSentiment());
        statement.bindString(6, _tmp_1);
        final Long _tmp_2 = __converters.dateToTimestamp(entity.getTimestamp());
        if (_tmp_2 == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, _tmp_2);
        }
      }
    };
    this.__preparedStmtOfDeleteMemory = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM memories WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertMemory(final MemoryEntity memory,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMemoryEntity.insert(memory);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMemory(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMemory.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteMemory.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MemoryEntity>> getMemories(final String characterId) {
    final String _sql = "SELECT * FROM memories WHERE characterId = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, characterId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"memories"}, new Callable<List<MemoryEntity>>() {
      @Override
      @NonNull
      public List<MemoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCharacterId = CursorUtil.getColumnIndexOrThrow(_cursor, "characterId");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfImportance = CursorUtil.getColumnIndexOrThrow(_cursor, "importance");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSentiment = CursorUtil.getColumnIndexOrThrow(_cursor, "sentiment");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<MemoryEntity> _result = new ArrayList<MemoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MemoryEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpCharacterId;
            _tmpCharacterId = _cursor.getString(_cursorIndexOfCharacterId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final int _tmpImportance;
            _tmpImportance = _cursor.getInt(_cursorIndexOfImportance);
            final MemoryCategory _tmpCategory;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfCategory);
            _tmpCategory = __converters.toMemoryCategory(_tmp);
            final Sentiment _tmpSentiment;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfSentiment);
            _tmpSentiment = __converters.toSentiment(_tmp_1);
            final Instant _tmpTimestamp;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfTimestamp)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfTimestamp);
            }
            final Instant _tmp_3 = __converters.fromTimestamp(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.Instant', but it was NULL.");
            } else {
              _tmpTimestamp = _tmp_3;
            }
            _item = new MemoryEntity(_tmpId,_tmpCharacterId,_tmpContent,_tmpImportance,_tmpCategory,_tmpSentiment,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getRelevantMemories(final String characterId, final int minImportance,
      final Continuation<? super List<MemoryEntity>> $completion) {
    final String _sql = "SELECT * FROM memories WHERE characterId = ? AND importance >= ? ORDER BY importance DESC, timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, characterId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, minImportance);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MemoryEntity>>() {
      @Override
      @NonNull
      public List<MemoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCharacterId = CursorUtil.getColumnIndexOrThrow(_cursor, "characterId");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfImportance = CursorUtil.getColumnIndexOrThrow(_cursor, "importance");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSentiment = CursorUtil.getColumnIndexOrThrow(_cursor, "sentiment");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<MemoryEntity> _result = new ArrayList<MemoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MemoryEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpCharacterId;
            _tmpCharacterId = _cursor.getString(_cursorIndexOfCharacterId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final int _tmpImportance;
            _tmpImportance = _cursor.getInt(_cursorIndexOfImportance);
            final MemoryCategory _tmpCategory;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfCategory);
            _tmpCategory = __converters.toMemoryCategory(_tmp);
            final Sentiment _tmpSentiment;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfSentiment);
            _tmpSentiment = __converters.toSentiment(_tmp_1);
            final Instant _tmpTimestamp;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfTimestamp)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfTimestamp);
            }
            final Instant _tmp_3 = __converters.fromTimestamp(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.Instant', but it was NULL.");
            } else {
              _tmpTimestamp = _tmp_3;
            }
            _item = new MemoryEntity(_tmpId,_tmpCharacterId,_tmpContent,_tmpImportance,_tmpCategory,_tmpSentiment,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
