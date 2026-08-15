package com.edricchan.studybuddy.core.resources.metadata

import android.net.Uri
import androidx.core.net.toUri
import java.time.Instant

data object StudyBuddyMetadata {
    /**
     * Timestamp when the app was built, in milliseconds.
     *
     * Consider using [StudyBuddyMetadata.BuildTime] where preferable.
     */
    const val BuildTimeMillis: Long = BuildConfig.BUILD_TIME

    /** Timestamp when the app was built. */
    val BuildTime: Instant = Instant.ofEpochMilli(BuildTimeMillis)

    /** The Git repository's commit SHA of `HEAD` when the app was built. */
    const val GitCommitSha: String = BuildConfig.GIT_COMMIT_SHA

    /**
     * URL pointing to the author of the project.
     * @see StudyBuddyMetadata.GitHubAuthorUri
     */
    const val GitHubAuthorUrl: String = "https://github.com/EdricChan03"

    /**
     * [Uri] pointing to the author of the project.
     * @see StudyBuddyMetadata.GitHubAuthorUrl
     */
    val GitHubAuthorUri: Uri = GitHubAuthorUrl.toUri()

    /**
     * URL pointing to the GitHub repository.
     * @see StudyBuddyMetadata.GitHubRepoUri
     */
    const val GitHubRepoUrl: String = "$GitHubAuthorUrl/studybuddy-android"

    /**
     * [Uri] pointing to the GitHub repository.
     * @see StudyBuddyMetadata.GitHubRepoUrl
     */
    val GitHubRepoUri: Uri = GitHubRepoUrl.toUri()

    /**
     * URL pointing to the specific GitHub commit metadata for [StudyBuddyMetadata.GitCommitSha].
     * @see StudyBuddyMetadata.GitHubCommitUri
     */
    const val GitHubCommitUrl: String = "$GitHubRepoUrl/commit/$GitCommitSha"

    /**
     * [Uri] pointing to the specific GitHub commit metadata for [StudyBuddyMetadata.GitCommitSha].
     * @see StudyBuddyMetadata.GitHubCommitUrl
     */
    val GitHubCommitUri: Uri = GitHubCommitUrl.toUri()

    /**
     * URL pointing to the GitHub repository's contributors graph.
     * @see StudyBuddyMetadata.GitHubContributorsUri
     */
    const val GitHubContributorsUrl: String = "$GitHubRepoUrl/graphs/contributors"

    /**
     * [Uri] pointing to the GitHub repository's contributors graph.
     * @see StudyBuddyMetadata.GitHubContributorsUrl
     */
    val GitHubContributorsUri: Uri = GitHubContributorsUrl.toUri()
}
