plugins {
    com.edricchan.studybuddy.library.android
}

android {
    namespace = "com.edricchan.studybuddy.data.common"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore.ktx)

    api(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.playServices)
}
