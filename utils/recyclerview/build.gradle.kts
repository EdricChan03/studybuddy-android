plugins {
    id("com.edricchan.studybuddy.library.android")
}

android {
    namespace = "com.edricchan.studybuddy.utils.recyclerview"
}

dependencies {
    api(libs.androidx.recyclerview.core)
}
