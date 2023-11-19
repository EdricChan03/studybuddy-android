package com.edricchan.studybuddy.ui.widgets.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

/**
 * An icon button with a back icon Composable that has a tooltip.
 * @param modifier Modifier to be passed to the [IconButton]
 * @param tooltipModifier Modifier to be passed to the
 * [androidx.compose.material3.TooltipBox] tooltip
 * @param tooltipTextModifier Modifier to be passed to the [Text] composable
 * representing the tooltip text
 * @param enabled Whether the icon button is enabled
 * @param onClick Function that is invoked when the icon button is clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackIconButton(
    modifier: Modifier = Modifier,
    tooltipModifier: Modifier = Modifier,
    tooltipTextModifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) = IconButtonWithTooltip(
    modifier = modifier,
    tooltipModifier = tooltipModifier,
    tooltip = {
        PlainTooltip {
            Text(
                modifier = tooltipTextModifier,
                text = stringResource(R.string.back_btn_tooltip_text)
            )
        }
    },
    icon = {
        Icon(
            Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = stringResource(R.string.back_btn_tooltip_text)
        )
    },
    enabled = enabled,
    onClick = onClick
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun BackIconButtonPreview() {
    StudyBuddyTheme {
        TopAppBar(
            title = { Text(text = "My screen") },
            navigationIcon = {
                BackIconButton(onClick = {})
            }
        )
    }
}
