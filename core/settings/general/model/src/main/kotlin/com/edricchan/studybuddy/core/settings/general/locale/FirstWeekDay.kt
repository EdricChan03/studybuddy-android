package com.edricchan.studybuddy.core.settings.general.locale

import com.edricchan.studybuddy.domain.common.temporal.toDomain
import java.time.DayOfWeek
import com.edricchan.studybuddy.core.settings.general.proto.LocaleSettings as LocaleSettingsProto
import com.edricchan.studybuddy.data.common.temporal.proto.DayOfWeek as DayOfWeekProto

sealed interface FirstWeekDay {
    data object System : FirstWeekDay
    data class Custom(val dayOfWeek: DayOfWeek) : FirstWeekDay

    companion object {
        fun fromProto(localeSettings: LocaleSettingsProto): FirstWeekDay? {
            localeSettings.first_day_of_week?.let {
                if (it == DayOfWeekProto.DOW_Unspecified) return null
                return Custom(
                    dayOfWeek = it.toDomain(DayOfWeek.MONDAY)
                )
            }
            if (localeSettings.use_system_first_week_day == true) return System

            return null
        }
    }
}
