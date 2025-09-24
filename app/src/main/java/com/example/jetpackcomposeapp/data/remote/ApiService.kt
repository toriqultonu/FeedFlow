package com.example.jetpackcomposeapp.data.remote

import com.example.jetpackcomposeapp.data.remote.PostDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("posts")
    suspend fun getPosts(
        @Query("_start") start: Int,
        @Query("_limit") limit: Int
    ): List<PostDto>
}