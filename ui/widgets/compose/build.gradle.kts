plugins {
    id("com.edricchan.studybuddy.library.android-compose")
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

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)

    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)

    implementation(libs.accompanist.drawablePainter)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)

    androidTestImplementation(libs.kotlin.test)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)

    // Compose rule support
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
