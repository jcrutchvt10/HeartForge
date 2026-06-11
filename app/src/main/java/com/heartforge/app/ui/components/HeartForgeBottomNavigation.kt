package com.heartforge.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.heartforge.app.navigation.Destination

private data class BottomNavItem(
    val destination: Destination,
    val label: String,
    val icon: ImageVector
)

@Composable
fun HeartForgeBottomNavigation(navController: NavHostController) {
    val items = listOf(
        BottomNavItem(Destination.Home, "Home", Icons.Default.Home),
        BottomNavItem(Destination.Matches, "Matches", Icons.Default.Favorite),
        BottomNavItem(Destination.Chat, "Chats", Icons.Default.Chat),
        BottomNavItem(Destination.Gallery, "Gallery", Icons.Default.Image),
        BottomNavItem(Destination.Creator, "Create", Icons.Default.Add),
        BottomNavItem(Destination.Settings, "Settings", Icons.Default.Settings)
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.label) },
                selected = currentDestination?.hierarchy?.any { it.route == item.destination.route } == true,
                onClick = {
                    navController.navigate(item.destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
