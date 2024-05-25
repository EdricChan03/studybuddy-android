package com.edricchan.studybuddy.features.tasks.ui.attrs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.edricchan.studybuddy.core.resources.temporal.appFormat
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import java.time.Instant

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
    formatDate: @Composable (Instant) -> String = {
        it.appFormat(LocalContext.current)
    },
    colors: ListItemColors = ListItemDefaults.colors()
) = ListItem(
    modifier = modifier,
    headlineContent = { Text(text = formatDate(dueDate)) },
    leadingContent = { Icon(Icons.Outlined.DateRange, contentDescription = null) },
    colors = colors
)

@Preview
@Composable
private fun TaskDueDateListItemPreview() {
    StudyBuddyTheme {
        TaskDueDateListItem(dueDate = Instant.now())
    }
}
