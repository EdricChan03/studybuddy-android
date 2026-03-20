package com.edricchan.studybuddy.exts.androidx.compose.ui.insets

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Adds padding to the bottom insets of [navigationBars] so that the content
 * doesn't enter this space.
 *
 * This is the equivalent of:
 * ```
 * Modifier.windowInsetsPadding(
 *     insets = WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
 * )
 * ```
 *
 * This can be used in a scrollable column (like a [androidx.compose.foundation.lazy.LazyColumn]
 * or a [androidx.compose.foundation.layout.Column] with the [androidx.compose.foundation.verticalScroll]
 * modifier applied):
 * ```
 * LazyColumn
 * ```
 */
@Composable
fun Modifier.navigationBarsBottomPadding(): Modifier = windowInsetsPadding(
    insets = WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
)
