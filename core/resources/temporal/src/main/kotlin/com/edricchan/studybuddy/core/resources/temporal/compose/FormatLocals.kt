package com.edricchan.studybuddy.core.resources.temporal.compose

import android.text.format.DateFormat
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalWithComputedDefaultOf
import androidx.compose.ui.platform.LocalContext

/**
 * [androidx.compose.runtime.CompositionLocal] for determining whether time-based values
 * should use their 24-hour format when displayed.
 *
 * Defaults to the result of [DateFormat.is24HourFormat] on Android.
 */
val LocalIs24HourFormat: ProvidableCompositionLocal<Boolean> =
    compositionLocalWithComputedDefaultOf {
        DateFormat.is24HourFormat(LocalContext.currentValue)
    }
