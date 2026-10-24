plugins {
    com.edricchan.studybuddy.library.android
    com.edricchan.studybuddy.library.`android-hilt`
}

android {
    namespace = "com.edricchan.studybuddy.core.di.testing"

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
    implementation(projects.ui.theming.views)

    api(libs.androidx.appcompat)
    api(libs.androidx.fragment.ktx)

    api(libs.androidx.test.runner)
    api(libs.dagger.hilt.android.testing)
    implementation(libs.androidx.test.core.ktx)
}
