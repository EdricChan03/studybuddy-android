package com.edricchan.studybuddy.core.settings.general.locale

import com.edricchan.studybuddy.core.settings.general.proto.HourCycle as HourCycleProto

enum class HourCycle(
    val protoValue: HourCycleProto
) {
    System(HourCycleProto.HrCycle_System),
    Hour12(HourCycleProto.HrCycle_Hour12),
    Hour24(HourCycleProto.HrCycle_Hour24);

    companion object {
        fun fromProto(protoValue: HourCycleProto): HourCycle? =
            entries.find { it.protoValue == protoValue }
    }
}
