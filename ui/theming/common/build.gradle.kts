plugins {
    com.edricchan.studybuddy.library.android
    com.edricchan.studybuddy.library.testing.`kotest-junit5`
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

    api(libs.flowPreferences)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.mockk.core)

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
