plugins {
    com.edricchan.studybuddy.library.`android-compose`
}

android {
    namespace = "com.edricchan.studybuddy.features.about"

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
    implementation(projects.core.resources)
    implementation(projects.exts.android.metadata)
    implementation(projects.ui.theming.compose)
    implementation(projects.ui.widgets.compose)
    implementation(projects.ui.widgets.compose.segmentedList)

    // Compose
    implementation(libs.bundles.androidx.compose)

    // Compose Tooling
    debugImplementation(libs.bundles.androidx.compose.tooling)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
