package com.edricchan.studybuddy.features.tasks.ui.fields.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Add
import com.edricchan.studybuddy.core.resources.icons.outlined.Block
import com.edricchan.studybuddy.data.common.compose.toComposeColor
import com.edricchan.studybuddy.features.tasks.R
import com.edricchan.studybuddy.features.tasks.domain.model.TaskProject
import com.edricchan.studybuddy.features.tasks.ui.fields.TaskProjectFieldDefaults
import com.edricchan.studybuddy.features.tasks.ui.fields.TaskProjectIconSurface
import com.edricchan.studybuddy.ui.widgets.compose.list.TrailingRadioButtonListItem
import com.edricchan.studybuddy.utils.compose.foundation.lazy.PaginationLoadingIndicator

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun ChooseTaskProjectLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues.Zero,
    selectedProjectId: String?,
    onSelectProjectId: (String?) -> Unit,
    pagedProjects: LazyPagingItems<TaskProject>,
    onCreateProjectRequest: () -> Unit
) {
    val itemsCount = pagedProjects.itemCount + 1

    LazyColumn(
        modifier = modifier.selectableGroup(),
        state = state,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
    ) {
        item {
            TrailingRadioButtonListItem(
                selected = selectedProjectId == null,
                onSelected = { onSelectProjectId(null) },
                text = {
                    Text(text = stringResource(R.string.text_field_task_project_option_none))
                },
                leadingContent = {
                    TaskProjectIconSurface(
                        color = null,
                        content = {
                            TaskProjectFieldDefaults.DefaultSurfaceIcon(
                                icon = AppIcons.Outlined.Block
                            )
                        }
                    )
                },
                shapes = ListItemDefaults.segmentedShapes(
                    index = 0,
                    count = itemsCount
                )
            )
        }

        if (pagedProjects.loadState.prepend == LoadState.Loading) {
            item {
                PaginationLoadingIndicator()
            }
        }

        items(
            count = pagedProjects.itemCount,
            key = pagedProjects.itemKey { it.id }
        ) {
            val item = pagedProjects[it] ?: return@items

            TrailingRadioButtonListItem(
                modifier = Modifier.animateItem(),
                shapes = ListItemDefaults.segmentedShapes(
                    index = it + 1,
                    count = itemsCount
                ),
                selected = selectedProjectId == item.id,
                onSelected = { onSelectProjectId(item.id) },
                text = {
                    Text(text = item.name)
                },
                leadingContent = {
                    TaskProjectIconSurface(
                        color = item.color?.toComposeColor()
                    )
                }
            )
        }

        if (pagedProjects.loadState.append == LoadState.Loading) {
            item {
                PaginationLoadingIndicator()
            }
        }

        item {
            CreateTaskProjectButton(onCreateProjectRequest = onCreateProjectRequest)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun LazyItemScope.CreateTaskProjectButton(
    modifier: Modifier = Modifier,
    onCreateProjectRequest: () -> Unit
) {
    val height = ButtonDefaults.ExtraSmallContainerHeight

    FilledTonalButton(
        modifier = modifier
            .heightIn(height)
            .fillParentMaxWidth(),
        onClick = onCreateProjectRequest,
        contentPadding = ButtonDefaults.contentPaddingFor(
            buttonHeight = height,
            hasStartIcon = true
        ),
        shapes = ButtonDefaults.shapesFor(height),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    ) {
        Icon(
            modifier = Modifier.size(ButtonDefaults.iconSizeFor(height)),
            imageVector = AppIcons.Outlined.Add,
            contentDescription = null
        )
        Spacer(
            modifier = Modifier.size(ButtonDefaults.iconSpacingFor(height))
        )
        Text(
            text = stringResource(R.string.text_field_choose_task_project_action_create_project),
            style = ButtonDefaults.textStyleFor(height)
        )
    }
}
