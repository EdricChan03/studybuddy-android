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

    implementation(libs.androidx.compose.material3)

    implementation(libs.accompanist.material3.themeAdapter)

    testImplementation(kotlin("test"))
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertion.core)
    testImplementation(libs.kotest.property)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
