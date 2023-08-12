package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.annotations

import androidx.annotation.StringDef
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces.ModalBottomSheetGroup

@StringDef(
    ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_NONE,
    ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_ALL,
    ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_SINGLE
)
annotation class ModalBottomSheetCheckableBehavior
