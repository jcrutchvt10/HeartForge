package com.heartforge.app.feature.matches

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.heartforge.app.core.model.Character
import com.heartforge.app.ui.components.GlassSurface
import com.heartforge.app.ui.components.shimmerEffect
import com.heartforge.app.ui.theme.LuxeGold
import com.heartforge.app.ui.theme.RoseRed
import com.heartforge.app.ui.theme.TextPrimary
import com.heartforge.app.ui.theme.TextSecondary
import kotlinx.coroutines.launch
import kotlin.math.abs
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchScreen(
    navController: NavController,
    viewModel: MatchViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            GlassSurface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(0.dp, 0.dp, 24.dp, 24.dp)
            ) {
                CenterAlignedTopAppBar(
                    title = { Text("Matchmaking", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(32.dp))
                            .shimmerEffect()
                    )
                }
                state.currentProfiles.isEmpty() -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "No more matches today!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = RoseRed
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Check back later for new profiles.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
                else -> {
                    // Background deck cards (always render underneath for peek effect)
                    val deck = state.currentProfiles.take(3)
                    if (deck.size >= 2) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(0.95f)
                                .offset(y = 8.dp)
                        ) {
                            Card(
                                modifier = Modifier.fillMaxSize(),
                                shape = RoundedCornerShape(32.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                )
                            ) {}
                        }
                    }
                    if (deck.size >= 3) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(0.90f)
                                .offset(y = 16.dp)
                        ) {
                            Card(
                                modifier = Modifier.fillMaxSize(),
                                shape = RoundedCornerShape(32.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                )
                            ) {}
                        }
                    }

                    // Animated top card — direction-aware (forward=1, backward=-1)
                    AnimatedContent(
                        targetState = deck.firstOrNull(),
                        modifier = Modifier.fillMaxSize(),
                        transitionSpec = {
                            val dir = state.swipeDirection
                            (slideInHorizontally(
                                animationSpec = tween(350, easing = FastOutSlowInEasing),
                                initialOffsetX = { dir * it }
                            ) togetherWith slideOutHorizontally(
                                animationSpec = tween(350, easing = FastOutSlowInEasing),
                                targetOffsetX = { -dir * it }
                            )).using(SizeTransform(clip = false))
                        },
                        label = "cardSwipe"
                    ) { topProfile ->
                        if (topProfile != null) {
                            SwipeableMatchCard(
                                profile = topProfile,
                                onLike = {
                                    viewModel.onPass(topProfile)
                                },
                                onPass = {
                                    viewModel.onLike(topProfile)
                                },
                                onFavorite = { viewModel.onLike(topProfile) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SwipeableMatchCard(
    profile: MatchProfile,
    onLike: () -> Unit,
    onPass: () -> Unit,
    onFavorite: () -> Unit
) {
    val character = profile.character
    val scope = rememberCoroutineScope()
    val swipeThreshold = 300f

    val offsetX = remember { Animatable(0f) }

    val rotation = { (offsetX.value / 50f).coerceIn(-25f, 25f) }
    val likeAlpha = { (offsetX.value / swipeThreshold).coerceIn(0f, 1f) }
    val passAlpha = { (-offsetX.value / swipeThreshold).coerceIn(0f, 1f) }

    suspend fun snapBack() {
        offsetX.animateTo(0f, animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f))
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
            .graphicsLayer {
                translationX = offsetX.value
                rotationZ = rotation()
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        scope.launch {
                            if (abs(offsetX.value) > swipeThreshold) {
                                val liked = offsetX.value > 0
                                offsetX.snapTo(0f)
                                if (liked) onLike() else onPass()
                            } else {
                                snapBack()
                            }
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        scope.launch {
                            val newValue = (offsetX.value + dragAmount)
                                .coerceIn(-1200f, 1200f)
                            offsetX.snapTo(newValue)
                        }
                    }
                )
            },
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = character.imageProfile.portraitId,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dark gradient overlay at bottom for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                            startY = 600f
                        )
                    )
            )

            // Like indicator (right swipe)
            if (likeAlpha() > 0.1f) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(24.dp)
                        .background(
                            Color.White.copy(alpha = 0.9f),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .graphicsLayer { alpha = likeAlpha() }
                ) {
                    Text("LIKE", color = RoseRed, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp)
                }
            }

            // Pass indicator (left swipe)
            if (passAlpha() > 0.1f) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(24.dp)
                        .background(
                            Color.White.copy(alpha = 0.9f),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .graphicsLayer { alpha = passAlpha() }
                ) {
                    Text("NOPE", color = Color.Red, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp)
                }
            }

            // Compatibility score badge
            Surface(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.TopEnd),
                shape = RoundedCornerShape(16.dp),
                color = RoseRed.copy(alpha = 0.9f)
            ) {
                Text(
                    text = "${profile.compatibilityScore}% Match",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            // Character info at bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = character.age.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                Text(
                    text = character.occupation,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Traits as chips
                if (character.personality.traits.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        character.personality.traits.take(3).forEach { trait ->
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = trait,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            // AI Audit snippet
            profile.aiAudit?.let { audit ->
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 100.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "✨ ${audit.summary}",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }

    // Bottom action buttons
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingActionButton(
                onClick = { onPass() },
                containerColor = Color(0xFF1E1E1E),
                contentColor = Color.Red,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Pass", modifier = Modifier.size(28.dp))
            }

            LargeFloatingActionButton(
                onClick = { onLike() },
                containerColor = RoseRed,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(Icons.Default.Favorite, contentDescription = "Like", modifier = Modifier.size(36.dp))
            }

            FloatingActionButton(
                onClick = onFavorite,
                containerColor = Color(0xFF1E1E1E),
                contentColor = LuxeGold,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Star, contentDescription = "Favorite", modifier = Modifier.size(28.dp))
            }
        }
    }
}
