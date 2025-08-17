package com.edricchan.studybuddy.domain.common

import java.time.Instant

/**
 * Classes marked with this interface have timestamp information, such as the
 * [creation date][createdAt] and when the data was [last modified][lastModified].
 */
interface WithTimestampMetadata {
    /** Timestamp when the data was created. */
    val createdAt: Instant

    /** Timestamp when the data was last updated. */
    val lastModified: Instant
}
