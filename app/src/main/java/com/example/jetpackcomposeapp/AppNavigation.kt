package com.example.jetpackcomposeapp

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposeapp.ui.screens.AuthViewModel
import com.example.jetpackcomposeapp.ui.screens.FavoritesScreen
import com.example.jetpackcomposeapp.ui.screens.LoginScreen
import com.example.jetpackcomposeapp.ui.screens.PostsScreen
import com.example.jetpackcomposeapp.ui.screens.RegisterScreen

@Composable
fun AppNavigation() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val startDestination = if (authViewModel.getLoggedInEmail() != null) "posts" else "login"
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("posts") { PostsScreen(navController) }
        composable("favorites") { FavoritesScreen(navController) }
    }
}