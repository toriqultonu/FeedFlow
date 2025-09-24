package com.example.jetpackcomposeapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.jetpackcomposeapp.data.local.AppDatabase
import com.example.jetpackcomposeapp.data.local.PostDao
import com.example.jetpackcomposeapp.data.local.UserDao
import com.example.jetpackcomposeapp.data.remote.ApiService
import com.example.jetpackcomposeapp.data.repository.PostRepository
import com.example.jetpackcomposeapp.data.repository.PostRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_db"
    ).fallbackToDestructiveMigration(false).build()

    @Provides
    @Singleton
    fun providePostDao(db: AppDatabase): PostDao = db.postDao()

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providePostRepository(api: ApiService, dao: PostDao): PostRepository = PostRepositoryImpl(api, dao)
}