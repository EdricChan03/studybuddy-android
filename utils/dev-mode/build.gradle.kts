plugins {
    com.edricchan.studybuddy.library.`android-compose`
}

android {
    namespace = "com.edricchan.studybuddy.utils.dev"

    buildFeatures.buildConfig = true
}

dependencies {
    implementation(projects.exts.androidx.preference)

    api(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)

    implementation(libs.androidx.core.ktx)
    api(libs.flowPreferences)
}
