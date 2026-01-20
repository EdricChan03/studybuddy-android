package com.edricchan.studybuddy.exts.androidx.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Stable
import androidx.compose.foundation.layout.plus as foundationPlus

/** Adds the specified [other]'s [PaddingValues] to the receiver's [PaddingValues]. */
@Deprecated(
    "Use the extension function from Compose Foundation instead",
    ReplaceWith(
        "this + other",
        "androidx.compose.foundation.layout.plus"
    )
)
@Stable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues = foundationPlus(other)
