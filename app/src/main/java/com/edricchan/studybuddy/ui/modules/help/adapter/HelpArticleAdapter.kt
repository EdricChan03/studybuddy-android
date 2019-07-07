package com.edricchan.studybuddy.ui.modules.help.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.interfaces.HelpArticle

class HelpArticleAdapter(
		private val helpArticlesList: List<HelpArticle>?
) : RecyclerView.Adapter<HelpArticleAdapter.Holder>() {
	private var listener: OnItemClickListener? = null

	var onItemClickListener: OnItemClickListener?
		get() = this.listener
		set(listener) {
			this.listener = listener
		}

	fun setOnItemClickListener(listener: (article: HelpArticle, position: Int) -> Unit) {
		onItemClickListener = object : OnItemClickListener {
			override fun onItemClick(article: HelpArticle, position: Int) {
				listener.invoke(article, position)
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
		val context = parent.context
		val inflater = LayoutInflater.from(context)
		val itemView = inflater.inflate(R.layout.helparticleadapter_item_row, parent, false)

		return Holder(itemView)
	}

	override fun onBindViewHolder(holder: Holder, position: Int) {
		val (articleDesc, _, articleTitle) = helpArticlesList!![position]
		val descTextView = holder.descTextView
		val titleTextView = holder.titleTextView

		if (articleTitle != null && articleTitle.isNotEmpty()) {
			titleTextView.text = articleTitle
		}
		if (articleDesc != null && articleDesc.isNotEmpty()) {
			descTextView.text = articleDesc
		}
	}

	override fun getItemCount(): Int {
		return helpArticlesList?.size ?: 0
		// Return 0 if the help articles don't exist
	}

	inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		internal var titleTextView: TextView = itemView.findViewById(R.id.nameTextView)
		internal var descTextView: TextView = itemView.findViewById(R.id.descTextView)

		init {
			if (adapterPosition != RecyclerView.NO_POSITION) {
				itemView.isEnabled = (!helpArticlesList!![adapterPosition].isDisabled!!)
				itemView.visibility = if (helpArticlesList[adapterPosition].isHidden!!) View.GONE else View.VISIBLE
			}
			itemView.setOnClickListener {
				if (adapterPosition != RecyclerView.NO_POSITION && listener != null) {
					listener!!.onItemClick(helpArticlesList!![adapterPosition], adapterPosition)
				}
			}
		}
	}

	/**
	 * The on item click listener
	 */
	interface OnItemClickListener {
		/**
		 * Called when an item is clicked on
		 *
		 * @param article  The article
		 * @param position The position of the item
		 */
		fun onItemClick(article: HelpArticle, position: Int)
	}
}
