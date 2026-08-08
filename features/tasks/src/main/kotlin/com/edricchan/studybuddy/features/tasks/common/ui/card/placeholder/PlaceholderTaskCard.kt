package com.edricchan.studybuddy.features.tasks.common.ui.card.placeholder

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider

/**
 * [TaskCard][com.edricchan.studybuddy.features.tasks.common.ui.card.TaskCard] composable for
 * the [TaskItem][com.edricchan.studybuddy.features.tasks.domain.model.TaskItem] content
 * that has yet to be loaded.
 */
@Composable
internal fun PlaceholderTaskCard(modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Overline
            Box(
                modifier = Modifier
                    .size(120.dp, 20.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.primary)
            )

            // Heading
            Box(
                modifier = Modifier
                    .size(240.dp, 40.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.onSurface)
            )

            // Content
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(ToggleButtonDefaults.MinHeight)
                        .weight(1f)
                        .clip(ToggleButtonDefaults.shape)
                        .border(ButtonDefaults.outlinedButtonBorder(), ToggleButtonDefaults.shape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(16.dp)
                )
                Box(
                    modifier = Modifier
                        .size(IconButtonDefaults.smallContainerSize())
                        .clip(IconButtonDefaults.filledShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun PlaceholderTaskCardPreview() {
    PlaceholderTaskCard()
}
