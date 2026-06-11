package com.heartforge.app.core.repository

import com.heartforge.app.core.database.MemoryDao
import com.heartforge.app.core.database.toEntity
import com.heartforge.app.core.model.Memory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoryRepositoryImpl @Inject constructor(
    private val memoryDao: MemoryDao
) : MemoryRepository {

    override suspend fun remember(memory: Memory) {
        memoryDao.insertMemory(memory.toEntity())
    }
}
