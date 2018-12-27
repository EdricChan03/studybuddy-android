package com.edricchan.studybuddy.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.OnItemActivatedListener;
import androidx.recyclerview.widget.RecyclerView;

import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.SharedHelper;
import com.edricchan.studybuddy.adapter.itemdetails.TaskItemDetails;
import com.edricchan.studybuddy.interfaces.TaskItem;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import ru.noties.markwon.Markwon;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.Holder> {
	private List<TaskItem> mTaskItemList;
	private Context mContext;
	private FirebaseUser mUser;
	private FirebaseFirestore mFirestore;
	private View mSnackbarView;
	private static final String TAG = SharedHelper.getTag(TasksAdapter.class);
	private int lastPosition = 1;
	private OnItemClickListener mItemListener;
	private boolean mUseDeprecatedListener;

	/**
	 * Initialises a new adapter and uses the new listener
	 *
	 * @param context      The context
	 * @param taskItemList The list of items to show
	 */
	public TasksAdapter(Context context, List<TaskItem> taskItemList) {
		this(context, taskItemList, false);
	}

	/**
	 * Initialises a new adapter
	 *
	 * @param context               The context
	 * @param taskItemList          The list of items to show
	 * @param useDeprecatedListener Whether to use the deprecated listener methods
	 * @throws IllegalStateException If <code>useDeprecatedListener</code> is set to <code>true</code>, this throws as such operations are unsupported.
	 */
	public TasksAdapter(Context context, List<TaskItem> taskItemList, boolean useDeprecatedListener) throws IllegalStateException {
		mTaskItemList = taskItemList;
		mContext = context;
		if (useDeprecatedListener) {
			throw new IllegalStateException("Please use the deprecated method (TasksAdapter#TasksAdapter(Context, List, FirebaseUser, FirebaseFirestore, View)!");
		}
		mUseDeprecatedListener = useDeprecatedListener;
	}

	/**
	 * Initialises a new adapter
	 *
	 * @param context      The context
	 * @param taskItemList The list of items to show
	 * @param user         An instance of the currently logged-in user. (This can be retrieved with {@link FirebaseAuth#getCurrentUser()})
	 * @param fs           An instance of {@link FirebaseFirestore}. (This can be retrieved with {@link FirebaseFirestore#getInstance()})
	 * @param snackbarView The view to show a snackbar in
	 * @deprecated Use {@link TasksAdapter#TasksAdapter(Context, List)}
	 */
	@Deprecated
	public TasksAdapter(Context context, List<TaskItem> taskItemList, FirebaseUser user, FirebaseFirestore fs, View snackbarView) {
		mTaskItemList = taskItemList;
		mContext = context;
		mUser = user;
		mFirestore = fs;
		mSnackbarView = snackbarView;
	}

	/**
	 * Checks if a string is non-empty
	 *
	 * @param var The string to check
	 * @return True if it is empty, false otherwise
	 * @deprecated Use {@link TextUtils#isEmpty(CharSequence)}
	 */
	@Deprecated
	private boolean checkNonEmpty(@NonNull String var) {
		return var.length() != 0;
	}

	/**
	 * Checks if a string is not null
	 *
	 * @param var The string to check
	 * @return True if it is not null, false otherwise
	 * @deprecated Use {@link TextUtils#isEmpty(CharSequence)}
	 */
	@Deprecated
	private boolean checkStringNonNull(String var) {
		return var != null;
	}

	/**
	 * Checks if a list is not empty
	 *
	 * @param var The list to check
	 * @return True if is not empty, false otherwise
	 * @implNote Note that this method doesn't check if the list is null.
	 * @deprecated Use {@link List#isEmpty()}
	 */
	@Deprecated
	private boolean checkNonEmpty(@NonNull List var) {
		return var.size() != 0;
	}

	/**
	 * Updates the done status
	 *
	 * @param item     The item
	 * @param button   The button to update the text
	 * @param position The adapter position
	 * @deprecated This should ideally be left up to the consumer, instead of handling it in the adapter
	 */
	@Deprecated
	private void updateDoneStatus(@NonNull TaskItem item, final Button button, int position) {
		if (item.isDone()) {
			mFirestore.document("users/" + mUser.getUid() + "/todos/" + item.getId()).update("hasDone", false).addOnSuccessListener(aVoid -> {
				Snackbar.make(mSnackbarView, "Task marked as undone.", Snackbar.LENGTH_LONG).show();
				notifyItemChanged(position);
				button.setText(R.string.action_mark_as_done);
			});
		} else {
			mFirestore.document("users/" + mUser.getUid() + "/todos/" + item.getId()).update("hasDone", true).addOnSuccessListener(aVoid -> {
				Snackbar.make(mSnackbarView, "Task marked as done.", Snackbar.LENGTH_LONG).show();
				notifyItemChanged(position);
				button.setText(R.string.action_mark_as_undone);
			});
		}
	}

	/**
	 * Sets the animation of the item view
	 *
	 * @param viewToAnimate The view to animate
	 * @param position      The adapter position
	 * @deprecated This doesn't really work.
	 */
	@Deprecated
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
		final TaskItem item = mTaskItemList.get(position);

		TextView itemTitle = holder.itemTitle;
		// TextView itemDate = holder.itemDate;
		TextView itemContent = holder.itemContent;
		// ChipGroup itemProjects = holder.itemProjects;
		// ChipGroup itemTags = holder.itemTags;
		if (!TextUtils.isEmpty(item.getTitle())) {
			itemTitle.setText(item.getTitle());
		} else {
			itemTitle.setText(R.string.task_adapter_empty_title);
		}
		/*if (item.dueDate != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
			String date = dateFormat.format(item.dueDate.toDate().getTime());
			itemDate.setText(date);
		}*/
		if (!TextUtils.isEmpty(item.getContent())) {
			Markwon.setMarkdown(itemContent, item.getContent());
		} else {
			itemContent.setText(R.string.task_adapter_empty_content);
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
		final Button markAsDoneBtn = holder.markAsDoneBtn;
		markAsDoneBtn.setText(R.string.action_mark_as_done);
		markAsDoneBtn.setOnClickListener(view -> {
			if (mItemListener != null) {
				if (mUseDeprecatedListener) {
					updateDoneStatus(item, markAsDoneBtn, position);
				} else {
					mItemListener.onMarkAsDoneButtonClick(item, position);
				}
			} else {
				if (!mUseDeprecatedListener) {
					Log.w(TAG, "Please supply a listener for the mark as done button!");
				} else {
					updateDoneStatus(item, markAsDoneBtn, position);
				}
			}
		});
		Button deleteBtn = holder.deleteBtn;
		deleteBtn.setOnClickListener(view -> {
			if (mItemListener != null) {
				if (mUseDeprecatedListener) {
					deleteTask(position);
				} else {
					mItemListener.onDeleteButtonClick(item, position);
				}
			} else {
				if (!mUseDeprecatedListener) {
					Log.w(TAG, "Please supply a listener for the delete button!");
				} else {
					if (mContext != null) {
						MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext);
						builder.setTitle("Delete todo?");
						builder.setPositiveButton(R.string.dialog_action_ok, (dialogInterface, i) -> {
							dialogInterface.dismiss();
							deleteTask(item.getId(), position);
						});
						builder.setNegativeButton(R.string.dialog_action_cancel, (dialogInterface, i) -> dialogInterface.dismiss());
						builder.show();
					} else {
						Log.w(TAG, "Please supply a context for the dialog!");
					}
				}
			}
		});
		if (mUseDeprecatedListener) {
			setAnimation(holder.itemView, position);
		}
	}

	/**
	 * Deletes a task at a specific position
	 *
	 * @param position The adapter position
	 * @deprecated This should ideally be left up to the consumer, instead of handling it in the adapter
	 */
	@Deprecated
	public void deleteTask(int position) {
		deleteTask(mTaskItemList.get(position).getId(), position);
	}

	/**
	 * Deletes a task at a specific position
	 *
	 * @param taskId   The document ID
	 * @param position The adapter position
	 * @deprecated This should ideally be left up to the consumer, instead of handling it in the adapter
	 */
	@Deprecated
	public void deleteTask(String taskId, int position) {
		TaskItem item = mTaskItemList.get(position);
		SharedHelper.removeTask(taskId, mUser, mFirestore).addOnSuccessListener(aVoid -> {
			notifyItemRemoved(position);
			Snackbar.make(mSnackbarView, "Todo was deleted", Snackbar.LENGTH_LONG).setAction("Undo", view1 -> SharedHelper.addTask(item, mUser, mFirestore).addOnFailureListener(e -> Snackbar.make(mSnackbarView, "Couldn't restore todo: " + e.getMessage(), Snackbar.LENGTH_LONG).show())).show();
		});
	}

	@Override
	public int getItemCount() {
		return mTaskItemList.size();
	}

	public class Holder extends RecyclerView.ViewHolder {
		TextView itemTitle;
		// TextView itemDate;
		TextView itemContent;
		// ChipGroup itemProjects;
		// ChipGroup itemTags;
		Button markAsDoneBtn;
		Button deleteBtn;
		MaterialCardView cardView;
		TaskItemDetails itemDetail;

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
			try {
				itemDetail = new TaskItemDetails(getAdapterPosition(), mTaskItemList.get(getAdapterPosition()).getId());
			} catch (IndexOutOfBoundsException e) {
				Log.e(TAG, "An error occurred while attempting to set the item details:", e);
			}
		}

		/**
		 * Retrieves the item details of the item
		 *
		 * @return The item details of the item
		 */
		public TaskItemDetails getItemDetails() {
			return itemDetail;
		}
	}

	public interface OnItemClickListener {
		/**
		 * Called when the item view is clicked on
		 *
		 * @param item     The task item at this position
		 * @param position The position of the adapter
		 * @deprecated Use {@link androidx.recyclerview.selection.SelectionTracker.Builder#withOnItemActivatedListener(OnItemActivatedListener)}
		 */
		@Deprecated
		void onItemClick(TaskItem item, int position);

		/**
		 * Called when the delete button is clicked on
		 *
		 * @param item     The task item at this position
		 * @param position The position of the adapter
		 */
		void onDeleteButtonClick(TaskItem item, int position);

		/**
		 * Called when the mark as done button is clicked on
		 *
		 * @param item     The task item at this position
		 * @param position The position of the adapter
		 */
		void onMarkAsDoneButtonClick(TaskItem item, int position);
	}

	/**
	 * Sets the on click listener for views in the holder
	 *
	 * @param listener The on click listener
	 */
	public void setOnItemClickListener(OnItemClickListener listener) {
		this.mItemListener = listener;
	}
}