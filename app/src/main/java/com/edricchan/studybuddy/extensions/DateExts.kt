package com.edricchan.studybuddy.extensions

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalUnit
import java.util.*

/**
 * Converts a [Date] to its [Timestamp] equivalent
 * @return A [Timestamp]
 */
fun Date.toTimestamp() = Timestamp(this)

/**
 * Converts a date to the specified [format]
 *
 * See the [`SimpleDateFormat` docs](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html) for more info
 * @param format A format that [SimpleDateFormat] supports
 * @param locale The locale to be used to convert the date
 */
fun Date.toDateFormat(format: String, locale: Locale = Locale.getDefault()): String =
    SimpleDateFormat(format, locale)
        .format(this)

/** Converts the [Long] to a [Duration] with the specified [unit]. */
fun Long.toDuration(unit: TemporalUnit) = Duration.of(this, unit)

/** Formats the temporal object using the given [formatter]. */
fun TemporalAccessor.format(formatter: DateTimeFormatter) = formatter.format(this)

/** Formats the temporal object using the given [pattern]. */
fun TemporalAccessor.format(pattern: String) = format(DateTimeFormatter.ofPattern(pattern))
