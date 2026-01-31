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
    implementation(projects.core.settings.appearance.resources)
    implementation(projects.core.settings.appearance.model)
    implementation(projects.core.settings.tasks)
    implementation(projects.core.settings.tasks.resources)
    implementation(projects.core.settings.tracking)
    implementation(projects.core.settings.updates)
    implementation(projects.core.settings.updates.resources)

    implementation(projects.core.auth.ui.compose)
    implementation(projects.core.resources.icons)
    implementation(projects.exts.androidx.compose)
    implementation(projects.exts.androidx.preference)
    implementation(projects.exts.common)
    implementation(projects.ui.common)
    implementation(projects.ui.theming.compose)
    implementation(projects.ui.theming.views)
    implementation(projects.ui.preference.compose)
    implementation(projects.utils.androidx.core)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    api(libs.androidx.preference.ktx)

    // Compose
    implementation(libs.bundles.androidx.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    api(libs.androidx.compose.material3.adaptive.navigation)

    debugImplementation(libs.bundles.androidx.compose.tooling)

    implementation(libs.flowPreferences)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    // Compose rule support
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    coreLibraryDesugaring(libs.android.desugar)
}
