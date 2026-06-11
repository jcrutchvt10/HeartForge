package com.heartforge.app.feature.matches

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.heartforge.app.core.model.Character

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchScreen(
    navController: NavController,
    viewModel: MatchViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Matchmaking", fontWeight = FontWeight.Bold) }
            )
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
                CircularProgressIndicator()
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

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image (Placeholder)
            AsyncImage(
                model = character.imageProfile.portraitId ?: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?q=80&w=1000&auto=format&fit=crop",
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 400f
                        )
                    )
            )

            // Compatibility Badge
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
                    color = Color.White
                )
            }

            // Info Content
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = character.age.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Text(
                    text = character.occupation,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Tags
                FlowRow(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val tags = character.personality.traits.take(2) + character.hobbies.take(2)
                    tags.forEach { tag ->
                        SuggestionChip(
                            onClick = { },
                            label = { Text("✓ $tag", color = Color.White) },
                            shape = CircleShape,
                            colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color.White.copy(alpha = 0.2f))
                        )
                    }
                }

                Text(
                    text = "Looking for: ${character.relationshipStyle.relationshipGoals.firstOrNull() ?: "Long-term"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }

    // Action Buttons
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingActionButton(
                onClick = onPass,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.Red,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Close, contentDescription = "Pass")
            }

            LargeFloatingActionButton(
                onClick = onLike,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Favorite, contentDescription = "Like")
            }

            FloatingActionButton(
                onClick = onFavorite,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.Yellow,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Star, contentDescription = "Favorite")
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
