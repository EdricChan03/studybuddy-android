package com.edricchan.studybuddy.exts.material.picker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.util.component1
import androidx.core.util.component2
import androidx.core.util.toAndroidXPair
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import kotlin.Pair
import androidx.core.util.Pair as AndroidXPair

//#region Utils

private typealias MaterialSingleDatePicker = MaterialDatePicker<Long>
private typealias MaterialSingleDatePickerBuilder = MaterialDatePicker.Builder<Long>
private typealias MaterialDateRangePicker = MaterialDatePicker<AndroidXPair<Long, Long>>
private typealias MaterialDateRangePickerBuilder = MaterialDatePicker.Builder<AndroidXPair<Long, Long>>

private fun <S1, S2, T1, T2> Pair<S1, S2>.map(
    transformFirst: (S1) -> T1, transformSecond: (S2) -> T2
): Pair<T1, T2> = transformFirst(first) to transformSecond(second)

private fun <S, T> Pair<S, S>.map(
    transform: (S) -> T
): Pair<T, T> = map(transformFirst = transform, transformSecond = transform)

//#endregion

//#region Single-date picker builder functions

/**
 * Creates a date-picker with the specified options using DSL syntax.
 * @see MaterialDatePicker.Builder.datePicker
 */
fun materialDatePicker(init: MaterialSingleDatePickerBuilder.() -> Unit) =
    MaterialDatePicker.Builder.datePicker().apply(init).build()

/**
 * Shows a date-picker with the specified options using DSL syntax.
 *
 * This is the equivalent of calling [androidx.fragment.app.DialogFragment.show]
 * with the values [FragmentActivity.getSupportFragmentManager] and the specified [tag].
 * @param tag The tag to be passed to [androidx.fragment.app.DialogFragment.show].
 * @see materialDatePicker
 */
fun FragmentActivity.showMaterialDatePicker(
    tag: String?,
    init: MaterialSingleDatePickerBuilder.() -> Unit
) = materialDatePicker(init).show(supportFragmentManager, tag)

/**
 * Shows a date-picker with the specified options using DSL syntax.
 *
 * This is the equivalent of calling [androidx.fragment.app.DialogFragment.show]
 * with the values [Fragment.getParentFragmentManager] and the specified [tag].
 * @param tag The tag to be passed to [androidx.fragment.app.DialogFragment.show].
 * @see materialDatePicker
 */
fun Fragment.showMaterialDatePicker(
    tag: String?,
    init: MaterialSingleDatePickerBuilder.() -> Unit
) = materialDatePicker(init).show(parentFragmentManager, tag)

/**
 * Sets the selection to the given [instant]. Its [Instant.toEpochMilli] value
 * will be used.
 * @see MaterialDatePicker.Builder.setSelection
 */
@RequiresApi(Build.VERSION_CODES.O)
fun MaterialSingleDatePickerBuilder.setSelection(instant: Instant) =
    setSelection(instant.toEpochMilli())

//#endregion

//#region Range picker builder functions

/**
 * Creates a date-range picker with the specified options using DSL syntax.
 * @see MaterialDatePicker.Builder.dateRangePicker
 */
fun materialDateRangePicker(
    init: MaterialDateRangePickerBuilder.() -> Unit
) =
    MaterialDatePicker.Builder.dateRangePicker().apply(init).build()

/**
 * Shows a date-range picker with the specified options using DSL syntax.
 *
 * This is the equivalent of calling [androidx.fragment.app.DialogFragment.show]
 * with the values [FragmentActivity.getSupportFragmentManager] and the specified [tag].
 * @param tag The tag to be passed to [androidx.fragment.app.DialogFragment.show].
 * @see materialDateRangePicker
 */
fun FragmentActivity.showMaterialDateRangePicker(
    tag: String?,
    init: MaterialDateRangePickerBuilder.() -> Unit
) = materialDateRangePicker(init).show(supportFragmentManager, tag)

/**
 * Shows a date-range picker with the specified options using DSL syntax.
 *
 * This is the equivalent of calling [androidx.fragment.app.DialogFragment.show]
 * with the values [Fragment.getParentFragmentManager] and the specified [tag].
 * @param tag The tag to be passed to [androidx.fragment.app.DialogFragment.show].
 * @see materialDateRangePicker
 */
fun Fragment.showMaterialDateRangePicker(
    tag: String?,
    init: MaterialDateRangePickerBuilder.() -> Unit
) = materialDateRangePicker(init).show(parentFragmentManager, tag)

/** Sets the selection for this date-range picker using a Kotlin [Pair]. */
fun MaterialDateRangePickerBuilder.setSelection(selection: Pair<Long, Long>) =
    setSelection(selection.toAndroidXPair())

/** Sets the selection for this date-range picker using named arguments. */
fun MaterialDateRangePickerBuilder.setSelection(start: Long, end: Long) =
    setSelection(start to end)

/**
 * Sets the selection to the given [instant]. Its [Instant.toEpochMilli] value
 * will be used.
 * @see MaterialDatePicker.Builder.setSelection
 */
@RequiresApi(Build.VERSION_CODES.O)
@JvmName("setSelectionInstants")
fun MaterialDateRangePickerBuilder.setSelection(selection: Pair<Instant, Instant>) =
    setSelection(selection.map { it.toEpochMilli() })

/** Sets the selection for this date-range picker using named arguments. */
@RequiresApi(Build.VERSION_CODES.O)
fun MaterialDateRangePickerBuilder.setSelection(start: Instant, end: Instant) =
    setSelection(start to end)

/**
 * Sets the selection for this date-range picker using a Kotlin range.
 *
 * Note that only its [ClosedRange.start] and [ClosedRange.endInclusive] values will
 * be used for `start` and `end` respectively - its contents are **not** used.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun MaterialDateRangePickerBuilder.setSelection(range: ClosedRange<Instant>) =
    setSelection(range.start to range.endInclusive)

/**
 * Sets the calendar constraints using DSL syntax.
 * @see MaterialDatePicker.Builder.setCalendarConstraints
 * @see calendarConstraints
 */
fun <S> MaterialDatePicker.Builder<S>.setCalendarConstraints(
    init: CalendarConstraints.Builder.() -> Unit
) = setCalendarConstraints(calendarConstraints(init))
//#endregion

/**
 * Retrieves the selection as an [Instant].
 * @see MaterialDatePicker.getSelection
 */
@get:RequiresApi(Build.VERSION_CODES.O)
val MaterialSingleDatePicker.selectionAsInstant
    get() = selection?.let { Instant.ofEpochMilli(it) }

/**
 * Retrieves the start and end selections as a pair of [Instant]s.
 * @see MaterialDatePicker.getSelection
 */
@get:RequiresApi(Build.VERSION_CODES.O)
val MaterialDateRangePicker.selectionAsInstants
    get() = selection?.let { (start, end) ->
        Instant.ofEpochMilli(start) to Instant.ofEpochMilli(
            end
        )
    }

/**
 * Retrieves the start selection as an [Instant].
 * @see MaterialDatePicker.getSelection
 */
@get:RequiresApi(Build.VERSION_CODES.O)
val MaterialDateRangePicker.selectionStartAsInstant
    get() = selection?.let { (before, _) ->
        Instant.ofEpochMilli(before)
    }

/**
 * Retrieves the end selection as an [Instant].
 * @see MaterialDatePicker.getSelection
 */
@get:RequiresApi(Build.VERSION_CODES.O)
val MaterialDateRangePicker.selectionEndAsInstant
    get() = selection?.let { (_, after) ->
        Instant.ofEpochMilli(after)
    }