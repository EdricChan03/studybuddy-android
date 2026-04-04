plugins {
    com.edricchan.studybuddy.library.android
    com.edricchan.studybuddy.library.`android-hilt`
}

android {
    namespace = "com.edricchan.studybuddy.core.settings.updates.datastore"

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

    compileOptions.isCoreLibraryDesugaringEnabled = true
}

dependencies {
    api(projects.core.settings.updates.datastoreProto)
    api(projects.core.settings.updates.model)
    api(projects.utils.wire.datastore)
    implementation(projects.core.settings.updates.resources)
    implementation(projects.exts.android.metadata)
    implementation(projects.exts.androidx.preference)

    api(libs.androidx.annotation)
    api(libs.androidx.datastore.typed)

    api(libs.kotlinx.coroutines.android)

    coreLibraryDesugaring(libs.android.desugar)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
