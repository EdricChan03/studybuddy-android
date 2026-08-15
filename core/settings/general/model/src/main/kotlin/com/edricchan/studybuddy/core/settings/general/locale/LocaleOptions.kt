package com.edricchan.studybuddy.core.settings.general.locale

import com.edricchan.studybuddy.domain.common.temporal.toProto
import com.edricchan.studybuddy.core.settings.general.proto.LocaleSettings as LocaleSettingsProto

data class LocaleOptions(
    val lang: AppLanguage = AppLanguage.System,
    val firstWeekDay: FirstWeekDay = FirstWeekDay.System,
    val hourCycle: HourCycle = HourCycle.System
)

fun LocaleSettingsProto.toDomain(): LocaleOptions = LocaleOptions(
    lang = AppLanguage.fromProto(this) ?: AppLanguage.System,
    firstWeekDay = FirstWeekDay.fromProto(localeSettings = this) ?: FirstWeekDay.System,
    hourCycle = HourCycle.fromProto(hour_format) ?: HourCycle.System
)

fun LocaleOptions.toProto(): LocaleSettingsProto = LocaleSettingsProto(
    use_system_lang = lang is AppLanguage.System,
    lang_code = (lang as? AppLanguage.Custom)?.langCode,
    use_system_first_week_day = firstWeekDay is FirstWeekDay.System,
    first_day_of_week = (firstWeekDay as? FirstWeekDay.Custom)?.dayOfWeek?.toProto(),
    hour_format = hourCycle.protoValue
)
