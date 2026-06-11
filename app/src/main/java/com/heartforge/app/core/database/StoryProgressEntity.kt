package com.heartforge.app.core.database

import androidx.room.Entity
import com.heartforge.app.core.model.StoryProgress

@Entity(tableName = "story_progress", primaryKeys = ["characterId", "arcId"])
data class StoryProgressEntity(
    val characterId: String,
    val arcId: String,
    val completedChapterIds: List<String>,
    val activeChapterId: String?
)

fun StoryProgressEntity.toExternal() = StoryProgress(
    characterId = characterId,
    arcId = arcId,
    completedChapterIds = completedChapterIds,
    activeChapterId = activeChapterId
)

fun StoryProgress.toEntity() = StoryProgressEntity(
    characterId = characterId,
    arcId = arcId,
    completedChapterIds = completedChapterIds,
    activeChapterId = activeChapterId
)
