package com.edricchan.studybuddy.ui.widgets.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

/**
 * An icon button with a back icon Composable that has a tooltip.
 * @param modifier Modifier to be passed to the [IconButton]
 * @param enabled Whether the icon button is enabled
 * @param onClick Function that is invoked when the icon button is clicked
 */
@Composable
fun BackIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    IconButtonWithTooltip(
        modifier = modifier,
        tooltip = { BackIconButtonDefaults.Tooltip() },
        icon = { BackIconButtonDefaults.Icon() },
        enabled = enabled,
        onClick = onClick
    )
}

object BackIconButtonDefaults {
    @Composable
    fun Tooltip() {
        Text(text = stringResource(R.string.back_btn_tooltip_text))
    }

    @Composable
    fun Icon() {
        androidx.compose.material3.Icon(Icons.Outlined.ArrowBack, contentDescription = null)
    }
}
