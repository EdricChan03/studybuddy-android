plugins {
    com.edricchan.studybuddy.library.android
}

android {
    namespace = "com.edricchan.studybuddy.utils.recyclerview"

    buildFeatures.viewBinding = true
}

dependencies {
    api(libs.androidx.recyclerview.core)
}
