package com.edricchan.studybuddy.ui.modules.task.adapter

import com.edricchan.studybuddy.interfaces.TodoItem

/**
 * Creates an [TodosAdapter.OnItemClickListener] with the given arguments.
 * @param onItemClick See [TodosAdapter.OnItemClickListener.onItemClick].
 * @param onDeleteButtonClick See [TodosAdapter.OnItemClickListener.onDeleteButtonClick].
 * @param onMarkAsDoneButtonClick See [TodosAdapter.OnItemClickListener.onMarkAsDoneButtonClick].
 */
inline fun itemListener(
    crossinline onItemClick: (item: TodoItem, position: Int) -> Unit = { _, _ -> },
    crossinline onDeleteButtonClick: (item: TodoItem, position: Int) -> Unit = { _, _ -> },
    crossinline onMarkAsDoneButtonClick: (item: TodoItem, position: Int) -> Unit = { _, _ -> },
) = object : TodosAdapter.OnItemClickListener {
    override fun onItemClick(item: TodoItem, position: Int) = onItemClick(item, position)

    override fun onDeleteButtonClick(item: TodoItem, position: Int) =
        onDeleteButtonClick(item, position)

    override fun onMarkAsDoneButtonClick(item: TodoItem, position: Int) =
        onMarkAsDoneButtonClick(item, position)
}

enum class TodoItemClickType {
    /**
     * Denotes that the task item itself was clicked.
     * @see TodosAdapter.OnItemClickListener.onItemClick
     */
    Container,

    /**
     * Denotes that the task item's delete button was clicked.
     * @see TodosAdapter.OnItemClickListener.onDeleteButtonClick
     */
    DeleteButton,

    /**
     * Denotes that the task item's mark as done button was clicked.
     * @see TodosAdapter.OnItemClickListener.onMarkAsDoneButtonClick
     */
    MarkAsDoneButton
}

/**
 * Creates an [TodosAdapter.OnItemClickListener] with the given arguments.
 *
 * This method allows for a single lambda to be specified, with the originating clicked
 * type denoted as an [enum][TodoItemClickType].
 * @param onClick Lambda that is invoked with the [TodoItemClickType], the clicked
 * `item` and its `position`.
 */
inline fun itemListener(
    crossinline onClick: (type: TodoItemClickType, item: TodoItem, position: Int) -> Unit
) = itemListener(
    onItemClick = { item, position -> onClick(TodoItemClickType.Container, item, position) },
    onDeleteButtonClick = { item, position ->
        onClick(TodoItemClickType.DeleteButton, item, position)
    },
    onMarkAsDoneButtonClick = { item, position ->
        onClick(TodoItemClickType.MarkAsDoneButton, item, position)
    }
)
