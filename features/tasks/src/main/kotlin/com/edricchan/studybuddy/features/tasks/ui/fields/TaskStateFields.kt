package com.edricchan.studybuddy.features.tasks.ui.fields

import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.edricchan.studybuddy.features.tasks.R

@Composable
fun TaskMarkCompletedListItem(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    colors: ListItemColors = ListItemDefaults.colors(),
    shapes: ListItemShapes
) = ListItem(
    modifier = modifier,
    checked = checked,
    onCheckedChange = onCheckedChange,
    enabled = enabled,
    leadingContent = {
        Checkbox(
            checked = checked,
            onCheckedChange = null
        )
    },
    content = {
        Text(
            text = stringResource(R.string.action_mark_task_as_done)
        )
    },
    colors = colors,
    shapes = shapes
)

@Composable
fun TaskMarkArchivedListItem(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    colors: ListItemColors = ListItemDefaults.colors(),
    shapes: ListItemShapes
) = ListItem(
    modifier = modifier,
    checked = checked,
    onCheckedChange = onCheckedChange,
    enabled = enabled,
    leadingContent = {
        Checkbox(
            checked = checked,
            onCheckedChange = null
        )
    },
    content = {
        Text(
            text = stringResource(R.string.action_mark_task_as_archived)
        )
    },
    colors = colors,
    shapes = shapes
)
