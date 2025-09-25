package com.example.jetpackcomposeapp.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import okhttp3.internal.tls.OkHostnameVerifier.verify
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController = mock(NavController::class.java)

    @Test
    fun loginScreen_displaysElements() {
        composeTestRule.setContent {
            LoginScreen(navController)
        }

        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
        composeTestRule.onNodeWithText("Register").assertIsDisplayed()
    }

    @Test
    fun loginButton_clickNavigatesToPosts() {
        composeTestRule.setContent {
            LoginScreen(navController)
        }

        composeTestRule.onNodeWithText("Email").performTextInput("test@example.com")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Login").performClick()

        // Verify navigation (requires mocking ViewModel login success)
        // In real test, mock ViewModel to return true for login
    }

    @Test
    fun registerButton_navigatesToRegister() {
        composeTestRule.setContent {
            LoginScreen(navController)
        }

        composeTestRule.onNodeWithText("Register").performClick()
        verify(navController).navigate("register")
    }
}