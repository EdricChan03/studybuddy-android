plugins {
    com.edricchan.studybuddy.library.`android-compose`
    com.edricchan.studybuddy.library.testing.`kotest-junit5`
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

    testFixtures.enable = true
}

dependencies {
    api(projects.ui.theming.common)

    api(libs.androidx.compose.material3)

    testImplementation(libs.kotlin.test)

    testFixturesApi(libs.kotlin.test)
    testFixturesApi(libs.kotest.assertion.core)
    testFixturesApi(libs.kotest.property)
    testFixturesApi(libs.androidx.compose.ui)
    testFixturesApi(libs.androidx.compose.material3)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
