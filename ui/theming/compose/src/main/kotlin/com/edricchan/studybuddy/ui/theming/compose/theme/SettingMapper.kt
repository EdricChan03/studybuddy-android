package com.edricchan.studybuddy.ui.theming.compose.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import com.edricchan.studybuddy.core.settings.appearance.AppThemeSetting
import com.edricchan.studybuddy.ui.theming.compose.theme.monet.MonetColorScheme
import com.edricchan.studybuddy.ui.theming.compose.theme.study.StudyBuddyColorScheme

@Composable
@ReadOnlyComposable
fun AppThemeSetting.toAppColorScheme(): AppColorScheme {
    if (this == AppThemeSetting.Monet && supportsDynamicColor) return MonetColorScheme(LocalContext.current)

    return StudyBuddyColorScheme
}
