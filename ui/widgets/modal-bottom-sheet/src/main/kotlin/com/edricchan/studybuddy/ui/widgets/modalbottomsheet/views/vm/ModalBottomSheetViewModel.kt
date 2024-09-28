package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.ModalBottomSheetFragment
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces.ModalBottomSheetItem
import kotlinx.coroutines.flow.StateFlow

class ModalBottomSheetViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val itemsState: StateFlow<List<ModalBottomSheetItem>> = savedStateHandle.getStateFlow(
        ModalBottomSheetFragment.TAG_ITEMS,
        listOf()
    )

    val headerTitleState: StateFlow<String?> =
        savedStateHandle.getStateFlow(ModalBottomSheetFragment.TAG_HEADER_TITLE, null)

    val hideDragHandleState: StateFlow<Boolean> = savedStateHandle.getStateFlow(
        ModalBottomSheetFragment.TAG_HIDE_DRAG_HANDLE, false
    )
}
