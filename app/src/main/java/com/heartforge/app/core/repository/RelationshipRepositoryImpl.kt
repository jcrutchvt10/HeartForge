package com.heartforge.app.core.repository

import com.heartforge.app.core.database.RelationshipDao
import com.heartforge.app.core.database.toEntity
import com.heartforge.app.core.database.toExternal
import com.heartforge.app.core.model.Relationship
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RelationshipRepositoryImpl @Inject constructor(
    private val relationshipDao: RelationshipDao
) : RelationshipRepository {

    override fun getRelationship(characterId: String): Flow<Relationship?> {
        return relationshipDao.getRelationship(characterId).map { it?.toExternal() }
    }

    override suspend fun updateRelationship(relationship: Relationship) {
        relationshipDao.insertRelationship(relationship.toEntity())
    }
}
