package com.heartforge.app.core.repository

import com.heartforge.app.core.model.Memory

interface MemoryRepository {
    suspend fun remember(
        memory: Memory
    )
}
