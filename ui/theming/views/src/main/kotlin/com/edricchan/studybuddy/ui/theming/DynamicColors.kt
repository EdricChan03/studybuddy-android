package com.edricchan.studybuddy.ui.theming

import android.content.Context
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.color.MaterialColors
import androidx.appcompat.R as AppCompatR
import com.google.android.material.R as MaterialR

/**
 * Retrieves the [`colorPrimary`][MaterialR.attr.colorPrimary] dynamic colour, or the
 * [default value][R.color.colorPrimary] if the receiver context does not have
 * dynamic theming applied, as a colour [Int].
 */
@get:ColorInt
val Context.dynamicColorPrimary
    get() = MaterialColors.getColor(
        this,
        AppCompatR.attr.colorPrimary,
        ContextCompat.getColor(this, R.color.colorPrimary)
    )

/**
 * Retrieves the specified colour for the given [colorAttrResId] given the receiver
 * [View] as a colour [Int].
 */
@ColorInt
fun View.getThemedColor(@AttrRes colorAttrResId: Int): Int = MaterialColors.getColor(
    this,
    colorAttrResId
)

/**
 * Sets dynamic theming colours on the receiver [SwipeRefreshLayout].
 *
 * * [`colorPrimary`][MaterialR.attr.colorPrimary] and [`colorSecondary`][MaterialR.attr.colorSecondary]
 * are used for the progress animation's colour scheme, and
 * * [`colorPrimaryContainer`][MaterialR.attr.colorPrimaryContainer] is used for the
 * background colour scheme
 */
fun SwipeRefreshLayout.setDynamicColors() {
    setColorSchemeColors(getThemedColor(MaterialR.attr.colorOnPrimaryContainer))
    setProgressBackgroundColorSchemeColor(getThemedColor(MaterialR.attr.colorPrimaryContainer))
}
