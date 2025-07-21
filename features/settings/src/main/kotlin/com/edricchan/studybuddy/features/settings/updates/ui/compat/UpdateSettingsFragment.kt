package com.edricchan.studybuddy.features.settings.updates.ui.compat

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.edricchan.studybuddy.core.compat.navigation.navigateToUpdates
import com.edricchan.studybuddy.features.settings.updates.ui.UpdateSettingsScreen
import com.edricchan.studybuddy.features.settings.updates.vm.UpdateSettingsViewModel
import com.edricchan.studybuddy.ui.common.fragment.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateSettingsFragment : ComposableFragment() {

    private val viewModel by viewModels<UpdateSettingsViewModel>()

    @Composable
    override fun Content(modifier: Modifier) {
        UpdateSettingsScreen(
            modifier = modifier.windowInsetsPadding(WindowInsets.navigationBars),
            contentPadding = PaddingValues(16.dp),
            onUpdatesClick = navController::navigateToUpdates,
            viewModel = viewModel
        )
    }
}
