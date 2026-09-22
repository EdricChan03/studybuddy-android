package com.edricchan.studybuddy.features.tasks.components.form.description

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.maxTextLength
import androidx.compose.ui.semantics.semantics
import com.edricchan.studybuddy.data.forms.InputValidator
import com.edricchan.studybuddy.data.forms.validationErrorAsState
import com.edricchan.studybuddy.features.tasks.components.form.R
import com.edricchan.studybuddy.features.tasks.components.form.description.validation.TaskDescriptionMaxLength
import com.edricchan.studybuddy.features.tasks.components.form.description.validation.TaskDescriptionValidationError
import com.edricchan.studybuddy.features.tasks.components.form.description.validation.TaskDescriptionValidator
import com.edricchan.studybuddy.utils.compose.material3.textfield.TextFieldAnimations
import com.edricchan.studybuddy.core.resources.R as CoreResR

// TODO: Show a Markdown text editor instead
@Composable
fun TaskDescriptionTextArea(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    validator: InputValidator<TaskDescriptionValidationError> = TaskDescriptionValidator
) {
    val interactionSource = remember { MutableInteractionSource() }
    val validationError by state.validationErrorAsState(
        interactionSource = interactionSource,
        validator = validator
    )

    val errorSemantics = validationError?.getSemanticsMessage(state.text)

    val counterText = stringResource(
        CoreResR.string.text_field_limit,
        state.text.length, TaskDescriptionMaxLength
    )

    OutlinedTextField(
        modifier = modifier.semantics {
            errorSemantics?.let { error(it) }
            maxTextLength = TaskDescriptionMaxLength
        },
        interactionSource = interactionSource,
        state = state,
        label = {
            Text(text = stringResource(R.string.task_description_text_field_label))
        },
        supportingText = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedContent(
                    label = "Task description form field supporting text",
                    targetState = validationError,
                    transitionSpec = TextFieldAnimations.supportingTextTransitionSpec(),
                ) { type ->
                    Text(
                        text = type?.getMessage(state.text)
                            ?: stringResource(R.string.task_description_text_field_supporting_text)
                    )
                }

                Text(text = counterText)
            }
        },
        isError = validationError != null
    )
}
