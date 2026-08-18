package com.edricchan.studybuddy.features.settings.general.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.BugReport
import com.edricchan.studybuddy.features.settings.R
import com.edricchan.studybuddy.features.settings.general.vm.GeneralSettingsViewModel
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategory
import com.edricchan.studybuddy.ui.preference.compose.twostate.SwitchPreference
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider

@Composable
fun GeneralSettingsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    enableUserTracking: Boolean,
    onEnableUserTrackingChange: (Boolean) -> Unit,
    openLinksInApp: Boolean,
    onOpenLinksInAppChange: (Boolean) -> Unit
) = Column(
    modifier = modifier
        .verticalScroll(rememberScrollState())
        .padding(contentPadding)
) {
    PreferenceCategory {
        SwitchPreference(
            icon = {
                Icon(
                    AppIcons.Outlined.BugReport,
                    contentDescription = null
                )
            },
            title = {
                Text(text = stringResource(R.string.pref_enable_crashlytics_user_tracking_title))
            },
            subtitle = { Text(text = stringResource(R.string.pref_enable_crashlytics_user_tracking_summary)) },
            checked = enableUserTracking,
            onCheckedChange = onEnableUserTrackingChange
        )

        InAppLinksPreference(
            openLinksInApp = openLinksInApp,
            onOpenLinksInAppChange = onOpenLinksInAppChange
        )
    }
}

@Composable
fun GeneralSettingsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: GeneralSettingsViewModel
) {
    val enableUserTracking by viewModel.prefEnableUserTracking.asFlow().collectAsStateWithLifecycle(
        initialValue = false
    )
    val openLinksInApp by viewModel.openLinksInApp.collectAsStateWithLifecycle()

    GeneralSettingsScreen(
        modifier = modifier,
        contentPadding = contentPadding,
        enableUserTracking = enableUserTracking,
        onEnableUserTrackingChange = viewModel.prefEnableUserTracking::set,
        openLinksInApp = openLinksInApp,
        onOpenLinksInAppChange = viewModel::setOpenLinksInApp
    )
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun GeneralSettingsScreenPreview() {
    var enableUserTracking by remember { mutableStateOf(false) }
    var useCustomTabs by remember { mutableStateOf(true) }

    GeneralSettingsScreen(
        enableUserTracking = enableUserTracking,
        onEnableUserTrackingChange = { enableUserTracking = it },
        openLinksInApp = useCustomTabs,
        onOpenLinksInAppChange = { useCustomTabs = it }
    )
}
