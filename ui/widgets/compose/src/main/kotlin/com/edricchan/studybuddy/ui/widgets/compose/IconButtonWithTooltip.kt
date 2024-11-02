package com.edricchan.studybuddy.ui.widgets.compose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipScope
import androidx.compose.material3.TooltipState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.PopupPositionProvider

/**
 * An icon button Composable that has a tooltip.
 * @param modifier Modifier to be passed to the [IconButton]
 * @param tooltipModifier Modifier to be passed to the [TooltipBox] tooltip
 * @param tooltip Composable to be used for the tooltip's content. The tooltip should be
 * enclosed in a [androidx.compose.material3.PlainTooltip] or
 * [androidx.compose.material3.RichTooltip].
 * @param icon Composable to be used for the icon button's icon
 * @param enabled Whether the icon button is enabled
 * @param onClick Function that is invoked when the icon button is clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconButtonWithTooltip(
    modifier: Modifier = Modifier,
    tooltipModifier: Modifier = Modifier,
    positionProvider: PopupPositionProvider = TooltipDefaults.rememberTooltipPositionProvider(),
    state: TooltipState = rememberTooltipState(),
    tooltip: @Composable TooltipScope.() -> Unit,
    icon: @Composable () -> Unit,
    enabled: Boolean = true,
    onClick: () -> Unit
) = TooltipBox(
    modifier = tooltipModifier,
    tooltip = tooltip,
    state = state,
    positionProvider = positionProvider
) {
    IconButton(modifier = modifier, onClick = onClick, enabled = enabled, content = icon)
}
