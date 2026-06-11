package com.heartforge.app.core.repository

import com.heartforge.app.core.database.StoryDao
import com.heartforge.app.core.database.toEntity
import com.heartforge.app.core.database.toExternal
import com.heartforge.app.core.model.StoryArc
import com.heartforge.app.core.model.StoryChapter
import com.heartforge.app.core.model.StoryProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepositoryImpl @Inject constructor(
    private val storyDao: StoryDao
) : StoryRepository {

    private val staticStories = listOf(
        StoryArc(
            id = "kai_arc_1",
            characterId = "1",
            title = "Coffee & Canvas",
            description = "Explore Kai's artistic world as he prepares for his first gallery showing.",
            chapters = listOf(
                StoryChapter(
                    id = "c1",
                    title = "The Blank Canvas",
                    description = "Meet Kai at his studio and help him find inspiration for a new masterpiece.",
                    order = 1,
                    startingPrompt = "You walk into Kai's sun-drenched studio. He's staring at a massive blank canvas, paint-stained fingers running through his hair. 'I'm stuck,' he mutters without looking up. 'What should I paint today?'"
                ),
                StoryChapter(
                    id = "c2",
                    title = "A Taste of Home",
                    description = "Visit Kai's coffee shop during a busy morning rush.",
                    order = 2,
                    requiredTrust = 15,
                    startingPrompt = "The smell of freshly ground beans and old books fills the air. Kai is behind the counter, flawlessly pulling shots. He spots you and his face lights up. 'Just in time. I've been experimenting with a new roast. Want a taste?'"
                )
            )
        ),
        StoryArc(
            id = "ethan_arc_1",
            characterId = "2",
            title = "Summit Pursuit",
            description = "Join Ethan on a challenging mountain trek that tests more than just your fitness.",
            chapters = listOf(
                StoryChapter(
                    id = "e1",
                    title = "The Trailhead",
                    description = "Early morning at the base of the trail.",
                    order = 1,
                    startingPrompt = "The air is crisp and the sun hasn't quite broken over the horizon yet. Ethan is stretching, looking energized. 'You ready for this? It's a steep climb, but the view at the top is worth everything.'"
                )
            )
        )
    )

    override fun getStoriesForCharacter(characterId: String): Flow<List<StoryArc>> {
        return flowOf(staticStories.filter { it.characterId == characterId })
    }

    override fun getProgressForCharacter(characterId: String): Flow<List<StoryProgress>> {
        return storyDao.getProgressForCharacter(characterId).map { list -> list.map { it.toExternal() } }
    }

    override suspend fun updateProgress(progress: StoryProgress) {
        storyDao.insertProgress(progress.toEntity())
    }

    override suspend fun getStoryArc(arcId: String): StoryArc? {
        return staticStories.find { it.id == arcId }
    }
}
