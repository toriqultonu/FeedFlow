package com.example.jetpackcomposeapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val postId: Int
)