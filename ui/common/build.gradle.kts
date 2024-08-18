plugins {
    com.edricchan.studybuddy.library.`android-compose`
    com.edricchan.studybuddy.library.`android-hilt`
}

android {
    namespace = "com.edricchan.studybuddy.ui.common"

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
    implementation(projects.ui.theming.compose)
    implementation(projects.ui.theming.views)
    implementation(projects.ui.widgets.compose)

    api(libs.androidx.fragment.ktx)
    api(libs.androidx.fragment.compose)
    api(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.materialComponents)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.activity.ktx)
    api(libs.androidx.compose.material3)

    implementation(libs.aboutLibraries)
    implementation(libs.aboutLibraries.compose.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
