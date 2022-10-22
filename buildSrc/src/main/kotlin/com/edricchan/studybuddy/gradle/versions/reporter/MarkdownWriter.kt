package com.edricchan.studybuddy.gradle.versions.reporter

import com.github.benmanes.gradle.versions.reporter.result.Dependency
import com.github.benmanes.gradle.versions.reporter.result.DependencyLatest
import com.github.benmanes.gradle.versions.reporter.result.DependencyOutdated
import com.github.benmanes.gradle.versions.reporter.result.DependencyUnresolved
import com.github.benmanes.gradle.versions.reporter.result.Result
import java.io.PrintStream

@RequiresOptIn(
    message = "This API is meant to be used by the Markdown reporter"
)
annotation class InternalMarkdownApi

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
private fun Set<Dependency>.getTable(useSimpleDependencyNotation: Boolean) =
    buildList<List<String>> {
        val getProjectUrl =
            { url: String? -> if (url != null) "Link".link(url) else "(No project URL specified)" }

        if (this@getTable.all { it is DependencyLatest }) {
            // DependencyLatest contains the following:
            // group, name, version, projectUrl, userReason, latest
            if (useSimpleDependencyNotation) {
                this += listOf(
                    "Dependency (current)",
                    "Dependency (latest)",
                    "Project URL",
                    "User Reason"
                )

                this@getTable.forEach {
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

                this@getTable.forEach {
                    this += listOf(
                        it.group?.code ?: "-",
                        it.name?.code ?: "-",
                        it.version?.code ?: "-",
                        (it as DependencyLatest).latest,
                        getProjectUrl(it.projectUrl),
                        it.userReason ?: "(No reason specified)"
                    )
                }
            }
        } else if (this@getTable.all { it is DependencyOutdated }) {
            // DependencyOutdated contains the following:
            // group, name, version, projectUrl, userReason, available
            if (useSimpleDependencyNotation) {
                this += listOf(
                    "Dependency (current)",
                    "Dependency (release)",
                    "Dependency (milestone)",
                    "Dependency (integration)",
                    "Project URL",
                    "User Reason"
                )

                this@getTable.forEach {
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

                this@getTable.forEach {
                    val dep = it as DependencyOutdated
                    this += listOf(
                        dep.group?.code ?: "-",
                        dep.name?.code ?: "-",
                        dep.version?.code ?: "-",
                        dep.available.release?.code ?: "(No release version available)",
                        dep.available.milestone?.code ?: "(No milestone version available)",
                        dep.available.integration?.code
                            ?: "(No integration version available)",
                        getProjectUrl(dep.projectUrl),
                        dep.userReason ?: "(No reason specified)"
                    )
                }
            }
        } else if (this@getTable.all { it is DependencyUnresolved }) {
            // DependencyUnresolved contains the following:
            // group, name, version, projectUrl, userReason, reason
            if (useSimpleDependencyNotation) {
                this += listOf(
                    "Group",
                    "Name",
                    "Version",
                    "Unresolved Reason",
                    "Project URL",
                    "User Reason"
                )

                this@getTable.forEach {
                    this += listOf(
                        it.group?.code ?: "-",
                        it.name?.code ?: "-",
                        it.version?.code ?: "-",
                        (it as DependencyUnresolved).reason,
                        getProjectUrl(it.projectUrl),
                        it.userReason ?: "(No reason specified)"
                    )
                }
            } else {
                this += listOf("Dependency", "Unresolved Reason", "Project URL", "User Reason")

                this@getTable.forEach {
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
        } else {
            // Dependency contains the following:
            // group, name, version, projectUrl, userReason
            if (useSimpleDependencyNotation) {
                this += listOf("Dependency", "Project URL", "User Reason")

                this@getTable.forEach {
                    this += listOf(
                        it.simple.code,
                        getProjectUrl(it.projectUrl),
                        it.userReason ?: "(No reason specified)"
                    )
                }
            } else {
                this += listOf("Group", "Name", "Version", "Project URL", "User Reason")

                this@getTable.forEach {
                    this += listOf(
                        it.group?.code ?: "-",
                        it.name?.code ?: "-",
                        it.version?.code ?: "-",
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
            if (this.available.milestone != null)
                "${this.name}:${this.group}:${this.available.milestone}"
            else null

        "integration" ->
            if (this.available.integration != null)
                "${this.name}:${this.group}:${this.available.integration}"
            else null

        else ->
            if (this.available.release != null)
                "${this.name}:${this.group}:${this.available.release}"
            else null
    }
}

@InternalMarkdownApi
fun PrintStream.writeHeader(name: String) {
    println("$name Dependency Updates".header(1))
}

@OptIn(ExperimentalStdlibApi::class)
@InternalMarkdownApi
fun PrintStream.writeGradleUpdates(releaseChannel: String, result: Result) {
    if (!result.gradle.enabled) return

    println("Gradle $releaseChannel updates".header(2))
    with(result.gradle) {
        val list = mutableListOf("Current version of Gradle used: ${running.version}")
        if (current.isFailure) list += "$crossSymbol Could not retrieve Gradle update: ${current.reason}".bold
        if (current.isUpdateAvailable && current > running) list += "An update is available! ${current.version}"
        println(list.unorderedList)

        println("Available Gradle versions".header(3, newPrevLine = true))
        val gradleVersionsTable = kotlin.collections.buildMap {
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

@InternalMarkdownApi
fun PrintStream.writeUpToDate(
    revision: String,
    useSimpleDependencyNotation: Boolean,
    result: Result
) {
    val versions = result.current.dependencies
    println("Up to date dependencies".header(2))
    if (versions.isNotEmpty()) {
        println("The following dependencies are using the latest $revision version:")
        println(versions.getTable(useSimpleDependencyNotation))
    } else println("(No up to date dependencies were found)".italics)
    println()
}

@InternalMarkdownApi
fun PrintStream.writeExceedLatestFound(
    revision: String,
    useSimpleDependencyNotation: Boolean,
    result: Result
) {
    val versions = result.exceeded.dependencies
    println("Exceeded dependencies".header(2))
    if (versions.isNotEmpty()) {
        println("The following dependencies exceed the version found at the $revision revision level:")
        println(versions.getTable(useSimpleDependencyNotation))
    } else println("(No dependencies were found that exceeded the latest version)".italics)
    println()
}

@InternalMarkdownApi
fun PrintStream.writeOutdated(
    revision: String,
    useSimpleDependencyNotation: Boolean,
    result: Result
) {
    val versions = result.outdated.dependencies
    println("Outdated dependencies".header(2))
    if (versions.isNotEmpty()) {
        println("The following dependencies have later $revision versions:")
        println(versions.getTable(useSimpleDependencyNotation))
    } else println("(No outdated dependencies were found)".italics)
    println()
}

@InternalMarkdownApi
fun PrintStream.writeUndeclared(useSimpleDependencyNotation: Boolean, result: Result) {
    val versions = result.undeclared.dependencies
    println("Undeclared dependencies".header(2))
    if (versions.isNotEmpty()) {
        println("The following dependencies could not be compared as they were declared without a version:")
        println(versions.getTable(useSimpleDependencyNotation))
    } else println("(No undeclared dependencies were found)".italics)
    println()
}

@InternalMarkdownApi
fun PrintStream.writeUnresolved(useSimpleDependencyNotation: Boolean, result: Result) {
    val versions = result.unresolved.dependencies
    println("Unresolved dependencies".header(2))
    if (versions.isNotEmpty()) {
        println("The following dependencies could not be resolved:")
        println(versions.getTable(useSimpleDependencyNotation))
    } else println("(No unresolved dependencies were found)".italics)
    println()
}
