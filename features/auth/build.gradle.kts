plugins {
    com.edricchan.studybuddy.library.`android-compose`
    com.edricchan.studybuddy.library.`android-hilt`
}

android {
    namespace = "com.edricchan.studybuddy.features.auth"

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
    api(projects.features.auth.navigation)
    implementation(projects.core.compat.navigation)
    implementation(projects.features.tasks.navigation)

    api(projects.core.deeplink)
    implementation(projects.core.auth.credentials)
    implementation(projects.core.auth.gms)
    implementation(projects.core.di)
    implementation(projects.core.resources)
    implementation(projects.core.resources.icons)
    implementation(projects.data.forms.compose)
    implementation(projects.exts.android)
    implementation(projects.exts.common)
    implementation(projects.exts.firebase.core)
    implementation(projects.exts.material)
    implementation(projects.ui.common)
    implementation(projects.ui.theming.views) {
        because("We need XML theming for the activity manifest declaration")
    }
    implementation(projects.ui.widgets.views)
    implementation(projects.utils.androidx.compose)
    implementation(projects.utils.network)

    // Jetpack Navigation
    api(libs.androidx.navigation.common.ktx)
    api(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment.ktx)

    implementation(libs.materialComponents)

    // Compose
    api(libs.bundles.androidx.compose)
    implementation(libs.androidx.compose.animation.graphics)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Compose Tooling
    debugImplementation(libs.bundles.androidx.compose.tooling)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth.ktx)

    // Auth Play Services
    api(libs.playServices.auth)

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.playServices)

    // CoIL
    implementation(libs.coil)

    coreLibraryDesugaring(libs.android.desugar)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
