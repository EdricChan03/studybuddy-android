plugins {
    com.edricchan.studybuddy.library.android
    com.edricchan.studybuddy.library.testing.`kotest-junit5`
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

    implementation(projects.core.settings.appearance.model)
    implementation(projects.exts.androidx.preference)

    api(libs.androidx.annotation)
    api(libs.androidx.swiperefreshlayout) {
        because("API for setting dynamic colours")
    }
    api(libs.materialComponents)
    implementation(libs.androidx.preference.ktx)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.mockk.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
