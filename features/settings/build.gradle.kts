plugins {
    com.edricchan.studybuddy.library.`android-compose`
    com.edricchan.studybuddy.library.`android-hilt`
}

android {
    namespace = "com.edricchan.studybuddy.features.settings"

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

    compileOptions.isCoreLibraryDesugaringEnabled = true
}

dependencies {
    implementation(projects.core.compat.navigation)
    implementation(projects.core.resources.temporal)
    implementation(projects.core.settings.appearance.model)
    implementation(projects.core.settings.tasks)
    implementation(projects.core.settings.tracking)
    implementation(projects.core.settings.updates)

    api(projects.ui.preference)

    implementation(projects.exts.androidx.preference)
    implementation(projects.exts.common)
    implementation(projects.ui.common)
    implementation(projects.ui.theming.compose)
    implementation(projects.ui.theming.views)
    implementation(projects.ui.preference.compose)

    api(libs.androidx.preference.ktx)

    // Compose
    implementation(libs.bundles.androidx.compose)
    debugImplementation(libs.bundles.androidx.compose.tooling)

    implementation(libs.flowPreferences)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    // Compose rule support
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    coreLibraryDesugaring(libs.android.desugar)
}
