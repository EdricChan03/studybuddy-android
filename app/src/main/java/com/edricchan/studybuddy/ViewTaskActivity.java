package com.edricchan.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.edricchan.studybuddy.interfaces.TaskItem;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

import ru.noties.markwon.Markwon;

public class ViewTaskActivity extends AppCompatActivity {
	private FirebaseAuth mAuth;
	private FirebaseFirestore mFirestore;
	private FirebaseUser mCurrentUser;
	private String TAG = SharedHelper.getTag(ViewTaskActivity.class);
	private String mTaskId;
	private TextView mTaskTitle;
	private TextView mTaskContent;
	private TextView mTaskDate;
	private TextView mTaskProject;
	private ChipGroup mTaskTags;
	private LinearLayout mTaskTagsParentView;
	private TaskItem taskItem;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_task);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Intent intent = getIntent();
		mTaskId = intent.getStringExtra("taskId");
		mTaskTitle = findViewById(R.id.taskTitle);
		mTaskContent = findViewById(R.id.taskContent);
		mTaskDate = findViewById(R.id.taskDate);
		mTaskTags = findViewById(R.id.taskTags);
		mTaskProject = findViewById(R.id.taskProject);
		mTaskTagsParentView = findViewById(R.id.taskTagsParentView);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mFirestore = FirebaseFirestore.getInstance();
		mAuth = FirebaseAuth.getInstance();
		mCurrentUser = mAuth.getCurrentUser();
		if (mCurrentUser == null) {
			Log.d(TAG, "Not logged in");
			AlertDialog signInDialog = new AlertDialog.Builder(this)
					.setTitle("Sign in")
					.setMessage("To access the content, please login or register for an account.")
					.setPositiveButton(R.string.dialog_action_login, (dialogInterface, i) -> {
						Intent loginIntent = new Intent(this, LoginActivity.class);
						startActivity(loginIntent);
						dialogInterface.dismiss();
					})
					.setNeutralButton(R.string.dialog_action_sign_up, (dialogInterface, i) -> {
						Intent registerIntent = new Intent(this, RegisterActivity.class);
						startActivity(registerIntent);
						dialogInterface.dismiss();
					})
					.setNegativeButton(R.string.dialog_action_cancel, (dialogInterface, i) -> dialogInterface.cancel())
					.create();
			signInDialog.show();

		} else {
			loadTask();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_view_task, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (taskItem != null) {
			if (taskItem.isDone()) {
				menu.findItem(R.id.action_mark_as_done)
						.setTitle(R.string.action_mark_as_undone);
			} else {
				menu.findItem(R.id.action_mark_as_done)
						.setTitle(R.string.action_mark_as_done);
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			case R.id.action_delete:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Delete todo?")
						.setPositiveButton(R.string.dialog_action_ok, (dialog, which) -> {
							mFirestore.document("users/" + mCurrentUser.getUid() + "/todos/" + mTaskId)
									.delete()
									.addOnCompleteListener(task -> {
										if (task.isSuccessful()) {
											Toast.makeText(this, "Successfully deleted todo!", Toast.LENGTH_SHORT).show();
											finish();
										} else {
											Toast.makeText(this, "An error occurred while deleting the todo. Try again later.", Toast.LENGTH_LONG).show();
											Log.e(TAG, "An error occurred while deleting the todo.", task.getException());
										}
									});
						})
						.setNegativeButton(R.string.dialog_action_cancel, (dialog, which) -> {
							dialog.dismiss();
						})
						.show();
				return true;
			case R.id.action_edit:
				Intent editTaskIntent = new Intent(ViewTaskActivity.this, NewTaskActivity.class);
				editTaskIntent.putExtra("taskId", mTaskId);
				startActivity(editTaskIntent);
				return true;
			case R.id.action_mark_as_done:
				mFirestore.document("users/" + mCurrentUser.getUid() + "/todos/" + mTaskId)
						.update("hasDone", !taskItem.isDone())
						.addOnCompleteListener(task -> {
							if (task.isSuccessful()) {
								Snackbar.make(findViewById(R.id.mainView), "Task marked as " + (!taskItem.isDone() ? "done" : "undone"), Snackbar.LENGTH_SHORT)
										.show();
							} else {
								Toast.makeText(this, "An error occurred while marking the todo as " + (!taskItem.isDone() ? "done" : "undone"), Toast.LENGTH_LONG)
										.show();
								Log.e(TAG, "An error occurred while marking the todo as " + (!taskItem.isDone() ? "done" : "undone"), task.getException());
							}
							invalidateOptionsMenu();
						});
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Loads the task that was supplied via `taskId`
	 */
	private void loadTask() {
		mFirestore.document("users/" + mCurrentUser.getUid() + "/todos/" + mTaskId)
				.addSnapshotListener((documentSnapshot, e) -> {
					if (e != null) {
						Log.e(TAG, "An error occurred while retrieving the task:", e);
						Snackbar.make(findViewById(R.id.mainView), R.string.view_task_unsuccessful_snackbar_text, Snackbar.LENGTH_LONG)
								.show();
					} else {
						if (documentSnapshot != null && documentSnapshot.exists()) {
							setViews(Objects.requireNonNull(documentSnapshot.toObject(TaskItem.class)));
							taskItem = documentSnapshot.toObject(TaskItem.class);
						}
					}
				});
				/*.addOnCompleteListener(task -> {
					if (task.isSuccessful()) {
						DocumentSnapshot document = task.getResult();
						if (document.exists()) {
							mItem = document.toObject(TaskItem.class);
							findViewById(R.id.progressBar).setVisibility(View.GONE);
							findViewById(R.id.taskView).setVisibility(View.VISIBLE);
							mTaskTitle.setText(mItem.title);
							if (mItem.content != null) {
								mTaskContent.setText(mItem.content);
							}
						} else {
							// TODO: Add handler for document not existing
						}
					} else {
						Snackbar.make(findViewById(R.id.mainView), R.string.view_task_unsuccessful_snackbar_text, Snackbar.LENGTH_INDEFINITE)
								.setBehavior(new NoSwipeBehavior())
								.setAction(R.string.view_task_unsuccessful_snackbar_btn_text, v -> loadTask());
					}
				});*/
	}

	/**
	 * Toggles the visibility of a view
	 *
	 * @param view The view to toggle the
	 */
	private void toggleViewVisibility(@NonNull View view) {
		if (view.getVisibility() == View.VISIBLE) {
			view.setVisibility(View.GONE);
		} else if (view.getVisibility() == View.GONE) {
			view.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Toggles the visiblity of a view to <code>visibility</code>
	 *
	 * @param view              The view to toggle the visibility of
	 * @param initialVisibility The visibility to check against the view. Can be {@link View#GONE}, {@link View#INVISIBLE} or {@link View#VISIBLE}
	 * @param visibility        The visibility to toggle the view to. Can be {@link View#GONE}, {@link View#INVISIBLE} or {@link View#VISIBLE}
	 */
	private void toggleViewVisibility(@NonNull View view, int initialVisibility, int visibility) {
		if (view.getVisibility() == initialVisibility) {
			view.setVisibility(visibility);
		}
	}

	/**
	 * Sets all of the {@link TextView} and {@link com.google.android.material.chip.ChipGroup} values
	 *
	 * @param item The task item
	 */
	private void setViews(TaskItem item) {
		if (item.getContent() != null) {
			Markwon.setMarkdown(mTaskContent, item.getContent());
			toggleViewVisibility(mTaskContent, View.GONE, View.VISIBLE);
		} else {
			toggleViewVisibility(mTaskContent, View.VISIBLE, View.GONE);
		}
		if (item.getDueDate() != null) {
			SimpleDateFormat format = new SimpleDateFormat(getString(R.string.date_format_pattern), Locale.ENGLISH);
			mTaskDate.setText(format.format(item.getDueDate().toDate()));
			toggleViewVisibility(mTaskDate, View.GONE, View.VISIBLE);
		} else {
			toggleViewVisibility(mTaskDate, View.VISIBLE, View.GONE);
		}
		if (item.getProject() != null) {
			item.getProject()
					.get()
					.addOnCompleteListener(task -> {
						if (task.isSuccessful()) {
							mTaskProject.setText(task.getResult().getString("name"));
						} else {
							Toast.makeText(this, "An error occurred while attempting to retrieve the project. Please try again later.", Toast.LENGTH_SHORT).show();
							Log.e(TAG, "An error occurred while attempting to retrieve the project:", task.getException());
						}
					});
			toggleViewVisibility(mTaskProject, View.GONE, View.VISIBLE);
		} else {
			toggleViewVisibility(mTaskProject, View.VISIBLE, View.GONE);
		}
		if (item.getTitle() != null) {
			mTaskTitle.setText(item.getTitle());
			toggleViewVisibility(mTaskTitle, View.GONE, View.VISIBLE);
		} else {
			toggleViewVisibility(mTaskTitle, View.VISIBLE, View.GONE);
		}
		if (item.getTags() != null && !item.getTags().isEmpty()) {
			// Remove all chips or this will cause duplicate tags
			mTaskTags.removeAllViews();
			for (String tag : item.getTags()) {
				Chip tempChip = new Chip(this);
				tempChip.setText(tag);
				mTaskTags.addView(tempChip);
			}
			toggleViewVisibility(mTaskTagsParentView, View.GONE, View.VISIBLE);
		} else {
			toggleViewVisibility(mTaskTagsParentView, View.VISIBLE, View.GONE);
		}
		toggleViewVisibility(findViewById(R.id.progressBar), View.VISIBLE, View.GONE);
		toggleViewVisibility(findViewById(R.id.scrollTaskView), View.GONE, View.VISIBLE);
	}
}
