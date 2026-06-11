package com.heartforge.app.core.database

import androidx.room.TypeConverter
import com.heartforge.app.core.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.Instant

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? = value?.let { Instant.ofEpochMilli(it) }

    @TypeConverter
    fun dateToTimestamp(date: Instant?): Long? = date?.toEpochMilli()

    @TypeConverter
    fun fromAppearance(value: Appearance): String = gson.toJson(value)

    @TypeConverter
    fun toAppearance(value: String): Appearance = gson.fromJson(value, Appearance::class.java)

    @TypeConverter
    fun fromPersonality(value: Personality): String = gson.toJson(value)

    @TypeConverter
    fun toPersonality(value: String): Personality = gson.fromJson(value, Personality::class.java)

    @TypeConverter
    fun fromRelationshipStyle(value: RelationshipStyle): String = gson.toJson(value)

    @TypeConverter
    fun toRelationshipStyle(value: String): RelationshipStyle = gson.fromJson(value, RelationshipStyle::class.java)

    @TypeConverter
    fun fromImageProfile(value: ImageProfile): String = gson.toJson(value)

    @TypeConverter
    fun toImageProfile(value: String): ImageProfile = gson.fromJson(value, ImageProfile::class.java)

    @TypeConverter
    fun fromPromptProfile(value: PromptProfile): String = gson.toJson(value)

    @TypeConverter
    fun toPromptProfile(value: String): PromptProfile = gson.fromJson(value, PromptProfile::class.java)

    @TypeConverter
    fun fromInterestList(value: List<Interest>): String = gson.toJson(value)

    @TypeConverter
    fun toInterestList(value: String): List<Interest> {
        val listType = object : TypeToken<List<Interest>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromMessageRole(value: MessageRole): String = value.name

    @TypeConverter
    fun toMessageRole(value: String): MessageRole = MessageRole.valueOf(value)

    @TypeConverter
    fun fromMessageStatus(value: MessageStatus): String = value.name

    @TypeConverter
    fun toMessageStatus(value: String): MessageStatus = MessageStatus.valueOf(value)

    @TypeConverter
    fun fromMemoryCategory(value: MemoryCategory): String = value.name

    @TypeConverter
    fun toMemoryCategory(value: String): MemoryCategory = MemoryCategory.valueOf(value)

    @TypeConverter
    fun fromSentiment(value: Sentiment): String = value.name

    @TypeConverter
    fun toSentiment(value: String): Sentiment = Sentiment.valueOf(value)
}
