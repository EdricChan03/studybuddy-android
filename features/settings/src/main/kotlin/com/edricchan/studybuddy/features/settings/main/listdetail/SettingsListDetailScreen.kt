package com.edricchan.studybuddy.features.settings.main.listdetail

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edricchan.studybuddy.core.settings.appearance.DarkThemeValue
import com.edricchan.studybuddy.features.settings.R
import com.edricchan.studybuddy.features.settings.general.ui.GeneralSettingsScreen
import com.edricchan.studybuddy.features.settings.main.list.SettingsListScreen
import com.edricchan.studybuddy.features.settings.navigation.SettingsCategory
import com.edricchan.studybuddy.features.settings.task.ui.TaskSettingsScreen
import com.edricchan.studybuddy.features.settings.updates.ui.UpdateSettingsScreen
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
val ThreePaneScaffoldNavigator<SettingsCategory>.settingsBackBehavior
    get() = if (isListExpanded() && isDetailExpanded()) {
        BackNavigationBehavior.PopUntilContentChange
    } else {
        BackNavigationBehavior.PopUntilScaffoldValueChange
    }

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SettingsListDetailScreen(
    modifier: Modifier = Modifier,
    navigator: ThreePaneScaffoldNavigator<SettingsCategory>,
    onNavigateToAbout: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onNavigateToUpdates: () -> Unit,
    onDynamicThemeChange: (Boolean) -> Unit = {},
    onDarkThemeChange: (DarkThemeValue) -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    fun onNavigateToDetail(cat: SettingsCategory) {
        scope.launch {
            navigator.navigateTo(
                ListDetailPaneScaffoldRole.Detail,
                cat
            )
        }
    }

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
        defaultBackBehavior = navigator.settingsBackBehavior,
        listPane = {
            AnimatedPane {
                SettingsListScreen(
                    contentPadding = PaddingValues(16.dp),
                    selectedItem = navigator.currentDestination?.contentKey,
                    onNavigateToDetail = ::onNavigateToDetail,
                    onNavigateToAccount = onNavigateToAccount,
                    onNavigateToAbout = onNavigateToAbout
                )
            }
        },
        detailPane = {
            AnimatedPane {
                SettingsDetailScreen(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    categoryData = navigator.currentDestination?.contentKey,
                    onNavigateToUpdates = onNavigateToUpdates,
                    onDynamicThemeChange = onDynamicThemeChange,
                    onDarkThemeChange = onDarkThemeChange
                )
            }
        }
    )
}

@Composable
fun SettingsDetailEmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
            ) {
                Icon(
                    modifier = Modifier.size(72.dp),
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.settings_detail_empty_state_header_text),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.settings_detail_empty_state_body_text),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsDetailEmptyStatePreview() {
    StudyBuddyTheme {
        SettingsDetailEmptyState()
    }
}

@Composable
private fun SettingsDetailScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    categoryData: SettingsCategory?,
    onNavigateToUpdates: () -> Unit,
    onDynamicThemeChange: (Boolean) -> Unit,
    onDarkThemeChange: (DarkThemeValue) -> Unit
) {
    Crossfade(categoryData) {
        when (it) {
            SettingsCategory.General -> GeneralSettingsScreen(
                modifier = modifier,
                contentPadding = contentPadding,
                viewModel = viewModel(),
                onDynamicThemeChange = onDynamicThemeChange,
                onDarkThemeChange = onDarkThemeChange
            )

            SettingsCategory.Task -> TaskSettingsScreen(
                modifier = modifier,
                contentPadding = contentPadding,
                viewModel = viewModel()
            )

            SettingsCategory.Updates -> UpdateSettingsScreen(
                modifier = modifier,
                contentPadding = contentPadding,
                viewModel = viewModel(),
                onUpdatesClick = onNavigateToUpdates
            )

            null -> SettingsDetailEmptyState(modifier = modifier)
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun ThreePaneScaffoldNavigator<*>.isListExpanded() =
    scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun ThreePaneScaffoldNavigator<*>.isDetailExpanded() =
    scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded
