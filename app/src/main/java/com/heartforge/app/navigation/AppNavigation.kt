package com.heartforge.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.heartforge.app.feature.chat.ChatScreen
import com.heartforge.app.feature.creator.CreatorScreen
import com.heartforge.app.feature.gallery.GalleryScreen
import com.heartforge.app.feature.home.HomeScreen
import com.heartforge.app.feature.matches.MatchScreen
import com.heartforge.app.feature.settings.SettingsScreen

import com.heartforge.app.feature.matches.CharacterProfileScreen
import com.heartforge.app.feature.matches.MatchScreen
import com.heartforge.app.feature.settings.SettingsScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Destination.Home.route
    ) {
        composable(Destination.Home.route) {
            HomeScreen(navController)
        }

        composable(Destination.Matches.route) {
            MatchScreen(navController)
        }

        composable(Destination.Chat.route) {
            ChatScreen(navController)
        }

        composable(Destination.Gallery.route) {
            GalleryScreen(navController)
        }

        composable(Destination.Creator.route) {
            CreatorScreen(navController)
        }

        composable(Destination.Settings.route) {
            SettingsScreen(navController)
        }

        composable(
            route = Destination.CharacterProfile.route,
            arguments = listOf(navArgument("characterId") { type = NavType.StringType })
        ) {
            CharacterProfileScreen(navController)
        }
    }
}
