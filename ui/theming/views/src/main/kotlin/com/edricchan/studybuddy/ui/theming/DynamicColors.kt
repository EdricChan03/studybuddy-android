package com.edricchan.studybuddy.ui.theming

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.google.android.material.color.MaterialColors
import com.google.android.material.R as MaterialR

/**
 * Retrieves the `colorPrimary` dynamic colour, or the default value if the
 * receiver context does not have dynamic theming applied, as a colour [Int].
 */
@get:ColorInt
val Context.dynamicColorPrimary
    get() = MaterialColors.getColor(
        this,
        MaterialR.attr.colorPrimary,
        ContextCompat.getColor(this, R.color.colorPrimary)
    )
