package com.edricchan.studybuddy.ui.preference.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.ui.widgets.compose.list.withDisabledColors

/** Test tag for the [Preference]'s `actionDivider` composable. */
const val ActionDividerTestTag = "Preference:ActionDivider"

/**
 * Simple UI composable to display a setting.
 *
 * Such preferences should ideally follow
 * [AOSP's settings design guidelines](https://source.android.com/docs/core/settings/settings-guidelines).
 *
 * Variants should be used where preferable:
 * * [CheckboxPreference] for displaying a preference with a
 * [androidx.compose.material3.Checkbox], representing a checkable state.
 * * [SwitchPreference] for displaying a preference with a
 * [androidx.compose.material3.Switch], representing a checkable state.
 * * [MainSwitchBar] if a root switch preference bar should be used,
 * [emulating AOSP's master setting](https://source.android.com/docs/core/settings/settings-guidelines#master_setting).
 * * [PreferenceCategory] for grouping a list of [Preference]s.
 * @param enabled Whether this preference is enabled. If disabled, clickable behaviours
 * should not have any effect.
 * @param icon [Composable] to be displayed at the start. This should be an
 * [androidx.compose.material3.Icon].
 * @param title [Composable] to be displayed in the middle. This should contain the
 * title of the preference.
 * @param subtitle (Optional) [Composable] to be displayed before the [title].
 * This should contain additional information.
 * @param action (Optional) [Composable] to be displayed at the end. An [showActionDivider]
 * can be optionally shown between it and its contents.
 * @param showActionDivider Whether a vertical divider should be shown between the [action]
 * and its contents.
 * @see CheckboxPreference
 * @see SwitchPreference
 * @see PreferenceCategory
 * @see MainSwitchBar
 */
@Composable
fun Preference(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    showActionDivider: Boolean = false
) = ListItem(
    modifier = modifier,
    colors = ListItemDefaults.colors().withDisabledColors(enabled),
    headlineContent = title,
    supportingContent = subtitle,
    leadingContent = icon,
    trailingContent = action?.let {
        {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (showActionDivider) {
                    val color = DividerDefaults.color.copy(
                        alpha = if (enabled) {
                            1f
                        } else {
                            0.6f
                        },
                    )
                    VerticalDivider(
                        color = color,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxHeight()
                            .width(1.dp)
                            .testTag(ActionDividerTestTag),
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }
                action()
            }
        }
    },
)

/**
 * Simple UI composable to display a setting.
 *
 * Such preferences should ideally follow
 * [AOSP's settings design guidelines](https://source.android.com/docs/core/settings/settings-guidelines).
 *
 * Variants should be used where preferable:
 * * [CheckboxPreference] for displaying a preference with a
 * [androidx.compose.material3.Checkbox], representing a checkable state.
 * * [SwitchPreference] for displaying a preference with a
 * [androidx.compose.material3.Switch], representing a checkable state.
 * * [MainSwitchBar] if a root switch preference bar should be used,
 * [emulating AOSP's master setting](https://source.android.com/docs/core/settings/settings-guidelines#master_setting).
 * * [PreferenceCategory] for grouping a list of [Preference]s.
 * @param enabled Whether this preference is enabled. If disabled, [onClick] will
 * not be invoked if the preference is clicked on.
 * @param onClick Lambda that is invoked when this preference is clicked on.
 * @param icon [Composable] to be displayed at the start. This should be an
 * [androidx.compose.material3.Icon].
 * @param title [Composable] to be displayed in the middle. This should contain the
 * title of the preference.
 * @param subtitle (Optional) [Composable] to be displayed before the [title].
 * This should contain additional information.
 * @param action (Optional) [Composable] to be displayed at the end. An [showActionDivider]
 * can be optionally shown between it and its contents.
 * @param showActionDivider Whether a vertical divider should be shown between the [action]
 * and its contents.
 * @see CheckboxPreference
 * @see SwitchPreference
 * @see PreferenceCategory
 * @see MainSwitchBar
 */
@Composable
fun Preference(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    icon: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    showActionDivider: Boolean = false
) = Preference(
    modifier = modifier.clickable(enabled = enabled, onClick = onClick),
    enabled = enabled,
    icon = icon,
    title = title,
    subtitle = subtitle,
    action = action,
    showActionDivider = showActionDivider
)
