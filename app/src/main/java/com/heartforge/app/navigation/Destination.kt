package com.heartforge.app.navigation

sealed class Destination(val route: String) {

    data object Home : Destination("home")

    data object Matches : Destination("matches")

    data object Chat : Destination("chat")

    data object Gallery : Destination("gallery")

    data object Creator : Destination("creator")

    data object Settings : Destination("settings")

    data object CharacterProfile : Destination("profile/{characterId}") {
        fun createRoute(id: String) = "profile/$id"
    }

}
