plugins {
    com.edricchan.studybuddy.library.`jvm-protobuf`
}

wire {
    protoLibrary = true
}

dependencies {
    // protoSource would include :data:common:protobuf's sources in this library, which would
    // result in a build-time error where the SortDirection enum is generated twice
    protoPath(projects.data.common.protobuf)
    api(projects.data.common.protobuf)
}
