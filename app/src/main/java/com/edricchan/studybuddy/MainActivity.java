package com.edricchan.studybuddy;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.edricchan.studybuddy.fragment.CalendarFragment;
import com.edricchan.studybuddy.fragment.ChatFragment;
import com.edricchan.studybuddy.fragment.TipsFragment;
import com.edricchan.studybuddy.fragment.TodoFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.provider.FontRequest;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
	/**
	 * Request code for new task activity
	 */
	static final int ACTION_NEW_TASK = 1;
	/**
	 * The constant for a new task shortcut
	 */
	private static final String ACTION_ADD_NEW_TODO = "com.edricchan.studybuddy.shortcuts.ADD_NEW_TODO";
	/**
	 * The Android tag for use with {@link android.util.Log}
	 */
	private static final String TAG = SharedHelper.getTag(MainActivity.class);
	private ArrayList taskItems;
	private int testInt, RC_SIGN_IN;
	private FirebaseAuth mAuth;
	private GoogleApiClient mGoogleApiClient;
	private String userName;
	private FirebaseUser currentUser;
	private Menu mOptionsMenu;
	private BottomNavigationView navigationView;
	private FrameLayout contentMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Use a downloadable font for EmojiCompat
		FontRequest fontRequest = new FontRequest(
				"com.google.android.gms.fonts",
				"com.google.android.gms",
				"Noto Color Emoji Compat",
				R.array.com_google_android_gms_fonts_certs);
		EmojiCompat.Config config = new FontRequestEmojiCompatConfig(getApplicationContext(), fontRequest)
				.setReplaceAll(true)
				.registerInitCallback(new EmojiCompat.InitCallback() {
					@Override
					public void onInitialized() {
						Log.i(TAG, "EmojiCompat initialized");
					}

					@Override
					public void onFailed(@Nullable Throwable throwable) {
						Log.e(TAG, "EmojiCompat initialization failed", throwable);
					}
				});
		EmojiCompat.init(config);
		setContentView(R.layout.activity_main);
		// Checks if the add new shortcut was tapped
		if (ACTION_ADD_NEW_TODO.equals(getIntent().getAction())) {
			newTaskActivity();
		}
		/*
		// FAB
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(view -> newTaskActivity());

		// Handles swiping down to refresh logic
		final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.recycler_swiperefresh);
		swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
		// Sets a refreshing listener
		swipeRefreshLayout.setOnRefreshListener(() -> {
			mAdapter.notifyDataSetChanged();
			loadTasksList(currentUser.getUid(), swipeRefreshLayout);
		});
		mRecyclerView = findViewById(R.id.recycler_list);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(false);

		// use a linear layout manager
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);
        */
		// Check if Android Oreo
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			SharedHelper.createNotificationChannels(MainActivity.this);
		}
		// Initially set a fragment view
		SharedHelper.replaceFragment(MainActivity.this, new TodoFragment(), R.id.content_main, false);
		contentMain = findViewById(R.id.content_main);
		navigationView = findViewById(R.id.bottom_navigation_view);
		navigationView.setOnNavigationItemSelectedListener((@NonNull MenuItem item) -> {
			switch (item.getItemId()) {
				case R.id.navigation_calendar:
					SharedHelper.replaceFragment(MainActivity.this, new CalendarFragment(), R.id.content_main, true);
					break;
				case R.id.navigation_chat:
					SharedHelper.replaceFragment(MainActivity.this, new ChatFragment(), R.id.content_main, true);
					break;
				case R.id.navigation_todos:
					SharedHelper.replaceFragment(MainActivity.this, new TodoFragment(), R.id.content_main, true);
					break;
				case R.id.navigation_tips:
					SharedHelper.replaceFragment(MainActivity.this, new TipsFragment(), R.id.content_main, true);
					break;
			}
			// Return a boolean to indicate that the listener has been set
			return true;
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
		FirebaseFirestore fs = FirebaseFirestore.getInstance();
		if (currentUser != null) {
			// User specific topic
			FirebaseMessaging.getInstance().subscribeToTopic("user_" + currentUser.getUid());
			FirebaseInstanceId.getInstance().getInstanceId()
					.addOnCompleteListener(task -> {
						if (task.isSuccessful()) {
							Log.d(TAG, "Successfully retrieved instance ID!");
							Map<String, Object> docData = new HashMap<>();
							docData.put("registrationToken", task.getResult().getToken());
							fs.document("users/" + currentUser.getUid())
									.set(docData)
									.addOnCompleteListener(updateTask -> {
										if (updateTask.isSuccessful()) {
											Log.d(TAG, "Successfully updated token!");
										} else {
											Log.e(TAG, "An error occurred while attempting to update the token:", updateTask.getException());
										}
									});
						} else {
							Log.e(TAG, "An error occurred while attempting to retrieve the instance ID:", task.getException());
						}
					});
		}
		// By default, subscribe to the "topic_all" topic
		FirebaseMessaging.getInstance().subscribeToTopic("all");
	}

	/**
	 * Shares the app
	 */
	public void share() {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT, R.string.share_content);
		shareIntent.setType("text/plain");
		startActivity(Intent.createChooser(shareIntent, getString(R.string.share_intent_value)));
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
						.setMessage("taskTitle: " + data.getStringExtra("taskTitle") + "\ntaskProject: " + data.getStringExtra("taskProject") + "\ntaskContent: " + data.getStringExtra("taskContent"))
						.setPositiveButton("Dismiss", (dialog, which) -> dialog.dismiss());
				builder.show();
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
//		if (currentUser != null) {
//			menu.getItem(R.id.action_account).setTitle("Log out");
//		} else {
//			menu.getItem(R.id.action_account).setTitle("Sign in");
//		}
		mOptionsMenu = menu;
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
				return true;
			case R.id.action_about:
				String aboutDialogText = String.format(getString(R.string.about_dialog_text), BuildConfig.VERSION_NAME);
				AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(this);
				aboutDialogBuilder.setTitle("About this app");
				aboutDialogBuilder.setMessage(aboutDialogText);
				aboutDialogBuilder.setPositiveButton(R.string.dialog_action_close, (dialogInterface, i) -> dialogInterface.dismiss());
				aboutDialogBuilder.setNeutralButton(R.string.dialog_action_view_source_code, (dialogInterface, i) -> {
					Intent githubIntent = new Intent();
					githubIntent.setAction(Intent.ACTION_VIEW);
					githubIntent.setData(Uri.parse("https://github.com/Chan4077/StudyBuddy"));
					startActivity(githubIntent);
				});
				aboutDialogBuilder.show();
				return true;
			case R.id.action_share:
				share();
				return true;
			case R.id.action_help:
				Intent helpIntent = new Intent(this, HelpActivity.class);
				startActivity(helpIntent);
				return true;
			case R.id.action_account:
				if (currentUser != null) {
					// Sign out
					mAuth.signOut();
				} else {
					// Show the Login activity
					Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
					startActivity(loginIntent);
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Log.e(TAG, "Connection failed:" + connectionResult);
	}
}
