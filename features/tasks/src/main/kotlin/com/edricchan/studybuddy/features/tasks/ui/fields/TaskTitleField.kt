package com.edricchan.studybuddy.features.tasks.ui.fields

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.maxTextLength
import androidx.compose.ui.semantics.semantics
import com.edricchan.studybuddy.data.forms.compose.InputValidationError
import com.edricchan.studybuddy.exts.androidx.compose.material3.TextFieldAnimations
import com.edricchan.studybuddy.features.tasks.R
import com.edricchan.studybuddy.core.resources.R as CoreResR

const val TaskTitleMaxLength = 5000

private enum class TaskTitleValidationError(
    @field:StringRes
    override val messageRes: Int
) : InputValidationError {
    Required(CoreResR.string.text_field_error_required),
    MaxLengthExceeded(CoreResR.string.text_field_error_semantics_max_limit_exceeded) {
        @Composable
        override fun getMessage(input: CharSequence): String =
            stringResource(messageRes, TaskTitleMaxLength, input.length)
    }
}

private fun getErrorType(input: CharSequence): TaskTitleValidationError? {
    if (input.isBlank()) return TaskTitleValidationError.Required
    if (input.length > TaskTitleMaxLength) return TaskTitleValidationError.MaxLengthExceeded

    return null
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TaskTitleTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState
) {
    val requiredMsg = stringResource(CoreResR.string.text_field_error_required)

    val errorType = remember(state.text) { getErrorType(state.text) }
    val errorSemanticsMsg = errorType?.getMessage(state.text)

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
                targetState = errorType,
                transitionSpec = TextFieldAnimations.supportingTextTransitionSpec()
            ) {
                Text(
                    text = if (it == TaskTitleValidationError.Required) requiredMsg
                    else counterText
                )
            }
        },
        isError = errorType != null,
        lineLimits = TextFieldLineLimits.SingleLine
    )
}
