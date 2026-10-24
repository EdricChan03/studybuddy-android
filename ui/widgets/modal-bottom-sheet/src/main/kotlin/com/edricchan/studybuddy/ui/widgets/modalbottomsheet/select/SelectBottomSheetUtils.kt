package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetItem

inline fun selectBottomSheet(
    headerTitle: String,
    hideDragHandle: Boolean = false,
    items: OptionBottomSheetGroup
): SelectBottomSheetFragment = SelectBottomSheetFragment.newInstance(
    items, headerTitle, hideDragHandle
)

inline fun FragmentManager.setSelectSheetFragmentResultListeners(
    lifecycleOwner: LifecycleOwner,
    crossinline onConfirm: (selectedItems: Set<OptionBottomSheetItem>) -> Unit = {},
    crossinline onCanceled: () -> Unit = {}
) {
    setFragmentResultListener(
        SelectBottomSheetFragment.RESULT_KEY,
        lifecycleOwner
    ) { _, bundle ->
        val result = SelectBottomSheetFragment.FragmentResult.fromBundle(bundle)
            ?: return@setFragmentResultListener

        if (result.isCanceled) {
            onCanceled()
            return@setFragmentResultListener
        }

        onConfirm(
            checkNotNull(result.selectedItems) { "Selected items was null. This is not intended behaviour" }
        )
    }
}

inline fun FragmentManager.showSelectionBottomSheet(
    headerTitle: String,
    hideDragHandle: Boolean = false,
    items: OptionBottomSheetGroup,
    lifecycleOwner: LifecycleOwner,
    crossinline onConfirm: (selectedItems: Set<OptionBottomSheetItem>) -> Unit = {},
    crossinline onCanceled: () -> Unit = {}
): SelectBottomSheetFragment = selectBottomSheet(headerTitle, hideDragHandle, items).apply {
    show(this@showSelectionBottomSheet, tag)
    setSelectSheetFragmentResultListeners(
        lifecycleOwner = lifecycleOwner,
        onConfirm = onConfirm,
        onCanceled = onCanceled
    )
}

inline fun Fragment.showSelectionBottomSheet(
    headerTitle: String,
    hideDragHandle: Boolean = false,
    items: OptionBottomSheetGroup,
    lifecycleOwner: LifecycleOwner = viewLifecycleOwner,
    crossinline onConfirm: (selectedItems: Set<OptionBottomSheetItem>) -> Unit = {},
    crossinline onCanceled: () -> Unit = {}
) = parentFragmentManager.showSelectionBottomSheet(
    headerTitle = headerTitle,
    hideDragHandle = hideDragHandle,
    items = items,
    lifecycleOwner = lifecycleOwner,
    onConfirm = onConfirm,
    onCanceled = onCanceled
)

inline fun FragmentActivity.showSelectionBottomSheet(
    headerTitle: String,
    hideDragHandle: Boolean = false,
    items: OptionBottomSheetGroup,
    lifecycleOwner: LifecycleOwner = this,
    crossinline onConfirm: (selectedItems: Set<OptionBottomSheetItem>) -> Unit = {},
    crossinline onCanceled: () -> Unit = {}
) = supportFragmentManager.showSelectionBottomSheet(
    headerTitle = headerTitle,
    hideDragHandle = hideDragHandle,
    items = items,
    lifecycleOwner = lifecycleOwner,
    onConfirm = onConfirm,
    onCanceled = onCanceled
)
