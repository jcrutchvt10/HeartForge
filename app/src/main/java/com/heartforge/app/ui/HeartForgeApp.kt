package com.heartforge.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.heartforge.app.navigation.AppNavigation
import com.heartforge.app.navigation.Destination
import com.heartforge.app.ui.components.HeartForgeBottomNavigation
import com.heartforge.app.ui.theme.HeartForgeTheme

@Composable
fun HeartForgeApp(pendingCharacterId: String? = null) {
    HeartForgeTheme {
        val navController = rememberNavController()

        // Handle deep-link from notification tap
        if (pendingCharacterId != null) {
            LaunchedEffect(pendingCharacterId) {
                navController.navigate(Destination.Chat.createRoute(pendingCharacterId))
            }
        }

        Scaffold(
            bottomBar = { HeartForgeBottomNavigation(navController = navController) },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(bottom = innerPadding.calculateBottomPadding())) {
                AppNavigation(navController = navController)
            }
        }
    }
}
