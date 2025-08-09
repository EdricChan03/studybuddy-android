package com.edricchan.studybuddy.core.resources.icons.compat

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.R

/**
 * Wrapper class for compat XML vector drawables.
 * @property iconRes Icon resource drawable.
 */
@JvmInline
value class CompatIcon(
    @field:DrawableRes
    val iconRes: Int
) {
    /** Converts this vector icon to its Compose [ImageVector] equivalent. */
    @Composable
    fun vector(): ImageVector = ImageVector.vectorResource(iconRes)
}

/** @see R.drawable.ic_bug_report_outline_24dp */
val AppIcons.Compat.BugReport: CompatIcon by lazy { CompatIcon(R.drawable.ic_bug_report_outline_24dp) }

/** @see R.drawable.ic_close_24dp */
val AppIcons.Compat.Close: CompatIcon by lazy { CompatIcon(R.drawable.ic_close_24dp) }

/** @see R.drawable.ic_help_outline_24dp */
val AppIcons.Compat.Help: CompatIcon by lazy { CompatIcon(R.drawable.ic_help_outline_24dp) }
