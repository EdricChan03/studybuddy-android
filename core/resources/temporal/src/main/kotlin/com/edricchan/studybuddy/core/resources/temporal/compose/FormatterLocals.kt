package com.edricchan.studybuddy.core.resources.temporal.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.edricchan.studybuddy.core.resources.temporal.AppDateFormatter
import com.edricchan.studybuddy.core.resources.temporal.AppDateTimeFormatter
import com.edricchan.studybuddy.core.resources.temporal.AppTimeFormatter
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

/**
 * [androidx.compose.runtime.CompositionLocal] for formatting date-time objects.
 * @see AppDateTimeFormatter
 */
val LocalDateTimeFormatter: ProvidableCompositionLocal<DateTimeFormatter> = compositionLocalOf {
    AppDateTimeFormatter
}

/**
 * [androidx.compose.runtime.CompositionLocal] for formatting date objects.
 * @see AppDateFormatter
 */
val LocalDateFormatter: ProvidableCompositionLocal<DateTimeFormatter> = compositionLocalOf {
    AppDateFormatter
}

/**
 * [androidx.compose.runtime.CompositionLocal] for formatting time objects.
 * @see AppTimeFormatter
 */
val LocalTimeFormatter: ProvidableCompositionLocal<DateTimeFormatter> = compositionLocalOf {
    AppTimeFormatter
}

/**
 * Formats the receiver [TemporalAccessor] with the [LocalDateTimeFormatter] composition local.
 * @see LocalDateTimeFormatter
 */
@Composable
fun TemporalAccessor.formatWithDateTime(): String = LocalDateTimeFormatter.current.format(this)

/**
 * Formats the receiver [TemporalAccessor] with the [LocalDateFormatter] composition local.
 * @see LocalDateFormatter
 */
@Composable
fun TemporalAccessor.formatWithDate(): String = LocalDateFormatter.current.format(this)

/**
 * Formats the receiver [TemporalAccessor] with the [LocalTimeFormatter] composition local.
 * @see LocalTimeFormatter
 */
@Composable
fun TemporalAccessor.formatWithTime(): String = LocalTimeFormatter.current.format(this)
