plugins {
    com.edricchan.studybuddy.library.`android-compose`
}

android {
    namespace = "com.edricchan.studybuddy.features.tasks"

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

    compileOptions.isCoreLibraryDesugaringEnabled = true
}


dependencies {
    implementation(projects.exts.androidx.preferenceFiles)
    implementation(projects.exts.common)

    // AndroidX
    implementation(libs.androidx.core.ktx)

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Migration
    implementation(libs.migration)

    coreLibraryDesugaring(libs.android.desugar.nio)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
