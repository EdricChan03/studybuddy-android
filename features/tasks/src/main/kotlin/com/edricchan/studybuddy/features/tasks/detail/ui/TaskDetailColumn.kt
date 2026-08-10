package com.edricchan.studybuddy.features.tasks.detail.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Build
import com.edricchan.studybuddy.core.resources.icons.outlined.ContentCopy
import com.edricchan.studybuddy.core.resources.icons.outlined.Info
import com.edricchan.studybuddy.core.resources.temporal.compose.formatWithDateTime
import com.edricchan.studybuddy.data.common.compose.toComposeColor
import com.edricchan.studybuddy.exts.datetime.toLocalDateTime
import com.edricchan.studybuddy.features.tasks.R
import com.edricchan.studybuddy.features.tasks.components.metadata.title.TaskTitleWithCheckboxListItem
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.features.tasks.domain.model.TaskProject
import com.edricchan.studybuddy.features.tasks.ui.attrs.TaskContentMarkdownText
import com.edricchan.studybuddy.features.tasks.ui.attrs.TaskDueDateListItem
import com.edricchan.studybuddy.features.tasks.ui.attrs.TaskProjectListItem
import com.edricchan.studybuddy.features.tasks.ui.attrs.TaskTagsListItem
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider
import com.edricchan.studybuddy.ui.widgets.compose.IconButtonWithTooltip
import com.edricchan.studybuddy.ui.widgets.compose.list.segmented.SegmentedListColumn
import com.edricchan.studybuddy.ui.widgets.compose.markdown.SampleMarkdownText
import com.edricchan.studybuddy.utils.compose.graphics.preview.LightColors
import com.edricchan.studybuddy.utils.dev.compose.isAppDevMode
import java.time.Duration
import java.time.Instant

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailColumn(
    modifier: Modifier = Modifier,
    taskId: String,
    title: String,
    content: String? = null,
    projectColor: Color? = null,
    projectName: String? = null,
    tags: Set<String> = emptySet(),
    dueDate: Instant? = null,
    isCompleted: Boolean = false,
    onCompletedChange: (Boolean) -> Unit,
    isArchived: Boolean = false,
    createdAt: Instant,
    lastModifiedAt: Instant,
    isDevMode: Boolean = isAppDevMode(),
    onRequestCopyText: (String) -> Unit
) {
    val createdAtText = createdAt.toLocalDateTime().formatWithDateTime()
    val lastModifiedAtText = lastModifiedAt.toLocalDateTime().formatWithDateTime()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TaskTitleWithCheckboxListItem(
            modifier = Modifier.fillMaxWidth(),
            title = title.takeIf(String::isNotBlank)
                ?: stringResource(R.string.task_attr_title_name_default),
            isDone = isCompleted,
            onDoneChange = onCompletedChange,
            isArchived = isArchived
        )
        content?.takeIf(String::isNotBlank)?.let {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = CardDefaults.shape,
                color = MaterialTheme.colorScheme.surfaceContainer
            ) {
                TaskContentMarkdownText(
                    modifier = Modifier.padding(16.dp),
                    text = it
                )
            }
        }
        SegmentedListColumn(
            listItemColors = ListItemDefaults.segmentedColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            projectName?.let {
                customItem {
                    TaskProjectListItem(
                        name = it, color = projectColor,
                        shapes = shapes,
                        colors = colors
                    )
                }
            }
            tags.takeUnless(Set<String>::isEmpty)?.let {
                customItem {
                    TaskTagsListItem(
                        tags = it,
                        shapes = shapes,
                        colors = colors
                    )
                }
            }
            dueDate?.let {
                customItem {
                    TaskDueDateListItem(
                        dueDate = it,
                        shapes = shapes,
                        colors = colors
                    )
                }
            }
        }

        SegmentedListColumn(
            listItemColors = ListItemDefaults.segmentedColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            item(
                leadingContent = {
                    Icon(AppIcons.Outlined.Info, contentDescription = null)
                }
            ) {
                SelectionContainer {
                    Text(
                        text = stringResource(
                            R.string.task_detail_timestamp_metadata_item_text,
                            createdAtText,
                            lastModifiedAtText
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            // Dev-mode only, so these text strings are not translated
            if (isDevMode) {
                item(
                    leadingContent = {
                        Icon(AppIcons.Outlined.Build, contentDescription = null)
                    },
                    trailingContent = {
                        IconButtonWithTooltip(
                            onClick = {
                                onRequestCopyText(taskId)
                            },
                            tooltip = {
                                PlainTooltip {
                                    Text(text = "Copy ID to clipboard")
                                }
                            }
                        ) {
                            Icon(AppIcons.Outlined.ContentCopy, contentDescription = null)
                        }
                    }
                ) {
                    Text(text = "Task ID: $taskId")
                }
            }
        }
    }
}

@Composable
fun TaskDetailColumn(
    modifier: Modifier = Modifier,
    task: TaskItem,
    project: TaskProject?,
    onCompletedChange: (Boolean) -> Unit,
    onRequestCopyText: (String) -> Unit
) = TaskDetailColumn(
    modifier = modifier,
    taskId = task.id,
    title = task.title,
    content = task.content,
    projectColor = project?.color?.toComposeColor(),
    projectName = project?.name,
    tags = task.tags.orEmpty(),
    dueDate = task.dueDate,
    isCompleted = task.isCompleted,
    isArchived = task.isArchived,
    onCompletedChange = onCompletedChange,
    createdAt = task.createdAt,
    lastModifiedAt = task.lastModified,
    onRequestCopyText = onRequestCopyText
)

@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Preview(showBackground = true)
@PreviewFontScale
@PreviewScreenSizes
@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun TaskDetailColumnPreview() {
    val (isCompleted, setCompleted) = remember { mutableStateOf(true) }
    TaskDetailColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        taskId = "testing",
        title = "Finish Compose rewrite",
        content = SampleMarkdownText,
        projectName = "StudyBuddy Compose Rewrite",
        projectColor = LightColors.first(),
        tags = setOf("compose", "rewrite", "studybuddy"),
        dueDate = Instant.now() + Duration.ofDays(7),
        isCompleted = isCompleted,
        onCompletedChange = setCompleted,
        createdAt = Instant.now(),
        lastModifiedAt = Instant.now(),
        onRequestCopyText = { /* No-op */ }
    )
}
