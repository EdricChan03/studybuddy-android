package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.dsl

import androidx.annotation.DrawableRes
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

@DslMarker
annotation class SelectableBottomSheetDsl

/**
 * Builder class to create instances of [OptionBottomSheetItem].
 * @property id The unique ID, which should be [android.os.Parcelable].
 * @property title The title to be displayed for this item.
 */
@SelectableBottomSheetDsl
class SelectBottomSheetItemBuilder<Id>(
    val id: Id,
    val title: String
) {
    /** The icon's resource, or `null` to not show any icon. */
    @DrawableRes
    var iconRes: Int? = null

    /** Whether clicking on this item has any effect. */
    var enabled: Boolean = true

    /** Creates the [OptionBottomSheetItem] with the specified options from this builder. */
    fun build(): OptionBottomSheetItem<Id> = OptionBottomSheetItem(
        id = id,
        title = title,
        icon = iconRes,
        enabled = enabled
    )
}

/**
 * Creates a [OptionBottomSheetItem] with the specified arguments.
 * @param id Unique ID for this item. This value should be [android.os.Parcelable].
 * @param title Title to be displayed for this item.
 * @param init Options for the [OptionBottomSheetItem].
 * @see SelectBottomSheetItemBuilder
 */
@SelectableBottomSheetDsl
fun <Id> item(
    id: Id,
    title: String,
    init: SelectBottomSheetItemBuilder<Id>.() -> Unit
): OptionBottomSheetItem<Id> =
    SelectBottomSheetItemBuilder(id, title).apply(init).build()

@SelectableBottomSheetDsl
sealed class SelectBottomSheetGroupBuilder<Id, T : OptionBottomSheetGroup<Id>> {
    /** The items to be used for this [OptionBottomSheetGroup]. */
    val items = mutableListOf<OptionBottomSheetItem<Id>>()

    /** Whether the items in this group are selectable. */
    var enabled: Boolean = true

    /**
     * Specifies which item should be selected.
     *
     * If this builder takes only a [single-selection][SingleSelectBottomSheetGroupBuilder],
     * there should **only** be one value that returns `true`. If multiple values return
     * `true` when passed to this lambda, the first item that returns `true` will be used
     * as the selected item.
     */
    var isSelected: ((item: OptionBottomSheetItem<Id>, index: Int) -> Boolean)? = null

    /**
     * Adds an item to this group with the specified arguments.
     *
     * @param selected Whether this item should be initially marked as selected.
     * If `null` is specified, [isSelected] will be used to determine its selection
     * state, or `false` otherwise.
     * @param item The item to add.
     * @return The added item.
     */
    abstract fun addItem(
        selected: Boolean? = false,
        item: OptionBottomSheetItem<Id>
    ): OptionBottomSheetItem<Id>

    /**
     * Adds an item to this group with the specified arguments.
     *
     * @param id Unique ID for this item. The value should be [android.os.Parcelable].
     * @param title Title to be displayed for this item in the user interface.
     * @param selected Whether this item should be initially marked as selected.
     * If `null` is specified, [isSelected] will be used to determine its selection
     * state, or `false` otherwise.
     * @param itemInit Additional options for the specified item.
     * @return The added item.
     */
    open fun addItem(
        id: Id,
        title: String,
        selected: Boolean? = false,
        itemInit: SelectBottomSheetItemBuilder<Id>.() -> Unit = {}
    ): OptionBottomSheetItem<Id> {
        return addItem(selected, item(id, title, itemInit))
    }

    /**
     * Adds the list of items with their selection status.
     *
     * This is the equivalent of the following code:
     *
     * ```
     * items.forEach {
     *     addItem(it.second, it.first)
     * }
     * ```
     *
     * @param items The items to be added, with their selection status.
     * @return The added items.
     * @see addItem
     */
    open fun addItems(
        vararg items: Pair<OptionBottomSheetItem<Id>, Boolean>
    ): List<OptionBottomSheetItem<Id>> = items.map {
        addItem(it.second, it.first)
    }

    /** Removes the [item] from this group. */
    abstract fun removeItem(item: OptionBottomSheetItem<Id>)

    /** Removes the set of [items] from this group. */
    open fun removeItems(vararg items: OptionBottomSheetItem<Id>) {
        items.forEach(::removeItem)
    }

    /** Creates the group [T] with the specified options from this builder. */
    abstract fun build(): T
}

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

/** Adds the existing [group]'s items to the receiver [SingleSelectBottomSheetGroupBuilder]. */
fun <Id> SingleSelectBottomSheetGroupBuilder<Id>.addItems(
    group: OptionBottomSheetGroup.SingleSelect<Id>
) {
    group.forEach {
        addItem(group.isChecked(it), it)
    }
}

/** Creates a [OptionBottomSheetGroup.SingleSelect] using the specified [init] options. */
inline fun <Id> singleSelectGroup(
    init: SingleSelectBottomSheetGroupBuilder<Id>.() -> Unit
): OptionBottomSheetGroup.SingleSelect<Id> =
    SingleSelectBottomSheetGroupBuilder<Id>().apply(init).build()

/**
 * Creates a [SelectBottomSheetFragment] set to **single**-selection mode with
 * the specified options.
 *
 * If multi-selection is desired, use [multiSelectBottomSheet] instead.
 * @param headerTitle Header title to be displayed at the top of the bottom sheet.
 * @param hideDragHandle Whether the drag handle should be hidden.
 * @param groupInit Options to configure the [OptionBottomSheetGroup.SingleSelect]
 * to be used.
 * @see singleSelectGroup
 * @see selectBottomSheet
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
 * the specified options, bound to the receiver [FragmentManager].
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
 * the specified options, bound to the receiver [Fragment]'s [Fragment.getParentFragmentManager].
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
 * the specified options, bound to the receiver [FragmentActivity]'s
 * [FragmentActivity.getSupportFragmentManager].
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

