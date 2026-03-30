package com.edricchan.studybuddy.utils.androidx.compose.ui.tooling.preview

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider

private val booleanValues = listOf(
    false, true
)

/**
 * [androidx.compose.ui.tooling.preview.PreviewParameterProvider] that provides a list of preset
 * [Boolean] values.
 */
open class BooleanPreviewParameterProvider : CollectionPreviewParameterProvider<Boolean>(
    booleanValues
) {
    /**
     * Returns the display name for the given [value].
     * @see androidx.compose.ui.tooling.preview.PreviewParameterProvider.getDisplayName
     */
    open fun getDisplayName(value: Boolean): String? = null

    final override fun getDisplayName(index: Int): String? {
        return getDisplayName(value = booleanValues[index])
    }
}

private val nullableBooleanValues = booleanValues + null

/**
 * [androidx.compose.ui.tooling.preview.PreviewParameterProvider] that provides a list of preset
 * [Boolean] values, including `null`.
 */
open class NullableBooleanPreviewParameterProvider : CollectionPreviewParameterProvider<Boolean?>(
    nullableBooleanValues
) {
    /**
     * Returns the display name for the given [value].
     * @see androidx.compose.ui.tooling.preview.PreviewParameterProvider.getDisplayName
     */
    open fun getDisplayName(value: Boolean?): String? = null

    final override fun getDisplayName(index: Int): String? {
        return getDisplayName(value = nullableBooleanValues[index])
    }
}
