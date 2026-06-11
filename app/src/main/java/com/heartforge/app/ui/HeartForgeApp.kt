package com.heartforge.app.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.heartforge.app.navigation.AppNavigation
import com.heartforge.app.ui.theme.HeartForgeTheme

@Composable
fun HeartForgeApp() {

    HeartForgeTheme {

        Surface {

            val navController = rememberNavController()

            AppNavigation(navController)

        }

    }

}
