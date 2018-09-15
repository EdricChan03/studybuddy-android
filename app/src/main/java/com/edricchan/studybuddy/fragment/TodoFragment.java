package com.edricchan.studybuddy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.edricchan.studybuddy.LoginActivity;
import com.edricchan.studybuddy.NewTaskActivity;
import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.RegisterActivity;
import com.edricchan.studybuddy.SettingsActivity;
import com.edricchan.studybuddy.SharedHelper;
import com.edricchan.studybuddy.StudyAdapter;
import com.edricchan.studybuddy.TaskItem;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static android.app.Activity.RESULT_OK;

public class TodoFragment extends Fragment {
	private FirebaseAuth mAuth;
	private GoogleApiClient mGoogleApiClient;
	private String mUserName;
	private FirebaseFirestore mFirestore;
	private FirebaseUser mCurrentUser;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView mRecyclerView;
	private ListenerRegistration mFirestoreListener;
	private View mFragmentView;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	/**
	 * Request code for new task activity
	 */
	private static final int ACTION_NEW_TASK = 1;
	/**
	 * The Android tag for use with {@link android.util.Log}
	 */
	private static final String TAG = SharedHelper.getTag(TodoFragment.class);

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_new_todo:
				newTaskActivity();
				break;
			case R.id.action_refresh_todos:
				loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout);
				break;
			case R.id.action_settings:
				Intent settingsIntent = new Intent(getContext(), SettingsActivity.class);
				Objects.requireNonNull(getContext()).startActivity(settingsIntent);
				break;
		}
		return true;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
		mFirestore = FirebaseFirestore.getInstance();
		mFragmentView = view;

		// FAB
		FloatingActionButton fab = (FloatingActionButton) mFragmentView.findViewById(R.id.new_todo_fab);
		fab.setOnClickListener(fabView -> newTaskActivity());

		// Handles swiping down to refresh logic
		mSwipeRefreshLayout = mFragmentView.findViewById(R.id.recycler_swiperefresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
		// Sets a refreshing listener
		mSwipeRefreshLayout.setOnRefreshListener(() -> {
			mAdapter.notifyDataSetChanged();
			loadTasksList(mCurrentUser.getUid(), mSwipeRefreshLayout);
		});
		mRecyclerView = mFragmentView.findViewById(R.id.recycler_list);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(false);

		// use a linear layout manager
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
		mRecyclerView.setLayoutManager(mLayoutManager);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mFirestoreListener != null) {
			mFirestoreListener.remove();
		}
	}

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
			loadTasksList(mCurrentUser.getUid());
			mFirestoreListener = mFirestore.collection("users/" + mCurrentUser.getUid() + "/todos")
					.addSnapshotListener((documentSnapshots, e) -> {
						if (e != null) {
							Log.e(TAG, "Listen failed!", e);
							return;
						}
						List<TaskItem> taskItemList = new ArrayList<>();

						for (DocumentSnapshot doc : documentSnapshots) {
							Log.d(TAG, "Document: " + doc.toString());
							TaskItem note = doc.toObject(TaskItem.class);
							note.setId(doc.getId());
							taskItemList.add(note);
						}

						mAdapter = new StudyAdapter(getContext(), taskItemList, mCurrentUser, mFirestore, mFragmentView.findViewById(R.id.todo_view));
						mRecyclerView.setAdapter(mAdapter);
						if (documentSnapshots.isEmpty()) {
							Log.d(TAG, "Empty!");
							mFragmentView.findViewById(R.id.todos_empty_state_view).setVisibility(View.VISIBLE);
						} else {
							Log.d(TAG, "Not Empty!");
							mFragmentView.findViewById(R.id.todos_empty_state_view).setVisibility(View.GONE);
						}
					});
			// User specific topic
			FirebaseMessaging.getInstance().subscribeToTopic("user_" + mCurrentUser.getUid());
			// By default, subscribe to the "topic_all" topic
			FirebaseMessaging.getInstance().subscribeToTopic("all");
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

	private void loadTasksList(String uid, final SwipeRefreshLayout mSwipeRefreshLayout) {
		mSwipeRefreshLayout.setRefreshing(true);
		mFirestore.collection("users/" + uid + "/todos")
				.get()
				.addOnCompleteListener(task -> {
					if (task.isSuccessful()) {
						mSwipeRefreshLayout.setRefreshing(false);
						List<TaskItem> taskItemList = new ArrayList<>();
						int i = 0;
						for (DocumentSnapshot doc : task.getResult()) {
							Log.d(TAG, "Documents: " + doc.toString());
							TaskItem taskItem = doc.toObject(TaskItem.class);
							taskItem.setId(doc.getId());
							taskItemList.add(taskItem);
							Log.d(TAG, "Adding item " + i++);
						}
						mAdapter = new StudyAdapter(getContext(), taskItemList, mCurrentUser, mFirestore, mFragmentView.findViewById(R.id.todo_view));
						RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
						mRecyclerView.setLayoutManager(mLayoutManager);
						mRecyclerView.setItemAnimator(new DefaultItemAnimator());
						mRecyclerView.setAdapter(mAdapter);
						if (task.getResult().isEmpty()) {
							Log.d(TAG, "Empty stuff!");
							mFragmentView.findViewById(R.id.todos_empty_state_view).setVisibility(View.VISIBLE);
						} else {
							Log.d(TAG, "Not empty stuff!");
							mFragmentView.findViewById(R.id.todos_empty_state_view).setVisibility(View.GONE);
						}
					} else {
						Log.d(TAG, "Error getting documents: ", task.getException());
					}
				});
	}

	private void loadTasksList(String uid) {
		mFirestore.collection("users/" + uid + "/todos")
				.get()
				.addOnCompleteListener(task -> {
					if (task.isSuccessful()) {
						List<TaskItem> taskItemList = new ArrayList<>();
						int i = 0;
						for (DocumentSnapshot doc : task.getResult()) {
							Log.d(TAG, "Documents: " + doc.toString());
							TaskItem taskItem = doc.toObject(TaskItem.class);
							taskItem.setId(doc.getId());
							taskItemList.add(taskItem);
							Log.d(TAG, "Adding item " + i++);
						}
						mAdapter = new StudyAdapter(getContext(), taskItemList, mCurrentUser, mFirestore, mFragmentView.findViewById(R.id.todo_view));
						RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
						mRecyclerView.setLayoutManager(mLayoutManager);
						mRecyclerView.setItemAnimator(new DefaultItemAnimator());
						mRecyclerView.setAdapter(mAdapter);
						if (task.getResult().isEmpty()) {
							Log.d(TAG, "Empty meh!");
							mFragmentView.findViewById(R.id.todos_empty_state_view).setVisibility(View.VISIBLE);
						} else {
							Log.d(TAG, "Not empty meh!");
							mFragmentView.findViewById(R.id.todos_empty_state_view).setVisibility(View.GONE);
						}
					} else {
						Log.d(TAG, "Error getting documents: ", task.getException());
					}
				});
	}
}
