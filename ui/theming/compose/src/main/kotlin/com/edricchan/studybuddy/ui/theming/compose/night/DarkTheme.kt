package com.edricchan.studybuddy.ui.theming.compose.night

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue
import com.edricchan.studybuddy.core.settings.appearance.proto.DarkModeSettingProto
import com.edricchan.studybuddy.ui.theming.common.night.shouldApplyDarkTheme as commonShouldApplyDarkTheme

@Composable
fun shouldApplyDarkTheme(
    context: Context = LocalContext.current,
    themeValue: DarkThemeValue.Version2
): Boolean = context.commonShouldApplyDarkTheme(
    themeValue = themeValue,
    isSystemInDarkTheme = isSystemInDarkTheme()
)

@Composable
fun shouldApplyDarkTheme(
    themeSetting: DarkModeSettingProto
): Boolean = commonShouldApplyDarkTheme(
    themeSetting = themeSetting,
    isSystemInDarkTheme = isSystemInDarkTheme()
)
