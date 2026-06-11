package com.heartforge.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = RoseRed,
    secondary = LuxeGold,
    tertiary = Silver,
    background = Charcoal,
    surface = Charcoal,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Charcoal,
    onBackground = White,
    onSurface = White,
    surfaceVariant = Color(0xFF2C2C2E), // Lighter variant for cards
    onSurfaceVariant = Silver
)

@Composable
fun HeartForgeTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes
    ) {
        // Subtle background gradient for depth, but maintaining deep black base
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF0F0F12),
                            Charcoal
                        ),
                        center = androidx.compose.ui.geometry.Offset(500f, 500f),
                        radius = 3000f
                    )
                )
        ) {
            content()
        }
    }
}
