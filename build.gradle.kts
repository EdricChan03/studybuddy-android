// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        jcenter()
        google()
    }
    dependencies {
        classpath(deps.android.gradlePlugin)
        classpath(deps.firebase.gradlePlugin)
        classpath(deps.firebase.crashlyticsPlugin)
        // Plugin to show all licenses
        classpath(deps.licenses.gradlePlugin)
        classpath(deps.kotlin.gradlePlugin)
        // Performance Monitoring plugin
        classpath(deps.firebase.perfPlugin)

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
