package com.example.jetpackcomposeapp.ui.screens

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.jetpackcomposeapp.data.local.UserDao
import com.example.jetpackcomposeapp.data.local.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userDao: UserDao,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    fun getLoggedInEmail(): String? = sharedPreferences.getString("logged_in_email", null)

    private fun saveLoggedInEmail(email: String) {
        sharedPreferences.edit().putString("logged_in_email", email).apply()
    }

    fun logout() {
        sharedPreferences.edit().remove("logged_in_email").apply()
    }

    suspend fun register(email: String, password: String): Boolean {
        return try {
            userDao.insertUser(UserEntity(email = email, password = password))
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun login(email: String, password: String): Boolean {
        val user = userDao.getUserByEmail(email) ?: return false
        if (user.password == password) {
            saveLoggedInEmail(email)
            return true
        }
        return false
    }
}