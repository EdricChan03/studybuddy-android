package com.edricchan.studybuddy.ui.theming.compose.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

/** [PreviewWrapperProvider] which wraps the content in the [StudyBuddyTheme] composable. */
class StudyBuddyThemeWrapperProvider : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable (() -> Unit)) {
        StudyBuddyTheme(content = content)
    }
}
