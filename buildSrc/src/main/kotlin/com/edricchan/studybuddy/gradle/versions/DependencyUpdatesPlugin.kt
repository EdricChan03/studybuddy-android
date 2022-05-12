package com.edricchan.studybuddy.gradle.versions

import com.edricchan.studybuddy.gradle.versions.MarkdownReporter.MarkdownSection
import com.github.benmanes.gradle.versions.reporter.JsonReporter
import com.github.benmanes.gradle.versions.reporter.PlainTextReporter
import com.github.benmanes.gradle.versions.reporter.Reporter
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
                @Suppress("LongLine")
                closureOf<com.github.benmanes.gradle.versions.reporter.result.Result> {
                    // (Logic from https://github.com/ben-manes/gradle-versions-plugin/blob/fba92314f34b24bf93fc3fb4f2243e2e887ed12f/src/main/groovy/com/github/benmanes/gradle/versions/updates/DependencyUpdatesReporter.groovy#L114-L124)

                    // Ensure that the output directory already exists
                    target.file(outputDir).mkdirs()

                    val reporters = mapOf(
                        // Generate console output
                        null to getReporter {
                            PlainTextReporter(
                                it.project,
                                it.revision,
                                it.gradleReleaseChannel
                            )
                        },
                        // Generate JSON file
                        project.file(File(outputDir, "$reportfileName.json")) to getReporter {
                            JsonReporter(
                                it.project,
                                it.revision,
                                it.gradleReleaseChannel
                            )
                        },
                        // Generate Markdown file
                        project.file(File(outputDir, "$reportfileName.md"))
                            to getReporter {
                            MarkdownReporter(
                                it,
                                MarkdownReporter.MarkdownReporterOptions(
                                    useSimpleDependencyNotation = false,
                                    sections = listOf(
                                        MarkdownSection.DEPENDENCIES_OUTDATED,
                                        MarkdownSection.DEPENDENCIES_UNRESOLVED,
                                        MarkdownSection.DEPENDENCIES_UNDECLARED,
                                        MarkdownSection.DEPENDENCIES_EXCEEDING,
                                        MarkdownSection.GRADLE,
                                        MarkdownSection.DEPENDENCIES_UP_TO_DATE,
                                    )
                                )
                            )
                        }
                    )

                    reporters.forEach { (output, reporter) ->
                        if (output == null)
                            project.logger.debug(
                                "\nNo output file was specified for " +
                                    "the reporter ${reporter.javaClass.simpleName}. " +
                                    "Assuming write to standard output"
                            )
                        // Note: The PrintWriter must be closed to persist writes,
                        // otherwise it doesn't write
                        output?.printWriter().use {
                            reporter.write(it ?: System.out, this@closureOf)
                        }
                        if (output != null) project.logger.lifecycle("\nGenerated report file $output")
                    }
                }
        }
    }

    private fun DependencyUpdatesTask.getReporter(
        reporterFn: (DependencyUpdatesTask) -> Reporter
    ) =
        reporterFn(this)
}
