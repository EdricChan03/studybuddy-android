package com.edricchan.studybuddy.ui.preference.compose.twostate

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.edricchan.studybuddy.ui.preference.compose.Preference
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategoryScope


/**
 * Adds a [CheckboxPreference] to the receiver [PreferenceCategoryScope].
 *
 * This is the equivalent of the `CheckboxPreference` view from Jetpack Preference.
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
 * @see CheckboxPreference
 */
@Composable
fun PreferenceCategoryScope.CheckboxPreference(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: @Composable (() -> Unit)? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    checkboxColors: CheckboxColors = CheckboxDefaults.colors()
) = CheckboxPreference(
    modifier = modifier,
    enabled = enabled,
    icon = icon,
    title = title,
    subtitle = subtitle,
    checked = checked,
    onCheckedChange = onCheckedChange,
    checkboxColors = checkboxColors
)

/**
 * Adds a [SwitchPreference] to the receiver [PreferenceCategoryScope].
 *
 * This is the equivalent of the `SwitchPreference` view from Jetpack Preference.
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
fun PreferenceCategoryScope.SwitchPreference(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    thumbContent: (@Composable () -> Unit)? = null,
    switchColors: SwitchColors = SwitchDefaults.colors()
) = SwitchPreference(
    modifier = modifier,
    enabled = enabled,
    icon = icon,
    title = title,
    subtitle = subtitle,
    checked = checked,
    onCheckedChange = onCheckedChange,
    thumbContent = thumbContent,
    switchColors = switchColors
)
