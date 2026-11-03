package com.edricchan.studybuddy.ui.design.textfield

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import com.edricchan.studybuddy.data.forms.InputValidator
import com.edricchan.studybuddy.data.forms.compose.InputValidationError
import com.edricchan.studybuddy.data.forms.compose.validationErrorAsState
import com.edricchan.studybuddy.ui.design.textfield.primitives.OutlinedTextField
import com.edricchan.studybuddy.utils.compose.material3.textfield.TextFieldAnimations

@Composable
fun <ValidationError : InputValidationError> OutlinedFormField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    validationError: ValidationError?,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: @Composable (TextFieldLabelScope.() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (AnimatedContentScope.() -> Unit)? = null,
    inputTransformation: InputTransformation? = null,
    outputTransformation: OutputTransformation? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    scrollState: ScrollState = rememberScrollState(),
    interactionSource: MutableInteractionSource? = null,
) {
    val errorSemantics = validationError?.getSemanticsMessage(state.text)

    OutlinedTextField(
        modifier = modifier.semantics {
            errorSemantics?.let(::error)
        },
        isError = validationError != null,
        state = state,
        enabled = enabled,
        readOnly = readOnly,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = {
            AnimatedContent(
                label = "Form field error supporting text",
                targetState = validationError,
                transitionSpec = TextFieldAnimations.supportingTextTransitionSpec()
            ) { type ->
                type?.let {
                    Text(text = stringResource(it.messageRes))
                } ?: supportingText?.invoke(this)
            }
        },
        inputTransformation = inputTransformation,
        outputTransformation = outputTransformation,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        lineLimits = lineLimits,
        onTextLayout = onTextLayout,
        scrollState = scrollState,
        interactionSource = interactionSource
    )
}

@Composable
fun <ValidationError : InputValidationError> OutlinedFormField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    validator: InputValidator<ValidationError>,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: @Composable (TextFieldLabelScope.() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (AnimatedContentScope.() -> Unit)? = null,
    inputTransformation: InputTransformation? = null,
    outputTransformation: OutputTransformation? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    scrollState: ScrollState = rememberScrollState(),
    interactionSource: MutableInteractionSource? = null,
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val validationError by state.validationErrorAsState(
        interactionSource = interactionSource,
        validator = validator
    )

    OutlinedFormField(
        modifier = modifier,
        state = state,
        validationError = validationError,
        enabled = enabled,
        readOnly = readOnly,
        label = label,
        supportingText = supportingText,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        inputTransformation = inputTransformation,
        outputTransformation = outputTransformation,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        lineLimits = lineLimits,
        onTextLayout = onTextLayout,
        scrollState = scrollState,
        interactionSource = interactionSource
    )
}
