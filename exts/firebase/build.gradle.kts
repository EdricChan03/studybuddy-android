plugins {
    com.edricchan.studybuddy.library.android
}

android {
    namespace = "com.edricchan.studybuddy.exts.firebase"

    compileOptions.isCoreLibraryDesugaringEnabled = true
}

dependencies {
    implementation(projects.exts.datetime)

    implementation(platform(libs.firebase.bom))
    api(libs.firebase.auth.ktx)
    api(libs.firebase.firestore.ktx)


    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.playServices)

    coreLibraryDesugaring(libs.android.desugar)
}
