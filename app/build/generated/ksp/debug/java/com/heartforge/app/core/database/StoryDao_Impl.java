package com.heartforge.app.core.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
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
public final class StoryDao_Impl implements StoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StoryProgressEntity> __insertionAdapterOfStoryProgressEntity;

  private final Converters __converters = new Converters();

  private final SharedSQLiteStatement __preparedStmtOfDeleteProgress;

  public StoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStoryProgressEntity = new EntityInsertionAdapter<StoryProgressEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `story_progress` (`characterId`,`arcId`,`completedChapterIds`,`activeChapterId`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StoryProgressEntity entity) {
        statement.bindString(1, entity.getCharacterId());
        statement.bindString(2, entity.getArcId());
        final String _tmp = __converters.fromStringList(entity.getCompletedChapterIds());
        statement.bindString(3, _tmp);
        if (entity.getActiveChapterId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getActiveChapterId());
        }
      }
    };
    this.__preparedStmtOfDeleteProgress = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM story_progress WHERE characterId = ? AND arcId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertProgress(final StoryProgressEntity progress,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfStoryProgressEntity.insert(progress);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteProgress(final String characterId, final String arcId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteProgress.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, characterId);
        _argIndex = 2;
        _stmt.bindString(_argIndex, arcId);
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
          __preparedStmtOfDeleteProgress.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<StoryProgressEntity>> getProgressForCharacter(final String characterId) {
    final String _sql = "SELECT * FROM story_progress WHERE characterId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, characterId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"story_progress"}, new Callable<List<StoryProgressEntity>>() {
      @Override
      @NonNull
      public List<StoryProgressEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCharacterId = CursorUtil.getColumnIndexOrThrow(_cursor, "characterId");
          final int _cursorIndexOfArcId = CursorUtil.getColumnIndexOrThrow(_cursor, "arcId");
          final int _cursorIndexOfCompletedChapterIds = CursorUtil.getColumnIndexOrThrow(_cursor, "completedChapterIds");
          final int _cursorIndexOfActiveChapterId = CursorUtil.getColumnIndexOrThrow(_cursor, "activeChapterId");
          final List<StoryProgressEntity> _result = new ArrayList<StoryProgressEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StoryProgressEntity _item;
            final String _tmpCharacterId;
            _tmpCharacterId = _cursor.getString(_cursorIndexOfCharacterId);
            final String _tmpArcId;
            _tmpArcId = _cursor.getString(_cursorIndexOfArcId);
            final List<String> _tmpCompletedChapterIds;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfCompletedChapterIds);
            _tmpCompletedChapterIds = __converters.toStringList(_tmp);
            final String _tmpActiveChapterId;
            if (_cursor.isNull(_cursorIndexOfActiveChapterId)) {
              _tmpActiveChapterId = null;
            } else {
              _tmpActiveChapterId = _cursor.getString(_cursorIndexOfActiveChapterId);
            }
            _item = new StoryProgressEntity(_tmpCharacterId,_tmpArcId,_tmpCompletedChapterIds,_tmpActiveChapterId);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
