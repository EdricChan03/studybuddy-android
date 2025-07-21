package com.edricchan.studybuddy.features.settings.general.ui.compat

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.edricchan.studybuddy.features.settings.general.ui.GeneralSettingsScreen
import com.edricchan.studybuddy.features.settings.general.vm.GeneralSettingsViewModel
import com.edricchan.studybuddy.ui.common.fragment.ComposableFragment
import com.edricchan.studybuddy.ui.theming.DarkThemeOption
import com.edricchan.studybuddy.ui.theming.applyDarkTheme
import com.edricchan.studybuddy.ui.theming.applyDynamicTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeneralSettingsFragment : ComposableFragment() {
    private val viewModel by viewModels<GeneralSettingsViewModel>()

    @Composable
    override fun Content(
        modifier: Modifier
    ) {
        GeneralSettingsScreen(
            modifier = modifier.windowInsetsPadding(WindowInsets.navigationBars),
            contentPadding = PaddingValues(16.dp),
            viewModel = viewModel,
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
