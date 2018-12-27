package com.edricchan.studybuddy.adapter.itemdetailslookup;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.edricchan.studybuddy.adapter.TasksAdapter;
import com.edricchan.studybuddy.adapter.itemdetails.TaskItemDetails;

public class TaskItemLookup extends ItemDetailsLookup<String> {
	private final RecyclerView recyclerView;

	public TaskItemLookup(RecyclerView recyclerView) {
		this.recyclerView = recyclerView;
	}

	@Nullable
	@Override
	public TaskItemDetails getItemDetails(@NonNull MotionEvent e) {
		View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
		if (view != null) {
			RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
			if (viewHolder instanceof TasksAdapter.Holder) {
				return ((TasksAdapter.Holder) viewHolder).getItemDetails();
			}
		}
		return null;
	}
}
