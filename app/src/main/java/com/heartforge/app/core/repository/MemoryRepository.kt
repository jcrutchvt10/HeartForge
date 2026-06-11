package com.heartforge.app.core.repository

import com.heartforge.app.core.model.Memory

interface MemoryRepository {
    suspend fun remember(memory: Memory)
    suspend fun forget(memoryId: String)
    suspend fun editMemory(memoryId: String, content: String, importance: Int, category: com.heartforge.app.core.model.MemoryCategory, sentiment: com.heartforge.app.core.model.Sentiment)
}
