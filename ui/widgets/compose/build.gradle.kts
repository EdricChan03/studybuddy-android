import com.android.build.gradle.tasks.factory.AndroidUnitTest

plugins {
    com.edricchan.studybuddy.library.`android-compose`
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

tasks.withType<AndroidUnitTest>().configureEach {
    useJUnitPlatform()
}

dependencies {
    implementation(projects.ui.theming.compose)
    implementation(projects.core.resources)

    api(libs.bundles.androidx.compose)

    debugImplementation(libs.bundles.androidx.compose.tooling)

    implementation(libs.accompanist.drawablePainter)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertion.core)
    testImplementation(libs.kotest.property)

    androidTestImplementation(libs.kotlin.test)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)

    // Compose rule support
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
