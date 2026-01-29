package com.edricchan.studybuddy.ui.modules.debug.compose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Delete
import com.edricchan.studybuddy.ui.preference.compose.Preference
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategoryScope

@Composable
private fun ConfirmClearSettingsDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) = AlertDialog(
    modifier = modifier,
    onDismissRequest = onDismissRequest,
    title = { Text(text = stringResource(R.string.debug_activity_confirm_clear_app_settings_dialog_title)) },
    text = { Text(text = stringResource(R.string.debug_activity_confirm_clear_app_settings_dialog_msg)) },
    dismissButton = {
        TextButton(onClick = onDismissRequest) {
            Text(text = stringResource(android.R.string.cancel))
        }
    },
    confirmButton = {
        FilledTonalButton(
            onClick = onConfirmClick,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            )
        ) {
            Text(text = stringResource(R.string.dialog_action_clear))
        }
    }
)

@Composable
fun PreferenceCategoryScope.ClearSettingsPreference(
    modifier: Modifier = Modifier,
    onClearSettingsRequest: () -> Unit
) {
    var isConfirmShown by rememberSaveable { mutableStateOf(false) }

    Preference(
        modifier = modifier,
        onClick = { isConfirmShown = true },
        icon = { Icon(AppIcons.Outlined.Delete, contentDescription = null) },
        title = { Text(text = stringResource(R.string.debug_activity_other_clear_app_settings_title)) },
    )

    if (isConfirmShown) {
        ConfirmClearSettingsDialog(
            onDismissRequest = { isConfirmShown = false },
            onConfirmClick = {
                onClearSettingsRequest()
                isConfirmShown = false
            }
        )
    }
}
