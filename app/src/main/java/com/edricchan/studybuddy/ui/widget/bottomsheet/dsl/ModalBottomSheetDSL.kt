package com.edricchan.studybuddy.ui.widget.bottomsheet.dsl

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.edricchan.studybuddy.ui.widget.bottomsheet.ModalBottomSheetAdapter
import com.edricchan.studybuddy.ui.widget.bottomsheet.annotations.ModalBottomSheetCheckableBehavior
import com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces.ModalBottomSheetGroup
import com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces.ModalBottomSheetItem

class ModalBottomSheetDSL {
    private var items = mutableListOf<ModalBottomSheetItem>()

    private var groupItems = mutableListOf<ModalBottomSheetGroupItemsDSL>()

    /** Adds a new item to the bottom sheet and returns the created item. */
    fun item(item: ModalBottomSheetItem) = item.also { items += it }

    /** Adds a new item to the bottom sheet and returns the created item. */
    fun item(init: ModalBottomSheetItemDSL.() -> Unit) =
        ModalBottomSheetItemDSL().apply(init).build().also { items += it }

    /** Adds a new item to the bottom sheet using the `+=` operator. */
    operator fun plusAssign(item: ModalBottomSheetItem) {
        item(item)
    }

    /** Adds a new item to the bottom sheet using the `+=` operator. */
    operator fun plusAssign(init: ModalBottomSheetItemDSL.() -> Unit) {
        item(init)
    }

    /** Adds the specified list of items to the bottom sheet and returns the created items. */
    fun items(vararg init: ModalBottomSheetItemDSL.() -> Unit) =
        init.map { ModalBottomSheetItemDSL().apply(it).build() }
            .also { this.items = it.toMutableList() }

    /** Adds the specified list of items to the bottom sheet. */
    fun items(vararg items: ModalBottomSheetItem) = items(items.toList())

    /** Adds the specified list of items to the bottom sheet. */
    fun items(items: List<ModalBottomSheetItem>) =
        items.also { this.items = it.toMutableList() }

    /** Creates a new group. */
    fun group(group: ModalBottomSheetGroup) =
        ModalBottomSheetGroupItemsDSL(group).also { groupItems += it }

    /** Creates a new group with the specified list of items and returns the created items. */
    fun group(
        group: ModalBottomSheetGroup,
        groupItemsInit: ModalBottomSheetGroupItemsDSL.() -> Unit
    ) =
        ModalBottomSheetGroupItemsDSL(group)
            .apply(groupItemsInit)
            .also { groupItems += it }
            .build()

    /** Creates a new group. */
    fun group(init: ModalBottomSheetGroupDSL.() -> Unit) =
        ModalBottomSheetGroupItemsDSL(ModalBottomSheetGroupDSL().apply(init).build())
            .also { groupItems += it }

    /** Creates a new group with the specified list of items and returns the created items. */
    fun group(
        init: ModalBottomSheetGroupDSL.() -> Unit,
        groupItemsInit: ModalBottomSheetGroupItemsDSL.() -> Unit
    ) =
        ModalBottomSheetGroupItemsDSL(ModalBottomSheetGroupDSL().apply(init).build())
            .apply(groupItemsInit)
            .also { groupItems += it }
            .build()

    /** Gets the final list of items. */
    fun build() = items + groupItems.map { it.build() }.flatten()
}

class ModalBottomSheetGroupDSL(
    private val group: ModalBottomSheetGroup = ModalBottomSheetGroup(id = Integer.MIN_VALUE),
    groupOptions: ModalBottomSheetGroup.() -> Unit = {}
) {
    init {
        group.apply(groupOptions)
    }

    var id by group::id

    @ModalBottomSheetCheckableBehavior
    var checkableBehavior by group::checkableBehavior
    var onItemCheckedChangeListener by group::onItemCheckedChangeListener

    fun setItemCheckedChangeListener(listener: ModalBottomSheetAdapter.OnItemCheckedChangeListener) {
        onItemCheckedChangeListener = listener
    }

    var visible by group::visible
    var enabled by group::enabled
    var selected by group::selected

    fun build() = group
}

class ModalBottomSheetGroupItemsDSL(val group: ModalBottomSheetGroup) {
    private var items = mutableListOf<ModalBottomSheetItem>()

    /** Adds the specified item to the group and returns the created item. */
    fun item(init: ModalBottomSheetItemDSL.() -> Unit) =
        ModalBottomSheetItemDSL().apply(init).build()
            .also { items += it }

    /** Adds the specified item to the group and returns the created item. */
    fun item(item: ModalBottomSheetItem) =
        item.also { items += it }

    /** Adds the specified list of items to the group and returns the created items. */
    fun items(vararg init: ModalBottomSheetItemDSL.() -> Unit) =
        init.map { ModalBottomSheetItemDSL().apply(it).build() }
            .also { items += it }

    /** Adds the specified list of items to the group and returns the created items. */
    fun items(vararg items: ModalBottomSheetItem) = items(items.toList())

    /** Adds the specified list of items to the group and returns the created items. */
    fun items(items: List<ModalBottomSheetItem>) =
        items.also { this.items = it.toMutableList() }

    /** Returns the added list of items with the group assigned to it. */
    fun build() = items.map { it.apply { group = this@ModalBottomSheetGroupItemsDSL.group } }
}

class ModalBottomSheetItemDSL(
    private val item: ModalBottomSheetItem = ModalBottomSheetItem(title = ""),
    itemOptions: ModalBottomSheetItem.() -> Unit = {}
) {
    init {
        item.apply(itemOptions)
    }

    var id by item::id
    var title by item::title

    private var icon by item::icon
    private var iconDrawable by item::iconDrawable

    fun setIcon(icon: Drawable) {
        iconDrawable = icon
    }

    fun setIcon(@DrawableRes iconRes: Int) {
        icon = iconRes
    }

    var onItemClickListener by item::onItemClickListener

    fun setItemClickListener(listener: ModalBottomSheetAdapter.OnItemClickListener) {
        onItemClickListener = listener
    }

    var visible by item::visible
    var enabled by item::enabled

    var group by item::group

    fun build() = item
}

inline fun group(init: ModalBottomSheetGroupDSL.() -> Unit) =
    ModalBottomSheetGroupDSL().apply(init).build()

inline fun item(init: ModalBottomSheetItemDSL.() -> Unit) =
    ModalBottomSheetItemDSL().apply(init).build()

inline fun items(init: ModalBottomSheetDSL.() -> Unit) =
    ModalBottomSheetDSL().apply(init).build()
