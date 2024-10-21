plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.hilt)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.test.movieapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.test.movieapplication"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "API_KEY", "\"${properties["apiKey"]}\"")
            buildConfigField( "String", "IMAGE_URL", "\"https://image.tmdb.org/t/p/w500\"")
        }
        release {
            buildConfigField("String", "API_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "API_KEY", "\"${properties["apiKey"]}\"")
            buildConfigField( "String", "IMAGE_URL", "\"https://image.tmdb.org/t/p/w500\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.lifecycle.viewmodel.compose)

    testImplementation (libs.mockk)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)

    // Add this if using Mockito with coroutines
    testImplementation(libs.mockito.inline)

    //  Room dependencies
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)
    testImplementation(libs.room.testing)
    implementation(libs.room.paging)

    // Accompanist Swipe Refresh
    implementation(libs.accompanist.swiperefresh)
    // Coroutines test library
    testImplementation(libs.kotlinx.coroutines.test)

    // Hilt dependencies
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Retrofit for networking
    implementation(libs.android.retrofit)
    implementation(libs.android.retrofit.converter)

    // Paging
    implementation(libs.android.paging)

    // Coil for images
    implementation(libs.coil.compose)

    // Compose Paging
    implementation(libs.compose.paging)
}
