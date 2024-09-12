plugins {
    com.edricchan.studybuddy.library.android
    com.edricchan.studybuddy.library.`android-hilt`

    kotlin("plugin.serialization")
}

android {
    namespace = "com.edricchan.studybuddy.features.help"

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
    // Project dependencies
    implementation(projects.ui.common)
    implementation(projects.ui.theming.views)
    implementation(projects.core.compat.navigation)
    implementation(projects.core.deeplink)
    implementation(projects.core.di)
    implementation(projects.core.resources)
    implementation(projects.data.serialization.android)
    implementation(projects.exts.androidx.preference)
    implementation(projects.exts.androidx.viewBinding)
    implementation(projects.exts.common)
    implementation(projects.exts.material)
    implementation(projects.utils.recyclerview)
    implementation(projects.utils.web)

    coreLibraryDesugaring(libs.android.desugar)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.recyclerview.core)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.materialComponents)

    implementation(libs.bundles.ktor)
    implementation(libs.kotlinx.serialization.json)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
