// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    rootProject.extra["kotlin_version"] = "1.3.72"
    repositories {
        jcenter()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.0-alpha08")
        classpath("com.google.gms:google-services:4.3.3")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.0.0")
        // Plugin to show all licenses
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${rootProject.extra["kotlin_version"]}")
        // Performance Monitoring plugin
        classpath("com.google.firebase:perf-plugin:1.3.1")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        jcenter()
        maven("https://jitpack.io")
        mavenCentral()
        google()
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(rootProject.buildDir)
    }
}
