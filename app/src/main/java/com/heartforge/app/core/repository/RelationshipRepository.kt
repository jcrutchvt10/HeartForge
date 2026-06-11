package com.heartforge.app.core.repository

import com.heartforge.app.core.model.Relationship

interface RelationshipRepository {
    suspend fun getRelationship(
        characterId: String
    ): Relationship
}
