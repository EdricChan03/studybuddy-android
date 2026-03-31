package com.edricchan.studybuddy.core.resources.temporal

import android.content.Context
import com.edricchan.studybuddy.exts.datetime.format
import com.edricchan.studybuddy.exts.datetime.toLocalDateTime
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.TemporalAccessor
import com.edricchan.studybuddy.core.resources.R as CoreResR

/** The application default [DateTimeFormatter], for formatting date-times. */
val AppDateTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)

/** The application default [DateTimeFormatter], for formatting dates. */
val AppDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

/** The application default [DateTimeFormatter], for formatting times. */
val AppTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)

/**
 * The application default [DateTimeFormatter].
 * @see CoreResR.string.java_time_format_pattern_default
 * @see appFormat
 */
@Deprecated(
    "Use AppDateTimeFormatter directly",
    ReplaceWith(
        "AppDateTimeFormatter",
        "com.edricchan.studybuddy.core.resources.temporal.AppDateTimeFormatter"
    )
)
@Suppress("UnusedReceiverParameter")
val Context.appDateTimeFormatter: DateTimeFormatter
    get() = AppDateTimeFormatter

/**
 * Formats the receiver [TemporalAccessor] using the application-default
 * format pattern.
 * @return The formatted [TemporalAccessor] as specified by [AppDateTimeFormatter].
 * @see AppDateTimeFormatter
 */
fun TemporalAccessor.appFormat() = format(AppDateTimeFormatter)

/**
 * Formats the receiver [TemporalAccessor] using the application-default
 * format pattern.
 * @param context Context used to retrieve the string resource.
 * @return The formatted [TemporalAccessor] as specified by
 * [appDateTimeFormatter].
 * @see CoreResR.string.java_time_format_pattern_default
 * @see appDateTimeFormatter
 */
@Deprecated(
    "The context parameter is no longer required - use the overload with no arguments",
    ReplaceWith("this.appFormat()")
)
fun TemporalAccessor.appFormat(context: Context) = appFormat()
