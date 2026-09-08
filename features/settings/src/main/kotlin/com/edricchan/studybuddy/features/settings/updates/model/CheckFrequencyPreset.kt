package com.edricchan.studybuddy.features.settings.updates.model

import androidx.annotation.StringRes
import com.edricchan.studybuddy.core.settings.updates.resources.R
import java.time.Duration

enum class CheckFrequencyPreset(
    @field:StringRes val stringResource: Int,
    val duration: Duration
) {
    Manual(
        R.string.pref_check_for_update_freq_manual,
        Duration.ZERO
    ),
    SixHours(
        R.string.pref_check_for_update_freq_six_hour,
        Duration.ofHours(6)
    ),
    TwelveHours(
        R.string.pref_check_for_update_freq_twelve_hour,
        Duration.ofHours(12)
    ),
    Daily(
        R.string.pref_check_for_update_freq_daily,
        Duration.ofDays(1)
    ),
    Weekly(
        R.string.pref_check_for_update_freq_weekly,
        Duration.ofDays(7)
    )
}
