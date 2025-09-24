package com.example.jetpackcomposeapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController) {
    val viewModel: PostsViewModel = hiltViewModel()
    val favorites = viewModel.favorites.collectAsLazyPagingItems()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Favorites") }) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(favorites.itemCount) { index ->
                val post = favorites[index] ?: return@items
                Card(modifier = Modifier.padding(8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(post.title, style = MaterialTheme.typography.titleMedium)
                        Text(post.body)
                    }
                }
            }
            if (favorites.itemCount == 0) {
                item { Text("No favorites") }
            }
        }
    }
}