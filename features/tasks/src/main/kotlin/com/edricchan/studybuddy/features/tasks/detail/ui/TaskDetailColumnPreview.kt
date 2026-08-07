package com.edricchan.studybuddy.features.tasks.detail.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider
import com.edricchan.studybuddy.ui.widgets.compose.markdown.SampleMarkdownText
import com.edricchan.studybuddy.utils.compose.graphics.preview.ColorPreviewParameterProvider
import java.time.Instant

@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Preview(showBackground = true)
@PreviewFontScale
@PreviewScreenSizes
@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun TaskColumnPreview(
    @PreviewParameter(ColorPreviewParameterProvider::class) projectColor: Color
) {
    val (isCompleted, setCompleted) = remember { mutableStateOf(true) }
    TaskDetailColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        taskId = "testing",
        title = "Finish Compose rewrite",
        content = SampleMarkdownText,
        projectName = "StudyBuddy Compose Rewrite",
        projectColor = projectColor,
        tags = setOf("compose", "rewrite", "studybuddy"),
        dueDate = Instant.now(),
        isCompleted = isCompleted,
        onCompletedChange = setCompleted,
        createdAt = Instant.now(),
        lastModifiedAt = Instant.now(),
        onRequestCopyText = { /* no-op */ }
    )
}
