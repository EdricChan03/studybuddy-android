package com.edricchan.studybuddy.ui.modules.task.gtasks.adapter

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.toDate
import com.google.api.services.tasks.model.TaskList
import com.google.api.services.tasks.model.TaskLists

//class TaskListsAdapter(context: Context, taskLists: List<TaskList>) : ArrayAdapter<TaskList>(
//		context, 0, taskLists) {
//	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//		val itemView: View
//		if (convertView == null) {
//			itemView = LayoutInflater.from(context).inflate(R.layout.item_gtasklist, parent, false)
//		} else {
//			itemView = convertView
//		}
//
//		// Get the data item for this position
//		val item = getItem(position)
//		itemView.findViewById<TextView>(R.id.nameTextView).text = item?.title
//
//		// Return the completed view to render on screen
//		return itemView
//	}
//}
class TaskListsAdapter(
		private val context: Context,
		var taskLists: List<TaskList>,
		private val onItemClickListener: ((item: TaskList) -> Unit)? = null
) : RecyclerView.Adapter<TaskListsAdapter.ViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val context = parent.context
		val inflater = LayoutInflater.from(context)
		val itemView = inflater.inflate(R.layout.item_gtasklist, parent, false)
		return ViewHolder(itemView)
	}

	override fun getItemCount(): Int = taskLists.size
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = taskLists[position]

		val itemNameTextView = holder.itemNameTextView
		val itemDescTextView = holder.itemDescTextView

		itemNameTextView.text = item.title
		itemDescTextView.text = context.getString(R.string.import_gtasks_adapter_desc_textview_text,
				getRelativeDateTimeString(item.updated.value))
		holder.itemView.setOnClickListener {
			onItemClickListener?.invoke(item)
		}
	}

	private fun getRelativeDateTimeString(longDate: Long): CharSequence = DateUtils.getRelativeDateTimeString(
			context,
			longDate.toDate().time,
			DateUtils.MINUTE_IN_MILLIS,
			DateUtils.WEEK_IN_MILLIS,
			0
	)

	/**
	 * Sets the task lists from the [TaskLists] class.
	 * @param argTaskLists The task lists to set.
	 */
	fun setTaskLists(argTaskLists: TaskLists) {
		taskLists = argTaskLists.items
	}


	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		// Note: This variable is currently unused for now.
		internal val itemIconImageView: ImageView = view.findViewById(R.id.iconImageView)
		internal val itemNameTextView: TextView = view.findViewById(R.id.nameTextView)
		internal val itemDescTextView: TextView = view.findViewById(R.id.descTextView)
	}
}