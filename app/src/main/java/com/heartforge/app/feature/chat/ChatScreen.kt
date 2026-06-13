package com.heartforge.app.feature.chat

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.heartforge.app.core.model.ChatMessage
import com.heartforge.app.core.model.MessageRole
import com.heartforge.app.ui.components.GlassSurface
import com.heartforge.app.ui.theme.LuxeGold
import com.heartforge.app.ui.theme.RoseRed
import com.heartforge.app.ui.theme.White
import com.heartforge.app.ui.theme.NeonGoldBorder
import com.heartforge.app.ui.theme.NeonRoseBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberLazyListState()

    val moodColor = remember(state.relationship?.mood) {
        when (state.relationship?.mood?.lowercase()) {
            "lustful", "hot", "affectionate" -> Color(0xFF4A0E0E) // Deep Crimson
            "playful", "happy" -> Color(0xFF0E3A4A) // Dark Teal
            "jealous", "angry" -> Color(0xFF2E0E4A) // Deep Purple
            "comfortable", "loving" -> Color(0xFF4A3B0E) // Dark Gold/Amber
            else -> Color(0xFF121212) // Default Noir
        }
    }

    val animatedBgColor by animateColorAsState(targetValue = moodColor, animationSpec = tween(1000))

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            scrollState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Scaffold(
        containerColor = animatedBgColor,
        topBar = {
            GlassSurface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(0.dp, 0.dp, 24.dp, 24.dp)
            ) {
                Column {
                    TopAppBar(
                        title = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(state.character?.name ?: "Chat", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    MoodIndicator(state.relationship?.mood ?: "Neutral")
                                }
                                state.relationship?.let {
                                    RelationshipMeterMini(trust = it.trust, romance = it.romance)
                                }
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        },
                        actions = {
                            state.character?.let { character ->
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
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                    )
                    state.activeStoryChapter?.let { chapter ->
                        Surface(
                            color = RoseRed.copy(alpha = 0.1f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Current Story: ${chapter.title}",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = RoseRed
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            GlassSurface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp)
            ) {
                ChatInput(
                    value = state.currentInput,
                    onValueChange = { viewModel.onInputChange(it) },
                    onSend = { viewModel.sendMessage() },
                    onImagePicked = { viewModel.sendImage(it) }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.messages, key = { it.id }) { message ->
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
                ) {
                    ChatBubble(
                        message = message,
                        onRetry = if (message.role == MessageRole.System) {
                            { viewModel.retryLastMessage() }
                        } else null
                    )
                }
            }
            if (state.isAssistantTyping) {
                item {
                    TypingIndicator(state.character?.name ?: "Partner")
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage, onRetry: (() -> Unit)? = null) {
    val isUser = message.role == MessageRole.User
    val isError = message.role == MessageRole.System
    val alignment = if (isUser) Alignment.End else Alignment.Start
    val borderColor = when {
        isError -> Color.Red.copy(alpha = 0.6f)
        isUser -> NeonRoseBorder
        else -> NeonGoldBorder
    }
    val shape = if (isUser) {
        RoundedCornerShape(24.dp, 24.dp, 4.dp, 24.dp)
    } else {
        RoundedCornerShape(24.dp, 24.dp, 24.dp, 4.dp)
    }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
        GlassSurface(
            modifier = Modifier.widthIn(max = 300.dp),
            shape = shape,
            borderColor = borderColor,
            borderWidth = 1.5.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                if (message.imageUrl != null) {
                    AsyncImage(
                        model = message.imageUrl,
                        contentDescription = "Shared Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .padding(bottom = 8.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                if (message.content.isNotBlank()) {
                    Text(
                        text = message.content,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isError) Color.Red.copy(alpha = 0.8f) else White,
                        lineHeight = 24.sp
                    )
                }
                if (isError && onRetry != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = onRetry) {
                        Text("Tap to retry", color = RoseRed, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun MoodIndicator(mood: String) {
    val emoji = when (mood.lowercase()) {
        "happy" -> "😊"
        "affectionate", "loving" -> "🔥"
        "jealous" -> "😒"
        "playful" -> "😜"
        "distant" -> "🌫️"
        else -> "😐"
    }
    Text(emoji, style = MaterialTheme.typography.titleMedium)
}

@Composable
fun ChatInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    onImagePicked: (String) -> Unit // Base64 string
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                val base64 = uriToBase64(context, it)
                if (base64 != null) {
                    onImagePicked(base64)
                }
            }
        }
    )

    Row(
        modifier = Modifier
            .padding(16.dp)
            .navigationBarsPadding()
            .imePadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { 
                pickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Upload", tint = RoseRed)
        }
        Spacer(modifier = Modifier.width(8.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Message...", color = Color.White.copy(alpha = 0.5f)) },
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(28.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.05f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = RoseRed
            ),
            trailingIcon = {
                IconButton(onClick = { onValueChange("Send me a sexy photo 😉") }) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Request Pic",
                        tint = RoseRed.copy(alpha = 0.6f)
                    )
                }
            }
        )
        Spacer(modifier = Modifier.width(12.dp))
        FloatingActionButton(
            onClick = onSend,
            containerColor = RoseRed,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(Icons.Default.Send, contentDescription = "Send", modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun RelationshipMeterMini(trust: Int, romance: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LinearProgressIndicator(
            progress = { trust / 100f },
            modifier = Modifier.width(44.dp).height(6.dp).clip(CircleShape),
            color = Color(0xFF00E5FF),
            trackColor = Color.White.copy(alpha = 0.1f)
        )
        LinearProgressIndicator(
            progress = { romance / 100f },
            modifier = Modifier.width(44.dp).height(6.dp).clip(CircleShape),
            color = RoseRed,
            trackColor = Color.White.copy(alpha = 0.1f)
        )
    }
}

@Composable
fun TypingIndicator(name: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "bounce")

    // Three dots with staggered bounce animations
    val delays = listOf(0, 200, 400)
    val bounces = delays.map { delay ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = -12f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 600, delayMillis = delay, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bounce_$delay"
        )
    }

    Row(
        modifier = Modifier
            .padding(start = 12.dp, top = 8.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Glassmorphic bubble container
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                bounces.forEach { bounce ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .graphicsLayer { translationY = bounce.value }
                            .background(RoseRed, CircleShape)
                    )
                }
            }
        }
    }
}

fun uriToBase64(context: android.content.Context, uri: android.net.Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        if (bytes != null) {
            android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
        } else null
    } catch (e: Exception) {
        null
    }
}
