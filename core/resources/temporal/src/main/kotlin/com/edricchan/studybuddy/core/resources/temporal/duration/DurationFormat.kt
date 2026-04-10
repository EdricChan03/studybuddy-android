package com.edricchan.studybuddy.core.resources.temporal.duration

import android.content.Context
import android.icu.text.ListFormatter
import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import android.os.Build
import androidx.annotation.RequiresApi
import com.edricchan.studybuddy.core.resources.temporal.R
import com.edricchan.studybuddy.utils.android.ifApi
import java.time.Duration
import java.util.Locale

private fun Int.takeIfPositive(): Int? = takeIf { it > 0 }
private fun Long.takeIfPositive(): Long? = takeIf { it > 0L }

@RequiresApi(Build.VERSION_CODES.N)
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

// TODO: Revisit/drop when we bump the minSdk to Nougat or higher, as ICU APIs only exist
//  on Nougat+
private fun Duration.toHumanReadable(
    context: Context,
    locale: Locale = Locale.getDefault()
): String {
    val parts = buildList {
        toDaysPart().takeIfPositive()?.let {
            this += context.resources.getQuantityString(
                R.plurals.duration_format_days,
                it.toInt(),
                it
            )
        }

        toHoursPart().takeIfPositive()?.let {
            this += context.resources.getQuantityString(
                R.plurals.duration_format_hrs,
                it,
                it
            )
        }

        toMinutesPart().takeIfPositive()?.let {
            this += context.resources.getQuantityString(
                R.plurals.duration_format_mins,
                it,
                it
            )
        }

        toSecondsPart().takeIf { it > 0 || isEmpty() }?.let {
            this += context.resources.getQuantityString(
                R.plurals.duration_format_secs,
                it,
                it
            )
        }
    }

    val joinedParts = ifApi(
        value = Build.VERSION_CODES.O,
        ifTrue = { ListFormatter.getInstance(locale).format(parts) },
        ifFalse = { parts.joinToString(" ") }
    )
    if (isNegative) return context.getString(R.string.duration_format_negative, joinedParts)

    return joinedParts
}

/**
 * Formats the receiver [Duration] to its human-readable equivalent.
 *
 * For example, a value of `PT1H30M` will result in "1 hour, 30 minutes".
 */
fun Duration.format(
    context: Context,
    locale: Locale = Locale.getDefault()
): String = ifApi(
    value = Build.VERSION_CODES.N,
    ifTrue = { toHumanReadableIcu(locale) },
    ifFalse = { toHumanReadable(context, locale) }
)
