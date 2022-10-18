package com.edricchan.studybuddy.utils.recyclerview

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/** Attaches the specified [helper] to the [RecyclerView]. */
fun RecyclerView.attachItemTouchHelper(helper: ItemTouchHelper) {
    helper.attachToRecyclerView(this)
}

/**
 * Creates and attaches a [ItemTouchHelper] to the [RecyclerView] with
 * the specified arguments.
 * @param dragAndSwipeDirs The drag and swipe directions to use.
 * @param onMove The callback to be used for [ItemTouchHelper.SimpleCallback.onMove].
 * @param onSwiped The callback to be used for [ItemTouchHelper.SimpleCallback.onSwiped].
 * @return The created [ItemTouchHelper].
 */
fun RecyclerView.setItemTouchHelper(
    dragAndSwipeDirs: Pair<ItemTouchDirection, ItemTouchDirection> =
        ItemTouchDirection.None to ItemTouchDirection.None,
    onMove: CallbackOnMove = { _, _, _ -> false },
    onSwiped: CallbackOnSwiped = { _, _ -> }
) =
    setItemTouchHelper(dragAndSwipeDirs.first, dragAndSwipeDirs.second, onMove, onSwiped)

/**
 * Creates and attaches a [ItemTouchHelper] to the [RecyclerView] with
 * the specified arguments.
 * @param dragDirs The drag direction to use.
 * @param swipeDirs The swipe direction to use.
 * @param onMove The callback to be used for [ItemTouchHelper.SimpleCallback.onMove].
 * @param onSwiped The callback to be used for [ItemTouchHelper.SimpleCallback.onSwiped].
 * @return The created [ItemTouchHelper].
 */
fun RecyclerView.setItemTouchHelper(
    dragDirs: ItemTouchDirection = ItemTouchDirection.None,
    swipeDirs: ItemTouchDirection = ItemTouchDirection.None,
    onMove: CallbackOnMove = { _, _, _ -> false },
    onSwiped: CallbackOnSwiped = { _, _ -> }
) =
    ItemTouchHelper(ItemTouchHelperCallback(dragDirs, swipeDirs, onMove, onSwiped)).also {
        attachItemTouchHelper(it)
    }

/**
 * Creates and attaches a [ItemTouchHelper] to the [RecyclerView] with
 * the specified arguments.
 * @param dragAndSwipeDirs The list of drag and swipe directions to use.
 * @param onMove The callback to be used for [ItemTouchHelper.SimpleCallback.onMove].
 * @param onSwiped The callback to be used for [ItemTouchHelper.SimpleCallback.onSwiped].
 * @return The created [ItemTouchHelper].
 */
@JvmName("setItemTouchHelperPairList")
fun RecyclerView.setItemTouchHelper(
    dragAndSwipeDirs: Pair<List<ItemTouchDirection>, List<ItemTouchDirection>> =
        listOf(ItemTouchDirection.None) to listOf(ItemTouchDirection.None),
    onMove: CallbackOnMove = { _, _, _ -> false },
    onSwiped: CallbackOnSwiped = { _, _ -> }
) =
    setItemTouchHelper(dragAndSwipeDirs.first, dragAndSwipeDirs.second, onMove, onSwiped)

/**
 * Creates and attaches a [ItemTouchHelper] to the [RecyclerView] with
 * the specified arguments.
 * @param dragDirs The list of drag directions to use.
 * @param swipeDirs The list of swipe directions to use.
 * @param onMove The callback to be used for [ItemTouchHelper.SimpleCallback.onMove].
 * @param onSwiped The callback to be used for [ItemTouchHelper.SimpleCallback.onSwiped].
 * @return The created [ItemTouchHelper].
 */
fun RecyclerView.setItemTouchHelper(
    dragDirs: List<ItemTouchDirection> = listOf(ItemTouchDirection.None),
    swipeDirs: List<ItemTouchDirection> = listOf(ItemTouchDirection.None),
    onMove: CallbackOnMove = { _, _, _ -> false },
    onSwiped: CallbackOnSwiped = { _, _ -> }
) =
    ItemTouchHelper(
        ItemTouchHelperCallback(dragDirs.sum(), swipeDirs.sum(), onMove, onSwiped)
    ).also {
        attachItemTouchHelper(it)
    }

/** The direction to be used for [ItemTouchHelper]. */
@JvmInline
value class ItemTouchDirection(val direction: Int) {
    companion object {
        val Up = ItemTouchDirection(ItemTouchHelper.UP)
        val Down = ItemTouchDirection(ItemTouchHelper.DOWN)
        val Left = ItemTouchDirection(ItemTouchHelper.LEFT)
        val Right = ItemTouchDirection(ItemTouchHelper.RIGHT)
        val Start = ItemTouchDirection(ItemTouchHelper.START)
        val End = ItemTouchDirection(ItemTouchHelper.END)
        val None = ItemTouchDirection(0)
    }

    operator fun plus(otherDirection: ItemTouchDirection) =
        ItemTouchDirection(this.direction or otherDirection.direction)
}

/**
 * Sums the list of [ItemTouchDirection]s and returns the summed
 * result as a [ItemTouchDirection].
 */
fun Iterable<ItemTouchDirection>.sum() =
    reduce { acc, sum -> acc + sum }

internal typealias CallbackOnMove = (RecyclerView, RecyclerView.ViewHolder, RecyclerView.ViewHolder) -> Boolean
internal typealias CallbackOnSwiped = (RecyclerView.ViewHolder, Int) -> Unit

internal class ItemTouchHelperCallback(
    dragDirs: ItemTouchDirection,
    swipeDirs: ItemTouchDirection,
    private val onMoveCallback: CallbackOnMove,
    private val onSwipedCallback: CallbackOnSwiped
) : ItemTouchHelper.SimpleCallback(dragDirs.direction, swipeDirs.direction) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = onMoveCallback(recyclerView, viewHolder, target)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipedCallback(viewHolder, direction)
    }
}
