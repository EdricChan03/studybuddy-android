package com.edricchan.studybuddy.features.tasks.components.form.due

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerDialogDefaults
import androidx.compose.material3.TimePickerDisplayMode
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TimeScroll
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.edricchan.studybuddy.core.resources.temporal.compose.LocalIs24HourFormat
import com.edricchan.studybuddy.core.resources.temporal.compose.LocalTimeFormatter
import com.edricchan.studybuddy.exts.androidx.compose.runtime.letComposable
import com.edricchan.studybuddy.features.tasks.components.form.R
import com.edricchan.studybuddy.ui.widgets.compose.textfield.OutlinedTextFieldBox
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun TaskDueTimeField(
    modifier: Modifier = Modifier,
    defaultTime: LocalTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES),
    is24HourFormat: Boolean = LocalIs24HourFormat.current,
    selectedTime: LocalTime?,
    onSelectedTimeChange: (LocalTime?) -> Unit,
    timeFormatter: DateTimeFormatter = LocalTimeFormatter.current
) {
    var isTimePickerShown by rememberSaveable { mutableStateOf(false) }
    val hideTimePicker = { isTimePickerShown = false }
    val showTimePicker = { isTimePickerShown = true }

    val timePickerState = rememberTimePickerState(
        initialHour = selectedTime?.hour ?: defaultTime.hour,
        initialMinute = selectedTime?.minute ?: defaultTime.minute,
        is24Hour = is24HourFormat
    )

    val formattedSelectedTime =
        remember(selectedTime, timeFormatter) { selectedTime?.format(timeFormatter) }

    OutlinedTextFieldBox(
        modifier = modifier,
        onClick = showTimePicker,
        headlineText = {
            Text(text = stringResource(R.string.task_due_time_text_field_label))
        },
        supportingText = formattedSelectedTime?.letComposable {
            Text(text = it)
        }
    )

    if (isTimePickerShown) {
        var displayMode by rememberSaveable(stateSaver = inputDisplayModeSaver) {
            mutableStateOf(TimePickerDisplayMode.Scroll)
        }

        TaskDueTimePickerDialog(
            displayMode = displayMode,
            onDisplayModeChange = { displayMode = it },
            hideTimePicker = hideTimePicker,
            onSelectedTimeChange = onSelectedTimeChange,
            timePickerState = timePickerState
        )
    }
}

private val inputDisplayModeSaver: Saver<TimePickerDisplayMode, Boolean> = Saver(
    save = {
        it == TimePickerDisplayMode.Input
    },
    restore = {
        if (it) TimePickerDisplayMode.Input else TimePickerDisplayMode.Scroll
    }
)

@Composable
private fun TaskDueTimePickerDialog(
    modifier: Modifier = Modifier,
    displayMode: TimePickerDisplayMode,
    onDisplayModeChange: (TimePickerDisplayMode) -> Unit,
    hideTimePicker: () -> Unit,
    onSelectedTimeChange: (LocalTime?) -> Unit,
    timePickerState: TimePickerState
) {
    TimePickerDialog(
        modifier = modifier,
        title = {
            TimePickerDialogDefaults.Title(
                displayMode = displayMode
            )
        },
        onDismissRequest = hideTimePicker,
        modeToggleButton = {
            TimePickerDialogDefaults.ScrollDisplayModeToggle(
                displayMode = displayMode,
                onDisplayModeChange = {
                    onDisplayModeChange(
                        if (displayMode == TimePickerDisplayMode.Input) TimePickerDisplayMode.Scroll
                        else TimePickerDisplayMode.Input
                    )
                }
            )
        },
        dismissButton = {
            OutlinedButton(
                onClick = hideTimePicker,
                shapes = ButtonDefaults.shapes()
            ) {
                Text(text = stringResource(android.R.string.cancel))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSelectedTimeChange(
                        LocalTime.of(
                            timePickerState.hour,
                            timePickerState.minute
                        )
                    )
                    hideTimePicker()
                },
                shapes = ButtonDefaults.shapes()
            ) {
                Text(text = stringResource(android.R.string.ok))
            }
        }
    ) {
        if (displayMode == TimePickerDisplayMode.Input) TimeInput(
            state = timePickerState,
            shapes = TimePickerDefaults.shapes()
        )
        else TimeScroll(
            state = timePickerState,
            shapes = TimePickerDefaults.shapes()
        )
    }
}
