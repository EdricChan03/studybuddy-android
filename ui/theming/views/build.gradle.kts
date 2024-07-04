plugins {
    com.edricchan.studybuddy.library.android
}

android {
    namespace = "com.edricchan.studybuddy.ui.theming"

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
    api(projects.ui.theming.common)
    implementation(projects.ui.preference) {
        because("Preference XML styling")
    }

    implementation(projects.core.settings.appearance)
    implementation(projects.exts.androidx.preference)

    api(libs.androidx.annotation)
    api(libs.materialComponents)
    implementation(libs.androidx.preference.ktx)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.mockk.core)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
