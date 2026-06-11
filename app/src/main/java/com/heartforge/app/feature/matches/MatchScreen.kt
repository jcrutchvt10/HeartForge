package com.heartforge.app.feature.matches

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import kotlinx.coroutines.launch

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
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(0.9f).clip(RoundedCornerShape(32.dp)).shimmerEffect())
            } else if (state.currentProfiles.isEmpty()) {
                Text("No more matches today!", style = MaterialTheme.typography.titleMedium)
            } else {
                val profile = state.currentProfiles.first()
                MatchCard(
                    profile = profile,
                    onLike = { viewModel.onLike(profile) },
                    onPass = { viewModel.onPass(profile) },
                    onFavorite = { /* TODO */ }
                )
            }
        }
    }
}

@Composable
fun MatchCard(
    profile: MatchProfile,
    onLike: () -> Unit,
    onPass: () -> Unit,
    onFavorite: () -> Unit
) {
    val character = profile.character
    val scope = rememberCoroutineScope()
    
    var likeScale by remember { mutableStateOf(1f) }
    var passScale by remember { mutableStateOf(1f) }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = character.imageProfile.portraitId ?: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?q=80&w=1000&auto=format&fit=crop",
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f)),
                            startY = 600f
                        )
                    )
            )

            Surface(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.TopEnd),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
            ) {
                Text(
                    text = "${profile.compatibilityScore}% Match",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

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

                FlowRow(
                    modifier = Modifier.padding(top = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val tags = character.personality.traits.take(2) + character.hobbies.take(2)
                    tags.forEach { tag ->
                        SuggestionChip(
                            onClick = { },
                            label = { Text("✓ $tag", color = Color.White) },
                            shape = CircleShape,
                            colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color.White.copy(alpha = 0.15f)),
                            border = SuggestionChipDefaults.suggestionChipBorder(borderColor = Color.White.copy(alpha = 0.1f))
                        )
                    }
                }
            }
        }
    }

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
                onClick = onPass,
                containerColor = Color(0xFF1E1E1E),
                contentColor = Color.Red,
                shape = CircleShape,
                modifier = Modifier.size(56.dp).scale(passScale)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Pass", modifier = Modifier.size(28.dp))
            }

            LargeFloatingActionButton(
                onClick = onLike,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.scale(likeScale)
            ) {
                Icon(Icons.Default.Favorite, contentDescription = "Like", modifier = Modifier.size(36.dp))
            }

            FloatingActionButton(
                onClick = onFavorite,
                containerColor = Color(0xFF1E1E1E),
                contentColor = Color(0xFFFFD600),
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Star, contentDescription = "Favorite", modifier = Modifier.size(28.dp))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        content = content
    )
}
