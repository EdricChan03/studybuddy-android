package com.edricchan.studybuddy.features.tasks.components.form.due

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.edricchan.studybuddy.core.resources.temporal.appFormat
import com.edricchan.studybuddy.exts.androidx.compose.runtime.letComposable
import com.edricchan.studybuddy.features.tasks.components.form.R
import com.edricchan.studybuddy.ui.widgets.compose.textfield.OutlinedTextFieldBox
import java.time.LocalDate

@Composable
fun TaskDueDateField(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate?,
    onSelectedDateChange: (LocalDate?) -> Unit,
    formatDate: (LocalDate) -> String = LocalDate::appFormat
) {
    var isDatePickerShown by rememberSaveable { mutableStateOf(false) }
    val hideDatePicker = { isDatePickerShown = false }
    val showDatePicker = { isDatePickerShown = true }

    val datePickerState = rememberDatePickerState(
        initialSelectedDate = selectedDate
    )

    OutlinedTextFieldBox(
        modifier = modifier,
        onClick = showDatePicker,
        headlineText = {
            Text(text = stringResource(R.string.task_due_date_text_field_label))
        },
        supportingText = selectedDate?.letComposable {
            Text(text = formatDate(it))
        }
    )

    if (isDatePickerShown) {
        DatePickerDialog(
            onDismissRequest = hideDatePicker,
            dismissButton = {
                OutlinedButton(
                    onClick = hideDatePicker,
                    shapes = ButtonDefaults.shapes()
                ) {
                    Text(text = stringResource(android.R.string.cancel))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSelectedDateChange(datePickerState.getSelectedDate())
                        hideDatePicker()
                    },
                    shapes = ButtonDefaults.shapes()
                ) {
                    Text(text = stringResource(android.R.string.ok))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
