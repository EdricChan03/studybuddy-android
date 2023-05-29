package com.edricchan.studybuddy.ui.widgets.compose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * An icon button Composable that has a tooltip.
 * @param modifier Modifier to be passed to the [IconButton]
 * @param tooltip Composable to be used for the tooltip's content
 * @param icon Composable to be used for the icon button's icon
 * @param enabled Whether the icon button is enabled
 * @param onClick Function that is invoked when the icon button is clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconButtonWithTooltip(
    modifier: Modifier = Modifier,
    tooltip: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    PlainTooltipBox(tooltip = tooltip) {
        IconButton(
            modifier = modifier.tooltipTrigger(),
            onClick = onClick,
            enabled = enabled
        ) {
            icon()
        }
    }
}
