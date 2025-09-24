package com.example.jetpackcomposeapp.data.repository

import androidx.paging.PagingData
import com.example.jetpackcomposeapp.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(): Flow<PagingData<Post>>
    fun searchPosts(query: String): Flow<PagingData<Post>>
    fun getFavorites(): Flow<PagingData<Post>>
    suspend fun toggleFavorite(postId: Int)
    suspend fun isFavorite(postId: Int): Boolean
}