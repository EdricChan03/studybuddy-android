plugins {
    com.edricchan.studybuddy.library.android
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
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
    api(projects.core.settings.tasks)
    implementation(projects.data.common)
    implementation(projects.exts.androidx.preferenceFiles)
    implementation(projects.exts.common)

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth.ktx)

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Dagger
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)

    // Migration
    implementation(libs.migration)

    coreLibraryDesugaring(libs.android.desugar.nio)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
