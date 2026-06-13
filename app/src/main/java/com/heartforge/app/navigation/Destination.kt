package com.heartforge.app.navigation

sealed class Destination(val route: String) {

    data object Home : Destination("home")

    data object Matches : Destination("matches")

    data object Chat : Destination("chat/{characterId}") {
        fun createRoute(id: String) = "chat/$id"
    }

    data object ChatList : Destination("chats")

    data object Gallery : Destination("gallery?characterId={characterId}&autoGenerate={autoGenerate}") {
        fun createRoute(characterId: String? = null, autoGenerate: Boolean = false) = 
            "gallery" + (if (characterId != null) "?characterId=$characterId&autoGenerate=$autoGenerate" else "")
    }

    data object Creator : Destination("creator")

    data object Settings : Destination("settings")

    data object ProfileSettings : Destination("profile_settings")

    data object Memories : Destination("memories/{characterId}") {
        fun createRoute(id: String) = "memories/$id"
    }

    data object Stories : Destination("stories/{characterId}") {
        fun createRoute(id: String) = "stories/$id"
    }

    data object StoryPlay : Destination("story_play/{characterId}/{arcId}/{chapterId}") {
        fun createRoute(characterId: String, arcId: String, chapterId: String) = "story_play/$characterId/$arcId/$chapterId"
    }

    data object CharacterProfile : Destination("profile/{characterId}") {
        fun createRoute(id: String) = "profile/$id"
    }

    data object Chronicles : Destination("chronicles/{characterId}") {
        fun createRoute(id: String) = "chronicles/$id"
    }
}
