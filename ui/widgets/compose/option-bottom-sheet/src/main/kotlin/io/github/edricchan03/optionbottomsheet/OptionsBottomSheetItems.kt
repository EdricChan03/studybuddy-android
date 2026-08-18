package io.github.edricchan03.optionbottomsheet

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider
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
    val groupItems = remember(
        group,
        group.itemsSelectionMap
    ) { group.itemsSelectionMap.filterKeys { it.visible }.entries.toList() }

    LazyColumn(
        modifier = modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
    ) {
        itemsIndexed(
            items = groupItems,
            key = { _, (item, _) -> item.id },
            contentType = { _, _ -> group.checkableBehavior }
        ) { index, (item, checked) ->
            when (group.checkableBehavior) {
                CheckableBehavior.None -> OptionsBottomSheetItem(
                    item = item,
                    onDismissBottomSheetRequest = {
                        onDismissBottomSheetRequest(item)
                    },
                    shapes = ListItemDefaults.segmentedShapes(
                        index = index,
                        count = groupItems.size
                    )
                )

                CheckableBehavior.All -> OptionsBottomSheetCheckableItem(
                    item = item,
                    checked = checked,
                    onCheckedChange = { group.onCheckedChange(item, it) },
                    shapes = ListItemDefaults.segmentedShapes(
                        index = index,
                        count = groupItems.size
                    )
                )

                CheckableBehavior.Single -> OptionsBottomSheetSelectableItem(
                    item = item,
                    selected = checked,
                    onSelectClick = { group.onCheckedChange(item, checked) },
                    shapes = ListItemDefaults.segmentedShapes(
                        index = index,
                        count = groupItems.size
                    )
                )
            }

        }
    }
}

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
    onClick: () -> Unit,
    icon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    shapes: ListItemShapes
) = ListItem(
    modifier = modifier,
    enabled = enabled,
    onClick = onClick,
    content = { Text(text = title) },
    leadingContent = icon,
    shapes = shapes
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
    item: BottomSheetOption,
    shapes: ListItemShapes
) = OptionsBottomSheetItem(
    modifier = modifier,
    title = item.title,
    icon = item.icon,
    enabled = item.enabled,
    onClick = item.onClick,
    shapes = shapes
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
    item: BottomSheetOption,
    shapes: ListItemShapes
) = OptionsBottomSheetItem(
    modifier = modifier,
    item = item.copy(onClick = {
        item.onClick(); if (item.requestDismissOnClick) onDismissBottomSheetRequest()
    }),
    shapes = shapes
)

private class BooleanPreviewParameterProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(false, true)
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun OptionsBottomSheetItemPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) enabled: Boolean
) {
    OptionsBottomSheetItem(
        title = "Settings",
        icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
        onClick = {},
        enabled = enabled,
        shapes = ListItemDefaults.shapes()
    )
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
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    shapes: ListItemShapes
) = ListItem(
    modifier = modifier,
    enabled = enabled,
    content = { Text(text = title) },
    leadingContent = icon,
    trailingContent = {
        Checkbox(enabled = enabled, checked = checked, onCheckedChange = null)
    },
    shapes = shapes
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
    onCheckedChange: (Boolean) -> Unit,
    shapes: ListItemShapes
) = OptionsBottomSheetCheckableItem(
    modifier = modifier,
    title = item.title,
    icon = item.icon,
    enabled = item.enabled,
    checked = checked,
    onCheckedChange = onCheckedChange,
    shapes = shapes
)

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun OptionsBottomSheetCheckableItemPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) enabled: Boolean
) {
    var checked by remember { mutableStateOf(true) }

    OptionsBottomSheetCheckableItem(
        title = "Debug mode",
        icon = { Icon(Icons.Outlined.Build, contentDescription = null) },
        enabled = enabled,
        checked = checked,
        onCheckedChange = { checked = it },
        shapes = ListItemDefaults.shapes()
    )
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun OptionsBottomSheetCheckableItemGroupPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) enabled: Boolean
) {
    val selectedOptions = remember {
        mutableStateMapOf<Int, Boolean>().apply {
            this += (0 until 50).toList().associateWith { false }
        }
    }

    LazyColumn(
        modifier = Modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
    ) {
        items(
            selectedOptions.entries.sortedBy { (index, _) -> index }
        ) { (index, checked) ->
            OptionsBottomSheetCheckableItem(
                title = "Option $index",
                icon = { Icon(Icons.Outlined.FavoriteBorder, contentDescription = null) },
                enabled = enabled,
                checked = checked,
                onCheckedChange = { selectedOptions[index] = it },
                shapes = ListItemDefaults.segmentedShapes(
                    index = index,
                    count = selectedOptions.size
                )
            )
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
    selected: Boolean,
    onClick: () -> Unit,
    shapes: ListItemShapes
) = ListItem(
    modifier = modifier,
    enabled = enabled,
    content = { Text(text = title) },
    leadingContent = icon,
    trailingContent = {
        RadioButton(
            enabled = enabled, selected = selected, onClick = null
        )
    },
    shapes = shapes
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
    onSelectClick: () -> Unit,
    shapes: ListItemShapes
) = OptionsBottomSheetSelectableItem(
    modifier = modifier,
    title = item.title,
    icon = item.icon,
    enabled = item.enabled,
    selected = selected,
    onClick = { item.onClick(); onSelectClick() },
    shapes = shapes
)

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun OptionsBottomSheetSelectableItemPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) enabled: Boolean
) {
    var checked by remember { mutableStateOf(true) }

    OptionsBottomSheetSelectableItem(
        title = "Option 1",
        icon = { Icon(Icons.Outlined.Build, contentDescription = null) },
        enabled = enabled,
        selected = checked,
        onClick = { checked = !checked },
        shapes = ListItemDefaults.shapes()
    )
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun OptionsBottomSheetSelectableItemGroupPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) enabled: Boolean
) {
    var selectedOption by remember { mutableIntStateOf(0) }
    val count = 50

    LazyColumn(
        modifier = Modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
    ) {
        items(count) { index ->
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
                onClick = { selectedOption = index },
                shapes = ListItemDefaults.segmentedShapes(
                    index = index,
                    count = count
                )
            )
        }
    }
}
