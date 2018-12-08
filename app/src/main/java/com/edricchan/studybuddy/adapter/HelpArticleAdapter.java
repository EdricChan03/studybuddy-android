package com.edricchan.studybuddy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.SharedHelper;
import com.edricchan.studybuddy.interfaces.HelpArticle;

import java.util.List;

public class HelpArticleAdapter extends RecyclerView.Adapter<HelpArticleAdapter.Holder> {
	private List<HelpArticle> mHelpArticles;
	private OnItemClickListener mListener;
	private static final String TAG = SharedHelper.getTag(HelpArticleAdapter.class);

	public HelpArticleAdapter(List<HelpArticle> helpArticles) {
		this.mHelpArticles = helpArticles;
	}
	@NonNull
	@Override
	public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		View itemView = inflater.inflate(R.layout.helparticleadapter_item_row, parent, false);

		return new Holder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull HelpArticleAdapter.Holder holder, int position) {
		final HelpArticle article = mHelpArticles.get(position);
		TextView descTextView = holder.descTextView;
		TextView titleTextView = holder.titleTextView;

		if (!article.getArticleTitle().isEmpty()) {
			titleTextView.setText(article.getArticleTitle());
		}
		if (!article.getArticleDesc().isEmpty()) {
			descTextView.setText(article.getArticleDesc());
		}
	}

	@Override
	public int getItemCount() {
		if (mHelpArticles != null) {
			return mHelpArticles.size();
		}
		// Return 0 if the help articles don't exist
		return 0;
	}

	public class Holder extends RecyclerView.ViewHolder {

		ImageView articleImageView;
		TextView descTextView;
		TextView titleTextView;

		public Holder(@NonNull View itemView) {
			super(itemView);
			articleImageView = itemView.findViewById(R.id.articleImageView);
			descTextView = itemView.findViewById(R.id.descTextView);
			titleTextView = itemView.findViewById(R.id.titleTextView);
			itemView.setOnClickListener(v -> {
				mListener.onItemClick(mHelpArticles.get(getAdapterPosition()), getAdapterPosition());
			});
		}
	}

	/**
	 * The on item click listener
	 */
	public interface OnItemClickListener {
		/**
		 * Called when an item is clicked on
		 *
		 * @param article  The article
		 * @param position The position of the item
		 */
		void onItemClick(HelpArticle article, int position);
	}

	/**
	 * Retrieves the current on click listener assigned to the view
	 *
	 * @returns The current on click listener
	 */
	public OnItemClickListener getOnItemClickListener() {
		if (this.mListener != null) {
			return this.mListener;
		}
		return null;
	}

	/**
	 * Sets the on click listener assigned to the view
	 *
	 * @param listener The on click listener
	 */
	public void setOnItemClickListener(OnItemClickListener listener) {
		this.mListener = listener;
	}
}
