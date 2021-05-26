package com.edricchan.studybuddy.ui.modules.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.provider.FontRequest
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.annotations.AppDeepLink
import com.edricchan.studybuddy.annotations.WebDeepLink
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.MimeTypeConstants
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.startActivity
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
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialFadeThrough
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*

@WebDeepLink(["/"])
@AppDeepLink(["/"])
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var contentMain: FrameLayout
    private lateinit var bar: BottomAppBar
    private lateinit var auth: FirebaseAuth
    private lateinit var uiUtils: UiUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        uiUtils = UiUtils.getInstance(this)
        uiUtils.setAppTheme()

        bar = findViewById(R.id.bottomAppBar)
        setSupportActionBar(bar)
        // Checks if the add new shortcut was tapped
        if (ACTION_ADD_NEW_TODO == intent.action) {
            startActivity<NewTaskActivity>()
        }

        // Create notification channels
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtils.createNotificationChannels(this@MainActivity)
        }
        // Initially set a fragment view
        // Note: As there isn't any fragment before this, we don't want this fragment
        // to be added to the back stack.
        setCurrentFragment(TodoFragment(), /* addToBackStack = */ false)
        contentMain = findViewById(R.id.content_main)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        val fs = Firebase.firestore
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
        }
        // By default, subscribe to the "topic_all" topic
        FirebaseMessaging.getInstance().subscribeToTopic("all")
    }

    private fun share() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content))
            type = MimeTypeConstants.textPlainMime
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_intent_value)))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        // Check if build is debug
        if (!BuildConfig.DEBUG) {
            menu.removeItem(R.id.action_debug)
        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            android.R.id.home -> {
                val navBottomSheet = NavBottomSheetDialogFragment()
                navBottomSheet.navigationViewListener = {
                    when (it.itemId) {
                        R.id.navigation_calendar -> {
                            setCurrentFragment(CalendarFragment())
                            true
                        }
                        R.id.navigation_chat -> {
                            setCurrentFragment(ChatFragment())
                            true
                        }
                        R.id.navigation_todos -> {
                            setCurrentFragment(TodoFragment())
                            true
                        }
                        R.id.navigation_tips -> {
                            setCurrentFragment(TipsFragment())
                            true
                        }
                        else -> false
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
                        else -> ResourcesCompat.ID_NULL
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
                aboutDialogBuilder.setPositiveButton(R.string.dialog_action_close) { dialog, _ -> dialog.dismiss() }
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

    private fun setCurrentFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.enterTransition = MaterialFadeThrough()
        fragment.exitTransition = MaterialFadeThrough()
        SharedUtils.replaceFragment(this, fragment, R.id.content_main, addToBackStack)
    }

    companion object {
        /**
         * The constant for a new task shortcut
         */
        private const val ACTION_ADD_NEW_TODO = "com.edricchan.studybuddy.shortcuts.ADD_NEW_TODO"
    }
}
