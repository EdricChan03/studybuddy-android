plugins {
    com.edricchan.studybuddy.library.android
    id("com.google.devtools.ksp")
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

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.dynamicLinks.ktx)

    api(libs.deepLinkDispatch.core)
    ksp(libs.deepLinkDispatch.processor)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}

ksp {
    arg("deepLink.incremental", "true")
    arg(
        "deepLink.customAnnotations",
        listOf(
            "com.edricchan.studybuddy.core.deeplink.AppDeepLink",
            "com.edricchan.studybuddy.core.deeplink.WebDeepLink"
        ).joinToString("|")
    )
}
