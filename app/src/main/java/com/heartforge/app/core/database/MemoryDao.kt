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

    @Query("UPDATE memories SET content = :content, importance = :importance, category = :category, sentiment = :sentiment WHERE id = :id")
    suspend fun updateMemory(id: String, content: String, importance: Int, category: com.heartforge.app.core.model.MemoryCategory, sentiment: com.heartforge.app.core.model.Sentiment)

    @Query("DELETE FROM memories WHERE id = :id")
    suspend fun deleteMemory(id: String)
}
