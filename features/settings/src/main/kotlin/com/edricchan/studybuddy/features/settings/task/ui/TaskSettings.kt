package com.edricchan.studybuddy.features.settings.task.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edricchan.studybuddy.features.settings.R
import com.edricchan.studybuddy.features.settings.task.model.TaskSortOptionCompat
import com.edricchan.studybuddy.features.settings.task.vm.TaskSettingsViewModel
import com.edricchan.studybuddy.ui.preference.compose.ListDialogPreference
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

@Composable
private fun TaskDefaultSortPreference(
    modifier: Modifier = Modifier,
    defaultSort: TaskSortOptionCompat,
    onDefaultSortChange: (TaskSortOptionCompat) -> Unit
) {
    ListDialogPreference(
        modifier = modifier,
        icon = { Icon(painterResource(R.drawable.ic_sort_24dp), contentDescription = null) },
        title = { Text(text = stringResource(R.string.pref_task_default_sort_title)) },
        subtitle = { Text(text = stringResource(defaultSort.stringResource)) },
        values = TaskSortOptionCompat.entries,
        value = defaultSort,
        onValueChanged = onDefaultSortChange,
        valueLabel = { Text(text = stringResource(it.stringResource)) }
    )
}

@Composable
fun TaskSettingsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    defaultSort: TaskSortOptionCompat,
    onDefaultSortChange: (TaskSortOptionCompat) -> Unit
) = Column(
    modifier = modifier
        .verticalScroll(rememberScrollState())
        .padding(contentPadding)
) {
    TaskDefaultSortPreference(
        defaultSort = defaultSort,
        onDefaultSortChange = onDefaultSortChange
    )
}

@Composable
fun TaskSettingsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: TaskSettingsViewModel
) {
    val defaultSort by viewModel.prefDefaultSort.asFlow().collectAsStateWithLifecycle(
        TaskSortOptionCompat.None
    )

    TaskSettingsScreen(
        modifier = modifier,
        contentPadding = contentPadding,
        defaultSort = defaultSort,
        onDefaultSortChange = viewModel.prefDefaultSort::set
    )
}

@Preview
@Composable
private fun TaskSettingsScreenPreview() {
    var defaultSort by remember { mutableStateOf(TaskSortOptionCompat.None) }

    StudyBuddyTheme {
        TaskSettingsScreen(
            defaultSort = defaultSort,
            onDefaultSortChange = { defaultSort = it }
        )
    }
}
