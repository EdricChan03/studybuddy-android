package com.edricchan.studybuddy.features.tasks.ui.attrs

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.edricchan.studybuddy.core.resources.temporal.appFormat
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import java.time.Instant

/**
 * Composable which displays a [TodoItem]'s [creation date][TodoItem.createdAt].
 *
 * The [text] should be formatted to its human-readable form.
 * @param modifier [Modifier] to be used for the [Text].
 * @param text Human-readable form of [TodoItem.createdAt].
 */
@Composable
fun TaskCreatedAtText(
    modifier: Modifier = Modifier,
    text: String
) = Text(
    modifier = modifier,
    text = text,
    style = MaterialTheme.typography.labelSmall
)

/**
 * Composable which displays a [TodoItem]'s [creation date][TodoItem.createdAt].
 *
 * The [instant] will be formatted using [appFormat].
 * @param modifier [Modifier] to be used for the [Text].
 * @param instant A Java Time [Instant] to be displayed.
 */
@Composable
fun TaskCreatedAtText(
    modifier: Modifier = Modifier,
    instant: Instant
) = Text(
    modifier = modifier,
    text = instant.appFormat(LocalContext.current),
    style = MaterialTheme.typography.labelSmall
)
