package com.edricchan.studybuddy.ui.theming.compose

import androidx.compose.runtime.Composable
import com.google.accompanist.themeadapter.material3.Mdc3Theme

@Composable
fun StudyBuddyCompatTheme(content: @Composable () -> Unit) =
    Mdc3Theme(content = content, setDefaultFontFamily = true)
