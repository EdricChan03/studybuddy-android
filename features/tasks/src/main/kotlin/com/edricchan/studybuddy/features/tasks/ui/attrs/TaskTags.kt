package com.edricchan.studybuddy.features.tasks.ui.attrs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Label
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider

/**
 * Composable that displays the list of [TaskItem.tags] of a [TaskItem].
 * @param modifier [Modifier] for the [ListItem].
 * @param [tags] The list of tags to be displayed. Tags that are blank
 * will be filtered out.
 * @param colors [ListItemColors] to be passed to [ListItem].
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TaskTagsListItem(
    modifier: Modifier = Modifier,
    tags: Set<String>,
    colors: ListItemColors = ListItemDefaults.colors()
) {
    val displayTags = tags.filter(String::isNotBlank)
    if (displayTags.isNotEmpty()) {
        ListItem(
            modifier = modifier,
            leadingContent = {
                Icon(AppIcons.Outlined.Label, contentDescription = null)
            },
            headlineContent = {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    displayTags.forEach {
                        AssistChip(
                            onClick = { /* no-op */ },
                            label = {
                                Text(
                                    text = it,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        )
                    }
                }
            },
            colors = colors
        )
    }
}

@Preview(showBackground = true)
@Composable
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
private fun TaskTagsListItemPreview() {
    TaskTagsListItem(
        tags = setOf(
            "compose",
            "studybuddy",
            "tasks",
            "    " // Test isBlank check
        )
    )
}

@Preview(showBackground = true)
@Composable
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
private fun TaskTagsListItemManyTagsPreview() {
    TaskTagsListItem(
        tags = (0..100).map { "Item $it" }.toSet()
    )
}
