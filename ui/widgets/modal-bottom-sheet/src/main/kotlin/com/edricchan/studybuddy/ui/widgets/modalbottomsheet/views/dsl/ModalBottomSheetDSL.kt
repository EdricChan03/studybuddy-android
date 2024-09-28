package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.dsl

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.ModalBottomSheetAdapter
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces.ModalBottomSheetItem

/** DSL marker for the [ModalBottomSheetBuilder]. */
@DslMarker
annotation class ModalBottomSheetDsl

@ModalBottomSheetDsl
class ModalBottomSheetBuilder {
    private var items = mutableListOf<ModalBottomSheetItem>()

    /** Adds a new item to the bottom sheet and returns the created item. */
    fun item(item: ModalBottomSheetItem) = item.also { items += it }

    /** Adds a new item to the bottom sheet and returns the created item. */
    private fun item(init: ModalBottomSheetItemBuilder.() -> Unit) =
        ModalBottomSheetItemBuilder().apply(init).build().also { items += it }

    /** Adds a new item to the bottom sheet and returns the created item. */
    @ModalBottomSheetDsl
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

    /** Gets the final list of items. */
    fun build() = items.toList()
}

@ModalBottomSheetDsl
class ModalBottomSheetItemBuilder(
    private val item: ModalBottomSheetItem = ModalBottomSheetItem(title = "")
) {
    var id: Int = item.id
    var title: String = item.title

    var iconValue = item.icon

    fun setIcon(icon: Drawable) {
        iconValue = ModalBottomSheetItem.Icon.Raw(icon.toBitmap())
    }

    fun setIcon(@DrawableRes iconRes: Int) {
        iconValue = ModalBottomSheetItem.Icon.Resource(iconRes)
    }

    var requestDismissOnClick: Boolean = item.requestDismissOnClick

    var onItemClickListener: ModalBottomSheetAdapter.OnItemClickListener? = item.onItemClickListener

    fun setItemClickListener(listener: ModalBottomSheetAdapter.OnItemClickListener) {
        onItemClickListener = listener
    }

    var visible: Boolean = item.visible
    var enabled: Boolean = item.enabled

    fun build(): ModalBottomSheetItem = item.copy(
        id = id,
        title = title,
        icon = iconValue,
        onItemClickListener = onItemClickListener,
        visible = visible,
        enabled = enabled,
        requestDismissOnClick = requestDismissOnClick
    )
}

/**
 * Creates a copy of the receiver [ModalBottomSheetItem] with the specified
 * properties modified to match that of [buildInit].
 * @return The modified copy of [ModalBottomSheetItem].
 */
fun ModalBottomSheetItem.copy(
    buildInit: ModalBottomSheetItemBuilder.() -> Unit
) = ModalBottomSheetItemBuilder(this).apply(buildInit).build()

/** Creates a [ModalBottomSheetItem] using DSL syntax. */
@ModalBottomSheetDsl
inline fun item(init: ModalBottomSheetItemBuilder.() -> Unit) =
    ModalBottomSheetItemBuilder().apply(init).build()

/** Creates a list of [ModalBottomSheetItem]s using DSL syntax. */
@ModalBottomSheetDsl
inline fun items(init: ModalBottomSheetBuilder.() -> Unit) =
    ModalBottomSheetBuilder().apply(init).build()
