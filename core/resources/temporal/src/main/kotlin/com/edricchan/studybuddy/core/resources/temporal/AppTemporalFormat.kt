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
 * Returns the formatted receiver [TemporalAccessor] using the application-default
 * format pattern, performing any conversions to the appropriate temporal objects
 * if required.
 * @see AppDateTimeFormatter
 * @see AppDateFormatter
 * @see AppTimeFormatter
 */
fun TemporalAccessor.appFormat(): String {
    // Instants don't have the DayOfWeek field, which FormatStyle.MEDIUM uses
    if (this is Instant) return toLocalDateTime().format(AppDateTimeFormatter)

    return runCatching { format(AppDateTimeFormatter) }
        .recoverCatching { format(AppDateFormatter) }
        .recoverCatching { format(AppTimeFormatter) }
        .getOrThrow()
}

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
