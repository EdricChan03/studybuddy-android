package com.edricchan.studybuddy.core.resources.metadata

import com.edricchan.studybuddy.core.resources.metadata.AppMetadata.BuildTime
import java.time.Instant

data object AppMetadata {
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
}
