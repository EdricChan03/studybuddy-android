package com.edricchan.studybuddy.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
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
import com.edricchan.studybuddy.ViewTaskActivity;
import com.edricchan.studybuddy.adapter.TasksAdapter;
import com.edricchan.studybuddy.adapter.itemdetailslookup.TaskItemLookup;
import com.edricchan.studybuddy.adapter.itemkeyprovider.TaskItemKeyProvider;
import com.edricchan.studybuddy.interfaces.TaskItem;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import java8.util.stream.IntStreams;

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
	private SelectionTracker<String> mSelectionTracker;
	private AppCompatActivity mParentActivity;
	private ActionMode.Callback mActionModeCallback;
	/**
	 * Request code for new task activity
	 */
	private static final int ACTION_NEW_TASK = 1;
	/**
	 * The Android tag for use with {@link android.util.Log}
	 */
	private static final String TAG = SharedHelper.Companion.getTag(TodoFragment.class);

	private static final String SHARED_PREFS_FILE = "TodoFragPrefs";

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_new_todo:
				newTaskActivity();
				return true;
			case R.id.action_refresh_todos:
				loadTasksList(mCurrentUser.getUid());
				return true;
			case R.id.action_settings:
				Intent settingsIntent = new Intent(getContext(), SettingsActivity.class);
				Objects.requireNonNull(getContext()).startActivity(settingsIntent);
				return true;
			case R.id.action_sort_none:
				if (!item.isChecked()) {
					SharedHelper.Companion.putPrefs(Objects.requireNonNull(getContext()), SHARED_PREFS_FILE, MODE_PRIVATE, "sortTasksBy", "none")
							.commit();
					item.setChecked(true);
					loadTasksList(mCurrentUser.getUid());
				}
			case R.id.action_sort_title_descending:
				if (!item.isChecked()) {
					SharedHelper.Companion.putPrefs(Objects.requireNonNull(getContext()), SHARED_PREFS_FILE, MODE_PRIVATE, "sortTasksBy", "title_desc")
							.commit();
					item.setChecked(true);
					loadTasksList(mCurrentUser.getUid(), "title", Query.Direction.DESCENDING);
				}
				return true;
			case R.id.action_sort_title_ascending:
				if (!item.isChecked()) {
					SharedHelper.Companion.putPrefs(Objects.requireNonNull(getContext()), SHARED_PREFS_FILE, MODE_PRIVATE, "sortTasksBy", "title_asc")
							.commit();
					item.setChecked(true);
					loadTasksList(mCurrentUser.getUid(), "title", Query.Direction.ASCENDING);
				}
				return true;
			case R.id.action_sort_due_date_new_to_old:
				if (!item.isChecked()) {
					SharedHelper.Companion.putPrefs(Objects.requireNonNull(getContext()), SHARED_PREFS_FILE, MODE_PRIVATE, "sortTasksBy", "due_date_new_to_old")
							.commit();
					item.setChecked(true);
					loadTasksList(mCurrentUser.getUid(), "dueDate", Query.Direction.DESCENDING);
				}
				return true;
			case R.id.action_sort_due_date_old_to_new:
				if (!item.isChecked()) {
					SharedHelper.Companion.putPrefs(Objects.requireNonNull(getContext()), SHARED_PREFS_FILE, MODE_PRIVATE, "sortTasksBy", "due_date_old_to_new")
							.commit();
					item.setChecked(true);
					loadTasksList(mCurrentUser.getUid(), "dueDate", Query.Direction.ASCENDING);
				}
			case R.id.action_debug:
				Intent debugIntent = new Intent(getContext(), DebugActivity.class);
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
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
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

		if (savedInstanceState != null && mSelectionTracker != null) {
			mSelectionTracker.onRestoreInstanceState(savedInstanceState);
		}

		mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		mTodoFragPrefs = Objects.requireNonNull(getContext()).getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
		mFirestore = FirebaseFirestore.getInstance();
		mFragmentView = view;

		SharedHelper.Companion.setBottomAppBarFabOnClickListener(mParentActivity, v -> newTaskActivity());

		// Handles swiping down to refresh logic
		mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
		// Sets a refreshing listener
		mSwipeRefreshLayout.setOnRefreshListener(() -> {
			mAdapter.notifyDataSetChanged();
			loadTasksListHandler();
		});
		mRecyclerView = findViewById(R.id.recyclerView);

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
		findViewById(R.id.actionNewTodo)
				.setOnClickListener(v -> newTaskActivity());
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mSelectionTracker != null) {
			mSelectionTracker.onSaveInstanceState(outState);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (mFirestoreListener != null) {
			mFirestoreListener.remove();
		}

		SharedHelper.Companion.clearBottomAppBarFabOnClickListener(mParentActivity);
	}

	@Override
	public void onStart() {
		super.onStart();
		mAuth = FirebaseAuth.getInstance();
		mCurrentUser = mAuth.getCurrentUser();
		if (mCurrentUser == null) {
			Log.d(TAG, "Not logged in");
			MaterialAlertDialogBuilder signInDialogBuilder = new MaterialAlertDialogBuilder(getContext());
			signInDialogBuilder.setTitle("Sign in")
					.setMessage("To access the content, please login or register for an account.")
					.setPositiveButton(R.string.dialog_action_login, (dialogInterface, i) -> {
						Intent loginIntent = new Intent(getContext(), LoginActivity.class);
						startActivity(loginIntent);
						dialogInterface.dismiss();
					})
					.setNeutralButton(R.string.dialog_action_sign_up, (dialogInterface, i) -> {
						Intent registerIntent = new Intent(getContext(), RegisterActivity.class);
						startActivity(registerIntent);
						dialogInterface.dismiss();
					})
					.setNegativeButton(R.string.dialog_action_cancel, (dialogInterface, i) -> dialogInterface.cancel())
					.show();

		} else {
			loadTasksList(mCurrentUser.getUid());
		}
	}

	@Override
	public void onAttach(@NonNull Context context) {
		mParentActivity = (AppCompatActivity) context;
		super.onAttach(context);
	}

	private void newTaskActivity() {
		Intent newTaskIntent = new Intent(getContext(), NewTaskActivity.class);
		startActivityForResult(newTaskIntent, ACTION_NEW_TASK);
	}

	private void loadTasksListHandler() {
		if (mPrefs.getString("pref_todo_default_sort", "no_value").equals("no_value")) {
			if (!mTodoFragPrefs.getString("sortTasksBy", "no_value").equals("no_value")) {
				switch (Objects.requireNonNull(mTodoFragPrefs.getString("sortTasksBy", "due_date_new_to_old"))) {
					case "title_desc":
						loadTasksList(mCurrentUser.getUid(), "title", Query.Direction.DESCENDING);
						break;
					case "title_asc":
						loadTasksList(mCurrentUser.getUid(), "title", Query.Direction.ASCENDING);
						break;
					case "due_date_new_to_old":
						loadTasksList(mCurrentUser.getUid(), "dueDate", Query.Direction.DESCENDING);
						break;
					case "due_date_old_to_new":
						loadTasksList(mCurrentUser.getUid(), "dueDate", Query.Direction.ASCENDING);
						break;
					default:
						loadTasksList(mCurrentUser.getUid());
						break;
				}
			} else {
				loadTasksList(mCurrentUser.getUid());
			}
		} else {
			switch (Objects.requireNonNull(mPrefs.getString("pref_todo_default_sort", "due_date"))) {
				case "title_desc":
					loadTasksList(mCurrentUser.getUid(), "title", Query.Direction.DESCENDING);
					break;
				case "title_asc":
					loadTasksList(mCurrentUser.getUid(), "title", Query.Direction.ASCENDING);
					break;
				case "due_date_new_to_old":
					loadTasksList(mCurrentUser.getUid(), "dueDate", Query.Direction.DESCENDING);
					break;
				case "due_date_old_to_new":
					loadTasksList(mCurrentUser.getUid(), "dueDate", Query.Direction.ASCENDING);
					break;
				default:
					loadTasksList(mCurrentUser.getUid());
					break;
			}
		}
	}

	/**
	 * Loads the task list
	 *
	 * @param uid       The user's ID
	 * @param fieldPath The field path to sort the list by
	 * @param direction The direction to sort the list in
	 */
	private void loadTasksList(String uid, @Nullable String fieldPath, @Nullable Query.Direction direction) {
		// Reduce the amount of duplicate code by placing the listener into a variable
		EventListener<QuerySnapshot> listener = (documentSnapshots, e) -> {
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

			mAdapter = new TasksAdapter(getContext(), taskItemList);
			mRecyclerView.setAdapter(mAdapter);
			mAdapter
					.setOnItemClickListener(new TasksAdapter.OnItemClickListener() {
						@Override
						public void onItemClick(TaskItem item, int position) {
							Intent viewItemIntent = new Intent(getContext(), ViewTaskActivity.class);
							viewItemIntent.putExtra("taskId", item.getId());
							startActivity(viewItemIntent);
						}

						@Override
						public void onDeleteButtonClick(TaskItem item, int position) {
							MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
							builder
									.setTitle(R.string.todo_frag_delete_task_dialog_title)
									.setMessage(R.string.todo_frag_delete_task_dialog_msg)
									.setNegativeButton(R.string.dialog_action_cancel, (dialog, which) -> dialog.dismiss())
									.setPositiveButton(R.string.dialog_action_ok, (dialog, which) -> mFirestore.document("users/" + mCurrentUser.getUid() + "/todos/" + item.getId())
											.delete()
											.addOnCompleteListener(task -> {
												if (task.isSuccessful()) {
													Snackbar.make(
															findParentActivityViewById(R.id.coordinatorLayout),
															"Successfully deleted todo!",
															Snackbar.LENGTH_SHORT)
															.show();
													mAdapter.notifyItemRemoved(position);
													dialog.dismiss();
												} else {
													Log.e(TAG, "An error occurred while attempting to delete the todo:", task.getException());
												}
											}))
									.show();
						}

						@Override
						public void onMarkAsDoneButtonClick(TaskItem item, int position) {
							mFirestore.document("users/" + mCurrentUser.getUid() + "/todos/" + item.getId())
									.update("isDone", !item.isDone())
									.addOnCompleteListener(task -> {
										if (task.isSuccessful()) {
											Snackbar.make(
													findParentActivityViewById(R.id.coordinatorLayout),
													"Successfully marked task as " + (!item.isDone() ? "done" : "undone") + "!",
													Snackbar.LENGTH_SHORT)
													.show();
											mAdapter.notifyItemChanged(position);
										} else {
											Snackbar.make(
													findParentActivityViewById(R.id.coordinatorLayout),
													"An error occurred while attempting to mark the todo as " + (!item.isDone() ? "done" : "undone"),
													Snackbar.LENGTH_LONG)
													.show();
											Log.e(TAG, "An error occurred while attempting to mark the todo as " + (!item.isDone() ? "done" : "undone") + ":", task.getException());
										}
									});
						}
					});
			if (mSelectionTracker == null) {
				mSelectionTracker = new SelectionTracker.Builder<>(
						"selection-id",
						mRecyclerView,
						new TaskItemKeyProvider(taskItemList),
						new TaskItemLookup(mRecyclerView),
						StorageStrategy.createStringStorage())
						.withOnItemActivatedListener((item, event) -> {
							Intent viewItemIntent = new Intent(getContext(), ViewTaskActivity.class);
							viewItemIntent.putExtra("taskId", taskItemList.get(item.getPosition()).getId());
							startActivity(viewItemIntent);
							return true;
						})
						.withSelectionPredicate(SelectionPredicates.createSelectAnything())
						.build();
			}
			mSwipeRefreshLayout.setRefreshing(false);
			if (documentSnapshots.isEmpty()) {
				Log.d(TAG, "Empty!");
				findViewById(R.id.todoEmptyStateView).setVisibility(View.VISIBLE);
				mSwipeRefreshLayout.setVisibility(View.GONE);
			} else {
				Log.d(TAG, "Not Empty!");
				findViewById(R.id.todoEmptyStateView).setVisibility(View.GONE);
				mSwipeRefreshLayout.setVisibility(View.VISIBLE);
			}
			mActionModeCallback = new ActionMode.Callback() {
				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.cab_tasks, menu);
					return true;
				}

				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					if (mSelectionTracker.hasSelection()) {
						if (mSelectionTracker.getSelection().size() == taskItemList.size()) {
							menu.removeItem(R.id.cab_action_select_all);
						}
					}
					return true;
				}

				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					switch (item.getItemId()) {
						case R.id.cab_action_delete_selected:
							MaterialAlertDialogBuilder confirmBuilder = new MaterialAlertDialogBuilder(getContext());
							confirmBuilder
									.setTitle(R.string.todo_frag_delete_selected_tasks_dialog_title)
									.setMessage(R.string.todo_frag_delete_selected_tasks_dialog_msg)
									.setNegativeButton(R.string.dialog_action_cancel, (dialog, which) -> {
										mode.finish();
										dialog.dismiss();
									})
									.setPositiveButton(R.string.dialog_action_ok, (dialog, which) -> {
										for (String id : mSelectionTracker.getSelection()) {
											mFirestore.document("users/" + mCurrentUser.getUid() + "/todos/" + id)
													.delete()
													.addOnCompleteListener(task -> {
														if (task.isSuccessful()) {
															Log.d(TAG, "Successfully deleted todo!");
															mAdapter.notifyItemRemoved(getTaskItemPosition(taskItemList, id));
														} else {
															Log.e(TAG, "An error occurred while attempting to delete the todo at ID " + id + ":", task.getException());
															Toast.makeText(mParentActivity, "An error occurred while attempting to delete the todo at ID " + id, Toast.LENGTH_SHORT).show();
														}
													});
										}
										dialog.dismiss();
									})
									.show();
							return true;
						case R.id.cab_action_select_all:
							Toast.makeText(mParentActivity, "Not implemented!", Toast.LENGTH_SHORT).show();
							return true;
						case R.id.cab_action_edit_selected:
							Toast.makeText(mParentActivity, "Not implemented!", Toast.LENGTH_SHORT).show();
						default:
							return false;
					}
				}

				@Override
				public void onDestroyActionMode(ActionMode mode) {
					mSelectionTracker.clearSelection();
				}
			};
			mSelectionTracker
					.addObserver(new SelectionTracker.SelectionObserver() {
						@Override
						public void onSelectionChanged() {
							super.onSelectionChanged();
							Log.d(TAG, "Current size of selection: " + mSelectionTracker.getSelection().size());
							if (mSelectionTracker.hasSelection()) {
								ActionMode mode = mParentActivity.startSupportActionMode(mActionModeCallback);
								mode.setTitle(R.string.cab_title);
								mode.setSubtitle(getResources().getQuantityString(R.plurals.cab_subtitle, mSelectionTracker.getSelection().size(), mSelectionTracker.getSelection().size()));
							}
						}
					});
		};
		mSwipeRefreshLayout.setRefreshing(true);
		CollectionReference collectionRef = mFirestore.collection("users/" + mCurrentUser.getUid() + "/todos");
		if (fieldPath != null && direction != null) {
			mFirestoreListener = collectionRef.orderBy(fieldPath, direction)
					.addSnapshotListener(listener);
		} else {
			mFirestoreListener = collectionRef.addSnapshotListener(listener);
		}
	}


	/**
	 * Gets the position of the first occurrence of the document ID
	 *
	 * @param list The list to get the position for
	 * @param id   The ID of the document
	 * @return The position of the first occurrence of the document ID
	 */
	private int getTaskItemPosition(final List<TaskItem> list, final String id) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			return IntStream.range(0, list.size())
					.filter(index -> list.get(index).getId().equals(id))
					.findFirst()
					.orElse(-1);
		} else {
			return IntStreams
					.range(0, list.size())
					.filter(index -> list.get(index).getId().equals(id))
					.findFirst()
					.orElse(-1);
		}
	}

	private <T extends View> T findParentActivityViewById(@IdRes int id) {
		return mParentActivity.findViewById(id);
	}

	private <T extends View> T findViewById(@IdRes int id) {
		return mFragmentView.findViewById(id);
	}

	/**
	 * Loads the task list
	 *
	 * @param uid The user's ID
	 */
	private void loadTasksList(String uid) {
		loadTasksList(uid, null, null);
		/*mSwipeRefreshLayout.setRefreshing(true);
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

					mAdapter = new TasksAdapter(getContext(), taskItemList, mCurrentUser, mFirestore, getActivity().findViewById(R.id.coordinatorLayout));
					mRecyclerView.setAdapter(mAdapter);
					mAdapter.setOnItemClickListener((document, position) -> {
						Intent viewItemIntent = new Intent(getContext(), ViewTaskActivity.class);
						viewItemIntent.putExtra("taskId", document.getId());
						getContext().startActivity(viewItemIntent);
					});
					mSelectionTracker = new SelectionTracker.Builder<>(
							"selection-id",
							mRecyclerView,
							new TaskItemKeyProvider(ItemKeyProvider.SCOPE_MAPPED, taskItemList),
							new TaskItemLookup(mRecyclerView),
							StorageStrategy.createStringStorage())
							.withSelectionPredicate(new TaskItemPredicate())
							.build();
					mAdapter.setSelectionTracker(mSelectionTracker);
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
				});*/
	}
}
