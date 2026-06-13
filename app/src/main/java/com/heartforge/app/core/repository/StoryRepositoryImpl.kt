package com.heartforge.app.core.repository

import com.heartforge.app.core.ai.StoryEngine
import com.heartforge.app.core.database.StoryDao
import com.heartforge.app.core.database.toEntity
import com.heartforge.app.core.database.toExternal
import com.heartforge.app.core.model.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepositoryImpl @Inject constructor(
    private val storyDao: StoryDao,
    private val storyEngine: StoryEngine,
    private val memoryDao: com.heartforge.app.core.database.MemoryDao,
    private val relationshipRepository: RelationshipRepository,
    private val userProfileRepository: UserProfileRepository
) : StoryRepository {

    private val staticArcs = listOf(
        StoryArc("arc_1", "1", "Coffee & Canvas",
            "An afternoon in Kai's studio turns into something far more intimate than either of you expected.",
            listOf(
                StoryChapter("1_c1", "Stripped Bare", "Kai asks you to model for him. Nude.", 1,
                    startingPrompt = "The studio is dim, lit only by warm golden lamps. Kai's easel is ready, charcoal in hand. He looks you up and slow — deliberate, hungry. 'I want to paint you. All of you. Take everything off and lie on the velvet. Don't be shy... I want to see exactly what you've been hiding.'"),
                StoryChapter("1_c2", "Midnight Unveiling", "The gallery opening reveals far more than art.", 2, 15, 10,
                    startingPrompt = "The gallery is packed, but Kai pulls you into a quiet corner. His voice is low, rough. 'The painting in the back — it's you. Every curve, every shadow. I wanted everyone to see how beautiful you are when you let go.' He presses against you, hard. 'But they only get to look. I'm the only one who gets to touch.'")
            )),
        StoryArc("arc_2", "2", "Summit Pursuit",
            "A mountain trek with Ethan leads to a secluded spot where nature isn't the only thing that's breathtaking.",
            listOf(
                StoryChapter("2_c1", "The Trailhead", "A pre-dawn hike leads to a hidden overlook.", 1,
                    startingPrompt = "The air is cold and sharp. Ethan's already stripped off his shirt, muscles glistening. He grabs your hand, pulling you off the trail. 'There's a spot I know. No one ever goes there.' When you reach it — a hidden overlook — he pushes you gently against the rock face and drops to his knees. 'Been thinking about your cock the whole hike up.'"),
                StoryChapter("2_c2", "Summit Camp", "Night falls and the tent gets very warm.", 2, 20, 15,
                    startingPrompt = "The fire crackles low. Ethan's zipped the tent shut, stripped down to nothing, his cock already half-hard in the dim light. He reaches for you. 'Come here. I want to feel you from the inside while the whole mountain listens.' His voice is a growl. 'I want you to fuck me.'")
            ))
    )

    override fun getStoriesForCharacter(characterId: String): Flow<List<StoryArc>> {
        return storyDao.getDynamicStories(characterId).map { dynamic ->
            val list = staticArcs.filter { it.characterId == characterId }.toMutableList()
            list.addAll(dynamic.map { it.toExternal() })
            list
        }
    }

    override fun getProgressForCharacter(characterId: String): Flow<List<StoryProgress>> {
        return storyDao.getProgressForCharacter(characterId).map { list -> list.map { it.toExternal() } }
    }

    override suspend fun updateProgress(progress: StoryProgress) {
        storyDao.insertProgress(progress.toEntity())
    }

    override suspend fun getStoryArc(arcId: String): StoryArc? {
        val static = staticArcs.find { it.id == arcId }
        if (static != null) return static
        
        return storyDao.getDynamicStoryById(arcId)?.toExternal()
    }

    /**
     * Triggers the AI to generate a new secret arc for this character and saves it.
     */
    suspend fun generateSecretArc(character: Character) {
        val userProfile = userProfileRepository.getProfile()
        val relationship = relationshipRepository.getRelationship(character.id).first() ?: return
        val memories = memoryDao.getRelevantMemories(character.id, 1).map { it.toExternal() }

        val newArc = storyEngine.generatePersonalizedArc(character, userProfile, relationship, memories)
        
        // Save to DB for persistence
        storyDao.insertDynamicStory(newArc.toEntity())
    }
}
