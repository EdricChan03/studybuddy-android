package com.edricchan.studybuddy.library.compose

import com.android.build.api.dsl.LibraryExtension
import com.android.compose.screenshot.PreviewScreenshotGradlePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

// Note: This is written as a binary plugin rather than a precompiled script
// as the screenshot testing plugin requires setting the
// android.experimental.enableScreenshotTest property which has no effect somehow when setting
// it in buildSrc/gradle.properties... :\
// TODO: Investigate if this binary plugin can be refactored back into a precompiled script
class AndroidScreenshotTestingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins()
            enableScreenshotTestSourceSet(extensions.getByType())
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            addScreenshotDependencies(libs)
        }
    }

    private fun Project.applyPlugins() {
        plugins.apply(PreviewScreenshotGradlePlugin::class)
    }

    private fun Project.addScreenshotDependencies(
        libs: VersionCatalog
    ) {
        dependencies {
            "screenshotTestImplementation"(
                libs.findLibrary("android-screenshot-validation-api").get()
            )
            "screenshotTestImplementation"(
                libs.findLibrary("androidx-compose-ui-tooling").get()
            )
        }
    }

    private fun Project.enableScreenshotTestSourceSet(
        android: LibraryExtension
    ) {
        android.experimentalProperties["android.experimental.enableScreenshotTest"] = true
    }
}
