package com.edricchan.studybuddy.features.settings.task.ui.compat

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.edricchan.studybuddy.features.settings.task.ui.TaskSettingsScreen
import com.edricchan.studybuddy.features.settings.task.vm.TaskSettingsViewModel
import com.edricchan.studybuddy.ui.common.fragment.ComposableFragment
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoSettingsFragment : ComposableFragment() {
    private val viewModel by viewModels<TaskSettingsViewModel>()

    @Composable
    override fun Content(modifier: Modifier) {
        StudyBuddyTheme {
            TaskSettingsScreen(
                modifier = modifier.windowInsetsPadding(WindowInsets.navigationBars),
                contentPadding = PaddingValues(16.dp),
                viewModel = viewModel
            )
        }
    }
}
