package com.edricchan.studybuddy.ui.design.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Button(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shapes: ButtonShapes = ButtonDefaults.shapes(),
    contentPadding: PaddingValues = ButtonDefaults.contentPaddingFor(ButtonDefaults.MinHeight),
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        content = content,
        onClick = onClick,
        colors = colors,
        shapes = shapes,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OutlinedButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    shapes: ButtonShapes = ButtonDefaults.shapes(),
    border: BorderStroke = ButtonDefaults.outlinedButtonBorder(enabled),
    contentPadding: PaddingValues = ButtonDefaults.contentPaddingFor(ButtonDefaults.MinHeight),
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        modifier = modifier,
        enabled = enabled,
        content = content,
        onClick = onClick,
        colors = colors,
        shapes = shapes,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FilledTonalButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(),
    shapes: ButtonShapes = ButtonDefaults.shapes(),
    contentPadding: PaddingValues = ButtonDefaults.contentPaddingFor(ButtonDefaults.MinHeight),
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    FilledTonalButton(
        modifier = modifier,
        enabled = enabled,
        content = content,
        onClick = onClick,
        colors = colors,
        shapes = shapes,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TextButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    shapes: ButtonShapes = ButtonDefaults.shapes(),
    contentPadding: PaddingValues = ButtonDefaults.contentPaddingFor(ButtonDefaults.MinHeight),
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(
        modifier = modifier,
        enabled = enabled,
        content = content,
        onClick = onClick,
        colors = colors,
        shapes = shapes,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ElevatedButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.elevatedButtonColors(),
    shapes: ButtonShapes = ButtonDefaults.shapes(),
    contentPadding: PaddingValues = ButtonDefaults.contentPaddingFor(ButtonDefaults.MinHeight),
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    ElevatedButton(
        modifier = modifier,
        enabled = enabled,
        content = content,
        onClick = onClick,
        colors = colors,
        shapes = shapes,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    )
}
