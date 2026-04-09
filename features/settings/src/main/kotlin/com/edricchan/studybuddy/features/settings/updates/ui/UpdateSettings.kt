package com.edricchan.studybuddy.features.settings.updates.ui

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.MobileCharge
import com.edricchan.studybuddy.core.resources.icons.outlined.NetworkCell
import com.edricchan.studybuddy.core.resources.icons.outlined.Refresh
import com.edricchan.studybuddy.core.resources.icons.outlined.SystemUpdateAlt
import com.edricchan.studybuddy.core.resources.temporal.duration.format
import com.edricchan.studybuddy.core.resources.temporal.relative.formatRelativeTimeSpan
import com.edricchan.studybuddy.features.settings.updates.model.CheckFrequencyCompat
import com.edricchan.studybuddy.features.settings.updates.model.asDuration
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
private fun Duration.formatFrequency(
    context: Context = LocalContext.current,
    manualText: String = stringResource(UpdateR.string.pref_check_for_update_freq_manual)
): String = remember(this) {
    if (this <= Duration.ZERO) return@remember manualText
    format(context)
}

@Composable
fun UpdateSettingsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onUpdatesClick: () -> Unit,
    lastUpdated: Instant?,
    lastChecked: Instant?,
    checkFrequency: Duration,
    onCheckFrequencyChange: (Duration) -> Unit,
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
                AppIcons.Outlined.SystemUpdateAlt,
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
        // TODO: Create a more versatile version that allows for an arbitrary duration
        //  to be set
        ListDialogPreference(
            icon = { Icon(AppIcons.Outlined.Refresh, contentDescription = null) },
            title = { Text(text = stringResource(UpdateR.string.pref_check_for_update_frequency_title)) },
            subtitle = { Text(text = checkFrequency.formatFrequency()) },
            values = CheckFrequencyCompat.entries.map(CheckFrequencyCompat::asDuration),
            valueLabel = { Text(text = it.formatFrequency()) },
            value = checkFrequency,
            onValueChanged = onCheckFrequencyChange
        )
        SwitchPreference(
            icon = {
                Icon(
                    AppIcons.Outlined.NetworkCell,
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
                    AppIcons.Outlined.MobileCharge,
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
    val checkFrequency by viewModel.prefCheckFrequency
        .collectAsStateWithLifecycle(initialValue = CheckFrequencyCompat.SixHours.asDuration())

    val canDownloadMetered by viewModel.prefCanDownloadMetered
        .collectAsStateWithLifecycle(initialValue = false)

    val onlyDownloadCharging by viewModel.prefOnlyDownloadCharging
        .collectAsStateWithLifecycle(initialValue = false)

    val lastChecked by viewModel.lastChecked
        .collectAsStateWithLifecycle(initialValue = null)

    val lastUpdated by viewModel.lastUpdated
        .collectAsStateWithLifecycle(initialValue = null)

    UpdateSettingsScreen(
        modifier = modifier,
        contentPadding = contentPadding,
        onUpdatesClick = onUpdatesClick,
        lastChecked = lastChecked,
        lastUpdated = lastUpdated,
        checkFrequency = checkFrequency,
        onCheckFrequencyChange = viewModel::setPrefCheckFrequency,
        canDownloadMetered = canDownloadMetered,
        onCanDownloadMeteredChange = viewModel::setPrefCanDownloadMetered,
        onlyDownloadCharging = onlyDownloadCharging,
        onOnlyDownloadCharging = viewModel::setPrefOnlyDownloadCharging
    )
}

@Preview
@Composable
private fun UpdateSettingsScreenPreview() {
    var checkFrequency by remember { mutableStateOf(Duration.ofHours(6)) }
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
