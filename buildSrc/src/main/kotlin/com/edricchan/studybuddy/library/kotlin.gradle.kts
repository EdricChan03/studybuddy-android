/** Convention plugin for pure-Kotlin libraries. */
package com.edricchan.studybuddy.library

plugins {
    `java-library`
    org.jetbrains.kotlin.jvm
}

kotlin {
    jvmToolchain(17)
}
