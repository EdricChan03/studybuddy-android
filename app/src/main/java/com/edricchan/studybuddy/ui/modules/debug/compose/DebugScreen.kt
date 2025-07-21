package com.edricchan.studybuddy.ui.modules.debug.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.ui.modules.debug.compose.device_info.DeviceInfoCategory
import com.edricchan.studybuddy.ui.modules.debug.vm.DebugViewModel
import com.edricchan.studybuddy.ui.preference.compose.MainSwitchBar
import com.edricchan.studybuddy.ui.preference.compose.MainSwitchBarDefaults
import com.edricchan.studybuddy.ui.preference.compose.Preference
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategory
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategoryScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

@Composable
private fun PreferenceCategoryScope.OpenDebugModalBottomSheetPreference(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Preference(
        modifier = modifier,
        onClick = onClick,
        icon = {
            Icon(
                painterResource(R.drawable.ic_bottom_sheets_24dp), contentDescription = null
            )
        },
        title = { Text(text = stringResource(R.string.debug_activity_other_modal_bottom_sheet_testing_title)) },
        subtitle = { Text(text = stringResource(R.string.debug_activity_other_modal_bottom_sheet_testing_summary)) },
    )
}

@Composable
fun DebugScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    viewModel: DebugViewModel = viewModel(),
    userFlow: Flow<@JvmSuppressWildcards FirebaseUser?>,
    onNavigateToDebugModalBottomSheet: () -> Unit,
    onNavigateToFeatureFlagsList: () -> Unit
) {
    val devModeEnabled by viewModel.devModeEnabled.asFlow()
        .collectAsStateWithLifecycle(initialValue = BuildConfig.DEBUG)
    val customJsonUrl by viewModel.customUpdateJsonUrl.asFlow()
        .collectAsStateWithLifecycle(initialValue = "")
    val lastCheckedInstant by viewModel.lastCheckedUpdates.asFlow()
        .collectAsStateWithLifecycle(initialValue = null)
    val lastUpdatedInstant by viewModel.lastUpdated.asFlow()
        .collectAsStateWithLifecycle(initialValue = null)

    val user by userFlow.collectAsStateWithLifecycle(null)

    LazyColumn(modifier = modifier, contentPadding = contentPadding) {
        item {
            MainSwitchBar(
                modifier = Modifier.padding(bottom = 16.dp),
                checked = devModeEnabled,
                onCheckedChange = viewModel.devModeEnabled::set,
                enabled = !BuildConfig.DEBUG
            ) {
                MainSwitchBarDefaults.TitleText(text = stringResource(R.string.debug_activity_dev_mode_enabled_title))
            }
        }

        item {
            Preference(
                onClick = onNavigateToFeatureFlagsList,
                title = { Text(text = stringResource(R.string.debug_activity_feature_flags_title)) },
                icon = {
                    Icon(
                        painterResource(R.drawable.ic_flag_outline_24dp),
                        contentDescription = null
                    )
                }
            )
        }

        item {
            DeviceInfoCategory()
        }
        item {
            PreferenceCategory(
                title = { Text(text = stringResource(R.string.debug_activity_category_firebase)) }
            ) {
                AccountInfoPreference(user = user)
            }
        }

        item {
            UpdatesCategory(
                customJsonUrl = customJsonUrl,
                onJsonUrlChange = viewModel.customUpdateJsonUrl::set,
                lastCheckedInstant = lastCheckedInstant,
                lastUpdatedInstant = lastUpdatedInstant
            )
        }

        item {
            PreferenceCategory(
                title = { Text(text = stringResource(R.string.debug_activity_category_other)) }
            ) {
                ClearSettingsPreference(onClearSettingsRequest = viewModel::clearAppPrefs)
                OpenDebugModalBottomSheetPreference(onClick = onNavigateToDebugModalBottomSheet)
            }
        }
    }
}
