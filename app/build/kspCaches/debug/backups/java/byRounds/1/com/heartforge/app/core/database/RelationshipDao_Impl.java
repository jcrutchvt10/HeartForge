package com.heartforge.app.core.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RelationshipDao_Impl implements RelationshipDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RelationshipEntity> __insertionAdapterOfRelationshipEntity;

  private final Converters __converters = new Converters();

  private final SharedSQLiteStatement __preparedStmtOfDeleteRelationship;

  public RelationshipDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRelationshipEntity = new EntityInsertionAdapter<RelationshipEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `relationships` (`characterId`,`trust`,`romance`,`comfort`,`affection`,`jealousy`,`loyalty`,`intimacy`,`playfulness`,`excitement`,`mood`,`insideJokes`,`sharedActivities`,`futurePlans`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RelationshipEntity entity) {
        statement.bindString(1, entity.getCharacterId());
        statement.bindLong(2, entity.getTrust());
        statement.bindLong(3, entity.getRomance());
        statement.bindLong(4, entity.getComfort());
        statement.bindLong(5, entity.getAffection());
        statement.bindLong(6, entity.getJealousy());
        statement.bindLong(7, entity.getLoyalty());
        statement.bindLong(8, entity.getIntimacy());
        statement.bindLong(9, entity.getPlayfulness());
        statement.bindLong(10, entity.getExcitement());
        statement.bindString(11, entity.getMood());
        final String _tmp = __converters.fromStringList(entity.getInsideJokes());
        statement.bindString(12, _tmp);
        final String _tmp_1 = __converters.fromStringList(entity.getSharedActivities());
        statement.bindString(13, _tmp_1);
        final String _tmp_2 = __converters.fromStringList(entity.getFuturePlans());
        statement.bindString(14, _tmp_2);
      }
    };
    this.__preparedStmtOfDeleteRelationship = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM relationships WHERE characterId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertRelationship(final RelationshipEntity relationship,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRelationshipEntity.insert(relationship);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteRelationship(final String characterId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteRelationship.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, characterId);
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
          __preparedStmtOfDeleteRelationship.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<RelationshipEntity> getRelationship(final String characterId) {
    final String _sql = "SELECT * FROM relationships WHERE characterId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, characterId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"relationships"}, new Callable<RelationshipEntity>() {
      @Override
      @Nullable
      public RelationshipEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCharacterId = CursorUtil.getColumnIndexOrThrow(_cursor, "characterId");
          final int _cursorIndexOfTrust = CursorUtil.getColumnIndexOrThrow(_cursor, "trust");
          final int _cursorIndexOfRomance = CursorUtil.getColumnIndexOrThrow(_cursor, "romance");
          final int _cursorIndexOfComfort = CursorUtil.getColumnIndexOrThrow(_cursor, "comfort");
          final int _cursorIndexOfAffection = CursorUtil.getColumnIndexOrThrow(_cursor, "affection");
          final int _cursorIndexOfJealousy = CursorUtil.getColumnIndexOrThrow(_cursor, "jealousy");
          final int _cursorIndexOfLoyalty = CursorUtil.getColumnIndexOrThrow(_cursor, "loyalty");
          final int _cursorIndexOfIntimacy = CursorUtil.getColumnIndexOrThrow(_cursor, "intimacy");
          final int _cursorIndexOfPlayfulness = CursorUtil.getColumnIndexOrThrow(_cursor, "playfulness");
          final int _cursorIndexOfExcitement = CursorUtil.getColumnIndexOrThrow(_cursor, "excitement");
          final int _cursorIndexOfMood = CursorUtil.getColumnIndexOrThrow(_cursor, "mood");
          final int _cursorIndexOfInsideJokes = CursorUtil.getColumnIndexOrThrow(_cursor, "insideJokes");
          final int _cursorIndexOfSharedActivities = CursorUtil.getColumnIndexOrThrow(_cursor, "sharedActivities");
          final int _cursorIndexOfFuturePlans = CursorUtil.getColumnIndexOrThrow(_cursor, "futurePlans");
          final RelationshipEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpCharacterId;
            _tmpCharacterId = _cursor.getString(_cursorIndexOfCharacterId);
            final int _tmpTrust;
            _tmpTrust = _cursor.getInt(_cursorIndexOfTrust);
            final int _tmpRomance;
            _tmpRomance = _cursor.getInt(_cursorIndexOfRomance);
            final int _tmpComfort;
            _tmpComfort = _cursor.getInt(_cursorIndexOfComfort);
            final int _tmpAffection;
            _tmpAffection = _cursor.getInt(_cursorIndexOfAffection);
            final int _tmpJealousy;
            _tmpJealousy = _cursor.getInt(_cursorIndexOfJealousy);
            final int _tmpLoyalty;
            _tmpLoyalty = _cursor.getInt(_cursorIndexOfLoyalty);
            final int _tmpIntimacy;
            _tmpIntimacy = _cursor.getInt(_cursorIndexOfIntimacy);
            final int _tmpPlayfulness;
            _tmpPlayfulness = _cursor.getInt(_cursorIndexOfPlayfulness);
            final int _tmpExcitement;
            _tmpExcitement = _cursor.getInt(_cursorIndexOfExcitement);
            final String _tmpMood;
            _tmpMood = _cursor.getString(_cursorIndexOfMood);
            final List<String> _tmpInsideJokes;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfInsideJokes);
            _tmpInsideJokes = __converters.toStringList(_tmp);
            final List<String> _tmpSharedActivities;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfSharedActivities);
            _tmpSharedActivities = __converters.toStringList(_tmp_1);
            final List<String> _tmpFuturePlans;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfFuturePlans);
            _tmpFuturePlans = __converters.toStringList(_tmp_2);
            _result = new RelationshipEntity(_tmpCharacterId,_tmpTrust,_tmpRomance,_tmpComfort,_tmpAffection,_tmpJealousy,_tmpLoyalty,_tmpIntimacy,_tmpPlayfulness,_tmpExcitement,_tmpMood,_tmpInsideJokes,_tmpSharedActivities,_tmpFuturePlans);
          } else {
            _result = null;
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
