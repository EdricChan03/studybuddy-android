package com.edricchan.studybuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.edricchan.studybuddy.interfaces.HelpFeatured;

import java.util.ArrayList;

import androidx.annotation.NonNull;

/**
 * Created by edricchan on 8/3/18.
 */

public class HelpFeaturedAdapter extends ArrayAdapter<HelpFeatured> {
	private ArrayList<HelpFeatured> helpFeaturedList;
	private Context mContext;

	// View lookup cache
	private static class ViewHolder {
		TextView textView;
	}
	public HelpFeaturedAdapter(ArrayList<HelpFeatured> data, Context context) {
		super(context, R.layout.help_featured_row_item, data);
		this.helpFeaturedList = data;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return helpFeaturedList.size();
	}

	@Override
	public HelpFeatured getItem(int position) {
		return helpFeaturedList.get(position);
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

			result=convertView;
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			result=convertView;
		}

		HelpFeatured item = getItem(position);


		assert item != null;
		viewHolder.textView.setText(item.helpTxt);

		return result;
	}

}
