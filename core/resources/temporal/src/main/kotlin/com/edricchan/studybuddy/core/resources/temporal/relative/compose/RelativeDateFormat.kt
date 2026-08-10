package com.edricchan.studybuddy.core.resources.temporal.relative.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.edricchan.studybuddy.core.resources.temporal.relative.formatRelativeTimeSpan
import com.edricchan.studybuddy.core.resources.temporal.relative.formatRelativeTo
import com.edricchan.studybuddy.core.resources.temporal.relative.formatRelativeToNow
import java.time.Instant

/**
 * Formats the receiver [Instant] as a time-span relative to the [toInstant] value.
 * @param toInstant The time to compare against.
 * @see formatRelativeTimeSpan
 */
@Composable
fun Instant.formatRelativeTo(toInstant: Instant): String = context(LocalContext.current) {
    formatRelativeTo(toInstant)
}

/**
 * Formats the receiver [Instant] as a time-span relative to [Instant.now].
 * @see formatRelativeTo
 */
@Composable
fun Instant.formatRelativeToNow(): String = context(LocalContext.current) {
    formatRelativeToNow()
}
