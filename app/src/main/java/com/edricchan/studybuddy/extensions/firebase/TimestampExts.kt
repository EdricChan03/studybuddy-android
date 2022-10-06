package com.edricchan.studybuddy.extensions.firebase

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

/**
 * Converts a [Timestamp] to the specified [format]
 *
 * See the [`SimpleDateFormat` docs](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html) for more info
 * @param format A format that [SimpleDateFormat] supports
 * @param locale The locale to be used to convert the date
 */
fun Timestamp.toDateFormat(format: String, locale: Locale = Locale.getDefault()): String =
    SimpleDateFormat(format, locale)
        .format(this.toDate())

/** Converts a [Timestamp] to a [LocalDateTime]. */
fun Timestamp.toLocalDateTime() = LocalDateTime.ofEpochSecond(seconds, nanoseconds, ZoneOffset.UTC)

/** Converts a [LocalDateTime] to a [Timestamp]. */
fun LocalDateTime.toTimestamp() = Timestamp(second.toLong(), nano)

/** Converts a [Timestamp] to an [Instant]. */
fun Timestamp.toInstant() = Instant.ofEpochSecond(seconds, nanoseconds.toLong())

/** Converts an [Instant] to a [Timestamp]. */
fun Instant.toTimestamp() = Timestamp(epochSecond, nano)
