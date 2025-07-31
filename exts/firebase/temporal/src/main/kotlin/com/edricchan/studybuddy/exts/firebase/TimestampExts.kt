package com.edricchan.studybuddy.exts.firebase

import android.annotation.SuppressLint
import com.edricchan.studybuddy.exts.datetime.format
import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/** Formats the [Timestamp] using the given [formatter]. */
@SuppressLint("NewApi") // Provided by core library desugaring
fun Timestamp.format(formatter: DateTimeFormatter): String = toInstant().format(formatter)

/**
 * Formats the [Timestamp] using the given [pattern] and configures
 * the formatter with [formatterInit].
 */
@SuppressLint("NewApi") // Provided by core library desugaring
fun Timestamp.format(
    pattern: String, formatterInit: DateTimeFormatter.() -> Unit = {}
): String = toInstant().format(pattern, formatterInit)

/** Converts a [Timestamp] to a [LocalDateTime]. */
fun Timestamp.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofEpochSecond(seconds, nanoseconds, ZoneOffset.UTC)

/** Converts a [LocalDateTime] to a [Timestamp]. */
fun LocalDateTime.toTimestamp(): Timestamp = Timestamp(second.toLong(), nano)

/** Converts an [Instant] to a [Timestamp]. */
@SuppressLint("NewApi") // Provided by core library desugaring
fun Instant.toTimestamp(): Timestamp = Timestamp(this)
