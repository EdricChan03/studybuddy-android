package com.edricchan.studybuddy.features.settings.general.ui.temporal

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategoryScope
import com.edricchan.studybuddy.ui.preference.compose.twostate.SwitchPreference
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import com.edricchan.studybuddy.core.settings.general.resources.R as GeneralR

@Composable
internal fun PreferenceCategoryScope.RelativeTimestampsPreference(
    modifier: Modifier = Modifier,
    value: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    val now = remember { LocalDate.now() }
    val formattedDate = remember {
        now.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    }

    SwitchPreference(
        modifier = modifier,
        title = {
            Text(text = stringResource(GeneralR.string.pref_use_relative_timestamps_title))
        },
        subtitle = {
            Text(
                text = stringResource(
                    GeneralR.string.pref_use_relative_timestamps_subtitle,
                    formattedDate
                )
            )
        },
        checked = value,
        onCheckedChange = onValueChange
    )
}
