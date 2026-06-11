package com.heartforge.app.core.repository

import com.heartforge.app.core.model.Relationship
import kotlinx.coroutines.flow.Flow

interface RelationshipRepository {
    fun getRelationship(characterId: String): Flow<Relationship?>
    suspend fun updateRelationship(relationship: Relationship)
}
