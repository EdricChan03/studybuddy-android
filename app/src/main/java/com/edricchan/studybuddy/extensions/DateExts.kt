package com.edricchan.studybuddy.extensions

import java.time.Duration
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalUnit

/** Converts the [Long] to a [Duration] with the specified [unit]. */
fun Long.toDuration(unit: TemporalUnit): Duration = Duration.of(this, unit)

/** Formats the temporal object using the given [formatter]. */
fun TemporalAccessor.format(formatter: DateTimeFormatter): String = formatter.format(this)

/** Formats the temporal object using the given [pattern]. */
fun TemporalAccessor.format(pattern: String) = format(DateTimeFormatter.ofPattern(pattern))
