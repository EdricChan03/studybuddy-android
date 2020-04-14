package com.edricchan.studybuddy.ui.modules.task.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.interfaces.TaskItem
import com.edricchan.studybuddy.ui.modules.task.adapter.itemdetails.TaskItemDetails
import com.google.android.material.card.MaterialCardView
import io.noties.markwon.Markwon

class TasksAdapter(
    val context: Context,
    val taskItemList: List<TaskItem>,
    private var itemListener: OnItemClickListener? = null
) : RecyclerView.Adapter<TasksAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.taskadapter_item_row, parent, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = taskItemList[position]

        val itemTitle = holder.itemTitle
        // TextView itemDate = holder.itemDate;
        val itemContent = holder.itemContent
        // ChipGroup itemProjects = holder.itemProjects;
        // ChipGroup itemTags = holder.itemTags;
        if (item.title != null && item.title.isNotEmpty()) {
            itemTitle.text = item.title
        } else {
            itemTitle.setText(R.string.task_adapter_empty_title)
        }
        /*if (item.dueDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
            String date = dateFormat.format(item.dueDate.toDate().getTime());
            itemDate.setText(date);
        }*/
        if (item.content != null && item.content.isNotEmpty()) {
            Markwon.create(context).setMarkdown(itemContent, item.content)
        } else {
            itemContent.setText(R.string.task_adapter_empty_content)
        }
        /*if (checkNonEmpty(item.projects)) {
            for (String project : item.projects) {
                Chip tempChip = new Chip(mContext);
                tempChip.setText(project);
                itemProjects.addView(tempChip);
            }
        }
        if (checkNonEmpty(item.tags)) {
            for (String tag : item.tags) {
                Chip tempChip = new Chip(mContext);
                tempChip.setText(tag);
                itemTags.addView(tempChip);
            }
        }*/
        holder.markAsDoneBtn.setOnClickListener {
            itemListener?.onMarkAsDoneButtonClick(item, position)
        }
        holder.deleteBtn.setOnClickListener {
            itemListener?.onDeleteButtonClick(item, position)
        }
        // TODO: Remove the following line once selection is fixed
        holder.itemView.setOnClickListener {
            itemListener?.onItemClick(item, position)
        }
    }

    override fun getItemCount(): Int {
        return taskItemList.size
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        internal var itemTitle: TextView = view.findViewById(R.id.itemTitle)
        // TextView itemDate;
        internal var itemContent: TextView = view.findViewById(R.id.itemContent)
        // ChipGroup itemProjects;
        // ChipGroup itemTags;
        internal var markAsDoneBtn: Button = view.findViewById(R.id.itemMarkAsDone)
        internal var deleteBtn: Button = view.findViewById(R.id.itemDelete)
        internal var cardView: MaterialCardView = view.findViewById(R.id.itemCardView)

        init {
            // itemDate = view.findViewById(R.id.itemDate);
            // itemProjects = view.findViewById(R.id.itemProjects);
            // itemTags = view.findViewById(R.id.itemTags);
        }

        fun getItemDetails(): TaskItemDetails {
            Log.d(TAG, "Adapter position: $adapterPosition")
            val id = taskItemList[adapterPosition].id
            Log.d(TAG, "ID at adapter position $adapterPosition: $id")
            return TaskItemDetails(adapterPosition, id)
        }
    }

    interface OnItemClickListener {
        /**
         * Called when the item view is clicked on
         *
         * @param item     The task item at this position
         * @param position The position of the adapter
         */
        @Deprecated("Use {@link androidx.recyclerview.selection.SelectionTracker.Builder#withOnItemActivatedListener(OnItemActivatedListener)}")
        fun onItemClick(item: TaskItem, position: Int)

        /**
         * Called when the delete button is clicked on
         *
         * @param item     The task item at this position
         * @param position The position of the adapter
         */
        fun onDeleteButtonClick(item: TaskItem, position: Int)

        /**
         * Called when the mark as done button is clicked on
         *
         * @param item     The task item at this position
         * @param position The position of the adapter
         */
        fun onMarkAsDoneButtonClick(item: TaskItem, position: Int)
    }
}
