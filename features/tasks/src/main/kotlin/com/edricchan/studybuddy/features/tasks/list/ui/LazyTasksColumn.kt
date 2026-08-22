package com.edricchan.studybuddy.features.tasks.list.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.edricchan.studybuddy.features.tasks.common.ui.card.placeholder.PlaceholderTaskCard
import com.edricchan.studybuddy.features.tasks.components.card.TaskCard
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.utils.compose.foundation.lazy.PaginationLoadingIndicator
import com.valentinilk.shimmer.shimmer

@Composable
fun LazyTasksColumn(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues.Zero,
    pagedTasks: LazyPagingItems<TaskItem>,
    onItemClick: (TaskItem) -> Unit,
    onCompleteClick: (TaskItem) -> Unit,
    onDeleteClick: (TaskItem) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (pagedTasks.loadState.prepend == LoadState.Loading) {
            item {
                PaginationLoadingIndicator()
            }
        }
        items(
            count = pagedTasks.itemCount,
            key = pagedTasks.itemKey { it.id },
            contentType = pagedTasks.itemContentType()
        ) {
            Crossfade(
                modifier = Modifier.animateItem(),
                targetState = pagedTasks[it]
            ) { item ->
                item?.let { item ->
                    TaskCard(
                        task = item,
                        onClick = { onItemClick(item) },
                        onMarkAsDoneClick = { onCompleteClick(item) },
                        onDeleteClick = { onDeleteClick(item) }
                    )
                } ?: run {
                    PlaceholderTaskCard(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .shimmer()
                    )
                }
            }
        }

        if (pagedTasks.loadState.append == LoadState.Loading) {
            item {
                PaginationLoadingIndicator()
            }
        }
    }
}
