package com.edricchan.studybuddy.ui.modules.settings.fragment

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.edricchan.studybuddy.core.auth.ProvideCurrentUser
import com.edricchan.studybuddy.core.compat.navigation.about.navigateToAbout
import com.edricchan.studybuddy.core.compat.navigation.auth.navigateToAccountInfo
import com.edricchan.studybuddy.core.compat.navigation.navigateToUpdates
import com.edricchan.studybuddy.features.settings.main.listdetail.SettingsListDetailScreen
import com.edricchan.studybuddy.features.settings.navigation.SettingsCategory
import com.edricchan.studybuddy.ui.common.fragment.ComposableFragment
import com.edricchan.studybuddy.ui.theming.DarkThemeOption
import com.edricchan.studybuddy.ui.theming.applyDarkTheme
import com.edricchan.studybuddy.ui.theming.applyDynamicTheme
import com.edricchan.studybuddy.utils.androidx.core.menuProvider
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@AndroidEntryPoint
class SettingsFragment : ComposableFragment() {
    @Inject
    lateinit var userFlow: Flow<@JvmSuppressWildcards FirebaseUser?>

    // Hacky code to hook up the Compose-based ListDetailPaneScaffold with the
    // view-based action-bar's up button
    // TODO: Revisit when the entire fragment is rewritten to 100% Compose

    private lateinit var navigator: ThreePaneScaffoldNavigator<SettingsCategory>

    // Composable animations require a clock, so we need to use `rememberCoroutineScope` -
    // see https://stackoverflow.com/q/70506054
    private lateinit var composeScope: CoroutineScope

    override val menuProvider = menuProvider(
        onCreateMenu = { _, _ -> }
    ) {
        if (it.itemId != android.R.id.home) return@menuProvider false

        if (!navigator.canNavigateBack()) return@menuProvider false

        composeScope.launch {
            navigator.navigateBack()
        }
        true
    }

    @Composable
    override fun Content(
        modifier: Modifier
    ) {
        navigator = rememberListDetailPaneScaffoldNavigator<SettingsCategory>()
        composeScope = rememberCoroutineScope()

        ProvideCurrentUser(
            userFlow = userFlow
        ) {
            SettingsListDetailScreen(
                modifier = modifier,
                navigator = navigator,
                onNavigateToUpdates = navController::navigateToUpdates,
                onNavigateToAccount = navController::navigateToAccountInfo,
                onNavigateToAbout = navController::navigateToAbout,
                onDynamicThemeChange = {
                    requireActivity().apply {
                        applyDynamicTheme(it)
                        recreate()
                    }
                },
                onDarkThemeChange = {
                    requireActivity().applyDarkTheme(DarkThemeOption.fromValue(it))
                }
            )
        }
    }
}
