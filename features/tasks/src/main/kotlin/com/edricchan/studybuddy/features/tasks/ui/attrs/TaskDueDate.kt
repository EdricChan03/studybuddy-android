package com.edricchan.studybuddy.features.tasks.ui.attrs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.CalendarClock
import com.edricchan.studybuddy.core.resources.temporal.appFormat
import com.edricchan.studybuddy.core.resources.temporal.relative.formatRelativeToNow
import com.edricchan.studybuddy.features.tasks.R
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.features.tasks.ui.attrs.date.TaskDateDefaults
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime

/**
 * Composable to display a [TodoItem]'s due-date as a [ListItem].
 * @param modifier [Modifier] to be used for the [ListItem].
 * @param dueDate The [TodoItem]'s due-date as a [LocalDateTime].
 * @param formatDate Lambda used to format the [dueDate] to its string form.
 * @param colors Colours to be used for the [ListItem].
 */
@Composable
fun TaskDueDateListItem(
    modifier: Modifier = Modifier,
    dueDate: LocalDateTime,
    formatDate: @Composable (LocalDateTime) -> String = {
        it.appFormat()
    },
    colors: ListItemColors = ListItemDefaults.colors()
) = ListItem(
    modifier = modifier,
    headlineContent = { Text(text = formatDate(dueDate)) },
    leadingContent = { Icon(AppIcons.Outlined.CalendarClock, contentDescription = null) },
    colors = colors
)

/**
 * Composable to display a [TodoItem]'s [TodoItem.dueDate][due-date] as a [ListItem].
 * @param modifier [Modifier] to be used for the [ListItem].
 * @param dueDate The [TodoItem]'s due-date as an [Instant].
 * @param formatDate Lambda used to format the [dueDate] to its string form.
 * @param colors Colours to be used for the [ListItem].
 */
@Composable
fun TaskDueDateListItem(
    modifier: Modifier = Modifier,
    dueDate: Instant,
    formatDate: (Instant) -> String = TaskDateDefaults.FormatInstantFn,
    colors: ListItemColors = ListItemDefaults.colors()
) = ListItem(
    modifier = modifier,
    headlineContent = { Text(text = formatDate(dueDate)) },
    leadingContent = { Icon(AppIcons.Outlined.CalendarClock, contentDescription = null) },
    colors = colors
)

@Preview
@Composable
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
private fun TaskDueDateListItemPreview() {
    TaskDueDateListItem(dueDate = Instant.now())
}

/**
 * Composable to display a [TaskItem]'s due-date as a chip.
 * @param modifier [Modifier] to be passed to the outer [Surface].
 * @param dueDate Due date to be shown.
 * @param isOverdue Whether a red background colour should be used if this lambda returns
 * `true`.
 */
@Composable
fun TaskDueDateChip(
    modifier: Modifier = Modifier,
    dueDate: Instant,
    isOverdue: () -> Boolean = { dueDate.isBefore(Instant.now()) }
) = Surface(
    modifier = modifier,
    color = if (isOverdue()) MaterialTheme.colorScheme.errorContainer
    else MaterialTheme.colorScheme.tertiaryContainer,
    shape = MaterialTheme.shapes.small
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            AppIcons.Outlined.CalendarClock,
            contentDescription = stringResource(R.string.view_task_due_date_content_desc)
        )
        context(LocalContext.current) {
            Text(
                text = dueDate.formatRelativeToNow(),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Preview
@Composable
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
private fun TaskDueDateChipPreview() {
    TaskDueDateChip(dueDate = Instant.now() + Duration.ofDays(1))
}

@Preview
@Composable
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
private fun TaskDueDateOverdueChipPreview() {
    TaskDueDateChip(dueDate = Instant.now() - Duration.ofDays(1))
}
