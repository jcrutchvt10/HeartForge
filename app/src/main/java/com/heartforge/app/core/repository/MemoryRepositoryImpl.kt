package com.heartforge.app.core.repository

import com.heartforge.app.core.database.MemoryDao
import com.heartforge.app.core.database.toEntity
import com.heartforge.app.core.model.Memory
import com.heartforge.app.core.model.MemoryCategory
import com.heartforge.app.core.model.Sentiment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoryRepositoryImpl @Inject constructor(
    private val memoryDao: MemoryDao
) : MemoryRepository {

    override suspend fun remember(memory: Memory) {
        memoryDao.insertMemory(memory.toEntity())
    }

    override suspend fun forget(memoryId: String) {
        memoryDao.deleteMemory(memoryId)
    }

    override suspend fun editMemory(
        memoryId: String,
        content: String,
        importance: Int,
        category: MemoryCategory,
        sentiment: Sentiment
    ) {
        memoryDao.updateMemory(memoryId, content, importance, category, sentiment)
    }
}
