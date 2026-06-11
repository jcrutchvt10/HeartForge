package com.heartforge.app.core.repository

import com.heartforge.app.core.model.Character

interface CharacterRepository {
    suspend fun getCharacters(): List<Character>
    suspend fun getCharacter(id: String): Character?
    suspend fun saveCharacter(character: Character)
}
