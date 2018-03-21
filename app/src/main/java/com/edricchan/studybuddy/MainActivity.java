package com.edricchan.studybuddy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
	/**
	 * Request code for new task activity
	 */
	static final int ACTION_NEW_TASK = 1;
	private static final String ACTION_ADD_NEW_TODO = "com.edricchan.studybuddy.shortcuts.ADD_NEW_TODO";
	final Context context = this;
	private final ArrayList taskItems = new ArrayList<>();
	private int testInt, RC_SIGN_IN;
	private FirebaseAuth mAuth;
	private GoogleApiClient mGoogleApiClient;
	private String userName;
	private FirebaseFirestore db = FirebaseFirestore.getInstance();
	private FirebaseUser currentUser;
	private RecyclerView.Adapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Checks if the add new shortcut was tapped
		if (ACTION_ADD_NEW_TODO.equals(getIntent().getAction())) {
			newTaskActivity();
		}
		//  Declare a new thread to do a preference check
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				//  Initialize SharedPreferences
				SharedPreferences getPrefs = PreferenceManager
						.getDefaultSharedPreferences(getBaseContext());

				//  Create a new boolean and preference and set it to true
				boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

				//  If the activity has never started before...
				if (isFirstStart) {

					//  Launch app intro
					final Intent i = new Intent(MainActivity.this, MyIntroActivity.class);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							startActivity(i);
						}
					});

					//  Make a new preferences editor
					SharedPreferences.Editor e = getPrefs.edit();

					//  Edit preference to make it false because we don't want this to run again
					e.putBoolean("firstStart", false);

					//  Apply changes
					e.apply();
				}
			}
		});

		// Start the thread
		t.start();
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
		RecyclerView mRecyclerView = findViewById(R.id.recycler_list);

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
			setupNotificationChannels();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		mAuth = FirebaseAuth.getInstance();
		currentUser = mAuth.getCurrentUser();
		if (currentUser == null) {
			out.println("Not logged in");
			AlertDialog signInDialog = new AlertDialog.Builder(context)
					.setTitle("Sign in")
					.setMessage("To access the content, please sign in or register an account. Click the \"Sign in\" button to continue. Otherwise, click Cancel.")
					.setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
							startActivity(loginIntent);
							dialogInterface.dismiss();
						}
					})
					.setNeutralButton("Sign up", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
							startActivity(registerIntent);
							dialogInterface.dismiss();
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							dialogInterface.cancel();
						}
					})
					.create();
			signInDialog.show();

		} else {
			getTodos();
		}
	}

	/**
	 * Gets data from Firestore
	 */
	public void getTodos() {
		db.collection("users/" + currentUser.getUid() + "/todos")
				.get()
				.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
					@Override
					public void onSuccess(QuerySnapshot documentSnapshots) {
						if (documentSnapshots.isEmpty()) {
							System.out.println("getTodos: onSuccess: List is empty");
						} else {
							// Convert the whole Query Snapshot to a list
							// of objects directly! No need to fetch each
							// document.
//							ArrayList<TaskItem> types = new ArrayList<TaskItem>(documentSnapshots.toObjects(TaskItem.class));
//
//							// Add all to your list
//							taskItems = types;
//							Log.d("Tag", "onSuccess: " + documentSnapshots.toObjects(TaskItem.class));
						}
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Toast.makeText(getApplicationContext(), "Error getting data!", Toast.LENGTH_LONG).show();
						Log.w("Error", e);
					}
				});
	}

	/**
	 * Used for setting up notification channels
	 * NOTE: This will only work if the device is Android Oreo or later
	 */
	public void setupNotificationChannels() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationManager notificationManager =
					(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// Create a new list
			List<NotificationChannel> channels = new ArrayList<NotificationChannel>();

			// Task updates notifications
			NotificationChannel todoUpdatesChannel = new NotificationChannel("todo_updates", getString(R.string.notification_channel_todo_updates_title), NotificationManager.IMPORTANCE_HIGH);
			todoUpdatesChannel.setDescription(getString(R.string.notification_channel_todo_updates_desc));
			todoUpdatesChannel.enableLights(true);
			todoUpdatesChannel.setLightColor(Color.YELLOW);
			todoUpdatesChannel.enableVibration(true);
			todoUpdatesChannel.setShowBadge(true);
			channels.add(todoUpdatesChannel);

			// Weekly summary notifications
			NotificationChannel weeklySummaryChannel = new NotificationChannel("weekly_summary", getString(R.string.notification_channel_weekly_summary_title), NotificationManager.IMPORTANCE_LOW);
			weeklySummaryChannel.setDescription(getString(R.string.notification_channel_weekly_summary_desc));
			weeklySummaryChannel.setShowBadge(true);
			channels.add(weeklySummaryChannel);

			// Syncing notifications
			NotificationChannel syncChannel = new NotificationChannel("sync", getString(R.string.notification_channel_sync_title), NotificationManager.IMPORTANCE_LOW);
			syncChannel.setDescription(getString(R.string.notification_channel_sync_desc));
			syncChannel.setShowBadge(false);
			channels.add(syncChannel);

			// App notifications
			NotificationChannel appUpdatesChannel = new NotificationChannel("app_updates", getString(R.string.notification_channel_app_updates_title), NotificationManager.IMPORTANCE_LOW);
			appUpdatesChannel.setDescription(getString(R.string.notification_channel_app_updates_desc));
			appUpdatesChannel.setShowBadge(false);
			channels.add(appUpdatesChannel);

			// Media playback notifications
			NotificationChannel playbackChannel = new NotificationChannel("playback", getString(R.string.notification_channel_playback_title), NotificationManager.IMPORTANCE_LOW);
			playbackChannel.setDescription(getString(R.string.notification_channel_playback_desc));
			playbackChannel.setShowBadge(true);
			channels.add(playbackChannel);

			// Uncategorized notifications
			NotificationChannel uncategorisedChannel = new NotificationChannel("uncategorised", getString(R.string.notification_channel_uncategorised_title), NotificationManager.IMPORTANCE_DEFAULT);
			uncategorisedChannel.setDescription(getString(R.string.notification_channel_uncategorised_desc));
			uncategorisedChannel.setShowBadge(true);
			channels.add(uncategorisedChannel);
			// Pass list to method
			notificationManager.createNotificationChannels(channels);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Checks if the activity was from the New task activity
		if (requestCode == ACTION_NEW_TASK) {
			if (resultCode == RESULT_OK) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Activity result")
						.setMessage(data.getData().toString())
						.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
				builder.create();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
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
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Log.d("Tag", "onConnectionFailed:" + connectionResult);
	}
}
