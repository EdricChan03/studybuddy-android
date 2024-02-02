plugins {
    com.edricchan.studybuddy.library.kotlin
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
