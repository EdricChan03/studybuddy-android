package com.edricchan.studybuddy.features.tasks.list.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.plus
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.features.tasks.domain.sample.SampleTaskItems
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ListTasksScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    contentWindowInsets: WindowInsets = WindowInsets.navigationBars.only(WindowInsetsSides.Bottom),
    pagedTasks: LazyPagingItems<TaskItem>,
    onItemClick: (TaskItem) -> Unit,
    onCompleteClick: (TaskItem) -> Unit,
    onDeleteClick: (TaskItem) -> Unit,
    pullToRefreshState: PullToRefreshState = rememberPullToRefreshState(),
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    val isInitialLoading = pagedTasks.loadState.refresh == LoadState.Loading &&
        pagedTasks.itemSnapshotList.items.isEmpty()

    PullToRefreshBox(
        modifier = modifier
            .padding(contentPadding)
            .consumeWindowInsets(contentPadding),
        state = pullToRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        indicator = {
            PullToRefreshDefaults.LoadingIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullToRefreshState,
                isRefreshing = isRefreshing
            )
        }
    ) {
        Crossfade(
            modifier = Modifier.matchParentSize(),
            targetState = isInitialLoading
        ) { isLoading ->
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularWavyProgressIndicator()
                }
            } else {
                LazyTasksColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 16.dp) + contentWindowInsets.asPaddingValues(),
                    pagedTasks = pagedTasks,
                    onItemClick = onItemClick,
                    onCompleteClick = onCompleteClick,
                    onDeleteClick = onDeleteClick
                )
            }
        }
    }
}

@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Preview
@Composable
private fun ListTasksScreenPreview() {
    val items = remember { flowOf(PagingData.from(SampleTaskItems)) }
        .collectAsLazyPagingItems()

    ListTasksScreen(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues.Zero,
        pagedTasks = items,
        onItemClick = {},
        onDeleteClick = {},
        onCompleteClick = {},
        onRefresh = {},
        isRefreshing = false
    )
}

@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Preview
@Composable
private fun ListTasksScreenWithLoadingItemsPreview() {
    val items = remember {
        flowOf(
            PagingData.from(
                data = SampleTaskItems,
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = false),
                    prepend = LoadState.NotLoading(endOfPaginationReached = false),
                    append = LoadState.NotLoading(endOfPaginationReached = false)
                )
            )
        )
    }.collectAsLazyPagingItems()

    ListTasksScreen(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues.Zero,
        pagedTasks = items,
        onItemClick = {},
        onDeleteClick = {},
        onCompleteClick = {},
        onRefresh = {},
        isRefreshing = false
    )
}
