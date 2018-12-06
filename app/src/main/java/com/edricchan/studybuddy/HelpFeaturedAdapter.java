package com.edricchan.studybuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.edricchan.studybuddy.interfaces.HelpArticle;

import java.util.List;

/**
 * Created by edricchan on 8/3/18.
 */

public class HelpFeaturedAdapter extends ArrayAdapter<HelpArticle> {
	private List<HelpArticle> helpArticleList;
	private Context mContext;

	// View lookup cache
	private static class ViewHolder {
		TextView textView;
	}

	public HelpFeaturedAdapter(List<HelpArticle> data, Context context) {
		super(context, R.layout.help_featured_row_item, data);
		this.helpArticleList = data;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return helpArticleList.size();
	}

	@Override
	public HelpArticle getItem(int position) {
		return helpArticleList.get(position);
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {

		ViewHolder viewHolder;
		final View result;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_featured_row_item, parent, false);
			viewHolder.textView = convertView.findViewById(R.id.textView);

			result = convertView;
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			result = convertView;
		}

		HelpArticle item = getItem(position);


		assert item != null;
		viewHolder.textView.setText(item.getArticleTitle());

		return result;
	}

}
