package com.example.jetpackcomposeapp.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class FavoritesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController = mock(NavController::class.java)

    @Test
    fun favoritesScreen_displaysElements() {
        composeTestRule.setContent {
            FavoritesScreen(navController)
        }

        composeTestRule.onNodeWithText("Favorites").assertIsDisplayed()
    }

    @Test
    fun emptyFavorites_showsMessage() {
        composeTestRule.setContent {
            FavoritesScreen(navController)
        }

        // Assuming empty state; assert message
        composeTestRule.onNodeWithText("No favorites").assertIsDisplayed()
    }
}