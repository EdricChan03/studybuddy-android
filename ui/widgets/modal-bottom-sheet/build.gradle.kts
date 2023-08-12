plugins {
    com.edricchan.studybuddy.library.android
}

android {
    namespace = "com.edricchan.studybuddy.ui.widgets.modalbottomsheet"

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
    implementation(projects.ui.widgets.views) {
        because("NoTextCheckBox, NoTextRadioButton")
    }
    implementation(projects.core.resources)

    api(libs.androidx.fragment.ktx)
    api(libs.androidx.recyclerview.core)
    api(libs.materialComponents)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)

    androidTestImplementation(libs.kotlin.test)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
