plugins {
    id("com.edricchan.studybuddy.library.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
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
}

dependencies {
    // Project dependencies
    implementation(projects.ui.common)
    implementation(projects.core.deeplink)
    implementation(projects.core.di)
    implementation(projects.core.resources)
    implementation(projects.data.serialization.android)
    implementation(projects.exts.common)
    implementation(projects.exts.material)
    implementation(projects.utils.recyclerview)
    implementation(projects.utils.web)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.recyclerview.core)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.materialComponents)
    implementation(libs.playServices.ossLicenses.core)

    implementation(libs.bundles.ktor)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
