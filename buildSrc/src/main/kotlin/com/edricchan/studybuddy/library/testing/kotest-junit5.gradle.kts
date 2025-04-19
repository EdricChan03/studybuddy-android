/** Convention script to add Kotest modules, and enable JUnit5. */
package com.edricchan.studybuddy.library.testing

import com.android.build.gradle.tasks.factory.AndroidUnitTest

private val libs = versionCatalogs.named("libs")

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.withType<AndroidUnitTest>().configureEach {
    useJUnitPlatform()
}

dependencies {
    "testImplementation"(libs.findLibrary("kotest-runner-junit5").get())
    "testImplementation"(libs.findLibrary("kotest-assertion-core").get())
    "testImplementation"(libs.findLibrary("kotest-property").get())
}
