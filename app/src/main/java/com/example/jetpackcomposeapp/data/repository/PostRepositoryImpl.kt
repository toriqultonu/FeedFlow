package com.example.jetpackcomposeapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.jetpackcomposeapp.data.local.FavoriteEntity
import com.example.jetpackcomposeapp.data.local.PostDao
import com.example.jetpackcomposeapp.data.local.PostEntity
import com.example.jetpackcomposeapp.data.remote.ApiService
import com.example.jetpackcomposeapp.data.remote.PostDto
import com.example.jetpackcomposeapp.domain.PostRemoteMediator
import com.example.jetpackcomposeapp.domain.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PostRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: PostDao
) : PostRepository {

    override fun getPosts(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { dao.getPosts() },
            remoteMediator = PostRemoteMediator(api, dao)
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun searchPosts(query: String): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { dao.searchPosts(query) }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun getFavorites(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { dao.getFavorites() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun toggleFavorite(postId: Int) {
        if (dao.isFavorite(postId) > 0) {
            dao.deleteFavorite(postId)
        } else {
            dao.insertFavorite(FavoriteEntity(postId))
        }
    }

    override suspend fun isFavorite(postId: Int): Boolean {
        return dao.isFavorite(postId) > 0
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}

fun PostEntity.toDomain() = Post(id, userId, title, body)

fun PostDto.toEntity() = PostEntity(id, userId, title, body)