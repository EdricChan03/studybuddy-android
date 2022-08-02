package com.edricchan.studybuddy.ui.widget.bottomsheet.annotations

import androidx.annotation.StringDef
import com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces.ModalBottomSheetGroup

@StringDef(
    ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_NONE,
    ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_ALL,
    ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_SINGLE
)
annotation class ModalBottomSheetCheckableBehavior