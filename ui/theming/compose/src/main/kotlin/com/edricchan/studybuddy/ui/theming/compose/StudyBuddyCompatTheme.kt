package com.edricchan.studybuddy.ui.theming.compose

import androidx.compose.runtime.Composable
import com.google.accompanist.themeadapter.material3.Mdc3Theme

@Deprecated(
    "This composable is used for backwards-compatibility with the existing theming " +
        "system. Once the theming system has been migrated to Compose, use the " +
        "StudyBuddyTheme composable instead",
    ReplaceWith(
        "StudyBuddyTheme(content)",
        "com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme"
    )
)
@Composable
fun StudyBuddyCompatTheme(content: @Composable () -> Unit) =
    Mdc3Theme(content = content, setDefaultFontFamily = true)
