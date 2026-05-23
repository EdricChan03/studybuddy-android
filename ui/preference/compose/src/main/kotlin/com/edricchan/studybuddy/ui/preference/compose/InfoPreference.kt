package com.edricchan.studybuddy.ui.preference.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Preference which shows additional information for a specific section of
 * content.
 * @param colors [PreferenceColors] to be used, if any.
 * @param icon Desired icon [Composable] to be shown on the start, or `null`
 * to not show any icon. Defaults to [PreferenceDefaults.InfoIcon] if not specified.
 * @param text Desired information text to be shown.
 */
@Composable
fun InfoPreference(
    modifier: Modifier = Modifier,
    colors: PreferenceColors = PreferenceDefaults.colors(),
    icon: @Composable (() -> Unit)? = { PreferenceDefaults.InfoIcon(contentDescription = null) },
    text: @Composable () -> Unit,
) {
    PreferenceContent(
        modifier = modifier,
        iconSpaceReserved = true,
        icon = icon,
        title = {},
        subtitle = text,
        action = null,
        showActionDivider = false,
        enabled = true,
        colors = colors
    )
}
