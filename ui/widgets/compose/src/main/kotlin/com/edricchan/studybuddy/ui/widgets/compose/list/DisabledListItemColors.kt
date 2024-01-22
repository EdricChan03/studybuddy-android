package com.edricchan.studybuddy.ui.widgets.compose.list

import androidx.compose.material3.ListItemColors

// TODO: Remove workaround for https://issuetracker.google.com/issues/280480132
/**
 * Adds disabled colours support for the receiver [ListItemColors] as a workaround
 * for [b/280480132](https://issuetracker.google.com/issues/280480132).
 */
fun ListItemColors.withDisabledColors(enabled: Boolean) = ListItemColors(
    containerColor = containerColor,
    headlineColor = if (enabled) headlineColor else disabledHeadlineColor,
    leadingIconColor = if (enabled) leadingIconColor else disabledLeadingIconColor,
    overlineColor = overlineColor,
    supportingTextColor = supportingTextColor,
    trailingIconColor = if (enabled) trailingIconColor else disabledTrailingIconColor,
    disabledHeadlineColor = disabledHeadlineColor,
    disabledLeadingIconColor = disabledLeadingIconColor,
    disabledTrailingIconColor = disabledTrailingIconColor
)
