plugins {
    com.edricchan.studybuddy.library.android
}

android {
    namespace = "com.edricchan.studybuddy.data.common"
}

dependencies {
    api(libs.androidx.annotation)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore.ktx)

    implementation(libs.androidx.core.ktx)

    api(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.playServices)
}
