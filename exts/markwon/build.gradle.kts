plugins {
    com.edricchan.studybuddy.library.android
}

android {
    namespace = "com.edricchan.studybuddy.exts.markwon"
}

dependencies {
    implementation(libs.bundles.markwon)

    // See https://github.com/noties/Markwon/issues/148#issuecomment-508003794
    configurations.all {
        exclude("org.jetbrains", "annotations-java5")
    }
}