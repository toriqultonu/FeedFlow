package com.example.jetpackcomposeapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("SELECT * FROM posts")
    fun getPosts(): PagingSource<Int, PostEntity>

    @Query("SELECT * FROM posts WHERE title LIKE '%' || :query || '%' OR body LIKE '%' || :query || '%'")
    fun searchPosts(query: String): PagingSource<Int, PostEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE postId = :postId")
    suspend fun deleteFavorite(postId: Int)

    @Query("SELECT * FROM posts WHERE id IN (SELECT postId FROM favorites)")
    fun getFavorites(): PagingSource<Int, PostEntity>

    @Query("SELECT COUNT(*) FROM favorites WHERE postId = :postId")
    suspend fun isFavorite(postId: Int): Int
}