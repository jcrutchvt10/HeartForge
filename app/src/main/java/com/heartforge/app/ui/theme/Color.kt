package com.heartforge.app.ui.theme

import androidx.compose.ui.graphics.Color

// Primary palette - bold, seductive, premium
val RoseRed = Color(0xFFFF2E63)        // Electric Rose
val LuxeGold = Color(0xFFD4AF37)       // Premium Gold
val DeepPurple = Color(0xFF673AB7)     // Deep Purple accent
val NeonCyan = Color(0xFF00E5FF)       // Cyberpunk accent

// Backgrounds - deep, immersive, but with good contrast
val Charcoal = Color(0xFF121212)       // True dark background
val SurfaceDark = Color(0xFF1E1E22)    // Card/surface background
val SurfaceVariantDark = Color(0xFF2C2C32) // Elevated surfaces

// Text - maximized readability
val TextPrimary = Color(0xFFF0F0F3)    // Near-white for body text
val TextSecondary = Color(0xFFB0B0B8)  // Readable gray for captions
val TextOnDark = Color(0xFFFFFFFF)     // Pure white for high-impact text
val White = Color(0xFFF5F5F7)          // Kept for backward compat

// Glass & effects
val GlassWhite = Color.White.copy(alpha = 0.12f)
val GlassCharcoal = Color(0xFF1A1A1E).copy(alpha = 0.92f)
val GlassBorder = Color.White.copy(alpha = 0.08f)
val NeonRoseBorder = RoseRed.copy(alpha = 0.35f)
val NeonGoldBorder = LuxeGold.copy(alpha = 0.35f)
val NeonCyanBorder = NeonCyan.copy(alpha = 0.25f)
