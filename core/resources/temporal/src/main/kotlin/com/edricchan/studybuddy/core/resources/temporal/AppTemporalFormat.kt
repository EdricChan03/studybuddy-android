package com.edricchan.studybuddy.core.resources.temporal

import android.content.Context
import com.edricchan.studybuddy.exts.datetime.format
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import com.edricchan.studybuddy.core.resources.R as CoreResR

/**
 * The application default [DateTimeFormatter].
 * @see CoreResR.string.java_time_format_pattern_default
 * @see appFormat
 */
val Context.appDateTimeFormatter: DateTimeFormatter
    get() = DateTimeFormatter.ofPattern(
        getString(CoreResR.string.java_time_format_pattern_default)
    )

/**
 * Formats the receiver [TemporalAccessor] using the application-default
 * format pattern.
 * @param context Context used to retrieve the string resource.
 * @return The formatted [TemporalAccessor] as specified by
 * [appDateTimeFormatter].
 * @see CoreResR.string.java_time_format_pattern_default
 * @see appDateTimeFormatter
 */
fun TemporalAccessor.appFormat(context: Context) =
    format(context.appDateTimeFormatter)
