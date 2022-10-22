package com.edricchan.studybuddy.gradle.versions

import com.edricchan.studybuddy.gradle.versions.reporter.InternalMarkdownApi
import com.edricchan.studybuddy.gradle.versions.reporter.writeExceedLatestFound
import com.edricchan.studybuddy.gradle.versions.reporter.writeGradleUpdates
import com.edricchan.studybuddy.gradle.versions.reporter.writeHeader
import com.edricchan.studybuddy.gradle.versions.reporter.writeOutdated
import com.edricchan.studybuddy.gradle.versions.reporter.writeUndeclared
import com.edricchan.studybuddy.gradle.versions.reporter.writeUnresolved
import com.edricchan.studybuddy.gradle.versions.reporter.writeUpToDate
import com.github.benmanes.gradle.versions.reporter.AbstractReporter
import com.github.benmanes.gradle.versions.reporter.result.Result
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.io.OutputStream
import java.io.PrintStream

// Matches that of AbstractReporter
@Suppress("HardCodedStringLiteral", "LongLine")
@OptIn(InternalMarkdownApi::class)
class MarkdownReporter(
    task: DependencyUpdatesTask,
    private val options: MarkdownReporterOptions = MarkdownReporterOptions()
) : AbstractReporter(task.project, task.revision, task.gradleReleaseChannel) {
    data class MarkdownReporterOptions(
        /**
         * Whether to use [simple dependency notation](https://docs.gradle.org/current/dsl/org.gradle.api.artifacts.dsl.DependencyHandler.html#N16F60).
         * By default, this is **`true`**.
         *
         * Enabling this will reduce the amount of columns outputted for the dependency
         * from 3 to 1. See the example below:
         *
         * **Before:**
         *
         * Group | Name | Version | Project URL | User Reason
         * ---|---|---|---|---
         * `com.example` | `example-name` | `1.0.0` | [Link](https://example.com) | Reason goes here
         *
         * **After:**
         *
         * Dependency | Project URL | User Reason
         * ---|---|---
         * `com.example:example-name:1.0.0` | [Link](https://example.com) | Reason goes here
         */
        val useSimpleDependencyNotation: Boolean = true,
        /** Whether to write the top-level header. */
        val shouldWriteHeader: Boolean = true,
        /**
         * Specifies what sections (as well as the order to output them) are outputted.
         *
         * Note: No checks are done to see if there are multiple of the same sections.
         */
        val sections: List<MarkdownSection> = DEFAULT_SECTIONS
    ) {
        companion object {
            /** The default order of the Markdown sections. */
            val DEFAULT_SECTIONS = MarkdownSection.values().toList()
        }
    }

    /** Defines the list of Markdown sections to output. */
    enum class MarkdownSection {
        /** Specifies the section for Gradle release channel information. */
        GRADLE {
            override fun write(
                reporter: MarkdownReporter,
                out: PrintStream,
                result: Result
            ) {
                out.writeGradleUpdates(reporter.gradleReleaseChannel, result)
            }
        },

        /** Specifies the section for up-to-date dependencies. */
        DEPENDENCIES_UP_TO_DATE {
            override fun write(
                reporter: MarkdownReporter,
                out: PrintStream,
                result: Result
            ) {
                out.writeUpToDate(
                    reporter.revision,
                    reporter.options.useSimpleDependencyNotation,
                    result
                )
            }
        },

        /** Specifies the section for dependencies that are exceeding the specified version. */
        DEPENDENCIES_EXCEEDING {
            override fun write(
                reporter: MarkdownReporter,
                out: PrintStream,
                result: Result
            ) {
                out.writeExceedLatestFound(
                    reporter.revision,
                    reporter.options.useSimpleDependencyNotation,
                    result
                )
            }
        },

        /** Specifies the section for dependencies that are outdated. */
        DEPENDENCIES_OUTDATED {
            override fun write(
                reporter: MarkdownReporter,
                out: PrintStream,
                result: Result
            ) {
                out.writeOutdated(
                    reporter.revision,
                    reporter.options.useSimpleDependencyNotation,
                    result
                )
            }
        },

        /** Specifies the section for dependencies that are not declared. */
        DEPENDENCIES_UNDECLARED {
            override fun write(
                reporter: MarkdownReporter,
                out: PrintStream,
                result: Result
            ) {
                out.writeUndeclared(reporter.options.useSimpleDependencyNotation, result)
            }
        },

        /** Specifies the section for dependencies that were unable to be resolved. */
        DEPENDENCIES_UNRESOLVED {
            override fun write(
                reporter: MarkdownReporter,
                out: PrintStream,
                result: Result
            ) {
                out.writeUnresolved(reporter.options.useSimpleDependencyNotation, result)
            }
        };

        abstract fun write(reporter: MarkdownReporter, out: PrintStream, result: Result)
    }

    override fun write(printStream: OutputStream, result: Result) {
        PrintStream(printStream).apply {
            if (options.shouldWriteHeader) writeHeader(project.name)
            if (result.count == 0) this.println("No dependencies found.")
            else options.sections.forEach { it.write(this@MarkdownReporter, this, result) }
        }
    }

    override fun getFileExtension() = "md"
}
