package com.edricchan.studybuddy.ui.modules.main.nav.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.edricchan.studybuddy.ui.modules.main.nav.NavItem
import com.edricchan.studybuddy.ui.modules.main.nav.compat.fragment.NavBottomSheetDialogFragment
import kotlinx.coroutines.flow.StateFlow

class NavBottomSheetViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val selectedItem: StateFlow<NavItem?>
        field = savedStateHandle.getMutableStateFlow<NavItem?>(
            NavBottomSheetDialogFragment.TAG_SELECTED_NAV_ITEM,
            null
        )

    val userData: StateFlow<NavBottomSheetDialogFragment.UserData>
        field = savedStateHandle.getMutableStateFlow<NavBottomSheetDialogFragment.UserData>(
            NavBottomSheetDialogFragment.TAG_USER_DATA,
            NavBottomSheetDialogFragment.UserData.Anonymous
        )

    fun setSelectedItem(item: NavItem?) {
        selectedItem.value = item
    }

    fun setUserData(data: NavBottomSheetDialogFragment.UserData) {
        userData.value = data
    }
}
