package com.edricchan.studybuddy.core.resources.temporal.relative

import android.icu.text.RelativeDateTimeFormatter
import android.text.format.DateUtils
import com.edricchan.studybuddy.exts.datetime.toLocalDateTime
import java.time.Instant
import java.time.temporal.ChronoUnit

// Implementation shamelessly stolen from
// https://github.com/libre-tube/LibreTube/blob/3a3b5df1a25c055a8189df2dcb938bb06e32126a/app/src/main/java/com/github/libretube/util/TextUtils.kt#L65-L94
/**
 * Formats the receiver [Instant] as a relative time-span to the [toInstant] value.
 * @receiver The [Instant] to format.
 * @param toInstant The current time to use.
 * @see DateUtils.getRelativeTimeSpanString
 */
fun Instant.formatRelativeTo(toInstant: Instant): String {
    val date = toLocalDateTime()
    val nowDateTime = toInstant.toLocalDateTime()
    val months = date.until(nowDateTime, ChronoUnit.MONTHS)

    return if (months > 0) {
        val years = months / 12

        val (timeFormat, timeMs) = if (years > 0) {
            RelativeDateTimeFormatter.RelativeUnit.YEARS to years
        } else {
            RelativeDateTimeFormatter.RelativeUnit.MONTHS to months
        }
        RelativeDateTimeFormatter.getInstance()
            .format(timeMs.toDouble(), RelativeDateTimeFormatter.Direction.LAST, timeFormat)
    } else {
        val weeks = date.until(nowDateTime, ChronoUnit.WEEKS)
        val minResolution = if (weeks > 0) DateUtils.WEEK_IN_MILLIS else 0L
        DateUtils.getRelativeTimeSpanString(toEpochMilli(), toInstant.toEpochMilli(), minResolution)
            .toString()
    }
}

/**
 * Formats the receiver [Instant] as a time-span relative to [Instant.now].
 * @see formatRelativeTo
 */
fun Instant.formatRelativeToNow(): String = formatRelativeTo(Instant.now())
