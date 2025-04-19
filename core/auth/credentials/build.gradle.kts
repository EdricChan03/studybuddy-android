plugins {
    com.edricchan.studybuddy.library.android
}

android {
    namespace = "com.edricchan.studybuddy.core.auth.credentials"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    api(projects.core.auth.gms)
    implementation(projects.exts.common)
    implementation(projects.exts.firebase.core)

    api(libs.playServices.auth)
    api(libs.androidx.credentials)
    api(libs.androidx.credentials.playServices)
    api(libs.google.identity.googleId)

    api(platform(libs.firebase.bom))
    api(libs.firebase.auth.ktx)
}
