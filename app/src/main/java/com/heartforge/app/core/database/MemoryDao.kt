package com.heartforge.app.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoryDao {
    @Query("SELECT * FROM memories WHERE characterId = :characterId ORDER BY timestamp DESC")
    fun getMemories(characterId: String): Flow<List<MemoryEntity>>

    @Query("SELECT * FROM memories WHERE characterId = :characterId AND importance >= :minImportance ORDER BY importance DESC, timestamp DESC")
    suspend fun getRelevantMemories(characterId: String, minImportance: Int): List<MemoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: MemoryEntity)

    @Query("DELETE FROM memories WHERE id = :id")
    suspend fun deleteMemory(id: String)
}
