package com.edricchan.studybuddy.core.resources.temporal.duration

import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import android.os.Build
import com.edricchan.studybuddy.utils.android.ifApi
import java.time.Duration
import java.util.Locale

private fun Int.takeIfPositive(): Int? = takeIf { it > 0 }
private fun Long.takeIfPositive(): Long? = takeIf { it > 0L }

private val Duration.daysPart: Long
    get() = ifApi(
        Build.VERSION_CODES.S,
        ifTrue = { toDaysPart() },
        ifFalse = { seconds / 86_400 }
    )

private val Duration.hoursPart: Int
    get() = ifApi(
        Build.VERSION_CODES.S,
        ifTrue = { toHoursPart() },
        ifFalse = { (toHours() % 24).toInt() }
    )

private val Duration.minutesPart: Int
    get() = ifApi(
        Build.VERSION_CODES.S,
        ifTrue = { toMinutesPart() },
        ifFalse = { (toMinutes() % 60).toInt() }
    )

private val Duration.secondsPart: Int
    get() = ifApi(
        Build.VERSION_CODES.S,
        ifTrue = { toSecondsPart() },
        ifFalse = { (seconds % 60).toInt() }
    )

private fun Duration.toHumanReadableIcu(
    locale: Locale = Locale.getDefault(),
    formatWidth: MeasureFormat.FormatWidth = MeasureFormat.FormatWidth.WIDE
): String {
    val measures = buildList {
        daysPart.takeIfPositive()?.let {
            this += Measure(it, MeasureUnit.DAY)
        }
        hoursPart.takeIfPositive()?.let {
            this += Measure(it, MeasureUnit.HOUR)
        }
        minutesPart.takeIfPositive()?.let {
            this += Measure(it, MeasureUnit.MINUTE)
        }
        secondsPart.takeIf { it > 0 || isEmpty() }?.let {
            this += Measure(it, MeasureUnit.SECOND)
        }
    }

    val formatter = MeasureFormat.getInstance(
        /* locale = */ locale,
        /* formatWidth = */ formatWidth
    )

    return formatter.formatMeasures(*measures.toTypedArray())
}

/**
 * Formats the receiver [Duration] to its human-readable equivalent.
 *
 * For example, a value of `PT1H30M` will result in "1 hour, 30 minutes".
 * @param locale [Locale] to be used when retrieving an instance of [MeasureFormat] -
 * see [MeasureFormat.getInstance].
 * @param formatWidth [MeasureFormat.FormatWidth] to be used when retrieving an
 * instance of [MeasureFormat] - see [MeasureFormat.getInstance].
 */
fun Duration.format(
    locale: Locale = Locale.getDefault(),
    formatWidth: MeasureFormat.FormatWidth = MeasureFormat.FormatWidth.WIDE
): String = toHumanReadableIcu(locale)
