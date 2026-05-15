package com.edricchan.studybuddy.features.settings.appearance.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.DarkMode
import com.edricchan.studybuddy.core.settings.appearance.DarkModeSetting
import com.edricchan.studybuddy.core.settings.appearance.labelResource
import com.edricchan.studybuddy.ui.preference.compose.ListDialogPreference
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategoryScope
import com.edricchan.studybuddy.core.settings.appearance.resources.R as AppearanceR

@Composable
internal fun PreferenceCategoryScope.DarkModePreference(
    modifier: Modifier = Modifier,
    values: List<DarkModeSetting> = DarkModeSetting.entries,
    value: DarkModeSetting,
    onValueChange: (DarkModeSetting) -> Unit
) = ListDialogPreference(
    modifier = modifier,
    icon = {
        Icon(
            AppIcons.Outlined.DarkMode,
            contentDescription = null
        )
    },
    title = { Text(text = stringResource(AppearanceR.string.pref_dark_theme_title)) },
    subtitle = { Text(text = stringResource(value.labelResource)) },
    values = values,
    value = value,
    valueLabel = { Text(text = stringResource(it.labelResource)) },
    onValueChanged = onValueChange
)
