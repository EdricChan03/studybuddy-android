package com.edricchan.studybuddy.features.tasks.ui.attrs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

/**
 * Composable to display a [TodoItem]'s title. The title is truncated to
 * a maximum of 2 lines - text after this limit are truncated by
 * [ellipses][TextOverflow.Ellipsis].
 *
 * @param modifier [Modifier] to be passed to the [Text].
 * @param text The task's title.
 * @param isDone Whether the task was marked as done. If `true`, the
 * text is rendered with a [line-through][TextDecoration.LineThrough].
 */
@Composable
fun TaskTitleText(
    modifier: Modifier = Modifier,
    text: String,
    isDone: Boolean = false
) = Text(
    modifier = modifier,
    text = text,
    style = MaterialTheme.typography.titleLarge.copy(
        textDecoration = if (isDone)
            TextDecoration.LineThrough else TextDecoration.None
    ),
    maxLines = 2,
    overflow = TextOverflow.Ellipsis
)

/**
 * Composable which displays a [TodoItem]'s title using a [ListItem] and [TaskTitleText].
 *
 * To display a checkbox as well, use [TaskTitleWithCheckboxListItem].
 * @param modifier [Modifier] to be passed to the [ListItem].
 * @param textModifier [Modifier] to be passed to the inner [TaskTitleText].
 * @param title The task's title.
 * @param isDone Whether the task was marked as done. See [TaskTitleText]'s `isDone`
 * parameter for more info.
 * @param colors Colours to be used for the [ListItem].
 */
@Composable
fun TaskTitleListItem(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    title: String,
    isDone: Boolean = false,
    colors: ListItemColors = ListItemDefaults.colors()
) = ListItem(
    modifier = modifier,
    headlineContent = {
        SelectionContainer {
            TaskTitleText(modifier = textModifier, text = title, isDone = isDone)
        }
    },
    colors = colors
)

/**
 * Composable which displays a [TodoItem]'s title using a [ListItem] and [TaskTitleText].
 * A [Checkbox] will be shown as the leading content.
 * @param modifier [Modifier] to be passed to the [ListItem].
 * @param textModifier [Modifier] to be passed to the inner [TaskTitleText].
 * @param title The task's title.
 * @param isDone Whether the task was marked as done. See [TaskTitleText]'s `isDone`
 * parameter for more info.
 * @param isArchived Whether the [Checkbox] should be disabled.
 * @param colors Colours to be used for the [ListItem].
 */
@Composable
fun TaskTitleWithCheckboxListItem(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    title: String,
    isDone: Boolean = false,
    onDoneChange: (Boolean) -> Unit,
    isArchived: Boolean = false,
    colors: ListItemColors = ListItemDefaults.colors(),
) = ListItem(
    modifier = modifier,
    headlineContent = {
        SelectionContainer {
            TaskTitleText(modifier = textModifier, text = title, isDone = isDone)
        }
    },
    leadingContent = {
        Checkbox(
            checked = isDone, onCheckedChange = onDoneChange, enabled = !isArchived
        )
    },
    colors = colors
)

@Preview(showBackground = true)
@Composable
private fun TaskTitleWithCheckboxListItemPreview() {
    var isDone by remember { mutableStateOf(false) }
    var isDone2 by remember { mutableStateOf(true) }
    StudyBuddyTheme {
        Column {
            TaskTitleWithCheckboxListItem(
                title = "Finish Compose tasks rewrite",
                isDone = isDone,
                onDoneChange = { isDone = it }
            )
            TaskTitleWithCheckboxListItem(
                title = "Add project data",
                isDone = isDone2,
                isArchived = true,
                onDoneChange = { isDone2 = it }
            )
        }
    }
}
