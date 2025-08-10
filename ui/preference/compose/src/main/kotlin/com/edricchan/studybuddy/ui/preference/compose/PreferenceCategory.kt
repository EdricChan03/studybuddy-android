package com.edricchan.studybuddy.ui.preference.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.ui.preference.compose.twostate.CheckboxPreference
import com.edricchan.studybuddy.ui.preference.compose.twostate.SwitchPreference

private fun Modifier.categoryPadding(withPadding: Boolean) =
    if (withPadding) padding(horizontal = 56.dp) else this

/** Desired spacing between items in a [PreferenceCategory]. */
val PreferenceCategoryItemsSpacing: Dp = 2.dp

/**
 * Scope for a [PreferenceCategory]. Currently, it provides access to whether
 * the category is [enabled], in addition to properties from [ColumnScope].
 */
@LayoutScopeMarker
@Immutable
interface PreferenceCategoryScope : ColumnScope {
    /** Whether this category is enabled. Its children should be tied to this state. */
    val enabled: Boolean

    /**
     * Whether there should be a starting horizontal padding if an `icon` is not
     * specified.
     */
    val iconSpaceReserved: Boolean
}

internal class PreferenceCategoryScopeImpl(
    override val enabled: Boolean,
    override val iconSpaceReserved: Boolean,
    private val columnScope: ColumnScope
) : PreferenceCategoryScope, ColumnScope by columnScope

/**
 * Simple UI composable to display a setting to be added to the receiver
 * [PreferenceCategoryScope].
 *
 * Such preferences should ideally follow
 * [AOSP's settings design guidelines](https://source.android.com/docs/core/settings/settings-guidelines).
 *
 * Variants should be used where preferable:
 * * [CheckboxPreference] for displaying a preference with a
 * [androidx.compose.material3.Checkbox], representing a checkable state.
 * * [SwitchPreference] for displaying a preference with a
 * [androidx.compose.material3.Switch], representing a checkable state.
 * * [ListDialogPreference] for displaying a list of options in a dialog when clicked.
 * * [InputDialogPreference] for displaying an input dialog when clicked.
 * * [MainSwitchBar] if a root switch preference bar should be used,
 * [emulating AOSP's master setting](https://source.android.com/docs/core/settings/settings-guidelines#master_setting).
 * * [PreferenceCategory] for grouping a list of [Preference]s.
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
 * @param shape Desired [Shape] to clip the content with.
 * @param colors Desired colours to be used for this preference. See [PreferenceColors].
 * @see CheckboxPreference
 * @see SwitchPreference
 * @see InputDialogPreference
 * @see ListDialogPreference
 * @see PreferenceCategory
 * @see MainSwitchBar
 */
@Composable
fun PreferenceCategoryScope.Preference(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    iconSpaceReserved: Boolean = this.iconSpaceReserved,
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    showActionDivider: Boolean = false,
    shape: Shape = PreferenceDefaults.categoryItemShape,
    colors: PreferenceColors = PreferenceDefaults.colors()
) = Preference(
    modifier = modifier,
    enabled = enabled,
    icon = icon,
    iconSpaceReserved = iconSpaceReserved,
    title = title,
    subtitle = subtitle,
    action = action,
    showActionDivider = showActionDivider,
    shape = shape,
    colors = colors
)

/**
 * Simple UI composable to display a setting to be added to the receiver
 * [PreferenceCategoryScope].
 *
 * Such preferences should ideally follow
 * [AOSP's settings design guidelines](https://source.android.com/docs/core/settings/settings-guidelines).
 *
 * Variants should be used where preferable:
 * * [CheckboxPreference] for displaying a preference with a
 * [androidx.compose.material3.Checkbox], representing a checkable state.
 * * [SwitchPreference] for displaying a preference with a
 * [androidx.compose.material3.Switch], representing a checkable state.
 * * [ListDialogPreference] for displaying a list of options in a dialog when clicked.
 * * [InputDialogPreference] for displaying an input dialog when clicked.
 * * [MainSwitchBar] if a root switch preference bar should be used,
 * [emulating AOSP's master setting](https://source.android.com/docs/core/settings/settings-guidelines#master_setting).
 * * [PreferenceCategory] for grouping a list of [Preference]s.
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
 * @param shape Desired [Shape] to clip the content with.
 * @param colors Desired colours to be used for this preference. See [PreferenceColors].
 * @see CheckboxPreference
 * @see SwitchPreference
 * @see InputDialogPreference
 * @see ListDialogPreference
 * @see PreferenceCategory
 * @see MainSwitchBar
 */
@Composable
fun PreferenceCategoryScope.Preference(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: (@Composable () -> Unit)? = null,
    iconSpaceReserved: Boolean = this.iconSpaceReserved,
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    showActionDivider: Boolean = false,
    shape: Shape = PreferenceDefaults.categoryItemShape,
    colors: PreferenceColors = PreferenceDefaults.colors()
) = Preference(
    modifier = modifier,
    enabled = enabled,
    onClick = onClick,
    icon = icon,
    iconSpaceReserved = iconSpaceReserved,
    title = title,
    subtitle = subtitle,
    action = action,
    showActionDivider = showActionDivider,
    shape = shape,
    colors = colors
)

/**
 * Wrapper [Composable] for a [PreferenceCategory]'s title.
 *
 * This composable is exposed for reusability outside of a [PreferenceCategory].
 * @param title Title [Composable] to be used.
 * @param withPadding Whether a horizontal padding of `56.dp` should be
 * added to the layout.
 */
@Composable
fun PreferenceCategoryTitle(
    title: @Composable () -> Unit,
    withPadding: Boolean = true
) = Box(
    modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .categoryPadding(withPadding = withPadding),
    contentAlignment = Alignment.CenterStart
) {
    val primary = MaterialTheme.colorScheme.primary
    val titleStyle = MaterialTheme.typography.labelLarge.copy(color = primary)
    ProvideTextStyle(value = titleStyle, content = title)
}

/**
 * UI composable which groups the specified [content].
 *
 * Although there are no strict requirements on what [content] should include,
 * it should ideally be a [Preference] or its variants ([ListDialogPreference],
 * [InputDialogPreference], [CheckboxPreference] or [SwitchPreference]).
 * @param title [Composable] for the category's title.
 * @param iconSpaceReserved Whether an additional horizontal padding of `56.dp`
 * should be added to the [title] composable and its contents.
 * @param enabled Whether the contents of this category should be enabled.
 * @param listShape [Shape] to be applied on the category's list items.
 * @param content Content to be displayed.
 * @see PreferenceCategoryScope
 */
@Composable
fun PreferenceCategory(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)? = null,
    iconSpaceReserved: Boolean = true,
    enabled: Boolean = true,
    listShape: Shape = PreferenceDefaults.itemShape,
    content: @Composable PreferenceCategoryScope.() -> Unit,
) {
    val scope: (ColumnScope) -> PreferenceCategoryScopeImpl = remember {
        { PreferenceCategoryScopeImpl(enabled, iconSpaceReserved, it) }
    }
    Surface {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            title?.let { PreferenceCategoryTitle(title = it, withPadding = iconSpaceReserved) }
            Surface(shape = listShape) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(PreferenceCategoryItemsSpacing)
                ) {
                    scope(this).content()
                }
            }
        }
    }
}
