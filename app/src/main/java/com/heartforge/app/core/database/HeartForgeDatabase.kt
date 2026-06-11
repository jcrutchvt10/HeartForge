package com.heartforge.app.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        CharacterEntity::class,
        RelationshipEntity::class,
        MemoryEntity::class,
        MessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HeartForgeDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun relationshipDao(): RelationshipDao
    abstract fun memoryDao(): MemoryDao
    abstract fun messageDao(): MessageDao
}
