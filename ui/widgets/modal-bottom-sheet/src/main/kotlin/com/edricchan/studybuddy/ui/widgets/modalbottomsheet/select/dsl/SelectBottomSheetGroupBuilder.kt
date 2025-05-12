package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.dsl

import androidx.annotation.DrawableRes
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetItem

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

