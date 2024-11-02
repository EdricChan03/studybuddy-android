package com.edricchan.studybuddy.ui.modules.debug.compose

import android.util.Log
import androidx.compose.foundation.text.selection.SelectionContainer
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.asFlow
import com.edricchan.studybuddy.R
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
) = Preference(
    modifier = modifier,
    icon = { Icon(Icons.Outlined.Info, contentDescription = null) },
    title = { Text(text = titleString) },
    subtitle = {
        SelectionContainer {
            Text(text = instant?.formatISO() ?: unsetString)
        }
    }
)

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

    var _customJsonUrlValue by rememberSaveable { mutableStateOf(customJsonUrl) }

    PreferenceCategory(
        modifier = modifier,
        title = { Text(text = stringResource(R.string.debug_activity_category_updates)) }
    ) {
        InputDialogPreference(
            title = { Text(text = stringResource(R.string.debug_activity_updates_set_custom_json_url_title)) },
            subtitle = { Text(text = _customJsonUrlValue) },
            icon = { Icon(painterResource(R.drawable.ic_link_24dp), contentDescription = null) },
            value = _customJsonUrlValue,
            onConfirm = onJsonUrlChange
        ) {
            OutlinedTextField(
                value = _customJsonUrlValue,
                onValueChange = { _customJsonUrlValue = it }
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
