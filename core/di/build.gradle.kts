plugins {
    com.edricchan.studybuddy.library.android

    com.google.devtools.ksp
    com.google.dagger.hilt.android
}

android {
    namespace = "com.edricchan.studybuddy.core.di"

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
    implementation(projects.exts.androidx.preference)
    implementation(projects.exts.firebase.core)

    api(projects.core.auth.service)
    api(projects.ui.common)

    implementation(platform(libs.firebase.bom))
    api(libs.bundles.firebase)

    api(libs.bundles.ktor)

    api(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    coreLibraryDesugaring(libs.android.desugar)
}
