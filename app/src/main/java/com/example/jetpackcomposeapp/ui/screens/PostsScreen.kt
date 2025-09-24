package com.example.jetpackcomposeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.jetpackcomposeapp.LocalDarkTheme
import com.example.jetpackcomposeapp.LocalSharedPreferences
import com.example.jetpackcomposeapp.ui.theme.PreferencesKeys
import kotlinx.coroutines.launch
import com.example.jetpackcomposeapp.ui.theme.themeDataStore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsScreen(navController: NavController) {
    val viewModel: PostsViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()
    val posts = viewModel.posts.collectAsLazyPagingItems()
    val coroutineScope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    val isDarkTheme = LocalDarkTheme.current
    val sharedPreferences = LocalSharedPreferences.current
    val context = LocalContext.current


    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Posts") }, actions = {
                IconButton(onClick = { navController.navigate("favorites") }) {
                    Icon(Icons.Default.Favorite, contentDescription = "Favorites")
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
                        contentDescription = "Toggle Dark Mode"
                    )
                }


                IconButton(onClick = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                }
            })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth()
            )
            val displayPosts = if (searchQuery.isEmpty()) posts else viewModel.searchPosts(searchQuery).collectAsLazyPagingItems()

            LazyColumn {
                items(displayPosts.itemCount) { index ->
                    val post = displayPosts[index] ?: return@items
                    var isFavorite by remember { mutableStateOf(false) }
                    LaunchedEffect(post.id) {
                        isFavorite = viewModel.isFavorite(post.id)
                    }
                    Card(modifier = Modifier.padding(8.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(post.title, style = MaterialTheme.typography.titleMedium)
                            Text(post.body)
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    viewModel.toggleFavorite(post.id)
                                    isFavorite = !isFavorite
                                }
                            }) {
                                Icon(if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, "Favorite")
                            }
                        }
                    }
                }
                if (displayPosts.loadState.refresh is LoadState.Loading) {
                    item { CircularProgressIndicator(modifier = Modifier.fillMaxWidth()) }
                }
                if (displayPosts.loadState.append is LoadState.Loading) {
                    item { CircularProgressIndicator(modifier = Modifier.fillMaxWidth()) }
                }
                if (displayPosts.loadState.refresh is LoadState.Error) {
                    item { Text("Error loading posts") }
                }
                if (displayPosts.itemCount == 0) {
                    item { Text("No posts found") }
                }
            }
        }
    }
}