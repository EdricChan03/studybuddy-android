package com.edricchan.studybuddy.ui.preference.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val DividerPreferenceIconPadding = 56.dp

private fun Modifier.dividerIconSpacePadding(withPadding: Boolean) =
    if (withPadding) padding(start = DividerPreferenceIconPadding) else this

/**
 * Draws a horizontal divider.
 *
 * If [iconSpaceReserved] is set to `true`, a [padding] of [DividerPreferenceIconPadding]
 * will be applied to the `start`ing side.
 * @param iconSpaceReserved Whether to add additional starting padding.
 * @see HorizontalDivider
 */
@Composable
fun PreferenceDivider(
    modifier: Modifier = Modifier,
    iconSpaceReserved: Boolean = true
) = HorizontalDivider(modifier = modifier.dividerIconSpacePadding(iconSpaceReserved))
