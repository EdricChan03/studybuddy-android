package com.edricchan.studybuddy.ui.modules.task.adapter.itemkeyprovider

import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem

/**
 * Creates a new provider.
 *
 * @param itemList The list of tasks
 */
class TodoItemKeyProvider(
    private val itemList: List<TodoItem>
) : ItemKeyProvider<String>(SCOPE_CACHED) {
    private val keyToPosition = itemList.withIndex().associate { it.value.id to it.index }

    override fun getKey(position: Int): String = itemList[position].id

    override fun getPosition(key: String): Int = keyToPosition[key] ?: RecyclerView.NO_POSITION
}
