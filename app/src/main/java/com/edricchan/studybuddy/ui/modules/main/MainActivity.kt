package com.edricchan.studybuddy.ui.modules.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.MimeTypeConstants
import com.edricchan.studybuddy.core.deeplink.AppDeepLink
import com.edricchan.studybuddy.core.deeplink.WebDeepLink
import com.edricchan.studybuddy.databinding.ActivityMainBinding
import com.edricchan.studybuddy.exts.android.startChooser
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.exts.material.snackbar.showSnackbar
import com.edricchan.studybuddy.navigation.compat.CompatDestination
import com.edricchan.studybuddy.navigation.compat.auth.navigateToAccountInfo
import com.edricchan.studybuddy.navigation.compat.compatGraphs
import com.edricchan.studybuddy.navigation.compat.navigateToCalendar
import com.edricchan.studybuddy.navigation.compat.navigateToDebug
import com.edricchan.studybuddy.navigation.compat.navigateToHelp
import com.edricchan.studybuddy.navigation.compat.navigateToSettings
import com.edricchan.studybuddy.navigation.compat.navigateToTips
import com.edricchan.studybuddy.navigation.compat.task.navigateToTasksList
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.common.MainViewModel
import com.edricchan.studybuddy.ui.modules.main.fragment.showNavBottomSheet
import com.edricchan.studybuddy.utils.createNotificationChannelsCompat
import com.edricchan.studybuddy.utils.firebase.setCrashlyticsTracking
import com.edricchan.studybuddy.utils.web.launchUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@WebDeepLink(["/"])
@AppDeepLink(["/"])
@AndroidEntryPoint
class MainActivity : BaseActivity() {
    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var messaging: FirebaseMessaging

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private val viewModel by viewModels<MainViewModel>()

    override val isEdgeToEdgeEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = binding.navHostMain.getFragment<NavHostFragment>().navController
        navController.initNavGraph()

        setSupportActionBar(binding.bottomAppBar)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updatePadding(
                left = insets.left,
                right = insets.right
            )

            windowInsets
        }

        // Create notification channels
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannelsCompat()
        }

        // Subscribe to snack-bar data
        lifecycleScope.launch {
            viewModel.snackBarData
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    showSnackbar(
                        binding.coordinatorLayoutMain,
                        it.message,
                        it.duration.value
                    ) {
                        setAnchorView(binding.fab)
                    }
                }
        }
        viewModel.init(
            fab = binding.fab,
            bottomAppBar = binding.bottomAppBar
        )
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
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

    private val navViewIdsMap = mapOf(
        CompatDestination.Calendar::class to R.id.navigation_calendar,
        CompatDestination.Task.List::class to R.id.navigation_todos,
        CompatDestination.Tips::class to R.id.navigation_tips
    )

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            showNavBottomSheet {
                navigationViewListener = {
                    when (it.itemId) {
                        R.id.navigation_calendar -> {
                            navController.navigateToCalendar()
                            true
                        }

                        R.id.navigation_todos -> {
                            navController.navigateToTasksList()
                            true
                        }

                        R.id.navigation_tips -> {
                            navController.navigateToTips()
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

                navigationViewCheckedItemId = navViewIdsMap.entries
                    .find { navController.currentDestination?.hasRoute(it.key) == true }
                    ?.value
            }
            true
        }

        R.id.action_settings -> {
            navController.navigateToSettings()
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
            navController.navigateToHelp()
            true
        }

        R.id.action_debug -> {
            navController.navigateToDebug()
            true
        }

        R.id.action_account -> {
            navController.navigateToAccountInfo()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun NavController.initNavGraph() {
        graph = createGraph(CompatDestination.Task.Root) {
            compatGraphs()
        }
    }

    companion object {
        /**
         * The constant for a new task shortcut
         */
        const val ACTION_ADD_NEW_TODO = "com.edricchan.studybuddy.shortcuts.ADD_NEW_TODO"
    }
}
