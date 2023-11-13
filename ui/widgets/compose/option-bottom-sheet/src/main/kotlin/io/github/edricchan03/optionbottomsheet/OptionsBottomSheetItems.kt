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
 * @param group The option group to display.
 */
@Composable
fun OptionsBottomSheetList(
    modifier: Modifier = Modifier,
    group: BottomSheetOptionGroup
) {
    LazyColumn(modifier = modifier.selectableGroup()) {
        items(
            group.itemsSelectionMap.filterKeys { it.visible }.entries.toList(),
            contentType = { group.checkableBehavior }
        ) { (item, checked) ->
            when (group.checkableBehavior) {
                CheckableBehavior.None -> OptionsBottomSheetItem(item = item)

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
fun OptionsBottomSheetItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    ListItem(
        modifier = modifier.clickable(enabled = enabled, onClick = onClick),
        headlineContent = { Text(text = title) },
        leadingContent = icon,
        colors = ListItemDefaults.colors().withDisabledColors(enabled)
    )
}

@Composable
fun OptionsBottomSheetItem(
    modifier: Modifier = Modifier,
    item: BottomSheetOption
) {
    if (item.visible) {
        OptionsBottomSheetItem(
            modifier = modifier,
            title = item.title,
            icon = item.icon,
            enabled = item.enabled,
            onClick = item.onClick
        )
    }
}

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

@Composable
fun OptionsBottomSheetCheckableItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    ListItem(
        modifier = modifier.toggleable(
            enabled = enabled,
            role = Role.Checkbox,
            value = checked,
            onValueChange = onCheckedChange
        ),
        headlineContent = { Text(text = title) },
        leadingContent = icon,
        trailingContent = {
            Checkbox(enabled = enabled, checked = checked, onCheckedChange = null)
        },
        colors = ListItemDefaults.colors().withDisabledColors(enabled)
    )
}

@Composable
fun OptionsBottomSheetCheckableItem(
    modifier: Modifier = Modifier,
    item: BottomSheetOption,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    OptionsBottomSheetCheckableItem(
        modifier = modifier,
        title = item.title,
        icon = item.icon,
        enabled = item.enabled,
        checked = checked,
        onCheckedChange = onCheckedChange
    )
}

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

@Composable
fun OptionsBottomSheetSelectableItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    ListItem(
        modifier = modifier.selectable(
            enabled = enabled,
            selected = selected,
            role = Role.RadioButton,
            onClick = onClick
        ),
        headlineContent = { Text(text = title) },
        leadingContent = icon,
        trailingContent = {
            RadioButton(
                enabled = enabled, selected = selected, onClick = null
            )
        },
        colors = ListItemDefaults.colors().withDisabledColors(enabled)
    )
}

@Composable
fun OptionsBottomSheetSelectableItem(
    modifier: Modifier = Modifier,
    item: BottomSheetOption,
    selected: Boolean = false,
    onSelectClick: () -> Unit
) {
    OptionsBottomSheetSelectableItem(
        modifier = modifier,
        title = item.title,
        icon = item.icon,
        enabled = item.enabled,
        selected = selected,
        onClick = { item.onClick(); onSelectClick() }
    )
}

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
