package com.edricchan.studybuddy.features.settings.task.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Archive
import com.edricchan.studybuddy.core.resources.icons.outlined.CheckCircle
import com.edricchan.studybuddy.core.resources.icons.outlined.Delete
import com.edricchan.studybuddy.ui.preference.compose.InfoPreference
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategory
import com.edricchan.studybuddy.ui.preference.compose.twostate.SwitchPreference
import com.edricchan.studybuddy.core.settings.tasks.resources.R as TaskR

@Composable
internal fun TaskActionConfirmationCategory(
    modifier: Modifier = Modifier,
    showOnDelete: Boolean,
    onShowOnDeleteChange: (Boolean) -> Unit,
    showOnArchive: Boolean,
    onShowOnArchiveChange: (Boolean) -> Unit,
    showOnComplete: Boolean,
    onShowOnCompleteChange: (Boolean) -> Unit
) {
    PreferenceCategory(
        modifier = modifier,
        title = { Text(text = stringResource(TaskR.string.pref_category_task_confirmations_title)) },
        info = {
            InfoPreference(
                text = {
                    Text(text = stringResource(TaskR.string.pref_category_task_confirmations_info_subtitle))
                }
            )
        }
    ) {
        SwitchPreference(
            icon = {
                Icon(
                    AppIcons.Outlined.Delete,
                    contentDescription = null
                )
            },
            title = { Text(text = stringResource(TaskR.string.pref_category_task_confirmation_on_delete_title)) },
            checked = showOnDelete,
            onCheckedChange = onShowOnDeleteChange
        )
        SwitchPreference(
            icon = {
                Icon(
                    AppIcons.Outlined.Archive,
                    contentDescription = null
                )
            },
            title = { Text(text = stringResource(TaskR.string.pref_category_task_confirmation_on_archive_title)) },
            checked = showOnArchive,
            onCheckedChange = onShowOnArchiveChange
        )
        SwitchPreference(
            icon = {
                Icon(
                    AppIcons.Outlined.CheckCircle,
                    contentDescription = null
                )
            },
            title = { Text(text = stringResource(TaskR.string.pref_category_task_confirmation_on_completion_title)) },
            checked = showOnComplete,
            onCheckedChange = onShowOnCompleteChange
        )
    }
}
