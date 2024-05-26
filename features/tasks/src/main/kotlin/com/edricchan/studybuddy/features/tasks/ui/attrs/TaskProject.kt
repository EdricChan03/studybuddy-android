package com.edricchan.studybuddy.features.tasks.ui.attrs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.features.tasks.R
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.model.TodoProject
import com.edricchan.studybuddy.features.tasks.ui.utils.composeColor
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

/**
 * Displays a [CircleShape] surface with the specified [color].
 * @param modifier [Modifier] to be passed to the [Surface].
 * @param color The background colour for the [Surface].
 * @param border The border for the [Surface].
 */
@Composable
fun TaskProjectCircle(
    modifier: Modifier = Modifier,
    color: Color?,
    border: BorderStroke? = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
) = Surface(
    modifier = modifier.size(16.dp),
    shape = CircleShape,
    color = color ?: MaterialTheme.colorScheme.surface,
    border = border,
    content = {}
)

/**
 * Composable which displays a [TodoProject]'s [name][TodoProject.name].
 *
 * The title is truncated to a maximum of 2 lines - text after this limit
 * are truncated by [ellipses][TextOverflow.Ellipsis].
 * @param modifier [Modifier] to be used for the [Text].
 * @param text The text to display.
 */
@Composable
fun TaskProjectText(
    modifier: Modifier = Modifier,
    text: String
) = Text(
    modifier = modifier,
    text = text,
    maxLines = 2,
    overflow = TextOverflow.Ellipsis
)

/**
 * Composable which displays a [TodoItem]'s [TodoItem.project] as a [ListItem].
 * @param modifier [Modifier] to be used for the [ListItem].
 * @param color The [TodoProject.color] as a Compose [Color].
 * This value is used for the [ListItem]'s trailing [TaskProjectCircle].
 * @param name The [TodoProject]'s [TodoProject.name].
 */
@Composable
fun TaskProjectListItem(
    modifier: Modifier = Modifier,
    color: Color? = null,
    name: String
) = ListItem(
    modifier = modifier,
    leadingContent = {
        Icon(painterResource(R.drawable.ic_checklist_outline_24dp), contentDescription = null)
    },
    headlineContent = {
        TaskProjectText(text = name)
    },
    trailingContent = { TaskProjectCircle(color = color) }
)

/**
 * Composable which displays a [TodoItem]'s [TodoItem.project] as a [ListItem].
 *
 * This variant allows for a [TodoProject] to be used.
 * @param modifier [Modifier] to be used for the [ListItem].
 * @param project The [TodoProject] to display. A default [name][TodoProject.name]
 * will be used if the project does not specify one.
 */
@Composable
fun TaskProjectListItem(
    modifier: Modifier = Modifier,
    project: TodoProject
) = TaskProjectListItem(
    modifier = modifier,
    color = project.color?.composeColor,
    // TODO: Remove default name when the project's name is required
    name = project.name ?: stringResource(R.string.task_attr_project_name_default)
)

@Preview(showBackground = true)
@Composable
private fun TaskProjectRowPreview() {
    StudyBuddyTheme {
        TaskProjectListItem(
            color = Color(red = 0xFF, green = 0xFF, blue = 0x00),
            name = "StudyBuddy - Compose Rewrite"
        )
    }
}
