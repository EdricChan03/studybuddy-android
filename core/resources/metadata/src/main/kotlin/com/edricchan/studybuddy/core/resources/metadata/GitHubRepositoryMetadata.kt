package com.edricchan.studybuddy.core.resources.metadata

import android.net.Uri
import androidx.core.net.toUri

data class GitHubRepositoryMetadata(
    val username: String,
    val repo: String
) {
    fun authorUrl(
        baseUrl: String = "https://github.com"
    ): String = "$baseUrl/$username"

    fun authorUri(
        baseUrl: String = "https://github.com"
    ): Uri = authorUrl(baseUrl).toUri()

    fun repoUrl(
        baseUrl: String = "https://github.com"
    ): String = "$baseUrl/$username/$repo"

    fun repoUri(
        baseUrl: String = "https://github.com"
    ): Uri = repoUrl(baseUrl).toUri()

    fun issuesUrl(
        baseUrl: String = "https://github.com"
    ): String = "$baseUrl/$username/$repo/issues"

    fun issuesUri(
        baseUrl: String = "https://github.com"
    ): Uri = issuesUrl(baseUrl).toUri()

    fun commitUrl(
        sha: String,
        baseUrl: String = "https://github.com"
    ): String = "$baseUrl/$username/$repo/commit/$sha"

    fun commitUri(
        sha: String,
        baseUrl: String = "https://github.com"
    ): Uri = commitUrl(sha, baseUrl).toUri()

    fun contributorsGraphUrl(
        baseUrl: String = "https://github.com"
    ): String = "$baseUrl/$username/$repo/graphs/contributors"

    fun contributorsGraphUri(
        baseUrl: String = "https://github.com"
    ): Uri = contributorsGraphUrl(baseUrl).toUri()
}
