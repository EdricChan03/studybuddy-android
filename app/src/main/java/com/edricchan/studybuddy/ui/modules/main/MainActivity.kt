package com.edricchan.studybuddy.ui.modules.main

import android.app.Activity
import android.content.Intent
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
import androidx.preference.PreferenceManager
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.annotations.AppDeepLink
import com.edricchan.studybuddy.annotations.WebDeepLink
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.MimeTypeConstants
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.extensions.startActivityForResult
import com.edricchan.studybuddy.ui.modules.account.AccountActivity
import com.edricchan.studybuddy.ui.modules.calendar.fragment.CalendarFragment
import com.edricchan.studybuddy.ui.modules.chat.fragment.ChatFragment
import com.edricchan.studybuddy.ui.modules.debug.DebugActivity
import com.edricchan.studybuddy.ui.modules.help.HelpActivity
import com.edricchan.studybuddy.ui.modules.main.fragment.NavBottomSheetDialogFragment
import com.edricchan.studybuddy.ui.modules.settings.SettingsActivity
import com.edricchan.studybuddy.ui.modules.task.NewTaskActivity
import com.edricchan.studybuddy.ui.modules.task.fragment.TodoFragment
import com.edricchan.studybuddy.ui.modules.tips.fragment.TipsFragment
import com.edricchan.studybuddy.utils.NotificationUtils
import com.edricchan.studybuddy.utils.SharedUtils
import com.edricchan.studybuddy.utils.UiUtils
import com.edricchan.studybuddy.utils.WebUtils
import com.edricchan.studybuddy.utils.firebase.FirebaseCrashlyticsUtils
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
class MainActivity : AppCompatActivity(R.layout.activity_main),
    GoogleApiClient.OnConnectionFailedListener {
    private var mOptionsMenu: Menu? = null
    //	private BottomNavigationView navigationView;
    private var contentMain: FrameLayout? = null
    private var bar: BottomAppBar? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var uiUtils: UiUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        uiUtils = UiUtils.getInstance(this)
        uiUtils.setAppTheme()
        // Use a downloadable font for EmojiCompat
        val fontRequest = FontRequest(
            "com.google.android.gms.fonts",
            "com.google.android.gms",
            "Noto Color Emoji Compat",
            R.array.com_google_android_gms_fonts_certs
        )
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
            NotificationUtils.createNotificationChannels(this@MainActivity)
        }
        // Initially set a fragment view
        SharedUtils.replaceFragment(this@MainActivity, TodoFragment(), R.id.content_main, false)
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
                    SharedUtils.replaceFragment(MainActivity.this, new TodoFragment(), R.id.content_main, true);
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
        val currentUser = auth.currentUser
        val fs = FirebaseFirestore.getInstance()
        if (currentUser != null) {
            val crashlyticsTrackingEnabled = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(Constants.prefEnableCrashlyticsUserTracking, false) &&
                    !BuildConfig.DEBUG
            FirebaseCrashlyticsUtils.setCrashlyticsUserTracking(
                currentUser,
                crashlyticsTrackingEnabled
            )
            // User specific topic
            FirebaseMessaging.getInstance().subscribeToTopic("user_${currentUser.uid}")
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
                                    Log.e(
                                        TAG,
                                        "An error occurred while attempting to update the token:",
                                        updateTask.exception
                                    )
                                }
                            }
                    } else {
                        Log.e(
                            TAG,
                            "An error occurred while attempting to retrieve the instance ID:",
                            task.exception
                        )
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
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content))
            type = MimeTypeConstants.textPlainMime
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_intent_value)))
    }

    private fun newTaskActivity() {
        startActivityForResult<NewTaskActivity>(ACTION_NEW_TASK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Checks if the activity was from the New task activity
        if (requestCode == ACTION_NEW_TASK) {
            if (resultCode == Activity.RESULT_OK) {
                val builder = MaterialAlertDialogBuilder(this)
                builder.setTitle("Activity result")
                    .setMessage(
                        "taskTitle: " + data!!.getStringExtra("taskTitle") + "\ntaskProject: " + data.getStringExtra(
                            "taskProject"
                        ) + "\ntaskContent: " + data.getStringExtra("taskContent")
                    )
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
        when (item.itemId) {
            android.R.id.home -> {
                val navBottomSheet = NavBottomSheetDialogFragment()
                navBottomSheet.navigationViewListener =
                    NavigationView.OnNavigationItemSelectedListener {
                        when (it.itemId) {
                            R.id.navigation_calendar -> {
                                SharedUtils.replaceFragment(
                                    this@MainActivity,
                                    CalendarFragment(),
                                    R.id.content_main,
                                    true
                                )
                                return@OnNavigationItemSelectedListener true
                            }
                            R.id.navigation_chat -> {
                                SharedUtils.replaceFragment(
                                    this@MainActivity,
                                    ChatFragment(),
                                    R.id.content_main,
                                    true
                                )
                                return@OnNavigationItemSelectedListener true
                            }
                            R.id.navigation_todos -> {
                                SharedUtils.replaceFragment(
                                    this@MainActivity,
                                    TodoFragment(),
                                    R.id.content_main,
                                    true
                                )
                                return@OnNavigationItemSelectedListener true
                            }
                            R.id.navigation_tips -> {
                                SharedUtils.replaceFragment(
                                    this@MainActivity,
                                    TipsFragment(),
                                    R.id.content_main,
                                    true
                                )
                                return@OnNavigationItemSelectedListener true
                            }
                            else -> return@OnNavigationItemSelectedListener false
                        }
                    }
                if (auth.currentUser != null) {
                    navBottomSheet.isLoggedIn = true
                    if (auth.currentUser?.displayName != null) navBottomSheet.displayName =
                        auth.currentUser?.displayName
                    if (auth.currentUser?.email != null) navBottomSheet.email =
                        auth.currentUser?.email
                    if (auth.currentUser?.photoUrl != null) navBottomSheet.photoUrl =
                        auth.currentUser?.photoUrl
                }
                navBottomSheet.show(supportFragmentManager, navBottomSheet.tag)
                val selectedMenuItem =
                    when (supportFragmentManager.findFragmentById(R.id.content_main)) {
                        is CalendarFragment -> R.id.navigation_calendar
                        is ChatFragment -> R.id.navigation_chat
                        is TodoFragment -> R.id.navigation_todos
                        is TipsFragment -> R.id.navigation_tips
                        else -> 0 // Resources.ID_NULL
                    }
                navBottomSheet.navigationViewCheckedItemId = selectedMenuItem
                return true
            }
            R.id.action_settings -> {
                startActivity<SettingsActivity>()
                return true
            }
            R.id.action_about -> {
                val aboutDialogText =
                    String.format(getString(R.string.about_dialog_msg), BuildConfig.VERSION_NAME)
                val aboutDialogBuilder = MaterialAlertDialogBuilder(this)
                aboutDialogBuilder.setTitle("About this app")
                aboutDialogBuilder.setMessage(aboutDialogText)
                aboutDialogBuilder.setPositiveButton(R.string.dialog_action_close) { dialogInterface, _ -> dialogInterface.dismiss() }
                aboutDialogBuilder.setNeutralButton(R.string.dialog_action_view_source_code) { _, _ ->
                    WebUtils.getInstance(this@MainActivity).launchUri(Constants.uriSrcCode)
                }
                aboutDialogBuilder.show()
                return true
            }
            R.id.action_share -> {
                share()
                return true
            }
            R.id.action_help -> {
                startActivity<HelpActivity>()
                return true
            }
            R.id.action_debug -> {
                startActivity<DebugActivity>()
                return true
            }
            R.id.action_account -> {
                startActivity<AccountActivity>()
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
        private const val ACTION_NEW_TASK = 1
        /**
         * The constant for a new task shortcut
         */
        private const val ACTION_ADD_NEW_TODO = "com.edricchan.studybuddy.shortcuts.ADD_NEW_TODO"
    }
}
