package com.edricchan.studybuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TooltipCompat;
import androidx.recyclerview.widget.RecyclerView;


/***
 * Part of code came from the following sources:
 * <a href="https://guides.codepath.com/android/using-the-recyclerview">Using the recyclerview</a>
 *
 */
public class StudyAdapter extends RecyclerView.Adapter<StudyAdapter.Holder> {
	private List<TaskItem> mFeedItemList;
	private Context mContext;

	public StudyAdapter(Context context, List<TaskItem> feedItemList) {
		mFeedItemList = feedItemList;
		mContext = context;
	}

	private Context getContext() {
		return mContext;
	}

	@Override
	@NonNull
	public StudyAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		View itemView = inflater.inflate(R.layout.recyclerview_item_row, parent, false);

		return new Holder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull StudyAdapter.Holder holder, final int position) {
		TaskItem item = mFeedItemList.get(position);
		TextView itemTitle = holder.itemTitle;
		TextView itemDate = holder.itemDate;
		TextView itemContent = holder.itemContent;
		ChipGroup itemProject = holder.itemProject;
		itemTitle.setText(item.title);
		itemDate.setText(item.dueDate.toString());
		itemContent.setText(item.content);
		for (String project : item.project) {
			Chip tempChip = new Chip(getContext());
			tempChip.setChipText(project);
			itemProject.addView(tempChip);
		}
		MaterialButton markAsDoneBtn = (MaterialButton) holder.markAsDoneBtn;
		TooltipCompat.setTooltipText(markAsDoneBtn, "Mark as done");
		markAsDoneBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				notifyItemRemoved(position);
				mFeedItemList.remove(position);
				Snackbar.make(view, "Task marked as done.", 6000).show();
			}
		});
	}

	@Override
	public int getItemCount() {
		return mFeedItemList.size();
	}

	public class Holder extends RecyclerView.ViewHolder {
		TextView itemTitle;
		TextView itemDate;
		TextView itemContent;
		ChipGroup itemProject;
		MaterialButton markAsDoneBtn;

		public Holder(View view) {
			super(view);
			markAsDoneBtn = (MaterialButton) view.findViewById(R.id.itemMarkAsDone);
			itemTitle = (TextView) view.findViewById(R.id.itemDesc);
			itemDate = (TextView) view.findViewById(R.id.itemDate);
			itemContent = (TextView) view.findViewById(R.id.itemTitle);
			itemProject = (ChipGroup) view.findViewById(R.id.itemProject);
		}
	}
}