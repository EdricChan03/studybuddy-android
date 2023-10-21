package com.edricchan.studybuddy.ui.modules.task.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.databinding.TodosAdapterItemRowBinding
import com.edricchan.studybuddy.extensions.markwon.coilImagesPlugin
import com.edricchan.studybuddy.extensions.markwon.linkifyPlugin
import com.edricchan.studybuddy.extensions.markwon.setMarkdown
import com.edricchan.studybuddy.extensions.markwon.strikethroughPlugin
import com.edricchan.studybuddy.extensions.markwon.taskListPlugin
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.ui.modules.task.adapter.itemdetails.TodoItemDetails
import com.edricchan.studybuddy.utils.recyclerview.diffCallback

class TodosAdapter(
    private val context: Context,
    private var itemListener: OnItemClickListener? = null
) : ListAdapter<TodoItem, TodosAdapter.Holder>(DIFF_CALLBACK) {
    @Deprecated(
        "This method is kept for backwards-compatibility - use the overload " +
            "that takes 2 arguments.",
        ReplaceWith(
            "TodosAdapter(context = context, itemListener = itemListener)"
        )
    )
    constructor(
        context: Context,
        todoItemList: List<TodoItem>,
        itemListener: OnItemClickListener?
    ) : this(
        context, itemListener
    ) {
        submitList(todoItemList)
    }

    companion object {
        val DIFF_CALLBACK = diffCallback<TodoItem>(
            areItemsTheSame = { old, new -> old.id == new.id },
            areContentsTheSame = { old, new -> old == new }
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
        TodosAdapterItemRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }

    inner class Holder(private val binding: TodosAdapterItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TodoItem) {
            binding.apply {
                if (!item.title.isNullOrEmpty()) itemTitle.text = item.title
                else itemTitle.setText(R.string.task_adapter_empty_title)
                if (!item.content.isNullOrEmpty()) {
                    // FIXME: Remove not-null assertion
                    itemContent.setMarkdown(item.content!!) {
                        usePlugins(
                            context.coilImagesPlugin,
                            linkifyPlugin,
                            context.taskListPlugin,
                            strikethroughPlugin
                        )
                    }
                } else itemContent.setText(R.string.task_adapter_empty_content)

                itemMarkAsDone.setOnClickListener {
                    itemListener?.onMarkAsDoneButtonClick(item, bindingAdapterPosition)
                }
                itemDelete.setOnClickListener {
                    itemListener?.onDeleteButtonClick(item, bindingAdapterPosition)
                }
                itemCardView.setOnClickListener {
                    itemListener?.onItemClick(item, bindingAdapterPosition)
                }
            }
        }

        val itemDetails
            get() = bindingAdapterPosition.let { position ->
                TodoItemDetails(position, getItem(position).id)
            }
    }

    interface OnItemClickListener {
        /**
         * Called when the item view is clicked on
         *
         * @param item     The task item at this position
         * @param position The position of the adapter
         */
        fun onItemClick(item: TodoItem, position: Int)

        /**
         * Called when the delete button is clicked on
         *
         * @param item     The task item at this position
         * @param position The position of the adapter
         */
        fun onDeleteButtonClick(item: TodoItem, position: Int)

        /**
         * Called when the mark as done button is clicked on
         *
         * @param item     The task item at this position
         * @param position The position of the adapter
         */
        fun onMarkAsDoneButtonClick(item: TodoItem, position: Int)
    }
}
