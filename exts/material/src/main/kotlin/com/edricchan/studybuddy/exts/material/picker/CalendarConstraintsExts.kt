package com.edricchan.studybuddy.exts.material.picker

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import java.time.DayOfWeek
import java.time.Instant

/** Creates a [CalendarConstraints] using DSL syntax. */
fun calendarConstraints(init: CalendarConstraints.Builder.() -> Unit) =
    CalendarConstraints.Builder().apply(init).build()

/**
 * Creates a [CalendarConstraints] using named arguments syntax.
 * @param start See [setStart].
 * @param end See [setEnd].
 * @param openAt See [setOpenAt].
 * @param firstDayOfWeek See [setFirstDayOfWeek].
 * @param validator See [CalendarConstraints.Builder.setValidator].
 * @see calendarConstraints
 * @see setStart
 * @see setEnd
 * @see setOpenAt
 * @see setFirstDayOfWeek
 * @see CalendarConstraints.Builder.setValidator
 */
@RequiresApi(Build.VERSION_CODES.O)
fun calendarConstraints(
    start: Instant? = null,
    end: Instant? = null,
    openAt: Instant? = null,
    firstDayOfWeek: DayOfWeek? = null,
    validator: DateValidator? = null
) = calendarConstraints {
    start?.let { setStart(it) }
    end?.let { setEnd(it) }
    openAt?.let { setOpenAt(it) }
    firstDayOfWeek?.let { setFirstDayOfWeek(it) }
    validator?.let { setValidator(it) }
}

/**
 * A UTC timeInMilliseconds contained within the earliest month the calendar will page to.
 * Defaults January, 1900.
 *
 * This variant allows for an [Instant] to be used.
 * @see CalendarConstraints.Builder.setStart
 */
@RequiresApi(Build.VERSION_CODES.O)
fun CalendarConstraints.Builder.setStart(instant: Instant) =
    setStart(instant.toEpochMilli())

/**
 * A UTC timeInMilliseconds contained within the latest month the calendar will page to.
 * Defaults December, 2100.
 *
 * This variant allows for an [Instant] to be used.
 * @see CalendarConstraints.Builder.setEnd
 */
@RequiresApi(Build.VERSION_CODES.O)
fun CalendarConstraints.Builder.setEnd(instant: Instant) =
    setEnd(instant.toEpochMilli())

/**
 * Sets the range of allowed months the calendar can page to.
 * See [setStart] and [setEnd] for their defaults respectively.
 *
 * @see setStart
 * @see CalendarConstraints.Builder.setStart
 * @see setEnd
 * @see CalendarConstraints.Builder.setEnd
 */
@RequiresApi(Build.VERSION_CODES.O)
fun CalendarConstraints.Builder.setRange(start: Instant, end: Instant) = apply {
    val data = start..end
    setStart(start)
    setEnd(end)
}

/**
 * Sets the range of allowed months the calendar can page to.
 * See [setStart] and [setEnd] for their defaults respectively.
 *
 * This variant allows for a Kotlin range to be used - only its [ClosedRange.start]
 * and [ClosedRange.endInclusive] values will be used for [setStart] and [setEnd]
 * respectively.
 *
 * @see setStart
 * @see CalendarConstraints.Builder.setStart
 * @see setEnd
 * @see CalendarConstraints.Builder.setEnd
 */
@RequiresApi(Build.VERSION_CODES.O)
fun CalendarConstraints.Builder.setRange(range: ClosedRange<Instant>) = apply {
    setStart(range.start)
    setEnd(range.endInclusive)
}

/**
 * A UTC timeInMilliseconds contained within the month the calendar should openAt.
 * Defaults to the month containing today if within bounds; otherwise, defaults to
 * the starting month.
 *
 * This variant allows for an [Instant] to be used.
 * @see CalendarConstraints.Builder.setOpenAt
 */
@RequiresApi(Build.VERSION_CODES.O)
fun CalendarConstraints.Builder.setOpenAt(instant: Instant) =
    setOpenAt(instant.toEpochMilli())

/**
 * Sets what the first day of the week is; e.g., [DayOfWeek.SUNDAY] in the U.S.,
 * [DayOfWeek.MONDAY] in France.
 *
 * This variant allows for a [DayOfWeek] to be used.
 * @see CalendarConstraints.Builder.setFirstDayOfWeek
 */
@RequiresApi(Build.VERSION_CODES.O)
fun CalendarConstraints.Builder.setFirstDayOfWeek(firstDayOfWeek: DayOfWeek) =
    setFirstDayOfWeek(firstDayOfWeek.value)
