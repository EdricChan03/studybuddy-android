package com.edricchan.studybuddy.features.tasks.ui.attrs

import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.ui.utils.SampleMarkdownText
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.edricchan.studybuddy.ui.widgets.compose.markdown.MarkdownViewer

/**
 * Composable to display a [TodoItem]'s content. The content is truncated to
 * a maximum of 5 lines - text after this limit are truncated by
 * [ellipses][TextOverflow.Ellipsis].
 *
 * Note: This composable does **not** render the text as Markdown. Use
 * [TaskContentMarkdownText] instead if this behaviour is desired.
 * @param modifier [Modifier] to be passed to the [Text].
 * @param text The task's content.
 */
@Composable
fun TaskContentRawText(
    modifier: Modifier = Modifier,
    text: String
) = Text(
    modifier = modifier,
    text = text,
    maxLines = 5,
    overflow = TextOverflow.Ellipsis
)

/**
 * Composable to display a [TodoItem]'s content as markdown. The text
 * is rendered using [MarkdownViewer].
 *
 * To display the [text] as-is, use [TaskContentRawText] instead.
 * @param modifier [Modifier] to be passed to the [Text].
 * @param text The task's content.
 */
@Composable
fun TaskContentMarkdownText(
    modifier: Modifier = Modifier,
    text: String
) = MarkdownViewer(modifier = modifier, markdownText = text)

/**
 * Composable which displays the [TodoItem]'s `content` field in a [ListItem].
 * @param modifier [Modifier] to be passed to the [ListItem].
 * @param textModifier [Modifier] to be passed to the [TaskContentMarkdownText] or
 * [TaskContentRawText].
 * @param content The task's content.
 * @param shouldRenderAsMarkdown Whether the [TaskContentMarkdownText] composable should be used
 * to render the [content].
 */
@Composable
fun TaskContentListItem(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    content: String,
    shouldRenderAsMarkdown: Boolean = true,
    colors: ListItemColors = ListItemDefaults.colors()
) = ListItem(
    modifier = modifier,
    headlineContent = {
        if (shouldRenderAsMarkdown) {
            TaskContentMarkdownText(
                modifier = textModifier,
                text = content
            )
        } else {
            TaskContentRawText(modifier = textModifier, text = content)
        }
    },
    colors = colors
)

@Preview(showBackground = true)
@Composable
private fun TaskContentListItemPreview() {
    StudyBuddyTheme {
        TaskContentListItem(
            content = SampleMarkdownText
        )
    }
}
