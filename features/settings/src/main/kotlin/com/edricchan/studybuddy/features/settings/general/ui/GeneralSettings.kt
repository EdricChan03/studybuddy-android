package com.edricchan.studybuddy.features.settings.general.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue
import com.edricchan.studybuddy.features.settings.R
import com.edricchan.studybuddy.features.settings.general.vm.GeneralSettingsViewModel
import com.edricchan.studybuddy.ui.preference.compose.ListDialogPreference
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategory
import com.edricchan.studybuddy.ui.preference.compose.twostate.SwitchPreference
import com.edricchan.studybuddy.ui.theming.common.dynamic.isDynamicColorAvailable
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme


@get:StringRes
val DarkThemeValue.Version2.labelResource
    get() = when (this) {
        DarkThemeValue.V2Always -> R.string.pref_dark_theme_entry_always
        DarkThemeValue.V2Never -> R.string.pref_dark_theme_entry_never
        DarkThemeValue.V2FollowSystem -> R.string.pref_dark_theme_entry_system
    }

@Composable
fun GeneralSettingsScreen(
    modifier: Modifier = Modifier,
    enableUserTracking: Boolean,
    onEnableUserTrackingChange: (Boolean) -> Unit,
    useCustomTabs: Boolean,
    onUseCustomTabsChange: (Boolean) -> Unit,
    useDarkTheme: DarkThemeValue.Version2,
    onDarkThemeChange: (DarkThemeValue.Version2) -> Unit,
    enableDynamicTheme: Boolean,
    onDynamicThemeChange: (Boolean) -> Unit,
    isDynamicThemeAvailable: Boolean = isDynamicColorAvailable
) = Column(modifier = modifier.verticalScroll(rememberScrollState())) {
    SwitchPreference(
        icon = {
            Icon(
                painterResource(R.drawable.ic_bug_report_outline_24dp),
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

    SwitchPreference(
        icon = {
            Icon(
                painterResource(R.drawable.ic_open_in_browser_24dp),
                contentDescription = null
            )
        },
        title = {
            Text(text = stringResource(R.string.pref_use_custom_tabs_title))
        },
        checked = useCustomTabs,
        onCheckedChange = onUseCustomTabsChange
    )

    PreferenceCategory(
        title = { Text(text = stringResource(R.string.pref_category_theme)) }
    ) {
        ListDialogPreference(
            icon = {
                Icon(
                    painterResource(R.drawable.ic_dark_mode_outline_24dp),
                    contentDescription = null
                )
            },
            title = { Text(text = stringResource(R.string.pref_dark_theme_title)) },
            subtitle = {
                // FIXME: Remove !! operator
                Text(text = stringResource(useDarkTheme.labelResource))
            },
            values = DarkThemeValue.Version2.entries,
            value = useDarkTheme,
            onValueChanged = onDarkThemeChange,
            valueLabel = { value ->
                Text(text = stringResource(value.labelResource))
            }
        )

        if (isDynamicThemeAvailable) {
            SwitchPreference(
                icon = {
                    Icon(
                        painterResource(R.drawable.ic_auto_awesome_outline_24dp),
                        contentDescription = null
                    )
                },
                title = { Text(text = stringResource(R.string.pref_dynamic_theme_title)) },
                subtitle = { Text(text = stringResource(R.string.pref_dynamic_theme_summary)) },
                checked = enableDynamicTheme,
                onCheckedChange = onDynamicThemeChange
            )
        }
    }
}

@Composable
fun GeneralSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: GeneralSettingsViewModel,
    onDynamicThemeChange: (Boolean) -> Unit = {},
    onDarkThemeChange: (DarkThemeValue) -> Unit = {}
) {
    val enableUserTracking by viewModel.prefEnableUserTracking.asFlow().collectAsStateWithLifecycle(
        initialValue = false
    )
    val useCustomTabs by viewModel.prefUseCustomTabs.asFlow().collectAsStateWithLifecycle(
        initialValue = true
    )
    val useDarkTheme by viewModel.prefDarkTheme.asFlow().collectAsStateWithLifecycle(
        initialValue = DarkThemeValue.V2FollowSystem
    )
    val enableDynamicTheme by viewModel.prefEnableDynamicTheme.asFlow().collectAsStateWithLifecycle(
        initialValue = isDynamicColorAvailable
    )

    GeneralSettingsScreen(
        modifier = modifier,
        enableUserTracking = enableUserTracking,
        onEnableUserTrackingChange = viewModel.prefEnableUserTracking::set,
        useCustomTabs = useCustomTabs,
        onUseCustomTabsChange = viewModel.prefUseCustomTabs::set,
        useDarkTheme = useDarkTheme,
        onDarkThemeChange = {
            viewModel.prefDarkTheme.set(it)
            onDarkThemeChange(it)
        },
        enableDynamicTheme = enableDynamicTheme,
        onDynamicThemeChange = {
            viewModel.prefEnableDynamicTheme.set(it)
            onDynamicThemeChange(it)
        }
    )
}

@Preview
@Composable
private fun GeneralSettingsScreenPreview() {
    var enableUserTracking by remember { mutableStateOf(false) }
    var useCustomTabs by remember { mutableStateOf(true) }
    var useDarkTheme: DarkThemeValue.Version2 by remember { mutableStateOf(DarkThemeValue.V2FollowSystem) }
    var enableDynamicTheme by remember { mutableStateOf(true) }

    StudyBuddyTheme {
        GeneralSettingsScreen(
            enableUserTracking = enableUserTracking,
            onEnableUserTrackingChange = { enableUserTracking = it },
            useCustomTabs = useCustomTabs,
            onUseCustomTabsChange = { useCustomTabs = it },
            useDarkTheme = useDarkTheme,
            onDarkThemeChange = { useDarkTheme = it },
            enableDynamicTheme = enableDynamicTheme,
            onDynamicThemeChange = { enableDynamicTheme = it }
        )
    }
}
