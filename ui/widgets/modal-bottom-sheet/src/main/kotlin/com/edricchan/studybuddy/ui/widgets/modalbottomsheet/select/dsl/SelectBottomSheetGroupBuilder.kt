package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.dsl

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.IdSerializer
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
class SelectBottomSheetItemBuilder(
    val id: String,
    val title: String
) {
    /** The icon's resource, or `null` to not show any icon. */
    @DrawableRes
    var iconRes: Int? = null

    /** Whether clicking on this item has any effect. */
    var enabled: Boolean = true

    /** Creates the [OptionBottomSheetItem] with the specified options from this builder. */
    fun build(): OptionBottomSheetItem = OptionBottomSheetItem(
        id = id,
        title = title,
        icon = iconRes,
        enabled = enabled
    )
}

/**
 * Creates a [OptionBottomSheetItem] with the specified arguments.
 * @param id Unique ID for this item.
 * @param title Title to be displayed for this item.
 * @param init Options for the [OptionBottomSheetItem].
 * @see SelectBottomSheetItemBuilder
 */
fun item(
    id: String,
    title: String,
    init: SelectBottomSheetItemBuilder.() -> Unit
): OptionBottomSheetItem =
    SelectBottomSheetItemBuilder(id, title).apply(init).build()

@SelectableBottomSheetDsl
sealed class SelectBottomSheetGroupBuilder<Id : Any, T : OptionBottomSheetGroup>(
    /** [IdSerializer] to be used when serialising `id`s to their [String] equivalent. */
    val idSerializer: IdSerializer<Id>
) {
    /** The items to be used for this [OptionBottomSheetGroup]. */
    val items = mutableListOf<OptionBottomSheetItem>()

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
    var isSelected: ((item: OptionBottomSheetItem, index: Int) -> Boolean)? = null

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
        item: OptionBottomSheetItem
    ): OptionBottomSheetItem

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
        itemInit: SelectBottomSheetItemBuilder.() -> Unit = {}
    ): OptionBottomSheetItem = addItem(
        selected = selected,
        item = item(idSerializer.serialize(id), title, itemInit)
    )

    /**
     * Adds an item to this group with the specified arguments.
     *
     * @param context [Context] to use to resolve the [titleRes] parameter with.
     * @param id Unique ID for this item. The value should be [android.os.Parcelable].
     * @param titleRes Title to be displayed for this item in the user interface as a
     * string resource.
     * @param selected Whether this item should be initially marked as selected.
     * If `null` is specified, [isSelected] will be used to determine its selection
     * state, or `false` otherwise.
     * @param itemInit Additional options for the specified item.
     * @return The added item.
     */
    context(context: Context)
    open fun addItem(
        id: Id,
        @StringRes
        titleRes: Int,
        selected: Boolean? = false,
        itemInit: SelectBottomSheetItemBuilder.() -> Unit = {}
    ): OptionBottomSheetItem = addItem(
        selected = selected,
        item = item(idSerializer.serialize(id), context.getString(titleRes), itemInit)
    )

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
        vararg items: Pair<OptionBottomSheetItem, Boolean>
    ): List<OptionBottomSheetItem> = items.map {
        addItem(it.second, it.first)
    }

    /** Removes the [item] from this group. */
    abstract fun removeItem(item: OptionBottomSheetItem)

    /** Removes the set of [items] from this group. */
    open fun removeItems(vararg items: OptionBottomSheetItem) {
        items.forEach(::removeItem)
    }

    /** Creates the group [T] with the specified options from this builder. */
    abstract fun build(): T
}

