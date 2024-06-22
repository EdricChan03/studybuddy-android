plugins {
    com.edricchan.studybuddy.library.android
}

android {
    namespace = "com.edricchan.studybuddy.exts.markwon"
}

dependencies {
    api(libs.bundles.markwon)

    // See https://github.com/noties/Markwon/issues/148#issuecomment-508003794
    configurations.configureEach {
        exclude("org.jetbrains", "annotations-java5")
    }
}
