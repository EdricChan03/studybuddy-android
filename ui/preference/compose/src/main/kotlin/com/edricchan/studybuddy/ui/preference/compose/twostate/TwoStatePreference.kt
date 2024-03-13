package com.edricchan.studybuddy.ui.preference.compose.twostate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.edricchan.studybuddy.ui.preference.compose.Preference
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

/** Test tag for the [SwitchPreference]'s [Switch] action. */
const val ActionSwitchTestTag = "SwitchPreference:SwitchAction"

/**
 * A [Preference] which displays a [Switch] as its `action`.
 *
 * This is the equivalent of the `SwitchPreference` view from Jetpack Preference.
 * @param enabled Whether this preference is enabled. If disabled,
 * [onCheckedChange] will not be called.
 * @param icon [Composable] to be displayed at the start. This should be an
 * [androidx.compose.material3.Icon].
 * @param title [Composable] to be displayed in the middle. This should contain
 * the title of the preference.
 * @param subtitle (Optional) [Composable] to be displayed before the title. This
 * should contain additional information.
 * @param checked Whether the preference is checked.
 * @param onCheckedChange Lambda that is invoked when the preference's checked state
 * has changed (usually when the user clicks on the preference), with the new `checked` state
 * passed as a parameter.
 * @param thumbContent The [Switch]'s thumb [Composable] to render.
 * @param switchColors [SwitchColors] to use for the [Switch].
 * @see Preference
 * @see CheckboxPreference
 */
@Composable
fun SwitchPreference(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    thumbContent: (@Composable () -> Unit)? = null,
    switchColors: SwitchColors = SwitchDefaults.colors()
) = Preference(
    modifier = modifier.toggleable(
        enabled = enabled,
        value = checked,
        onValueChange = onCheckedChange
    ),
    enabled = enabled,
    icon = icon,
    title = title,
    subtitle = subtitle,
    action = {
        Switch(
            modifier = Modifier.testTag(ActionSwitchTestTag),
            enabled = enabled,
            checked = checked,
            onCheckedChange = null, // Prevent the switch from being clicked
            thumbContent = thumbContent,
            colors = switchColors
        )
    }
)

/** Test tag for the [CheckboxPreference]'s [Checkbox] action. */
const val ActionCheckboxTestTag = "CheckboxPreference:CheckboxAction"

/**
 * A [Preference] which displays a [Checkbox] as its `action`.
 *
 * This is the equivalent of the `CheckboxPreference` view from Jetpack Preference.
 * @param enabled Whether this preference is enabled. If disabled,
 * [onCheckedChange] will not be called.
 * @param icon [Composable] to be displayed at the start. This should be an
 * [androidx.compose.material3.Icon].
 * @param title [Composable] to be displayed in the middle. This should contain
 * the title of the preference.
 * @param subtitle (Optional) [Composable] to be displayed before the title. This
 * should contain additional information.
 * @param checked Whether the preference is checked.
 * @param onCheckedChange Lambda that is invoked when the preference's checked state
 * has changed (usually when the user clicks on the preference), with the new `checked` state
 * passed as a parameter.
 * @param checkboxColors [CheckboxColors] to use for the [Checkbox].
 * @see Preference
 * @see SwitchPreference
 */
@Composable
fun CheckboxPreference(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    checkboxColors: CheckboxColors = CheckboxDefaults.colors()
) = Preference(
    modifier = modifier.toggleable(
        enabled = enabled,
        value = checked,
        onValueChange = onCheckedChange
    ),
    enabled = enabled,
    icon = icon,
    title = title,
    subtitle = subtitle,
    action = {
        Checkbox(
            modifier = Modifier.testTag(ActionCheckboxTestTag),
            enabled = enabled,
            checked = checked,
            onCheckedChange = null, // Prevent the checkbox from being clicked
            colors = checkboxColors
        )
    }
)

private class BooleanPreviewParameterProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(false, true)
}

@Preview
@Composable
private fun SwitchPreferencePreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class)
    enabled: Boolean
) {
    val (checked, onChecked) = remember { mutableStateOf(false) }
    StudyBuddyTheme {
        Column {
            SwitchPreference(
                enabled = enabled,
                title = { Text(text = "Switch preference title") },
                checked = checked,
                onCheckedChange = onChecked
            )
            SwitchPreference(
                enabled = enabled,
                title = { Text(text = "Switch preference title") },
                subtitle = { Text(text = "Switch preference subtitle") },
                checked = !checked,
                onCheckedChange = onChecked
            )
            SwitchPreference(
                enabled = enabled,
                title = { Text(text = "Switch preference title") },
                subtitle = { Text(text = "Switch preference subtitle") },
                icon = {
                    Icon(
                        Icons.Outlined.Settings,
                        contentDescription = "Icon content description"
                    )
                },
                checked = checked,
                onCheckedChange = onChecked
            )
        }
    }
}

@Preview
@Composable
private fun CheckboxPreferencePreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class)
    enabled: Boolean
) {
    val (checked, onChecked) = remember { mutableStateOf(false) }
    StudyBuddyTheme {
        Column {
            CheckboxPreference(
                enabled = enabled,
                title = { Text(text = "Checkbox preference title") },
                checked = checked,
                onCheckedChange = onChecked
            )
            CheckboxPreference(
                enabled = enabled,
                title = { Text(text = "Checkbox preference title") },
                subtitle = { Text(text = "Checkbox preference subtitle") },
                checked = !checked,
                onCheckedChange = onChecked
            )
            CheckboxPreference(
                enabled = enabled,
                title = { Text(text = "Checkbox preference title") },
                subtitle = { Text(text = "Checkbox preference subtitle") },
                icon = {
                    Icon(
                        Icons.Outlined.Settings,
                        contentDescription = "Icon content description"
                    )
                },
                checked = checked,
                onCheckedChange = onChecked
            )
        }
    }
}

