package com.edricchan.studybuddy.core.resources.metadata

import android.net.Uri
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

    /** [GitHubRepositoryMetadata] instance for the project. */
    val GitHubRepository: GitHubRepositoryMetadata = GitHubRepositoryMetadata(
        username = "EdricChan03",
        repo = "studybuddy-android"
    )

    /**
     * URL pointing to the author of the project.
     * @see StudyBuddyMetadata.GitHubAuthorUri
     * @see GitHubRepositoryMetadata.authorUrl
     */
    val GitHubAuthorUrl: String = GitHubRepository.authorUrl()

    /**
     * [Uri] pointing to the author of the project.
     * @see StudyBuddyMetadata.GitHubAuthorUrl
     * @see GitHubRepositoryMetadata.authorUri
     */
    val GitHubAuthorUri: Uri = GitHubRepository.authorUri()

    /**
     * URL pointing to the GitHub repository.
     * @see StudyBuddyMetadata.GitHubRepoUri
     * @see GitHubRepositoryMetadata.repoUri
     */
    val GitHubRepoUrl: String = GitHubRepository.repoUrl()

    /**
     * [Uri] pointing to the GitHub repository.
     * @see StudyBuddyMetadata.GitHubRepoUrl
     */
    val GitHubRepoUri: Uri = GitHubRepository.repoUri()

    /**
     * URL pointing to the GitHub repository's issues list.
     * @see StudyBuddyMetadata.GitHubIssuesUri
     */
    val GitHubIssuesUrl: String = GitHubRepository.issuesUrl()

    /**
     * [Uri] pointing to the GitHub repository's issues list.
     * @see StudyBuddyMetadata.GitHubIssuesUrl
     */
    val GitHubIssuesUri: Uri = GitHubRepository.issuesUri()

    /**
     * URL pointing to the specific GitHub commit metadata for [StudyBuddyMetadata.GitCommitSha].
     * @see StudyBuddyMetadata.GitHubCommitUri
     */
    val GitHubCommitUrl: String = GitHubRepository.commitUrl(sha = GitCommitSha)

    /**
     * [Uri] pointing to the specific GitHub commit metadata for [StudyBuddyMetadata.GitCommitSha].
     * @see StudyBuddyMetadata.GitHubCommitUrl
     */
    val GitHubCommitUri: Uri = GitHubRepository.commitUri(sha = GitCommitSha)

    /**
     * URL pointing to the GitHub repository's contributors graph.
     * @see StudyBuddyMetadata.GitHubContributorsUri
     */
    val GitHubContributorsUrl: String = GitHubRepository.contributorsGraphUrl()

    /**
     * [Uri] pointing to the GitHub repository's contributors graph.
     * @see StudyBuddyMetadata.GitHubContributorsUrl
     */
    val GitHubContributorsUri: Uri = GitHubRepository.contributorsGraphUri()
}
