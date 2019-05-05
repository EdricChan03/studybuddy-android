package com.edricchan.studybuddy.ui.modules.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.provider.FontRequest
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.annotations.AppDeepLink
import com.edricchan.studybuddy.annotations.WebDeepLink
import com.edricchan.studybuddy.ui.modules.account.AccountActivity
import com.edricchan.studybuddy.ui.modules.calendar.fragment.CalendarFragment
import com.edricchan.studybuddy.ui.modules.chat.fragment.ChatFragment
import com.edricchan.studybuddy.ui.modules.debug.DebugActivity
import com.edricchan.studybuddy.ui.modules.help.HelpActivity
import com.edricchan.studybuddy.ui.modules.main.fragment.NavBottomSheetDialogFragment
import com.edricchan.studybuddy.ui.modules.settings.SettingsActivity
import com.edricchan.studybuddy.ui.modules.task.NewTaskActivity
import com.edricchan.studybuddy.ui.modules.task.fragment.TaskFragment
import com.edricchan.studybuddy.ui.modules.tips.fragment.TipsFragment
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*

@WebDeepLink(["/"])
@AppDeepLink(["/"])
class MainActivity : AppCompatActivity(R.layout.activity_main), GoogleApiClient.OnConnectionFailedListener {
	private var mOptionsMenu: Menu? = null
	//	private BottomNavigationView navigationView;
	private var contentMain: FrameLayout? = null
	private var bar: BottomAppBar? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		SharedUtils.setAppTheme(this)
		// Use a downloadable font for EmojiCompat
		val fontRequest = FontRequest(
				"com.google.android.gms.fonts",
				"com.google.android.gms",
				"Noto Color Emoji Compat",
				R.array.com_google_android_gms_fonts_certs)
		val config = FontRequestEmojiCompatConfig(applicationContext, fontRequest)
				.setReplaceAll(true)
				.registerInitCallback(object : EmojiCompat.InitCallback() {
					override fun onInitialized() {
						Log.i(TAG, "EmojiCompat initialized")
					}

					override fun onFailed(throwable: Throwable?) {
						Log.e(TAG, "EmojiCompat initialization failed", throwable)
					}
				})
		EmojiCompat.init(config)
		bar = findViewById(R.id.bottomAppBar)
		setSupportActionBar(bar)
		// Checks if the add new shortcut was tapped
		if (ACTION_ADD_NEW_TODO == intent.action) {
			newTaskActivity()
		}

		/*
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
			SharedUtils.createNotificationChannels(this@MainActivity)
		}
		// Initially set a fragment view
		SharedUtils.replaceFragment(this@MainActivity, TaskFragment(), R.id.content_main, false)
		contentMain = findViewById(R.id.content_main)
		/*navigationView = findViewById(R.id.bottom_navigation_view);
		navigationView.setOnNavigationItemSelectedListener((@NonNull MenuItem item) -> {
			switch (item.getItemId()) {
				case R.id.navigation_calendar:
					SharedUtils.replaceFragment(MainActivity.this, new CalendarFragment(), R.id.content_main, true);
					break;
				case R.id.navigation_chat:
					SharedUtils.replaceFragment(MainActivity.this, new ChatFragment(), R.id.content_main, true);
					break;
				case R.id.navigation_todos:
					SharedUtils.replaceFragment(MainActivity.this, new TaskFragment(), R.id.content_main, true);
					break;
				case R.id.navigation_tips:
					SharedUtils.replaceFragment(MainActivity.this, new TipsFragment(), R.id.content_main, true);
					break;
			}
			// Return a boolean to indicate that the listener has been set
			return true;
		});*/
	}

	override fun onStart() {
		super.onStart()
		val currentUser = FirebaseAuth.getInstance().currentUser
		val fs = FirebaseFirestore.getInstance()
		if (currentUser != null) {
			SharedUtils.setCrashlyticsUserTracking(this, currentUser)
			// User specific topic
			FirebaseMessaging.getInstance().subscribeToTopic("user_" + currentUser.uid)
			FirebaseInstanceId.getInstance().instanceId
					.addOnCompleteListener { task ->
						if (task.isSuccessful) {
							Log.d(TAG, "Successfully retrieved instance ID!")
							val docData = HashMap<String, Any>()
							docData["registrationToken"] = task.result!!.token
							fs.document("users/" + currentUser.uid)
									.set(docData)
									.addOnCompleteListener { updateTask ->
										if (updateTask.isSuccessful) {
											Log.d(TAG, "Successfully updated token!")
										} else {
											Log.e(TAG, "An error occurred while attempting to update the token:", updateTask.exception)
										}
									}
						} else {
							Log.e(TAG, "An error occurred while attempting to retrieve the instance ID:", task.exception)
						}
					}
		}
		// By default, subscribe to the "topic_all" topic
		FirebaseMessaging.getInstance().subscribeToTopic("all")
	}

	/**
	 * Shares the app
	 */
	private fun share() {
		val shareIntent = Intent()
		shareIntent.action = Intent.ACTION_SEND
		shareIntent.putExtra(Intent.EXTRA_TEXT, R.string.share_content)
		shareIntent.type = "text/plain"
		startActivity(Intent.createChooser(shareIntent, getString(R.string.share_intent_value)))
	}

	private fun newTaskActivity() {
		val newTaskIntent = Intent(this, NewTaskActivity::class.java)
		startActivityForResult(newTaskIntent, ACTION_NEW_TASK)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		// Checks if the activity was from the New task activity
		if (requestCode == ACTION_NEW_TASK) {
			if (resultCode == Activity.RESULT_OK) {
				val builder = MaterialAlertDialogBuilder(this)
				builder.setTitle("Activity result")
						.setMessage("taskTitle: " + data!!.getStringExtra("taskTitle") + "\ntaskProject: " + data.getStringExtra("taskProject") + "\ntaskContent: " + data.getStringExtra("taskContent"))
						.setPositiveButton("Dismiss") { dialog, _ -> dialog.dismiss() }
				builder.show()
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		// Inflate the menu; this adds items to the action bar if it is present.
		menuInflater.inflate(R.menu.menu_main, menu)
		// Check if build is debug
		if (!BuildConfig.DEBUG) {
			menu.removeItem(R.id.action_debug)
		}
		mOptionsMenu = menu
		return true
	}


	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		val id = item.itemId
		when (id) {
			android.R.id.home -> {
				val navBottomSheet = NavBottomSheetDialogFragment()
				navBottomSheet.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener {
					when (it.itemId) {
						R.id.navigation_calendar -> {
							SharedUtils.replaceFragment(this@MainActivity, CalendarFragment(), R.id.content_main, true)
							return@OnNavigationItemSelectedListener true
						}
						R.id.navigation_chat -> {
							SharedUtils.replaceFragment(this@MainActivity, ChatFragment(), R.id.content_main, true)
							return@OnNavigationItemSelectedListener true
						}
						R.id.navigation_todos -> {
							SharedUtils.replaceFragment(this@MainActivity, TaskFragment(), R.id.content_main, true)
							return@OnNavigationItemSelectedListener true
						}
						R.id.navigation_tips -> {
							SharedUtils.replaceFragment(this@MainActivity, TipsFragment(), R.id.content_main, true)
							return@OnNavigationItemSelectedListener true
						}
						else -> return@OnNavigationItemSelectedListener false
					}
				})
				navBottomSheet.show(supportFragmentManager, navBottomSheet.tag)
				return true
			}
			R.id.action_settings -> {
				val prefsIntent = Intent(this, SettingsActivity::class.java)
				startActivity(prefsIntent)
				return true
			}
			R.id.action_about -> {
				val aboutDialogText = String.format(getString(R.string.about_dialog_text), BuildConfig.VERSION_NAME)
				val aboutDialogBuilder = MaterialAlertDialogBuilder(this)
				aboutDialogBuilder.setTitle("About this app")
				aboutDialogBuilder.setMessage(aboutDialogText)
				aboutDialogBuilder.setPositiveButton(R.string.dialog_action_close) { dialogInterface, _ -> dialogInterface.dismiss() }
				aboutDialogBuilder.setNeutralButton(R.string.dialog_action_view_source_code) { _, _ ->
					val githubIntent = Intent()
					githubIntent.action = Intent.ACTION_VIEW
					githubIntent.data = Uri.parse("https://github.com/EdricChan03/StudyBuddy")
					startActivity(githubIntent)
				}
				aboutDialogBuilder.show()
				return true
			}
			R.id.action_share -> {
				share()
				return true
			}
			R.id.action_help -> {
				val helpIntent = Intent(this, HelpActivity::class.java)
				startActivity(helpIntent)
				return true
			}
			R.id.action_debug -> {
				val debugIntent = Intent(this, DebugActivity::class.java)
				startActivity(debugIntent)
				return true
			}
			R.id.action_account -> {
				val accountIntent = Intent(this, AccountActivity::class.java)
				startActivity(accountIntent)
				return true
			}
			else -> return super.onOptionsItemSelected(item)
		}
	}

	override fun onConnectionFailed(connectionResult: ConnectionResult) {
		Log.e(TAG, "Connection failed:$connectionResult")
	}

	companion object {
		/**
		 * Request code for new task activity
		 */
		internal const val ACTION_NEW_TASK = 1
		/**
		 * The constant for a new task shortcut
		 */
		private const val ACTION_ADD_NEW_TODO = "com.edricchan.studybuddy.shortcuts.ADD_NEW_TODO"
		/**
		 * The Android tag for use with [android.util.Log]
		 */
		private val TAG = SharedUtils.getTag(MainActivity::class.java)
	}
}
