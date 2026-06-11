package com.heartforge.app.core.repository

import com.heartforge.app.core.database.RelationshipDao
import com.heartforge.app.core.database.toEntity
import com.heartforge.app.core.database.toExternal
import com.heartforge.app.core.model.Relationship
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RelationshipRepositoryImpl @Inject constructor(
    private val relationshipDao: RelationshipDao
) : RelationshipRepository {

    override suspend fun getRelationship(characterId: String): Relationship {
        return relationshipDao.getRelationship(characterId).first()?.toExternal() 
            ?: Relationship(characterId, 0, 0, 0, 0, 0, 0, 0)
    }
}
