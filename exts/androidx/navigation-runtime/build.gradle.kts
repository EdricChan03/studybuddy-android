plugins {
    com.edricchan.studybuddy.library.android
    com.edricchan.studybuddy.library.testing.`kotest-junit5`
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

dependencies {
    api(libs.androidx.navigation.runtime.ktx)
    api(libs.kotlinx.serialization.core)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
