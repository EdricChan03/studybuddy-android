package com.edricchan.studybuddy.ui.modules.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.annotations.AppDeepLink
import com.edricchan.studybuddy.annotations.WebDeepLink
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.MimeTypeConstants
import com.edricchan.studybuddy.databinding.ActivityMainBinding
import com.edricchan.studybuddy.extensions.replaceFragment
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.ui.common.BaseActivity
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
import com.edricchan.studybuddy.utils.WebUtils
import com.edricchan.studybuddy.utils.firebase.FirebaseCrashlyticsUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

@WebDeepLink(["/"])
@AppDeepLink(["/"])
class MainActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        setSupportActionBar(binding.bottomAppBar)

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
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
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
        menuInflater.inflate(R.menu.menu_main, menu)
        // Check if build is debug
        if (!BuildConfig.DEBUG) {
            menu.removeItem(R.id.action_debug)
        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
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
                when (supportFragmentManager.findFragmentById(R.id.frameLayoutMain)) {
                    is CalendarFragment -> R.id.navigation_calendar
                    is ChatFragment -> R.id.navigation_chat
                    is TodoFragment -> R.id.navigation_todos
                    is TipsFragment -> R.id.navigation_tips
                    else -> ResourcesCompat.ID_NULL
                }
            navBottomSheet.navigationViewCheckedItemId = selectedMenuItem
            true
        }

        R.id.action_settings -> {
            startActivity<SettingsActivity>()
            true
        }

        R.id.action_about -> {
            val aboutDialogText =
                String.format(getString(R.string.about_dialog_msg), BuildConfig.VERSION_NAME)
            MaterialAlertDialogBuilder(this).apply {
                setTitle("About this app")
                setMessage(aboutDialogText)
                setPositiveButton(R.string.dialog_action_close) { dialog, _ -> dialog.dismiss() }
                setNeutralButton(R.string.dialog_action_view_source_code) { _, _ ->
                    WebUtils.getInstance(this@MainActivity).launchUri(Constants.uriSrcCode)
                }
            }.show()
            true
        }

        R.id.action_share -> {
            share()
            true
        }

        R.id.action_help -> {
            startActivity<HelpActivity>()
            true
        }

        R.id.action_debug -> {
            startActivity<DebugActivity>()
            true
        }

        R.id.action_account -> {
            startActivity<AccountActivity>()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun setCurrentFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        replaceFragment(R.id.frameLayoutMain, fragment, addToBackStack)
    }

    companion object {
        /**
         * The constant for a new task shortcut
         */
        private const val ACTION_ADD_NEW_TODO = "com.edricchan.studybuddy.shortcuts.ADD_NEW_TODO"
    }
}
