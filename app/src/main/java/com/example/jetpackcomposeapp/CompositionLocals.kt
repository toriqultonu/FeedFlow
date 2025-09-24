package com.example.jetpackcomposeapp

import android.content.SharedPreferences
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.MutableState

val LocalDarkTheme = compositionLocalOf<MutableState<Boolean>> { error("No dark theme state provided") }

val LocalSharedPreferences = compositionLocalOf<SharedPreferences> { error("No shared preferences provided") }