plugins {
    // Compose interop for selection bottom sheet
    com.edricchan.studybuddy.library.`android-compose`
    com.edricchan.studybuddy.library.testing.`kotest-junit5`
    `kotlin-parcelize`
}

android {
    namespace = "com.edricchan.studybuddy.ui.widgets.modalbottomsheet"

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

    // See https://stackoverflow.com/a/76462186
    packaging {
        // For MockK
        jniLibs.useLegacyPackaging = true
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }

    buildFeatures.viewBinding = true

    testFixtures.enable = true
}

dependencies {
    implementation(projects.exts.androidx.viewBinding)
    implementation(projects.ui.insets)
    implementation(projects.ui.widgets.views) {
        because("NoTextCheckBox, NoTextRadioButton")
    }
    implementation(projects.core.resources)
    implementation(projects.utils.recyclerview)

    api(libs.androidx.fragment.ktx)
    api(libs.androidx.recyclerview.core)
    api(libs.materialComponents)

    //#region Compose dependencies
    implementation(projects.exts.androidx.compose)
    implementation(projects.ui.theming.compose)
    implementation(projects.ui.widgets.compose)

    api(libs.bundles.androidx.compose)
    implementation(libs.androidx.fragment.compose)

    debugImplementation(libs.bundles.androidx.compose.tooling)
    //#endregion

    debugImplementation(libs.androidx.fragment.testing.manifest)
    androidTestImplementation(libs.mockk.core)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.androidx.lifecycle.viewmodel.testing)
    androidTestImplementation(libs.androidx.fragment.testing)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.ext.junit.ktx)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.espresso.core)

    // Compose rule support
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    testFixturesApi(libs.kotlin.test)
    testFixturesApi(libs.kotest.assertion.core)
    testFixturesApi(libs.kotest.property)
}
