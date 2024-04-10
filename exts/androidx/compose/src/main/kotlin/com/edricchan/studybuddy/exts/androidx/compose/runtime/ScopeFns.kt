package com.edricchan.studybuddy.exts.androidx.compose.runtime

import androidx.compose.runtime.Composable

/**
 * The equivalent of using the [let] function, allowing for a [Composable] function
 * to be returned.
 *
 * ```kt
 * val nullableValue: String?
 * val value: List<Example>
 *
 * @Composable
 * fun MyComposable(
 *  text: @Composable (() -> Unit)?,
 *  example: @Composable () -> Example
 * ) {}
 *
 * // Usage
 * MyComposable(
 *  text = nullableValue?.letComposable { Text(text = it) },
 *  example = value.letComposable { it.first() } // Similar to the .let {} scope function
 * )
 * ```
 */
inline fun <T, R> T.letComposable(
    crossinline block: @Composable (T) -> R
): @Composable () -> R = { block(this) }
