import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    // Used for the custom reporter
    implementation(libs.gradleVersions.gradle)
    implementation(libs.android.gradle.library)
    // See https://github.com/google/dagger/issues/3068
    implementation("com.squareup:javapoet:1.13.0") {
        because("AGP DataBinding uses an older version of Javapoet which breaks Hilt")
    }
    implementation(libs.kotlin.gradle)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs += listOf(
            "-opt-in=kotlin.RequiresOptIn"
        )
    }
}
