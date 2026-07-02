package com.edricchan.studybuddy.core.resources.temporal

import com.edricchan.studybuddy.exts.datetime.format
import com.edricchan.studybuddy.exts.datetime.toLocalDateTime
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.TemporalAccessor

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
