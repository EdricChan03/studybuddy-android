plugins {
    com.edricchan.studybuddy.library.android
    com.edricchan.studybuddy.library.`kotlinx-serialization`
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
}
