package com.edricchan.studybuddy.ui.modules.task.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.databinding.TodosAdapterItemRowBinding
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.markwon.*
import com.edricchan.studybuddy.extensions.markwon.setMarkdown
import com.edricchan.studybuddy.interfaces.TodoItem
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
                    itemContent.setMarkdown(context.markwon {
                        usePlugins(
                            context.coilImagesPlugin,
                            linkifyPlugin,
                            context.taskListPlugin,
                            strikethroughPlugin
                        )
                    }, item.content)
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

        fun getItemDetails(): TodoItemDetails {
            Log.d(TAG, "Adapter position: $adapterPosition")
            val id = todoItemList[adapterPosition].id
            Log.d(TAG, "ID at adapter position $adapterPosition: $id")
            return TodoItemDetails(adapterPosition, id)
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
