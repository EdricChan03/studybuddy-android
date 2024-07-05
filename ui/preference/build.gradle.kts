plugins {
    com.edricchan.studybuddy.library.`android-compose`
    `kotlin-parcelize`
}

android {
    namespace = "com.edricchan.studybuddy.ui.preference"

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

    buildFeatures.viewBinding = true
}

dependencies {
    api(projects.ui.preference.compose)
    implementation(projects.ui.theming.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.materialComponents)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
