package com.edricchan.studybuddy.features.settings.appearance.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.FormatFont
import com.edricchan.studybuddy.core.settings.appearance.font.TypefaceSetting
import com.edricchan.studybuddy.core.settings.appearance.font.labelResource
import com.edricchan.studybuddy.ui.preference.compose.ListDialogPreference
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategoryScope
import com.edricchan.studybuddy.ui.preference.compose.PreferenceDefaults
import com.edricchan.studybuddy.ui.theming.compose.theme.font.toFontFamily

@Composable
internal fun PreferenceCategoryScope.FontStylePreference(
    modifier: Modifier = Modifier,
    shape: Shape = PreferenceDefaults.categoryItemShape,
    title: @Composable () -> Unit,
    values: List<TypefaceSetting> = TypefaceSetting.entries,
    value: TypefaceSetting,
    onValueChange: (TypefaceSetting) -> Unit
) = ListDialogPreference(
    modifier = modifier,
    shape = shape,
    icon = {
        Icon(
            AppIcons.Outlined.FormatFont,
            contentDescription = null
        )
    },
    title = title,
    subtitle = {
        val fontFamily = value.toFontFamily()
        Text(text = stringResource(value.labelResource), fontFamily = fontFamily)
    },
    values = values,
    value = value,
    onValueChanged = onValueChange,
    valueLabel = {
        val fontFamily = it.toFontFamily()
        Text(text = stringResource(it.labelResource), fontFamily = fontFamily)
    }
)
