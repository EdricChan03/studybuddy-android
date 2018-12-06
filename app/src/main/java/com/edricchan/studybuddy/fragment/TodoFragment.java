package com.edricchan.studybuddy.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.edricchan.studybuddy.BuildConfig;
import com.edricchan.studybuddy.DebugActivity;
import com.edricchan.studybuddy.LoginActivity;
import com.edricchan.studybuddy.NewTaskActivity;
import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.RegisterActivity;
import com.edricchan.studybuddy.SettingsActivity;
import com.edricchan.studybuddy.SharedHelper;
import com.edricchan.studybuddy.TasksAdapter;
import com.edricchan.studybuddy.ViewTaskActivity;
import com.edricchan.studybuddy.interfaces.TaskItem;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class TodoFragment extends Fragment {
	private FirebaseAuth mAuth;
	private GoogleApiClient mGoogleApiClient;
	private String mUserName;
	private FirebaseFirestore mFirestore;
	private FirebaseUser mCurrentUser;
	private TasksAdapter mAdapter;
	private RecyclerView mRecyclerView;
	private ListenerRegistration mFirestoreListener;
	private View mFragmentView;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private SharedPreferences mPrefs;
	private SharedPreferences mTodoFragPrefs;
	/**
	 * Request code for new task activity
	 */
	private static final int ACTION_NEW_TASK = 1;
	/**
	 * The Android tag for use with {@link android.util.Log}
	 */
	private static final String TAG = SharedHelper.getTag(TodoFragment.class);

	private static final String SHARED_PREFS_FILE = "TodoFragPrefs";

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_new_todo:
				newTaskActivity();
				return true;
			case R.id.action_refresh_todos:
				loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout);
				return true;
			case R.id.action_settings:
				Intent settingsIntent = new Intent(getContext(), SettingsActivity.class);
				Objects.requireNonNull(getContext()).startActivity(settingsIntent);
				return true;
			case R.id.action_sort_none:
				if (!item.isChecked()) {
					SharedHelper.putPrefs(Objects.requireNonNull(getContext()), SHARED_PREFS_FILE, MODE_PRIVATE, "sortTasksBy", "none")
							.commit();
					item.setChecked(true);
					loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout);
				}
			case R.id.action_sort_title_descending:
				if (!item.isChecked()) {
					SharedHelper.putPrefs(Objects.requireNonNull(getContext()), SHARED_PREFS_FILE, MODE_PRIVATE, "sortTasksBy", "title_desc")
							.commit();
					item.setChecked(true);
					loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout, "title", Query.Direction.DESCENDING);
				}
				return true;
			case R.id.action_sort_title_ascending:
				if (!item.isChecked()) {
					SharedHelper.putPrefs(Objects.requireNonNull(getContext()), SHARED_PREFS_FILE, MODE_PRIVATE, "sortTasksBy", "title_asc")
							.commit();
					item.setChecked(true);
					loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout, "title", Query.Direction.ASCENDING);
				}
				return true;
			case R.id.action_sort_due_date_new_to_old:
				if (!item.isChecked()) {
					SharedHelper.putPrefs(Objects.requireNonNull(getContext()), SHARED_PREFS_FILE, MODE_PRIVATE, "sortTasksBy", "due_date_new_to_old")
							.commit();
					item.setChecked(true);
					loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout, "dueDate", Query.Direction.DESCENDING);
				}
				return true;
			case R.id.action_sort_due_date_old_to_new:
				if (!item.isChecked()) {
					SharedHelper.putPrefs(Objects.requireNonNull(getContext()), SHARED_PREFS_FILE, MODE_PRIVATE, "sortTasksBy", "due_date_old_to_new")
							.commit();
					item.setChecked(true);
					loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout, "dueDate", Query.Direction.ASCENDING);
				}
			case R.id.action_debug:
				Intent debugIntent = new Intent(getActivity(), DebugActivity.class);
				startActivity(debugIntent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onPrepareOptionsMenu(@NonNull Menu menu) {
		if (!BuildConfig.DEBUG) {
			menu.removeItem(R.id.action_debug);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Clear the main activity's menu before inflating the fragment's menu
		menu.clear();
		inflater.inflate(R.menu.menu_frag_todo, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_todo, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		mTodoFragPrefs = Objects.requireNonNull(getContext()).getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
		mFirestore = FirebaseFirestore.getInstance();
		mFragmentView = view;

		// FAB
		FloatingActionButton fab = mFragmentView.findViewById(R.id.new_todo_fab);
		fab.setOnClickListener(fabView -> newTaskActivity());

		// Handles swiping down to refresh logic
		mSwipeRefreshLayout = mFragmentView.findViewById(R.id.recycler_swiperefresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
		// Sets a refreshing listener
		mSwipeRefreshLayout.setOnRefreshListener(() -> {
			mAdapter.notifyDataSetChanged();
			loadTasksListHandler();
		});
		mRecyclerView = mFragmentView.findViewById(R.id.recycler_list);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(false);

		// use a linear layout manager
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
			@Override
			public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
				mAdapter.deleteTask(viewHolder.getAdapterPosition());
			}
		}).attachToRecyclerView(mRecyclerView);
		mFragmentView.findViewById(R.id.action_new_todo)
				.setOnClickListener(v -> newTaskActivity());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mFirestoreListener != null) {
			mFirestoreListener.remove();
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void onStart() {
		super.onStart();
		mAuth = FirebaseAuth.getInstance();
		mCurrentUser = mAuth.getCurrentUser();
		if (mCurrentUser == null) {
			Log.d(TAG, "Not logged in");
			AlertDialog signInDialog = new AlertDialog.Builder(getContext())
					.setTitle("Sign in")
					.setMessage("To access the content, please login or register for an account.")
					.setPositiveButton(R.string.dialog_action_login, (dialogInterface, i) -> {
						Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
						startActivity(loginIntent);
						dialogInterface.dismiss();
					})
					.setNeutralButton(R.string.dialog_action_sign_up, (dialogInterface, i) -> {
						Intent registerIntent = new Intent(getActivity(), RegisterActivity.class);
						startActivity(registerIntent);
						dialogInterface.dismiss();
					})
					.setNegativeButton(R.string.dialog_action_cancel, (dialogInterface, i) -> dialogInterface.cancel())
					.create();
			signInDialog.show();

		} else {
			loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout);
		}
	}

	private void newTaskActivity() {
		Intent newTaskIntent = new Intent(getContext(), NewTaskActivity.class);
		startActivityForResult(newTaskIntent, ACTION_NEW_TASK);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Checks if the activity was from the New task activity
		if (requestCode == ACTION_NEW_TASK) {
			if (resultCode == RESULT_OK) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				builder.setTitle("Activity result")
						.setMessage("taskTitle: " + data.getStringExtra("taskTitle") + "\ntaskProject: " + data.getStringExtra("taskProject") + "\ntaskContent: " + data.getStringExtra("taskContent"))
						.setPositiveButton("Dismiss", (dialog, which) -> dialog.dismiss());
				builder.show();
			}
		}
	}

	private void loadTasksListHandler() {
		if (mPrefs.getString("pref_todo_default_sort", "no_value").equals("no_value")) {
			if (!mTodoFragPrefs.getString("sortTasksBy", "no_value").equals("no_value")) {
				switch (Objects.requireNonNull(mTodoFragPrefs.getString("sortTasksBy", "due_date_new_to_old"))) {
					case "title_desc":
						loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout, "title", Query.Direction.DESCENDING);
						break;
					case "title_asc":
						loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout, "title", Query.Direction.ASCENDING);
						break;
					case "due_date_new_to_old":
						loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout, "dueDate", Query.Direction.DESCENDING);
						break;
					case "due_date_old_to_new":
						loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout, "dueDate", Query.Direction.ASCENDING);
						break;
					default:
						loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout);
						break;
				}
			} else {
				loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout);
			}
		} else {
			switch (Objects.requireNonNull(mPrefs.getString("pref_todo_default_sort", "due_date"))) {
				case "title_desc":
					loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout, "title", Query.Direction.DESCENDING);
					break;
				case "title_asc":
					loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout, "title", Query.Direction.ASCENDING);
					break;
				case "due_date_new_to_old":
					loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout, "dueDate", Query.Direction.DESCENDING);
					break;
				case "due_date_old_to_new":
					loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout, "dueDate", Query.Direction.ASCENDING);
					break;
				default:
					loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout);
					break;
			}
		}
	}

	private void loadTasksList(String uid, final SwipeRefreshLayout swipeRefreshLayout, String fieldPath, Query.Direction direction) {
		mSwipeRefreshLayout.setRefreshing(true);
		mFirestoreListener = mFirestore.collection("users/" + mCurrentUser.getUid() + "/todos")
				.orderBy(fieldPath, direction)
				.addSnapshotListener((documentSnapshots, e) -> {
					if (e != null) {
						Log.e(TAG, "Listen failed!", e);
						return;
					}
					List<TaskItem> taskItemList = new ArrayList<>();

					assert documentSnapshots != null;
					for (DocumentSnapshot doc : documentSnapshots) {
						Log.d(TAG, "Document: " + doc.toString());
						TaskItem note = doc.toObject(TaskItem.class);
						taskItemList.add(note);
					}

					mAdapter = new TasksAdapter(getContext(), taskItemList, mCurrentUser, mFirestore, mFragmentView.findViewById(R.id.todo_view));
					mRecyclerView.setAdapter(mAdapter);
					mAdapter.setOnItemClickListener((document, position) -> {
						Intent viewItemIntent = new Intent(getContext(), ViewTaskActivity.class);
						viewItemIntent.putExtra("taskId", document.getId());
						getContext().startActivity(viewItemIntent);
					});
					swipeRefreshLayout.setRefreshing(false);
					if (documentSnapshots.isEmpty()) {
						Log.d(TAG, "Empty!");
						mFragmentView.findViewById(R.id.todos_empty_state_view).setVisibility(View.VISIBLE);
						mSwipeRefreshLayout.setVisibility(View.GONE);
					} else {
						Log.d(TAG, "Not Empty!");
						mFragmentView.findViewById(R.id.todos_empty_state_view).setVisibility(View.GONE);
						mSwipeRefreshLayout.setVisibility(View.VISIBLE);
					}
				});
	}

	private void loadTasksList(String uid, @NonNull final SwipeRefreshLayout swipeRefreshLayout) {
		mSwipeRefreshLayout.setRefreshing(true);
		mFirestoreListener = mFirestore.collection("users/" + mCurrentUser.getUid() + "/todos")
				.addSnapshotListener((documentSnapshots, e) -> {
					if (e != null) {
						Log.e(TAG, "Listen failed!", e);
						return;
					}
					List<TaskItem> taskItemList = new ArrayList<>();

					assert documentSnapshots != null;
					for (DocumentSnapshot doc : documentSnapshots) {
						Log.d(TAG, "Document: " + doc.toString());
						TaskItem note = doc.toObject(TaskItem.class);
						taskItemList.add(note);
					}

					mAdapter = new TasksAdapter(getContext(), taskItemList, mCurrentUser, mFirestore, mFragmentView.findViewById(R.id.todo_view));
					mRecyclerView.setAdapter(mAdapter);
					mAdapter.setOnItemClickListener((document, position) -> {
						Intent viewItemIntent = new Intent(getContext(), ViewTaskActivity.class);
						viewItemIntent.putExtra("taskId", document.getId());
						getContext().startActivity(viewItemIntent);
					});
					swipeRefreshLayout.setRefreshing(false);
					if (documentSnapshots.isEmpty()) {
						Log.d(TAG, "Empty!");
						mFragmentView.findViewById(R.id.todos_empty_state_view).setVisibility(View.VISIBLE);
						mSwipeRefreshLayout.setVisibility(View.GONE);
					} else {
						Log.d(TAG, "Not Empty!");
						mFragmentView.findViewById(R.id.todos_empty_state_view).setVisibility(View.GONE);
						mSwipeRefreshLayout.setVisibility(View.VISIBLE);
					}
				});
	}
}
