package com.edricchan.studybuddy.exts.firebase

import com.edricchan.studybuddy.exts.datetime.format
import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/** Formats the [Timestamp] using the given [formatter]. */
fun Timestamp.format(formatter: DateTimeFormatter) = toInstant().format(formatter)

/**
 * Formats the [Timestamp] using the given [pattern] and configures
 * the formatter with [formatterInit].
 */
fun Timestamp.format(
    pattern: String, formatterInit: DateTimeFormatter.() -> Unit = {}
) = toInstant().format(pattern, formatterInit)

/** Converts a [Timestamp] to a [LocalDateTime]. */
fun Timestamp.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofEpochSecond(seconds, nanoseconds, ZoneOffset.UTC)

/** Converts a [LocalDateTime] to a [Timestamp]. */
fun LocalDateTime.toTimestamp() = Timestamp(second.toLong(), nano)

/** Converts a [Timestamp] to an [Instant]. */
fun Timestamp.toInstant(): Instant = Instant.ofEpochSecond(seconds, nanoseconds.toLong())

/** Converts an [Instant] to a [Timestamp]. */
fun Instant.toTimestamp() = Timestamp(epochSecond, nano)
