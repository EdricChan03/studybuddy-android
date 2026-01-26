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
    implementation(projects.exts.coil)
    implementation(projects.ui.theming.compose)
    implementation(projects.utils.coil.compose)

    // Compose dependencies
    implementation(libs.bundles.androidx.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    debugImplementation(libs.bundles.androidx.compose.tooling)

    api(libs.coil.compose)
    api(libs.coil.zoomable)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    // Compose rule support
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
