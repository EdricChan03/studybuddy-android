package com.edricchan.studybuddy.features.auth.common.ui.fields

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.autofill.contentType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.animated.VisibilityToggle
import com.edricchan.studybuddy.core.resources.icons.outlined.Password
import com.edricchan.studybuddy.data.forms.InputValidator
import com.edricchan.studybuddy.data.forms.compose.InputValidationError
import com.edricchan.studybuddy.data.forms.compose.validationErrorAsState
import com.edricchan.studybuddy.features.auth.R
import com.edricchan.studybuddy.utils.compose.material3.textfield.TextFieldAnimations
import com.edricchan.studybuddy.core.resources.R as CoreResR

@Composable
private fun getVisibilityText(isVisible: Boolean): String {
    return stringResource(
        if (isVisible) R.string.password_toggle_visibility_action_hide_text
        else R.string.password_toggle_visibility_action_show_text
    )
}

@Composable
fun VisibilityIcon(isVisible: Boolean) {
    val animatedVector = AppIcons.Outlined.VisibilityToggle(isVisible)

    Icon(
        painter = rememberAnimatedVectorPainter(animatedVector, true),
        contentDescription = getVisibilityText(isVisible)
    )
}

@Stable
object PasswordTextFieldDefaults {
    val contentType: ContentType = ContentType.Password
    val Label: @Composable (TextFieldLabelScope.() -> Unit) = {
        Text(text = stringResource(R.string.account_password_field_label))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    contentType: ContentType = PasswordTextFieldDefaults.contentType,
    state: TextFieldState,
    enabled: Boolean = true,
    label: @Composable (TextFieldLabelScope.() -> Unit)? = PasswordTextFieldDefaults.Label,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean,
    interactionSource: MutableInteractionSource? = null
) {
    var isTextObfuscated by rememberSaveable { mutableStateOf(false) }
    val obfuscatedStateDesc = stringResource(
        if (isTextObfuscated) R.string.password_toggle_visibility_state_desc_hidden
        else R.string.password_toggle_visibility_state_desc_shown
    )

    val tooltipState = rememberTooltipState()

    OutlinedSecureTextField(
        modifier = modifier.contentType(contentType),
        state = state,
        enabled = enabled,
        label = label,
        leadingIcon = {
            Icon(AppIcons.Outlined.Password, contentDescription = null)
        },
        trailingIcon = {
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                    TooltipAnchorPosition.Below
                ),
                state = tooltipState,
                tooltip = {
                    PlainTooltip {
                        Text(text = getVisibilityText(isTextObfuscated))
                    }
                }
            ) {
                FilledTonalIconToggleButton(
                    modifier = Modifier.semantics {
                        stateDescription = obfuscatedStateDesc
                    },
                    shapes = IconButtonDefaults.toggleableShapes(),
                    checked = isTextObfuscated,
                    onCheckedChange = { isTextObfuscated = it }
                ) {
                    VisibilityIcon(isVisible = isTextObfuscated)
                }
            }
        },
        supportingText = supportingText,
        isError = isError,
        shape = OutlinedTextFieldDefaults.roundedShape,
        colors = OutlinedTextFieldDefaults.tonalColors(),
        labelPosition = TextFieldLabelPosition.Inside(),
        textObfuscationMode = if (isTextObfuscated) TextObfuscationMode.Visible else TextObfuscationMode.RevealLastTyped,
        interactionSource = interactionSource
    )
}

const val PasswordMinLength = 6
const val PasswordMaxLength = 4096

enum class PasswordValidationError(
    @field:StringRes override val messageRes: Int,
    @field:StringRes override val semanticsMessageRes: Int = messageRes,
) : InputValidationError {
    Required(messageRes = CoreResR.string.text_field_error_required),
    MinimumRequired(
        messageRes = CoreResR.string.text_field_error_min_limit_required,
        semanticsMessageRes = CoreResR.string.text_field_error_semantics_min_limit_required
    ) {
        @Composable
        override fun getMessage(input: CharSequence): String =
            stringResource(messageRes, PasswordMinLength)

        @Composable
        override fun getSemanticsMessage(input: CharSequence): String =
            stringResource(messageRes, PasswordMinLength, input.length)
    },
    MaximumExceeded(
        messageRes = CoreResR.string.text_field_error_max_limit_exceeded,
        semanticsMessageRes = CoreResR.string.text_field_error_semantics_max_limit_exceeded
    ) {
        @Composable
        override fun getMessage(input: CharSequence): String =
            stringResource(messageRes, PasswordMaxLength)

        @Composable
        override fun getSemanticsMessage(input: CharSequence): String =
            stringResource(messageRes, PasswordMaxLength, input.length)
    }
}

fun CharSequence.validatePassword(): PasswordValidationError? = when {
    isBlank() -> PasswordValidationError.Required
    length < PasswordMinLength -> PasswordValidationError.MinimumRequired
    length > PasswordMaxLength -> PasswordValidationError.MaximumExceeded
    else -> null
}

val PasswordValidator: InputValidator<PasswordValidationError> =
    InputValidator(CharSequence::validatePassword)

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    contentType: ContentType = PasswordTextFieldDefaults.contentType,
    state: TextFieldState,
    validationError: PasswordValidationError?,
    enabled: Boolean = true,
    label: @Composable (TextFieldLabelScope.() -> Unit)? = PasswordTextFieldDefaults.Label,
    interactionSource: MutableInteractionSource? = null
) {
    val errorSemantics = validationError?.getSemanticsMessage(state.text)

    PasswordTextField(
        modifier = modifier.semantics {
            errorSemantics?.let(::error)
        },
        contentType = contentType,
        state = state,
        enabled = enabled,
        label = label,
        supportingText = {
            AnimatedContent(
                label = "Password form field supporting text",
                targetState = validationError,
                transitionSpec = TextFieldAnimations.supportingTextTransitionSpec()
            ) { type ->
                type?.let {
                    Text(text = it.getMessage(state.text))
                }
            }
        },
        isError = validationError != null,
        interactionSource = interactionSource
    )
}

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    contentType: ContentType = PasswordTextFieldDefaults.contentType,
    state: TextFieldState,
    enabled: Boolean = true,
    label: @Composable (TextFieldLabelScope.() -> Unit)? = PasswordTextFieldDefaults.Label,
    passwordValidator: InputValidator<PasswordValidationError> = PasswordValidator,
    interactionSource: MutableInteractionSource? = null
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val validationError by state.validationErrorAsState(
        validator = passwordValidator,
        interactionSource = interactionSource
    )

    PasswordTextField(
        modifier = modifier,
        contentType = contentType,
        state = state,
        enabled = enabled,
        label = label,
        supportingText = {
            AnimatedContent(
                label = "Password form field supporting text",
                targetState = validationError,
                transitionSpec = TextFieldAnimations.supportingTextTransitionSpec()
            ) { type ->
                type?.let {
                    Text(text = it.getMessage(state.text))
                }
            }
        },
        isError = validationError != null,
        interactionSource = interactionSource
    )
}
