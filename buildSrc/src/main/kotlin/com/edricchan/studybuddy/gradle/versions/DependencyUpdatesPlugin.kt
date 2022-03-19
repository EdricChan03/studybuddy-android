package com.edricchan.studybuddy.gradle.versions

import com.github.benmanes.gradle.versions.reporter.JsonReporter
import com.github.benmanes.gradle.versions.reporter.PlainTextReporter
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.withType
import java.io.File

class DependencyUpdatesPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        // Add the Markdown reporter
        target.tasks.withType<DependencyUpdatesTask> {
            outputFormatter =
                closureOf<com.github.benmanes.gradle.versions.reporter.result.Result> {
                    // Generate plain-text
                    PlainTextReporter(project, revision, gradleReleaseChannel).write(
                        System.out,
                        this
                    )

                    // Generate JSON (used to show a brief summary of results)
                    JsonReporter(project, revision, gradleReleaseChannel).write(
                        File(
                            outputDir,
                            "$reportfileName.json"
                        ).printWriter(), this
                    )

                    // Generate custom Markdown (for the details in the GitHub Check description)
                    MarkdownReporter.fromTask(
                        this@withType,
                        MarkdownReporter.MarkdownReporterOptions(useSimpleDependencyNotation = false)
                    ).write(File(outputDir, "$reportfileName.md").printWriter(), this)
                }
        }
    }
}
