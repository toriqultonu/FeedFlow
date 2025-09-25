plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.devtools.ksp)
    id("com.google.dagger.hilt.android")
    id("androidx.room")

}

android {
    namespace = "com.example.jetpackcomposeapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.jetpackcomposeapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17) // Use the same Java version as in compileOptions
    }
    buildFeatures {
        compose = true
    }
//
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    implementation(libs.androidx.hilt.work) // Or the latest version
    ksp(libs.androidx.hilt.compiler) //

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)


    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.paging.compose) // Or the latest version
    implementation(libs.androidx.hilt.navigation.compose) // Or the latest version

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.androidx.material.icons.extended)


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.material)

    implementation(libs.gson)


    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)



    implementation(libs.androidx.room.runtime)


    ksp(libs.androidx.room.room.compiler)


    annotationProcessor(libs.androidx.room.room.compiler)

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)

    // optional - RxJava2 support for Room
    implementation(libs.androidx.room.rxjava2)

    // optional - RxJava3 support for Room
    implementation(libs.androidx.room.rxjava3)

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation(libs.androidx.room.guava)

    // optional - Test helpers
    testImplementation(libs.androidx.room.testing)

    // optional - Paging 3 Integration
    implementation(libs.androidx.room.paging)

    implementation(libs.androidx.ui.text.google.fonts)

    // Unit tests
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.androidx.core.testing) // For InstantTaskExecutorRule
    testImplementation(libs.kotlinx.coroutines.test) // For coroutine testing

// UI tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core) // Optional if mixing with Espresso
    androidTestImplementation(libs.ui.test.junit4) // Compose UI Testing
    debugImplementation(libs.ui.test.manifest) // For test manifest

    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation("org.mockito:mockito-android:5.14.0")


}