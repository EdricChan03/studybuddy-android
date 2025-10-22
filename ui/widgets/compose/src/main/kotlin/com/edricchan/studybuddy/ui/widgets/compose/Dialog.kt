// From https://github.com/boswelja/SmartwatchExtensions/blob/main/core/mobile/src/main/kotlin/com/boswelja/smartwatchextensions/core/ui/dialog/Dialog.kt
package com.edricchan.studybuddy.ui.widgets.compose

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import com.edricchan.studybuddy.exts.androidx.compose.ui.graphics.animateInterpolatableAsState
import com.edricchan.studybuddy.ui.widgets.compose.list.m3.ExpListItem
import com.edricchan.studybuddy.ui.widgets.compose.list.m3.ExpListItemDefaults
import com.edricchan.studybuddy.core.resources.R as CoreResR

/**
 * A material dialog for selecting an option from a list of available options.
 * @param modifier [Modifier].
 * @param title The dialog title.
 * @param icon The dialog's icon.
 * @param onDismissRequest Called when the dialog should be dismissed.
 * @param itemContent The item content Composable. This content automatically has a RadioButton
 * added to the start of the layout.
 * @param items The list of available items.
 * @param selectedItem The current selected item.
 * @param onItemSelectionChanged Called when a new item is selected.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun <T> ListDialog(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    icon: @Composable (() -> Unit)? = null,
    onDismissRequest: () -> Unit,
    itemContent: @Composable (T) -> Unit,
    items: List<T>,
    selectedItem: T?,
    onItemSelectionChanged: (T) -> Unit
) {
    var dialogSelectedItem by remember(selectedItem) {
        mutableStateOf(selectedItem)
    }

    AlertDialog(
        modifier = modifier,
        title = title,
        icon = icon,
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                enabled = dialogSelectedItem != null,
                onClick = {
                    onItemSelectionChanged(dialogSelectedItem!!)
                    onDismissRequest()
                },
                shapes = ButtonDefaults.shapes()
            ) {
                Text(stringResource(CoreResR.string.dialog_action_done))
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismissRequest,
                shapes = ButtonDefaults.shapes()
            ) {
                Text(stringResource(CoreResR.string.dialog_action_cancel))
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(ExpListItemDefaults.groupedItemsSpacing)
            ) {
                itemsIndexed(items) { i, item ->
                    val isSelected = item == dialogSelectedItem
                    val shape by animateInterpolatableAsState(
                        targetValue = ExpListItemDefaults.itemShape(
                            index = i,
                            count = items.size,
                            selected = isSelected
                        ),
                        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                    )
                    ExpListItem(
                        shape = shape,
                        colors = ExpListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        selected = isSelected,
                        onClick = { dialogSelectedItem = item },
                        headlineContent = { itemContent(item) },
                        trailingContent = {
                            RadioButton(
                                selected = isSelected, onClick = null,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )
                        }
                    )
                }
            }
        }
    )
}

/**
 * A Material Design alert dialog for requesting for input from the user.
 * @param title The dialog's title.
 * @param icon The dialog's icon.
 * @param onDismissRequest Invoked when the dialog is to be dismissed.
 * @param isValid Whether the confirm button should be enabled.
 * @param onConfirm Invoked when the confirm button is pressed.
 * @param textField Composable to be shown for the input text-field.
 * This should be a [DialogDefaults.TextField].
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InputDialog(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)?,
    icon: @Composable (() -> Unit)?,
    onDismissRequest: () -> Unit,
    isValid: Boolean,
    onConfirm: () -> Unit,
    textField: @Composable () -> Unit
) = AlertDialog(
    modifier = modifier,
    title = title,
    icon = icon,
    onDismissRequest = onDismissRequest,
    confirmButton = {
        Button(
            enabled = isValid,
            onClick = onConfirm,
            shapes = ButtonDefaults.shapes()
        ) {
            Text(stringResource(CoreResR.string.dialog_action_done))
        }
    },
    dismissButton = {
        OutlinedButton(
            onClick = onDismissRequest,
            shapes = ButtonDefaults.shapes()
        ) {
            Text(stringResource(CoreResR.string.dialog_action_cancel))
        }
    },
    text = {
        textField()
    }
)

/** Defaults for [InputDialog]/[ListDialog]. */
@Stable
object DialogDefaults {
    /** The [OutlinedTextField] used in [InputDialog]. */
    @Composable
    fun TextField(
        value: String,
        onValueChange: (String) -> Unit,
        enabled: Boolean = true,
        readOnly: Boolean = false,
        label: @Composable (() -> Unit)? = null,
        placeholder: @Composable (() -> Unit)? = null,
        leadingIcon: @Composable (() -> Unit)? = null,
        showClearButton: Boolean = true,
        trailingIcon: @Composable (() -> Unit)? = null,
        supportingText: @Composable (() -> Unit)? = null,
        isError: Boolean = false,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        keyboardOptions: KeyboardOptions = KeyboardOptions(),
        keyboardActions: KeyboardActions = KeyboardActions(),
        singleLine: Boolean = false,
        maxLines: Int = Int.MAX_VALUE,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    ) = OutlinedTextField(
        value = value, onValueChange = onValueChange,
        enabled = enabled, readOnly = readOnly,
        label = label, placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = if (showClearButton && value.isNotEmpty()) ({
            IconButton(onClick = { onValueChange("") }) {
                Icon(Icons.Outlined.Clear, contentDescription = "Clear button")
            }
        }) else trailingIcon,
        supportingText = supportingText,
        isError = isError, visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions, keyboardActions = keyboardActions,
        singleLine = singleLine, maxLines = maxLines,
        interactionSource = interactionSource
    )
}
