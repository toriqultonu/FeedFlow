package com.example.jetpackcomposeapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    val viewModel: AuthViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var generalError by remember { mutableStateOf("") }
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    var emailShake by remember { mutableStateOf(false) }
    var passwordShake by remember { mutableStateOf(false) }

    // Shake animation for error feedback
    val emailShakeOffset by animateFloatAsState(
        targetValue = if (emailShake) 4f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        )
    )
    val passwordShakeOffset by animateFloatAsState(
        targetValue = if (passwordShake) 4f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        )
    )

    AnimatedVisibility(visible = true, enter = fadeIn()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .shadow(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome Back",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = null // Clear error when typing
                        },
                        label = { Text("Email") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(emailFocusRequester)
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused && email.isNotEmpty()) {
                                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                        emailError = "Invalid email format"
                                        emailShake = true
                                        coroutineScope.launch {
                                            delay(300)
                                            emailShake = false
                                        }
                                    }
                                }
                            }
                            .graphicsLayer {
                                transformOrigin = TransformOrigin.Center
                                translationX = emailShakeOffset
                            },
                        shape = MaterialTheme.shapes.small,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            errorIndicatorColor = MaterialTheme.colorScheme.error
                        ),
                        isError = emailError != null
                    )
                    emailError?.let { error ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(200)),
                            exit = fadeOut(animationSpec = tween(200))
                        ) {
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = null // Clear error when typing
                        },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(passwordFocusRequester)
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused && password.isNotEmpty()) {
                                    if (password.length < 6) {
                                        passwordError = "Password must be at least 6 characters"
                                        passwordShake = true
                                        coroutineScope.launch {
                                            delay(300)
                                            passwordShake = false
                                        }
                                    }
                                }
                            }
                            .graphicsLayer {
                                transformOrigin = TransformOrigin.Center
                                translationX = passwordShakeOffset
                            },
                        shape = MaterialTheme.shapes.small,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            errorIndicatorColor = MaterialTheme.colorScheme.error
                        ),
                        isError = passwordError != null
                    )
                    passwordError?.let { error ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(200)),
                            exit = fadeOut(animationSpec = tween(200))
                        ) {
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // Validate all fields before login
                            emailError = null
                            passwordError = null
                            generalError = ""
                            var hasError = false
                            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                emailError = "Invalid email format"
                                emailShake = true
                                hasError = true
                                coroutineScope.launch {
                                    delay(300)
                                    emailShake = false
                                }
                            }
                            if (password.length < 6) {
                                passwordError = "Password must be at least 6 characters"
                                passwordShake = true
                                hasError = true
                                coroutineScope.launch {
                                    delay(300)
                                    passwordShake = false
                                }
                            }
                            if (!hasError) {
                                coroutineScope.launch {
                                    if (viewModel.login(email, password)) {
                                        navController.navigate("posts") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    } else {
                                        generalError = "Invalid credentials"
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Login", style = MaterialTheme.typography.labelMedium)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(onClick = { navController.navigate("register") }) {
                        Text(
                            "Create an account",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    if (generalError.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            generalError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}