package com.heartforge.app.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RelationshipDao {
    @Query("SELECT * FROM relationships WHERE characterId = :characterId")
    fun getRelationship(characterId: String): Flow<RelationshipEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRelationship(relationship: RelationshipEntity)

    @Query("DELETE FROM relationships WHERE characterId = :characterId")
    suspend fun deleteRelationship(characterId: String)
}
