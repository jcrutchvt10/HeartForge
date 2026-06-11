package com.heartforge.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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
        BottomNavItem(Destination.ChatList, "Chats", Icons.Default.Chat),
        BottomNavItem(Destination.Gallery, "Gallery", Icons.Default.Image),
        BottomNavItem(Destination.Creator, "Create", Icons.Default.Add),
        BottomNavItem(Destination.ProfileSettings, "Settings", Icons.Default.Settings)
    )

    GlassSurface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .navigationBarsPadding(),
        shape = androidx.compose.foundation.shape.CircleShape
    ) {
        NavigationBar(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
            tonalElevation = 0.dp,
            modifier = Modifier.height(64.dp)
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            items.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = null, modifier = Modifier.size(24.dp)) },
                    label = { Text(item.label, style = MaterialTheme.typography.labelSmall) },
                    selected = currentDestination?.hierarchy?.any { it.route == item.destination.route } == true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = androidx.compose.ui.graphics.Color.Transparent
                    ),
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
}
