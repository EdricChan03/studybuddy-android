package com.edricchan.studybuddy.features.tasks.ui.fields

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.maxTextLength
import androidx.compose.ui.semantics.semantics
import com.edricchan.studybuddy.data.forms.InputValidator
import com.edricchan.studybuddy.data.forms.compose.InputValidationError
import com.edricchan.studybuddy.data.forms.validationErrorAsState
import com.edricchan.studybuddy.features.tasks.R
import com.edricchan.studybuddy.utils.compose.material3.textfield.TextFieldAnimations
import com.edricchan.studybuddy.core.resources.R as CoreResR

const val TaskTitleMaxLength = 5000

enum class TaskTitleValidationError(
    @field:StringRes
    override val messageRes: Int,
    @field:StringRes
    override val semanticsMessageRes: Int = messageRes
) : InputValidationError {
    Required(CoreResR.string.text_field_error_required),
    MaxLengthExceeded(
        messageRes = CoreResR.string.text_field_error_max_limit_exceeded,
        semanticsMessageRes = CoreResR.string.text_field_error_semantics_max_limit_exceeded,
    ) {
        @Composable
        override fun getMessage(input: CharSequence): String =
            stringResource(messageRes, input.length)

        @Composable
        override fun getSemanticsMessage(input: CharSequence): String =
            stringResource(messageRes, TaskTitleMaxLength, input.length)
    }
}

fun CharSequence.validateTaskTitle(): TaskTitleValidationError? = when {
    isBlank() -> TaskTitleValidationError.Required
    length > TaskTitleMaxLength -> TaskTitleValidationError.MaxLengthExceeded
    else -> null
}

val TaskTitleValidator: InputValidator<TaskTitleValidationError> =
    InputValidator(CharSequence::validateTaskTitle)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TaskTitleTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    validator: InputValidator<TaskTitleValidationError> = TaskTitleValidator
) {
    val requiredMsg = stringResource(CoreResR.string.text_field_error_required)

    val validationError by state.validationErrorAsState(validator = validator)
    val errorSemanticsMsg = validationError?.getSemanticsMessage(state.text)

    val counterText = stringResource(
        CoreResR.string.text_field_limit,
        state.text.length, TaskTitleMaxLength
    )

    OutlinedTextField(
        modifier = modifier.semantics {
            errorSemanticsMsg?.let { error(it) }
            maxTextLength = TaskTitleMaxLength
        },
        state = state,
        label = {
            Text(text = stringResource(R.string.text_field_task_title_label))
        },
        supportingText = {
            AnimatedContent(
                label = "Task title form field supporting text",
                targetState = validationError,
                transitionSpec = TextFieldAnimations.supportingTextTransitionSpec()
            ) {
                Text(
                    text = if (it == TaskTitleValidationError.Required) requiredMsg
                    else counterText
                )
            }
        },
        isError = validationError != null,
        lineLimits = TextFieldLineLimits.SingleLine
    )
}
