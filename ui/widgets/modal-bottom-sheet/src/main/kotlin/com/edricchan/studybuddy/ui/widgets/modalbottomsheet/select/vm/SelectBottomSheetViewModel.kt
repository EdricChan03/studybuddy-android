package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.vm

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.SelectBottomSheetFragment
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetItem
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.toggleSelectedItem
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.withSelectedItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SelectBottomSheetViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _items by savedStateHandle.saved(key = SelectBottomSheetFragment.TAG_ITEMS_DATA) {
        MutableStateFlow<OptionBottomSheetGroup>(OptionBottomSheetGroup.SingleSelect(listOf()))
    }
    val items: StateFlow<OptionBottomSheetGroup> = _items.asStateFlow()

    val headerTitle: StateFlow<String?> = savedStateHandle.getStateFlow(
        SelectBottomSheetFragment.TAG_HEADER_TITLE,
        null
    )

    val hideDragHandle: StateFlow<Boolean> = savedStateHandle.getStateFlow(
        SelectBottomSheetFragment.TAG_HIDE_DRAG_HANDLE,
        false
    )

    fun toggleSelectedItem(item: OptionBottomSheetItem, isSelected: Boolean) {
        Log.d("SelectSheetViewModel", "New item to be selected: $item")
        Log.d("SelectSheetViewModel", "Current items: ${items.value}")
        _items.update { items ->
            when (items) {
                is OptionBottomSheetGroup.SingleSelect -> items.withSelectedItem(item = item)
                is OptionBottomSheetGroup.MultiSelect -> items.toggleSelectedItem(
                    item = item,
                    isSelected = isSelected
                )
            }
        }
        Log.d("SelectSheetViewModel", "New value: ${items.value}")
    }
}
