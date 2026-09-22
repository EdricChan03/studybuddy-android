package com.edricchan.studybuddy.features.tasks.components.form.description.validation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.edricchan.studybuddy.data.forms.InputValidator
import com.edricchan.studybuddy.data.forms.compose.InputValidationError
import com.edricchan.studybuddy.core.resources.R as CoreResR

const val TaskDescriptionMaxLength = 20_000

enum class TaskDescriptionValidationError(
    @field:StringRes override val messageRes: Int,
    @field:StringRes override val semanticsMessageRes: Int = messageRes,
) : InputValidationError {
    MaximumExceeded(
        messageRes = CoreResR.string.text_field_error_max_limit_exceeded,
        semanticsMessageRes = CoreResR.string.text_field_error_semantics_max_limit_exceeded
    ) {
        @Composable
        override fun getMessage(input: CharSequence): String =
            stringResource(messageRes, TaskDescriptionMaxLength)

        @Composable
        override fun getSemanticsMessage(input: CharSequence): String =
            stringResource(messageRes, TaskDescriptionMaxLength, input.length)
    }
}

fun CharSequence.validateDescription(): TaskDescriptionValidationError? = when {
    length > TaskDescriptionMaxLength -> TaskDescriptionValidationError.MaximumExceeded
    else -> null
}

val TaskDescriptionValidator: InputValidator<TaskDescriptionValidationError> = InputValidator(
    CharSequence::validateDescription
)
