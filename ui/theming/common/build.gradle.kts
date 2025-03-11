plugins {
    com.edricchan.studybuddy.library.android
}

android {
    namespace = "com.edricchan.studybuddy.ui.theming.common"

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
    api(projects.core.settings.appearance)
    implementation(projects.exts.androidx.preference)

    implementation(libs.androidx.core.ktx)
    api(libs.materialComponents) {
        because("Check if dynamic colours are supported")
    }
    implementation(libs.androidx.preference.ktx) {
        because("Accessing SharedPreferences")
    }

    implementation(libs.flowPreferences)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertion.core)
    testImplementation(libs.kotest.property)
    testImplementation(libs.mockk.core)

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
