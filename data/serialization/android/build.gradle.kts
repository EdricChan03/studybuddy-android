plugins {
    com.edricchan.studybuddy.library.android
    com.edricchan.studybuddy.library.`kotlinx-serialization`
}

android {
    namespace = "com.edricchan.studybuddy.data.serialization"
}

dependencies {
    implementation(libs.androidx.core.ktx)
}
