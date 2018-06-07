package com.edricchan.studybuddy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
	/**
	 * Request code for new task activity
	 */
	static final int ACTION_NEW_TASK = 1;
	private static final String ACTION_ADD_NEW_TODO = "com.edricchan.studybuddy.shortcuts.ADD_NEW_TODO";
	private static final String TAG = SharedHelper.getTag(MainActivity.class);
	final Context context = this;
	private ArrayList taskItems;
	private int testInt, RC_SIGN_IN;
	private FirebaseAuth mAuth;
	private GoogleApiClient mGoogleApiClient;
	private String userName;
	private FirebaseFirestore db;
	private FirebaseUser currentUser;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView mRecyclerView;
	private ListenerRegistration firestoreListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = FirebaseFirestore.getInstance();
		// Checks if the add new shortcut was tapped
		if (ACTION_ADD_NEW_TODO.equals(getIntent().getAction())) {
			newTaskActivity();
		}
		// FAB
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				newTaskActivity();
			}
		});

		// Handles swiping down to refresh logic
		final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.recycler_swiperefresh);
		// Sets a refreshing listener
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mAdapter.notifyDataSetChanged();
				new android.os.Handler().postDelayed(
						new Runnable() {
							@Override
							public void run() {
								swipeRefreshLayout.setRefreshing(false);

							}
						},
						1000);
			}
		});
		mRecyclerView = findViewById(R.id.recycler_list);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(true);

		// use a linear layout manager
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);

		// specify an adapter (see also next example)
		//noinspection unchecked
		mAdapter = new StudyAdapter(this, taskItems);
		mRecyclerView.setAdapter(mAdapter);
		// Check if Android Oreo
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			SharedHelper.createNotificationChannels(MainActivity.this);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		firestoreListener.remove();
	}

	@Override
	public void onStart() {
		super.onStart();
		mAuth = FirebaseAuth.getInstance();
		currentUser = mAuth.getCurrentUser();
		if (currentUser == null) {
			Log.d(TAG, "Not logged in");
			AlertDialog signInDialog = new AlertDialog.Builder(context)
					.setTitle("Sign in")
					.setMessage("To access the content, please login or register for an account.")
					.setPositiveButton(R.string.dialog_action_login, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
							startActivity(loginIntent);
							dialogInterface.dismiss();
						}
					})
					.setNeutralButton(R.string.dialog_action_sign_up, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
							startActivity(registerIntent);
							dialogInterface.dismiss();
						}
					})
					.setNegativeButton(R.string.dialog_action_cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							dialogInterface.cancel();
						}
					})
					.create();
			signInDialog.show();

		} else {
			loadTasksList(currentUser.getUid());
			firestoreListener = db.collection("users/" + currentUser.getUid() + "/todos")
					.addSnapshotListener(new EventListener<QuerySnapshot>() {
						@Override
						public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
							if (e != null) {
								Log.e(TAG, "Listen failed!", e);
								return;
							}

							List<TaskItem> taskItemList = new ArrayList<>();

							for (DocumentSnapshot doc : documentSnapshots) {
								TaskItem note = doc.toObject(TaskItem.class);
								note.setId(doc.getId());
								taskItemList.add(note);
							}

							mAdapter = new StudyAdapter(getApplicationContext(), taskItemList);
							mRecyclerView.setAdapter(mAdapter);
						}
					});
			// User specific topic
			FirebaseMessaging.getInstance().subscribeToTopic("user_" + currentUser.getUid());
			// By default, subscribe to the "topic_all" topic
			FirebaseMessaging.getInstance().subscribeToTopic("all");
		}
	}

	/**
	 * Used for debugging.
	 * Will not be used on release.
	 */
	public void sendNotification(int type) {
		SharedHelper helper = new SharedHelper(this);
		switch (type) {
			case 0:
				helper.sendNotificationToUserWithBody(currentUser.getUid(), "Testing testing", "This is a test notification which will only be sent to a specific device! Hopefully it'll work though.", getString(R.string.notification_channel_uncategorised_id));
				break;
			case 1:
				helper.sendNotificationToUserWithBody(currentUser.getUid(), "Weekly summary", "You have 8 todos left to do for this week. Your total karma is 80%. Keep it up! :D", getString(R.string.notification_channel_weekly_summary_id));
				break;
			case 2:
				helper.sendNotificationToUserWithBody(currentUser.getUid(), "Notification alert", "This notification should create a new notification channel! See the source code for more info.", "test");
				break;
			case 3:
				helper.sendNotificationToUserWithBody(currentUser.getUid(), "Todo due soon", "Your todo will be due soon. Mark as done?", getString(R.string.notification_channel_todo_updates_id));
				break;
			case 4:
				helper.sendNotificationToUser(currentUser.getUid(), "No body", getString(R.string.notification_channel_uncategorised_id));
				break;
		}
	}

	/**
	 * Shares the app
	 */
	public void share() {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT, R.string.share_content);
		shareIntent.setType("text/plain");
		startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_intent_value)));
	}

	private void newTaskActivity() {
		Intent newTaskIntent = new Intent(this, NewTaskActivity.class);
		startActivityForResult(newTaskIntent, ACTION_NEW_TASK);
	}

	private void loadTasksList(String uid) {
		db.collection("users/" + uid + "/todos")
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							List<TaskItem> taskItemList = new ArrayList<>();

							for (DocumentSnapshot doc : task.getResult()) {
								TaskItem note = doc.toObject(TaskItem.class);
								note.setId(doc.getId());
								taskItemList.add(note);
							}

							mAdapter = new StudyAdapter(getApplicationContext(), taskItemList);
							RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
							mRecyclerView.setLayoutManager(mLayoutManager);
							mRecyclerView.setItemAnimator(new DefaultItemAnimator());
							mRecyclerView.setAdapter(mAdapter);
						} else {
							Log.d(TAG, "Error getting documents: ", task.getException());
						}
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Checks if the activity was from the New task activity
		if (requestCode == ACTION_NEW_TASK) {
			if (resultCode == RESULT_OK) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Activity result")
						.setMessage("taskTitle: " + data.getStringExtra("taskTitle") + "\ntaskProject: " + data.getStringExtra("taskProject") + "\ntaskContent: " + data.getStringExtra("taskContent"))
						.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		// Check if build is debug
		if (!BuildConfig.DEBUG) {
			menu.removeItem(R.id.action_debug_send_notification);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
			case R.id.action_settings:
				Intent prefsIntent = new Intent(this, SettingsActivity.class);
				startActivity(prefsIntent);
				break;
			case R.id.action_about:
				String aboutDialogText = getString(R.string.about_dialog_text);
				AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(context);
				aboutDialogBuilder.setTitle("About this app");
				aboutDialogBuilder.setMessage(aboutDialogText);
				aboutDialogBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
					}
				});
				aboutDialogBuilder.setNeutralButton("Visit Source Code", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						String githubUrl = "https://github.com/Chan4077/StudyBuddy";
						Intent githubIntent = new Intent();
						githubIntent.setAction(Intent.ACTION_VIEW);
						githubIntent.setData(Uri.parse(githubUrl));
						startActivity(githubIntent);
					}
				});
				AlertDialog aboutDialog = aboutDialogBuilder.create();
				aboutDialog.show();
				break;
			case R.id.action_share:
				share();
				break;
			case R.id.action_help:
				Intent helpIntent = new Intent(this, HelpActivity.class);
				startActivity(helpIntent);
				break;
			case R.id.action_debug_send_notification:
				Random rand = new Random();

				int randomInt = rand.nextInt(4) + 1;
				sendNotification(randomInt);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Log.e(TAG, "Connection failed:" + connectionResult);
	}
}
