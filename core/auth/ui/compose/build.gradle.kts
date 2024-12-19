plugins {
    com.edricchan.studybuddy.library.`android-compose`
}

android {
    namespace = "com.edricchan.studybuddy.core.auth"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    api(projects.core.auth.model)
    api(projects.core.auth.ui.common)
    implementation(projects.ui.theming.compose)

    // Firebase dependencies
    api(platform(libs.firebase.bom))
    api(libs.firebase.auth.ktx)
    implementation(libs.playServices.auth)

    // Compose dependencies
    implementation(libs.bundles.androidx.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    debugImplementation(libs.bundles.androidx.compose.tooling)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    // Compose rule support
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
