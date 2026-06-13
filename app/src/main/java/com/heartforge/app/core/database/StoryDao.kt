package com.heartforge.app.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {
    @Query("SELECT * FROM story_progress WHERE characterId = :characterId")
    fun getProgressForCharacter(characterId: String): Flow<List<StoryProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: StoryProgressEntity)

    @Query("DELETE FROM story_progress WHERE characterId = :characterId AND arcId = :arcId")
    suspend fun deleteProgress(characterId: String, arcId: String)

    @Query("DELETE FROM story_progress")
    suspend fun deleteAll()

    @Query("SELECT * FROM dynamic_stories WHERE characterId = :characterId")
    fun getDynamicStories(characterId: String): Flow<List<DynamicStoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDynamicStory(story: DynamicStoryEntity)

    @Query("SELECT * FROM dynamic_stories WHERE id = :arcId")
    suspend fun getDynamicStoryById(arcId: String): DynamicStoryEntity?

    @Query("DELETE FROM dynamic_stories")
    suspend fun deleteAllDynamic()
}
