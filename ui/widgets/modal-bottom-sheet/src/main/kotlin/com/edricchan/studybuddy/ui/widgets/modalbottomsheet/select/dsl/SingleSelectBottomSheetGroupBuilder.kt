package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.dsl

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.SelectBottomSheetFragment
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetItem
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.selectBottomSheet
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.setSelectSheetFragmentResultListeners

/**
 * Group builder to create a [OptionBottomSheetGroup.SingleSelect].
 *
 * To create a [OptionBottomSheetGroup.MultiSelect], use [MultiSelectBottomSheetGroupBuilder]
 * instead.
 *
 * Consumers should use the relevant [singleSelectGroup] and [singleSelectBottomSheet]
 * methods where appropriate.
 */
class SingleSelectBottomSheetGroupBuilder<Id> :
    SelectBottomSheetGroupBuilder<Id, OptionBottomSheetGroup.SingleSelect<Id>>() {
    /**
     * The item's index in this group which should be initially selected,
     * or `null` for no initial selection.
     */
    var selectedIndex: Int? = null

    override fun addItem(
        selected: Boolean?,
        item: OptionBottomSheetItem<Id>
    ): OptionBottomSheetItem<Id> {
        items += item
        val index = items.indexOf(item)

        if (selected == true || isSelected?.invoke(item, index) == true) selectedIndex = index

        return item
    }

    override fun removeItem(
        item: OptionBottomSheetItem<Id>
    ) {
        if (selectedIndex != null && selectedIndex == items.indexOf(item)) {
            selectedIndex = null
        }

        items -= item
    }

    /** Creates a [OptionBottomSheetGroup.SingleSelect] with the options from this builder. */
    override fun build(): OptionBottomSheetGroup.SingleSelect<Id> =
        OptionBottomSheetGroup.SingleSelect(
            items = items.toList(),
            enabled = enabled,
            selectedIndex = selectedIndex
        )
}

/** Creates a [com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetGroup.SingleSelect] using the specified [init] options. */
inline fun <Id> singleSelectGroup(
    init: SingleSelectBottomSheetGroupBuilder<Id>.() -> Unit
): OptionBottomSheetGroup.SingleSelect<Id> =
    SingleSelectBottomSheetGroupBuilder<Id>().apply(init).build()

/** Adds the existing [group]'s items to the receiver [SingleSelectBottomSheetGroupBuilder]. */
fun <Id> SingleSelectBottomSheetGroupBuilder<Id>.addItems(
    group: OptionBottomSheetGroup.SingleSelect<Id>
) {
    group.forEach {
        addItem(group.isChecked(it), it)
    }
}

/**
 * Creates a [com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.SelectBottomSheetFragment] set to **single**-selection mode with
 * the specified options.
 *
 * If multi-selection is desired, use [multiSelectBottomSheet] instead.
 * @param headerTitle Header title to be displayed at the top of the bottom sheet.
 * @param hideDragHandle Whether the drag handle should be hidden.
 * @param groupInit Options to configure the [OptionBottomSheetGroup.SingleSelect]
 * to be used.
 * @see singleSelectGroup
 * @see com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.selectBottomSheet
 * @see showSingleSelectBottomSheet
 */
inline fun <Id : Any> singleSelectBottomSheet(
    headerTitle: String,
    hideDragHandle: Boolean = false,
    groupInit: SingleSelectBottomSheetGroupBuilder<Id>.() -> Unit
): SelectBottomSheetFragment<Id> = selectBottomSheet(
    headerTitle = headerTitle,
    hideDragHandle = hideDragHandle,
    items = singleSelectGroup(groupInit)
)

/**
 * Shows a [SelectBottomSheetFragment] set to **single**-selection mode with
 * the specified options, bound to the receiver [androidx.fragment.app.FragmentManager].
 *
 * Use [showMultiSelectBottomSheet] instead if multi-selection is desired.
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
 * @see singleSelectGroup
 * @see selectBottomSheet
 * @see singleSelectBottomSheet
 * @see setSelectSheetFragmentResultListeners
 */
inline fun <Id : Any> FragmentManager.showSingleSelectBottomSheet(
    headerTitle: String,
    hideDragHandle: Boolean = false,
    lifecycleOwner: LifecycleOwner,
    crossinline onConfirm: (selectedItem: OptionBottomSheetItem<Id>) -> Unit = {},
    crossinline onCanceled: () -> Unit = {},
    groupInit: SingleSelectBottomSheetGroupBuilder<Id>.() -> Unit,
): SelectBottomSheetFragment<Id> = singleSelectBottomSheet(
    headerTitle = headerTitle,
    hideDragHandle = hideDragHandle,
    groupInit = groupInit
).apply {
    show(this@showSingleSelectBottomSheet, tag)
    setSelectSheetFragmentResultListeners(
        lifecycleOwner = lifecycleOwner, onConfirm = {
            onConfirm(it.single())
        }, onCanceled = onCanceled
    )
}

/**
 * Shows a [SelectBottomSheetFragment] set to **single**-selection mode with
 * the specified options, bound to the receiver [androidx.fragment.app.FragmentActivity]'s
 * [androidx.fragment.app.FragmentActivity.getSupportFragmentManager].
 *
 * Use [showMultiSelectBottomSheet] instead if multi-selection is desired.
 *
 * This overload allows for a string resource to be used for the [header title][headerTitleRes].
 * @param headerTitleRes String resource for the header title to be displayed at the
 * top of the bottom sheet.
 * @param hideDragHandle Whether the drag handle should be hidden.
 * @param lifecycleOwner [LifecycleOwner] used to configure the
 * [fragment result listeners][setSelectSheetFragmentResultListeners].
 * @param onConfirm Lambda that is invoked when the "Confirm" action is pressed in
 * the bottom sheet, with the selected item passed as the argument.
 * @param onCanceled Lambda that is invoked when the "Cancel" option is pressed in
 * the bottom sheet.
 * @param groupInit Options to configure the [OptionBottomSheetGroup.SingleSelect]
 * to be used.
 * @see singleSelectGroup
 * @see selectBottomSheet
 * @see singleSelectBottomSheet
 * @see setSelectSheetFragmentResultListeners
 */
inline fun <Id : Any> FragmentActivity.showSingleSelectBottomSheet(
    @StringRes headerTitleRes: Int,
    hideDragHandle: Boolean = false,
    lifecycleOwner: LifecycleOwner = this,
    crossinline onConfirm: (selectedItems: OptionBottomSheetItem<Id>) -> Unit = {},
    crossinline onCanceled: () -> Unit = {},
    groupInit: SingleSelectBottomSheetGroupBuilder<Id>.() -> Unit
): SelectBottomSheetFragment<Id> = supportFragmentManager.showSingleSelectBottomSheet(
    headerTitle = getString(headerTitleRes),
    hideDragHandle = hideDragHandle,
    groupInit = groupInit,
    lifecycleOwner = lifecycleOwner, onConfirm = onConfirm, onCanceled = onCanceled
)

/**
 * Shows a [SelectBottomSheetFragment] set to **single**-selection mode with
 * the specified options, bound to the receiver [FragmentActivity]'s
 * [FragmentActivity.getSupportFragmentManager].
 *
 * Use [showMultiSelectBottomSheet] instead if multi-selection is desired.
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
 * @see singleSelectGroup
 * @see selectBottomSheet
 * @see singleSelectBottomSheet
 * @see setSelectSheetFragmentResultListeners
 */
inline fun <Id : Any> FragmentActivity.showSingleSelectBottomSheet(
    headerTitle: String,
    hideDragHandle: Boolean = false,
    lifecycleOwner: LifecycleOwner = this,
    crossinline onConfirm: (selectedItems: OptionBottomSheetItem<Id>) -> Unit = {},
    crossinline onCanceled: () -> Unit = {},
    groupInit: SingleSelectBottomSheetGroupBuilder<Id>.() -> Unit
): SelectBottomSheetFragment<Id> = supportFragmentManager.showSingleSelectBottomSheet(
    headerTitle = headerTitle,
    hideDragHandle = hideDragHandle,
    lifecycleOwner = lifecycleOwner,
    onConfirm = onConfirm,
    onCanceled = onCanceled,
    groupInit = groupInit,
)

/**
 * Shows a [SelectBottomSheetFragment] set to **single**-selection mode with
 * the specified options, bound to the receiver [androidx.fragment.app.Fragment]'s [androidx.fragment.app.Fragment.getParentFragmentManager].
 *
 * Use [showMultiSelectBottomSheet] instead if multi-selection is desired.
 *
 * This overload allows for a string resource to be used for the [header title][headerTitleRes].
 * @param headerTitleRes String resource for the header title to be displayed at the
 * top of the bottom sheet.
 * @param hideDragHandle Whether the drag handle should be hidden.
 * @param lifecycleOwner [LifecycleOwner] used to configure the
 * [fragment result listeners][setSelectSheetFragmentResultListeners].
 * @param onConfirm Lambda that is invoked when the "Confirm" action is pressed in
 * the bottom sheet, with the selected item passed as the argument.
 * @param onCanceled Lambda that is invoked when the "Cancel" option is pressed in
 * the bottom sheet.
 * @param groupInit Options to configure the [OptionBottomSheetGroup.SingleSelect]
 * to be used.
 * @see singleSelectGroup
 * @see selectBottomSheet
 * @see singleSelectBottomSheet
 * @see setSelectSheetFragmentResultListeners
 */
inline fun <Id : Any> Fragment.showSingleSelectBottomSheet(
    @StringRes headerTitleRes: Int,
    hideDragHandle: Boolean = false,
    lifecycleOwner: LifecycleOwner = viewLifecycleOwner,
    crossinline onConfirm: (selectedItems: OptionBottomSheetItem<Id>) -> Unit = {},
    crossinline onCanceled: () -> Unit = {},
    groupInit: SingleSelectBottomSheetGroupBuilder<Id>.() -> Unit
): SelectBottomSheetFragment<Id> = parentFragmentManager.showSingleSelectBottomSheet(
    headerTitle = getString(headerTitleRes),
    hideDragHandle = hideDragHandle,
    groupInit = groupInit,
    lifecycleOwner = lifecycleOwner, onConfirm = onConfirm, onCanceled = onCanceled
)

/**
 * Shows a [SelectBottomSheetFragment] set to **single**-selection mode with
 * the specified options, bound to the receiver [Fragment]'s
 * [Fragment.getParentFragmentManager].
 *
 * Use [showMultiSelectBottomSheet] instead if multi-selection is desired.
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
 * @see singleSelectGroup
 * @see selectBottomSheet
 * @see singleSelectBottomSheet
 * @see setSelectSheetFragmentResultListeners
 */
inline fun <Id : Any> Fragment.showSingleSelectBottomSheet(
    headerTitle: String,
    hideDragHandle: Boolean = false,
    lifecycleOwner: LifecycleOwner = viewLifecycleOwner,
    crossinline onConfirm: (selectedItem: OptionBottomSheetItem<Id>) -> Unit = {},
    crossinline onCanceled: () -> Unit = {},
    groupInit: SingleSelectBottomSheetGroupBuilder<Id>.() -> Unit
): SelectBottomSheetFragment<Id> = parentFragmentManager.showSingleSelectBottomSheet(
    headerTitle = headerTitle,
    hideDragHandle = hideDragHandle,
    lifecycleOwner = lifecycleOwner,
    onConfirm = onConfirm,
    onCanceled = onCanceled,
    groupInit = groupInit,
)
