plugins {
    id("com.edricchan.studybuddy.library.android")
    kotlin("kapt")
}

android {
    namespace = "com.edricchan.studybuddy.core.deeplink"

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

kapt {
    arguments {
        arg("deepLink.incremental", "true")
        arg(
            "deepLink.customAnnotations",
            listOf(
                "com.edricchan.studybuddy.core.deeplink.AppDeepLink",
                "com.edricchan.studybuddy.core.deeplink.WebDeepLink"
            ).joinToString("|")
        )
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.dynamicLinks.ktx)

    implementation(libs.deepLinkDispatch.core)
    kapt(libs.deepLinkDispatch.processor)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
