package com.edricchan.studybuddy.utils.androidx.compose.ui.tooling.preview

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider

private val booleanValues = listOf(
    true, false
)

/**
 * [androidx.compose.ui.tooling.preview.PreviewParameterProvider] that provides a list of preset
 * [Boolean] values.
 */
class BooleanPreviewParameterProvider : CollectionPreviewParameterProvider<Boolean>(
    booleanValues
)

private val nullableBooleanValues = booleanValues + null

/**
 * [androidx.compose.ui.tooling.preview.PreviewParameterProvider] that provides a list of preset
 * [Boolean] values, including `null`.
 */
class NullableBooleanPreviewParameterProvider : CollectionPreviewParameterProvider<Boolean?>(
    nullableBooleanValues
)
