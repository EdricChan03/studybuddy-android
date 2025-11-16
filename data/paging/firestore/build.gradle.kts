plugins {
    com.edricchan.studybuddy.library.android
}

android {
    namespace = "com.edricchan.studybuddy.data.paging.firestore"
}

dependencies {
    api(libs.androidx.paging.common)

    implementation(platform(libs.firebase.bom))
    api(libs.firebase.firestore.ktx)

    implementation(libs.androidx.core.ktx)

    api(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.playServices)
}
