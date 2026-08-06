package com.edricchan.studybuddy.features.tasks.detail.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.data.common.compose.toComposeColor
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.features.tasks.domain.model.TaskProject
import com.edricchan.studybuddy.features.tasks.ui.attrs.TaskContentMarkdownText
import com.edricchan.studybuddy.features.tasks.ui.attrs.TaskDueDateListItem
import com.edricchan.studybuddy.features.tasks.ui.attrs.TaskProjectListItem
import com.edricchan.studybuddy.features.tasks.ui.attrs.TaskTagsListItem
import com.edricchan.studybuddy.features.tasks.ui.attrs.TaskTitleWithCheckboxListItem
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider
import com.edricchan.studybuddy.ui.widgets.compose.markdown.SampleMarkdownText
import com.edricchan.studybuddy.utils.compose.graphics.preview.LightColors
import com.edricchan.studybuddy.utils.compose.material3.list.SegmentedListColumn
import java.time.Instant

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TaskDetailColumn(
    modifier: Modifier = Modifier,
    title: String,
    content: String? = null,
    projectColor: Color? = null,
    projectName: String? = null,
    tags: Set<String> = emptySet(),
    dueDate: Instant? = null,
    isCompleted: Boolean = false,
    onCompletedChange: (Boolean) -> Unit,
    isArchived: Boolean = false
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    TaskTitleWithCheckboxListItem(
        modifier = Modifier.fillMaxWidth(),
        title = title,
        isDone = isCompleted,
        onDoneChange = onCompletedChange,
        isArchived = isArchived
    )
    content?.let {
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
    SegmentedListColumn {
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
}

@Composable
fun TaskDetailColumn(
    modifier: Modifier = Modifier,
    task: TaskItem,
    project: TaskProject?,
    onCompletedChange: (Boolean) -> Unit
) = TaskDetailColumn(
    modifier = modifier,
    title = task.title,
    content = task.content,
    projectColor = project?.color?.toComposeColor(),
    projectName = project?.name,
    tags = task.tags.orEmpty(),
    dueDate = task.dueDate,
    isCompleted = task.isCompleted,
    isArchived = task.isArchived,
    onCompletedChange = onCompletedChange
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
        title = "Finish Compose rewrite",
        content = SampleMarkdownText,
        projectName = "StudyBuddy Compose Rewrite",
        projectColor = LightColors.first(),
        tags = setOf("compose", "rewrite", "studybuddy"),
        dueDate = Instant.now(),
        isCompleted = isCompleted,
        onCompletedChange = setCompleted
    )
}
