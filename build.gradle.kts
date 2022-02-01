// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        jcenter()
        google()
    }
    dependencies {
        classpath(libs.android.gradle)
        classpath(libs.kotlin.gradle)
        classpath(libs.googleServices.gradle)
        classpath(libs.firebase.crashlytics.gradle)
        classpath(libs.firebase.perf.gradle)
        classpath(libs.playServices.ossLicenses.gradle)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        jcenter()
        maven(deps.build.repositories.jitpack)
        mavenCentral()
        google()
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(rootProject.buildDir)
    }
}
