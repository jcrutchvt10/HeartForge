package com.heartforge.app.core.repository

import com.heartforge.app.core.model.StoryArc
import com.heartforge.app.core.model.StoryProgress
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
    fun getStoriesForCharacter(characterId: String): Flow<List<StoryArc>>
    fun getProgressForCharacter(characterId: String): Flow<List<StoryProgress>>
    suspend fun updateProgress(progress: StoryProgress)
    suspend fun getStoryArc(arcId: String): StoryArc?
}
