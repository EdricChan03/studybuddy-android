package com.edricchan.studybuddy.domain.common.temporal

import java.time.DayOfWeek
import com.edricchan.studybuddy.data.common.temporal.proto.DayOfWeek as DayOfWeekProto

fun DayOfWeekProto.toDomain(default: DayOfWeek): DayOfWeek = toDomainOrNull() ?: default

fun DayOfWeekProto.toDomainOrNull(): DayOfWeek? = when (this) {
    DayOfWeekProto.DOW_Unspecified -> null
    DayOfWeekProto.DOW_Monday -> DayOfWeek.MONDAY
    DayOfWeekProto.DOW_Tuesday -> DayOfWeek.TUESDAY
    DayOfWeekProto.DOW_Wednesday -> DayOfWeek.WEDNESDAY
    DayOfWeekProto.DOW_Thursday -> DayOfWeek.THURSDAY
    DayOfWeekProto.DOW_Friday -> DayOfWeek.FRIDAY
    DayOfWeekProto.DOW_Saturday -> DayOfWeek.SATURDAY
    DayOfWeekProto.DOW_Sunday -> DayOfWeek.SUNDAY
}

fun DayOfWeek.toProto(): DayOfWeekProto = when (this) {
    DayOfWeek.MONDAY -> DayOfWeekProto.DOW_Monday
    DayOfWeek.TUESDAY -> DayOfWeekProto.DOW_Tuesday
    DayOfWeek.WEDNESDAY -> DayOfWeekProto.DOW_Wednesday
    DayOfWeek.THURSDAY -> DayOfWeekProto.DOW_Thursday
    DayOfWeek.FRIDAY -> DayOfWeekProto.DOW_Friday
    DayOfWeek.SATURDAY -> DayOfWeekProto.DOW_Saturday
    DayOfWeek.SUNDAY -> DayOfWeekProto.DOW_Sunday
}
