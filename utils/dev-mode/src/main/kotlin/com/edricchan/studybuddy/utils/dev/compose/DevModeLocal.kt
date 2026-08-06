package com.edricchan.studybuddy.utils.dev.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalWithComputedDefaultOf
import androidx.compose.ui.platform.LocalContext
import com.edricchan.studybuddy.utils.dev.isDevMode

/**
 * [androidx.compose.runtime.CompositionLocal] indicating whether developer mode is enabled.
 *
 * Note that this defaults to `true` if the build variant is set to `debug`.
 * @see isDevMode
 */
val LocalIsAppDevMode: ProvidableCompositionLocal<Boolean> = compositionLocalWithComputedDefaultOf {
    LocalContext.currentValue.isDevMode()
}

/**
 * Whether developer mode is enabled. This is equivalent to calling `LocalIsAppDevMode.current`.
 * @see LocalIsAppDevMode
 */
@Composable
fun isAppDevMode(): Boolean = LocalIsAppDevMode.current
