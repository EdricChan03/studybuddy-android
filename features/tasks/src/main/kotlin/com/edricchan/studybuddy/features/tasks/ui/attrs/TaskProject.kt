package com.edricchan.studybuddy.features.tasks.ui.attrs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Assignment
import com.edricchan.studybuddy.data.common.compose.toComposeColor
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.features.tasks.domain.model.TaskProject
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider

/**
 * Displays a [CircleShape] surface with the specified [color].
 * @param modifier [Modifier] to be passed to the [Surface].
 * @param color The background colour for the [Surface].
 * @param border The border for the [Surface].
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TaskProjectCircle(
    modifier: Modifier = Modifier,
    color: Color?,
    border: BorderStroke? = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
) = Surface(
    modifier = modifier.size(16.dp),
    shape = MaterialShapes.Clover4Leaf.toShape(),
    color = color ?: MaterialTheme.colorScheme.surface,
    border = border,
    content = {}
)

/**
 * Composable which displays a [TaskProject]'s [name][TaskProject.name].
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
 * Composable which displays a [TaskItem]'s [TaskItem.project] as a [ListItem].
 * @param modifier [Modifier] to be used for the [ListItem].
 * @param color The [TaskProject.color] as a Compose [Color].
 * This value is used for the [ListItem]'s trailing [TaskProjectCircle].
 * @param name The [TaskProject]'s [TaskProject.name].
 * @param colors Colours to be used for the [ListItem] - see [ListItemColors].
 * @param shapes [ListItemShapes] to be used for the [ListItem].
 */
@Composable
fun TaskProjectListItem(
    modifier: Modifier = Modifier,
    color: Color? = null,
    name: String,
    colors: ListItemColors = ListItemDefaults.colors(),
    shapes: ListItemShapes = ListItemDefaults.shapes()
) = ListItem(
    modifier = modifier,
    leadingContent = {
        Icon(AppIcons.Outlined.Assignment, contentDescription = null)
    },
    content = {
        TaskProjectText(text = name)
    },
    trailingContent = { TaskProjectCircle(color = color) },
    colors = colors,
    shapes = shapes
)

/**
 * Composable which displays a [TaskItem]'s [TaskItem.project] as a [ListItem].
 *
 * This variant allows for a [TaskProject] to be used.
 * @param modifier [Modifier] to be used for the [ListItem].
 * @param project The [TaskProject] to display.
 */
@Composable
fun TaskProjectListItem(
    modifier: Modifier = Modifier,
    project: TaskProject,
    colors: ListItemColors = ListItemDefaults.colors(),
    shapes: ListItemShapes = ListItemDefaults.shapes()
) = TaskProjectListItem(
    modifier = modifier,
    color = project.color?.toComposeColor(),
    name = project.name,
    colors = colors,
    shapes = shapes
)

@Preview(showBackground = true)
@Composable
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
private fun TaskProjectRowPreview() {
    TaskProjectListItem(
        color = Color(red = 0xFF, green = 0xFF, blue = 0x00),
        name = "StudyBuddy - Compose Rewrite"
    )
}
