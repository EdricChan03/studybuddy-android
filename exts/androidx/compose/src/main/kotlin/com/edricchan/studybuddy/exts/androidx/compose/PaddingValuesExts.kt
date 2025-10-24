package com.edricchan.studybuddy.exts.androidx.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection

/** Adds the specified [other]'s [PaddingValues] to the receiver's [PaddingValues]. */
@Composable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    val direction = LocalLayoutDirection.current

    return PaddingValues(
        start = calculateStartPadding(direction) + other.calculateStartPadding(direction),
        top = calculateTopPadding() + other.calculateTopPadding(),
        end = calculateEndPadding(direction) + other.calculateEndPadding(direction),
        bottom = calculateBottomPadding() + other.calculateBottomPadding(),
    )
}
