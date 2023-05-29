package com.edricchan.studybuddy.ui.widgets.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

@Composable
fun MoreActionsIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    popupModifier: Modifier = Modifier,
    popupProperties: PopupProperties = PopupProperties(focusable = true),
    popupContent: @Composable ColumnScope.(requestDismissPopup: () -> Unit) -> Unit
) {
    var moreExpanded by remember { mutableStateOf(false) }

    IconButtonWithTooltip(
        modifier = modifier,
        tooltip = { MoreActionsIconButtonDefaults.Tooltip() },
        icon = { MoreActionsIconButtonDefaults.Icon() },
        enabled = enabled,
        onClick = { moreExpanded = true }
    )

    DropdownMenu(
        modifier = popupModifier,
        expanded = moreExpanded,
        onDismissRequest = { moreExpanded = false },
        properties = popupProperties
    ) {
        popupContent { moreExpanded = false }
    }
}

object MoreActionsIconButtonDefaults {
    @Composable
    fun Tooltip() {
        Text(text = stringResource(R.string.more_actions_btn_tooltip_text))
    }

    @Composable
    fun Icon() {
        Icon(Icons.Outlined.MoreVert, contentDescription = null)
    }
}

@Preview
@Composable
private fun MoreActionsIconButtonPreview() {
    StudyBuddyTheme {
        Surface {
            Box(modifier = Modifier.padding(72.dp)) {
                MoreActionsIconButton { dismiss ->
                    DropdownMenuItem(text = { Text(text = "Settings") }, onClick = { dismiss() })
                }
            }
        }
    }
}
