package com.edricchan.studybuddy.features.settings.appearance.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edricchan.studybuddy.core.settings.appearance.AppThemeSetting
import com.edricchan.studybuddy.core.settings.appearance.DarkModeSetting
import com.edricchan.studybuddy.core.settings.appearance.font.TypefaceSetting
import com.edricchan.studybuddy.features.settings.appearance.vm.AppearanceSettingsViewModel
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider

@Composable
fun AppearanceSettingsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    darkTheme: DarkModeSetting,
    onDarkThemeChange: (DarkModeSetting) -> Unit,
    appTheme: AppThemeSetting,
    onAppThemeChange: (AppThemeSetting) -> Unit,
    displayFontStyle: TypefaceSetting,
    onDisplayFontStyleChange: (TypefaceSetting) -> Unit,
    bodyFontStyle: TypefaceSetting,
    onBodyFontStyleChange: (TypefaceSetting) -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
    ) {
        // Theming category
        ThemingPreferenceCategory(
            appTheme = appTheme,
            onAppThemeChange = onAppThemeChange,
            darkTheme = darkTheme,
            onDarkThemeChange = onDarkThemeChange
        )

        // Fonts category
        FontsPreferenceCategory(
            displayFontStyle = displayFontStyle,
            onDisplayFontStyleChange = onDisplayFontStyleChange,
            bodyFontStyle = bodyFontStyle,
            onBodyFontStyleChange = onBodyFontStyleChange
        )
    }
}

@Composable
fun AppearanceSettingsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: AppearanceSettingsViewModel,
    onDarkThemeChange: (DarkModeSetting) -> Unit = {},
    onAppThemeChange: (AppThemeSetting) -> Unit = {}
) {
    val darkTheme by viewModel.darkMode.collectAsStateWithLifecycle()
    val appTheme by viewModel.appTheme.collectAsStateWithLifecycle()
    val displayFontStyle by viewModel.displayFontStyle.collectAsStateWithLifecycle()
    val bodyFontStyle by viewModel.bodyFontStyle.collectAsStateWithLifecycle()

    AppearanceSettingsScreen(
        modifier = modifier,
        contentPadding = contentPadding,
        darkTheme = darkTheme,
        onDarkThemeChange = {
            viewModel.onDarkThemeChange(it)
            onDarkThemeChange(it)
        },
        appTheme = appTheme,
        onAppThemeChange = {
            viewModel.onAppThemeChange(it)
            onAppThemeChange(it)
        },
        displayFontStyle = displayFontStyle,
        onDisplayFontStyleChange = viewModel::onDisplayFontStyleChange,
        bodyFontStyle = bodyFontStyle,
        onBodyFontStyleChange = viewModel::onBodyFontStyleChange,
    )
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun AppearanceSettingsScreenPreview() {
    val (darkTheme, onDarkThemeChange) = remember { mutableStateOf(DarkModeSetting.FollowSystem) }
    val (appTheme, onAppThemeChange) = remember { mutableStateOf(AppThemeSetting.Monet) }
    val (displayFontStyle, onDisplayFontStyleChange) = remember {
        mutableStateOf(TypefaceSetting.FunnelDisplay)
    }
    val (bodyFontStyle, onBodyFontStyleChange) = remember { mutableStateOf(TypefaceSetting.Baloo2) }

    AppearanceSettingsScreen(
        darkTheme = darkTheme,
        onDarkThemeChange = onDarkThemeChange,
        appTheme = appTheme,
        onAppThemeChange = onAppThemeChange,
        displayFontStyle = displayFontStyle,
        onDisplayFontStyleChange = onDisplayFontStyleChange,
        bodyFontStyle = bodyFontStyle,
        onBodyFontStyleChange = onBodyFontStyleChange
    )
}
