import com.android.build.gradle.tasks.factory.AndroidUnitTest

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

    testFixtures.enable = true
}

tasks.withType<AndroidUnitTest>().configureEach {
    useJUnitPlatform()
}

dependencies {
    api(projects.ui.theming.common)

    implementation(libs.androidx.compose.material3)

    implementation(libs.accompanist.material3.themeAdapter)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertion.core)
    testImplementation(libs.kotest.property)

    // TODO: Remove when https://issuetracker.google.com/issues/340315591 is fixed
    testFixturesCompileOnly(libs.kotlin.stdlib) {
        because("Required until https://issuetracker.google.com/issues/340315591 is fixed")
    }
    testFixturesApi(libs.kotlin.test)
    testFixturesApi(libs.kotest.assertion.core)
    testFixturesApi(libs.kotest.property)
    testFixturesApi(libs.androidx.compose.ui)
    testFixturesApi(libs.androidx.compose.material3)

    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
