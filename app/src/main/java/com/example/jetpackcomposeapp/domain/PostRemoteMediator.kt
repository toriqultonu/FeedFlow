package com.example.jetpackcomposeapp.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.jetpackcomposeapp.data.local.PostDao
import com.example.jetpackcomposeapp.data.local.PostEntity
import com.example.jetpackcomposeapp.data.remote.ApiService
import com.example.jetpackcomposeapp.data.repository.toEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val api: ApiService,
    private val dao: PostDao
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) 0 else (state.pages.size * state.config.pageSize)
                }
            }

            val posts = api.getPosts(loadKey, state.config.pageSize)

            dao.insertPosts(posts.map { it.toEntity() })

            MediatorResult.Success(endOfPaginationReached = posts.isEmpty())
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}