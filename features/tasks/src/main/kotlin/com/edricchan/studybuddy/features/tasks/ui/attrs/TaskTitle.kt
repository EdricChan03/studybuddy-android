package com.edricchan.studybuddy.features.tasks.ui.attrs

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow

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
