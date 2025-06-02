plugins {
    com.edricchan.studybuddy.library.android
    com.edricchan.studybuddy.library.`android-hilt`
}

android {
    namespace = "com.edricchan.studybuddy.core.auth.service"

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
    implementation(projects.exts.common)
    api(projects.exts.firebase.core)

    api(projects.core.auth.credentials)
    api(projects.core.auth.model)

    api(platform(libs.firebase.bom))
    api(libs.firebase.auth.ktx)
    api(libs.playServices.auth)
}
