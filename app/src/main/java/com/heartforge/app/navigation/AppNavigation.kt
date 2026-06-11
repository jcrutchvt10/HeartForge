package com.heartforge.app.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.heartforge.app.feature.chat.ChatScreen
import com.heartforge.app.feature.chat.ChatListScreen
import com.heartforge.app.feature.creator.CreatorScreen
import com.heartforge.app.feature.gallery.GalleryScreen
import com.heartforge.app.feature.home.HomeScreen
import com.heartforge.app.feature.matches.MatchScreen
import com.heartforge.app.feature.profile.ProfileSettingsScreen
import com.heartforge.app.feature.settings.SettingsScreen
import com.heartforge.app.feature.matches.CharacterProfileScreen
import com.heartforge.app.feature.memories.MemoryScreen
import com.heartforge.app.feature.stories.StoryScreen
import com.heartforge.app.feature.stories.StoryPlayScreen

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Destination.Home.route,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400))
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400))
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400))
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400))
        }
    ) {
        composable(Destination.Home.route) {
            HomeScreen(navController)
        }

        composable(Destination.Matches.route) {
            MatchScreen(navController)
        }

        composable(
            route = Destination.Chat.route,
            arguments = listOf(navArgument("characterId") { type = NavType.StringType })
        ) {
            ChatScreen(navController)
        }

        composable(Destination.ChatList.route) {
            ChatListScreen(navController)
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

        composable(Destination.ProfileSettings.route) {
            ProfileSettingsScreen(navController)
        }

        composable(
            route = Destination.Memories.route,
            arguments = listOf(navArgument("characterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("characterId") ?: ""
            MemoryScreen(navController = navController)
        }

        composable(
            route = Destination.Stories.route,
            arguments = listOf(navArgument("characterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("characterId") ?: ""
            StoryScreen(characterId = id, navController = navController)
        }

        composable(
            route = Destination.StoryPlay.route,
            arguments = listOf(
                navArgument("characterId") { type = NavType.StringType },
                navArgument("arcId") { type = NavType.StringType },
                navArgument("chapterId") { type = NavType.StringType }
            )
        ) {
            StoryPlayScreen(navController)
        }

        composable(
            route = Destination.CharacterProfile.route,
            arguments = listOf(navArgument("characterId") { type = NavType.StringType })
        ) {
            CharacterProfileScreen(navController)
        }
    }
}
