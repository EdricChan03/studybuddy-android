plugins {
    com.edricchan.studybuddy.library.`android-compose`
    org.jetbrains.kotlin.plugin.parcelize
}

android {
    namespace = "com.edricchan.studybuddy.ui.widgets.compose.form.chips"

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
    implementation(projects.core.resources.icons)

    api(libs.bundles.androidx.compose)

    debugImplementation(libs.bundles.androidx.compose.tooling)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    // Compose rule support
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
