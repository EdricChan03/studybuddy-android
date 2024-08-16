plugins {
    com.edricchan.studybuddy.library.android
}

android {
    namespace = "com.edricchan.studybuddy.utils.dev"

    buildFeatures.buildConfig = true
}

dependencies {
    implementation(libs.androidx.core.ktx)
}
