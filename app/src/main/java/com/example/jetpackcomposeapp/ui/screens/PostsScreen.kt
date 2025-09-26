package com.example.jetpackcomposeapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.jetpackcomposeapp.LocalDarkTheme
import com.example.jetpackcomposeapp.R
import com.example.jetpackcomposeapp.ui.theme.PreferencesKeys
import com.example.jetpackcomposeapp.ui.theme.themeDataStore
import com.example.jetpackcomposeapp.ui.viewmodel.AuthViewModel
import com.example.jetpackcomposeapp.ui.viewmodel.PostsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsScreen(navController: NavController) {
    val viewModel: PostsViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()
    val posts = viewModel.posts.collectAsLazyPagingItems()
    val coroutineScope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    val isDarkTheme = LocalDarkTheme.current
    val context = LocalContext.current

    AnimatedVisibility(visible = true, enter = fadeIn()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.explore_posts), style = MaterialTheme.typography.titleLarge) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    actions = {
                        IconButton(onClick = { navController.navigate("favorites") }) {
                            Icon(Icons.Default.Favorite, contentDescription = stringResource(R.string.favorites))
                        }
                        IconButton(onClick = {
                            val newValue = !isDarkTheme.value
                            isDarkTheme.value = newValue
                            coroutineScope.launch {
                                context.themeDataStore.edit { prefs ->
                                    prefs[PreferencesKeys.DARK_MODE] = newValue
                                }
                            }
                        }) {
                            Icon(
                                if (isDarkTheme.value) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                                contentDescription = "Toggle Theme"
                            )
                        }
                        IconButton(onClick = {
                            authViewModel.logout()
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = stringResource(
                                R.string.logout
                            ))
                        }
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text(stringResource(R.string.search_posts)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = MaterialTheme.shapes.small,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                )
                val displayPosts = if (searchQuery.isEmpty()) posts else viewModel.searchPosts(searchQuery).collectAsLazyPagingItems()

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(displayPosts.itemCount) { index ->
                        val post = displayPosts[index] ?: return@items
                        var isFavorite by remember { mutableStateOf(false) }
                        LaunchedEffect(post.id) {
                            isFavorite = viewModel.isFavorite(post.id)
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .shadow(2.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(post.title, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    post.body,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            viewModel.toggleFavorite(post.id)
                                            isFavorite = !isFavorite
                                        }
                                    },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Icon(
                                        if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = stringResource(R.string.favorite),
                                        tint = if (isFavorite) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                    if (displayPosts.loadState.refresh is LoadState.Loading) {
                        // Show 5 shimmering placeholders for initial load
                        items(5) {
                            ShimmerPostCard()
                        }
                    }
                    if (displayPosts.loadState.append is LoadState.Loading) {
                        // Show one shimmering placeholder for appending more items
                        item {
                            ShimmerPostCard()
                        }
                    }
                    if (displayPosts.loadState.refresh is LoadState.Error) {
                        item {
                            Text(
                                "Error loading posts",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    if (displayPosts.itemCount == 0 && displayPosts.loadState.refresh !is LoadState.Loading) {
                        item {
                            Text(
                                "No posts found",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

// Reusable Shimmer Post Card for loading animation
@Composable
fun ShimmerPostCard() {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .shadow(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(brush),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Placeholder for title
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(20.dp)
                    .background(brush, RoundedCornerShape(4.dp))
            )
            // Placeholder for body (3 lines)
            repeat(3) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .background(brush, RoundedCornerShape(4.dp))
                )
            }
            // Placeholder for favorite button
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .size(24.dp)
                    .background(brush, RoundedCornerShape(50))
            )
        }
    }
}