plugins {
    com.edricchan.studybuddy.library.android
    com.edricchan.studybuddy.library.`android-hilt`
}

android {
    namespace = "com.edricchan.studybuddy.core.settings.general.datastore"

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
    api(projects.core.settings.general.datastoreProto)
    api(projects.core.settings.general.model)
    api(projects.utils.wire.datastore)
    implementation(projects.exts.androidx.preference)

    api(libs.androidx.datastore.typed)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
