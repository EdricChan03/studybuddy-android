package com.edricchan.studybuddy.core.resources.metadata

import com.edricchan.studybuddy.core.resources.metadata.StudyBuddyMetadata.BuildTime
import com.edricchan.studybuddy.core.resources.metadata.StudyBuddyMetadata.GitCommitSha
import java.time.Instant

data object StudyBuddyMetadata {
    /**
     * Timestamp when the app was built, in milliseconds.
     *
     * Consider using [BuildTime] where preferable.
     */
    const val BuildTimeMillis: Long = BuildConfig.BUILD_TIME

    /** Timestamp when the app was built. */
    val BuildTime: Instant = Instant.ofEpochMilli(BuildTimeMillis)

    /** The commit SHA of HEAD when the app was built. */
    const val GitCommitSha: String = BuildConfig.GIT_COMMIT_SHA

    /** URL pointing to the GitHub repository. */
    const val GitHubRepoUrl: String = "https://github.com/EdricChan03/studybuddy-android"

    /** URL pointing to the specific GitHub commit of [GitCommitSha]. */
    const val GitHubCommitUrl: String = "$GitHubRepoUrl/$GitCommitSha"
}
