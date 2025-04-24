package com.edricchan.studybuddy.ui.preference.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.edricchan.studybuddy.ui.preference.compose.dialog.DialogPreferenceScope
import com.edricchan.studybuddy.ui.preference.compose.dialog.DialogPreferenceScopeImpl
import com.edricchan.studybuddy.ui.widgets.compose.InputDialog
import com.edricchan.studybuddy.ui.widgets.compose.ListDialog

/**
 * [Preference] which allows the user to select from a list of values offered in a [ListDialog].
 * @param T The value's type.
 * @param modifier A [Modifier] to apply to the setting item.
 * @param dialogModifier A [Modifier] to apply to the dialog.
 * @param title [Composable] to be displayed in the middle. This should contain the
 * title of the preference, which will be applied to the [Preference].
 * @param subtitle [Composable] to be displayed below the [title]. This will only be
 * applied to the preference.
 * @param icon [Composable] to be displayed on the left of this preference.
 * @param iconSpaceReserved Whether an additional horizontal padding of `56.dp`
 * should be added to the [title] composable and its contents.
 * @param dialogIcon [Composable] to be used for the [ListDialog]'s `icon`. Defaults to
 * the preference's [icon] if not explicitly set.
 * @param dialogTitle [Composable] to be used for the [ListDialog]'s `title`. Defaults to
 * the preference's [title] if not explicitly set.
 * @param enabled Whether this preference is clickable.
 * @param values The list of values the user can choose from.
 * @param value The current value.
 * @param onValueChanged Called when the current value changes.
 * @param valueLabel [Composable] to be displayed for each `value` in the [ListDialog].
 * The value to render will be passed as the parameter.
 * @see Preference
 * @see ListDialog
 */
@Composable
fun <T> ListDialogPreference(
    modifier: Modifier = Modifier,
    dialogModifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    subtitle: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    iconSpaceReserved: Boolean = true,
    dialogIcon: @Composable (() -> Unit)? = null,
    dialogTitle: @Composable () -> Unit = title,
    enabled: Boolean = true,
    values: List<T>,
    value: T,
    onValueChanged: (T) -> Unit,
    valueLabel: @Composable (T) -> Unit
) {
    var dialogVisible by rememberSaveable { mutableStateOf(false) }
    Preference(
        modifier = modifier,
        title = title,
        subtitle = subtitle,
        icon = icon,
        iconSpaceReserved = iconSpaceReserved,
        onClick = { dialogVisible = true },
        enabled = enabled
    )
    if (dialogVisible) {
        ListDialog(
            modifier = dialogModifier,
            title = dialogTitle,
            icon = dialogIcon,
            onDismissRequest = { dialogVisible = false },
            itemContent = valueLabel,
            items = values,
            selectedItem = value,
            onItemSelectionChanged = onValueChanged
        )
    }
}

/**
 * [Preference] which allows the user to input a value via an [InputDialog].
 * @param modifier The [Modifier] to be applied to the settings item.
 * @param dialogModifier The [Modifier] to be applied to the dialog.
 * @param title [Composable] to be displayed in the middle. This should contain the
 * title of the preference, which will be applied to the [Preference].
 * @param subtitle [Composable] to be displayed below the [title]. This will only be
 * applied to the preference.
 * @param icon [Composable] to be displayed on the left of this preference.
 * @param iconSpaceReserved Whether an additional horizontal padding of `56.dp`
 * should be added to the [title] composable and its contents.
 * @param enabled Whether the preference is enabled.
 * @param dialogIcon [Composable] to be used for the [InputDialog]'s `icon`. Defaults to
 * the preference's [icon] if not explicitly set.
 * @param dialogTitle [Composable] to be used for the [InputDialog]'s `title`. Defaults to
 * the preference's [title] if not explicitly set.
 * @param onConfirm Method that is invoked when the dialog's confirm button is pressed.
 * @param isValid Whether the dialog's confirm button should be enabled.
 * @param dialogTextField Contents of the [InputDialog]. This should preferably be an
 * [androidx.compose.material3.OutlinedTextField] or similar.
 * @see Preference
 * @see InputDialog
 */
@Composable
fun InputDialogPreference(
    modifier: Modifier = Modifier,
    dialogModifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    subtitle: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    iconSpaceReserved: Boolean = true,
    enabled: Boolean = true,
    dialogIcon: @Composable (() -> Unit)? = icon,
    dialogTitle: @Composable () -> Unit = title,
    onConfirm: DialogPreferenceScope.() -> Unit,
    isValid: Boolean = true,
    dialogTextField: @Composable DialogPreferenceScope.() -> Unit
) {
    var dialogVisible by rememberSaveable { mutableStateOf(false) }

    val dialogScope =
        remember { DialogPreferenceScopeImpl(onDismissRequest = { dialogVisible = false }) }

    Preference(
        modifier = modifier,
        enabled = enabled,
        title = title,
        subtitle = subtitle,
        icon = icon,
        iconSpaceReserved = iconSpaceReserved,
        onClick = { dialogVisible = true }
    )

    if (dialogVisible) {
        InputDialog(
            modifier = dialogModifier,
            title = dialogTitle,
            icon = dialogIcon,
            onConfirm = { dialogScope.onConfirm() },
            isValid = isValid,
            onDismissRequest = { dialogVisible = false },
            textField = { dialogScope.dialogTextField() }
        )
    }
}

/**
 * [Preference] which allows the user to select from a list of values offered in a [ListDialog],
 * to be added to the receiver [PreferenceCategoryScope].
 * @param T The value's type.
 * @param modifier A [Modifier] to apply to the setting item.
 * @param dialogModifier A [Modifier] to apply to the dialog.
 * @param title [Composable] to be displayed in the middle. This should contain the
 * title of the preference, which will be applied to the [Preference].
 * @param subtitle [Composable] to be displayed below the [title]. This will only be
 * applied to the preference.
 * @param icon [Composable] to be displayed on the left of this preference.
 * @param iconSpaceReserved Whether an additional horizontal padding of `56.dp`
 * should be added to the [title] composable and its contents.
 * @param dialogIcon [Composable] to be used for the [ListDialog]'s `icon`. Defaults to
 * the preference's [icon] if not explicitly set.
 * @param dialogTitle [Composable] to be used for the [ListDialog]'s `title`. Defaults to
 * the preference's [title] if not explicitly set.
 * @param values The list of values the user can choose from.
 * @param value The current value.
 * @param onValueChanged Called when the current value changes.
 * @param valueLabel [Composable] to be displayed for each `value` in the [ListDialog].
 * The value to render will be passed as the parameter.
 * @see Preference
 * @see ListDialog
 */
@Composable
fun <T> PreferenceCategoryScope.ListDialogPreference(
    modifier: Modifier = Modifier,
    dialogModifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    subtitle: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    iconSpaceReserved: Boolean = this.iconSpaceReserved,
    dialogIcon: @Composable (() -> Unit)? = null,
    dialogTitle: @Composable () -> Unit = title,
    values: List<T>,
    value: T,
    onValueChanged: (T) -> Unit,
    valueLabel: @Composable (T) -> Unit
) = ListDialogPreference(
    modifier = modifier,
    dialogModifier = dialogModifier,
    title = title,
    subtitle = subtitle,
    icon = icon,
    iconSpaceReserved = iconSpaceReserved,
    dialogIcon = dialogIcon,
    dialogTitle = dialogTitle,
    enabled = enabled,
    values = values,
    value = value,
    onValueChanged = onValueChanged,
    valueLabel = valueLabel
)

/**
 * [Preference] which allows the user to input a value via an [InputDialog], to be
 * added to the receiver [PreferenceCategoryScope].
 * @param modifier The [Modifier] to be applied to the settings item.
 * @param dialogModifier The [Modifier] to be applied to the dialog.
 * @param title [Composable] to be displayed in the middle. This should contain the
 * title of the preference, which will be applied to the [Preference].
 * @param subtitle [Composable] to be displayed below the [title]. This will only be
 * applied to the preference.
 * @param icon [Composable] to be displayed on the left of this preference.
 * @param iconSpaceReserved Whether an additional horizontal padding of `56.dp`
 * should be added to the [title] composable and its contents.
 * @param dialogIcon [Composable] to be used for the [InputDialog]'s `icon`. Defaults to
 * the preference's [icon] if not explicitly set.
 * @param dialogTitle [Composable] to be used for the [InputDialog]'s `title`. Defaults
 * to the preference's [title] if not explicitly set.
 * @param onConfirm Method that is invoked when the confirm button is pressed.
 * @param isValid Whether the dialog's confirm button should be enabled.
 * @param dialogTextField Contents of the [InputDialog]. This should preferably be an
 * [androidx.compose.material3.OutlinedTextField] or similar.
 * @param
 * @see Preference
 * @see InputDialog
 */
@Composable
fun PreferenceCategoryScope.InputDialogPreference(
    modifier: Modifier = Modifier,
    dialogModifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    subtitle: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    iconSpaceReserved: Boolean = this.iconSpaceReserved,
    dialogIcon: @Composable (() -> Unit)? = icon,
    dialogTitle: @Composable () -> Unit = title,
    onConfirm: DialogPreferenceScope.() -> Unit,
    isValid: Boolean = true,
    dialogTextField: @Composable DialogPreferenceScope.() -> Unit
) = InputDialogPreference(
    modifier = modifier,
    dialogModifier = dialogModifier,
    title = title,
    subtitle = subtitle,
    icon = icon,
    iconSpaceReserved = iconSpaceReserved,
    dialogIcon = dialogIcon,
    dialogTitle = dialogTitle,
    onConfirm = onConfirm,
    isValid = isValid,
    enabled = enabled,
    dialogTextField = dialogTextField
)
