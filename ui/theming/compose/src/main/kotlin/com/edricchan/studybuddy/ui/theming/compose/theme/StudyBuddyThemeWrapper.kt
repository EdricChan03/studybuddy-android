package com.edricchan.studybuddy.ui.theming.compose.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

/** [PreviewWrapper] which wraps the content in the [StudyBuddyTheme] composable. */
class StudyBuddyThemeWrapper : PreviewWrapper {
    @Composable
    override fun Wrap(content: @Composable (() -> Unit)) {
        StudyBuddyTheme(content = content)
    }
}
