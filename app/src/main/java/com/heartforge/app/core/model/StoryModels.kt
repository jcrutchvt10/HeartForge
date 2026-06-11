package com.heartforge.app.core.model

data class StoryArc(
    val id: String,
    val characterId: String,
    val title: String,
    val description: String,
    val chapters: List<StoryChapter>,
    val imageUrl: String? = null
)

data class StoryChapter(
    val id: String,
    val title: String,
    val description: String,
    val order: Int,
    val requiredTrust: Int = 0,
    val requiredRomance: Int = 0,
    val startingPrompt: String,
    val completionMemory: String? = null
)

data class StoryProgress(
    val characterId: String,
    val arcId: String,
    val completedChapterIds: List<String>,
    val activeChapterId: String?
)

data class StoryChoice(
    val id: String,
    val text: String,
    val narration: String,
    val statChanges: StoryStatChanges,
    val chapterContext: String
)

data class StoryStatChanges(
    val trust: Int = 0,
    val romance: Int = 0,
    val intimacy: Int = 0,
    val affection: Int = 0
)
