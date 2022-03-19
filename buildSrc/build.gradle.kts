plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    // Used for the custom reporter
    implementation(libs.gradleVersions.gradle)
}
