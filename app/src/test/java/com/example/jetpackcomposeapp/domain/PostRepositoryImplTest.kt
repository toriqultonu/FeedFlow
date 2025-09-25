package com.example.jetpackcomposeapp.domain

import androidx.paging.PagingSource
import com.example.jetpackcomposeapp.data.local.FavoriteEntity
import com.example.jetpackcomposeapp.data.local.PostDao
import com.example.jetpackcomposeapp.data.local.PostEntity
import com.example.jetpackcomposeapp.data.remote.ApiService
import com.example.jetpackcomposeapp.data.repository.PostRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class PostRepositoryImplTest {

    private val apiService = mock(ApiService::class.java)
    private val postDao = mock(PostDao::class.java)
    private val repository = PostRepositoryImpl(apiService, postDao)

    @Test
    fun toggleFavorite_insertsWhenNotFavorite() = runTest {
        `when`(postDao.isFavorite(1)).thenReturn(0)
        repository.toggleFavorite(1)
        verify(postDao).insertFavorite(FavoriteEntity(1))
    }

    @Test
    fun toggleFavorite_deletesWhenFavorite() = runTest {
        `when`(postDao.isFavorite(1)).thenReturn(1)
        repository.toggleFavorite(1)
        verify(postDao).deleteFavorite(1)
    }

    @Test
    fun isFavorite_returnsTrueWhenFavorite() = runTest {
        `when`(postDao.isFavorite(1)).thenReturn(1)
        val result = repository.isFavorite(1)
        assert(result)
    }

    @Test
    fun getPosts_returnsPagingSource() {
        val mockPagingSource = mock<PagingSource<Int, PostEntity>>()
        `when`(postDao.getPosts()).thenReturn(mockPagingSource)
        repository.getPosts() // Just invoke to verify setup; actual flow testing requires more setup
        verify(postDao).getPosts()
    }

    @Test
    fun searchPosts_returnsPagingSource() {
        val mockPagingSource = mock<PagingSource<Int, PostEntity>>()
        `when`(postDao.searchPosts("query")).thenReturn(mockPagingSource)
        repository.searchPosts("query")
        verify(postDao).searchPosts("query")
    }

    @Test
    fun getFavorites_returnsPagingSource() {
        val mockPagingSource = mock<PagingSource<Int, PostEntity>>()
        `when`(postDao.getFavorites()).thenReturn(mockPagingSource)
        repository.getFavorites()
        verify(postDao).getFavorites()
    }
}