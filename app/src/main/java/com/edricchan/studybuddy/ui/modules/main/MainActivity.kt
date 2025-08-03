package com.edricchan.studybuddy.ui.modules.main

import android.annotation.SuppressLint
import android.app.Notification
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
import androidx.navigation.serialization.generateHashCode
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.MimeTypeConstants
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.core.compat.navigation.about.navigateToAbout
import com.edricchan.studybuddy.core.compat.navigation.auth.navigateToAccountInfo
import com.edricchan.studybuddy.core.compat.navigation.navigateToCalendar
import com.edricchan.studybuddy.core.compat.navigation.navigateToDebug
import com.edricchan.studybuddy.core.compat.navigation.navigateToHelp
import com.edricchan.studybuddy.core.compat.navigation.navigateToSettings
import com.edricchan.studybuddy.core.compat.navigation.task.navigateToTasksList
import com.edricchan.studybuddy.core.deeplink.AppDeepLink
import com.edricchan.studybuddy.core.deeplink.WebDeepLink
import com.edricchan.studybuddy.core.settings.tracking.keyPrefEnableUserTracking
import com.edricchan.studybuddy.databinding.ActivityMainBinding
import com.edricchan.studybuddy.exts.android.startChooser
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.exts.material.snackbar.showSnackbar
import com.edricchan.studybuddy.navigation.compat.compatGraphs
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.common.MainViewModel
import com.edricchan.studybuddy.ui.common.fab.setupFabController
import com.edricchan.studybuddy.ui.modules.main.fragment.showNavBottomSheet
import com.edricchan.studybuddy.utils.createNotificationChannelsCompat
import com.edricchan.studybuddy.utils.firebase.setCrashlyticsTracking
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
    private lateinit var navHost: NavHostFragment

    private val viewModel by viewModels<MainViewModel>()

    override val isEdgeToEdgeEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.bottomAppBar.setNavigationOnClickListener { onShowNavBottomSheet() }

        navHost = binding.navHostMain.getFragment()
        navController = navHost.navController
        navController.initNavGraph()

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
                        anchorView = binding.bottomAppBar
                    }
                }
        }

        setupFabController(binding.fab, binding.bottomAppBar, navHost)

        // Handle system notification preference navigation
        // NOTE: getCategories can return null, so we use orEmpty to handle this
        // possibility
        if (intent.categories.orEmpty().any {
                it in listOf(
                    Notification.INTENT_CATEGORY_NOTIFICATION_PREFERENCES,
                    Intent.CATEGORY_PREFERENCE
                )
            }) {
            navController.navigateToSettings()
        }
    }

    override fun onStart() {
        super.onStart()
        auth.currentUser?.let { user ->
            val crashlyticsTrackingEnabled = defaultSharedPreferences
                .getBoolean(keyPrefEnableUserTracking, false) &&
                !BuildConfig.DEBUG
            user.setCrashlyticsTracking(
                enabled = crashlyticsTrackingEnabled
            )
            // User-specific topic
            messaging.subscribeToTopic("user_${user.uid}")
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

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val destination = navController.currentBackStackEntry
            ?.destination ?: return super.onPrepareOptionsMenu(menu)

        val routesMap = mapOf(
            CompatDestination.Settings::class to R.id.action_settings,
            CompatDestination.Auth.Account::class to R.id.action_account,
            CompatDestination.About.AppAbout::class to R.id.action_about,
            CompatDestination.Help::class to R.id.action_help,
        )
        routesMap.entries.forEach { (dest, itemId) ->
            if (destination.hasRoute(dest)) menu.removeItem(itemId)
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp() || super.onSupportNavigateUp()

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }

    private val navViewIdsMap = mapOf(
        CompatDestination.Calendar::class to R.id.navigation_calendar,
        CompatDestination.Task.List::class to R.id.navigation_todos
    )

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            navController.navigateToSettings()
            true
        }

        R.id.action_about -> {
            navController.navigateToAbout()
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

    private fun onShowNavBottomSheet() {
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
    }

    @SuppressLint("RestrictedApi") // For generateHashCode
    private fun NavController.initNavGraph() {
        graph = createGraph(
            if (auth.currentUser != null) CompatDestination.Task.Root
            else CompatDestination.Auth.Login
        ) {
            compatGraphs(context = this@MainActivity)
        }

        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = listOf(
                CompatDestination.Task.Root.serializer(),
                CompatDestination.Calendar.serializer()
            ).map { it.generateHashCode() }.toSet()
        )
        setupActionBarWithNavController(
            navController = this,
            configuration = appBarConfiguration
        )
    }
}
