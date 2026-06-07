package com.edricchan.studybuddy.ui.modules.debug

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.compose.AndroidFragment
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.core.compat.navigation.navigateToDebugModalBottomSheet
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.modules.debug.compose.DebugScreen
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.edricchan.studybuddy.ui.widgets.compose.BackIconButton
import com.edricchan.studybuddy.ui.widgets.compose.navigation.animation.horizontalSlideComposable
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@AndroidEntryPoint
class DebugActivity : BaseActivity() {
    @Inject
    lateinit var userFlow: Flow<@JvmSuppressWildcards FirebaseUser?>

    override val isEdgeToEdgeEnabled = true

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

            val navController = rememberNavController()

            val initialTitle = stringResource(R.string.title_activity_debug)
            var title by rememberSaveable { mutableStateOf(initialTitle) }

            StudyBuddyTheme {
                Scaffold(
                    topBar = {
                        LargeTopAppBar(
                            navigationIcon = {
                                BackIconButton(
                                    onClick = {
                                        if (!navController.navigateUp()) onBackPressedDispatcher.onBackPressed()
                                    }
                                )
                            },
                            title = {
                                Text(text = title)
                            },
                            scrollBehavior = scrollBehavior
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        modifier = Modifier
                            .padding(innerPadding)
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        navController = navController,
                        startDestination = CompatDestination.Debug
                    ) {
                        horizontalSlideComposable<CompatDestination.Debug> {
                            title = initialTitle
                            DebugScreen(
                                userFlow = userFlow,
                                onNavigateToDebugModalBottomSheet = navController::navigateToDebugModalBottomSheet
                            )
                        }
                        horizontalSlideComposable<CompatDestination.DebugModalBottomSheet> {
                            title = stringResource(R.string.title_activity_debug_modal_bottom_sheet)
                            AndroidFragment<DebugModalBottomSheetFragment>(
                                // Nested scrolling isn't enabled on FragmentContainerViews so this
                                // hacky workaround is needed; see
                                // https://issuetracker.google.com/issues/348598122
                                onUpdate = DebugModalBottomSheetFragment::tryEnableNestedScrolling
                            )
                        }
                    }
                }
            }
        }
    }
}

// Code from https://issuetracker.google.com/issues/376479680
private fun Fragment.tryEnableNestedScrolling() {
    var view = view
    while (view != null && view !is FragmentContainerView) {
        view = view.parent as? View
    }
    view?.isNestedScrollingEnabled = true
}
