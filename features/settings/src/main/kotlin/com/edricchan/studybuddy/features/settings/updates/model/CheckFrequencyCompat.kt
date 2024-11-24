package com.edricchan.studybuddy.features.settings.updates.model

import androidx.annotation.StringRes
import com.edricchan.studybuddy.features.settings.R

private val hourToEnumMap = mapOf(
    CheckFrequencyCompat.Manual to 0,
    CheckFrequencyCompat.ThreeHours to 3,
    CheckFrequencyCompat.SixHours to 6,
    CheckFrequencyCompat.TwelveHours to 12,
    CheckFrequencyCompat.Daily to 24,
    CheckFrequencyCompat.Weekly to 24 * 7
)

enum class CheckFrequencyCompat(@StringRes val stringResource: Int) {
    Manual(R.string.pref_check_for_update_freq_manual),
    ThreeHours(R.string.pref_check_for_update_freq_three_hour),
    SixHours(R.string.pref_check_for_update_freq_six_hour),
    TwelveHours(R.string.pref_check_for_update_freq_twelve_hour),
    Daily(R.string.pref_check_for_update_freq_daily),
    Weekly(R.string.pref_check_for_update_freq_weekly);

    companion object {
        fun fromHoursOrNull(hours: Int): CheckFrequencyCompat? =
            hourToEnumMap.entries
                .find { entry -> entry.value == hours }
                ?.key
    }
}

val CheckFrequencyCompat.hourValue: Int
    get() = hourToEnumMap[this] ?: 0
