package com.edricchan.studybuddy.core.resources.icons.compat

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
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

    /** Converts this vector icon to its Compose [Painter] equivalent. */
    @Composable
    fun painter(): Painter = painterResource(iconRes)
}

/** @see R.drawable.ic_bug_report_outline_24dp */
val AppIcons.Compat.BugReport: CompatIcon by lazy { CompatIcon(R.drawable.ic_bug_report_outline_24dp) }

/** @see R.drawable.ic_close_24dp */
val AppIcons.Compat.Close: CompatIcon by lazy { CompatIcon(R.drawable.ic_close_24dp) }

/** @see R.drawable.ic_help_outline_24dp */
val AppIcons.Compat.Help: CompatIcon by lazy { CompatIcon(R.drawable.ic_help_outline_24dp) }

/** @see R.drawable.ic_more_vert_24dp */
val AppIcons.Compat.MoreVert: CompatIcon by lazy { CompatIcon(R.drawable.ic_more_vert_24dp) }

/** @see R.drawable.ic_open_in_new_24dp */
val AppIcons.Compat.OpenInNew: CompatIcon by lazy { CompatIcon(R.drawable.ic_open_in_new_24dp) }

/** @see R.drawable.ic_plus_24dp */
val AppIcons.Compat.Plus: CompatIcon by lazy { CompatIcon(R.drawable.ic_plus_24dp) }

/** @see R.drawable.ic_refresh_24dp */
val AppIcons.Compat.Refresh: CompatIcon by lazy { CompatIcon(R.drawable.ic_refresh_24dp) }

/** @see R.drawable.ic_settings_outline_24dp */
val AppIcons.Compat.Settings: CompatIcon by lazy { CompatIcon(R.drawable.ic_settings_outline_24dp) }
