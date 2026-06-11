package com.heartforge.app.core.database;

import android.database.Cursor;
import android.os.CancellationSignal;
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
import com.heartforge.app.core.model.Appearance;
import com.heartforge.app.core.model.ImageProfile;
import com.heartforge.app.core.model.Interest;
import com.heartforge.app.core.model.Personality;
import com.heartforge.app.core.model.PromptProfile;
import com.heartforge.app.core.model.RelationshipStyle;
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
public final class CharacterDao_Impl implements CharacterDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CharacterEntity> __insertionAdapterOfCharacterEntity;

  private final Converters __converters = new Converters();

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCharacter;

  public CharacterDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCharacterEntity = new EntityInsertionAdapter<CharacterEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `characters` (`id`,`name`,`age`,`occupation`,`location`,`biography`,`appearance`,`personality`,`interests`,`hobbies`,`likes`,`dislikes`,`relationshipStyle`,`imageProfile`,`promptProfile`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CharacterEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getAge());
        statement.bindString(4, entity.getOccupation());
        statement.bindString(5, entity.getLocation());
        statement.bindString(6, entity.getBiography());
        final String _tmp = __converters.fromAppearance(entity.getAppearance());
        statement.bindString(7, _tmp);
        final String _tmp_1 = __converters.fromPersonality(entity.getPersonality());
        statement.bindString(8, _tmp_1);
        final String _tmp_2 = __converters.fromInterestList(entity.getInterests());
        statement.bindString(9, _tmp_2);
        final String _tmp_3 = __converters.fromStringList(entity.getHobbies());
        statement.bindString(10, _tmp_3);
        final String _tmp_4 = __converters.fromStringList(entity.getLikes());
        statement.bindString(11, _tmp_4);
        final String _tmp_5 = __converters.fromStringList(entity.getDislikes());
        statement.bindString(12, _tmp_5);
        final String _tmp_6 = __converters.fromRelationshipStyle(entity.getRelationshipStyle());
        statement.bindString(13, _tmp_6);
        final String _tmp_7 = __converters.fromImageProfile(entity.getImageProfile());
        statement.bindString(14, _tmp_7);
        final String _tmp_8 = __converters.fromPromptProfile(entity.getPromptProfile());
        statement.bindString(15, _tmp_8);
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM characters";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteCharacter = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM characters WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertCharacter(final CharacterEntity character,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCharacterEntity.insert(character);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertCharacters(final List<CharacterEntity> characters,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCharacterEntity.insert(characters);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
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
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCharacter(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCharacter.acquire();
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
          __preparedStmtOfDeleteCharacter.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CharacterEntity>> getCharacters() {
    final String _sql = "SELECT * FROM characters";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"characters"}, new Callable<List<CharacterEntity>>() {
      @Override
      @NonNull
      public List<CharacterEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfOccupation = CursorUtil.getColumnIndexOrThrow(_cursor, "occupation");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfBiography = CursorUtil.getColumnIndexOrThrow(_cursor, "biography");
          final int _cursorIndexOfAppearance = CursorUtil.getColumnIndexOrThrow(_cursor, "appearance");
          final int _cursorIndexOfPersonality = CursorUtil.getColumnIndexOrThrow(_cursor, "personality");
          final int _cursorIndexOfInterests = CursorUtil.getColumnIndexOrThrow(_cursor, "interests");
          final int _cursorIndexOfHobbies = CursorUtil.getColumnIndexOrThrow(_cursor, "hobbies");
          final int _cursorIndexOfLikes = CursorUtil.getColumnIndexOrThrow(_cursor, "likes");
          final int _cursorIndexOfDislikes = CursorUtil.getColumnIndexOrThrow(_cursor, "dislikes");
          final int _cursorIndexOfRelationshipStyle = CursorUtil.getColumnIndexOrThrow(_cursor, "relationshipStyle");
          final int _cursorIndexOfImageProfile = CursorUtil.getColumnIndexOrThrow(_cursor, "imageProfile");
          final int _cursorIndexOfPromptProfile = CursorUtil.getColumnIndexOrThrow(_cursor, "promptProfile");
          final List<CharacterEntity> _result = new ArrayList<CharacterEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CharacterEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpAge;
            _tmpAge = _cursor.getInt(_cursorIndexOfAge);
            final String _tmpOccupation;
            _tmpOccupation = _cursor.getString(_cursorIndexOfOccupation);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final String _tmpBiography;
            _tmpBiography = _cursor.getString(_cursorIndexOfBiography);
            final Appearance _tmpAppearance;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfAppearance);
            _tmpAppearance = __converters.toAppearance(_tmp);
            final Personality _tmpPersonality;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfPersonality);
            _tmpPersonality = __converters.toPersonality(_tmp_1);
            final List<Interest> _tmpInterests;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfInterests);
            _tmpInterests = __converters.toInterestList(_tmp_2);
            final List<String> _tmpHobbies;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfHobbies);
            _tmpHobbies = __converters.toStringList(_tmp_3);
            final List<String> _tmpLikes;
            final String _tmp_4;
            _tmp_4 = _cursor.getString(_cursorIndexOfLikes);
            _tmpLikes = __converters.toStringList(_tmp_4);
            final List<String> _tmpDislikes;
            final String _tmp_5;
            _tmp_5 = _cursor.getString(_cursorIndexOfDislikes);
            _tmpDislikes = __converters.toStringList(_tmp_5);
            final RelationshipStyle _tmpRelationshipStyle;
            final String _tmp_6;
            _tmp_6 = _cursor.getString(_cursorIndexOfRelationshipStyle);
            _tmpRelationshipStyle = __converters.toRelationshipStyle(_tmp_6);
            final ImageProfile _tmpImageProfile;
            final String _tmp_7;
            _tmp_7 = _cursor.getString(_cursorIndexOfImageProfile);
            _tmpImageProfile = __converters.toImageProfile(_tmp_7);
            final PromptProfile _tmpPromptProfile;
            final String _tmp_8;
            _tmp_8 = _cursor.getString(_cursorIndexOfPromptProfile);
            _tmpPromptProfile = __converters.toPromptProfile(_tmp_8);
            _item = new CharacterEntity(_tmpId,_tmpName,_tmpAge,_tmpOccupation,_tmpLocation,_tmpBiography,_tmpAppearance,_tmpPersonality,_tmpInterests,_tmpHobbies,_tmpLikes,_tmpDislikes,_tmpRelationshipStyle,_tmpImageProfile,_tmpPromptProfile);
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
  public Object getCharacter(final String id,
      final Continuation<? super CharacterEntity> $completion) {
    final String _sql = "SELECT * FROM characters WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CharacterEntity>() {
      @Override
      @Nullable
      public CharacterEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfOccupation = CursorUtil.getColumnIndexOrThrow(_cursor, "occupation");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfBiography = CursorUtil.getColumnIndexOrThrow(_cursor, "biography");
          final int _cursorIndexOfAppearance = CursorUtil.getColumnIndexOrThrow(_cursor, "appearance");
          final int _cursorIndexOfPersonality = CursorUtil.getColumnIndexOrThrow(_cursor, "personality");
          final int _cursorIndexOfInterests = CursorUtil.getColumnIndexOrThrow(_cursor, "interests");
          final int _cursorIndexOfHobbies = CursorUtil.getColumnIndexOrThrow(_cursor, "hobbies");
          final int _cursorIndexOfLikes = CursorUtil.getColumnIndexOrThrow(_cursor, "likes");
          final int _cursorIndexOfDislikes = CursorUtil.getColumnIndexOrThrow(_cursor, "dislikes");
          final int _cursorIndexOfRelationshipStyle = CursorUtil.getColumnIndexOrThrow(_cursor, "relationshipStyle");
          final int _cursorIndexOfImageProfile = CursorUtil.getColumnIndexOrThrow(_cursor, "imageProfile");
          final int _cursorIndexOfPromptProfile = CursorUtil.getColumnIndexOrThrow(_cursor, "promptProfile");
          final CharacterEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpAge;
            _tmpAge = _cursor.getInt(_cursorIndexOfAge);
            final String _tmpOccupation;
            _tmpOccupation = _cursor.getString(_cursorIndexOfOccupation);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final String _tmpBiography;
            _tmpBiography = _cursor.getString(_cursorIndexOfBiography);
            final Appearance _tmpAppearance;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfAppearance);
            _tmpAppearance = __converters.toAppearance(_tmp);
            final Personality _tmpPersonality;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfPersonality);
            _tmpPersonality = __converters.toPersonality(_tmp_1);
            final List<Interest> _tmpInterests;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfInterests);
            _tmpInterests = __converters.toInterestList(_tmp_2);
            final List<String> _tmpHobbies;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfHobbies);
            _tmpHobbies = __converters.toStringList(_tmp_3);
            final List<String> _tmpLikes;
            final String _tmp_4;
            _tmp_4 = _cursor.getString(_cursorIndexOfLikes);
            _tmpLikes = __converters.toStringList(_tmp_4);
            final List<String> _tmpDislikes;
            final String _tmp_5;
            _tmp_5 = _cursor.getString(_cursorIndexOfDislikes);
            _tmpDislikes = __converters.toStringList(_tmp_5);
            final RelationshipStyle _tmpRelationshipStyle;
            final String _tmp_6;
            _tmp_6 = _cursor.getString(_cursorIndexOfRelationshipStyle);
            _tmpRelationshipStyle = __converters.toRelationshipStyle(_tmp_6);
            final ImageProfile _tmpImageProfile;
            final String _tmp_7;
            _tmp_7 = _cursor.getString(_cursorIndexOfImageProfile);
            _tmpImageProfile = __converters.toImageProfile(_tmp_7);
            final PromptProfile _tmpPromptProfile;
            final String _tmp_8;
            _tmp_8 = _cursor.getString(_cursorIndexOfPromptProfile);
            _tmpPromptProfile = __converters.toPromptProfile(_tmp_8);
            _result = new CharacterEntity(_tmpId,_tmpName,_tmpAge,_tmpOccupation,_tmpLocation,_tmpBiography,_tmpAppearance,_tmpPersonality,_tmpInterests,_tmpHobbies,_tmpLikes,_tmpDislikes,_tmpRelationshipStyle,_tmpImageProfile,_tmpPromptProfile);
          } else {
            _result = null;
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
