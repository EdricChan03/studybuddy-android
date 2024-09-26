package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.vm

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.SelectBottomSheetFragment
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetItem
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.toggleSelectedItem
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.withSelectedItem
import kotlinx.coroutines.flow.StateFlow

class SelectBottomSheetViewModel<Id>(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val items: StateFlow<OptionBottomSheetGroup<Id>> =
        savedStateHandle.getStateFlow(
            SelectBottomSheetFragment.TAG_ITEMS_DATA,
            OptionBottomSheetGroup.SingleSelect(listOf())
        )

    val headerTitle: StateFlow<String?> = savedStateHandle.getStateFlow(
        SelectBottomSheetFragment.TAG_HEADER_TITLE,
        null
    )

    val hideDragHandle: StateFlow<Boolean> = savedStateHandle.getStateFlow(
        SelectBottomSheetFragment.TAG_HIDE_DRAG_HANDLE,
        false
    )

    fun toggleSelectedItem(item: OptionBottomSheetItem<Id>, isSelected: Boolean) {
        Log.d("SelectSheetViewModel", "New item to be selected: $item")
        Log.d("SelectSheetViewModel", "Current items: ${items.value}")
        savedStateHandle[SelectBottomSheetFragment.TAG_ITEMS_DATA] =
            when (val value = items.value) {
                is OptionBottomSheetGroup.SingleSelect -> value.withSelectedItem(item = item)
                is OptionBottomSheetGroup.MultiSelect -> value.toggleSelectedItem(
                    item = item,
                    isSelected = isSelected
                )
            }
        Log.d("SelectSheetViewModel", "New value: ${items.value}")
    }
}
