package com.example.jetpackcomposeapp.ui.screens

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.example.jetpackcomposeapp.data.repository.PostRepository
import com.example.jetpackcomposeapp.domain.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class PostsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: PostsViewModel
    private lateinit var repository: PostRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(PostRepository::class.java)
        viewModel = PostsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getPosts_returnsPagingData() = runTest {
        val mockPosts = flowOf(PagingData.from(listOf(Post(1, 1, "Title", "Body"))))
        `when`(repository.getPosts()).thenReturn(mockPosts)
        assert(viewModel.posts == mockPosts)
    }

    @Test
    fun searchPosts_returnsPagingData() = runTest {
        val mockSearch = flowOf(PagingData.from(listOf(Post(1, 1, "Title", "Body"))))
        `when`(repository.searchPosts("query")).thenReturn(mockSearch)
        val result = viewModel.searchPosts("query")
        assert(result == mockSearch)
    }

    @Test
    fun toggleFavorite_callsRepository() = runTest {
        viewModel.toggleFavorite(1)
        verify(repository).toggleFavorite(1)
    }

    @Test
    fun isFavorite_returnsBoolean() = runTest {
        `when`(repository.isFavorite(1)).thenReturn(true)
        val result = viewModel.isFavorite(1)
        assert(result)
    }
}