package com.edricchan.studybuddy.ui.modules.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.fragment.app.viewModels
import androidx.fragment.compose.content
import com.edricchan.studybuddy.core.compat.navigation.navigateToUpdates
import com.edricchan.studybuddy.features.settings.updates.ui.UpdateSettingsScreen
import com.edricchan.studybuddy.features.settings.updates.vm.UpdateSettingsViewModel
import com.edricchan.studybuddy.ui.common.fragment.BaseFragment
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateSettingsFragment : BaseFragment() {

    private val viewModel by viewModels<UpdateSettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        val nestedScrollInterop = rememberNestedScrollInteropConnection()

        StudyBuddyTheme {
            UpdateSettingsScreen(
                modifier = Modifier
                    .nestedScroll(nestedScrollInterop)
                    .windowInsetsPadding(WindowInsets.navigationBars),
                onUpdatesClick = navController::navigateToUpdates,
                viewModel = viewModel
            )
        }
    }
}
