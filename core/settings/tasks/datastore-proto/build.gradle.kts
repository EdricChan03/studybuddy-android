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

dependencies {
    // protoSource would include :features:tasks:domain-proto's sources in this library, which would
    // result in a build-time error where the SortDirection enum is generated twice
    protoPath(projects.features.tasks.domainProto)
    api(projects.features.tasks.domainProto)
}
