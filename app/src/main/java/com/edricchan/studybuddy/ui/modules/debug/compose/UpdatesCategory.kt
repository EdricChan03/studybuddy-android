package com.edricchan.studybuddy.ui.modules.debug.compose

import android.content.ClipData
import android.util.Log
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.util.PatternsCompat
import androidx.lifecycle.asFlow
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.exts.androidx.compose.runtime.letComposable
import com.edricchan.studybuddy.exts.datetime.formatISO
import com.edricchan.studybuddy.ui.preference.compose.InputDialogPreference
import com.edricchan.studybuddy.ui.preference.compose.Preference
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategory
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategoryScope
import com.edricchan.studybuddy.utils.enqueueUniqueCheckForUpdatesWorker
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
private fun PreferenceCategoryScope.UpdateInfoInstantPreference(
    modifier: Modifier = Modifier,
    instant: Instant?,
    titleString: String,
    unsetString: String
) {
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    Preference(
        modifier = modifier,
        onClick = {
            scope.launch {
                clipboard.setClipEntry(
                    ClipEntry(
                        ClipData.newPlainText(
                            /* label = */ "",
                            /* text = */ instant?.formatISO() ?: unsetString
                        )
                    )
                )
            }
        },
        icon = { Icon(Icons.Outlined.Info, contentDescription = null) },
        title = { Text(text = titleString) },
        subtitle = {
            Text(text = instant?.formatISO() ?: unsetString)
        }
    )
}

@Composable
fun UpdatesCategory(
    modifier: Modifier = Modifier,
    customJsonUrl: String,
    onJsonUrlChange: (String) -> Unit,
    lastCheckedInstant: Instant?,
    lastUpdatedInstant: Instant?
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    fun startCheckForUpdates() {
        scope.launch {
            context.enqueueUniqueCheckForUpdatesWorker()?.state?.asFlow()
                ?.collect { state ->
                    Log.d(
                        "UpdatesCategory",
                        "Enqueued worker request with state $state"
                    )
                }
        }
    }

    PreferenceCategory(
        modifier = modifier,
        title = { Text(text = stringResource(R.string.debug_activity_category_updates)) }
    ) {
        var customJsonUrlValue by rememberSaveable(customJsonUrl) { mutableStateOf(customJsonUrl) }

        InputDialogPreference(
            title = {
                Text(
                    text = stringResource(R.string.debug_activity_updates_set_custom_json_url_title)
                )
            },
            subtitle = customJsonUrl.takeIf { it.isNotBlank() }
                ?.letComposable { Text(text = it) },
            icon = { Icon(painterResource(R.drawable.ic_link_24dp), contentDescription = null) },
            isValid = PatternsCompat.WEB_URL.toRegex().matches(customJsonUrlValue),
            onConfirm = {
                onJsonUrlChange(customJsonUrlValue)
                requestDismiss()
            }
        ) {
            OutlinedTextField(
                value = customJsonUrlValue,
                onValueChange = { customJsonUrlValue = it },
                label = { Text(text = stringResource(R.string.debug_activity_updates_set_custom_json_url_title)) },
                singleLine = true,
                keyboardActions = KeyboardActions {
                    onJsonUrlChange(customJsonUrlValue)
                    requestDismiss()
                }
            )
        }

        //#region Update metadata
        UpdateInfoInstantPreference(
            instant = lastCheckedInstant,
            titleString = stringResource(R.string.debug_activity_updates_last_checked_for_updates_date_title),
            unsetString = stringResource(R.string.debug_activity_updates_last_checked_for_updates_date_summary_default)
        )
        UpdateInfoInstantPreference(
            instant = lastUpdatedInstant,
            titleString = stringResource(R.string.debug_activity_updates_last_updated_date_title),
            unsetString = stringResource(R.string.debug_activity_updates_last_updated_date_summary_default)
        )
        //#endregion

        Preference(
            onClick = ::startCheckForUpdates,
            icon = {
                Icon(
                    painterResource(R.drawable.ic_refresh_24dp),
                    contentDescription = null
                )
            },
            title = { Text(text = stringResource(R.string.debug_activity_updates_start_worker_title)) },
            subtitle = { Text(text = stringResource(R.string.debug_activity_updates_start_worker_summary)) }
        )
    }
}
