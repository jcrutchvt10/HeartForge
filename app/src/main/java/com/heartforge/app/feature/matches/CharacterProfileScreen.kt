package com.heartforge.app.feature.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.heartforge.app.core.model.Character
import com.heartforge.app.ui.theme.RoseRed
import com.heartforge.app.ui.theme.GlassWhite

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterProfileScreen(
    navController: NavController,
    viewModel: CharacterProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val character = state.character ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(character.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { 
                            navController.navigate(
                                com.heartforge.app.navigation.Destination.Gallery.createRoute(character.id, true)
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite, 
                            contentDescription = "Generate NSFW",
                            tint = RoseRed
                        )
                    }
                    IconButton(
                        onClick = { 
                            navController.navigate(
                                com.heartforge.app.navigation.Destination.Chronicles.createRoute(character.id)
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Book, 
                            contentDescription = "Chronicles",
                            tint = RoseRed
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Photo Gallery (Simplified for now)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                AsyncImage(
                    model = character.imageProfile.portraitId ?: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?q=80&w=1000&auto=format&fit=crop",
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Header Info
                Column {
                    Text(
                        text = "${character.name}, ${character.age}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = character.occupation,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = character.location,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Biography
                InfoSection(title = "Biography") {
                    Text(text = character.biography, style = MaterialTheme.typography.bodyLarge)
                }

                // Personality Sheet
                InfoSection(title = "Personality") {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        character.personality.traits.forEach { trait ->
                            SuggestionChip(
                                onClick = {},
                                label = { Text(trait) },
                                shape = CircleShape
                            )
                        }
                    }
                }

                // Relationship Style
                InfoSection(title = "Relationship Style") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        DetailItem(label = "Love Language", value = character.relationshipStyle.loveLanguages.joinToString())
                        DetailItem(label = "Flirting Style", value = character.relationshipStyle.flirtingStyle)
                        DetailItem(label = "Goal", value = character.relationshipStyle.relationshipGoals.firstOrNull() ?: "")
                    }
                }

                // Likes & Dislikes
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Likes", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                        character.likes.forEach { Text("✓ $it", color = Color.Green.copy(alpha = 0.7f)) }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Dislikes", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                        character.dislikes.forEach { Text("✕ $it", color = Color.Red.copy(alpha = 0.7f)) }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
                
                Button(
                    onClick = { navController.navigate(com.heartforge.app.navigation.Destination.Stories.createRoute(character.id)) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RoseRed)
                ) {
                    Text("Enter Story Mode")
                }

                OutlinedButton(
                    onClick = { navController.navigate(com.heartforge.app.navigation.Destination.Chat.createRoute(character.id)) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Start Conversation")
                }
            }
        }
    }
}

@Composable
fun InfoSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        content()
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Row {
        Text(text = "$label: ", fontWeight = FontWeight.SemiBold)
        Text(text = value)
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
        horizontalArrangement = horizontalArrangement
    ) {
        content()
    }
}
