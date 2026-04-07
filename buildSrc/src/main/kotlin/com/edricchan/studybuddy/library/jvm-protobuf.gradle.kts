/** Base convention script for modules that support ProtoBuf. */
package com.edricchan.studybuddy.library

plugins {
    `java-library`
    org.jetbrains.kotlin.jvm
    com.squareup.wire
}

private val libs = versionCatalogs.named("libs")

dependencies {
    "api"(libs.findLibrary("wire-runtime").get())
}

wire {
    kotlin {}
}
