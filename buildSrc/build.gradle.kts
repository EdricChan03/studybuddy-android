plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
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

gradlePlugin {
    plugins {
        val dependencyUpdatePlugin by creating {
            id = "com.edricchan.studybuddy.dependency-updates"
            implementationClass = "com.edricchan.studybuddy.gradle.versions.DependencyUpdatesPlugin"
            displayName = "Dependency Updates plugin"
            description =
                "Adds additional support for reporting updated dependencies as a Markdown file"
        }
    }
}
