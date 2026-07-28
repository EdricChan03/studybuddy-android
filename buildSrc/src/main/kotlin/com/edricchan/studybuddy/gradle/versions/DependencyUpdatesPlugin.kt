package com.edricchan.studybuddy.gradle.versions

import com.edricchan.studybuddy.gradle.versions.MarkdownReporter.MarkdownSection
import com.github.benmanes.gradle.versions.reporter.JsonReporter
import com.github.benmanes.gradle.versions.reporter.PlainTextReporter
import com.github.benmanes.gradle.versions.reporter.Reporter
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import java.io.File
import java.io.PrintStream

class DependencyUpdatesPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val projectPath = target.path
        val projectName = target.name
        val layout = target.layout

        // Add the Markdown reporter
        target.tasks.withType<DependencyUpdatesTask>().configureEach {
            outputFormatter {
                // (Logic from https://github.com/ben-manes/gradle-versions-plugin/blob/fba92314f34b24bf93fc3fb4f2243e2e887ed12f/src/main/groovy/com/github/benmanes/gradle/versions/updates/DependencyUpdatesReporter.groovy#L114-L124)

                // Ensure that the output directory already exists
                File(outputDir).mkdirs()

                val reporters = mapOf(
                    // Generate console output
                    null to getReporter {
                        PlainTextReporter(
                            projectPath,
                            it.revision,
                            it.gradleReleaseChannel
                        )
                    },
                    // Generate JSON file
                    layout.projectDirectory.file(
                        File(
                            outputDir,
                            "$reportfileName.json"
                        ).path
                    ) to getReporter {
                        JsonReporter(
                            projectPath,
                            it.revision,
                            it.gradleReleaseChannel
                        )
                    },
                    // Generate Markdown file
                    layout.projectDirectory.file(File(outputDir, "$reportfileName.md").path)
                        to getReporter {
                        MarkdownReporter(
                            projectName = projectName,
                            projectPath = projectPath,
                            revision = it.revision,
                            gradleReleaseChannel = it.gradleReleaseChannel,
                            options = MarkdownReporter.MarkdownReporterOptions(
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
                    if (output == null) logger.debug(
                        "\nNo output file was specified for " +
                            "the reporter ${reporter.javaClass.simpleName}. " +
                            "Assuming write to standard output"
                    )
                    // Note: The PrintWriter must be closed to persist writes,
                    // otherwise it doesn't write
                    output?.asFile?.let { PrintStream(it) }.use {
                        reporter.write(it ?: System.out, this)
                    }
                    if (output != null) logger.lifecycle("\nGenerated report file $output")
                }
            }
        }
    }

    private fun DependencyUpdatesTask.getReporter(
        reporterFn: (DependencyUpdatesTask) -> Reporter
    ) = reporterFn(this)
}
