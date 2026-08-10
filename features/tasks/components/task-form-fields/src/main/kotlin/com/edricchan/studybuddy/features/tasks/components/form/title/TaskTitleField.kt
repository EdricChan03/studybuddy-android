package com.edricchan.studybuddy.features.tasks.components.form.title

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
import com.edricchan.studybuddy.data.forms.validationErrorAsState
import com.edricchan.studybuddy.features.tasks.components.form.R
import com.edricchan.studybuddy.features.tasks.components.form.title.validation.TaskTitleMaxLength
import com.edricchan.studybuddy.features.tasks.components.form.title.validation.TaskTitleValidationError
import com.edricchan.studybuddy.features.tasks.components.form.title.validation.TaskTitleValidator
import com.edricchan.studybuddy.utils.compose.material3.textfield.TextFieldAnimations
import com.edricchan.studybuddy.core.resources.R as CoreResR

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
            Text(text = stringResource(R.string.task_title_text_field_label))
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
