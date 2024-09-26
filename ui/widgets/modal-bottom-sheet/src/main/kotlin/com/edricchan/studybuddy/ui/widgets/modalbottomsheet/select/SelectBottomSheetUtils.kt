package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetItem

inline fun <Id : Any> selectBottomSheet(
    headerTitle: String,
    hideDragHandle: Boolean = false,
    items: OptionBottomSheetGroup<Id>
): SelectBottomSheetFragment<Id> = SelectBottomSheetFragment.newInstance(
    items, headerTitle, hideDragHandle
)

inline fun <Id : Any> FragmentManager.setSelectSheetFragmentResultListeners(
    lifecycleOwner: LifecycleOwner,
    crossinline onConfirm: (selectedItems: Set<OptionBottomSheetItem<Id>>) -> Unit = {},
    crossinline onCanceled: () -> Unit = {}
) {
    setFragmentResultListener(
        SelectBottomSheetFragment.RESULT_KEY,
        lifecycleOwner
    ) { _, bundle ->
        val result = SelectBottomSheetFragment.FragmentResult.fromBundle<Id>(bundle)
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

inline fun <Id : Any> FragmentManager.showSelectionBottomSheet(
    headerTitle: String,
    hideDragHandle: Boolean = false,
    items: OptionBottomSheetGroup<Id>,
    lifecycleOwner: LifecycleOwner,
    crossinline onConfirm: (selectedItems: Set<OptionBottomSheetItem<*>>) -> Unit = {},
    crossinline onCanceled: () -> Unit = {}
): SelectBottomSheetFragment<Id> = selectBottomSheet(headerTitle, hideDragHandle, items).apply {
    show(this@showSelectionBottomSheet, tag)
    setSelectSheetFragmentResultListeners<Id>(
        lifecycleOwner = lifecycleOwner,
        onConfirm = onConfirm,
        onCanceled = onCanceled
    )
}

inline fun <Id : Any> Fragment.showSelectionBottomSheet(
    headerTitle: String,
    hideDragHandle: Boolean = false,
    items: OptionBottomSheetGroup<Id>,
    lifecycleOwner: LifecycleOwner = viewLifecycleOwner,
    crossinline onConfirm: (selectedItems: Set<OptionBottomSheetItem<*>>) -> Unit = {},
    crossinline onCanceled: () -> Unit = {}
) = parentFragmentManager.showSelectionBottomSheet(
    headerTitle = headerTitle,
    hideDragHandle = hideDragHandle,
    items = items,
    lifecycleOwner = lifecycleOwner,
    onConfirm = onConfirm,
    onCanceled = onCanceled
)

inline fun <Id : Any> FragmentActivity.showSelectionBottomSheet(
    headerTitle: String,
    hideDragHandle: Boolean = false,
    items: OptionBottomSheetGroup<Id>,
    lifecycleOwner: LifecycleOwner = this,
    crossinline onConfirm: (selectedItems: Set<OptionBottomSheetItem<*>>) -> Unit = {},
    crossinline onCanceled: () -> Unit = {}
) = supportFragmentManager.showSelectionBottomSheet(
    headerTitle = headerTitle,
    hideDragHandle = hideDragHandle,
    items = items,
    lifecycleOwner = lifecycleOwner,
    onConfirm = onConfirm,
    onCanceled = onCanceled
)
