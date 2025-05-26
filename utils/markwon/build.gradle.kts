plugins {
    com.edricchan.studybuddy.library.android
}

android {
    namespace = "com.edricchan.studybuddy.utils.markwon"
}

dependencies {
    api(libs.coil)
    api(libs.markwon.core)

    implementation(libs.androidx.core.ktx)

    // See https://github.com/noties/Markwon/issues/148#issuecomment-508003794
    configurations.configureEach {
        exclude("org.jetbrains", "annotations-java5")
    }
}
