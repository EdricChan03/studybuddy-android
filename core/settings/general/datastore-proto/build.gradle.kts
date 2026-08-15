plugins {
    com.edricchan.studybuddy.library.`jvm-protobuf`
}

dependencies {
    // protoSource would include :data:common:protobuf's sources in this library, which
    // would result in duplicate code
    protoPath(projects.data.common.protobuf)
    api(projects.data.common.protobuf)
}
