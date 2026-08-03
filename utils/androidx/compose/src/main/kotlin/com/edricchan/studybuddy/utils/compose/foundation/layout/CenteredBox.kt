package com.edricchan.studybuddy.utils.compose.foundation.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * [Box] where its [content] is centered.
 *
 * This can be used for showing empty state content or other content that should be centered.
 *
 * Note that no padding is applied to the [content] - this must be applied manually with the
 * applicable [Modifier]s such as [androidx.compose.foundation.layout.padding].
 * @param shouldFillMaxSize Whether the [fillMaxSize] modifier should be applied to this [Box].
 * If set to `true`, the [fillMaxSize] modifier is applied first, followed by the rest of the
 * specified [modifiers][modifier].
 */
@Composable
fun CenteredBox(
    modifier: Modifier = Modifier,
    shouldFillMaxSize: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) = Box(
    modifier = if (shouldFillMaxSize) Modifier
        .fillMaxSize()
        .then(modifier) else modifier,
    contentAlignment = Alignment.Center,
    content = content
)
