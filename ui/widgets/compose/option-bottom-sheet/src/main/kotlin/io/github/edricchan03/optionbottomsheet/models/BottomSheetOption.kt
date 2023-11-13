package io.github.edricchan03.optionbottomsheet.models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/**
 * Represents an option in a modal bottom sheet.
 * @property id Unique ID to be used for this option.
 * @property title The title of the item.
 * @property icon A composable that displays an icon.
 * @property onClick Lambda to be invoked when the option is clicked on.
 * @property visible Whether the item is visible.
 * @property enabled Whether the item is enabled.
 */
@Immutable
data class BottomSheetOption(
    val id: Int = Int.MIN_VALUE,
    val title: String,
    val icon: (@Composable () -> Unit)? = null,
    val onClick: () -> Unit = {},
    val visible: Boolean = true,
    val enabled: Boolean = true
)
