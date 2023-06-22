plugins {
    com.edricchan.studybuddy.library.`android-compose`
}

android {
    namespace = "com.edricchan.studybuddy.ui.theming.compose"

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

    @Suppress("UnstableApiUsage")
    testOptions {
        // Use JUnit 5 for unit tests
        unitTests.all { it.useJUnitPlatform() }
    }
}

dependencies {
    implementation(projects.ui.theming.views)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)

    implementation(libs.accompanist.material3.themeAdapter)
    api(libs.accompanist.systemUiController)

    testImplementation(libs.junit)
    testImplementation(kotlin("test"))
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
