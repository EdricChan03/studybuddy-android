plugins {
    com.edricchan.studybuddy.library.android
    com.edricchan.studybuddy.library.`kotlinx-serialization`
}

android {
    namespace = "com.edricchan.studybuddy.data.common"
}

dependencies {
    api(projects.data.common.protobuf)

    api(libs.androidx.annotation)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore.ktx)

    implementation(libs.androidx.core.ktx)

    api(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.playServices)
}
