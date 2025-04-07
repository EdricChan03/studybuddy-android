import com.android.build.gradle.tasks.factory.AndroidUnitTest

plugins {
    com.edricchan.studybuddy.library.android
    alias(libs.plugins.kotlin.plugin.serialization)
}

android {
    namespace = "com.edricchan.studybuddy.exts.androidx.navigation.runtime"

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
    api(libs.androidx.navigation.runtime.ktx)
    api(libs.kotlinx.serialization.core)

    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertion.core)
    testImplementation(libs.kotest.property)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
