package com.edricchan.studybuddy.features.tasks.ui.attrs

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem

/**
 * Composable to display a [TodoItem]'s content. The content is truncated to
 * a maximum of 5 lines - text after this limit are truncated by
 * [ellipses][TextOverflow.Ellipsis].
 *
 * Note: This composable does **not** render the text as Markdown.
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
