package com.edricchan.studybuddy.ui.design.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit
) {
    IconButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        colors = colors,
        shapes = IconButtonDefaults.shapes(),
        interactionSource = interactionSource,
        content = content
    )
}

@Composable
fun FilledIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    colors: IconButtonColors = IconButtonDefaults.filledIconButtonColors(),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit
) {
    FilledIconButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        colors = colors,
        shapes = IconButtonDefaults.shapes(),
        interactionSource = interactionSource,
        content = content
    )
}

@Composable
fun FilledTonalIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    colors: IconButtonColors = IconButtonDefaults.filledTonalIconButtonColors(),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit
) {
    FilledTonalIconButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        colors = colors,
        shapes = IconButtonDefaults.shapes(),
        interactionSource = interactionSource,
        content = content
    )
}

@Composable
fun OutlinedIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    colors: IconButtonColors = IconButtonDefaults.outlinedIconButtonColors(),
    border: BorderStroke = IconButtonDefaults.outlinedIconButtonBorder(enabled),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit
) {
    OutlinedIconButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        colors = colors,
        border = border,
        shapes = IconButtonDefaults.shapes(),
        interactionSource = interactionSource,
        content = content
    )
}
