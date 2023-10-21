package com.edricchan.studybuddy.ui.modules.task.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
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

class TodosAdapter(
    private val context: Context,
    private val todoItemList: List<TodoItem>,
    private var itemListener: OnItemClickListener? = null
) : RecyclerView.Adapter<TodosAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
        TodosAdapterItemRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = todoItemList[position]

        holder.bind(item)
    }

    override fun getItemCount() = todoItemList.size

    fun getItem(position: Int) = todoItemList[position]

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
                TodoItemDetails(position, todoItemList[position].id)
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
