plugins {
    com.edricchan.studybuddy.library.`jvm-protobuf`
}

dependencies {
    // protoSource would include :features:tasks:domain-proto's sources in this library, which would
    // result in a build-time error where the SortDirection enum is generated twice
    protoPath(projects.features.tasks.domainProto)
    api(projects.features.tasks.domainProto)
}
