plugins {
    com.edricchan.studybuddy.library.`android-protobuf`
}

android {
    namespace = "com.edricchan.studybuddy.features.tasks.domain.proto"

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
    // protoSource would include :data:common:protobuf's sources in this library, which would
    // result in a build-time error where the SortDirection enum is generated twice
    protoPath(projects.data.common.protobuf)
    api(projects.data.common.protobuf)
}
