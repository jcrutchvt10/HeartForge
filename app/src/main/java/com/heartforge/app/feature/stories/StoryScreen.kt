package com.heartforge.app.feature.stories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.heartforge.app.core.model.StoryArc
import com.heartforge.app.core.model.StoryChapter
import com.heartforge.app.ui.components.GlassSurface
import com.heartforge.app.ui.theme.RoseRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryScreen(
    characterId: String,
    navController: NavController,
    viewModel: StoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(characterId) {
        viewModel.loadStories(characterId)
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            GlassSurface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(0.dp, 0.dp, 24.dp, 24.dp)
            ) {
                TopAppBar(
                    title = { Text("Story Mode", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        }
    ) { padding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                item {
                    SecretArcGenerator(
                        isGenerating = state.isGeneratingSecret,
                        onClick = { viewModel.generateSecretArc() }
                    )
                }
                
                items(state.availableArcs) { arc ->
                    StoryArcItem(
                        arc = arc,
                        relationshipStats = state.relationship,
                        onChapterClick = { chapter ->
                            navController.navigate(
                                com.heartforge.app.navigation.Destination.StoryPlay.createRoute(
                                    arc.characterId, arc.id, chapter.id
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SecretArcGenerator(
    isGenerating: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = RoseRed.copy(alpha = 0.1f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, RoseRed.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = RoseRed, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text("Unlock a Midnight Secret", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                "Let AI craft a unique 3-chapter story based on your shared memories.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Button(
                onClick = onClick,
                enabled = !isGenerating,
                colors = ButtonDefaults.buttonColors(containerColor = RoseRed),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Generate Custom Story")
                }
            }
        }
    }
}

@Composable
fun StoryArcItem(
    arc: StoryArc,
    relationshipStats: com.heartforge.app.core.model.Relationship?,
    onChapterClick: (StoryChapter) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = arc.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
        Text(text = arc.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        arc.chapters.forEach { chapter ->
            val isUnlocked = (relationshipStats?.trust ?: 0) >= chapter.requiredTrust && 
                            (relationshipStats?.romance ?: 0) >= chapter.requiredRomance
            
            ChapterCard(
                chapter = chapter,
                isUnlocked = isUnlocked,
                onClick = { if (isUnlocked) onChapterClick(chapter) }
            )
        }
    }
}

@Composable
fun ChapterCard(
    chapter: StoryChapter,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isUnlocked) { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) 
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(if (isUnlocked) RoseRed else Color.Gray.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                } else {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White.copy(alpha = 0.5f))
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Chapter ${chapter.order}: ${chapter.title}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
                if (!isUnlocked) {
                    Text(
                        text = "Requires: ${chapter.requiredTrust} Trust, ${chapter.requiredRomance} Romance",
                        style = MaterialTheme.typography.labelSmall,
                        color = RoseRed.copy(alpha = 0.7f)
                    )
                } else {
                    Text(
                        text = chapter.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
