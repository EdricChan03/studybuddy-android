/** Convention plugin for pure-Kotlin libraries. */
package com.edricchan.studybuddy.library

plugins {
    `java-library`
    org.jetbrains.kotlin.jvm
    id("com.edricchan.studybuddy.library.common")
}

kotlin {
    jvmToolchain(17)
}
