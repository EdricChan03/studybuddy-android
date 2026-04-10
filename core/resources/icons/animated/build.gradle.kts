plugins {
    com.edricchan.studybuddy.library.`android-compose`
}

android {
    namespace = "com.edricchan.studybuddy.core.resources.icons.animated"

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
    api(projects.core.resources.icons.common)

    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.animation.graphics)
    implementation(libs.materialComponents) {
        because("To resolve MDC attributes in XML resources")
    }

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
