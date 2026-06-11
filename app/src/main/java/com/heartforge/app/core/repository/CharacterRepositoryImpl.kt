package com.heartforge.app.core.repository

import com.heartforge.app.core.database.CharacterDao
import com.heartforge.app.core.database.toEntity
import com.heartforge.app.core.database.toExternal
import com.heartforge.app.core.model.Character
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepositoryImpl @Inject constructor(
    private val characterDao: CharacterDao
) : CharacterRepository {

    override suspend fun getCharacters(): List<Character> {
        return characterDao.getCharacters().first().map { it.toExternal() }
    }

    override suspend fun getCharacter(id: String): Character? {
        return characterDao.getCharacter(id)?.toExternal()
    }
    
    suspend fun saveCharacter(character: Character) {
        characterDao.insertCharacter(character.toEntity())
    }
}
