plugins {
    com.edricchan.studybuddy.library.`android-compose`
    org.jetbrains.kotlin.plugin.parcelize
}

android {
    namespace = "com.edricchan.studybuddy.ui.widgets.views.markdown"

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

    packaging.resources {
        excludes += setOf(
            "META-INF/LICENSE.md",
            "META-INF/LICENSE-notice.md"
        )
    }
}

dependencies {
    api(projects.ui.widgets.compose.markdownViewer)
    implementation(projects.ui.theming.compose)

    api(libs.bundles.androidx.compose)

    implementation(libs.androidx.savedstate.ktx)

    testImplementation(libs.kotlin.test)

    androidTestImplementation(libs.kotlin.test)
    androidTestImplementation(libs.mockk.core)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.androidx.activity.ktx)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    // Compose rule support
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
