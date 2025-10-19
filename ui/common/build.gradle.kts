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
    implementation(projects.core.compat.navigation)
    implementation(projects.core.resources)
    implementation(projects.core.resources.icons)
    implementation(projects.exts.androidx.compose)
    implementation(projects.exts.androidx.viewBinding)
    implementation(projects.exts.material)
    implementation(projects.ui.theming.compose)
    implementation(projects.ui.theming.views)
    implementation(projects.ui.widgets.compose)
    implementation(projects.ui.widgets.compose)
    implementation(projects.utils.web)

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

    api(libs.aboutLibraries.core)
    api(libs.aboutLibraries.compose.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
