package com.edricchan.studybuddy.features.tasks.detail.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Error
import com.edricchan.studybuddy.features.tasks.R
import com.edricchan.studybuddy.features.tasks.detail.data.state.TaskDetailState
import com.edricchan.studybuddy.features.tasks.detail.vm.TaskDetailViewModel
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider
import com.edricchan.studybuddy.utils.compose.foundation.layout.CenteredBox

@Composable
fun TaskDetailScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    windowInsets: WindowInsets = WindowInsets.navigationBars,
    state: TaskDetailState,
    onCompletedChange: (Boolean) -> Unit
) {
    Crossfade(
        modifier = modifier,
        targetState = state
    ) { state ->
        when (state) {
            is TaskDetailState.Error -> TaskDetailScreenErrorState(
                contentPadding = contentPadding,
                errorText = state.error.message
            )

            TaskDetailState.Loading -> TaskDetailScreenLoadingState(
                contentPadding = contentPadding
            )

            TaskDetailState.NoData -> TaskDetailScreenNoDataState(
                contentPadding = contentPadding
            )

            is TaskDetailState.Success -> {
                TaskDetailColumn(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(contentPadding)
                        .windowInsetsPadding(windowInsets),
                    task = state.item,
                    project = state.item.project,
                    onCompletedChange = onCompletedChange
                )
            }
        }
    }
}

@Composable
fun TaskDetailScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    windowInsets: WindowInsets = WindowInsets.navigationBars,
    viewModel: TaskDetailViewModel = hiltViewModel()
) {
    val taskState by viewModel.currentTaskStateFlow.collectAsStateWithLifecycle()

    TaskDetailScreen(
        modifier = modifier,
        contentPadding = contentPadding,
        windowInsets = windowInsets,
        state = taskState,
        onCompletedChange = viewModel::onCompletedChange
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TaskDetailScreenLoadingState(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    shouldFillMaxSize: Boolean = true
) = CenteredBox(
    modifier = modifier,
    shouldFillMaxSize = shouldFillMaxSize
) {
    BoxWithConstraints(
        modifier = Modifier.padding(contentPadding)
    ) {
        val indicatorSize = (maxWidth * 0.15f)
            .coerceIn(32.dp, 144.dp)

        val indicatorDesc = stringResource(R.string.task_detail_status_loading_content_desc)
        LoadingIndicator(
            modifier = Modifier
                .size(indicatorSize)
                .semantics {
                    contentDescription = indicatorDesc
                }
        )
    }
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun TaskDetailScreenLoadingStatePreview() {
    TaskDetailScreenLoadingState(
        shouldFillMaxSize = false
    )
}

@Composable
private fun TaskDetailScreenNoDataState(
    modifier: Modifier = Modifier,
    shouldFillMaxSize: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(16.dp)
) = CenteredBox(
    modifier = modifier,
    shouldFillMaxSize = shouldFillMaxSize
) {
    Column(
        modifier = Modifier.padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Icon(AppIcons.Outlined.Error, contentDescription = null)
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.task_detail_status_no_data_title),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.task_detail_status_no_data_subtitle),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun TaskDetailScreenNoDataStatePreview() {
    TaskDetailScreenNoDataState(
        shouldFillMaxSize = false
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TaskDetailScreenErrorState(
    modifier: Modifier = Modifier,
    shouldFillMaxSize: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    errorText: String?
) = CenteredBox(
    modifier = modifier,
    shouldFillMaxSize = shouldFillMaxSize
) {
    Column(
        modifier = Modifier.padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Icon(AppIcons.Outlined.Error, contentDescription = null)
        Text(
            textAlign = TextAlign.Center,
            text = errorText ?: stringResource(R.string.task_detail_status_error_title),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            textAlign = TextAlign.Center,
            text = errorText ?: stringResource(R.string.task_detail_status_error_subtitle),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun TaskDetailScreenErrorStatePreview() {
    TaskDetailScreenErrorState(
        shouldFillMaxSize = false,
        errorText = null
    )
}
