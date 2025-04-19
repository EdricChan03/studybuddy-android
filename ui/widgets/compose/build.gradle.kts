plugins {
    com.edricchan.studybuddy.library.`android-compose`
    com.edricchan.studybuddy.library.testing.`kotest-junit5`
}

android {
    namespace = "com.edricchan.studybuddy.ui.widgets.compose"

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
    implementation(projects.ui.theming.compose)
    implementation(projects.core.resources)

    api(libs.bundles.androidx.compose)

    debugImplementation(libs.bundles.androidx.compose.tooling)

    implementation(libs.accompanist.drawablePainter)

    testImplementation(libs.kotlin.test)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    // Compose rule support
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
