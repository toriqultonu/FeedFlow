package com.example.jetpackcomposeapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposeapp.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import androidx.paging.PagingData
import com.example.jetpackcomposeapp.domain.model.Post
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    val posts: Flow<PagingData<Post>> = repository.getPosts()

    val favorites: Flow<PagingData<Post>> = repository.getFavorites()

    fun searchPosts(query: String): Flow<PagingData<Post>> = repository.searchPosts(query)

    suspend fun isFavorite(postId: Int): Boolean = repository.isFavorite(postId)

    fun toggleFavorite(postId: Int) {
        viewModelScope.launch {
            repository.toggleFavorite(postId)
        }
    }
}