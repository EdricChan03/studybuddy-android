package com.edricchan.studybuddy.gradle.versions

import com.github.benmanes.gradle.versions.reporter.AbstractReporter
import com.github.benmanes.gradle.versions.reporter.result.*
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.io.PrintWriter
import java.util.*

// Matches that of AbstractReporter
@Suppress("HardCodedStringLiteral", "LongLine")
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
            override fun write(reporter: MarkdownReporter, writer: PrintWriter, result: Result) {
                reporter.writeGradleUpdates(writer, result)
            }
        },

        /** Specifies the section for up-to-date dependencies. */
        DEPENDENCIES_UP_TO_DATE {
            override fun write(reporter: MarkdownReporter, writer: PrintWriter, result: Result) {
                reporter.writeUpToDate(writer, result)
            }
        },

        /** Specifies the section for dependencies that are exceeding the specified version. */
        DEPENDENCIES_EXCEEDING {
            override fun write(reporter: MarkdownReporter, writer: PrintWriter, result: Result) {
                reporter.writeExceedLatestFound(writer, result)
            }
        },

        /** Specifies the section for dependencies that are outdated. */
        DEPENDENCIES_OUTDATED {
            override fun write(reporter: MarkdownReporter, writer: PrintWriter, result: Result) {
                reporter.writeOutdated(writer, result)
            }
        },

        /** Specifies the section for dependencies that are not declared. */
        DEPENDENCIES_UNDECLARED {
            override fun write(reporter: MarkdownReporter, writer: PrintWriter, result: Result) {
                reporter.writeUndeclared(writer, result)
            }
        },

        /** Specifies the section for dependencies that were unable to be resolved. */
        DEPENDENCIES_UNRESOLVED {
            override fun write(reporter: MarkdownReporter, writer: PrintWriter, result: Result) {
                reporter.writeUnresolved(writer, result)
            }
        };

        abstract fun write(reporter: MarkdownReporter, writer: PrintWriter, result: Result)
    }

    override fun write(target: Any?, result: Result) {
        // Assume that the target is a PrintWriter
        val out = target as? PrintWriter

        out?.apply {
            if (options.shouldWriteHeader) writeHeader()
            if (result.count == 0) println("No dependencies found.")
            else options.sections.forEach { it.write(this@MarkdownReporter, this, result) }
        }
    }

    override fun getFileExtension() = "md"

    private fun PrintWriter.writeHeader() {
        println("${project.name} Dependency Updates".header(1))
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun PrintWriter.writeGradleUpdates(result: Result) {
        if (!result.gradle.isEnabled) return

        println("Gradle $gradleReleaseChannel updates".header(2))
        with(result.gradle) {
            val list = mutableListOf("Current version of Gradle used: ${running.version}")
            if (current.isFailure) list += "$crossSymbol Could not retrieve Gradle update: ${current.reason}".bold
            if (current.isUpdateAvailable && current > running) list += "An update is available! ${current.version}"
            println(list.unorderedList)

            println("Available Gradle versions".header(3, newPrevLine = true))
            val gradleVersionsTable = buildMap {
                this += "Current" to current
                this += "Nightly" to nightly
                this += "Release candidate" to releaseCandidate
            }

            // GradleUpdateResult:
            // version, isUpdateAvailable, isFailure, reason
            //language=HTML
            println(
                """
                <table>
                    <thead>
                        <th scope="col">Release Channel</th>
                        <th scope="col">Update available?</th>
                        <th scope="col">Is failed?</th>
                        <th scope="col">Failure Reason</th>
                    </thead>
                    <tbody>
                        ${
                    gradleVersionsTable.entries.joinToString("\n") {
                        """
                                <tr>
                                    <th scope="row">${it.key}</th>
                                    <td>${it.value.isUpdateAvailable.verbose}</td>
                                    <td>${it.value.isFailure.verbose}</td>
                                    <td>${it.value.reason.ifBlank { "(Not specified)" }}</td>
                                </tr>
                            """.trimIndent()
                    }
                }
                    </tbody>
                </table>
            """.replace(">\\s+<".toRegex(), "><").trim()
            )
        }
        println()
    }

    internal fun writeGradleUpdates(writer: PrintWriter, result: Result) {
        writer.writeGradleUpdates(result)
    }

    private fun PrintWriter.writeUpToDate(result: Result) {
        val versions = result.current.dependencies
        println("Up to date dependencies".header(2))
        if (versions.isNotEmpty()) {
            println("The following dependencies are using the latest $revision version:")
            println(versions.table)
        } else println("(No up to date dependencies were found)".italics)
        println()
    }

    internal fun writeUpToDate(writer: PrintWriter, result: Result) {
        writer.writeUpToDate(result)
    }

    private fun PrintWriter.writeExceedLatestFound(result: Result) {
        val versions = result.exceeded.dependencies
        println("Exceeded dependencies".header(2))
        if (versions.isNotEmpty()) {
            println("The following dependencies exceed the version found at the $revision revision level:")
            println(versions.table)
        } else println("(No dependencies were found that exceeded the latest version)".italics)
        println()
    }

    internal fun writeExceedLatestFound(writer: PrintWriter, result: Result) {
        writer.writeExceedLatestFound(result)
    }

    private fun PrintWriter.writeOutdated(result: Result) {
        val versions = result.outdated.dependencies
        println("Outdated dependencies".header(2))
        if (versions.isNotEmpty()) {
            println("The following dependencies have later $revision versions:")
            println(versions.table)
        } else println("(No outdated dependencies were found)".italics)
        println()
    }

    internal fun writeOutdated(writer: PrintWriter, result: Result) {
        writer.writeOutdated(result)
    }

    private fun PrintWriter.writeUndeclared(result: Result) {
        val versions = result.undeclared.dependencies
        println("Undeclared dependencies".header(2))
        if (versions.isNotEmpty()) {
            println("The following dependencies could not be compared as they were declared without a version:")
            println(versions.table)
        } else println("(No undeclared dependencies were found)".italics)
        println()
    }

    internal fun writeUndeclared(writer: PrintWriter, result: Result) {
        writer.writeUndeclared(result)
    }

    private fun PrintWriter.writeUnresolved(result: Result) {
        val versions = result.unresolved.dependencies
        println("Unresolved dependencies".header(2))
        if (versions.isNotEmpty()) {
            println("The following dependencies could not be resolved:")
            println(versions.table)
        } else println("(No unresolved dependencies were found)".italics)
        println()
    }

    internal fun writeUnresolved(writer: PrintWriter, result: Result) {
        writer.writeUnresolved(result)
    }

    // Markdown utilities

    private fun String.header(
        level: Int,
        newNextLine: Boolean = true,
        newPrevLine: Boolean = false
    ): String {
        require(level in (1..6)) { "Markdown only supports heading levels 1 - 6, but $level was specified" }
        return "${if (newPrevLine) "\n" else ""}${"#".repeat(level)} $this${if (newNextLine) "\n" else ""}"
    }

    private val String.bold: String
        get() = "**$this**"

    private val String.italics: String
        get() = "*$this*"

    private val String.code: String
        get() = "`$this`"

    private val List<String>.unorderedList: String
        get() = joinToString("\n") { "* $it" }

    private val List<List<String>>.table: String
        get() {
            // Table should at least have a heading row
            require(size > 1)

            return foldIndexed("") { index, acc, list ->
                var output = list.joinToString(" | ")
                // Separator between the heading and the rest of the table
                if (index == 0) output += "\n${"---|".repeat(list.size).dropLast(1)}"
                "$acc\n$output"
            }
        }

    @Suppress("RemoveExplicitTypeArguments")
    @OptIn(ExperimentalStdlibApi::class)
    private val SortedSet<out Dependency>.table: String
        get() = buildList<List<String>> {
            val getProjectUrl =
                { url: String? -> if (url != null) "Link".link(url) else "(No project URL specified)" }

            if (this@table.all { it is DependencyLatest }) {
                // DependencyLatest contains the following:
                // group, name, version, projectUrl, userReason, latest
                if (options.useSimpleDependencyNotation) {
                    this += listOf(
                        "Dependency (current)",
                        "Dependency (latest)",
                        "Project URL",
                        "User Reason"
                    )

                    this@table.forEach {
                        this += listOf(
                            it.simple.code,
                            (it as DependencyLatest).simpleLatest.code,
                            getProjectUrl(it.projectUrl),
                            it.userReason ?: "(No reason specified)"
                        )
                    }
                } else {
                    this += listOf(
                        "Group",
                        "Name",
                        "Version",
                        "Latest",
                        "Project URL",
                        "User Reason"
                    )

                    this@table.forEach {
                        this += listOf(
                            it.group.code,
                            it.name.code,
                            it.version.code,
                            (it as DependencyLatest).latest,
                            getProjectUrl(it.projectUrl),
                            it.userReason ?: "(No reason specified)"
                        )
                    }
                }
            } else if (this@table.all { it is DependencyOutdated }) {
                // DependencyOutdated contains the following:
                // group, name, version, projectUrl, userReason, available
                if (options.useSimpleDependencyNotation) {
                    this += listOf(
                        "Dependency (current)",
                        "Dependency (release)",
                        "Dependency (milestone)",
                        "Dependency (integration)",
                        "Project URL",
                        "User Reason"
                    )

                    this@table.forEach {
                        val dep = it as DependencyOutdated
                        this += listOf(
                            dep.simple.code,
                            dep.getSimpleLatest("release")?.code
                                ?: "(No release version available)",
                            dep.getSimpleLatest("milestone")?.code
                                ?: "(No milestone version available)",
                            dep.getSimpleLatest("integration")?.code
                                ?: "(No integration version available)",
                            getProjectUrl(dep.projectUrl),
                            dep.userReason ?: "(No reason specified)"
                        )
                    }
                } else {
                    this += listOf(
                        "Group",
                        "Name",
                        "Version",
                        "Available (release)",
                        "Available (milestone)",
                        "Available (integration)",
                        "Project URL",
                        "User Reason"
                    )

                    this@table.forEach {
                        val dep = it as DependencyOutdated
                        this += listOf(
                            dep.group.code,
                            dep.name.code,
                            dep.version.code,
                            dep.available?.release?.code ?: "(No release version available)",
                            dep.available?.milestone?.code ?: "(No milestone version available)",
                            dep.available?.integration?.code
                                ?: "(No integration version available)",
                            getProjectUrl(dep.projectUrl),
                            dep.userReason ?: "(No reason specified)"
                        )
                    }
                }
            } else if (this@table.all { it is DependencyUnresolved }) {
                // DependencyUnresolved contains the following:
                // group, name, version, projectUrl, userReason, reason
                if (options.useSimpleDependencyNotation) {
                    this += listOf(
                        "Group",
                        "Name",
                        "Version",
                        "Unresolved Reason",
                        "Project URL",
                        "User Reason"
                    )

                    this@table.forEach {
                        this += listOf(
                            it.group.code,
                            it.name.code,
                            it.version.code,
                            (it as DependencyUnresolved).reason,
                            getProjectUrl(it.projectUrl),
                            it.userReason ?: "(No reason specified)"
                        )
                    }
                } else {
                    this += listOf("Dependency", "Unresolved Reason", "Project URL", "User Reason")

                    this@table.forEach {
                        this += listOf(
                            it.simple.code,
                            (it as DependencyUnresolved).reason,
                            getProjectUrl(it.projectUrl),
                            it.userReason ?: "(No reason specified)"
                        )
                    }
                }
                // This should be last as this is the base class that all Dependency* classes
                // extend from
            } else if (this@table.all { it is Dependency }) {
                // Dependency contains the following:
                // group, name, version, projectUrl, userReason
                if (options.useSimpleDependencyNotation) {
                    this += listOf("Dependency", "Project URL", "User Reason")

                    this@table.forEach {
                        this += listOf(
                            it.simple.code,
                            getProjectUrl(it.projectUrl),
                            it.userReason ?: "(No reason specified)"
                        )
                    }
                } else {
                    this += listOf("Group", "Name", "Version", "Project URL", "User Reason")

                    this@table.forEach {
                        this += listOf(
                            it.group.code,
                            it.name.code,
                            it.version.code,
                            getProjectUrl(it.projectUrl),
                            it.userReason ?: "(No reason specified)"
                        )
                    }
                }
            }
        }.table

    private fun String.link(link: String, title: String? = null) =
        "[$this]($link${if (title != null) " $title" else ""})"

    private val String.blockquote: String
        get() = "> $this"

    private val crossSymbol = "❌"

    private val Boolean?.verbose: String
        get() =
            if (this != null)
                if (this) "yes" else "no"
            else "(unknown)"

    private val Dependency.simple: String
        get() = "${this.name}:${this.group}:${this.version}"

    private val DependencyLatest.simpleLatest: String
        get() = "${this.name}:${this.group}:${this.latest}"

    private fun DependencyOutdated.getSimpleLatest(availableType: String): String? {
        require(availableType in listOf("release", "milestone", "integration")) {
            "Available type must be one of release, milestone or integration " +
                "but $availableType was specified"
        }
        return when (availableType) {
            "milestone" ->
                if (this.available?.milestone != null)
                    "${this.name}:${this.group}:${this.available.milestone}"
                else null
            "integration" ->
                if (this.available?.integration != null)
                    "${this.name}:${this.group}:${this.available.integration}"
                else null
            else ->
                if (this.available?.release != null)
                    "${this.name}:${this.group}:${this.available.release}"
                else null
        }
    }
}
