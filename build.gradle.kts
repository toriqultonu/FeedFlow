// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // Make this version consistent with hilt-android-compiler
    id("com.google.dagger.hilt.android") version "2.57.1" apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    id("androidx.room") version "2.8.0" apply false // Ensure this Room version is compatible if you use Room with Hilt
}
    