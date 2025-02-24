plugins {
    com.edricchan.studybuddy.library.android

    kotlin("plugin.serialization")
}

android {
    namespace = "com.edricchan.studybuddy.core.compat.navigation"

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
    api(libs.androidx.navigation.common.ktx)
    api(libs.androidx.navigation.runtime.ktx)

    api(libs.kotlinx.serialization.core)
}
