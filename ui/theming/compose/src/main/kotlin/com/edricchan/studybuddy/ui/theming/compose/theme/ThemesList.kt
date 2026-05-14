package com.edricchan.studybuddy.ui.theming.compose.theme

import com.edricchan.studybuddy.core.settings.appearance.AppThemeSetting

private val CustomThemes = AppThemeSetting.entries.filterNot { it == AppThemeSetting.Monet }

val AppThemes = buildList {
    if (supportsDynamicColor) {
        this += AppThemeSetting.Monet
    }

    addAll(CustomThemes)
}
