plugins {
    id("com.edricchan.studybuddy.library.android")
    kotlin("plugin.serialization")
}

android {
    namespace = "com.edricchan.studybuddy.data.serialization"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.core)
}
