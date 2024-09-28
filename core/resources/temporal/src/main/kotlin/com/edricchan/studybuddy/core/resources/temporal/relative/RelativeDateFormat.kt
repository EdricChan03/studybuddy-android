package com.edricchan.studybuddy.core.resources.temporal.relative

import android.content.Context
import android.icu.text.RelativeDateTimeFormatter
import android.os.Build
import android.text.format.DateUtils
import com.edricchan.studybuddy.core.resources.temporal.R
import com.edricchan.studybuddy.exts.datetime.toLocalDateTime
import java.time.Instant
import java.time.temporal.ChronoUnit

// Implementation shamelessly stolen from
// https://github.com/libre-tube/LibreTube/blob/3a3b5df1a25c055a8189df2dcb938bb06e32126a/app/src/main/java/com/github/libretube/util/TextUtils.kt#L65-L94
/**
 * Formats the [time] as a relative time-span to the [now] value.
 * @receiver [Context] to retrieve the years and months plurals resources from.
 * @param time The [Instant] to format.
 * @param now The current time to use.
 * @see DateUtils.getRelativeTimeSpanString
 */
fun Context.formatRelativeTimeSpan(time: Instant, now: Instant = Instant.now()): String {
    val date = time.toLocalDateTime()
    val nowDateTime = now.toLocalDateTime()
    val months = date.until(nowDateTime, ChronoUnit.MONTHS)

    return if (months > 0) {
        val years = months / 12

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val (timeFormat, timeMs) = if (years > 0) {
                RelativeDateTimeFormatter.RelativeUnit.YEARS to years
            } else {
                RelativeDateTimeFormatter.RelativeUnit.MONTHS to months
            }
            RelativeDateTimeFormatter.getInstance()
                .format(timeMs.toDouble(), RelativeDateTimeFormatter.Direction.LAST, timeFormat)
        } else {
            val (timeAgoRes, timeMs) = if (years > 0) {
                R.plurals.relative_years_ago to years
            } else {
                R.plurals.relative_months_ago to months
            }
            resources.getQuantityString(timeAgoRes, timeMs.toInt(), timeMs)
        }
    } else {
        val weeks = date.until(nowDateTime, ChronoUnit.WEEKS)
        val minResolution = if (weeks > 0) DateUtils.WEEK_IN_MILLIS else 0L
        DateUtils.getRelativeTimeSpanString(time.toEpochMilli(), now.toEpochMilli(), minResolution)
            .toString()
    }
}
