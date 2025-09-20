plugins {
    com.edricchan.studybuddy.library.`android-compose`
}

android {
    namespace = "com.edricchan.studybuddy.data.common.compose"
}

dependencies {
    api(projects.data.common)

    api(libs.androidx.annotation)
    api(libs.androidx.compose.foundation)

    implementation(libs.androidx.core.ktx)

    api(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.playServices)
}
