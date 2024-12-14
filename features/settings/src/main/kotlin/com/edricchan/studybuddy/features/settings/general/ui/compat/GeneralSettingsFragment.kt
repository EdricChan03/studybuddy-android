package com.edricchan.studybuddy.features.settings.general.ui.compat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.fragment.app.viewModels
import androidx.fragment.compose.content
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edricchan.studybuddy.features.settings.general.ui.GeneralSettingsScreen
import com.edricchan.studybuddy.features.settings.general.vm.GeneralSettingsViewModel
import com.edricchan.studybuddy.ui.common.fragment.BaseFragment
import com.edricchan.studybuddy.ui.theming.DarkThemeOption
import com.edricchan.studybuddy.ui.theming.applyDarkTheme
import com.edricchan.studybuddy.ui.theming.applyDynamicTheme
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.edricchan.studybuddy.ui.theming.isDynamicColorAvailable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeneralSettingsFragment : BaseFragment() {
    private val viewModel by viewModels<GeneralSettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        val nestedScrollInterop = rememberNestedScrollInteropConnection()

        val dynamicTheme by viewModel.prefEnableDynamicTheme.asFlow()
            .collectAsStateWithLifecycle(initialValue = isDynamicColorAvailable)

        StudyBuddyTheme(
            useDynamicTheme = dynamicTheme
        ) {
            GeneralSettingsScreen(
                modifier = Modifier
                    .nestedScroll(nestedScrollInterop)
                    .windowInsetsPadding(WindowInsets.navigationBars),
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
}
