package com.edricchan.studybuddy.features.tasks.ui.attrs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.temporal.appFormat
import com.edricchan.studybuddy.core.resources.temporal.relative.formatRelativeTimeSpan
import com.edricchan.studybuddy.features.tasks.R
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
    text = LocalContext.current.formatRelativeTimeSpan(instant),
    style = MaterialTheme.typography.labelSmall
)

/**
 * Composable [ListItem] which displays a [TodoItem]'s [creation date][TodoItem.createdAt]
 * using [TaskCreatedAtText].
 * @param modifier [Modifier] to be used for the [ListItem].
 * @param textModifier [Modifier] to be used for the [TaskCreatedAtText].
 * @param createdAt The task's creation date as an [Instant].
 */
@Composable
fun TaskCreatedAtListItem(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    createdAt: Instant,
    formatDate: @Composable (Instant) -> String = {
        LocalContext.current.formatRelativeTimeSpan(it)
    },
) = ListItem(
    modifier = modifier,
    leadingContent = {
        Icon(
            painterResource(R.drawable.ic_access_time_outline_24dp),
            contentDescription = stringResource(
                R.string.view_task_created_at_content_desc
            )
        )
    },
    headlineContent = {
        TaskCreatedAtText(modifier = textModifier, text = formatDate(createdAt))
    }
)

/**
 * Composable which displays a [TodoItem]'s [creation date][TodoItem.createdAt]
 * using [TaskCreatedAtText]. This variant is intended to be displayed as an
 * overline for a card.
 * @param modifier [Modifier] to be used for the [Row].
 * @param textModifier [Modifier] to be used for the [TaskCreatedAtText].
 * @param createdAt The task's creation date as an [Instant].
 */
@Composable
fun TaskCreatedAtOverline(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    createdAt: Instant,
    formatDate: @Composable (Instant) -> String = {
        LocalContext.current.formatRelativeTimeSpan(it)
    },
) = Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    Icon(
        painterResource(R.drawable.ic_access_time_outline_24dp),
        contentDescription = stringResource(
            R.string.view_task_created_at_content_desc
        )
    )
    TaskCreatedAtText(modifier = textModifier, text = formatDate(createdAt))
}
