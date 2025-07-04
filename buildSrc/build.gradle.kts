plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

private fun Provider<PluginDependency>.text() =
    map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" }

dependencies {
    // Used for the custom reporter
    implementation(libs.gradleVersions.gradle)
    implementation(libs.android.gradle.library)
    // See https://github.com/google/dagger/issues/3068
    implementation("com.squareup:javapoet:1.13.0") {
        because("AGP DataBinding uses an older version of Javapoet which breaks Hilt")
    }
    // See https://github.com/autonomousapps/dependency-analysis-gradle-plugin/issues/1240
    implementation("com.google.guava:guava:33.4.8-jre") {
        because(
            "Guava 33+ uses an ImmutableSet for graphs, required " +
                "for Dependency Analysis 1.33.0 (see " +
                "https://github.com/autonomousapps/dependency-analysis-gradle-plugin/issues/1240)"
        )
    }
    implementation(libs.kotlin.gradle)
    implementation(libs.kotlin.composeCompiler.gradle)
    implementation(libs.plugins.protobuf.text())
    implementation(libs.plugins.ksp.text())
    implementation(libs.plugins.dagger.hilt.text())
}

gradlePlugin {
    plugins {
        val dependencyUpdatePlugin by registering {
            id = "com.edricchan.studybuddy.dependency-updates"
            implementationClass = "com.edricchan.studybuddy.gradle.versions.DependencyUpdatesPlugin"
            displayName = "Dependency Updates plugin"
            description =
                "Adds additional support for reporting updated dependencies as a Markdown file"
        }
    }
}
