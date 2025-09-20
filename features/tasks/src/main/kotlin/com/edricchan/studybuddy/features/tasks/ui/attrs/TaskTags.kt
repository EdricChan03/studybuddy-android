package com.edricchan.studybuddy.features.tasks.ui.attrs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.features.tasks.R
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

/**
 * Composable that displays the list of [TodoItem]'s [TodoItem.tags].
 * @param modifier [Modifier] for the [ListItem].
 * @param [tags] The list of tags to be displayed. Tags that are blank
 * will be filtered out.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TaskTagsListItem(
    modifier: Modifier = Modifier,
    tags: Set<String>
) {
    val displayTags = tags.filter(String::isNotBlank)
    if (displayTags.isNotEmpty()) {
        ListItem(
            modifier = modifier,
            leadingContent = {
                Icon(painterResource(R.drawable.ic_label_outline_24dp), contentDescription = null)
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
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskTagsListItemPreview() {
    StudyBuddyTheme {
        TaskTagsListItem(
            tags = setOf(
                "compose",
                "studybuddy",
                "tasks",
                "    " // Test isBlank check
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskTagsListItemManyTagsPreview() {
    StudyBuddyTheme {
        TaskTagsListItem(
            tags = (0..100).map { "Item $it" }.toSet()
        )
    }
}
