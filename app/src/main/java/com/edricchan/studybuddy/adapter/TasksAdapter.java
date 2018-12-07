package com.edricchan.studybuddy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.SharedHelper;
import com.edricchan.studybuddy.interfaces.TaskItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import ru.noties.markwon.Markwon;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.Holder> {
	private List<TaskItem> mFeedItemList;
	private Context mContext;
	private FirebaseUser mUser;
	private FirebaseFirestore mFirestore;
	private View snackbarView;
	private static final String TAG = SharedHelper.getTag(TasksAdapter.class);
	private int lastPosition = 1;
	private OnItemClickListener mListener;

	public TasksAdapter(Context context, List<TaskItem> feedItemList, FirebaseUser user, FirebaseFirestore fs, View view) {
		mFeedItemList = feedItemList;
		mContext = context;
		mUser = user;
		mFirestore = fs;
		snackbarView = view;
	}

	private boolean checkNonEmpty(@NonNull String var) {
		return var.length() != 0;
	}

	private boolean checkStringNonNull(String var) {
		return var != null;
	}

	private boolean checkNonEmpty(@NonNull List var) {
		return var.size() != 0;
	}

	private void updateDoneStatus(@NonNull TaskItem item, final MaterialButton button, int position) {
		if (item.isDone()) {
			mFirestore.document("users/" + mUser.getUid() + "/todos/" + item.getId()).update("hasDone", false).addOnSuccessListener(aVoid -> {
				Snackbar.make(snackbarView, "Task marked as undone.", Snackbar.LENGTH_LONG).show();
				notifyItemChanged(position);
				button.setText(R.string.action_mark_as_done);
			});
		} else {
			mFirestore.document("users/" + mUser.getUid() + "/todos/" + item.getId()).update("hasDone", true).addOnSuccessListener(aVoid -> {
				Snackbar.make(snackbarView, "Task marked as done.", Snackbar.LENGTH_LONG).show();
				notifyItemChanged(position);
				button.setText(R.string.action_mark_as_undone);
			});
		}
	}

	private void setAnimation(View viewToAnimate, int position) {
		// If the bound view wasn't previously displayed on screen, it's animated
		if (position > lastPosition) {
			ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			anim.setDuration(250);
			viewToAnimate.startAnimation(anim);
			lastPosition = position;
		}
	}

	@Override
	@NonNull
	public TasksAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		View itemView = inflater.inflate(R.layout.taskadapter_item_row, parent, false);

		return new Holder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull TasksAdapter.Holder holder, final int position) {
		final TaskItem item = mFeedItemList.get(position);
		TextView itemTitle = holder.itemTitle;
		// TextView itemDate = holder.itemDate;
		TextView itemContent = holder.itemContent;
		// ChipGroup itemProjects = holder.itemProjects;
		// ChipGroup itemTags = holder.itemTags;
		if (checkNonEmpty(item.getTitle())) {
			itemTitle.setText(item.getTitle());
		}
		/*if (item.dueDate != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
			String date = dateFormat.format(item.dueDate.toDate().getTime());
			itemDate.setText(date);
		}*/
		if (checkStringNonNull(item.getContent())) {
			Markwon.setMarkdown(itemContent, item.getContent());
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
		final MaterialButton markAsDoneBtn = holder.markAsDoneBtn;
		markAsDoneBtn.setText(R.string.action_mark_as_done);
		markAsDoneBtn.setOnClickListener(view -> updateDoneStatus(item, markAsDoneBtn, position));
		MaterialButton deleteBtn = holder.deleteBtn;
		deleteBtn.setOnClickListener(view -> {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle("Delete todo?");
			builder.setPositiveButton(R.string.dialog_action_ok, (dialogInterface, i) -> {
				dialogInterface.dismiss();
				deleteTask(item.getId(), position);
			});
			builder.setNegativeButton(R.string.dialog_action_cancel, (dialogInterface, i) -> dialogInterface.dismiss());
			builder.show();
		});
		setAnimation(holder.itemView, position);
	}

	public void deleteTask(int position) {
		deleteTask(mFeedItemList.get(position).getId(), position);
	}

	public void deleteTask(String taskId, int position) {
		TaskItem item = mFeedItemList.get(position);
		SharedHelper.removeTask(taskId, mUser, mFirestore).addOnSuccessListener(aVoid -> {
			notifyItemRemoved(position);
			Snackbar.make(snackbarView, "Todo was deleted", Snackbar.LENGTH_LONG).setAction("Undo", view1 -> SharedHelper.addTask(item, mUser, mFirestore).addOnFailureListener(e -> Snackbar.make(snackbarView, "Couldn't restore todo: " + e.getMessage(), Snackbar.LENGTH_LONG).show())).show();
		});
	}

	@Override
	public int getItemCount() {
		return mFeedItemList.size();
	}

	public class Holder extends RecyclerView.ViewHolder {
		TextView itemTitle;
		// TextView itemDate;
		TextView itemContent;
		// ChipGroup itemProjects;
		// ChipGroup itemTags;
		MaterialButton markAsDoneBtn;
		MaterialButton deleteBtn;
		MaterialCardView cardView;

		public Holder(View view) {
			super(view);
			cardView = view.findViewById(R.id.itemCardView);
			markAsDoneBtn = view.findViewById(R.id.itemMarkAsDone);
			deleteBtn = view.findViewById(R.id.itemDelete);
			itemTitle = view.findViewById(R.id.itemTitle);
			// itemDate = view.findViewById(R.id.itemDate);
			itemContent = view.findViewById(R.id.itemContent);
			// itemProjects = view.findViewById(R.id.itemProjects);
			// itemTags = view.findViewById(R.id.itemTags);
			view.setOnClickListener(v -> {
				int position = getAdapterPosition();
				if (position != RecyclerView.NO_POSITION && mListener != null) {
					mListener.onItemClick(mFirestore.document("users/" + mUser.getUid() + "/todos/" + mFeedItemList.get(position).getId()), position);
				}
			});
		}
	}

	public interface OnItemClickListener {
		void onItemClick(DocumentReference document, int position);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.mListener = listener;
	}
}