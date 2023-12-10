package io.github.edricchan03.optionbottomsheet

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import io.github.edricchan03.optionbottomsheet.models.BottomSheetOption
import io.github.edricchan03.optionbottomsheet.models.BottomSheetOptionGroup
import io.github.edricchan03.optionbottomsheet.models.BottomSheetOptionGroup.CheckableBehavior

/**
 * List content for an [OptionsModalBottomSheet].
 * @param onDismissBottomSheetRequest Lambda that is invoked to indicate a request to dismiss
 * the parent [OptionsModalBottomSheet]. The [BottomSheetOption] that was clicked on is passed
 * as the `item` parameter.
 * @param group The option group to display.
 */
@Composable
fun OptionsBottomSheetList(
    modifier: Modifier = Modifier,
    onDismissBottomSheetRequest: (item: BottomSheetOption) -> Unit,
    group: BottomSheetOptionGroup
) {
    LazyColumn(modifier = modifier.selectableGroup()) {
        items(
            group.itemsSelectionMap.filterKeys { it.visible }.entries.toList(),
            contentType = { group.checkableBehavior }
        ) { (item, checked) ->
            when (group.checkableBehavior) {
                CheckableBehavior.None -> OptionsBottomSheetItem(
                    item = item,
                    onDismissBottomSheetRequest = {
                        onDismissBottomSheetRequest(item)
                    }
                )

                CheckableBehavior.All -> OptionsBottomSheetCheckableItem(
                    item = item,
                    checked = checked,
                    onCheckedChange = { group.onCheckedChange(item, it) }
                )

                CheckableBehavior.Single -> OptionsBottomSheetSelectableItem(
                    item = item,
                    selected = checked,
                    onSelectClick = { group.onCheckedChange(item, checked) }
                )
            }

        }
    }
}

// https://issuetracker.google.com/issues/280480132
private fun ListItemColors.withDisabledColors(enabled: Boolean) = ListItemColors(
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

@Composable
private fun BaseOptionBottomSheetItem(
    modifier: Modifier,
    title: @Composable () -> Unit,
    icon: @Composable (() -> Unit)?,
    trailingContent: @Composable (() -> Unit)? = null,
    visible: Boolean = true,
    enabled: Boolean = true
) {
    if (visible) {
        ListItem(
            modifier = modifier,
            headlineContent = title,
            leadingContent = icon,
            trailingContent = trailingContent,
            colors = ListItemDefaults.colors().withDisabledColors(enabled)
        )
    }
}

@Composable
private fun BaseOptionBottomSheetItem(
    modifier: Modifier,
    item: BottomSheetOption,
    trailingContent: @Composable (() -> Unit)? = null,
) = BaseOptionBottomSheetItem(
    modifier = modifier,
    title = { Text(text = item.title) },
    icon = item.icon,
    trailingContent = trailingContent,
    visible = item.visible,
    enabled = item.enabled
)

/**
 * Displays a clickable item to be rendered in a [OptionsModalBottomSheet].
 *
 * * To display a selectable item, use [OptionsBottomSheetSelectableItem].
 * * To display a checkable item, use [OptionsBottomSheetCheckableItem].
 * @param title The item's title.
 * @param icon The item's icon.
 * @param enabled Whether the item is clickable.
 * @param onClick Lambda that is invoked when the item is clicked.
 */
@Composable
fun OptionsBottomSheetItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) = BaseOptionBottomSheetItem(
    modifier = modifier.clickable(enabled = enabled, onClick = onClick),
    title = { Text(text = title) },
    icon = icon
)

/**
 * Displays an item to be rendered in a [OptionsModalBottomSheet].
 *
 * * To display a selectable item, use [OptionsBottomSheetSelectableItem].
 * * To display a checkable item, use [OptionsBottomSheetCheckableItem].
 * @param item The [BottomSheetOption] to render.
 */
@Composable
fun OptionsBottomSheetItem(
    modifier: Modifier = Modifier,
    item: BottomSheetOption
) = BaseOptionBottomSheetItem(
    modifier = modifier.clickable(enabled = item.enabled, onClick = item.onClick),
    item = item
)

/**
 * Displays an item to be rendered in a [OptionsModalBottomSheet].
 *
 * This variant specifies a lambda, [onDismissBottomSheetRequest], which when invoked
 * should dismiss the [OptionsModalBottomSheet].
 *
 * * To display a selectable item, use [OptionsBottomSheetSelectableItem].
 * * To display a checkable item, use [OptionsBottomSheetCheckableItem].
 * @param item The [BottomSheetOption] to render.
 * @param onDismissBottomSheetRequest Lambda which when invoked should dismiss the
 * [OptionsModalBottomSheet]. This lambda is invoked **after** [BottomSheetOption.onClick]
 * is invoked.
 */
@Composable
fun OptionsBottomSheetItem(
    modifier: Modifier = Modifier,
    onDismissBottomSheetRequest: () -> Unit,
    item: BottomSheetOption
) = OptionsBottomSheetItem(
    modifier = modifier,
    item = item.copy(onClick = {
        item.onClick(); onDismissBottomSheetRequest()
    })
)

private class BooleanPreviewParameterProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(false, true)
}

@Preview
@Composable
private fun OptionsBottomSheetItemPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) enabled: Boolean
) {
    StudyBuddyTheme {
        OptionsBottomSheetItem(
            title = "Settings",
            icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
            enabled = enabled
        )
    }
}

/**
 * Displays a _checkable_ item to be rendered in a [OptionsModalBottomSheet].
 *
 * * To display a clickable item, use [OptionsBottomSheetItem].
 * * To display a selectable item, use [OptionsBottomSheetSelectableItem].
 * @param title The item's title.
 * @param icon The item's icon.
 * @param enabled Whether the item is checkable.
 * @param checked Whether the item is checked.
 * @param onCheckedChange Lambda that is invoked when the item's checked status has
 * changed, with the new value passed as the argument.
 */
@Composable
fun OptionsBottomSheetCheckableItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) = BaseOptionBottomSheetItem(
    modifier = modifier.toggleable(
        enabled = enabled,
        role = Role.Checkbox,
        value = checked,
        onValueChange = onCheckedChange
    ),
    title = { Text(text = title) },
    icon = icon,
    trailingContent = {
        Checkbox(enabled = enabled, checked = checked, onCheckedChange = null)
    }
)

/**
 * Displays a _checkable_ item to be rendered in a [OptionsModalBottomSheet].
 *
 * * To display a clickable item, use [OptionsBottomSheetItem].
 * * To display a selectable item, use [OptionsBottomSheetSelectableItem].
 * @param item The [BottomSheetOption] to render.
 * @param checked Whether the item is checked.
 * @param onCheckedChange Lambda that is invoked when the item's checked status has
 * changed, with the new value passed as the argument.
 */
@Composable
fun OptionsBottomSheetCheckableItem(
    modifier: Modifier = Modifier,
    item: BottomSheetOption,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) = OptionsBottomSheetCheckableItem(
    modifier = modifier,
    title = item.title,
    icon = item.icon,
    enabled = item.enabled,
    checked = checked,
    onCheckedChange = onCheckedChange
)

@Preview
@Composable
private fun OptionsBottomSheetCheckableItemPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) enabled: Boolean
) {
    var checked by remember { mutableStateOf(true) }

    StudyBuddyTheme {
        OptionsBottomSheetCheckableItem(
            title = "Debug mode",
            icon = { Icon(Icons.Outlined.Build, contentDescription = null) },
            enabled = enabled,
            checked = checked,
            onCheckedChange = { checked = it }
        )
    }
}

@Preview
@Composable
private fun OptionsBottomSheetCheckableItemGroupPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) enabled: Boolean
) {
    val selectedOptions = remember {
        mutableStateMapOf<Int, Boolean>().apply {
            this += (0 until 50).toList().associateWith { false }
        }
    }

    StudyBuddyTheme {
        LazyColumn(modifier = Modifier.selectableGroup()) {
            items(
                selectedOptions.entries.sortedBy { (index, _) -> index }
            ) { (index, checked) ->
                OptionsBottomSheetCheckableItem(
                    title = "Option $index",
                    icon = { Icon(Icons.Outlined.FavoriteBorder, contentDescription = null) },
                    enabled = enabled,
                    checked = checked,
                    onCheckedChange = { selectedOptions[index] = it }
                )
            }
        }
    }
}

/**
 * Displays a _selectable_ item to be rendered in a [OptionsModalBottomSheet].
 *
 * * To display a clickable item, use [OptionsBottomSheetItem].
 * * To display a checkable item, use [OptionsBottomSheetCheckableItem].
 * @param title The item's title.
 * @param icon The item's icon.
 * @param enabled Whether the item is checkable.
 * @param selected Whether the item is selected.
 * @param onClick Lambda that is invoked to select the item.
 */
@Composable
fun OptionsBottomSheetSelectableItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    selected: Boolean = false,
    onClick: () -> Unit
) = BaseOptionBottomSheetItem(
    modifier = modifier.selectable(
        enabled = enabled,
        selected = selected,
        role = Role.RadioButton,
        onClick = onClick
    ),
    title = { Text(text = title) },
    icon = icon,
    trailingContent = {
        RadioButton(
            enabled = enabled, selected = selected, onClick = null
        )
    }
)

/**
 * Displays a _selectable_ item to be rendered in a [OptionsModalBottomSheet].
 *
 * * To display a clickable item, use [OptionsBottomSheetItem].
 * * To display a checkable item, use [OptionsBottomSheetCheckableItem].
 * @param item The item to render.
 * @param selected Whether the item is selected.
 * @param onSelectClick Lambda that is invoked to select the item. This lambda is
 * invoked **after** [BottomSheetOption.onClick] is invoked.
 */
@Composable
fun OptionsBottomSheetSelectableItem(
    modifier: Modifier = Modifier,
    item: BottomSheetOption,
    selected: Boolean = false,
    onSelectClick: () -> Unit
) = OptionsBottomSheetSelectableItem(
    modifier = modifier,
    title = item.title,
    icon = item.icon,
    enabled = item.enabled,
    selected = selected,
    onClick = { item.onClick(); onSelectClick() }
)

@Preview
@Composable
private fun OptionsBottomSheetSelectableItemPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) enabled: Boolean
) {
    var checked by remember { mutableStateOf(true) }

    StudyBuddyTheme {
        OptionsBottomSheetSelectableItem(
            title = "Option 1",
            icon = { Icon(Icons.Outlined.Build, contentDescription = null) },
            enabled = enabled,
            selected = checked,
            onClick = { checked = !checked }
        )
    }
}

@Preview
@Composable
private fun OptionsBottomSheetSelectableItemGroupPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) enabled: Boolean
) {
    var selectedOption by remember { mutableIntStateOf(0) }

    StudyBuddyTheme {
        LazyColumn(modifier = Modifier.selectableGroup()) {
            items(50) { index ->
                val selected = remember(selectedOption) { selectedOption == index }
                OptionsBottomSheetSelectableItem(
                    title = "Option $index",
                    icon = {
                        AnimatedContent(targetState = selected, label = "Favourite icon toggle") {
                            Icon(
                                if (it) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = null
                            )
                        }
                    },
                    enabled = enabled,
                    selected = selected,
                    onClick = { selectedOption = index }
                )
            }
        }
    }
}
