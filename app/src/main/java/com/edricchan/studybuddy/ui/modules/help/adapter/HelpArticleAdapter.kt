package com.edricchan.studybuddy.ui.modules.help.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.interfaces.HelpArticle

class HelpArticleAdapter(
		private val mHelpArticles: List<HelpArticle>?
) : RecyclerView.Adapter<HelpArticleAdapter.Holder>() {
	private var mListener: OnItemClickListener? = null

	var onItemClickListener: OnItemClickListener?
		get() = this.mListener
		set(listener) {
			this.mListener = listener
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

		val (articleDesc, _, articleTitle) = mHelpArticles!![position]
	override fun onBindViewHolder(holder: Holder, position: Int) {
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
		return mHelpArticles?.size ?: 0
		// Return 0 if the help articles don't exist
	}

	inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		internal var articleImageView: ImageView
		internal var descTextView: TextView
		internal var titleTextView: TextView

		init {
			if (adapterPosition != RecyclerView.NO_POSITION) {
				itemView.isEnabled = (!mHelpArticles!![adapterPosition].isDisabled!!)
				itemView.visibility = if (mHelpArticles[adapterPosition].isHidden!!) View.GONE else View.VISIBLE
			}
			articleImageView = itemView.findViewById(R.id.articleImageView)
			descTextView = itemView.findViewById(R.id.descTextView)
			titleTextView = itemView.findViewById(R.id.titleTextView)
			itemView.setOnClickListener {
				if (adapterPosition != RecyclerView.NO_POSITION && mListener != null) {
					mListener!!.onItemClick(mHelpArticles!![adapterPosition], adapterPosition)
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
