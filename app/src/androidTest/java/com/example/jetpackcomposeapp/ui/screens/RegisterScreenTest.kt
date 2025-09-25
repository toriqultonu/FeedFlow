package com.example.jetpackcomposeapp.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class RegisterScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController = mock(NavController::class.java)

    @Test
    fun registerScreen_displaysElements() {
        composeTestRule.setContent {
            RegisterScreen(navController)
        }

        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Confirm Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Register").assertIsDisplayed()
    }

    @Test
    fun registerButton_validInputNavigatesToLogin() {
        composeTestRule.setContent {
            RegisterScreen(navController)
        }

        composeTestRule.onNodeWithText("Email").performTextInput("test@example.com")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Confirm Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Register").performClick()

        verify(navController).navigate("login")
    }

    @Test
    fun registerButton_invalidEmailShowsError() {
        composeTestRule.setContent {
            RegisterScreen(navController)
        }

        composeTestRule.onNodeWithText("Email").performTextInput("invalid")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Confirm Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Register").performClick()

        composeTestRule.onNodeWithText("Invalid email").assertIsDisplayed()
    }
}