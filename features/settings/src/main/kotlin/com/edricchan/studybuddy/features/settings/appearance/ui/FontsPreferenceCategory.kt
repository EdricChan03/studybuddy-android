package com.edricchan.studybuddy.features.settings.appearance.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import com.edricchan.studybuddy.core.settings.appearance.font.TypefaceSetting
import com.edricchan.studybuddy.ui.preference.compose.InfoPreference
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategory
import com.edricchan.studybuddy.ui.preference.compose.PreferenceDefaults
import com.edricchan.studybuddy.core.settings.appearance.resources.R as AppearanceR

@Composable
fun FontsPreferenceCategory(
    modifier: Modifier = Modifier,
    displayFontStyle: TypefaceSetting,
    onDisplayFontStyleChange: (TypefaceSetting) -> Unit,
    bodyFontStyle: TypefaceSetting,
    onBodyFontStyleChange: (TypefaceSetting) -> Unit
) {
    PreferenceCategory(
        modifier = modifier,
        listShape = RectangleShape,
        title = { Text(text = stringResource(AppearanceR.string.pref_category_fonts_title)) }
    ) {
        FontStylePreference(
            shape = PreferenceDefaults.itemShape,
            title = { Text(text = stringResource(AppearanceR.string.pref_display_font_style_title)) },
            value = displayFontStyle,
            onValueChange = onDisplayFontStyleChange
        )
        InfoPreference(
            text = {
                Text(text = stringResource(AppearanceR.string.pref_display_font_style_info_text))
            }
        )

        FontStylePreference(
            shape = PreferenceDefaults.itemShape,
            title = { Text(text = stringResource(AppearanceR.string.pref_body_font_style_title)) },
            value = bodyFontStyle,
            onValueChange = onBodyFontStyleChange
        )
        InfoPreference(
            text = {
                Text(text = stringResource(AppearanceR.string.pref_body_font_style_info_text))
            }
        )
    }
}
