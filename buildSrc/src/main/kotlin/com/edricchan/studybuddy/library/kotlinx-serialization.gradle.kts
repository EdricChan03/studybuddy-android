package com.edricchan.studybuddy.library

plugins {
    org.jetbrains.kotlin.plugin.serialization
}

private val libs = versionCatalogs.named("libs")

dependencies {
    "api"(libs.findLibrary("kotlinx-serialization-core").get())
}
