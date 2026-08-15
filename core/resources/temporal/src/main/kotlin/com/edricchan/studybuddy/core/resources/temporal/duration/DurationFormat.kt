package com.edricchan.studybuddy.core.resources.temporal.duration

import android.content.Context
import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import java.time.Duration
import java.util.Locale

private fun Int.takeIfPositive(): Int? = takeIf { it > 0 }
private fun Long.takeIfPositive(): Long? = takeIf { it > 0L }

private fun Duration.toHumanReadableIcu(
    locale: Locale = Locale.getDefault()
): String {
    val measures = buildList {
        toDaysPart().takeIfPositive()?.let {
            this += Measure(it, MeasureUnit.DAY)
        }
        toHoursPart().takeIfPositive()?.let {
            this += Measure(it, MeasureUnit.HOUR)
        }
        toMinutesPart().takeIfPositive()?.let {
            this += Measure(it, MeasureUnit.MINUTE)
        }
        toSecondsPart().takeIf { it > 0 || isEmpty() }?.let {
            this += Measure(it, MeasureUnit.SECOND)
        }
    }

    val formatter = MeasureFormat.getInstance(
        locale,
        MeasureFormat.FormatWidth.WIDE
    )

    return formatter.formatMeasures(*measures.toTypedArray())
}

/**
 * Formats the receiver [Duration] to its human-readable equivalent.
 *
 * For example, a value of `PT1H30M` will result in "1 hour, 30 minutes".
 */
fun Duration.format(
    context: Context,
    locale: Locale = Locale.getDefault()
): String = toHumanReadableIcu(locale)
