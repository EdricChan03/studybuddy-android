plugins {
    com.edricchan.studybuddy.library.`android-protobuf`
}

android {
    namespace = "com.edricchan.studybuddy.core.settings.tasks.proto"

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
