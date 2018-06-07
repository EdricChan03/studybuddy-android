package com.edricchan.studybuddy;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edricchan.studybuddy.interfaces.HelpFeatured;

import java.util.List;

/**
 * Created by edricchan on 8/3/18.
 */

public class HelpFeaturedAdapter extends RecyclerView.Adapter<HelpFeaturedAdapter.HelpFeaturedViewHolder> {
	private List<HelpFeatured> helpFeaturedList;

	HelpFeaturedAdapter(List<HelpFeatured> myList) {
		this.helpFeaturedList = myList;
	}

	@Override
	public int getItemCount() {
		return helpFeaturedList.size();
	}

	@Override
	public HelpFeaturedAdapter.HelpFeaturedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.help_featured_row_item, parent, false);
		HelpFeaturedViewHolder helpFeaturedViewHolder = new HelpFeaturedViewHolder(view);
		return helpFeaturedViewHolder;
	}

	@Override
	public void onBindViewHolder(@NonNull HelpFeaturedViewHolder holder, int position) {
		holder.textView.setText(helpFeaturedList.get(position).getHelpTxt());
	}

	public static class HelpFeaturedViewHolder extends RecyclerView.ViewHolder {
		public TextView textView;

		HelpFeaturedViewHolder(View itemView) {
			super(itemView);
			textView = itemView.findViewById(R.id.help_featured_textview);
		}
	}
}
