plugins {
    com.edricchan.studybuddy.library.android
}

android {
    namespace = "com.edricchan.studybuddy.exts.material"

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
    api(libs.androidx.annotation)
    implementation(libs.androidx.appcompat)
    implementation(libs.materialComponents)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    coreLibraryDesugaring(libs.android.desugar)
}
