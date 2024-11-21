package com.edricchan.studybuddy.ui.modules.debug.compose

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.core.settings.updates.UpdateInfoPrefConstants
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.ui.modules.debug.compose.device_info.DeviceInfoCategory
import com.edricchan.studybuddy.ui.preference.compose.MainSwitchBar
import com.edricchan.studybuddy.ui.preference.compose.MainSwitchBarDefaults
import com.edricchan.studybuddy.ui.preference.compose.Preference
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategory
import com.edricchan.studybuddy.ui.preference.compose.PreferenceDivider
import com.edricchan.studybuddy.utils.dev.isDevMode
import com.edricchan.studybuddy.utils.dev.prefDevModeEnabled
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Composable
private fun OpenDebugModalBottomSheetPreference(
    modifier: Modifier = Modifier, onClick: () -> Unit
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

private const val INSTANT_UNSET = -1L

private val SharedPreferences.lastCheckedInstant: Instant?
    get() = getLong(
        UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE,
        -1
    ).takeIf { it != INSTANT_UNSET }?.let { Instant.ofEpochMilli(it) }

private val SharedPreferences.lastUpdatedInstant: Instant?
    get() = getLong(
        UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE,
        -1
    ).takeIf { it != INSTANT_UNSET }?.let { Instant.ofEpochMilli(it) }

@Composable
fun DebugScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    userFlow: Flow<@JvmSuppressWildcards FirebaseUser?>,
    onNavigateToDebugModalBottomSheet: () -> Unit,
    onNavigateToFeatureFlagsList: () -> Unit
) {
    val context = LocalContext.current
    val defaultSharedPrefs = remember(context, context::defaultSharedPreferences)

    val updateInfoSharedPrefs = remember(context) {
        context.getSharedPreferences(
            UpdateInfoPrefConstants.FILE_UPDATE_INFO,
            Context.MODE_PRIVATE
        )
    }

    val user by userFlow.collectAsStateWithLifecycle(null)

    LazyColumn(modifier = modifier, contentPadding = contentPadding) {
        item {
            MainSwitchBar(
                checked = context.isDevMode(),
                onCheckedChange = { context.prefDevModeEnabled = it },
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
            PreferenceDivider(iconSpaceReserved = false)
        }
        item {
            PreferenceCategory(
                title = { Text(text = stringResource(R.string.debug_activity_category_firebase)) }
            ) {
                AccountInfoPreference(user = user)
            }
            PreferenceDivider(iconSpaceReserved = false)
        }

        item {
            UpdatesCategory(
                customJsonUrl = defaultSharedPrefs.getString(Constants.debugSetCustomJsonUrl, "")
                    .orEmpty(),
                onJsonUrlChange = {
                    defaultSharedPrefs.edit {
                        putString(Constants.debugSetCustomJsonUrl, it)
                    }
                },
                lastCheckedInstant = updateInfoSharedPrefs.lastCheckedInstant,
                lastUpdatedInstant = updateInfoSharedPrefs.lastUpdatedInstant
            )
            PreferenceDivider(iconSpaceReserved = false)
        }

        item {
            PreferenceCategory(
                title = { Text(text = stringResource(R.string.debug_activity_category_other)) }
            ) {
                ClearSettingsPreference(onClearSettingsRequest = {
                    defaultSharedPrefs.edit {
                        clear()
                    }
                })
                OpenDebugModalBottomSheetPreference(onClick = onNavigateToDebugModalBottomSheet)
            }
        }
    }
}
