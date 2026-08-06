package com.edricchan.studybuddy.features.tasks.detail.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.edricchan.studybuddy.features.tasks.detail.data.state.TaskDetailState
import com.edricchan.studybuddy.features.tasks.domain.sample.SampleTaskItems
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider

private val states = listOf(
    TaskDetailState.Loading to "Loading",
    TaskDetailState.Success(SampleTaskItems.first()) to "Success",
    TaskDetailState.NoData to "No data",
    TaskDetailState.Error(IllegalStateException()) to "Error"
)

private class TaskDetailStateParameterProvider :
    CollectionPreviewParameterProvider<TaskDetailState>(
        collection = states.map { it.first }
    ) {
    override fun getDisplayName(index: Int): String? = states.getOrNull(index)?.second
}

@Preview(
    showBackground = true, showSystemUi = true
)
@PreviewScreenSizes
@PreviewDynamicColors
@PreviewLightDark
@PreviewFontScale
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun TaskDetailScreenPreview(
    @PreviewParameter(TaskDetailStateParameterProvider::class) state: TaskDetailState
) {
    TaskDetailScreen(
        modifier = Modifier.fillMaxSize(),
        state = state,
        onCompletedChange = {}
    )
}
