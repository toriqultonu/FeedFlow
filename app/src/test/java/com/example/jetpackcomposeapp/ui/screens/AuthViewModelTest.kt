package com.example.jetpackcomposeapp.ui.screens

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.jetpackcomposeapp.data.local.UserDao
import com.example.jetpackcomposeapp.data.local.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class AuthViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AuthViewModel
    private lateinit var userDao: UserDao
    private lateinit var sharedPreferences: SharedPreferences

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        userDao = mock(UserDao::class.java)
        sharedPreferences = mock(SharedPreferences::class.java)
        viewModel = AuthViewModel(userDao, sharedPreferences)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getLoggedInEmail_returnsEmailFromPrefs() {
        `when`(sharedPreferences.getString("logged_in_email", null)).thenReturn("test@example.com")
        val result = viewModel.getLoggedInEmail()
        assert(result == "test@example.com")
    }

    @Test
    fun logout_clearsPrefs() {
        val editor = mock(SharedPreferences.Editor::class.java)
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.remove("logged_in_email")).thenReturn(editor)
        `when`(editor.apply()).then { }

        viewModel.logout()
        verify(editor).remove("logged_in_email")
        verify(editor).apply()
    }

    @Test
    fun register_success() = runTest {
        val result = viewModel.register("test@example.com", "password123")
        verify(userDao).insertUser(UserEntity(email = "test@example.com", password = "password123"))
        assert(result)
    }

    @Test
    fun register_failure_duplicateEmail() = runTest {
        doThrow(RuntimeException()).`when`(userDao).insertUser(any())
        val result = viewModel.register("duplicate@example.com", "password123")
        assert(!result)
    }

    @Test
    fun login_success_savesEmail() = runTest {
        val editor = mock(SharedPreferences.Editor::class.java)
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString("logged_in_email", "test@example.com")).thenReturn(editor)
        `when`(editor.apply()).then { }

        `when`(userDao.getUserByEmail("test@example.com")).thenReturn(UserEntity(email = "test@example.com", password = "password123"))
        val result = viewModel.login("test@example.com", "password123")
        assert(result)
        verify(editor).putString("logged_in_email", "test@example.com")
        verify(editor).apply()
    }

    @Test
    fun login_failure_wrongPassword() = runTest {
        `when`(userDao.getUserByEmail("test@example.com")).thenReturn(UserEntity(email = "test@example.com", password = "wrong"))
        val result = viewModel.login("test@example.com", "password123")
        assert(!result)
    }

    @Test
    fun login_failure_userNotFound() = runTest {
        `when`(userDao.getUserByEmail("nonexistent@example.com")).thenReturn(null)
        val result = viewModel.login("nonexistent@example.com", "password123")
        assert(!result)
    }
}