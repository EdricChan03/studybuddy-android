/**
 * Convention plugin which adds the minimum Hilt dependencies required for a
 * module that uses Hilt.
 *
 * Note: This plugin does **not** set the relevant Android defaults - add the relevant
 * `android`/`android-compose` convention plugins as needed.
 */
package com.edricchan.studybuddy.library

plugins {
    com.android.library // Needed such that dependency configurations are resolved correctly
    com.google.devtools.ksp
    com.google.dagger.hilt.android
}

private val libs = versionCatalogs.named("libs")

dependencies {
    api(libs.findLibrary("dagger-hilt-android").get())
    ksp(libs.findLibrary("dagger-hilt-compiler").get())
}
