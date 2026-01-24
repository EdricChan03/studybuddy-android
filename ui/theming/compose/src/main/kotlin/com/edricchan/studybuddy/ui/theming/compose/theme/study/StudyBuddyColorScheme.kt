package com.edricchan.studybuddy.ui.theming.compose.theme.study

import androidx.compose.material3.ColorScheme
import com.edricchan.studybuddy.ui.theming.compose.theme.AppColorScheme
import com.edricchan.studybuddy.ui.theming.compose.theme.m3.expressive.StudyBuddyExpressiveDarkColors
import com.edricchan.studybuddy.ui.theming.compose.theme.m3.expressive.StudyBuddyExpressiveLightColors

object StudyBuddyColorScheme : AppColorScheme() {
    override val lightScheme: ColorScheme = StudyBuddyExpressiveLightColors
    override val darkScheme: ColorScheme = StudyBuddyExpressiveDarkColors
}
