package com.example.jetpackcomposeapp.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class PostsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController = mock(NavController::class.java)

    @Test
    fun postsScreen_displaysElements() {
        composeTestRule.setContent {
            PostsScreen(navController)
        }

        composeTestRule.onNodeWithText("Posts").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search").assertIsDisplayed()
    }

    @Test
    fun favoritesButton_navigatesToFavorites() {
        composeTestRule.setContent {
            PostsScreen(navController)
        }

        composeTestRule.onNodeWithContentDescription("Favorites").performClick()
        verify(navController).navigate("favorites")
    }

    @Test
    fun search_updatesDisplay() {
        composeTestRule.setContent {
            PostsScreen(navController)
        }

        composeTestRule.onNodeWithText("Search").performTextInput("query")
        // Assert UI updates (e.g., filtered posts shown; requires mocking ViewModel)
    }

    @Test
    fun logoutButton_navigatesToLogin() {
        composeTestRule.setContent {
            PostsScreen(navController)
        }

        composeTestRule.onNodeWithContentDescription("Logout").performClick()
        verify(navController).navigate("login")
    }
}