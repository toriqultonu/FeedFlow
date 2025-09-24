package com.example.jetpackcomposeapp.ui.theme

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.jetpackcomposeapp.LocalDarkTheme
import com.example.jetpackcomposeapp.LocalSharedPreferences
import kotlinx.coroutines.flow.map
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme")

private val DarkColorScheme = darkColorScheme(
    primary = LightPurpleDark,
    secondary = MutedPurpleDark,
    tertiary = SoftPinkDark,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = LightGray,
    onSurface = LightGray,
    error = LightRed
)

private val LightColorScheme = lightColorScheme(
    primary = DeepPurple,
    secondary = LightPurple,
    tertiary = VividPink,
    background = SoftGray,
    surface = White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = DarkGray,
    onSurface = DarkGray,
    error = ErrorRed
)

val AppShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp)
)

@Composable
fun JetpackComposeAppTheme(
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val systemDarkTheme = isSystemInDarkTheme()

    val darkThemeFlow = remember {
        context.themeDataStore.data.map { prefs ->
            prefs[PreferencesKeys.DARK_MODE] ?: systemDarkTheme
        }
    }
    val darkTheme by darkThemeFlow.collectAsState(initial = systemDarkTheme)

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    val darkThemeState = remember { mutableStateOf(darkTheme) }

    LaunchedEffect(darkTheme) {
        darkThemeState.value = darkTheme
    }

    CompositionLocalProvider(
        LocalDarkTheme provides darkThemeState,
        LocalSharedPreferences provides
                context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = AppShapes,
            content = content
        )
    }
}