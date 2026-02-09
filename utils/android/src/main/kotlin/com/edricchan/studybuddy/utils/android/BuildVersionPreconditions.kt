package com.edricchan.studybuddy.utils.android

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

/**
 * Executes the [action] if the current device's [Build.VERSION.SDK_INT] is higher or equal
 * to the specified [value].
 */
@ChecksSdkIntAtLeast(parameter = 0, lambda = 1)
inline fun fromApi(value: Int, action: () -> Unit) {
    if (Build.VERSION.SDK_INT >= value) {
        action()
    }
}

/**
 * Returns the called value of [ifTrue] if the current device's [Build.VERSION.SDK_INT] is higher or
 * equal to the specified [value], or [ifFalse].
 * @param value Desired [Build.VERSION_CODES] value.
 * @param ifTrue Lambda to be called if `Build.VERSION.SDK_INT >= value` returns `true`, with the
 * desired value to be used.
 * @param ifFalse Lambda to be called if `Build.VERSION.SDK_INT >= value` returns `false`, with the
 * desired value to be used.
 * @return The result of [ifTrue] or [ifFalse] depending upon the result of
 * `Build.VERSION.SDK_INT >= value`.
 */
@ChecksSdkIntAtLeast(parameter = 0, lambda = 1)
inline fun <T> ifApi(value: Int, ifTrue: () -> T, ifFalse: () -> T): T {
    return if (Build.VERSION.SDK_INT >= value) {
        ifTrue()
    } else ifFalse()
}

/**
 * Returns the called value of [ifTrue] if the current device's [Build.VERSION.SDK_INT] is higher or
 * equal to the specified [value], or `null`.
 * @param value Desired [Build.VERSION_CODES] value.
 * @param ifTrue Lambda to be called if `Build.VERSION.SDK_INT >= value` returns `true`, with the
 * desired value to be used.
 * @return The result of [ifTrue] or `null` depending upon the result of
 * `Build.VERSION.SDK_INT >= value`.
 */
@ChecksSdkIntAtLeast(parameter = 0, lambda = 1)
inline fun <T : Any> ifApiOrNull(value: Int, ifTrue: () -> T): T? =
    ifApi(value = value, ifTrue = ifTrue, ifFalse = { null })
