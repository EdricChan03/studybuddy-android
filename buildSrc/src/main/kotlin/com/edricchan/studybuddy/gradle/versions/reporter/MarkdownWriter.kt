package com.edricchan.studybuddy.gradle.versions.reporter

import com.github.benmanes.gradle.versions.reporter.result.Dependency
import com.github.benmanes.gradle.versions.reporter.result.DependencyLatest
import com.github.benmanes.gradle.versions.reporter.result.DependencyOutdated
import com.github.benmanes.gradle.versions.reporter.result.DependencyUnresolved
import com.github.benmanes.gradle.versions.reporter.result.Result
import java.io.PrintStream
import kotlin.reflect.KClass

@RequiresOptIn(
    message = "This API is intended to be used by the Markdown reporter and " +
        "is not suited for outside use."
)
annotation class InternalMarkdownApi

// Markdown utilities

private fun String.header(
    level: Int,
    newNextLine: Boolean = true,
    newPrevLine: Boolean = false
): String {
    require(level in (1..6)) {
        "Markdown only supports heading levels 1 - 6, but $level was specified"
    }
    return "${if (newPrevLine) "\n" else ""}${"#".repeat(level)} $this${if (newNextLine) "\n" else ""}"
}

private inline val String.bold: String
    get() = "**$this**"

private inline val String.italics: String
    get() = "*$this*"

private inline val String.code: String
    get() = "`$this`"

private inline val List<String>.unorderedList: String
    get() = joinToString("\n") { "* $it" }

private inline val List<List<String>>.tableString: String
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

private inline val String?.formattedUrl
    get() = if (this != null) "Link".link(this) else "(No URL specified)"

private typealias TableRow = List<String>
private typealias Table = List<TableRow>

// Convert a dependency entry to a table row

private fun DependencyLatest.toTableRow(useSimpleDependencyNotation: Boolean): TableRow {
    // DependencyLatest contains the following:
    // group, name, version, projectUrl, userReason, latest
    return if (useSimpleDependencyNotation) listOf(
        simple.code,
        simpleLatest.code,
        projectUrl.formattedUrl,
        userReason ?: "(No reason specified)"
    ) else listOf(
        group?.code ?: "-",
        name?.code ?: "-",
        version?.code ?: "-",
        latest,
        projectUrl.formattedUrl,
        userReason ?: "(No reason specified)"
    )
}

private fun DependencyOutdated.toTableRow(useSimpleDependencyNotation: Boolean): TableRow {
    // DependencyOutdated contains the following:
    // group, name, version, projectUrl, userReason, available
    return if (useSimpleDependencyNotation) listOf(
        simple.code,
        getSimpleLatest("release")?.code
            ?: "(No release version available)",
        getSimpleLatest("milestone")?.code
            ?: "(No milestone version available)",
        getSimpleLatest("integration")?.code
            ?: "(No integration version available)",
        projectUrl.formattedUrl,
        userReason ?: "(No reason specified)"
    ) else listOf(
        group?.code ?: "-",
        name?.code ?: "-",
        version?.code ?: "-",
        available.release?.code ?: "(No release version available)",
        available.milestone?.code ?: "(No milestone version available)",
        available.integration?.code
            ?: "(No integration version available)",
        projectUrl.formattedUrl,
        userReason ?: "(No reason specified)"
    )
}

private fun DependencyUnresolved.toTableRow(useSimpleDependencyNotation: Boolean): TableRow {
    // DependencyUnresolved contains the following:
    // group, name, version, projectUrl, userReason, reason
    return if (useSimpleDependencyNotation) listOf(
        simple.code,
        reason,
        projectUrl.formattedUrl,
        userReason ?: "(No reason specified)"
    ) else listOf(
        group?.code ?: "-",
        name?.code ?: "-",
        version?.code ?: "-",
        reason,
        projectUrl.formattedUrl,
        userReason ?: "(No reason specified)"
    )
}

private fun Dependency.toTableRow(useSimpleDependencyNotation: Boolean): TableRow {
    // Dependency contains the following:
    // group, name, version, projectUrl, userReason
    return if (useSimpleDependencyNotation) listOf(
        simple.code,
        projectUrl.formattedUrl,
        userReason ?: "(No reason specified)"
    ) else listOf(
        group?.code ?: "-",
        name?.code ?: "-",
        version?.code ?: "-",
        projectUrl.formattedUrl,
        userReason ?: "(No reason specified)"
    )
}

// Retrieve the table header given the dependency class
private fun <T : Dependency> getTableHeader(
    depClass: KClass<T>,
    useSimpleDependencyNotation: Boolean
): TableRow {
    return when (depClass) {
        DependencyLatest::class -> if (useSimpleDependencyNotation) listOf(
            "Dependency (current)",
            "Dependency (latest)",
            "Project URL",
            "User Reason"
        ) else listOf("Group", "Name", "Version", "Latest", "Project URL", "User Reason")

        DependencyOutdated::class -> if (useSimpleDependencyNotation) listOf(
            "Dependency (current)",
            "Dependency (release)",
            "Dependency (milestone)",
            "Dependency (integration)",
            "Project URL",
            "User Reason"
        ) else listOf(
            "Group",
            "Name",
            "Version",
            "Available (release)",
            "Available (milestone)",
            "Available (integration)",
            "Project URL",
            "User Reason"
        )

        DependencyUnresolved::class -> if (useSimpleDependencyNotation) listOf(
            "Group",
            "Name",
            "Version",
            "Unresolved Reason",
            "Project URL",
            "User Reason"
        ) else listOf("Dependency", "Unresolved Reason", "Project URL", "User Reason")

        Dependency::class -> if (useSimpleDependencyNotation) listOf(
            "Dependency",
            "Project URL",
            "User Reason"
        )
        else listOf("Group", "Name", "Version", "Project URL", "User Reason")

        else -> error("Dependency class is not supported, received: $depClass")
    }
}


private inline fun <reified T : Dependency> Set<T>.toTable(
    useSimpleDependencyNotation: Boolean,
    showHeader: Boolean = true
): Table = buildList<TableRow> {
    if (showHeader) this += getTableHeader(T::class, useSimpleDependencyNotation)
    this += this@toTable.map { it.toTableRow(useSimpleDependencyNotation) }
}

private inline fun <reified T : Dependency> Set<T>.toTableString(
    useSimpleDependencyNotation: Boolean,
    showHeader: Boolean = true
) = toTable(useSimpleDependencyNotation, showHeader).tableString

private fun String.link(link: String, title: String? = null) =
    "[$this]($link${if (title != null) " $title" else ""})"

private val String.blockquote: String
    get() = "> $this"

private const val crossSymbol = "❌"

private val Boolean?.verbose: String
    get() =
        if (this != null)
            if (this) "yes" else "no"
        else "(unknown)"

private inline val Dependency.simple: String
    get() = "${this.name}:${this.group}:${this.version}"

private inline val DependencyLatest.simpleLatest: String
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

@InternalMarkdownApi
fun PrintStream.writeGradleUpdates(releaseChannel: String, result: Result) {
    if (!result.gradle.enabled) return

    println("Gradle $releaseChannel updates".header(2))
    with(result.gradle) {
        val gradleList = buildList {
            this += "Current version of Gradle used: ${running.version}"
            if (current.isFailure) this += "$crossSymbol Could not retrieve Gradle update: ${current.reason}".bold
            if (current.isUpdateAvailable && current > running) this += "An update is available! ${current.version}"
        }
        println(gradleList.unorderedList)

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
        println(versions.toTableString(useSimpleDependencyNotation))
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
        println(versions.toTableString(useSimpleDependencyNotation))
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
        println(versions.toTableString(useSimpleDependencyNotation))
    } else println("(No outdated dependencies were found)".italics)
    println()
}

@InternalMarkdownApi
fun PrintStream.writeUndeclared(useSimpleDependencyNotation: Boolean, result: Result) {
    val versions = result.undeclared.dependencies
    println("Undeclared dependencies".header(2))
    if (versions.isNotEmpty()) {
        println("The following dependencies could not be compared as they were declared without a version:")
        println(versions.toTableString(useSimpleDependencyNotation))
    } else println("(No undeclared dependencies were found)".italics)
    println()
}

@InternalMarkdownApi
fun PrintStream.writeUnresolved(useSimpleDependencyNotation: Boolean, result: Result) {
    val versions = result.unresolved.dependencies
    println("Unresolved dependencies".header(2))
    if (versions.isNotEmpty()) {
        println("The following dependencies could not be resolved:")
        println(versions.toTableString(useSimpleDependencyNotation))
    } else println("(No unresolved dependencies were found)".italics)
    println()
}
