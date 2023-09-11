package com.edricchan.studybuddy.exts.datetime

import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalUnit

/** Converts the [Long] to a [Duration] with the specified [unit]. */
fun Long.toDuration(unit: TemporalUnit): Duration = Duration.of(this, unit)

/** Converts the [Instant] to a [LocalDateTime] given the [zoneId]. */
fun Instant.toLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime =
    LocalDateTime.ofInstant(this, zoneId)

/** Formats the temporal object using the given [formatter]. */
fun TemporalAccessor.format(formatter: DateTimeFormatter): String = formatter.format(this)

/**
 * Formats the temporal object using the given [pattern] and configures
 * the formatter with [formatterInit].
 */
fun TemporalAccessor.format(
    pattern: String, formatterInit: DateTimeFormatter.() -> Unit = {}
): String = DateTimeFormatter.ofPattern(pattern).apply(formatterInit).format(this)

/**
 * Creates a [DateTimeFormatter] from the given pattern string and configures the
 * formatter with [formatterInit].
 */
fun String.toDateTimeFormatter(formatterInit: DateTimeFormatter.() -> Unit = {}) =
    DateTimeFormatter.ofPattern(this).apply(formatterInit)

/** Formats the [Instant] to an ISO8601 date-time value. */
fun Instant.formatISO() = format(DateTimeFormatter.ISO_INSTANT)
