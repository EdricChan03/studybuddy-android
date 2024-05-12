plugins {
    com.edricchan.studybuddy.library.`android-compose`
    com.edricchan.studybuddy.library.`android-hilt`
}

android {
    namespace = "com.edricchan.studybuddy.features.tasks"

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
    api(projects.core.settings.tasks)
    implementation(projects.data.common)
    implementation(projects.exts.androidx.preferenceFiles)
    implementation(projects.exts.common)
    implementation(projects.ui.theming.compose)

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Compose
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.bundles.androidx.compose)

    // Compose Tooling
    debugImplementation(libs.bundles.androidx.compose.tooling)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth.ktx)

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Markdown support
    implementation(libs.composeRichText.ui.material3)
    implementation(libs.composeRichText.commonmark)

    // Migration
    implementation(libs.migration)

    coreLibraryDesugaring(libs.android.desugar.nio)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
