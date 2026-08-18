package com.edricchan.studybuddy.features.settings.general.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.OpenInBrowser
import com.edricchan.studybuddy.core.settings.general.resources.R
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategoryScope
import com.edricchan.studybuddy.ui.preference.compose.twostate.SwitchPreference

@Composable
internal fun PreferenceCategoryScope.InAppLinksPreference(
    modifier: Modifier = Modifier,
    openLinksInApp: Boolean,
    onOpenLinksInAppChange: (Boolean) -> Unit
) {
    SwitchPreference(
        modifier = modifier,
        icon = {
            Icon(
                AppIcons.Outlined.OpenInBrowser,
                contentDescription = null
            )
        },
        title = {
            Text(text = stringResource(R.string.pref_general_open_links_in_app_title))
        },
        checked = openLinksInApp,
        onCheckedChange = onOpenLinksInAppChange
    )
}
