package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.dsl

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.SelectBottomSheetFragment
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.IdSerializer
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetItem
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.selectBottomSheet
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.setSelectSheetFragmentResultListeners

/**
 * Group builder to create a [com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetGroup.MultiSelect].
 *
 * To create a [com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetGroup.SingleSelect], use [SingleSelectBottomSheetGroupBuilder]
 * instead.
 *
 * Consumers should use the relevant [multiSelectGroup] and [multiSelectBottomSheet]
 * methods where appropriate.
 */
class MultiSelectBottomSheetGroupBuilder<Id : Any>(
    idSerializer: IdSerializer<Id>
) : SelectBottomSheetGroupBuilder<Id, OptionBottomSheetGroup.MultiSelect>(
    idSerializer = idSerializer
) {
    val selectedIndices = mutableSetOf<Int>()

    override fun addItem(
        selected: Boolean?,
        item: OptionBottomSheetItem
    ): OptionBottomSheetItem {
        items += item

        val index = items.indexOf(item)

        if (selected == true || isSelected?.invoke(item, index) == true) selectedIndices += index

        return item
    }

    override fun removeItem(
        item: OptionBottomSheetItem
    ) {
        val index = items.indexOf(item)
        selectedIndices -= index

        items -= item
    }

    override fun build(): OptionBottomSheetGroup.MultiSelect =
        OptionBottomSheetGroup.MultiSelect(
            items = items.toList(),
            enabled = enabled,
            selectedIndices = selectedIndices.toSet()
        )
}

/**
 * Shows a [SelectBottomSheetFragment] set to **multi**-selection mode with
 * the specified options, bound to the receiver [androidx.fragment.app.Fragment]'s
 * [androidx.fragment.app.Fragment.getParentFragmentManager].
 *
 * Use [showSingleSelectBottomSheet] instead if single-selection is desired.
 * @param headerTitle Header title to be displayed at the top of the bottom sheet.
 * @param hideDragHandle Whether the drag handle should be hidden.
 * @param lifecycleOwner [androidx.lifecycle.LifecycleOwner] used to configure the
 * [fragment result listeners][setSelectSheetFragmentResultListeners].
 * @param onConfirm Lambda that is invoked when the "Confirm" action is pressed in
 * the bottom sheet, with the selected item passed as the argument.
 * @param onCanceled Lambda that is invoked when the "Cancel" option is pressed in
 * the bottom sheet.
 * @param groupInit Options to configure the [OptionBottomSheetGroup.SingleSelect]
 * to be used.
 * @see multiSelectGroup
 * @see com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.selectBottomSheet
 * @see multiSelectBottomSheet
 * @see setSelectSheetFragmentResultListeners
 */
inline fun <Id : Any> Fragment.showMultiSelectBottomSheet(
    idSerializer: IdSerializer<Id>,
    headerTitle: String,
    hideDragHandle: Boolean = false,
    lifecycleOwner: LifecycleOwner = viewLifecycleOwner,
    crossinline onConfirm: (selectedItems: Set<OptionBottomSheetItem>) -> Unit = {},
    crossinline onCanceled: () -> Unit = {},
    groupInit: MultiSelectBottomSheetGroupBuilder<Id>.() -> Unit
) = parentFragmentManager.showMultiSelectBottomSheet(
    idSerializer = idSerializer,
    headerTitle = headerTitle,
    hideDragHandle = hideDragHandle,
    lifecycleOwner = lifecycleOwner,
    onConfirm = onConfirm,
    onCanceled = onCanceled,
    groupInit = groupInit
)

/**
 * Shows a [SelectBottomSheetFragment] set to **multi**-selection mode with the specified options,
 * bound to the receiver [Fragment]'s [Fragment.getParentFragmentManager].
 *
 * Use [showSingleSelectBottomSheet] instead if single-selection is desired.
 *
 * This overload allows for a string resource to be used for the [header title][headerTitleRes].
 * @param headerTitleRes String resource for the header title to be displayed at
 * the top of the bottom sheet.
 * @param hideDragHandle Whether the drag handle should be hidden.
 * @param lifecycleOwner [LifecycleOwner] used to configure the
 * [fragment result listeners][setSelectSheetFragmentResultListeners].
 * @param onConfirm Lambda that is invoked when the "Confirm" action is pressed in
 * the bottom sheet, with the selected item passed as the argument.
 * @param onCanceled Lambda that is invoked when the "Cancel" option is pressed in
 * the bottom sheet.
 * @param groupInit Options to configure the [OptionBottomSheetGroup.SingleSelect]
 * to be used.
 * @see multiSelectGroup
 * @see com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.selectBottomSheet
 * @see multiSelectBottomSheet
 * @see setSelectSheetFragmentResultListeners
 */
inline fun <Id : Any> Fragment.showMultiSelectBottomSheet(
    idSerializer: IdSerializer<Id>,
    @StringRes headerTitleRes: Int,
    hideDragHandle: Boolean = false,
    lifecycleOwner: LifecycleOwner = viewLifecycleOwner,
    crossinline onConfirm: (selectedItems: Set<OptionBottomSheetItem>) -> Unit = {},
    crossinline onCanceled: () -> Unit = {},
    groupInit: MultiSelectBottomSheetGroupBuilder<Id>.() -> Unit
) = parentFragmentManager.showMultiSelectBottomSheet(
    idSerializer = idSerializer,
    headerTitle = getString(headerTitleRes),
    hideDragHandle = hideDragHandle,
    lifecycleOwner = lifecycleOwner,
    onConfirm = onConfirm,
    onCanceled = onCanceled,
    groupInit = groupInit,
)

/**
 * Shows a [com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.SelectBottomSheetFragment] set to **multi**-selection mode with
 * the specified options, bound to the receiver [androidx.fragment.app.FragmentActivity]'s
 * [androidx.fragment.app.FragmentActivity.getSupportFragmentManager].
 *
 * Use [showSingleSelectBottomSheet] instead if single-selection is desired.
 * @param headerTitle Header title to be displayed at the top of the bottom sheet.
 * @param hideDragHandle Whether the drag handle should be hidden.
 * @param lifecycleOwner [LifecycleOwner] used to configure the
 * [fragment result listeners][setSelectSheetFragmentResultListeners].
 * @param onConfirm Lambda that is invoked when the "Confirm" action is pressed in
 * the bottom sheet, with the selected item passed as the argument.
 * @param onCanceled Lambda that is invoked when the "Cancel" option is pressed in
 * the bottom sheet.
 * @param groupInit Options to configure the [OptionBottomSheetGroup.SingleSelect]
 * to be used.
 * @see multiSelectGroup
 * @see com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.selectBottomSheet
 * @see multiSelectBottomSheet
 * @see setSelectSheetFragmentResultListeners
 */
inline fun <Id : Any> FragmentActivity.showMultiSelectBottomSheet(
    idSerializer: IdSerializer<Id>,
    headerTitle: String,
    hideDragHandle: Boolean = false,
    lifecycleOwner: LifecycleOwner = this,
    crossinline onConfirm: (selectedItems: Set<OptionBottomSheetItem>) -> Unit = {},
    crossinline onCanceled: () -> Unit = {},
    groupInit: MultiSelectBottomSheetGroupBuilder<Id>.() -> Unit
) = supportFragmentManager.showMultiSelectBottomSheet(
    idSerializer = idSerializer,
    headerTitle = headerTitle,
    hideDragHandle = hideDragHandle,
    lifecycleOwner = lifecycleOwner,
    onConfirm = onConfirm,
    onCanceled = onCanceled,
    groupInit = groupInit,
)

/**
 * Shows a [com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.SelectBottomSheetFragment] set to **multi**-selection mode with
 * the specified options, bound to the receiver [FragmentActivity]'s
 * [FragmentActivity.getSupportFragmentManager].
 *
 * Use [showSingleSelectBottomSheet] instead if single-selection is desired.
 *
 * This overload allows for a string resource to be used for the [header title][headerTitleRes].
 * @param headerTitleRes String resource for the header title to be displayed at
 * the top of the bottom sheet.
 * @param hideDragHandle Whether the drag handle should be hidden.
 * @param lifecycleOwner [LifecycleOwner] used to configure the
 * [fragment result listeners][setSelectSheetFragmentResultListeners].
 * @param onConfirm Lambda that is invoked when the "Confirm" action is pressed in
 * the bottom sheet, with the selected item passed as the argument.
 * @param onCanceled Lambda that is invoked when the "Cancel" option is pressed in
 * the bottom sheet.
 * @param groupInit Options to configure the [OptionBottomSheetGroup.SingleSelect]
 * to be used.
 * @see multiSelectGroup
 * @see com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.selectBottomSheet
 * @see multiSelectBottomSheet
 * @see setSelectSheetFragmentResultListeners
 */
inline fun <Id : Any> FragmentActivity.showMultiSelectBottomSheet(
    idSerializer: IdSerializer<Id>,
    @StringRes headerTitleRes: Int,
    hideDragHandle: Boolean = false,
    lifecycleOwner: LifecycleOwner = this,
    crossinline onConfirm: (selectedItems: Set<OptionBottomSheetItem>) -> Unit = {},
    crossinline onCanceled: () -> Unit = {},
    groupInit: MultiSelectBottomSheetGroupBuilder<Id>.() -> Unit
) = supportFragmentManager.showMultiSelectBottomSheet(
    idSerializer = idSerializer,
    headerTitle = getString(headerTitleRes),
    hideDragHandle = hideDragHandle,
    lifecycleOwner = lifecycleOwner,
    onConfirm = onConfirm,
    onCanceled = onCanceled,
    groupInit = groupInit,
)

/**
 * Shows a [com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.SelectBottomSheetFragment] set to **multi**-selection mode with
 * the specified options, bound to the receiver [androidx.fragment.app.FragmentManager].
 *
 * Use [showSingleSelectBottomSheet] instead if single-selection is desired.
 * @param headerTitle Header title to be displayed at the top of the bottom sheet.
 * @param hideDragHandle Whether the drag handle should be hidden.
 * @param lifecycleOwner [LifecycleOwner] used to configure the
 * [fragment result listeners][setSelectSheetFragmentResultListeners].
 * @param onConfirm Lambda that is invoked when the "Confirm" action is pressed in
 * the bottom sheet, with the selected item passed as the argument.
 * @param onCanceled Lambda that is invoked when the "Cancel" option is pressed in
 * the bottom sheet.
 * @param groupInit Options to configure the [OptionBottomSheetGroup.SingleSelect]
 * to be used.
 * @see multiSelectGroup
 * @see com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.selectBottomSheet
 * @see multiSelectBottomSheet
 * @see setSelectSheetFragmentResultListeners
 */
inline fun <Id : Any> FragmentManager.showMultiSelectBottomSheet(
    idSerializer: IdSerializer<Id>,
    headerTitle: String,
    hideDragHandle: Boolean = false,
    lifecycleOwner: LifecycleOwner,
    crossinline onConfirm: (selectedItems: Set<OptionBottomSheetItem>) -> Unit = {},
    crossinline onCanceled: () -> Unit = {},
    groupInit: MultiSelectBottomSheetGroupBuilder<Id>.() -> Unit,
) = multiSelectBottomSheet(
    idSerializer = idSerializer,
    headerTitle = headerTitle,
    hideDragHandle = hideDragHandle,
    groupInit = groupInit
).apply {
    show(this@showMultiSelectBottomSheet, tag)
    setSelectSheetFragmentResultListeners(
        lifecycleOwner = lifecycleOwner,
        onConfirm = onConfirm,
        onCanceled = onCanceled
    )
}

/**
 * Creates a [com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.SelectBottomSheetFragment] set to **multi**-selection mode with the
 * specified options.
 *
 * If single-selection is desired, use [singleSelectBottomSheet] instead.
 * @param headerTitle Header title to be displayed at thetop of the bottom sheet.
 * @param hideDragHandle Whether the drag handle should be hidden.
 * @param groupInit Options to configure the [OptionBottomSheetGroup.MultiSelect]
 * to be used.
 */
inline fun <Id : Any> multiSelectBottomSheet(
    idSerializer: IdSerializer<Id>,
    headerTitle: String,
    hideDragHandle: Boolean = false,
    groupInit: MultiSelectBottomSheetGroupBuilder<Id>.() -> Unit,
): SelectBottomSheetFragment = selectBottomSheet(
    headerTitle = headerTitle,
    hideDragHandle = hideDragHandle,
    items = multiSelectGroup(idSerializer = idSerializer, init = groupInit)
)

/** Adds the existing [group]'s items to the receiver [MultiSelectBottomSheetGroupBuilder]. */
fun MultiSelectBottomSheetGroupBuilder<*>.addItems(
    group: OptionBottomSheetGroup.MultiSelect
) {
    group.forEach {
        addItem(group.isChecked(it), it)
    }
}

/** Creates a [OptionBottomSheetGroup.MultiSelect] using the specified [init] options. */
inline fun <Id : Any> multiSelectGroup(
    idSerializer: IdSerializer<Id>,
    init: MultiSelectBottomSheetGroupBuilder<Id>.() -> Unit
): OptionBottomSheetGroup.MultiSelect =
    MultiSelectBottomSheetGroupBuilder(idSerializer = idSerializer).apply(init).build()
