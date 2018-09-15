package com.edricchan.studybuddy;

import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
	final Context context = this;
	private ArrayList taskItems;
	private int testInt, RC_SIGN_IN;
	private FirebaseAuth mAuth;
	private GoogleApiClient mGoogleApiClient;
	private String userName;
	private FirebaseUser currentUser;
	private Menu mOptionsMenu;
	private BottomNavigationView navigationView;
	private FrameLayout contentMain;

	/**
	 * Replaces a view with an initialised fragment.
	 * Note: This method checks if there's already a fragment in the view.
	 *
	 * @param fragment The fragment to replace the view with. (Needs to be initialised with a `new` constructor)
	 * @param viewId   The ID of the view
	 * @return True if the fragment was replaced, false if there's already an existing fragment.
	 */
	private boolean replaceFragment(Fragment fragment, int viewId, String tag) {
		// Check if fragment already has been replaced
		if ((getSupportFragmentManager().findFragmentByTag(tag) != fragment) &&
				(getSupportFragmentManager().findFragmentById(viewId) != fragment)) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(
					viewId,
					fragment,
					tag
			);
			transaction.addToBackStack(null);
			transaction.commit();
			// Indicate that the fragment replacement has been done.
			return true;
		}
		// Return false if there's already an existing fragment.
		return false;
	}

	/**
	 * Replaces a view with an initialised fragment.
	 * Note: This method checks if there's already a fragment in the view.
	 *
	 * @param fragment The fragment to replace the view with. (Needs to be initialised with a `new` constructor)
	 * @param viewId   The ID of the view
	 * @return True if the fragment was replaced, false if there's already an existing fragment.
	 */
	private boolean replaceFragment(Fragment fragment, int viewId) {
		if (getSupportFragmentManager().findFragmentById(viewId) != fragment) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(
					viewId,
					fragment
			);
			transaction.addToBackStack(null);
			transaction.commit();
			// Indicate that the fragment replacement has been done.
			return true;
		}
		// Return false if there's already an existing fragment.
		return false;
	}

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
		replaceFragment(new TodoFragment(), R.id.content_main);
		contentMain = findViewById(R.id.content_main);
		navigationView = findViewById(R.id.bottom_navigation_view);
		navigationView.setOnNavigationItemSelectedListener((@NonNull MenuItem item) -> {
			switch (item.getItemId()) {
				case R.id.navigation_calendar:
					replaceFragment(new CalendarFragment(), R.id.content_main);
					break;
				case R.id.navigation_chat:
					replaceFragment(new ChatFragment(), R.id.content_main);
					break;
				case R.id.navigation_todos:
					replaceFragment(new TodoFragment(), R.id.content_main);
					break;
				case R.id.navigation_tips:
					replaceFragment(new TipsFragment(), R.id.content_main);
					break;
			}
			// Return a boolean to indicate that the listener has been set
			return true;
		});
	}

	/**
	 * Used for debugging.
	 * Will not be used on release.
	 */
	public void sendNotification(int type) {
		if (currentUser != null) {
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
		} else {
			Log.e(TAG, "Please login before executing this method.");
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
				break;
			case R.id.action_about:
				String aboutDialogText = String.format(getString(R.string.about_dialog_text), BuildConfig.VERSION_NAME);
				AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(context);
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
				break;
			case R.id.action_account:
				if (currentUser != null) {
					// Sign out
					mAuth.signOut();
				} else {
					// Show the Login activity
					Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
					startActivity(loginIntent);
				}
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Log.e(TAG, "Connection failed:" + connectionResult);
	}
}
