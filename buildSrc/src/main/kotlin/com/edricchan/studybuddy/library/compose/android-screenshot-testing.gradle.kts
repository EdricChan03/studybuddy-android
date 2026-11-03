package com.edricchan.studybuddy.library.compose

plugins {
    com.android.library
}

private val libs = versionCatalogs.named("libs")

android {
    testOptions {
        screenshotTests {
            create("screenshotTest") {
                engineVersion =
                    libs.findVersion("android-compose-screenshotTest").get().toString()
                targetVariants.add("release")
                dependencies {
                    implementation(libs.findLibrary("androidx-compose-ui-tooling").get())
                    implementation(libs.findLibrary("android-screenshot-validation-api").get())
                }
            }
        }
    }
}
