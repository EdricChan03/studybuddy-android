package com.edricchan.studybuddy.features.settings.updates.ui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edricchan.studybuddy.core.resources.temporal.relative.formatRelativeTimeSpan
import com.edricchan.studybuddy.features.settings.R
import com.edricchan.studybuddy.features.settings.updates.model.CheckFrequencyCompat
import com.edricchan.studybuddy.features.settings.updates.vm.UpdateSettingsViewModel
import com.edricchan.studybuddy.ui.preference.compose.ListDialogPreference
import com.edricchan.studybuddy.ui.preference.compose.Preference
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategory
import com.edricchan.studybuddy.ui.preference.compose.twostate.SwitchPreference
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import java.time.Duration
import java.time.Instant
import com.edricchan.studybuddy.core.settings.updates.resources.R as UpdateR

@Composable
private fun Instant.formatRelativeTimeSpan(
    context: Context = LocalContext.current,
    now: Instant = Instant.now()
): String = context.formatRelativeTimeSpan(time = this, now = now)

@Composable
fun UpdateSettingsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onUpdatesClick: () -> Unit,
    lastUpdated: Instant?,
    lastChecked: Instant?,
    checkFrequency: CheckFrequencyCompat,
    onCheckFrequencyChange: (CheckFrequencyCompat) -> Unit,
    canDownloadMetered: Boolean,
    onCanDownloadMeteredChange: (Boolean) -> Unit,
    onlyDownloadCharging: Boolean,
    onOnlyDownloadCharging: (Boolean) -> Unit
) = Column(
    modifier = modifier
        .verticalScroll(rememberScrollState())
        .padding(contentPadding)
) {
    Preference(
        icon = {
            Icon(
                painterResource(R.drawable.ic_system_update_24dp),
                contentDescription = null
            )
        },
        title = { Text(text = stringResource(UpdateR.string.pref_updates_title)) },
        subtitle = {
            Text(
                text = stringResource(
                    UpdateR.string.pref_updates_summary,
                    lastChecked?.formatRelativeTimeSpan()
                        ?: stringResource(UpdateR.string.pref_updates_summary_never),
                    lastUpdated?.formatRelativeTimeSpan()
                        ?: stringResource(UpdateR.string.pref_updates_summary_never)
                )
            )
        },
        onClick = onUpdatesClick
    )

    PreferenceCategory(
        title = { Text(text = stringResource(UpdateR.string.pref_updates_options_title)) }
    ) {
        ListDialogPreference(
            icon = { Icon(Icons.Outlined.Refresh, contentDescription = null) },
            title = { Text(text = stringResource(UpdateR.string.pref_check_for_update_frequency_title)) },
            subtitle = { Text(text = stringResource(checkFrequency.stringResource)) },
            values = CheckFrequencyCompat.entries,
            valueLabel = { Text(text = stringResource(it.stringResource)) },
            value = checkFrequency,
            onValueChanged = onCheckFrequencyChange
        )
        SwitchPreference(
            icon = {
                Icon(
                    painterResource(R.drawable.ic_attach_money_outline_24dp),
                    contentDescription = null
                )
            },
            title = { Text(text = stringResource(UpdateR.string.pref_updates_download_over_metered_title)) },
            subtitle = { Text(text = stringResource(UpdateR.string.pref_updates_download_over_metered_summary)) },
            checked = canDownloadMetered,
            onCheckedChange = onCanDownloadMeteredChange
        )
        SwitchPreference(
            icon = {
                Icon(
                    painterResource(R.drawable.ic_battery_charging_outline_24dp),
                    contentDescription = null
                )
            },
            title = { Text(text = stringResource(UpdateR.string.pref_updates_download_only_when_charging_title)) },
            subtitle = { Text(text = stringResource(UpdateR.string.pref_updates_download_only_when_charging_summary)) },
            checked = onlyDownloadCharging,
            onCheckedChange = onOnlyDownloadCharging
        )
    }
}

@Composable
fun UpdateSettingsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: UpdateSettingsViewModel,
    onUpdatesClick: () -> Unit
) {
    val checkFrequency by viewModel.prefCheckFrequency.asFlow()
        .collectAsStateWithLifecycle(initialValue = CheckFrequencyCompat.SixHours)

    val canDownloadMetered by viewModel.prefCanDownloadMetered.asFlow()
        .collectAsStateWithLifecycle(initialValue = false)

    val onlyDownloadCharging by viewModel.prefOnlyDownloadCharging.asFlow()
        .collectAsStateWithLifecycle(initialValue = false)

    val lastChecked by viewModel.lastChecked.asFlow()
        .collectAsStateWithLifecycle(initialValue = null)

    val lastUpdated by viewModel.lastUpdated.asFlow()
        .collectAsStateWithLifecycle(initialValue = null)

    UpdateSettingsScreen(
        modifier = modifier,
        contentPadding = contentPadding,
        onUpdatesClick = onUpdatesClick,
        lastChecked = lastChecked,
        lastUpdated = lastUpdated,
        checkFrequency = checkFrequency,
        onCheckFrequencyChange = viewModel.prefCheckFrequency::set,
        canDownloadMetered = canDownloadMetered,
        onCanDownloadMeteredChange = viewModel.prefCanDownloadMetered::set,
        onlyDownloadCharging = onlyDownloadCharging,
        onOnlyDownloadCharging = viewModel.prefOnlyDownloadCharging::set
    )
}

@Preview
@Composable
private fun UpdateSettingsScreenPreview() {
    var checkFrequency by remember { mutableStateOf(CheckFrequencyCompat.Manual) }
    var canDownloadMetered by remember { mutableStateOf(false) }
    var onlyDownloadCharging by remember { mutableStateOf(false) }

    val now = Instant.now()

    StudyBuddyTheme {
        UpdateSettingsScreen(
            onUpdatesClick = {},
            lastUpdated = now - Duration.ofDays(2),
            lastChecked = now,
            checkFrequency = checkFrequency,
            onCheckFrequencyChange = { checkFrequency = it },
            canDownloadMetered = canDownloadMetered,
            onCanDownloadMeteredChange = { canDownloadMetered = it },
            onlyDownloadCharging = onlyDownloadCharging,
            onOnlyDownloadCharging = { onlyDownloadCharging = it },
        )
    }
}
