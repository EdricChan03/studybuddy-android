package com.edricchan.studybuddy.gradle.versions

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

class DependencyUpdatesPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        // Add the Markdown reporter
        target.tasks.withType<DependencyUpdatesTask> {
            outputFormatter = MarkdownReporter.fromTask(
                this,
                MarkdownReporter.MarkdownReporterOptions(useSimpleDependencyNotation = false)
            )
        }
    }
}
