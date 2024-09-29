package com.edricchan.studybuddy.features.tasks.ui.fields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.edricchan.studybuddy.features.tasks.R

@Composable
fun TaskTitleTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    val isError = remember(value, value::isBlank)

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = stringResource(R.string.text_field_task_title_label))
        },
        supportingText = {
            AnimatedVisibility(isError) {
                Text(text = stringResource(R.string.text_field_error_required))
            }
        },
        isError = isError,
        singleLine = true
    )
}
