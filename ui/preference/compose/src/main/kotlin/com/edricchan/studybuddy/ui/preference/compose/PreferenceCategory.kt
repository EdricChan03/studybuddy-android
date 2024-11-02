package com.edricchan.studybuddy.ui.preference.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private fun Modifier.categoryPadding(withPadding: Boolean) =
    if (withPadding) padding(horizontal = 56.dp) else this

/**
 * Scope for a [PreferenceCategory]. Currently, it provides access to whether
 * the category is [enabled], in addition to properties from [ColumnScope].
 */
@Stable
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
    ProvideTextStyle(value = titleStyle) { title() }
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
 * @param content Content to be displayed.
 * @see PreferenceCategoryScope
 */
@Composable
fun PreferenceCategory(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)? = null,
    iconSpaceReserved: Boolean = true,
    enabled: Boolean = true,
    content: @Composable PreferenceCategoryScope.() -> Unit,
) {
    val scope: (ColumnScope) -> PreferenceCategoryScopeImpl = remember {
        { PreferenceCategoryScopeImpl(enabled, iconSpaceReserved, it) }
    }
    Surface {
        Column(
            modifier = modifier.fillMaxWidth(),
        ) {
            title?.let { PreferenceCategoryTitle(title = it, withPadding = iconSpaceReserved) }
            scope(this).content()
        }
    }
}
