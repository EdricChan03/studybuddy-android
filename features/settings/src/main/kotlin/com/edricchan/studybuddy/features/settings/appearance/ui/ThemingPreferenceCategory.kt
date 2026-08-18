package com.edricchan.studybuddy.features.settings.appearance.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.edricchan.studybuddy.core.settings.appearance.AppThemeSetting
import com.edricchan.studybuddy.core.settings.appearance.DarkModeSetting
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategory
import com.edricchan.studybuddy.core.settings.appearance.resources.R as AppearanceR

@Composable
internal fun ThemingPreferenceCategory(
    modifier: Modifier = Modifier,
    appTheme: AppThemeSetting,
    onAppThemeChange: (AppThemeSetting) -> Unit,
    darkTheme: DarkModeSetting,
    onDarkThemeChange: (DarkModeSetting) -> Unit
) {
    PreferenceCategory(
        modifier = modifier,
        title = { Text(text = stringResource(AppearanceR.string.pref_category_theming_title)) }
    ) {
        AppThemePreference(
            selectedTheme = appTheme,
            onThemeChange = onAppThemeChange
        )
        DarkModePreference(
            value = darkTheme,
            onValueChange = onDarkThemeChange
        )
    }
}
