package com.edricchan.studybuddy.ui.widgets.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

/**
 * Icon button composable which should show more actions when clicked on.
 * @param modifier Modifier to be passed to [IconButtonWithTooltip].
 * @param enabled Whether the icon button is enabled.
 * @param onClick Function that is invoked when the icon button is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreActionsIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) = IconButtonWithTooltip(
    modifier = modifier,
    tooltip = {
        PlainTooltip {
            Text(text = stringResource(R.string.more_actions_btn_tooltip_text))
        }
    },
    icon = { Icon(Icons.Outlined.MoreVert, contentDescription = null) },
    enabled = enabled,
    onClick = onClick
)

/** Scope for the menu items of [OverflowIconButton]. */
@LayoutScopeMarker
@Immutable
interface MenuScope : ColumnScope {
    /** Request for the menu to be dismissed. */
    fun hideMenu()
}

internal class MenuScopeImpl(
    private val requestDismiss: () -> Unit,
    private val column: ColumnScope
) : MenuScope, ColumnScope by column {
    override fun hideMenu() {
        requestDismiss()
    }
}

/**
 * Icon button composable which displays a [dropdown][DropdownMenu] of more actions.
 * @param modifier Modifier to be passed to [IconButtonWithTooltip].
 * @param enabled Whether the icon button is enabled.
 * @param popupModifier Modifier to be passed to [DropdownMenu].
 * @param popupProperties Properties for the [DropdownMenu]'s popup.
 * @param popupContent Content for the [DropdownMenu].
 */
@Composable
fun OverflowIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    popupModifier: Modifier = Modifier,
    popupProperties: PopupProperties = PopupProperties(focusable = true),
    popupContent: @Composable MenuScope.() -> Unit
) {
    var moreExpanded by rememberSaveable { mutableStateOf(false) }
    val scope = remember {
        { column: ColumnScope ->
            MenuScopeImpl(
                requestDismiss = { moreExpanded = false },
                column = column
            )
        }
    }

    MoreActionsIconButton(
        modifier = modifier,
        enabled = enabled,
        onClick = { moreExpanded = true }
    )

    DropdownMenu(
        modifier = popupModifier,
        expanded = moreExpanded,
        onDismissRequest = { moreExpanded = false },
        properties = popupProperties
    ) {
        scope(this).popupContent()
    }
}

@Preview
@Composable
private fun OverflowIconButtonPreview() {
    StudyBuddyTheme {
        Surface {
            Box(modifier = Modifier.padding(72.dp)) {
                OverflowIconButton {
                    DropdownMenuItem(text = { Text(text = "Settings") }, onClick = ::hideMenu)
                }
            }
        }
    }
}
