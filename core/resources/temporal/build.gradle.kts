plugins {
    com.edricchan.studybuddy.library.`android-compose`
}

android {
    namespace = "com.edricchan.studybuddy.core.resources.temporal"

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
    api(projects.exts.datetime)
    api(projects.core.resources)
    implementation(projects.exts.android)
    implementation(projects.utils.android)

    api(libs.androidx.compose.runtime)

    implementation(libs.androidx.core.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
