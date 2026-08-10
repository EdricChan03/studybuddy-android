package com.edricchan.studybuddy.features.tasks.components.form.title.validation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.edricchan.studybuddy.core.resources.R
import com.edricchan.studybuddy.data.forms.InputValidator
import com.edricchan.studybuddy.data.forms.compose.InputValidationError

enum class TaskTitleValidationError(
    @field:StringRes
    override val messageRes: Int,
    @field:StringRes
    override val semanticsMessageRes: Int = messageRes
) : InputValidationError {
    Required(R.string.text_field_error_required),
    MaxLengthExceeded(
        messageRes = R.string.text_field_error_max_limit_exceeded,
        semanticsMessageRes = R.string.text_field_error_semantics_max_limit_exceeded,
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

const val TaskTitleMaxLength = 5000
val TaskTitleValidator: InputValidator<TaskTitleValidationError> =
    InputValidator(CharSequence::validateTaskTitle)
