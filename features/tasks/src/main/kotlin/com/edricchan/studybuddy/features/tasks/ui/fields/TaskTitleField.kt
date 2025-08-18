package com.edricchan.studybuddy.features.tasks.ui.fields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import com.edricchan.studybuddy.features.tasks.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TaskTitleTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    val isError = remember(value, value::isBlank)
    val requiredMsg = stringResource(R.string.text_field_error_required)

    OutlinedTextField(
        modifier = modifier.semantics {
            if (isError) error(requiredMsg)
        },
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = stringResource(R.string.text_field_task_title_label))
        },
        supportingText = {
            AnimatedVisibility(
                label = "Task title error message animated visibility",
                visible = isError,
                enter = fadeIn(
                    animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()
                ) + expandVertically(
                    animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                ),
                exit = fadeOut(
                    animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()
                ) + shrinkVertically(
                    animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                )
            ) {
                Text(text = requiredMsg)
            }
        },
        isError = isError,
        singleLine = true
    )
}
