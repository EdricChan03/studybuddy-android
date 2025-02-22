package com.edricchan.studybuddy.features.tasks.ui.attrs

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.ui.utils.SampleMarkdownText
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.material3.RichText

/**
 * Composable to display a [TodoItem]'s content. The content is truncated to
 * a maximum of 5 lines - text after this limit are truncated by
 * [ellipses][TextOverflow.Ellipsis].
 *
 * Note: This composable does **not** render the text as Markdown. Use
 * [TaskContentRichText] instead if this behaviour is desired.
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
 * Composable to display a [TodoItem]'s content as [RichText]. The text
 * is rendered using [Markdown].
 *
 * To display the [text] as-is, use [TaskContentRawText] instead.
 * @param modifier [Modifier] to be passed to the [Text].
 * @param text The task's content.
 * @param richTextStyle [RichTextStyle] to use for the [RichText].
 */
@Composable
fun TaskContentRichText(
    modifier: Modifier = Modifier,
    text: String,
    richTextStyle: RichTextStyle? = null
) = RichText(modifier = modifier, style = richTextStyle) {
    Markdown(content = text)
}

/**
 * Composable which displays the [TodoItem]'s `content` field in a [ListItem].
 * @param modifier [Modifier] to be passed to the [ListItem].
 * @param textModifier [Modifier] to be passed to the [TaskContentRichText] or
 * [TaskContentRawText].
 * @param content The task's content.
 * @param useRichText Whether the [TaskContentRichText] composable should be used
 * to render the [content].
 * @param richTextStyle [RichTextStyle] for the [TaskContentRichText].
 */
@Composable
fun TaskContentListItem(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    content: String,
    useRichText: Boolean = true,
    richTextStyle: RichTextStyle? = null
) = ListItem(
    modifier = modifier,
    headlineContent = {
        if (useRichText) {
            TaskContentRichText(
                modifier = textModifier,
                text = content,
                richTextStyle = richTextStyle
            )
        } else {
            TaskContentRawText(modifier = textModifier, text = content)
        }
    }
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
