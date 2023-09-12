package com.edricchan.studybuddy.ui.modules.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.MimeTypeConstants
import com.edricchan.studybuddy.core.deeplink.AppDeepLink
import com.edricchan.studybuddy.core.deeplink.WebDeepLink
import com.edricchan.studybuddy.databinding.ActivityMainBinding
import com.edricchan.studybuddy.extensions.replaceFragment
import com.edricchan.studybuddy.exts.android.startActivity
import com.edricchan.studybuddy.exts.android.startChooser
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.features.help.HelpActivity
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.modules.account.AccountActivity
import com.edricchan.studybuddy.ui.modules.calendar.fragment.CalendarFragment
import com.edricchan.studybuddy.ui.modules.chat.fragment.ChatFragment
import com.edricchan.studybuddy.ui.modules.debug.DebugActivity
import com.edricchan.studybuddy.ui.modules.main.fragment.showNavBottomSheet
import com.edricchan.studybuddy.ui.modules.settings.SettingsActivity
import com.edricchan.studybuddy.ui.modules.task.NewTaskActivity
import com.edricchan.studybuddy.ui.modules.task.fragment.TodoFragment
import com.edricchan.studybuddy.ui.modules.tips.fragment.TipsFragment
import com.edricchan.studybuddy.utils.NotificationUtils
import com.edricchan.studybuddy.utils.firebase.setCrashlyticsTracking
import com.edricchan.studybuddy.utils.web.launchUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

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
        val messaging = Firebase.messaging
        if (currentUser != null) {
            val crashlyticsTrackingEnabled = defaultSharedPreferences
                .getBoolean(Constants.prefEnableCrashlyticsUserTracking, false) &&
                !BuildConfig.DEBUG
            currentUser.setCrashlyticsTracking(
                enabled = crashlyticsTrackingEnabled
            )
            // User-specific topic
            messaging.subscribeToTopic("user_${currentUser.uid}")
        }
        // By default, subscribe to the "topic_all" topic
        messaging.subscribeToTopic("all")
    }

    private fun share() {
        startChooser(getString(R.string.share_intent_value)) {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content))
            type = MimeTypeConstants.textPlainMime
        }
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
            showNavBottomSheet {
                navigationViewListener = {
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

                auth.currentUser?.let {
                    isLoggedIn = true
                    displayName = it.displayName
                    email = it.email
                    photoUrl = it.photoUrl
                }

                navigationViewCheckedItemId =
                    when (supportFragmentManager.findFragmentById(R.id.frameLayoutMain)) {
                        is CalendarFragment -> R.id.navigation_calendar
                        is ChatFragment -> R.id.navigation_chat
                        is TodoFragment -> R.id.navigation_todos
                        is TipsFragment -> R.id.navigation_tips
                        else -> ResourcesCompat.ID_NULL
                    }
            }
            true
        }

        R.id.action_settings -> {
            startActivity<SettingsActivity>()
            true
        }

        R.id.action_about -> {
            showMaterialAlertDialog {
                // TODO: i18n
                setTitle("About this app")
                setMessage(getString(R.string.about_dialog_msg, BuildConfig.VERSION_NAME))
                setPositiveButton(R.string.dialog_action_close) { dialog, _ -> dialog.dismiss() }
                setNeutralButton(R.string.dialog_action_view_source_code) { _, _ ->
                    launchUri(Constants.uriSrcCode)
                }
            }
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
