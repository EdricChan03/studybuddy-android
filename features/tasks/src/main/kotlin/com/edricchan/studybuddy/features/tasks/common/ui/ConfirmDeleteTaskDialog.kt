package com.edricchan.studybuddy.features.tasks.common.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Warning
import com.edricchan.studybuddy.features.tasks.R


private const val ConfirmDeleteDialogMaxTitleLength = 50

// Code adapted from https://stackoverflow.com/a/32223795/6782707
// This should hopefully handle non-UTF8 text such as emojis which might get cut off
private val String.truncated: String
    get() {
        val maxWidth = ConfirmDeleteDialogMaxTitleLength
        val correctedMaxWidth: Int =
            if (this[maxWidth].isLowSurrogate()) maxWidth - 1 else maxWidth
        return take(
            correctedMaxWidth
        ) + Typography.ellipsis
    }

@Composable
private fun DeleteTaskDialogTitleText(
    modifier: Modifier = Modifier,
    taskTitle: String?
) {
    Text(
        modifier = modifier,
        text = taskTitle?.takeUnless(String::isBlank)?.let {
            stringResource(
                R.string.confirm_delete_task_dialog_title,
                it.truncated
            )
        } ?: stringResource(R.string.confirm_delete_task_dialog_title_no_title)
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ConfirmDeleteTaskDialog(
    modifier: Modifier = Modifier,
    taskTitle: String?,
    onDismissRequest: () -> Unit,
    onDeleteTask: () -> Unit
) = AlertDialog(
    modifier = modifier,
    onDismissRequest = onDismissRequest,
    icon = {
        Icon(
            AppIcons.Outlined.Warning,
            contentDescription = null
        )
    },
    iconContentColor = MaterialTheme.colorScheme.error,
    title = {
        DeleteTaskDialogTitleText(taskTitle = taskTitle)
    },
    text = { Text(text = stringResource(R.string.confirm_delete_task_dialog_content)) },
    dismissButton = {
        OutlinedButton(
            onClick = onDismissRequest,
            shapes = ButtonDefaults.shapes()
        ) {
            Text(text = stringResource(android.R.string.cancel))
        }
    },
    confirmButton = {
        Button(
            onClick = onDeleteTask,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ),
            shapes = ButtonDefaults.shapes()
        ) {
            Text(text = stringResource(R.string.task_adapter_delete_task_btn_text))
        }
    }
)
