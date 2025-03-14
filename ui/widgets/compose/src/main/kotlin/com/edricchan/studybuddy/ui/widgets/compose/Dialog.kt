// From https://github.com/boswelja/SmartwatchExtensions/blob/main/core/mobile/src/main/kotlin/com/boswelja/smartwatchextensions/core/ui/dialog/Dialog.kt
package com.edricchan.studybuddy.ui.widgets.compose

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.ui.widgets.compose.list.RadioButtonListItem
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
            TextButton(
                enabled = dialogSelectedItem != null,
                onClick = {
                    onItemSelectionChanged(dialogSelectedItem!!)
                    onDismissRequest()
                }
            ) {
                Text(stringResource(CoreResR.string.dialog_action_done))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(CoreResR.string.dialog_action_cancel))
            }
        },
        text = {
            Column {
                LazyColumn(modifier = Modifier.selectableGroup()) {
                    items(items) { item ->
                        RadioButtonListItem(
                            modifier = Modifier.height(48.dp),
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent
                            ),
                            selected = item == dialogSelectedItem,
                            onSelected = { dialogSelectedItem = item }
                        ) {
                            itemContent(item)
                        }
                    }
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
 * @param inputValue The current input's value.
 * @param valueValid Whether the input is valid. Defaults to [String.isNotBlank] on [inputValue]
 * if not specified.
 * @param onConfirm Invoked when the confirm button is pressed with the new input value.
 * @param textField Composable to be shown for the input text-field.
 * This should be a [DialogDefaults.TextField].
 */
@Composable
fun InputDialog(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)?,
    icon: @Composable (() -> Unit)?,
    onDismissRequest: () -> Unit,
    inputValue: String,
    valueValid: Boolean = inputValue.isNotBlank(),
    onConfirm: (String) -> Unit,
    textField: @Composable () -> Unit
) = AlertDialog(
    modifier = modifier,
    title = title,
    icon = icon,
    onDismissRequest = onDismissRequest,
    confirmButton = {
        TextButton(
            enabled = valueValid,
            onClick = {
                onConfirm(inputValue)
                onDismissRequest()
            }
        ) {
            Text(stringResource(CoreResR.string.dialog_action_done))
        }
    },
    dismissButton = {
        TextButton(onClick = onDismissRequest) {
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
