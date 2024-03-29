package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.dsl

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.ModalBottomSheetAdapter
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.annotations.ModalBottomSheetCheckableBehavior
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces.ModalBottomSheetGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces.ModalBottomSheetItem

/** DSL marker for the [ModalBottomSheetBuilder]. */
@DslMarker
annotation class ModalBottomSheetDsl

@ModalBottomSheetDsl
class ModalBottomSheetBuilder {
    private var items = mutableListOf<ModalBottomSheetItem>()

    private var groupItems = mutableListOf<ModalBottomSheetGroupItemsBuilder>()

    /** Adds a new item to the bottom sheet and returns the created item. */
    fun item(item: ModalBottomSheetItem) = item.also { items += it }

    /** Adds a new item to the bottom sheet and returns the created item. */
    private fun item(init: ModalBottomSheetItemBuilder.() -> Unit) =
        ModalBottomSheetItemBuilder().apply(init).build().also { items += it }

    /** Adds a new item to the bottom sheet and returns the created item. */
    fun item(title: String, init: ModalBottomSheetItemBuilder.() -> Unit = {}) = item {
        this.title = title
        init()
    }

    /** Adds a new item to the bottom sheet using the `+=` operator. */
    operator fun plusAssign(item: ModalBottomSheetItem) {
        item(item)
    }

    /** Adds a new item to the bottom sheet using the `+=` operator. */
    operator fun plusAssign(init: ModalBottomSheetItemBuilder.() -> Unit) {
        item(init)
    }

    /** Adds the specified list of items to the bottom sheet and returns the created items. */
    fun items(vararg init: ModalBottomSheetItemBuilder.() -> Unit) =
        init.map { ModalBottomSheetItemBuilder().apply(it).build() }
            .also { this.items = it.toMutableList() }

    /** Adds the specified list of items to the bottom sheet. */
    fun items(vararg items: ModalBottomSheetItem) = items(items.toList())

    /** Adds the specified list of items to the bottom sheet. */
    fun items(items: List<ModalBottomSheetItem>) =
        items.also { this.items = it.toMutableList() }

    /** Adds the specified list of items to the bottom sheet. */
    fun items(itemsInit: MutableList<ModalBottomSheetItem>.() -> Unit) =
        items(buildList(itemsInit))

    /** Creates a new group. */
    fun group(group: ModalBottomSheetGroup) =
        ModalBottomSheetGroupItemsBuilder(group).also { groupItems += it }

    /** Creates a new group with the specified list of items and returns the created items. */
    fun group(
        group: ModalBottomSheetGroup,
        groupItemsInit: ModalBottomSheetGroupItemsBuilder.() -> Unit
    ) =
        ModalBottomSheetGroupItemsBuilder(group)
            .apply(groupItemsInit)
            .also { groupItems += it }
            .build()

    /** Creates a new group. */
    private fun group(init: ModalBottomSheetGroupBuilder.() -> Unit) =
        ModalBottomSheetGroupItemsBuilder(ModalBottomSheetGroupBuilder().apply(init).build())
            .also { groupItems += it }

    /** Creates a new group. */
    fun group(id: Int, init: ModalBottomSheetGroupBuilder.() -> Unit) = group {
        this.id = id
        init()
    }

    /** Creates a new group with the specified list of items and returns the created items. */
    private fun group(
        init: ModalBottomSheetGroupBuilder.() -> Unit,
        groupItemsInit: ModalBottomSheetGroupItemsBuilder.() -> Unit
    ) =
        ModalBottomSheetGroupItemsBuilder(ModalBottomSheetGroupBuilder().apply(init).build())
            .apply(groupItemsInit)
            .also { groupItems += it }
            .build()

    /** Creates a new group with the specified list of items and returns the created items. */
    fun group(
        id: Int,
        init: ModalBottomSheetGroupBuilder.() -> Unit,
        groupItemsInit: ModalBottomSheetGroupItemsBuilder.() -> Unit
    ) = group({
        this.id = id
        init()
    }, groupItemsInit)

    /** Gets the final list of items. */
    fun build() = items + groupItems.flatMap { it.build() }
}

@ModalBottomSheetDsl
class ModalBottomSheetGroupBuilder(
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

@ModalBottomSheetDsl
class ModalBottomSheetGroupItemsBuilder(private val group: ModalBottomSheetGroup) {
    private var items = mutableListOf<ModalBottomSheetItem>()

    /** Adds the specified item to the group and returns the created item. */
    private fun item(init: ModalBottomSheetItemBuilder.() -> Unit) =
        ModalBottomSheetItemBuilder().apply(init).build()
            .also { items += it }

    /** Adds the specified item to the group and returns the created item. */
    fun item(title: String, init: ModalBottomSheetItemBuilder.() -> Unit) = item {
        this.title = title
        init()
    }

    /** Adds the specified item to the group and returns the created item. */
    fun item(item: ModalBottomSheetItem) =
        item.also { items += it }

    /** Adds the specified list of items to the group and returns the created items. */
    fun items(vararg init: ModalBottomSheetItemBuilder.() -> Unit) =
        init.map { ModalBottomSheetItemBuilder().apply(it).build() }
            .also { items += it }

    /** Adds the specified list of items to the group and returns the created items. */
    fun items(vararg items: ModalBottomSheetItem) = items(items.toList())

    /** Adds the specified list of items to the group and returns the created items. */
    fun items(items: List<ModalBottomSheetItem>) =
        items.also { this.items = it.toMutableList() }

    /** Adds the specified list of items to the group and returns the created items. */
    fun items(itemsInit: MutableList<ModalBottomSheetItem>.() -> Unit) =
        items(buildList(itemsInit))

    /** Returns the added list of items with the group assigned to it. */
    fun build() = items.map { it.apply { group = this@ModalBottomSheetGroupItemsBuilder.group } }
}

@ModalBottomSheetDsl
class ModalBottomSheetItemBuilder(
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

/** Creates a [ModalBottomSheetGroup] using DSL syntax. */
@ModalBottomSheetDsl
inline fun group(init: ModalBottomSheetGroupBuilder.() -> Unit) =
    ModalBottomSheetGroupBuilder().apply(init).build()

/** Creates a [ModalBottomSheetItem] using DSL syntax. */
@ModalBottomSheetDsl
inline fun item(init: ModalBottomSheetItemBuilder.() -> Unit) =
    ModalBottomSheetItemBuilder().apply(init).build()

/** Creates a list of [ModalBottomSheetItem]s using DSL syntax. */
@ModalBottomSheetDsl
inline fun items(init: ModalBottomSheetBuilder.() -> Unit) =
    ModalBottomSheetBuilder().apply(init).build()
