plugins {
    com.edricchan.studybuddy.library.android
    com.edricchan.studybuddy.library.testing.`kotest-junit5`
}

android {
    namespace = "com.edricchan.studybuddy.exts.androidx.preference"

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
    api(libs.androidx.annotation)
    implementation(libs.androidx.preference.ktx)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
