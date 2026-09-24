package com.edricchan.studybuddy.ui.widgets.compose.form.chips

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Close
import com.edricchan.studybuddy.ui.widgets.compose.form.chips.data.ChipItem
import com.edricchan.studybuddy.ui.widgets.compose.form.chips.data.ChipTextFieldState

@Stable
object OutlinedChipTextFieldDefaults {
    @Composable
    fun <C : ChipItem> Chip(
        modifier: Modifier = Modifier,
        chip: C,
        onRemoveClick: () -> Unit
    ) {
        InputChip(
            modifier = modifier,
            onClick = onRemoveClick,
            selected = false,
            label = {
                Text(text = chip.text)
            },
            trailingIcon = {
                Icon(AppIcons.Outlined.Close, contentDescription = null)
            },
            shapes = InputChipDefaults.shapes(),
        )
    }
}

@Composable
fun <C : ChipItem> OutlinedChipTextField(
    state: ChipTextFieldState<C>,
    modifier: Modifier = Modifier,
    newChipState: TextFieldState = rememberTextFieldState(),
    chip: @Composable (chip: C, onRemoveClick: () -> Unit) -> Unit = { chip, onRemoveClick ->
        OutlinedChipTextFieldDefaults.Chip(
            chip = chip,
            onRemoveClick = onRemoveClick
        )
    },
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (TextFieldLabelScope.() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    inputTransformation: InputTransformation? = null,
    outputTransformation: OutputTransformation? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    scrollState: ScrollState = rememberScrollState(),
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val textColor =
        textStyle.color.takeOrElse {
            val focused = interactionSource.collectIsFocusedAsState().value
            colors.textColor(enabled, false, focused)
        }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    fun onSubmit() {
        state.onSubmit(newChipState.text.toString())
        if (state.chipsState.any { it.text == newChipState.text.toString() }) newChipState.clearText()
    }

    fun onRemoveLastTag() {
        state.onRemoveLast()
    }

    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        BasicTextField(
            modifier = modifier.onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                when (event.key) {
                    Key.Enter -> {
                        onSubmit()
                        true
                    }

                    Key.Backspace -> {
                        onRemoveLastTag()
                        true
                    }

                    else -> false
                }
            },
            enabled = enabled,
            readOnly = readOnly,
            state = newChipState,
            lineLimits = TextFieldLineLimits.SingleLine,
            onKeyboardAction = {
                onSubmit()
            },
            cursorBrush = SolidColor(colors.cursorColor(isError)),
            textStyle = mergedTextStyle,
            inputTransformation = inputTransformation,
            outputTransformation = outputTransformation,
            keyboardOptions = keyboardOptions,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            decorator = { innerTextField ->
                OutlinedTextFieldDefaults.decorator(
                    state = newChipState,
                    enabled = enabled,
                    label = label,
                    labelPosition = TextFieldLabelPosition.Cutout(isAlwaysMinimized = true),
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    outputTransformation = outputTransformation,
                    interactionSource = interactionSource,
                    container = {
                        OutlinedTextFieldDefaults.Container(
                            shape = shape,
                            enabled = enabled,
                            isError = isError,
                            interactionSource = interactionSource,
                        )
                    }
                ).Decoration {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        itemVerticalAlignment = Alignment.CenterVertically
                    ) {
                        state.chipsState.forEach {
                            key(it.text) {
                                chip(
                                    it, { state.onRemoveChip(it) }
                                )
                            }
                        }
                        innerTextField()
                    }
                }
            },
            scrollState = scrollState
        )
    }
}
