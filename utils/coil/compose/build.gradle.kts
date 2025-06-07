plugins {
    com.edricchan.studybuddy.library.`android-compose`
}

android {
    namespace = "com.edricchan.studybuddy.utils.coil.compose"
}

dependencies {
    api(libs.androidx.compose.ui)
    api(libs.coil.compose)
    implementation(libs.androidx.compose.material3)
}
