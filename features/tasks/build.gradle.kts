plugins {
    com.edricchan.studybuddy.library.`android-compose`
    com.edricchan.studybuddy.library.`android-hilt`
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

    buildFeatures.viewBinding = true

    compileOptions.isCoreLibraryDesugaringEnabled = true
}

dependencies {
    implementation(projects.core.compat.navigation)

    api(projects.core.settings.tasks)
    implementation(projects.core.resources)
    implementation(projects.core.resources.temporal)
    implementation(projects.data.common)
    implementation(projects.exts.androidx.preferenceFiles)
    implementation(projects.exts.common)
    implementation(projects.exts.datetime)
    implementation(projects.exts.firebase.core)
    implementation(projects.exts.firebase.temporal)
    implementation(projects.exts.markwon)
    implementation(projects.utils.recyclerview)
    implementation(projects.ui.theming.compose)

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.recyclerview.core)
    implementation(libs.androidx.recyclerview.selection)
    implementation(libs.materialComponents)

    // Compose
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.bundles.androidx.compose)

    // Compose Tooling
    debugImplementation(libs.bundles.androidx.compose.tooling)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth.ktx)

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Markdown support
    implementation(libs.composeRichText.ui.material3)
    implementation(libs.composeRichText.commonmark)

    // Migration
    implementation(libs.migration)

    coreLibraryDesugaring(libs.android.desugar.nio)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
