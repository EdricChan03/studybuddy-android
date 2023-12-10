package io.github.edricchan03.optionbottomsheet.models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import io.github.edricchan03.optionbottomsheet.models.BottomSheetOptionGroup.CheckableBehavior

/**
 * Represents an option in a modal bottom sheet.
 * @property id Unique ID to be used for this option.
 * @property title The title of the item.
 * @property icon A composable that displays an icon.
 * @property onClick Lambda to be invoked when the option is clicked on.
 * @property visible Whether the item is visible.
 * @property enabled Whether the item is enabled.
 * @property requestDismissOnClick Whether a request should be sent to dismiss the
 * modal bottom sheet when this item is clicked.
 *
 * **Note:** This option has no effect if the [BottomSheetOptionGroup] this option is in
 * has a [BottomSheetOptionGroup.checkableBehavior] set to either of
 * [CheckableBehavior.Single] or [CheckableBehavior.All].
 */
@Immutable
data class BottomSheetOption(
    val id: Int = Int.MIN_VALUE,
    val title: String,
    val icon: (@Composable () -> Unit)? = null,
    val onClick: () -> Unit = {},
    val visible: Boolean = true,
    val enabled: Boolean = true,
    val requestDismissOnClick: Boolean = true
)
