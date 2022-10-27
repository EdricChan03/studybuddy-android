package com.edricchan.studybuddy.ui.modules.task.adapter.itemkeyprovider

import androidx.recyclerview.selection.ItemKeyProvider
import com.edricchan.studybuddy.interfaces.TodoItem

/**
 * Creates a new provider.
 *
 * @param mItemList The list of tasks
 */
class TodoItemKeyProvider(
    private val mItemList: List<TodoItem>
) : ItemKeyProvider<String>(SCOPE_CACHED) {
    private val mKeyToPosition: MutableMap<String, Int>

    init {
        this.mKeyToPosition = HashMap(mItemList.size)
        var i = 0
        for ((id) in mItemList) {
            mKeyToPosition[id] = i
            i++
        }

    }

    override fun getKey(position: Int): String {
        return mItemList[position].id
    }

    override fun getPosition(key: String): Int {
        return mKeyToPosition[key]!!
    }
}
