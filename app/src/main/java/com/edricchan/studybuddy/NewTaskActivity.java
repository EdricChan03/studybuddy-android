package com.edricchan.studybuddy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;

import com.edricchan.studybuddy.adapter.TaskProjectSpinnerAdapter;
import com.edricchan.studybuddy.interfaces.TaskItem;
import com.edricchan.studybuddy.interfaces.TaskProject;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by edricchan on 8/3/18.
 */

public class NewTaskActivity extends AppCompatActivity {
	static final String ACTION_NEW_TASK_FROM_SHORTCUT = "com.edricchan.studybuddy.ACTION_NEW_TASK_FROM_SHORTCUT";
	private TextInputLayout mTaskTitle, mTaskContent, mTaskTags;
	private Spinner mTaskProjectSpinner;
	private CheckBox mTaskIsDone;
	private Date mTaskDate;
	private FirebaseAuth mAuth;
	private FirebaseFirestore mFirestore;
	private FirebaseUser mCurrentUser;
	private boolean mAllowAccess;
	private String TAG = SharedHelper.Companion.getTag(NewTaskActivity.class);
	private String tempTaskProject;
	private TaskProjectSpinnerAdapter mTaskProjectSpinnerAdapter;
	/**
	 * Whether the intent is to edit a task
	 */
	private boolean mIsEditing;

	private boolean checkSignedIn() {
		return mAuth.getCurrentUser() != null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivty_new_task);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mAuth = FirebaseAuth.getInstance();
		mFirestore = FirebaseFirestore.getInstance();
		mIsEditing = getIntent().getStringExtra("taskId") != null;
		mCurrentUser = mAuth.getCurrentUser();
		if (!checkSignedIn()) {
			Toast.makeText(this, "Please sign in before continuing", Toast.LENGTH_SHORT).show();
			Intent signInIntent = new Intent(NewTaskActivity.this, LoginActivity.class);
			startActivity(signInIntent);
			mAllowAccess = false;
		} else {
			mAllowAccess = true;
		}
		mTaskTitle = findViewById(R.id.taskTitle);
		mTaskProjectSpinner = findViewById(R.id.taskProject);
		mTaskTags = findViewById(R.id.taskTags);
		mTaskContent = findViewById(R.id.taskContent);
		mTaskIsDone = findViewById(R.id.taskIsDone);
		ImageButton mDialogDate = findViewById(R.id.taskDatePickerBtn);
		final TextView mDialogDateText = findViewById(R.id.taskDatePickerResult);
		TooltipCompat.setTooltipText(mDialogDate, "Open datepicker dialog");
		mDialogDate.setOnClickListener(view -> {
			final Calendar c = Calendar.getInstance();
			DatePickerDialog dpd = new DatePickerDialog(NewTaskActivity.this,
					(datePicker, year, monthOfYear, dayOfMonth) -> {
						SimpleDateFormat format = new SimpleDateFormat(getString(R.string.date_format_pattern), Locale.ENGLISH);
						Date datepickerDate = new Date(year, monthOfYear, dayOfMonth);
						mDialogDateText.setText(format.format(datepickerDate));
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
			dpd.getDatePicker().setMinDate(c.getTimeInMillis());
			dpd.show();
			mTaskDate = SharedHelper.Companion.getDateFromDatePicker(dpd.getDatePicker());
		});

		ArrayList<TaskProject> projectArrayList = new ArrayList<>();
		mTaskProjectSpinnerAdapter = new TaskProjectSpinnerAdapter(this, android.R.layout.simple_spinner_item, projectArrayList);
		mTaskProjectSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mTaskProjectSpinner.setAdapter(mTaskProjectSpinnerAdapter);
		mFirestore.collection("users/" + mCurrentUser.getUid() + "/todoProjects")
				.addSnapshotListener((documentSnapshots, e) -> {
					if (e != null) {
						Log.e(TAG, "An error occurred while listening to changes:", e);
						return;
					}
					projectArrayList.clear();
					TaskProject.Builder createProjectBuilder = new TaskProject.Builder();
					createProjectBuilder
							.setColor("#FFFFFF")
							.setId("PLUS")
							.setName(getString(R.string.task_project_create));
					projectArrayList.add(createProjectBuilder.create());
					for (DocumentSnapshot document : documentSnapshots) {
						projectArrayList.add(document.toObject(TaskProject.class));
					}
					TaskProject.Builder chooseProjectBuilder = new TaskProject.Builder();
					chooseProjectBuilder
							.setId("CHOOSE")
							.setName(getString(R.string.task_project_prompt));
					projectArrayList.add(chooseProjectBuilder.create());
					mTaskProjectSpinnerAdapter.notifyDataSetChanged();
				});
		mTaskProjectSpinner.setSelection(mTaskProjectSpinnerAdapter.getCount());
		mTaskProjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Log.d(TAG, "Task project ID: " + mTaskProjectSpinnerAdapter.getTaskProjectId(position));
				Log.d(TAG, "Task project name: " + Objects.requireNonNull(mTaskProjectSpinnerAdapter.getItem(position)).getName());
				if (mTaskProjectSpinnerAdapter.getTaskProjectId(position).equals("PLUS")) {
					View editTextDialogView = getLayoutInflater().inflate(R.layout.edit_text_dialog, null);
					final TextInputLayout textInputLayout = editTextDialogView.findViewById(R.id.textInputLayout);
					SharedHelper.Companion.getEditText(textInputLayout).setHint("Project name");
					MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(NewTaskActivity.this);
					builder.setTitle("New project")
							.setView(editTextDialogView)
							.setPositiveButton(R.string.dialog_action_create, (dialog, which) -> {
								TaskProject.Builder project = new TaskProject.Builder();
								project.setName(SharedHelper.Companion.getEditTextString(textInputLayout));
								mFirestore.collection("users/" + mCurrentUser.getUid() + "/todoProjects")
										.add(project.create())
										.addOnCompleteListener(task -> {
											if (task.isSuccessful()) {
												Toast.makeText(NewTaskActivity.this, "Successfully created project!", Toast.LENGTH_SHORT)
														.show();
												dialog.dismiss();
											} else {
												Toast.makeText(NewTaskActivity.this, "An error occurred while attempting to create the project. Try again later.", Toast.LENGTH_LONG)
														.show();
												Log.e(TAG, "An error occurred while attempting to create the project:", task.getException());
											}
										});
							})
							.setNegativeButton(R.string.dialog_action_cancel, (dialog, which) -> dialog.dismiss())
							.show();
				} else {
					TaskProject selectedProject = (TaskProject) mTaskProjectSpinner.getItemAtPosition(mTaskProjectSpinner.getSelectedItemPosition());
					tempTaskProject = selectedProject.getId();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				tempTaskProject = null;
			}
		});
		if (mIsEditing) {
			setTitle("Edit task");
			findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
			findViewById(R.id.newTaskScrollView).setVisibility(View.GONE);
			String mTaskId = getIntent().getStringExtra("taskId");
			mFirestore.document("users/" + mCurrentUser.getUid() + "/todos/" + mTaskId)
					.get()
					.addOnCompleteListener(task -> {
						if (task.isSuccessful()) {
							DocumentSnapshot document = task.getResult();
							if (document.exists()) {
								findViewById(R.id.progressBar).setVisibility(View.GONE);
								findViewById(R.id.newTaskScrollView).setVisibility(View.VISIBLE);
								TaskItem mItem = document.toObject(TaskItem.class);
								assert mItem != null;
								mTaskTitle.getEditText().setText(mItem.getTitle());
								if (mItem.getContent() != null) {
									mTaskContent.getEditText().setText(mItem.getContent());
								}
								mTaskIsDone.setChecked(mItem.isDone());
								if (mItem.getDueDate() != null) {
									SimpleDateFormat format = new SimpleDateFormat(getString(R.string.date_format_pattern), Locale.ENGLISH);
									mTaskDate = new Date(mItem.getDueDate().toDate().getTime());
									mDialogDateText.setText(format.format(mTaskDate));
								}
								if (mItem.getProject() != null) {
									mTaskProjectSpinner.setSelection(mTaskProjectSpinnerAdapter.getPosition(mTaskProjectSpinnerAdapter.getTaskProjectById(mItem.getProject().getId())));
								}
								if (mItem.getTags() != null) {
									mTaskTags.getEditText().setText(TextUtils.join(",", mItem.getTags()));
								}
							} else {
								Log.i(TAG, "Task with ID " + mTaskId + " does not exist!");
								Toast.makeText(this, "Task does not exist!", Toast.LENGTH_LONG).show();
							}
						} else {
							Log.i(TAG, "Could not load task.", task.getException());
							Toast.makeText(this, "An error occurred while loading the task", Toast.LENGTH_LONG).show();
						}
					});
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mIsEditing) {
			menu.findItem(R.id.action_submit).setTitle("Save");
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_new_task, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			case R.id.action_submit:
				if (mAllowAccess) {
					if (SharedHelper.Companion.getEditText(mTaskTitle).length() != 0) {
						mTaskTitle.setErrorEnabled(false);
						TaskItem.Builder taskItemBuilder = new TaskItem.Builder();
						taskItemBuilder.setTitle(SharedHelper.Companion.getEditTextString(mTaskTitle));
						if (SharedHelper.Companion.getEditTextString(mTaskContent).length() > 0) {
							taskItemBuilder.setContent(SharedHelper.Companion.getEditTextString(mTaskContent));
						}
						if (SharedHelper.Companion.getEditTextString(mTaskTags).length() > 0) {
							taskItemBuilder.setTags(Arrays.asList(SharedHelper.Companion.getEditTextString(mTaskTags).split(",")));
						}
						if (mTaskDate != null) {
							taskItemBuilder.setDueDate(new Timestamp(mTaskDate));
						}
						if (tempTaskProject != null) {
							taskItemBuilder.setProject(mFirestore.document("users/" + mCurrentUser.getUid() + "/todoProjects/" + tempTaskProject));
						}
						taskItemBuilder.setIsDone(mTaskIsDone.isChecked());
						if (mIsEditing) {
							mFirestore.document("users/" + mCurrentUser.getUid() + "/todos/" + getIntent().getStringExtra("taskId"))
									.set(taskItemBuilder.create())
									.addOnCompleteListener(task -> {
										if (task.isSuccessful()) {
											Toast.makeText(this, "Successfully edited the task!", Toast.LENGTH_SHORT).show();
											finish();
										} else {
											Log.e(TAG, "An error occurred while editing the task:", task.getException());
											Toast.makeText(this, "An error occurred while editing the task. Try again later.", Toast.LENGTH_LONG).show();
										}
									});
						} else {
							SharedHelper.Companion.addTask(taskItemBuilder.create(), mCurrentUser, mFirestore)
									.addOnCompleteListener(task -> {
										if (task.isSuccessful()) {
											Toast.makeText(this, "Successfully added task!", Toast.LENGTH_SHORT).show();
											finish();
										} else {
											Log.e(TAG, "An error occurred while adding the task:", task.getException());
											Toast.makeText(this, "An error occurred while adding the task. Try again later.", Toast.LENGTH_LONG).show();
										}
									});
						}
					} else {
						mTaskTitle.setError("Please enter something.");
						Snackbar.make(findViewById(R.id.newTaskView), "Some errors occurred while attempting to submit the form.", Snackbar.LENGTH_LONG).show();
					}
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
