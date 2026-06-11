package com.heartforge.app.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE characterId = :characterId ORDER BY timestamp ASC")
    fun getMessages(characterId: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE characterId = :characterId ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentMessages(characterId: String, limit: Int): List<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("DELETE FROM messages WHERE characterId = :characterId")
    suspend fun deleteMessagesForCharacter(characterId: String)
    
    @Query("SELECT DISTINCT characterId FROM messages")
    suspend fun getCharacterIdsWithMessages(): List<String>
}
