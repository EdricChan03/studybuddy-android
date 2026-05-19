plugins {
    com.edricchan.studybuddy.library.android
    com.edricchan.studybuddy.library.`android-hilt`
    com.edricchan.studybuddy.library.`kotlinx-serialization`
}

android {
    namespace = "com.edricchan.studybuddy.core.updater"

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
    api(projects.data.serialization.android)
    implementation(projects.core.di)
    implementation(projects.core.resources)
    implementation(projects.exts.android)
    implementation(projects.exts.android.metadata)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.work.runtime.ktx)

    // Ktor + kotlinx.serialization
    implementation(libs.bundles.ktor)
    implementation(libs.kotlinx.serialization.json)

    // Dagger
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.semver)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
