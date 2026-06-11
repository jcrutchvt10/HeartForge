package com.heartforge.app.ui.theme

import androidx.compose.ui.graphics.Color

val RoseRed = Color(0xFFFF2E63) // Electric Rose
val LuxeGold = Color(0xFFD4AF37) // Premium Gold
val Charcoal = Color(0xFF1A1A1E) // Lightened Charcoal for better legibility
val White = Color(0xFFF5F5F7) // Off-white for softer contrast
val Silver = Color(0xFFB0B0B5) // Muted silver for variant text

// Secondary/Tertiary variants
val RoseRedDark = Color(0xFF880E4F)
val LuxeGoldDark = Color(0xFF996515)

// Surface colors for Glassmorphism - deep and immersive
val GlassWhite = White.copy(alpha = 0.2f)
val GlassCharcoal = Color(0xFF1A1A1E).copy(alpha = 0.95f)
val GlassBorder = White.copy(alpha = 0.15f)
val NeonRoseBorder = RoseRed.copy(alpha = 0.4f)
val NeonGoldBorder = LuxeGold.copy(alpha = 0.4f)
