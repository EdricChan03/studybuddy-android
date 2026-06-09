package com.edricchan.studybuddy.features.auth.common.ui.fields

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.autofill.contentType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Mail
import com.edricchan.studybuddy.data.forms.InputValidator
import com.edricchan.studybuddy.data.forms.compose.InputValidationError
import com.edricchan.studybuddy.data.forms.validationErrorAsState
import com.edricchan.studybuddy.exts.androidx.compose.material3.TextFieldAnimations
import com.edricchan.studybuddy.features.auth.R
import com.edricchan.studybuddy.features.auth.exts.isInvalidEmail
import kotlinx.coroutines.FlowPreview
import com.edricchan.studybuddy.core.resources.R as CoreResR

@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: @Composable (TextFieldLabelScope.() -> Unit)? = {
        Text(text = stringResource(R.string.account_email_field_label))
    },
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean
) = OutlinedTextField(
    modifier = modifier.contentType(ContentType.EmailAddress),
    state = state,
    enabled = enabled,
    readOnly = readOnly,
    label = label,
    leadingIcon = {
        Icon(AppIcons.Outlined.Mail, contentDescription = null)
    },
    supportingText = supportingText,
    isError = isError,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    lineLimits = TextFieldLineLimits.SingleLine
)

enum class EmailValidationError(@field:StringRes override val messageRes: Int) :
    InputValidationError {
    Required(messageRes = CoreResR.string.text_field_error_required),
    InvalidEmail(messageRes = R.string.account_email_field_error_invalid_text)
}

fun CharSequence.validateEmail(): EmailValidationError? = when {
    isBlank() -> EmailValidationError.Required
    toString().isInvalidEmail() -> EmailValidationError.InvalidEmail
    else -> null
}

val EmailValidator: InputValidator<EmailValidationError> =
    InputValidator(CharSequence::validateEmail)

@OptIn(FlowPreview::class)
@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: @Composable (TextFieldLabelScope.() -> Unit)? = {
        Text(text = stringResource(R.string.account_email_field_label))
    },
    emailValidator: InputValidator<EmailValidationError> = EmailValidator
) {
    val validationError by state.validationErrorAsState(validator = emailValidator)

    val errorSemantics = validationError?.getSemanticsMessage(state.text)

    EmailTextField(
        modifier = modifier.semantics {
            errorSemantics?.let(::error)
        },
        state = state,
        enabled = enabled,
        readOnly = readOnly,
        label = label,
        supportingText = {
            AnimatedContent(
                label = "Email form field supporting text",
                targetState = validationError,
                transitionSpec = TextFieldAnimations.supportingTextTransitionSpec()
            ) { type ->
                type?.let {
                    Text(text = stringResource(it.messageRes))
                }
            }
        },
        isError = validationError != null
    )
}
